import org.opengauss.PGProperty;
import org.opengauss.jdbc.PgConnection;

import java.sql.DriverManager;
import java.util.LinkedHashMap;
import java.util.Properties;


public class openGaussConnection {
    static String config = "config.yaml";
    static YamlTool yamls = new YamlTool(config);
    static LinkedHashMap re_og_conn = yamls.getValue("og_conn");
    static String HOST = (String) re_og_conn.get("host");
    static String PORT = (String) re_og_conn.get("port");
    static String USER = (String) re_og_conn.get("user");
    static String PASSWORD = (String) re_og_conn.get("password");
    static String DATABASE = (String) re_og_conn.get("database");
    static String ASSUME_MIN_SERVER_VERSION = (String) re_og_conn.get("ASSUME_MIN_SERVER_VERSION");
    static String REPLICATION = (String) re_og_conn.get("REPLICATION");
    static String PREFER_QUERY_MODE = (String) re_og_conn.get("PREFER_QUERY_MODE");


    public static PgConnection main() {
        String sourceURL = "jdbc:opengauss://"+HOST+":"+PORT+"/"+DATABASE;
        PgConnection conn = null;
        try {

            Properties properties = new Properties();
            PGProperty.USER.set(properties, USER);
            PGProperty.PASSWORD.set(properties, PASSWORD);
            PGProperty.ASSUME_MIN_SERVER_VERSION.set(properties, ASSUME_MIN_SERVER_VERSION);
            PGProperty.REPLICATION.set(properties, REPLICATION);
            PGProperty.PREFER_QUERY_MODE.set(properties, PREFER_QUERY_MODE);
            conn = (PgConnection) DriverManager.getConnection(sourceURL, properties);
            conn.execSQLQuery("show session_timeout;");
            System.out.println("openGauss connection success!");
        }catch (Exception e){
            e.printStackTrace();
        }


        return conn;
    }
}
