package in.dubbadhar.CPBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;


public class preferenceSetter extends Reply {
    public preferenceSetter(MessageReceivedEvent messageReceivedEvent, String[] args)
    {
        super(messageReceivedEvent, args);
    }

    @Override
    public void runMethod() throws Exception {
        String[] args = getArgs();
        if(args.length == 3)
        {
            if(args[1].equals("-myself"))
            {
                if(args[2].equals("CodeForces") || args[2].equals("CodeChef"))
                {
                    database.setUserPreference(getMessageReceivedEvent().getMessage().getAuthor().getId(), Source.fromString(args[2]));
                    sendMessage("Successfully changed default preference to "+args[2]+"!");
                }
                else
                {
                    sendMessage("Incorrect argument usage. Check `cp!help`.");
                }
            }
            else if(args[1].equals("-server"))
            {
                if(getMessageChannel().getType().isGuild())
                {
                    if(getMessageReceivedEvent().getAuthor().getId().equals(getMessageReceivedEvent().getGuild().getOwnerId()))
                    {
                        if(args[2].equals("CodeForces") || args[2].equals("CodeChef"))
                        {
                            database.setGuildPreference(getMessageReceivedEvent().getGuild().getId(), Source.fromString(args[2]));
                            sendMessage("Successfully changed default preference to "+args[2]+"!");
                        }
                        else
                        {
                            sendMessage("Incorrect argument usage. Check `cp!help`.");
                        }
                    }
                    else
                    {
                        sendMessage("Only the owner of this server can set a default source.");
                    }
                }
                else
                {
                    sendMessage("You're not even in a server. Try using the `-myself` argument if you want to set for yourself.");
                }
            }
        }
        else
        {
            sendMessage("Incorrect usage of `cp!setsource`. Check `cp!help`.");
        }
    }
}
