import lombok.Data;
import lombok.ToString;

import java.util.List;
@Data
@ToString
public class OpenGaussLogicData {
    public String tableName;
    public String opType;
    public List<String> columnsName;
    public List<String> columnsType;
    public List<String> columnsVal;
    public List<String> oldKeysName;
    public List<String> oldKeysType;
    public List<String> oldKeysVal;


}
