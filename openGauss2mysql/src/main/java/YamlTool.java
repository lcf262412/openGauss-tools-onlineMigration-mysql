import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description: YamlTool class
 *
 * @author zhangyaozhong
 * @date 2022/09/09
 **/
public class YamlTool {
    Map<String, Object> properties;

    public YamlTool(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Yaml yaml = new Yaml();
        properties = yaml.loadAs(inputStream, Map.class);
    }

    public LinkedHashMap getValue(String key) {
        return (LinkedHashMap) properties.get(key);
    }

}
