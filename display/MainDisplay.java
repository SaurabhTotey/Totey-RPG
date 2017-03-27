/**
 * 
 */
package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

/**
 * @author Saurabh Totey This is the class for the main display where the game
 *         will be played
 */
public class MainDisplay {

	/**
	 * These are all of the core parts of the Main GUI
	 */
	public JFrame frame;
	public JPanel mainPane;
	public JPanel optionsPane;
	public JPanel userPane;
	public JPanel logPane;
	public JTextArea logArea;
	public JTextField inputField;
	public Font titleFont;
	public Font defaultFont;

	/**
	 * This is the constructor for the gui object It makes a jframe with all of
	 * the necessary parts and makes it somewhat visually appealing TODO this
	 * could use a LOT of work
	 */
	public MainDisplay() {
		// This part makes the frame, sizes and centers it, and sets it's layout
		frame = new JFrame("TOTEY RPG");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] allDevices = env.getScreenDevices();
		int topLeftX, topLeftY, screenX, screenY, windowPosX, windowPosY;
		topLeftX = allDevices[0].getDefaultConfiguration().getBounds().x;
		topLeftY = allDevices[0].getDefaultConfiguration().getBounds().y;
		screenX = allDevices[0].getDefaultConfiguration().getBounds().width;
		screenY = allDevices[0].getDefaultConfiguration().getBounds().height;
		frame.setBounds(0, 0, screenX / 2, screenY / 2);
		windowPosX = ((screenX - frame.getWidth()) / 2 + topLeftX);
		windowPosY = ((screenY - frame.getHeight()) / 2 + topLeftY);
		frame.setLocation(windowPosX, windowPosY);
		frame.setLayout(new MigLayout());

		// This part makes the new fonts based on screen size
		titleFont = new Font("titleFont", Font.ROMAN_BASELINE, screenX / 70);
		defaultFont = new Font("defaultFont", Font.PLAIN, screenX / 120);

		// This code makes two textareas, one for a log, and another for text
		// input and configures them so that one displays info, and the other
		// sends it to main
		logPane = new JPanel(new MigLayout("fill"));
		logArea = new JTextArea();
		logPane.add(new JScrollPane(logArea), "width 100%, push, grow");
		inputField = new JTextField();
		logPane.add(inputField, "width 100%!, dock south");
		logArea.setFont(defaultFont);
		logArea.setEditable(false);
		logArea.setLineWrap(true);
		frame.add(logPane, "dock south, height 25%!, width 100%!");
		redirectSystemStreams();

		// This code makes the optionspane where all options will be displayed
		optionsPane = new JPanel();
		optionsPane.setBackground(Color.LIGHT_GRAY);
		frame.add(optionsPane, "dock west, width 25%!");
		setOptionsPane(optionsPaneMenu.DEFAULT);

		// TODO this part makes a user pane where the user's information will be
		// displayed
		userPane = new JPanel();
		userPane.setBackground(Color.LIGHT_GRAY);
		JLabel userTitle = new JLabel("Player Info");
		userTitle.setFont(titleFont);
		userTitle.setHorizontalAlignment(SwingConstants.CENTER);
		userPane.add(userTitle, "dock north");
		frame.add(userPane, "dock east, width 25%!");

		// This part adds the main pane of the game
		// All of the main stuff should show up on this pane
		mainPane = new JPanel();
		frame.add(mainPane, "grow, push, width 50%!, height 75%!");

		// This part shows the frame and sets the focus to the inputfield
		frame.setVisible(true);
		inputField.requestFocus();
	}

	/**
	 * This function just makes it so that all logged text goes to the GUI
	 * instead of the java log
	 */
	public void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				updateTextArea(String.valueOf((char) b));
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				updateTextArea(new String(b, off, len));
			}

			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};
		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	}

	/**
	 * This adds text to the log area of the GUI
	 * 
	 * @param text
	 *            text to be added to log area of GUI
	 */
	public void updateTextArea(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				logArea.append(text);
			}
		});
	}

	/**
	 * This method just refreshes and updates the GUI
	 */
	public void refreshGUI() {
		this.frame.repaint();
		this.frame.revalidate();
	}

	/**
	 * This is a set of menus of what the optionspane can be set to, the options
	 * pane is always one of these
	 */
	public enum optionsPaneMenu {
		DEFAULT("Options"), RACES("Choose race"), STATS_GAMBLE("Choose Stat");

		public String title;

		/**
		 * This is the constructor for the possible optionspanes possibilities
		 * 
		 * @param title
		 *            what the optionspane title should be with this specific
		 *            menu
		 */
		private optionsPaneMenu(String title) {
			this.title = title;
		}
	}

	/**
	 * Sets the option pane to show a certain set of options
	 * 
	 * @param type
	 *            the set of options to display
	 */
	public void setOptionsPane(optionsPaneMenu type) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				optionsPane.removeAll();
				JLabel optionsTitle = new JLabel(type.title);
				optionsTitle.setFont(titleFont);
				optionsTitle.setHorizontalAlignment(SwingConstants.CENTER);
				optionsPane.add(optionsTitle, "dock north");
				refreshGUI();
			}
		});

	}

}
