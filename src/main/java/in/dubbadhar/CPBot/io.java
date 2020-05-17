package in.dubbadhar.CPBot;

import java.io.*;
import java.util.HashMap;

public class io {
    public static HashMap<String, String> getConfig() throws Exception
    {
        HashMap<String, String> config = new HashMap<>();

        StringBuilder configRaw = new StringBuilder();
        int c;
        FileReader fileReader = new FileReader(new File(io.class.getResource("config.properties").toExternalForm().substring(5)));
        while((c = fileReader.read()) != -1)
        {
            configRaw.append((char)c);
        }
        fileReader.close();

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

    public static void updateConfig(HashMap<String,String> config) throws Exception
    {
        FileWriter fileWriter = new FileWriter(new File(io.class.getResource("config.properties").toExternalForm().substring(5)));
        for(String eachKey : config.keySet())
        {
            fileWriter.write(eachKey+" = "+config.get(eachKey)+"\n");
        }
        fileWriter.close();
    }

    public static String readLine() throws Exception
    {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        return bf.readLine();
    }
}
