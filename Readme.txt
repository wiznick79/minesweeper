Minesweeper clone by Nikolaos Perris (nperris@gmail.com), (c) 2019
Git repository : github.com/wiznick79/minesweeper/

08/10/2019 v0.99
- When you use the built-in cheat or you repeat a gameboard, your score cannot count as a high score.

23/07/2019 v0.98
- Added the option to repeat a gameboard

20/07/2019 v0.97
- Added a 'cheat'

14/07/2019 v0.96
- Some visual improvements
- In custom game mode, changed the input TextFields to ComboBoxes. The minimum number of mines that can be selected
  is 5% of the number of tiles. The maximum number is 50%.

12/07/2019 v0.95
- Added pause button. Gameboard is disabled and hidden during pause.
- Added option to enable/disable question marks in the options menu. By default, they are disabled.

08/07/2019 v0.93
- Minor improvements
- Help file added

06/07/2019 v0.92
- Minor improvements
- Smaller tile size and game window when screen resolution is low
- Reset scores option added on scores window

05/07/2019 v0.91
- Added a game over popup window

04/07/2019 v0.90
- Winning condition bug fixed (I hope!)

03/07/2019 v0.87
- Added question mark system on 2nd right click (->qmark->question mark->none)
- Added double-click on open tiles to open its adjacent tiles, if there are equal flagged adjacent tiles.
  eg. if you double-click on a '2' and there are 2 flagged adjacent tiles, it will open the non-flagged adjacent tiles.
  If the flagged tiles are wrong you are going to lose!
- Minor fixes

02/07/2019 v0.85
- Added best scores, which are saved in a simple text file and loaded everytime the game starts
- Added a menu in the main window
- Minor changes

01/07/2019 v0.82
- Added qmark system so the player can mark the mines and make the tile unclickable
- Added a timer and a mines counter

30/06/2019 v0.80
- Working version with basic gameplay features implemented

TODO List:
- add sounds
- add animations
