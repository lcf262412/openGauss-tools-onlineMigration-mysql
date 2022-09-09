import com.mysql.cj.x.protobuf.MysqlxCursor;
import org.junit.Test;

public class DataRunnableTest {

    @Test
    public void test(){
        DataRunnable tesRunnable = new DataRunnable();
        OpenGaussLogicData data = new OpenGaussLogicData();
        tesRunnable.addData(data);
        tesRunnable.start();
    }
}
