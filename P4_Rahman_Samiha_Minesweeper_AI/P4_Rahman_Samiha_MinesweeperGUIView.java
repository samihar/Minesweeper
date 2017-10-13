import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class P4_Rahman_Samiha_MinesweeperGUIView extends JFrame {
	private JPanel contentPane;
	private BufferedImage[][] pic;
	private JPanel board;
	private JLabel time;
	private int timeText;
	private JLabel mines;
	private int minesText;
	private JMenuItem mntmNewGame;
	private JMenuItem mntmExit;
	private JMenuItem mntmTotalMines;
	private JMenuItem mntmHowToPlay;
	private JMenuItem mntmAbout;

	public P4_Rahman_Samiha_MinesweeperGUIView() {
		this.setTitle("Minesweeper");
		this.setBounds(100, 100, 600, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 550);
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu mnGame = new JMenu("Game");
		menuBar.add(mnGame);

		mntmNewGame = new JMenuItem("New Game");
		mnGame.add(mntmNewGame);

		mntmExit = new JMenuItem("Exit");
		mnGame.add(mntmExit);

		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		mntmTotalMines = new JMenuItem("Total Mines");
		mnOptions.add(mntmTotalMines);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		mntmHowToPlay = new JMenuItem("How To Play");
		mnHelp.add(mntmHowToPlay);

		mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		board = new MyDrawingPanel();
		board.setPreferredSize(new Dimension(400, 400));
		contentPane.add(board);

		time = new JLabel();
		timeText = 0;
		time.setText(String.valueOf(timeText));
		time.setHorizontalAlignment(SwingConstants.CENTER);
		time.setBorder(new TitledBorder(null, "Time Elapsed", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		time.setPreferredSize(new Dimension(100, 60));
		contentPane.add(time);

		mines = new JLabel("0");
		minesText = 0;
		mines.setText(String.valueOf(minesText));
		mines.setHorizontalAlignment(SwingConstants.CENTER);
		mines.setBorder(new TitledBorder(null, "Mines", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		mines.setPreferredSize(new Dimension(100, 60));
		contentPane.add(mines);

		pic = new BufferedImage[20][20];
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				try {
					pic[i][j] = ImageIO.read(new File("blank.gif"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		this.setVisible(true);
	}

	public boolean playAgain() {
		int response = JOptionPane.showInternalOptionDialog(contentPane, "Want to  play again?", "Play Again?",
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
		if (response == JOptionPane.YES_OPTION) {
			return true;
		} else
			return false;
	}

	public void showHelp() {
		JEditorPane helpContent;
		try {
			helpContent = new JEditorPane(new URL("file:HowToPlay.html"));
			JScrollPane helpPane = new JScrollPane(helpContent);
			helpPane.setPreferredSize(new Dimension(600, 600));
			JOptionPane.showMessageDialog(null, helpPane, "How To Play", JOptionPane.PLAIN_MESSAGE, null);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showAbout() {
		JEditorPane helpContent;
		try {
			helpContent = new JEditorPane(new URL("file:About.html"));
			JScrollPane helpPane = new JScrollPane(helpContent);
			helpPane.setPreferredSize(new Dimension(200, 150));
			JOptionPane.showMessageDialog(null, helpPane, "About", JOptionPane.PLAIN_MESSAGE, null);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int askNumMines(int defaultMines) {
		int totalMines = -1;
		totalMines = Integer.valueOf((String) (JOptionPane.showInternalInputDialog(contentPane,
				"Enter number of mines to be hidden:", "Total Mines", JOptionPane.PLAIN_MESSAGE, null, null, defaultMines)));
		while (totalMines >= (pic.length * pic[0].length) || totalMines <= 0) {
			totalMines = Integer.valueOf((String) (JOptionPane.showInternalInputDialog(contentPane,
					"Invalid Input. Enter number of mines to be hidden:", "Total Mines", JOptionPane.PLAIN_MESSAGE,
					null, null, defaultMines)));
		}
		return totalMines;
	}

	public void topToPic(char[][] top, char bombR, char bombD, char unRevealed, char flag, char question, char[] nums) {
		for (int i = 0; i < top.length; i++) {
			for (int j = 0; j < top[0].length; j++) {
				BufferedImage img = null;
				try {
					if (top[i][j] == bombR) {
						img = ImageIO.read(new File("bomb_revealed.gif"));
					} else if (top[i][j] == bombD) {
						img = ImageIO.read(new File("bomb_death.gif"));
					} else if (top[i][j] == unRevealed) {
						img = ImageIO.read(new File("blank.gif"));
					} else if (top[i][j] == flag) {
						img = ImageIO.read(new File("bomb_flagged.gif"));
					} else if (top[i][j] == question) {
						img = ImageIO.read(new File("bomb_question.gif"));
					} else if (top[i][j] == nums[0]) {
						img = ImageIO.read(new File("num_0.gif"));
					} else if (top[i][j] == nums[1]) {
						img = ImageIO.read(new File("num_1.gif"));
					} else if (top[i][j] == nums[2]) {
						img = ImageIO.read(new File("num_2.gif"));
					} else if (top[i][j] == nums[3]) {
						img = ImageIO.read(new File("num_3.gif"));
					} else if (top[i][j] == nums[4]) {
						img = ImageIO.read(new File("num_4.gif"));
					} else if (top[i][j] == nums[5]) {
						img = ImageIO.read(new File("num_5.gif"));
					} else if (top[i][j] == nums[6]) {
						img = ImageIO.read(new File("num_6.gif"));
					} else if (top[i][j] == nums[7]) {
						img = ImageIO.read(new File("num_7.gif"));
					} else if (top[i][j] == nums[8]) {
						img = ImageIO.read(new File("num_8.gif"));
					}
					pic[i][j] = img;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void printEndMessage(boolean wonGame) {
		if (wonGame) {
			JOptionPane.showInternalMessageDialog(contentPane, "Congratulations, you won!", "Congratulations!",
					JOptionPane.PLAIN_MESSAGE);
		} else {
			JOptionPane.showInternalMessageDialog(contentPane, "Sorry, you lost.", "Sorry", JOptionPane.PLAIN_MESSAGE);
		}
	}

	private class MyDrawingPanel extends JPanel {

		// Not required, but gets rid of the serialVersionUID warning. Google
		// it, if desired.
		static final long serialVersionUID = 1234567890L;

		public void paintComponent(Graphics g) {

			g.setColor(Color.white);
			g.fillRect(2, 2, this.getWidth() - 2, this.getHeight() - 2);

			g.setColor(Color.lightGray);
			for (int x = 0; x < this.getWidth(); x += 20)
				g.drawLine(x, 0, x, this.getHeight());

			for (int y = 0; y < this.getHeight(); y += 20)
				g.drawLine(0, y, this.getWidth(), y);
			paintPic(g, pic);
		}

		public void paintPic(Graphics g, BufferedImage[][] pic) {
			for (int i = 0; i < pic.length; i++) {
				for (int j = 0; j < pic[0].length; j++) {
					BufferedImage img = pic[i][j];
					g.drawImage(img, 20 * j + 1, 20 * i + 1, 19, 19, null);
				}
			}

		}

	}

	public JPanel getBoard() {
		return board;
	}

	public void setBoard(JPanel board) {
		this.board = board;
	}

	public JLabel getTime() {
		return time;
	}

	public void setTime(JLabel time) {
		this.time = time;
	}

	public int getTimeText() {
		return timeText;
	}

	public void setTimeText(int timeText) {
		this.timeText = timeText;
	}

	public JLabel getMines() {
		return mines;
	}

	public void setMines(JLabel mines) {
		this.mines = mines;
	}

	public int getMinesText() {
		return minesText;
	}

	public void setMinesText(int minesText) {
		this.minesText = minesText;
	}

	public JMenuItem getMntmNewGame() {
		return mntmNewGame;
	}

	public void setMntmNewGame(JMenuItem mntmNewGame) {
		this.mntmNewGame = mntmNewGame;
	}

	public JMenuItem getMntmExit() {
		return mntmExit;
	}

	public void setMntmExit(JMenuItem mntmExit) {
		this.mntmExit = mntmExit;
	}

	public JMenuItem getMntmTotalMines() {
		return mntmTotalMines;
	}

	public void setMntmTotalMines(JMenuItem mntmTotalMines) {
		this.mntmTotalMines = mntmTotalMines;
	}

	public JMenuItem getMntmHowToPlay() {
		return mntmHowToPlay;
	}

	public void setMntmHowToPlay(JMenuItem mntmHowToPlay) {
		this.mntmHowToPlay = mntmHowToPlay;
	}

	public JMenuItem getMntmAbout() {
		return mntmAbout;
	}

	public void setMntmAbout(JMenuItem mntmAbout) {
		this.mntmAbout = mntmAbout;
	}

}
