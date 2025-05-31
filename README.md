CCLC is a mod that adds compatibility between the ComputerCraft and Lightman's Currency mods, allowing you to make programs that integrate currency.

Requirements:
Lightman's Currency

CC:Tweaked

(CC:Tweaked only receives updates through Modrinth. CCLC tries to target the latest version of CC:Tweaked, though should be compatible with older versions)

 

What it does:
=======

It adds a block called the Trade Link, craftable with a few LC blocks and gold, that you can connect to a CC: Tweaked computer (via wired modem or direct connection) in order to unlock new functions in your programs.

How do I use it?
=======

Interacting with a Trade Link allows you to open its GUI, which contains many slots to input coins into, along with an ATM Card, and will display the current balance it contains. When connecting it to a Computer, you can access it just as any other peripheral, with the ID cclc:cardreader, in order to gain access to its Lua functions.

Currently Implemented Functions
=======

GetBalance() - Allows getting the current balance in the Trade Link, in a formatted String value
GetNumericalBalance() - Allows getting the current balance in the Trade Link, in a direct numerical value
getAllAccounts() - Returns a List of Strings, formatted to show an account by name followed by a constant ID to access it with
payAccount(int accountID, int amount) - Allows withdrawing currency from the Trade Link to send to a player or team's account. The Trade Link automatically transfers coins/gives change when needed.
 

Future Plans
=======

In the future, there are plans for:

Turtles being able to use the Trade Link
Pocket Computers being able to use the Trade Link
Allowing access and function to pay for trades/transactions available through trade networks
Variant selection using the LC Variant Wand

Questions, Bugs, or Suggestions?
=======

Feel free to comment or open issues in the Github (Here)! Feedback's always welcome!
