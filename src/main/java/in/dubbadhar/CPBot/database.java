package in.dubbadhar.CPBot;

import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class database {
    private static Connection connection;

    static void init() throws Exception
    {
        Class.forName("org.sqlite.JDBC");
        String fileName = database.class.getResource("source.preferences.db").toExternalForm().replace("file:/","");
        System.out.println(fileName);
        connection = DriverManager.getConnection("jdbc:sqlite:"+fileName);
    }

    static Source getUserPreference(String id) throws Exception
    {
        Source tbr;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS WHERE ID='"+id+"';");
        if(resultSet.next())
            tbr = Source.valueOf(resultSet.getString("SOURCE"));
        else
            tbr = Source.NO_PREFERENCE;
        statement.close();
        return tbr;
    }

    static Source getGuildPreference(String id) throws Exception
    {
        Source tbr;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM GUILDS WHERE ID='"+id+"';");
        if(resultSet.next())
            tbr = Source.valueOf(resultSet.getString("SOURCE"));
        else
            tbr = Source.NO_PREFERENCE;
        statement.close();
        return tbr;
    }

    static boolean setUserPreference(String id, Source source) throws Exception
    {
        Statement statement = connection.createStatement();
        return statement.execute("INSERT OR REPLACE INTO USERS VALUES ('"+id+"', '"+source.toString()+"');");
    }

    static boolean setGuildPreference(String id, Source source) throws Exception
    {
        Statement statement = connection.createStatement();
        return statement.execute("INSERT OR REPLACE INTO GUILDS (ID,SOURCE)" +
                "VALUES ('"+id+"', '"+source.toString()+"');");
    }
}
