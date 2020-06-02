package in.dubbadhar.CPBot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CodeChefProblem extends Problem{
    public CodeChefProblem(queryType queryType, MessageReceivedEvent messageReceivedEvent, String[] args)
    {
        super(queryType, messageReceivedEvent, args);
    }

    @Override
    void onRANDOMQuery() throws Exception {

    }

    @Override
    void onLISTQuery() throws Exception {

    }

    @Override
    void onGETQuery() throws Exception {

    }
}
