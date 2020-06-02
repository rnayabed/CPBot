package in.dubbadhar.CPBot;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception
    {
        try
        {
            HashMap<String, String> config = io.getConfig();

            if(args.length>0)
            {
                if(args[0].equals("-auto-start"))
                {
                    startBot(config);
                    return;
                }
            }

            System.out.println("CPBot "+config.get("VERSION")+"\n" +
                    "Menu\n" +
                    "1. Start\n" +
                    "2. Edit Discord Bot API Key\n" +
                    "3. About\n" +
                    "4. Exit\n");
            String userChoice = io.readLine();


            switch (userChoice) {
                case "1" -> startBot(config);
                case "2" -> {
                    if (!config.get("DISCORD_BOT_TOKEN").contains("NULL"))
                        System.out.println("\nCurrent Discord Bot Token : " + config.get("DISCORD_BOT_TOKEN"));
                    System.out.println("\nEnter new Discord Bot Token Key : ");
                    String newToken = io.readLine();
                    System.out.println("New token : '" + newToken + "'\nFinalize [Y/N]?");
                    String choice = io.readLine();
                    config.replace("DISCORD_BOT_TOKEN", newToken);
                    if (choice.equalsIgnoreCase("Y")) {
                        io.updateConfig(config);
                        System.out.println("Successfuly changed Discord Bot Token");
                    } else
                        System.out.println("Abort!");
                }
                case "3" -> System.out.println("\nAbout\n" +
                        "Made by Debayan Sutradhar\n" +
                        "Twitter : dubbadhar\n" +
                        "Github : dubbadhar\n" +
                        "Source Code : " + config.get("REPO_LINK") +
                        "\nVersion : " + config.get("VERSION"));
                case "4" -> System.out.println("\nQuitting ...");
                default -> System.out.println("\nInvalid Input.\nAbort");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Internal Error Occurred. Contact Developer.");
        }
    }

    static void startBot(HashMap<String,String> config) throws Exception
    {
        if(config.get("DISCORD_BOT_TOKEN").contains("NULL"))
            System.out.println("\nDiscord Bot token is not provided. Unable to Start. Run option 2 to change the key.");
        else
        {
            System.out.println("\nInit database ...");
            database.init();
            System.out.println("Done ...\nStarting bot ...");
            DefaultShardManagerBuilder.createDefault(config.get("DISCORD_BOT_TOKEN"))
                    .addEventListeners(new BotListener(config))
                    .build();
            System.out.println("... Done!");
        }
    }
}
