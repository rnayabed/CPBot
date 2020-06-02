package in.dubbadhar.CPBot;

import java.io.*;
import java.util.HashMap;

public class io {
    public static HashMap<String, String> getConfig() throws Exception
    {
        return readPropertiesFile("config.properties");
    }

    public static HashMap<String, String> readPropertiesFile(String fileName) throws Exception
    {
        HashMap<String, String> config = new HashMap<>();

        StringBuilder configRaw = new StringBuilder();
        int c;
        FileReader fileReader = new FileReader(new File(io.class.getResource(fileName).toExternalForm().substring(5)));
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
            if(conf.length==2) // empty file if 1
            {
                config.put(conf[0],conf[1]);
            }
        }

        return config;
    }

    public static void updateConfig(HashMap<String, String> config) throws Exception
    {
        updatePropertiesFile(config, "config.properties");
    }

    public static void updatePropertiesFile(HashMap<String,String> properties, String fileName) throws Exception
    {
        FileWriter fileWriter = new FileWriter(new File(io.class.getResource(fileName).toExternalForm().substring(5)));
        for(String eachKey : properties.keySet())
        {
            fileWriter.write(eachKey+" = "+properties.get(eachKey)+"\n");
        }
        fileWriter.close();
    }

    public static String readLine() throws Exception
    {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        return bf.readLine();
    }
}
