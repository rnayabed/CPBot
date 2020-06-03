package in.dubbadhar.CPBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class BotListener extends ListenerAdapter {

    HashMap<String,String> config;

    final Source defaultSource = Source.CodeForces;

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
    public void onResume(@NotNull ResumedEvent event)
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

        if(args[0].startsWith("cp!"))
        {
            consoleOutput(event);

            switch(args[0])
            {
                case "cp!help" -> channel.sendMessage("Available Commands\n\n" +
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
                case "cp!list" -> sendRequest(queryType.LIST, messageReceivedEvent, args);
                case "cp!get" -> sendRequest(queryType.GET, messageReceivedEvent, args);
                case "cp!about" -> channel.sendMessage(new MessageBuilder()
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
                case "cp!random" -> sendRequest(queryType.RANDOM, messageReceivedEvent, args);
                case "cp!setsource" -> new preferenceSetter(messageReceivedEvent, args);
                default -> channel.sendMessage("Command not recognised. Check `cp!help`.").queue();
            }
        }
    }

    private void sendRequest(queryType queryType, MessageReceivedEvent messageReceivedEvent, String[] args)
    {
        Source source = getSource(messageReceivedEvent, args);
        if(source!=null)
        {
            System.out.println(Arrays.toString(argsWithoutSourceArg(args)));
            if(source == Source.CodeForces) new CodeForcesProblem(queryType, messageReceivedEvent, argsWithoutSourceArg(args));
            else if(source == Source.CodeChef) new CodeChefProblem(queryType, messageReceivedEvent, argsWithoutSourceArg(args));
        }
    }

    public String[] argsWithoutSourceArg(String[] args)
    {
        List<String> list = new ArrayList<>();
        for(int i = 0;i<args.length;i++)
        {
            if(args[i].equals("-source"))
                args[i+1] = "";
            else if(!args[i].equals(""))
                list.add(args[i]);
        }
        return list.toArray(new String[0]);
    }

    private Source getSource(MessageReceivedEvent messageReceivedEvent, String[] args)
    {
        for(int i = 1;i<args.length;i++)
        {
            if(args[i].equals("-source"))
            {
                if(i!=(args.length-1))
                {
                    if(args[i+1] == null)
                    {
                        messageReceivedEvent.getChannel().sendMessage("Invalid usage of `-source` argument. Check `cp!help`.").queue();
                        return null;
                    }
                    else
                    {
                        Source s = Source.fromString(args[i+1]);
                        if(s==null)
                        {
                            messageReceivedEvent.getChannel().sendMessage("Invalid usage of `-source` argument. Check `cp!help`.").queue();
                            return null;
                        }
                        else
                        {
                            return s;
                        }
                    }
                }
            }
        }

        try
        {
            Source userPreference = database.getUserPreference(messageReceivedEvent.getMessage().getAuthor().getId());
            if(userPreference == Source.NO_PREFERENCE)
            {
                Source guildPreference = database.getGuildPreference(messageReceivedEvent.getMessage().getAuthor().getId());
                if(guildPreference == Source.NO_PREFERENCE)
                    return defaultSource;
                else
                    return guildPreference;
            }
            else
                return userPreference;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            messageReceivedEvent.getChannel().sendMessage("Internal Error Occurred. Contact Developer.").queue();
            return null;
        }
    }

    private void consoleOutput(GenericEvent event)
    {
        System.out.println();
        if(event instanceof MessageReceivedEvent)
        {
            MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
            System.out.print("Message Received from "+messageReceivedEvent.getMessage().getAuthor().getAsTag());
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
