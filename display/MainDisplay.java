/**
 * 
 */
package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import main.Main;
import net.miginfocom.swing.MigLayout;

/**
 * @author Saurabh Totey 
 * This is the class for the main display where the game will be played
 */
public class MainDisplay {

	/**
	 * These are all of the core parts of the Main GUI
	 */
	public JFrame frame;
	public JPanel mainPane;
	public OptionPane optionsPane;
	public PlayerPane userPane;
	public JPanel logPane;
	public JTextArea logArea;
	public JTextField inputField;
	public Font titleFont;
	public Font defaultFont;
	public Main mainGame;
	
	/**
	 * These variables keep track of indexes of user inputs
	 * This allows the user to traverse their previous inputs with arrow keys
	 */
	public int userLocation;
	public ArrayList<String> lastEntered = new ArrayList<String>();

	/**
	 * This is the constructor for the gui object It makes a jframe with all of
	 * the necessary parts and makes it somewhat visually appealing 
	 * TODO this could use a LOT of work
	 * @param mainGame the main game object is passed on to the gui so data can be sent to it from the gui easily
	 * @throws InterruptedException 
	 * @throws InvocationTargetException 
	 */
	public MainDisplay(Main mainGame) throws InterruptedException, InvocationTargetException {
		this.mainGame = mainGame;
		SwingUtilities.invokeAndWait(new Runnable(){
			@Override
			public void run(){
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
				inputField.setFont(defaultFont);
				inputField.addActionListener(new ActionListener(){
					@Override
				    public void actionPerformed(ActionEvent e){
						mainGame.interpretText(inputField.getText());
						lastEntered.add(inputField.getText());
						inputField.setText("");
						userLocation = lastEntered.size();
				    }
				});
				inputField.addKeyListener(new KeyListener(){
					@Override
					public void keyTyped(KeyEvent e) {}
					@Override
					public void keyPressed(KeyEvent e) {
						if(e.getKeyCode() == KeyEvent.VK_DOWN && userLocation < lastEntered.size() - 1) {
							inputField.setText(lastEntered.get(userLocation + 1));
							userLocation++;
				        }else if(e.getKeyCode() == KeyEvent.VK_UP && userLocation > 0){
				           	inputField.setText(lastEntered.get(userLocation - 1));
							userLocation--;
				        }else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP){
				        	inputField.setText("");
				        	userLocation = (e.getKeyCode() == KeyEvent.VK_DOWN)? lastEntered.size() : -1;
				        }
					}
					@Override
					public void keyReleased(KeyEvent e) {}
				});
				logPane.add(inputField, "width 100%!, dock south");
				logArea.setFont(defaultFont);
				logArea.setEditable(false);
				logArea.setLineWrap(true);
				frame.add(logPane, "dock south, height 25%!, width 100%!");
				redirectSystemStreams();

				// This code makes the optionspane where all options will be displayed
				optionsPane = new OptionPane(Color.LIGHT_GRAY, defaultFont, titleFont, mainGame);
				frame.add(new JScrollPane(optionsPane), "dock west, width 25%!");
				
				//TODO this part makes a user pane where the user's information will be displayed				
				//TODO add the rest of these
				userPane = new PlayerPane(Color.LIGHT_GRAY, defaultFont, titleFont, mainGame);
				frame.add(new JScrollPane(userPane), "dock east, width 25%!");

				// This part adds the main pane of the game
				// All of the main stuff should show up on this pane
				mainPane = new JPanel();
				frame.add(mainPane, "grow, push, width 50%!, height 75%!");

				// This part shows the frame and sets the focus to the inputfield
				frame.setVisible(true);
				inputField.requestFocus();
			}
		});
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
	 * @param text text to be added to log area of GUI
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
		userPane.updatePane();
		frame.repaint();
		frame.revalidate();
	}

}
