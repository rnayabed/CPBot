package in.dubbadhar.CPBot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

abstract class Reply extends Thread{

    private queryType queryType = null;
    private MessageReceivedEvent messageReceivedEvent;
    private MessageChannel messageChannel;
    private String[] args;
    private Document document = null;
    private boolean isFirstMessage = true;
    private Message firstMessage;

    public Reply(queryType queryType, MessageReceivedEvent messageReceivedEvent, String[] args)
    {
        this.queryType = queryType;
        init(messageReceivedEvent, args);
    }

    public Reply(MessageReceivedEvent messageReceivedEvent, String[] args)
    {
        init(messageReceivedEvent, args);
    }

    private void init(MessageReceivedEvent messageReceivedEvent, String[] args)
    {
        this.messageReceivedEvent = messageReceivedEvent;
        this.messageChannel = messageReceivedEvent.getChannel();
        this.args = args;
        this.messageChannel.sendMessage("Processing ...").queue(message -> {
            firstMessage = message;
            start();
        });
    }

    public String[] getArgs()
    {
        return args;
    }

    public MessageChannel getMessageChannel()
    {
        return messageChannel;
    }

    public MessageReceivedEvent getMessageReceivedEvent()
    {
        return messageReceivedEvent;
    }

    public queryType getQuery()
    {
        return queryType;
    }

    public void loadDocument(String url) throws Exception
    {
        document = Jsoup.connect(url).get();
    }

    public Document getDocument()
    {
        return document;
    }

    public void sendMessage(String message)
    {
        if(message.length()>0)
        {
            if(isFirstMessage)
            {
                firstMessage.editMessage(message).queue();
                isFirstMessage = false;
            }
            else
            {
                this.messageChannel.sendMessage(message).queue();
            }
        }
    }

    public String formatLaTeX(String text)
    {
        int i;

        //pmod
        while((i = text.indexOf("\\pmod{"))>-1)
        {
            int j = text.indexOf("}");
            text = text.substring(0,i)+"(mod "+text.substring(i+6,j)+")"+text.substring(j+1);
        }

        //frac
        while((i = text.indexOf("\\frac{"))>-1)
        {
            int j = text.indexOf("}{");
            text = text.substring(0,i)+text.substring(i+6,j)+"/"+text.substring(j+2);
            int k = text.indexOf("}");
            text = text.substring(0,k)+text.substring(k+1);
        }


        text = text.replace("*","\\*") //Prevent unnecessary formatting
                .replace("$$$","*")//$$$ to italics
                .replace("\\end{matrix}","```\n") //Matrix
                .replace("\\begin{matrix}","\n```\n") //Matrix
                .replace("\\\\","\n") //Matrix
                .replace("\\pm","±") // Plus Minus
                .replace("\\times","×") // multiply
                .replace("\\equiv","≡") // Equivalent
                .replace("\\neq","≠") //Not Equal To
                .replace("\\div","÷") // Division
                .replace("\\leq","≤") //Less Than Or Equal To
                .replace("\\le","≤") // Less Than Or Equal To
                .replace("\\geq","≥") // Greater Than Or Equal To
                .replace("\\ge","≥") // Greater Than Or Equal To
                .replace("\\sim","∼") // Tilde
                .replace("^\\circ","°") // Degree
                .replace("\\approx","≈") // Approximate
                .replace("\\mu","µ") // Meu
                .replace("\\ldots","…") // Three Dots
                .replace("\\cdot","⋅"); // Multiplication Dot

        return text;
    }

    public abstract void runMethod() throws Exception;

    @Override
    public void run() {
        try
        {
            runMethod();
        }
        catch (Exception e)
        {
            sendMessage("Internal Error Occurred. Contact developer.");
            e.printStackTrace();
        }
    }
}
