import com.mysql.cj.x.protobuf.MysqlxCursor;
import org.junit.Test;

public class DataRunnableTest {

    @Test
    public void test(){
        DataThread tesRunnable = new DataThread();
        OpenGaussLogicData data = new OpenGaussLogicData();
        tesRunnable.addData(data);
        tesRunnable.start();
    }
}
