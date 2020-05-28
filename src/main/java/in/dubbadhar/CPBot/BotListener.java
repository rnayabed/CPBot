package in.dubbadhar.CPBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;


public class BotListener extends ListenerAdapter {

    HashMap<String,String> config;

    public BotListener(HashMap<String, String> config) {
        this.config = config;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        updateStatus(event);
    }

    @Override
    public void onDisconnect(@NotNull DisconnectEvent event)
    {
        consoleOutput(event);
    }

    @Override
    public void onReconnect(@NotNull ReconnectedEvent event)
    {
        consoleOutput(event);
    }

    @Override
    public void onGenericEvent(@NotNull GenericEvent event)
    {
        if(event instanceof MessageReceivedEvent) msgReceived(event);
        else if(event instanceof GuildJoinEvent || event instanceof GuildLeaveEvent) {
            consoleOutput(event);
            updateStatus(event);
        }
    }

    private void updateStatus(GenericEvent event)
    {
        event.getJDA().getPresence().setActivity(Activity.listening("cp!help in "+getNoOfGuildsJoined(event)+" servers"));
    }

    private int getNoOfGuildsJoined(GenericEvent event)
    {
        return event.getJDA().getGuilds().size();
    }

    private void msgReceived(GenericEvent event)
    {
        MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
        Message msg = messageReceivedEvent.getMessage();
        String rawMsg = msg.getContentRaw();
        MessageChannel channel = messageReceivedEvent.getChannel();

        String[] args = rawMsg.split(" ");

        if(rawMsg.startsWith("cp!"))
        {
            consoleOutput(event);

            if (rawMsg.equals("cp!help"))
                channel.sendMessage("Available Commands\n\n" +
                        "`cp!list`\n" +
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
                        "`cp!random`\n" +
                        "**To get a random problem**\n" +
                        "**Arguments (Optional)**\n" +
                        "   `-d {Difficulty Rating}`\n" +
                        "   `-d {Minimum Difficulty}-{Maximum Difficulty}`\n" +
                        "   `-t \"{Tag1},{Tag2},{TagN}`\"\n\n\n" +
                        "`cp!get`\n" +
                        "**To get a particular problem**\n" +
                        "**Arguments (Required)**\n" +
                        "   `{Problem ID}`\n\n\n" +
                        "`cp!about`\n" +
                        "**To get details of the bot**\n\n\n" +
                        "**Examples**\n" +
                        "`cp!list -d 2000 -o diff-asc`\n" +
                        "`cp!random -d 800-1200`\n" +
                        "`cp!get 1A`").queue();
            else if(rawMsg.startsWith("cp!list"))
                new CodeForcesProblem(CodeForcesProblem.queryType.LIST, channel, args);
            else if(rawMsg.startsWith("cp!get"))
                new CodeForcesProblem(CodeForcesProblem.queryType.GET, channel, args);
            else if(rawMsg.equals("cp!about"))
                channel.sendMessage(new MessageBuilder()
                        .setEmbed(new EmbedBuilder()
                                .setTitle("About")
                                .setColor(Color.RED)
                                .setDescription("Made by Debayan Sutradhar\n" +
                                        "Twitter : dubbadhar\n" +
                                        "Discord : dubbadhar#5744\n" +
                                        "GitHub : dubbadhar\n\n" +
                                        "This Bot fetches problems from Code Forces.\n" +
                                        "Support for other websites coming soon.\n" +
                                        "Source : "+config.get("REPO_LINK")+
                                        "\nVersion : "+config.get("VERSION"))
                                .build())
                        .build()).queue();
            else if(rawMsg.startsWith("cp!random"))
                new CodeForcesProblem(CodeForcesProblem.queryType.RANDOM, channel, args);
            else
                channel.sendMessage("Command not recognised. Check `cp!help`.").queue();
        }
    }

    private void consoleOutput(GenericEvent event)
    {
        System.out.println();
        if(event instanceof MessageReceivedEvent)
        {
            MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
            System.out.print("Message Received from "+messageReceivedEvent.getMessage().getAuthor());
            if(messageReceivedEvent.getChannelType().isGuild())
                System.out.print(", \""+messageReceivedEvent.getGuild().getName()+"\", #"+messageReceivedEvent.getChannel().getName());
            System.out.println("\n"+messageReceivedEvent.getMessage().getContentRaw());
        }
        else if(event instanceof GuildJoinEvent)
        {
            GuildJoinEvent guildJoinEvent = (GuildJoinEvent) event;
            System.out.println("Joined Guild '"+guildJoinEvent.getGuild().getName()+"'!\n" +
                    "Now running on "+getNoOfGuildsJoined(event)+" servers");
        }
        else if(event instanceof GuildLeaveEvent)
        {
            GuildLeaveEvent guildLeaveEvent = (GuildLeaveEvent) event;
            System.out.println("Left Guild '"+guildLeaveEvent.getGuild().getName()+"'\n" +
                    "Now running on "+getNoOfGuildsJoined(event)+" servers");
        }
        else if(event instanceof DisconnectEvent)
            System.out.println("Disconnected from Discord");
        else if(event instanceof ReconnectedEvent)
            System.out.println("Reconnected to Discord");
        System.out.println();
    }
}
