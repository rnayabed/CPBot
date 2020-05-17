package in.dubbadhar.CPBot;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class io {
    public static HashMap<String, String> getConfig() throws Exception
    {
        HashMap<String, String> config = new HashMap<>();

        StringBuilder configRaw = new StringBuilder();
        int c;
        FileReader fr = new FileReader(new File(io.class.getResource("config.properties").toExternalForm().substring(5)));
        while((c = fr.read()) != -1)
        {
            configRaw.append((char)c);
        }
        fr.close();

        String lineBreakChar = "\n";
        String configStr = configRaw.toString();
        if(!configStr.contains(lineBreakChar)) lineBreakChar = "\r\n";

        for(String eachLine : configStr.split(lineBreakChar))
        {
            String[] conf = eachLine.split(" = ");
            config.put(conf[0],conf[1]);
        }

        return config;
    }
}
