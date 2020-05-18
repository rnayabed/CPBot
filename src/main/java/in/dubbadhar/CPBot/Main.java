package in.dubbadhar.CPBot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Main {

    final static String VERSION = "1.3";
    final static String REPO_LINK = "https://github.com/dubbadhar/CPBot";

    public static void main(String[] args) throws Exception
    {
        System.out.println("Menu\n" +
                "1. Start\n" +
                "2. Edit Discord Bot API Key\n" +
                "3. About\n" +
                "4. Exit\n");
        String userChoice = io.readLine();

        HashMap<String, String> config = io.getConfig();

        if(userChoice.equals("1"))
        {
            if(config.get("DISCORD_BOT_TOKEN").equals("NULL"))
                System.out.println("\nDiscord Bot token is not provided. Unable to Start. Run option 2 to change the key.");
            else
            {
                System.out.println("\nStarting bot ...");

                DefaultShardManagerBuilder.createDefault(config.get("DISCORD_BOT_TOKEN"))
                    .setActivity(Activity.of(Activity.ActivityType.LISTENING, "_help"))
                    .addEventListeners(new BotListener())
                    .build();

                System.out.println("... Done!");
            }
        }
        else if(userChoice.equals("2"))
        {
            if(!config.get("DISCORD_BOT_TOKEN").equals("NULL"))
                System.out.println("\nCurrent Discord Bot Token : "+config.get("DISCORD_BOT_TOKEN"));
            System.out.println("\nEnter new Discord Bot Token Key : ");
            String newToken = io.readLine();
            System.out.println("New token : '"+newToken+"'\nFinalize [Y/N]?");
            String choice = io.readLine();
            config.replace("DISCORD_BOT_TOKEN",newToken);
            if(choice.equalsIgnoreCase("Y"))
            {
                io.updateConfig(config);
                System.out.println("Successfuly changed Discord Bot Token");
            }
            else
            {
                System.out.println("Abort!");
            }
        }
        else if(userChoice.equals("3"))
        {
            System.out.println("\nAbout\n" +
                    "Made by Debayan Sutradhar\n" +
                    "Twitter : dubbadhar\n" +
                    "Github : dubbadhar\n" +
                    "Source Code : "+REPO_LINK +
                    "\nVersion : "+VERSION);
        }
    }
}
