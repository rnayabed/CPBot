package in.dubbadhar.CPBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jsoup.nodes.Document;

import java.awt.*;

import static in.dubbadhar.CPBot.Main.REPO_LINK;
import static in.dubbadhar.CPBot.Main.VERSION;


public class BotListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        String rawMsg = msg.getContentRaw();
        MessageChannel channel = event.getChannel();

        String[] args = rawMsg.split(" ");

        if (rawMsg.equals("_help"))
        {
            plnMsgReceived(rawMsg, event.getGuild().getName(), channel.getName());
            channel.sendMessage("Available Commands\n\n" +
                    "`_list`\n" +
                    "**To get a list of problems**\n" +
                    "**Arguments (Optional)**\n" +
                    "   `-p {Page No.}`\n" +
                    "   `-d {Difficulty Rating}`\n" +
                    "   `-d {Minimum Difficulty}-{Maximum Difficulty}`\n" +
                    "   `-t \"{Tag1},{Tag2},{TagN}`\"\n" +
                    "   `-o {Order}`\n" +
                    "   `{Order}` can be any one of the following ONLY\n" +
                    "       `diff-asc` : Ascending Difficulty\n" +
                    "       `diff-des` : Descending Difficulty\n" +
                    "       `solv-asc` : Ascending Solved\n" +
                    "       `solv-des` : Descending Solved\n\n\n" +
                    "`_random`\n" +
                    "**To get a random problem**\n" +
                    "**Arguments (Optional)**\n" +
                    "   `-d {Difficulty Rating}`\n" +
                    "   `-d {Minimum Difficulty}-{Maximum Difficulty}`\n" +
                    "   `-t \"{Tag1},{Tag2},{TagN}`\"\n\n\n" +
                    "`_get`\n" +
                    "**To get a particular problem**\n" +
                    "**Arguments (Required)**\n" +
                    "   `{Problem ID}`\n\n\n" +
                    "`_about`\n" +
                    "**To get details of the bot**\n\n\n" +
                    "**Examples**\n" +
                    "`_list -d 2000 -o diff-asc`\n" +
                    "`_random -d 800-1200`\n" +
                    "`_get 1A`").queue();
        }
        else if(rawMsg.startsWith("_list"))
        {
            plnMsgReceived(rawMsg, event.getGuild().getName(), channel.getName());
            new CodeForcesProblem(CodeForcesProblem.queryType.LIST, channel, args);
        }
        else if(rawMsg.startsWith("_get"))
        {
            plnMsgReceived(rawMsg, event.getGuild().getName(), channel.getName());
            new CodeForcesProblem(CodeForcesProblem.queryType.GET, channel, args);
        }
        else if(rawMsg.equals("_about"))
        {
            plnMsgReceived(rawMsg, event.getGuild().getName(), channel.getName());
            EmbedBuilder aboutEmbedBuilder = new EmbedBuilder();
            aboutEmbedBuilder.setTitle("About");
            aboutEmbedBuilder.setColor(Color.RED);
            aboutEmbedBuilder.setDescription("Made by Debayan Sutradhar\n" +
                    "Twitter : dubbadhar\n" +
                    "Discord : dubbadhar#5744\n" +
                    "GitHub : dubbadhar\n\n" +
                    "This Bot fetches problems from CodeForces.\n" +
                    "Source : "+REPO_LINK+
                    "\nVersion : "+VERSION);
            Message m = new MessageBuilder().setEmbed(aboutEmbedBuilder.build()).build();
            channel.sendMessage(m).queue();
        }
        else if(rawMsg.startsWith("_random"))
        {
            plnMsgReceived(rawMsg, event.getGuild().getName(), channel.getName());
            new CodeForcesProblem(CodeForcesProblem.queryType.RANDOM, channel, args);
        }
    }

    void plnMsgReceived(String raw, String guildName, String channelName)
    {
        System.out.println("Message Received from \""+guildName+"\", #"+channelName+"\n"+raw+"\n");
    }
}
