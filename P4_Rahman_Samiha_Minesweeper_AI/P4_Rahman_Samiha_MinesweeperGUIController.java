import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Timer;

public class P4_Rahman_Samiha_MinesweeperGUIController implements P4Rahman_Samiha_MS_ControllerInterface{
	private P4_Rahman_Samiha_MinesweeperModel model;
	private P4_Rahman_Samiha_MinesweeperGUIView view;
	P4_Rahman_Samiha_Minesweeper_AI auto;
	
	Timer timer;
	boolean firstTime;
	MyMouseAdapter adapter;
	private char[] nums = { ' ', '1', '2', '3', '4', '5', '6', '7', '8' };

	

	public P4_Rahman_Samiha_MinesweeperGUIController() {
		model = new P4_Rahman_Samiha_MinesweeperModel();
		view = new P4_Rahman_Samiha_MinesweeperGUIView();
		auto = new P4_Rahman_Samiha_Minesweeper_AI(this);
		timer = new Timer(1000, null);
		firstTime = true;
		adapter = new MyMouseAdapter();
		timer.addActionListener(new MyActionListener());
		view.getMntmNewGame().addActionListener(new MyActionListener());
		view.getMntmExit().addActionListener(new MyActionListener());
		view.getMntmTotalMines().addActionListener(new MyActionListener());
		view.getMntmHowToPlay().addActionListener(new MyActionListener());
		view.getMntmAbout().addActionListener(new MyActionListener());
		view.getBoard().addMouseListener(adapter);
		view.addKeyListener(new MyKeyAdapter());

	}

	public void play() {
		// model.setNumMines(view.askNumMines(P4_Rahman_Samiha_MinesweeperModel.getRows()));
		if (firstTime) {
			model.setNumMines(P4_Rahman_Samiha_MinesweeperModel.getRows());
		}
		view.setMinesText(model.getNumMines());
		view.getMines().setText(Integer.toString(view.getMinesText()));
		model.setBottom(model.makeBoard(P4_Rahman_Samiha_MinesweeperModel.getBottomLayer()));
		view.getBoard().repaint();
	}
	
	public char[] getNums() {
		return nums;
	}
	
	public P4_Rahman_Samiha_MinesweeperModel getModel(){
		return model;
	}
	
	public P4_Rahman_Samiha_MinesweeperGUIView getView(){
		return view;
	}

	public void restartGame() {
		model.resetGame();
		view.setTimeText(0);
		view.getTime().setText(Integer.toString(view.getTimeText()));
		view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
				P4_Rahman_Samiha_MinesweeperModel.getMineDeath(), P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
				P4_Rahman_Samiha_MinesweeperModel.getFlag(), P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
		view.getBoard().repaint();
		auto.reset();
		play();
	}
	
	private class MyKeyAdapter extends KeyAdapter{
		
		public void keyReleased(KeyEvent e){
			
			if (e.getKeyChar()== 'a'){
				if (!model.isStarted()) {
					model.setStarted(true);
					firstTime = false;
					timer.start();
				}
				auto.autoplay();
				view.getBoard().repaint();
				if (model.isStarted() && !model.isPlaying()) {
					timer.stop();
					if (!model.isWin()) {
						model.makeLosingBoard();
						view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
								P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
								P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
								P4_Rahman_Samiha_MinesweeperModel.getFlag(),
								P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
						view.getBoard().repaint();
						view.printEndMessage(false);
					} else if (model.isWin()) {
						model.makeWinningBoard();
						view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
								P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
								P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
								P4_Rahman_Samiha_MinesweeperModel.getFlag(),
								P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
						view.getBoard().repaint();
						view.printEndMessage(true);
					}
					view.getBoard().repaint();
					if (view.playAgain())
						restartGame();
					else
						System.exit(0);
				}
			}
			
		}		
	}
	
