package in.dubbadhar.CPBot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Problem extends Reply{
    public Problem(in.dubbadhar.CPBot.queryType queryType, MessageReceivedEvent messageReceivedEvent, String[] args) {
        super(queryType, messageReceivedEvent, args);
    }

    @Override
    public void runMethod() throws Exception {
        switch(getQuery())
        {
            case RANDOM -> onRANDOMQuery();
            case LIST -> onLISTQuery();
            case GET -> {
                if(getArgs().length<2)
                    sendMessage("You need to mention the Problem ID");
                else
                    onGETQuery();
            }
        }
    }

    abstract void onRANDOMQuery() throws Exception;
    abstract void onLISTQuery() throws Exception;
    abstract void onGETQuery() throws Exception;
}
