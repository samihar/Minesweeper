import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * Samiha Rahman 
 * Mar 20, 2016 
 * Period 4 
 * Time taken: 6 hours
 *
 * REFLECTION This assignment was kind of difficult. This first 1.5 hours was
 * spent actually debugging my original Minesweeper, which didn't work when the
 * player wanted to change number of mines or start a new game. I spent block
 * day trying to fix it, and eventually solved it by the end of lunch. I was
 * initially a little confused as to where to start with autoplay class. I
 * started with a frame for the state machine logic, then created the methods
 * (guess, flag, reveal) and printed out stuff when they were called to check if
 * pressing 'a' worked. After that, I worked on each of the separate methods
 * (Guess,flag, reveal). Guess was very straightforward initially (although I
 * added the extra part to choose from unrevealed cells later when I realized
 * that just random choosing might not do anything). The flagging took a little
 * longer. I had to use several separate boolean methods to make the code
 * shorter. One of the methods were a little long because I wanted to check each
 * of the corners separately and use four of the same for loops. I used a
 * similar structure for the reveal. This time, I had to also look for the cells
 * with numbers as I only wanted to look at the cells around those ones. I think
 * because of this, although it is not looking at every single cell, caused my
 * reveal to take so long, but I'm not sure how to make it faster without
 * messing up the format.
 */

public class P4_Rahman_Samiha_Minesweeper_AI {
	private int mode;
	private char[][] board;
	private P4_Rahman_Samiha_MinesweeperGUIController controller;
	private static int GUESSING = 1;
	private static int FLAGGING = 2;
	private static int REVEALING = 3;

	public P4_Rahman_Samiha_Minesweeper_AI(P4_Rahman_Samiha_MinesweeperGUIController c) {
		mode = GUESSING;
		controller = c;
		board = controller.getModel().getTop();
	}

	public void reset() {
		mode = GUESSING;
		board = controller.getModel().getTop();
	}

	public void autoplay() {
		if (mode == GUESSING) {
			guess();
			mode = FLAGGING;
		} else if (mode == FLAGGING) {
			if (flag() > 0)
				mode = REVEALING;
			else
				mode = GUESSING;
		} else if (mode == REVEALING) {
			if (reveal() > 0)
				mode = FLAGGING;
			else
				mode = GUESSING;
		}
	}

	public void guess() {
		Random r = new Random();
		int p = r.nextInt(getUnrevealedPositions().size());
		p = r.nextInt(getUnrevealedPositions().size());
		int[] pos = getUnrevealedPositions().get(p);
		controller.getModel().revealPos(pos[0], pos[1]);
		update();
	}