	private class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == null) {
				if (view.getTimeText() < 999)
					view.setTimeText(view.getTimeText() + 1);
				view.getTime().setText(Integer.toString(view.getTimeText()));
			}
			if (e.getActionCommand() != null) {
				if (e.getActionCommand().equals("New Game")) {
					timer.stop();
					model.setPlaying(false);
					restartGame();
				}
				if (e.getActionCommand().equals("Exit")) {
					System.exit(0);
				}
				if (e.getActionCommand().equals("Total Mines")) {
					model.setPlaying(false);
					timer.stop();
					model.setNumMines(view.askNumMines(model.getNumMines()));
					view.setMinesText(model.getNumMines());
					restartGame();
				}
				if (e.getActionCommand().equals("How To Play")) {
					view.showHelp();
				}
				if (e.getActionCommand().equals("About")) {
					view.showAbout();
				}
			}
		}

	}

	private class MyMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getX() < 0 || e.getY() < 0 || e.getX() > 400 || e.getY() > 400) {
				;
			} else {
				if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
					if (!model.isStarted()) {
						model.setStarted(true);
						firstTime = false;
						timer.start();
					}
					int i = (e.getX() - e.getX() % 20) / 20;
					int j = (e.getY() - e.getY() % 20) / 20;
					model.revealPos(j, i);
					view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
							P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
							P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
							P4_Rahman_Samiha_MinesweeperModel.getFlag(),
							P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
					view.getBoard().repaint();
				} else if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {
					int i = (e.getX() - e.getX() % 20) / 20;
					int j = (e.getY() - e.getY() % 20) / 20;
					char label = model.flagPos(j, i);
					if (label == P4_Rahman_Samiha_MinesweeperModel.getFlag()) {
						view.setMinesText(view.getMinesText() - 1);
						view.getMines().setText(Integer.toString(view.getMinesText()));
					} else if (label == P4_Rahman_Samiha_MinesweeperModel.getQuestionMark()) {
						view.setMinesText(view.getMinesText() + 1);
						view.getMines().setText(Integer.toString(view.getMinesText()));
					}
					view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
							P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
							P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
							P4_Rahman_Samiha_MinesweeperModel.getFlag(),
							P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
					view.getBoard().repaint();
				}
			}
			if (model.isStarted() && !model.isPlaying()) {
				timer.stop();
				if (!model.isWin()) {
					model.makeLosingBoard();
					view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
							P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
							P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
							P4_Rahman_Samiha_MinesweeperModel.getFlag(),
							P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
					view.getBoard().repaint();
					view.printEndMessage(false);
				} else if (model.isWin()) {
					model.makeWinningBoard();
					view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
							P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
							P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
							P4_Rahman_Samiha_MinesweeperModel.getFlag(),
							P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
					view.getBoard().repaint();
					view.printEndMessage(true);
				}
				view.getBoard().repaint();
				if (view.playAgain())
					restartGame();
				else
					System.exit(0);
			}
		}

		public void mousePressed(MouseEvent e) {
			view.requestFocus();
			if (e.getX() < 0 || e.getY() < 0 || e.getX() > 400 || e.getY() > 400) {
				;
			} else {
				if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
					if (!model.isStarted()) {
						model.setStarted(true);
						firstTime = false;
						timer.start();
					}
					int i = (e.getX() - e.getX() % 20) / 20;
					int j = (e.getY() - e.getY() % 20) / 20;
					model.revealPos(j, i);
					view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
							P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
							P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
							P4_Rahman_Samiha_MinesweeperModel.getFlag(),
							P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
					view.getBoard().repaint();
				} else if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {
					int i = (e.getX() - e.getX() % 20) / 20;
					int j = (e.getY() - e.getY() % 20) / 20;
					char label = model.flagPos(j, i);
					if (label == P4_Rahman_Samiha_MinesweeperModel.getFlag()) {
						view.setMinesText(view.getMinesText() - 1);
						view.getMines().setText(Integer.toString(view.getMinesText()));
					} else if (label == P4_Rahman_Samiha_MinesweeperModel.getQuestionMark()) {
						view.setMinesText(view.getMinesText() + 1);
						view.getMines().setText(Integer.toString(view.getMinesText()));
					}
					view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
							P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
							P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
							P4_Rahman_Samiha_MinesweeperModel.getFlag(),
							P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
					view.getBoard().repaint();
				}
			}
			if (model.isStarted() && !model.isPlaying()) {
				timer.stop();
				if (!model.isWin()) {
					model.makeLosingBoard();
					view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
							P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
							P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
							P4_Rahman_Samiha_MinesweeperModel.getFlag(),
							P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
					view.getBoard().repaint();
					view.printEndMessage(false);
				} else if (model.isWin()) {
					model.makeWinningBoard();
					view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
							P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
							P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
							P4_Rahman_Samiha_MinesweeperModel.getFlag(),
							P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
					view.getBoard().repaint();
					view.printEndMessage(true);
				}
				view.getBoard().repaint();
				if (view.playAgain())
					restartGame();
				else
					System.exit(0);
			}

		}

		public void mouseReleased(MouseEvent e) {
			if (e.getX() < 0 || e.getY() < 0 || e.getX() > 400 || e.getY() > 400) {
				;
			} else {
				if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
					if (!model.isStarted()) {
						model.setStarted(true);
						firstTime = false;
						timer.start();
					}
					int i = (e.getX() - e.getX() % 20) / 20;
					int j = (e.getY() - e.getY() % 20) / 20;
					model.revealPos(j, i);
					view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
							P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
							P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
							P4_Rahman_Samiha_MinesweeperModel.getFlag(),
							P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
					view.getBoard().repaint();
				} else if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {
					int i = (e.getX() - e.getX() % 20) / 20;
					int j = (e.getY() - e.getY() % 20) / 20;
					char label = model.flagPos(j, i);
					if (label == P4_Rahman_Samiha_MinesweeperModel.getFlag()) {
						view.setMinesText(view.getMinesText() - 1);
						view.getMines().setText(Integer.toString(view.getMinesText()));
					} else if (label == P4_Rahman_Samiha_MinesweeperModel.getQuestionMark()) {
						view.setMinesText(view.getMinesText() + 1);
						view.getMines().setText(Integer.toString(view.getMinesText()));
					}
					view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
							P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
							P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
							P4_Rahman_Samiha_MinesweeperModel.getFlag(),
							P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
					view.getBoard().repaint();
				}
			}
			if (model.isStarted() && !model.isPlaying()) {
				timer.stop();
				if (!model.isWin()) {
					model.makeLosingBoard();
					view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
							P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
							P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
							P4_Rahman_Samiha_MinesweeperModel.getFlag(),
							P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
					view.getBoard().repaint();
					view.printEndMessage(false);
				} else if (model.isWin()) {
					model.makeWinningBoard();
					view.topToPic(model.getTop(), P4_Rahman_Samiha_MinesweeperModel.getMine(),
							P4_Rahman_Samiha_MinesweeperModel.getMineDeath(),
							P4_Rahman_Samiha_MinesweeperModel.getUnrevealedCell(),
							P4_Rahman_Samiha_MinesweeperModel.getFlag(),
							P4_Rahman_Samiha_MinesweeperModel.getQuestionMark(), nums);
					view.getBoard().repaint();
					view.printEndMessage(true);
				}
				view.getBoard().repaint();
				if (view.playAgain())
					restartGame();
				else
					System.exit(0);
			}

		}
	}
}
