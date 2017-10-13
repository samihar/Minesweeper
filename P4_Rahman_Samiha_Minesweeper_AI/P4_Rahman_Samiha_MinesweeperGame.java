
/**
 * 
 * Samiha Rahman 
 * Mar 14, 2016 
 * Period 4 
 * Time taken: 6 hours (2 yesterday, 4
 * today)
 * 
 * REFLECTION This assignment was difficult mostly for the reason that it
 * required a lot of organization since there were quite a few variables AND
 * class to keep track of. The day before (2 hours) I had gotten the game itself
 * to work, with the board's recursion working correctly. It essentially worked
 * up till the timer, which, while it was working, I wanted to start only when
 * the player hits its first cell. Today, I figured out how to do that, as well
 * as added all the other components, such as making all the menu items worked
 * properly. For the flagging option, I just modified my original flagPos()
 * method to essentially "cycle" through the right-click options. This all took
 * about 3 hours. The last hours went to cleaning up my code and debugging minor
 * errors (every once in a while (~1 in 11 times) the player "won" when touching
 * a mine. Organizing the methods and replacing hardcoded areas as well as
 * static/non-static issues helped me fix those errors.
 *
 * 
 * 
 * 
 */
public class P4_Rahman_Samiha_MinesweeperGame {
	public static void main(String[] args) {
		P4_Rahman_Samiha_MinesweeperGUIController game = new P4_Rahman_Samiha_MinesweeperGUIController();
		game.play();
	}
}
