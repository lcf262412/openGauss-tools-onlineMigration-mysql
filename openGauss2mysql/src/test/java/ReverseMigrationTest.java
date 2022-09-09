import org.junit.Test;

import java.sql.SQLException;

public class ReverseMigrationTest {
    @Test
    public void test() throws SQLException, InterruptedException {
        ReverseMigration.dropSlot();
        ReverseMigration.createSlot();
        ReverseMigration.startSlot();
    }
}
