package in.dubbadhar.CPBot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.jsoup.nodes.Document;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception
    {
        HashMap<String, String> config = io.getConfig();

        JDABuilder jdaBuilder = JDABuilder.createDefault(config.get("DISCORD_BOT_TOKEN"));
        jdaBuilder.setActivity(Activity.of(Activity.ActivityType.DEFAULT, "Type _help"));
        jdaBuilder.addEventListeners(new BotListener());
        jdaBuilder.build();
    }
}
