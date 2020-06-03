package in.dubbadhar.CPBot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        String[] args = getArgs();
        loadDocument("https://www.codechef.com/problems/"+args[1]);

        Elements headerGroups = getDocument().getElementsByClass("large-12 columns");
        if(headerGroups.isEmpty())
            sendMessage("No such problem found");
        else
        {
            Element header = headerGroups.first().getElementsByTag("h1").first();
            System.out.println(header.text());
        }
    }
}
