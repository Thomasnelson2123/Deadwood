# Deadwood

----------SETUP------------

make sure the java files are compiled and that you provide an argument for the number of players when running the command.
if you get an exception when starting the game from the xml parser, make sure your xml files are in the correct location
and that you are running the game on a lab machine.
contact us at staleyc@wwu.edu and nelso343@wwu.edu if the game continues to not boot.

------BASIC GAME FLOW------

players take turns moving around the map, taking roles and acting to recieve money and credits, and upgrading their rank.
once a scene has had enough successful act actions taken on it, it is completed. When there is only 1 uncompleted scene left, advance to next day.
After 3 days, a winner is determined based on who has the most money/credits/rank.

---------COMMANDS------------

who - displays current player info

where - displays current player's locational info. includes roles and current scene data.

end - end current player's turn. can be entered if player has taken no actions.

move - move current player to adjacent room.

work - current player takes a role determined by user. can be done after having just moved.

act - if you have a role, attempt to remove a rehearsal chip and get rewarded if successful.

rehearse - if you have a role, adds one rehearsal chips to current player.

upgrade - if you are in the office, you may upgrade to a specified rank using credits or money
