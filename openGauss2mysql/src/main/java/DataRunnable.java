import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DataRunnable extends Thread {
    private final BlockingQueue<OpenGaussLogicData> queue = new LinkedBlockingQueue(Integer.MAX_VALUE);

    @Override
    public void run() {
        Statement stmt = mysqlconnection.main();
        OpenGaussLogicData logicData = null;
        while(true){
            try {
                logicData = queue.take();
                stmt.execute(insertMysql(logicData));
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void addData(OpenGaussLogicData sql) {
        queue.add(sql);
    }

    public static String insertMysql(OpenGaussLogicData sqlObj) {

        String sql = "";
        if (sqlObj.opType.equals("INSERT")) {
            sql = "INSERT INTO " + sqlObj.tableName + "(" + StringUtils.join(sqlObj.columnsName, ',') + ") VALUES(" + StringUtils.join(sqlObj.columnsVal, ',') + ");";
        }
        if (sqlObj.opType.equals("UPDATE")) {
            sql = "UPDATE " + sqlObj.tableName + " set " + getKeyValue(sqlObj.columnsName,sqlObj.columnsVal) + " WHERE (" + StringUtils.join(sqlObj.oldKeysName,',') + ") = (" + StringUtils.join(sqlObj.oldKeysVal,',') + ");";
        }

        if (sqlObj.opType.equals("DELETE")) {
            sql = "DELETE FROM " + sqlObj.tableName + " WHERE (" + StringUtils.join(sqlObj.oldKeysName,',') + ") = (" + StringUtils.join(sqlObj.oldKeysVal, ',') + ");";
        }
        return sql;
    }

    public static String getKeyValue(List keys, List values){
        String sql = "";
        for(int i = 0;i<keys.size();i++){
            sql = sql+keys.get(i)+"="+values.get(i)+",";
        }
        sql = sql.substring(0,sql.length()-1);
        return sql;
    }

}


