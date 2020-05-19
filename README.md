# CPBot

A Discord bot for fetching Competitive Programming Problems from Codechef
Support for other websites coming soon.

Current version : 1.3

Built with Java 

## Available Commands
* `_random` : Gets a random problem
* `_list` : Returns a table of available problems to solve
* `_get` : Fetches details about a specific problem
* `_about` : Gets details of the bot

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
Gets details of the bot

## License
[GNU GPL v3 License](https://github.com/dubbadhar/CPBot/blob/master/LICENSE)