	public ArrayList<int[]> getUnrevealedPositions() {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == controller.getModel().getUnrevealedCell()) {
					int[] pos = { i, j };
					positions.add(pos);
				}
			}
		}
		return positions;
	}

	public void update() {
		controller.getView().topToPic(controller.getModel().getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
				P4_Rahman_Samiha_MinesweeperModel.getMineDeath(), P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
				P4_Rahman_Samiha_MinesweeperModel.getFlag(), P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(),
				controller.getNums());
		board = controller.getModel().getTop();
		controller.getView().getBoard().repaint();
	}

	public int flag() {
		int count = 0;
		ArrayList<int[]> cells = getUnrevealedPositions();
		for (int i = 0; i < cells.size(); i++) {
			int[] pos = cells.get(i);
			if (mustBeMine(pos[0], pos[1])) {
				controller.getModel().flagPos(pos[0], pos[1]);
				count++;
				update();
			}
		}
		return count;
	}

	public boolean mustBeMine(int i, int j) {
		return isSurrounded(i, j) || isCorner(i, j);
	}

	public boolean isSurrounded(int i, int j) {
		int[] h = { 0, 1, 1, 1, 0, -1, -1, -1 };
		int[] v = { 1, 1, 0, -1, -1, -1, 0, 1 };
		for (int k = 0; k < h.length; k++) {
			if (i + v[k] >= 0 && i + v[k] < board.length && j + h[k] >= 0 && j + h[k] < board[0].length) {
				if (!isNum(i + v[k], j + h[k])) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isCorner(int i, int j) {
		int[] h = { 0, 1, 1, 1, 0, -1, -1, -1 };
		int[] v = { 1, 1, 0, -1, -1, -1, 0, 1 };
		int count = 0;
		for (int k = 0; k <= 2; k++) {
			if (i + v[k] >= 0 && i + v[k] < board.length && j + h[k] >= 0 && j + h[k] < board[0].length) {
				if (isNum(i + v[k], j + h[k])) {
					count++;
				}
			}
		}
		if (count == 3)
			return true;
		count = 0;
		for (int k = 2; k <= 4; k++) {
			if (i + v[k] >= 0 && i + v[k] < board.length && j + h[k] >= 0 && j + h[k] < board[0].length) {
				if (isNum(i + v[k], j + h[k])) {
					count++;
				}
			}
		}
		if (count == 3)
			return true;
		count = 0;
		for (int k = 4; k <= 6; k++) {
			if (i + v[k] >= 0 && i + v[k] < board.length && j + h[k] >= 0 && j + h[k] < board[0].length) {
				if (isNum(i + v[k], j + h[k])) {
					count++;
				}
			}
		}
		if (count == 3)
			return true;
		count = 0;
		for (int k = 6; k <= 8; k++) {
			if (k == 8)
				k = 0;
			if (i + v[k] >= 0 && i + v[k] < board.length && j + h[k] >= 0 && j + h[k] < board[0].length) {
				if (isNum(i + v[k], j + h[k])) {
					count++;

				}
			}
			if (k == 0)
				k = 8;
		}
		if (count == 3)
			return true;
		return false;

	}

	public boolean isNum(int i, int j) {
		char cell = board[i][j];
		return cell == '1' || cell == '2' || cell == '3' || cell == '4' || cell == '5' || cell == '6' || cell == '7'
				|| cell == '8';
	}

	public int reveal() {
		int count = 0;
		ArrayList<int[]> numbered = getNumberedCells();
		for (int i = 0; i < numbered.size(); i++) {
			int[] pos = numbered.get(i);
			int num = (int) board[pos[0]][pos[1]] - (int) ('0');
			if (alreadyTouchingMines(pos[0], pos[1], num)) {
				count += revealAround(pos[0], pos[1]);
			}
		}
		return count;
	}

	public int revealAround(int i, int j) {
		int[] h = { 0, 1, 1, 1, 0, -1, -1, -1 };
		int[] v = { 1, 1, 0, -1, -1, -1, 0, 1 };
		int count = 0;
		for (int k = 0; k < h.length; k++) {
			if (i + v[k] >= 0 && i + v[k] < board.length && j + h[k] >= 0 && j + h[k] < board[0].length) {
				controller.getModel().revealPos(i + v[k], j + h[k]);
				update();
				count++;
			}
		}
		return count;
	}

	public ArrayList<int[]> getNumberedCells() {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				int num = (int) board[i][j] - (int) ('0');
				if (num > 0 && num < 9) {
					int[] pos = { i, j };
					positions.add(pos);
				}
			}
		}
		return positions;
	}

	public boolean alreadyTouchingMines(int i, int j, int num) {
		int[] h = { 0, 1, 1, 1, 0, -1, -1, -1 };
		int[] v = { 1, 1, 0, -1, -1, -1, 0, 1 };
		int count = 0;
		for (int k = 0; k < h.length; k++) {
			if (i + v[k] >= 0 && i + v[k] < board.length && j + h[k] >= 0 && j + h[k] < board[0].length) {
				if (board[i + v[k]][j + h[k]] == controller.getModel().getFlag()) {
					count++;
				}
			}
		}
		return count == num;
	}

}
