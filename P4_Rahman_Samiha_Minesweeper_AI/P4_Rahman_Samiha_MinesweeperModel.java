import java.util.Random;

public class P4_Rahman_Samiha_MinesweeperModel {
	private char[][] top;
	private char[][] bottom;
	private boolean playing;
	private boolean started;
	private boolean win;
	private int numMines;
	private static final int TOP_LAYER = 1;
	private static final int BOTTOM_LAYER = 2;
	private static final char FLAG = 'F';
	private static final char QUESTION_MARK = '?';
	private static final char MINE = '*';
	private static final char MINE_DEATH = '#';
	private static final char BLANK = ' ';
	private static final char UNREVEALED_CELL = '-';
	private static final int ROWS = 20;
	private static final int COLS = 20;

	public P4_Rahman_Samiha_MinesweeperModel() {
		top = makeBoard(TOP_LAYER);
		bottom = makeBoard(BOTTOM_LAYER);
		playing = true;
		started = false;
		win = false;
	}

	public char flagPos(int r, int c) {
		if (top[r][c] == UNREVEALED_CELL) {
			top[r][c] = FLAG;
			if (allClear()) {
				playing = false;
				win = true;
			}
			return FLAG;
		} else if (top[r][c] == FLAG) {
			top[r][c] = QUESTION_MARK;
			if (allClear()) {
				playing = false;
				win = true;
			}
			return QUESTION_MARK;
		} else if (top[r][c] == QUESTION_MARK) {
			top[r][c] = UNREVEALED_CELL;
			if (allClear()) {
				playing = false;
				win = true;
			}
			return UNREVEALED_CELL;
		}
		return ' ';
	}

	public char revealPos(int r, int c) {
		if (top[r][c] == UNREVEALED_CELL) {
			top[r][c] = bottom[r][c];
			if (bottom[r][c] == MINE) {
				playing = false;
			} else if (bottom[r][c] == BLANK) {
				int[] vertical = { 1, 1, 0, -1, -1, -1, 0, 1 };
				int[] horizontal = { 0, 1, 1, 1, 0, -1, -1, -1 };
				for (int i = 0; i < 8; i++) {
					if (isInBounds(r + vertical[i], c + horizontal[i]))
						revealPos(r + vertical[i], c + horizontal[i]);

				}
			}
		} else {
			return UNREVEALED_CELL;
		}
		if (allClear()) {
			playing = false;
			win = true;
		}
		return bottom[r][c];
	}

	public boolean allClear() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if ((top[i][j] == UNREVEALED_CELL || top[i][j] == FLAG) && bottom[i][j] != MINE)
					return false;
			}
		}
		return true;
	}

	public boolean isInBounds(int i, int j) {
		return i >= 0 && j >= 0 && i < ROWS && j < COLS && bottom[i][j] != MINE;
	}

	public void resetGame() {
		top = makeBoard(TOP_LAYER);
		bottom = makeBoard(BOTTOM_LAYER);
		playing = true;
		started = false;
	}

	public char[][] makeBoard(int type) {
		char[][] board = new char[ROWS][COLS];
		if (type == TOP_LAYER) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					board[i][j] = UNREVEALED_CELL;
				}
			}
		} else if (type == BOTTOM_LAYER) {
			board = addMines(board);
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					if (board[i][j] != MINE) {
						int neighbors = getNeighboringMines(board, i, j);
						if (neighbors == 0) {
							board[i][j] = BLANK;
						} else {
							board[i][j] = Integer.toString(neighbors).charAt(0);
						}
					}
				}
			}
		}
		return board;
	}

	public char[][] addMines(char[][] board) {
		Random ran = new Random();
		int minesLeft = numMines;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (minesLeft > 0 && ran.nextInt(ROWS * COLS) == ROWS) {
					board[i][j] = MINE;
					minesLeft--;
				}
			}
		}
		while (minesLeft > 0) {
			int i = ran.nextInt(ROWS);
			int j = ran.nextInt(COLS);
			while (board[i][j] == MINE) {
				i = ran.nextInt(ROWS);
				j = ran.nextInt(COLS);
			}
			board[i][j] = MINE;
			minesLeft--;
		}
		// System.out.println("Number mines: " + numMines);
		return board;
	}

	public int getNeighboringMines(char[][] board, int ipos, int jpos) {
		int[] pos = { ipos, jpos };
		int count = 0;
		int[] vertical = { -1, -1, -1, 0, 0, 1, 1, 1 };
		int[] horizontal = { -1, 0, 1, 1, -1, -1, 0, 1 };
		for (int i = 0; i < 8; i++) {
			if (pos[0] + vertical[i] >= 0 && pos[0] + vertical[i] < board.length) {
				if (pos[1] + horizontal[i] >= 0 && pos[1] + horizontal[i] < board[0].length) {
					if (board[pos[0] + vertical[i]][pos[1] + horizontal[i]] == MINE) {
						count++;
					}
				}
			}
		}
		return count;
	}

	public void makeWinningBoard() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (bottom[i][j] == MINE) {
					top[i][j] = FLAG;
				} else{
					bottom[i][j] = top[i][j];
				}
			}
		}
	}
	
	public void makeLosingBoard() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (bottom[i][j] == MINE) {
					top[i][j] = MINE_DEATH;
				} 
			}
		}
	}

	public char[][] getTop() {
		return top;
	}

	public void setTop(char[][] top) {
		this.top = top;
	}

	public char[][] getBottom() {
		return bottom;
	}

	public void setBottom(char[][] bottom) {
		this.bottom = bottom;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	public int getNumMines() {
		return numMines;
	}

	public void setNumMines(int numMines) {
		this.numMines = numMines;
	}

	public static int getTopLayer() {
		return TOP_LAYER;
	}

	public static int getBottomLayer() {
		return BOTTOM_LAYER;
	}

	public static char getFlag() {
		return FLAG;
	}

	public static char getQuestionMark() {
		return QUESTION_MARK;
	}

	public static char getMine() {
		return MINE;
	}

	public static char getMineDeath() {
		return MINE_DEATH;
	}

	public static char getBlank() {
		return BLANK;
	}

	public static char getUnrevealedCell() {
		return UNREVEALED_CELL;
	}

	public static int getRows() {
		return ROWS;
	}

	public static int getCols() {
		return COLS;
	}

}
