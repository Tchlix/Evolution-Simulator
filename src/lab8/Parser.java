package lab8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public Map <String,Integer> parameters;
    public Parser() throws Exception {
        parameters = parameters();
    }

    public String load() {
        Path fileName = Path.of("parameters.json");
        {
            try {
                return Files.readString(fileName);
            } catch (IOException e) {
                System.out.println("Error while loading JSON file");
                return null;
            }
        }
    }
    private Map<String, Integer> parameters() throws Exception {
        Map<String,Integer> parameters = new HashMap<>();
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        String json = load();
        Matcher m = p.matcher(json);
        while (m.find()) {
            String key = m.group(1);
            m.find();
            int value = Integer.parseInt(m.group(1));
            parameters.put(key,value);
        }
        if(parameters.get("jungleRatio") > Math.min(parameters.get("height"),parameters.get("width")))
            throw new Exception("Jungle can't be bigger than the map");
        return parameters;
    }
}
