package in.dubbadhar.CPBot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CodeForcesProblem extends Thread {
    enum queryType{
        RANDOM, LIST, GET
    }

    queryType qt;
    MessageChannel channel;
    String[] args;
    Document document;

    public CodeForcesProblem(queryType queryType, MessageChannel channel, String[] args)
    {
        qt = queryType;
        this.channel = channel;
        this.args = args;
        this.channel.sendMessage("Processing ...").queue(message -> {
            msg = message;
            start();
        });
    }

    public void loadCodeForcesDefaultProblemSite() throws Exception
    {
        if(document==null)
            document = Jsoup.connect("https://codeforces.com/problemset").get();
        else if(!document.location().equals("https://codeforces.com/problemset"))
            document = Jsoup.connect("https://codeforces.com/problemset").get();
    }

    public int getMaximumPages()
    {
        try
        {
            Elements liElements = document.getElementsByClass("pagination").first().getElementsByTag("li");
            int maxPages;
            if(liElements.last().text().contains("→"))
                maxPages = Integer.parseInt(liElements.get(liElements.size()-2).text());
            else
                maxPages = Integer.parseInt(liElements.last().text());
            return maxPages;
        }
        catch (NullPointerException e)
        {
            return 1;
        }
    }

    Message msg;


    @Override
    public void run()
    {
        try
        {
            if(qt == queryType.LIST)
            {
                PageLoadStatus pageLoadStatus = loadFilteredPage(args);
                if(pageLoadStatus.isSuccess())
                {
                    Elements problems = document.getElementsByClass("problems").first().getElementsByTag("tbody").first().getElementsByTag("tr");


                    if(problems.size() == 1)
                    {
                        sendMessage("No items found with that filter :/");
                    }
                    else
                    {
                        StringBuilder listTable = new StringBuilder();

                        String pageNo = document.getElementsByClass("page-index active").first().text();

                        listTable.append("```\n___________________________________________________________________________________________________\n" +
                                "| Problem | Name                                                         | Difficulty | Solved By |\n");
                        for(int i = 1;i<problems.size(); i++)
                        {
                            Elements problemParts = problems.get(i).getElementsByTag("td");
                            String problemID = problemParts.get(0).text();
                            String problemTitle = problemParts.get(1).getElementsByTag("div").first().text();
                            //String problemTags = problemParts.get(1).getElementsByTag("div").last().text();
                            String problemDifficulty = problemParts.get(3).text();
                            String solvedBy = problemParts.get(4).text();

                            listTable.append("| ")
                                    .append(problemID)
                                    .append(" ".repeat((7 - problemID.length())))
                                    .append(" | ")
                                    .append(problemTitle)
                                    .append(" ".repeat((60-problemTitle.length())))
                                    .append(" | ")
                                    .append(problemDifficulty)
                                    .append(" ".repeat(10-problemDifficulty.length()))
                                    .append(" | ")
                                    .append(solvedBy)
                                    .append(" ".repeat(9-solvedBy.length()))
                                    .append(" |\n");

                            if(listTable.length()>1500)
                            {
                                sendMessage(listTable.toString()+"\n```");
                                listTable.setLength(0);
                                listTable.append("```\n");
                            }
                        }

                        listTable.append("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\n```");

                        listTable.append("\nCurrent Page : ")
                                .append(pageNo)
                                .append("\nMax Page [with current filter(s) applied] : ")
                                .append(getMaximumPages());

                        sendMessage(listTable.toString());
                    }
                }
                else
                    sendMessage(pageLoadStatus.getErr());
            }
            else if(qt == queryType.GET)
            {
                if(args.length < 2)
                {
                    sendMessage("You need to mention the problem ID");
                }
                else
                {
                    int index = -1;
                    if(args[1].contains("A")) index = args[1].indexOf("A");
                    else if(args[1].contains("B")) index = args[1].indexOf("B");
                    else if(args[1].contains("C")) index = args[1].indexOf("C");
                    else if(args[1].contains("D")) index = args[1].indexOf("D");
                    else if(args[1].contains("E")) index = args[1].indexOf("E");
                    else if(args[1].contains("F")) index = args[1].indexOf("F");
                    else if(args[1].contains("G")) index = args[1].indexOf("G");
                    else if(args[1].contains("H")) index = args[1].indexOf("H");
                    else if(args[1].contains("I")) index = args[1].indexOf("I");
                    else if(args[1].contains("J")) index = args[1].indexOf("J");

                    if(index==-1)
                    {
                        sendMessage("Incorrect Problem ID Format");
                    }
                    else
                    {
                        String url = "https://codeforces.com/problemset/problem/"+args[1].substring(0,index)+"/"+args[1].substring(index);
                        document = Jsoup.connect(url).get();
                        if(!document.location().equals(url)) sendMessage("No such problem found");
                        else
                        {
                            StringBuilder output = new StringBuilder();
                            Element problemStatementDiv = document.getElementsByClass("problem-statement").first();
                            Element headerClass = problemStatementDiv.getElementsByClass("header").first();

                            String problemTitle = headerClass.getElementsByClass("title").first().text();
                            String timeLimit = headerClass.getElementsByClass("time-limit").first().text().substring(20);
                            String memoryLimit = headerClass.getElementsByClass("memory-limit").first().text().substring(22);
                            String inputType = headerClass.getElementsByClass("input-file").text().substring(6);
                            String outputType = headerClass.getElementsByClass("output-file").first().text().substring(7);

                            output.append("**Problem ")
                                    .append(args[1])
                                    .append("**\n\nTitle : ")
                                    .append(problemTitle)
                                    .append("\nTime Limit Per Test : ")
                                    .append(timeLimit)
                                    .append("\nMemory Limit Per Test : ")
                                    .append(memoryLimit)
                                    .append("\nInput Type : ")
                                    .append(inputType)
                                    .append("\nOutput Type : ")
                                    .append(outputType)
                                    .append("\n");

                            Elements roundBoxes = document.getElementsByClass("roundbox sidebox");
                            for(Element eachRoundBox : roundBoxes)
                            {
                                if(eachRoundBox.child(2).text().contains("Problem tags"))
                                {
                                    Elements tagElements = eachRoundBox.child(3).children();


                                    int minus = 1;
                                    String difficulty = tagElements.get(tagElements.size()-2).text();
                                    if(difficulty.startsWith("*"))
                                    {
                                        minus = 2;
                                        output.append("Difficulty Rating : ")
                                                .append(difficulty.substring(1));
                                    }

                                    output.append("\nTags : ");
                                    for(int i =0;i<tagElements.size()-minus;i++)
                                    {
                                        output.append(tagElements.get(i).text());
                                        if(i<tagElements.size()-(minus+1))
                                        {
                                            output.append(", ");
                                        }
                                    }

                                }
                            }

                            output.append("\n\nLink : ")
                                    .append(document.location());

                            sendMessage(output.toString());
                            output.setLength(0);

                            Element explanationDiv = problemStatementDiv.child(1);

                            output = formatText(explanationDiv, output);

                            Element inputDiv = problemStatementDiv.getElementsByClass("input-specification").first();
                            output.append("\n**Input**\n");
                            output = formatText(inputDiv, output);

                            Element outputDiv = problemStatementDiv.getElementsByClass("output-specification").first();
                            output.append("\n**Output**\n");
                            output = formatText(outputDiv, output);

                            Element examplesDiv = problemStatementDiv.getElementsByClass("sample-test").first();
                            output.append("\n**Examples**\n");
                            output = formatText(examplesDiv, output);

                            Element noteDiv = problemStatementDiv.getElementsByClass("note").first();
                            if(noteDiv!=null)
                            {
                                output.append("\n**Note**\n");
                                output = formatText(noteDiv, output);
                            }

                            sendMessage(output.toString());
                        }
                    }
                }
            }
            else if(qt == queryType.RANDOM)
            {
                if(args.length == 1)
                {
                    loadCodeForcesDefaultProblemSite();

                    int maxPages = getMaximumPages();
                    int randomPage = ((int) (Math.random() * maxPages));
                    document = Jsoup.connect("https://codeforces.com/problemset/page/"+randomPage).get();
                }
                else
                {
                    PageLoadStatus pageLoadStatus = loadFilteredPage(args);

                    if(pageLoadStatus.isSuccess())
                    {
                        int maxPages = getMaximumPages();
                        int randomPage = ((int) (Math.random() * maxPages));
                        PageLoadStatus pageLoadStatus2 = loadFilteredPage(args, randomPage);
                        if(!pageLoadStatus2.isSuccess())
                        {
                            sendMessage(pageLoadStatus.getErr());
                            return;
                        }
                    }
                    else
                    {
                        sendMessage(pageLoadStatus.getErr());
                        return;
                    }
                }

                Elements problems = document.getElementsByClass("problems").first().getElementsByTag("tbody").first().getElementsByTag("tr");
                if(problems.size() == 1)
                    sendMessage("No problem found with the filter(s) applied :/");
                else
                {
                    int randomProblem = ((int) (Math.random() * problems.size()));

                    Elements problemParts = problems.get(randomProblem).getElementsByTag("td");
                    String problemID = problemParts.get(0).text();
                    msg.delete().queue();
                    new CodeForcesProblem(CodeForcesProblem.queryType.GET, channel, new String[]{"cp!get",problemID});
                }
            }
        }
        catch (Exception e)
        {
            sendMessage("Internal Error Occurred. Contact developer.");
            e.printStackTrace();
        }
    }

    public StringBuilder formatText(Element parent, StringBuilder output)
    {
        Elements explanationChildren = parent.children();
        for(Element eachElement : explanationChildren)
        {
            switch (eachElement.tagName()) {
                case "p" -> {
                    output.append(formatLaTeX(eachElement.text())).append("\n");

                    String imageURL = searchForImage(eachElement);
                    if (imageURL != null) {
                        sendMessage(output.toString());
                        output.setLength(0);
                        sendMessage(imageURL);
                    }
                }
                case "center" -> {
                    String imageURL = searchForImage(eachElement);
                    if (imageURL != null) {
                        sendMessage(output.toString());
                        output.setLength(0);
                        sendMessage(imageURL);
                    }
                }
                case "ul" -> {
                    Elements lis = eachElement.getElementsByTag("li");
                    for (Element li : lis) {
                        output.append(" **•** ")
                                .append(formatLaTeX(li.text()))
                                .append("\n");
                    }
                    output.append("\n");
                }
                case "div" -> {
                    if (eachElement.className().equals("input")) {
                        output.append("\n\nInput : \n```\n");
                        Element pre = eachElement.getElementsByTag("pre").first();
                        output.append(pre.html().replace("<br>", "\n"))
                                .append("\n```");
                    }
                    if (eachElement.className().equals("output")) {
                        output.append("\nOutput : \n```\n");
                        Element pre = eachElement.getElementsByTag("pre").first();
                        output.append(pre.text())
                                .append("\n```");
                    }
                }
            }

            if(output.length()>1500)
            {
                sendMessage(output.toString());
                output.setLength(0);
            }
        }

        return output;
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

    public String searchForImage(Element element)
    {
        Element img = element.getElementsByTag("img").first();
        if(img==null)
            return null;
        else
        {
            return img.attr("src");
        }
    }

    boolean firstMessage = true;
    public void sendMessage(String message)
    {
        if(message.length()>0)
        {
            if(firstMessage)
            {
                msg.editMessage(message).queue();
                firstMessage = false;
            }
            else
            {
                this.channel.sendMessage(message).queue();
            }
        }
    }


    public PageLoadStatus loadFilteredPage(String[] args)
    {
       try
       {
           return loadFilteredPage(args, 0);
       }
       catch (Exception e)
       {
           e.printStackTrace();
           return new PageLoadStatus(e.getLocalizedMessage());
       }
    }

    public PageLoadStatus loadFilteredPage(String[] args, int pageNo) throws Exception
    {
        if(args.length == 1)
        {
            loadCodeForcesDefaultProblemSite();
            return new PageLoadStatus(true);
        }

        String order = "";
        StringBuilder tags = new StringBuilder();
        for(int i=1;i<args.length;i+=2)
        {
            if(i == (args.length - 1))
            {
                if(args[i].equals("-p") || args[i].equals("-d") || args[i].equals("-t") || args[i].equals("-o"))
                    return new PageLoadStatus("Invalid argument usage. Check `cp!help`");
            }

            switch (args[i]) {
                case "-p"-> {
                    String pgNo = args[i + 1];
                    pageNo = Integer.parseInt(pgNo);
                    if (pageNo < 1)
                        return new PageLoadStatus("Page no 0 isnt possible :/");
                }
                case "-d"-> {
                    if (args[i + 1].contains("-")) {
                        String[] difficulty = args[i + 1].split("-");
                        if (difficulty.length == 2) {
                            try {
                                int minDiff = Integer.parseInt(difficulty[0]);
                                int maxDiff = Integer.parseInt(difficulty[1]);
                                if (minDiff > maxDiff)
                                    return new PageLoadStatus("Min Diff cant be greater than Max diff :/");
                                else
                                    tags.append(difficulty[0]).append("-").append(difficulty[1]).append(",");
                            } catch (NumberFormatException e) {
                                return new PageLoadStatus("Provide a number when using -d argument -_-");
                            }
                        } else {
                            return new PageLoadStatus("Incorrect `-d` argument usage. Check `cp!help`");
                        }
                    } else {
                        try {
                            int diff = Integer.parseInt(args[i + 1]);
                            tags.append(diff).append("-").append(diff).append(",");
                        } catch (NumberFormatException e) {
                            return new PageLoadStatus("Difficulty should be a number");
                        }
                    }
                }
                case "-t"-> {
                    if (args[i + 1].startsWith("\"")) {
                        if (args[i + 1].endsWith("\"")) {
                            String finalS = args[i + 1].replace(" ", "%20");
                            tags.append(finalS, 1, finalS.lastIndexOf('"'));
                        } else {
                            StringBuilder fulltags = new StringBuilder(args[i + 1].substring(1));
                            boolean quoteFound = false;
                            for (int j = i + 2; j < args.length; j++) {
                                if (args[j].indexOf('"') > -1) {
                                    quoteFound = true;
                                    fulltags.append("%20").append(args[j], 0, args[j].indexOf('"'));
                                    break;
                                }
                            }
                            if (quoteFound)
                                tags.append(fulltags.toString().replace(" ", "%20")).append(",");
                            else
                                return new PageLoadStatus("You forgot to close double quotes!");
                        }
                    } else {
                        return new PageLoadStatus("You need to enclose tags with double quotes");
                    }
                }
                case "-o"-> {
                    if (args[0].equals("_random"))
                        return new PageLoadStatus("`cp!random` doesn't support `-o` argument.");

                    try {
                        order = switch(args[i+1]) {
                            case "diff-asc"-> "?order=BY_RATING_ASC&";
                            case "diff-des"-> "?order=BY_RATING_DESC&";
                            case "solv-asc"-> "?order=BY_SOLVED_ASC&";
                            case "solv-des"-> "?order=BY_SOLVED_DESC&";
                            default -> throw new IllegalStateException();
                        };
                    } catch (IllegalStateException e) {
                        return new PageLoadStatus("Incorrect `-o` argument usage. Check `cp!help`.");
                    }
                }
            }
        }

        if(pageNo==0)
        {
            document = Jsoup.connect("https://codeforces.com/problemset"+order+"?tags="+tags.toString()).get();
        }
        else
        {
            document = Jsoup.connect("https://codeforces.com/problemset/page/"+pageNo+order+"?tags="+tags.toString()).get();
        }

        return new PageLoadStatus(true);
    }
}
