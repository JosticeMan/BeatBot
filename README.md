# General Information

AP CSA Group Create A Application Project <br />
This repository will contain a Vertical Scrolling Rhythm Game that incorporates fighting aspects <br />

# BeatBot:

  <p> In the distant future, the world has been taken over by dancing robots. You are one of the few humans left in the world 
  that went into hiding following the robotic takeover. Your goal is to challenge these robots to fights and win.  
  Luckily, these robots allow their challengers to pick the beats, difficulty, and song, giving you the upperhand. 
  Don't let this advantage go to waste! You are one of the last hopes that humanity have to survive! </p>
  
# Development Teams:

Main Game: <br />

  - Tyler Overden <br />
  - Steven Li <br />
  - Justin Yau <br />
  
 Shop: <br />
 
  - Kevin Lin <br />
  - Daniel Dong <br />

Graphics: <br />

  - Andrew Zhou <br />
  - Sunny Kwong <br />
  - Yonathan "Jeff" Ben-Reuven <br />

# Planning:

  For each map object, it will have a method that will read a certain text file for the moves. <br />
  
  VERSION 1 <br />
  "Song Name", BPM, Some integer for difficulty <br />
  "Column that the move will appear in", Some integer representing timeframe <br />

  Example: <br />
  "Counting Stars", 10, 1 - 1 for HARD <br />
  "LEFT", 10 - 10 for ms <br />
  
  <b> VERSION 2 AND THE FINAL VERSION </b> <br />
  
  Title: Hitroigoto <br />
  BPM: 165 <br />
  Arist: ClariS <br />
  Offset: 0 <br />

  0,0,1 - First number represents which lane the stroke spawns in, the second number represents when the stroke spawns, <br />
  and the last number represents how long the stroke should be held for since start of the game (NOT THE ACTUAL PRESS ITSELF)<br />
  
  <b> Interface Planning </b> <br /> 

  Instead of having placeholders per accuracy calculation, create a interface for STEVEN to implement <br /> 
  that contains methods to determine the accuracy based on the specified parameters. <br />
  
  determineAccuracy(stroke); <br />
  
  Determine accuracy takes in a stroke and then utilizes the properties of the stroke and current game properties to <br /> 
  to determine whether or not the player made a PERFECT, GREAT, GOOD, OKAY, or MISS stroke. <br />
  
  playerMiss(); <br />
  
  This method is called when the program already knows that the player missed without making the calculations in the other <br />
  accuracy method. Perform the calculations with the score and possibly update it on the screen. <br />
  
  Resizable Game Planning: <br /> 

  Make the constants private as the window properties may be subjected to change per game...? <br />
  
