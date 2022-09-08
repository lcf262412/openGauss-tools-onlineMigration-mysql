import com.alibaba.fastjson.JSONObject;
import org.opengauss.jdbc.PgConnection;
import org.opengauss.replication.LogSequenceNumber;
import org.opengauss.replication.PGReplicationStream;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;



import static java.lang.System.out;

public class ReverseMigration {
    static String config = "config.yaml";
    static PgConnection conn = openGaussConnection.main();
    static PgConnection jdbc_conn = openGaussConnection.main();
    static YamlTool yamls = new YamlTool(config);
    static LinkedHashMap re_og_conn = yamls.getValue("og_conn");

    static LinkedHashMap re_slot = yamls.getValue("slot");
    static String LSN_VALUE = (String) re_slot.get("waitLSN");
    static String LSN_NAME = (String) re_slot.get("name");
    static boolean LSN_INCLUDE_XIDS = (boolean) re_slot.get("include-xids");
    static boolean LSN_SKIP_EMPTY_XACTS = (boolean) re_slot.get("skip-empty-xacts");
    static int LSN_PARALLEL_DECODE_NUM = (int) re_slot.get("parallel-decode-num");
    static String LSN_WHITE_TABLE_LIST = (String) re_slot.get("white-table-list");
    static boolean LSN_STANDBY_CONNECTION = (boolean) re_slot.get("standby-connection");
    static String LSN_DECODE_STYLE = (String) re_slot.get("decode-style");
    static String LSN_DECODING = (String) re_slot.get("decoding");
    static int LSN_RUNNABLE_NUM = (int) re_slot.get("runnable_num");

    static String DATA_NAME = "table_name";
    static String DRIVER = (String) re_og_conn.get("driver");
    private static final Map<String, DataRunnable> TABLE_POOL = new ConcurrentHashMap<>();


    static int SLOT_NUM = 0;

    static Map runnable_map = new HashMap<>();;

    public static void main(String[] args) {
        try {
            Class.forName(DRIVER);
            if(args.length == 0 || args[0].equals("start")){
                startSlot();
            }else if(args[0].equals("drop")){
                dropSlot();
            }else if(args[0].equals("create")){
                createSlot();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }


    public static void startSlot() throws SQLException, InterruptedException {
        if(LSN_VALUE == ""){
            LSN_VALUE = getSQLResult("select pg_current_xlog_location();").get(0);
        }
        LogSequenceNumber waitLSN = LogSequenceNumber.valueOf(LSN_VALUE);
        PGReplicationStream stream = conn
                .getReplicationAPI()
                .replicationStream()
                .logical()
                .withSlotName(LSN_NAME)
                .withSlotOption("include-xids", LSN_INCLUDE_XIDS)
                .withSlotOption("skip-empty-xacts", LSN_SKIP_EMPTY_XACTS)
                .withStartPosition(waitLSN)
                .withSlotOption("parallel-decode-num",LSN_PARALLEL_DECODE_NUM)
                .withSlotOption("white-table-list", LSN_WHITE_TABLE_LIST)
                .withSlotOption("standby-connection", LSN_STANDBY_CONNECTION)
                .withSlotOption("decode-style", LSN_DECODE_STYLE)
                .start();

        TABLE_POOL.clear();
        for(int i = 0; i<LSN_RUNNABLE_NUM; i++){
            DataRunnable runnable_i = new DataRunnable();
            TABLE_POOL.put("RUNNABLE_"+i, runnable_i);
        }

        while (true) {
            ByteBuffer byteBuffer = stream.readPending();

            if (byteBuffer == null) {
                TimeUnit.MILLISECONDS.sleep(1L);
                continue;
            }

            int offset = byteBuffer.arrayOffset();
            byte[] source = byteBuffer.array();
            int length = source.length - offset;
            String res = new String(source, offset, length);

            if (res.contains(DATA_NAME)) {
                JSONObject json_test = JSONObject.parseObject(res);
                OpenGaussLogicData json_test_Obj = JSONObject.toJavaObject(json_test, OpenGaussLogicData.class);


                String runnable_name = String.valueOf(runnable_map.get(json_test_Obj.tableName));
                if(!runnable_name.equals("null")){
                    DataRunnable runnable = TABLE_POOL.get(runnable_name);
                    runnable.addData(json_test_Obj);
                }else {
                    String[] tableName_arr = json_test_Obj.tableName.split("\\.");
                    List create_sql_list = getSQLResult("SELECT\n" +
                            "   ccu.table_name AS foreign_table_name\n" +
                            " FROM\n" +
                            "   information_schema.table_constraints AS tc \n" +
                            "   JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name\n" +
                            " WHERE constraint_type = 'FOREIGN KEY' AND tc.table_name = '"+tableName_arr[1]+"';");

                    String name = getRunnable();
                    if(!create_sql_list.isEmpty()){
                        for (int i =0; i<create_sql_list.size();i++){
                            String runnable_name_forigen = tableName_arr[0]+"."+create_sql_list.get(i);

                            runnable_map.put(runnable_name_forigen,name);
                        }
                    }
                    runnable_map.put(json_test_Obj.tableName,name);
                    DataRunnable runnable = TABLE_POOL.get(name);
                    runnable.addData(json_test_Obj);
                    if(String.valueOf(runnable.getState()).equals("NEW")){
                        runnable.start();
                    }
                }

            }

        }
    }


    public  static String getRunnable(){
        String name = "";
        if(SLOT_NUM < LSN_RUNNABLE_NUM){
            name = "RUNNABLE_"+SLOT_NUM;
            SLOT_NUM++;
        }else {
            SLOT_NUM = 0;
            name = "RUNNABLE_"+SLOT_NUM;
        }
        return  name;
    }
    public static ArrayList<String> getSQLResult(String sql){
        PreparedStatement pstmt = null;
        List<String> create_sql = new ArrayList<String>();
        try {
            pstmt = jdbc_conn
                    .prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                create_sql.add(rs.getString(1));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (ArrayList<String>) create_sql;
    }


    public static void createSlot() throws SQLException {
        conn.getReplicationAPI()
                .createReplicationSlot()
                .logical()
                .withSlotName(LSN_NAME)
                .withOutputPlugin(LSN_DECODING)
                .make();
    }

    public static void dropSlot() throws SQLException {
        conn.getReplicationAPI()
                .dropReplicationSlot(LSN_NAME);
    }

}
