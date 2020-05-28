# CPBot

A Discord bot for fetching Competitive Programming Problems from Code Forces.  
Support for other websites coming soon.

####[Invite Link to add Bot to your server](https://discord.com/api/oauth2/authorize?client_id=711231840484065280&permissions=76800&scope=bot)

You can build it and run your own instance as well, but you need to have a [Discord Bot Token](https://www.writebots.com/discord-bot-token/).
## Build
**Make sure you have Maven 3.6.0+ and Java 14+**

1. Run `mvn clean install exec:java` and select option 2 from menu to set your Discord Bot Token.

2. Run `mvn exec:java` and select option 1 from menu to start the bot.

**You can also start the bot with `mvn exec:java -Dexec.args="-auto-start"` on start up, without facing the menu.**

## Available Commands
* `cp!random` : Gets a random problem
* `cp!list` : Returns a table of available problems to solve
* `cp!get` : Fetches details about a specific problem
* `cp!about` : Gets details of the bot

### `_random`
Optional Arguments :
* `-t "{Tag1},{Tag2},{Tag3},...,{TagN}"` : Allows user to add preferred tags
* `-d {Difficulty Min}-{Difficulty-Max}` : Allows user to set a difficulty range
* `-d {Difficulty}` : Allows user to se a specific difficulty level

### `_list`
Optional Arguments :
* `-t "{Tag1},{Tag2},{Tag3},...,{TagN}"` : Allows user to add preferred tags
* `-o {order}` : Sets the order of sorting of the list  
    `{order}` can be
    * `diff-asc` : Ascending difficulty 
    * `diff-des` : Descending difficulty
    * `solv-asc` : Ascending no. of solved
    * `solv-des` : Descending no. of solved
* `-d {Difficulty Min}-{Difficulty-Max}` : Allows user to set a difficulty range
* `-d {Difficulty}` : Allows user to se a specific difficulty level

### `_get`
Required Argument :
* `{Problem ID}` : Problem ID of the particular problem

### `_about`
Get details of the bot.

## Examples
`cp!list -d 2000 -o diff-asc`
`cp!random -d 800-1200`  
`cp!get 1A`  

## Libraries
* [JDA](https://github.com/DV8FromTheWorld/JDA) - Java Discord API
* [JSoup](https://jsoup.org/) - For Webscraping from Code Forces

## License
[GNU GPL v3 License](https://github.com/dubbadhar/CPBot/blob/master/LICENSE)




