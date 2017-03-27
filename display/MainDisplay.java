/**
 * 
 */
package display;

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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

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
	public JPanel optionsPane;
	public JPanel inventoryPane;
	public JPanel logPane;
	public JTextArea logArea;
	public JTextArea inputField;
	public Font titleFont;
	public Font defaultFont;
	
	/**
	 * This is the constructor for the gui object
	 * It makes a jframe with all of the necessary parts and makes it somewhat visually appealing
	 * TODO this could use a LOT of work
	 */
	public MainDisplay(){
		//This part makes the frame, sizes and centers it, and sets it's layout
		this.frame = new JFrame("TOTEY RPG");
		this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] allDevices = env.getScreenDevices();
		int topLeftX, topLeftY, screenX, screenY, windowPosX, windowPosY;
		topLeftX = allDevices[0].getDefaultConfiguration().getBounds().x;
		topLeftY = allDevices[0].getDefaultConfiguration().getBounds().y;
		screenX = allDevices[0].getDefaultConfiguration().getBounds().width;
		screenY = allDevices[0].getDefaultConfiguration().getBounds().height;
		this.frame.setBounds(0, 0, screenX / 2, screenY / 2);
		windowPosX = ((screenX - frame.getWidth()) / 2 + topLeftX);
		windowPosY = ((screenY - frame.getHeight()) / 2 + topLeftY);
		this.frame.setLocation(windowPosX, windowPosY);
		this.frame.setLayout(new MigLayout());
		
		//This part makes the new fonts based on screen size
		titleFont = new Font("titleFont", Font.ROMAN_BASELINE, screenX / 70);
		defaultFont = new Font("defaultFont", Font.PLAIN, screenX / 120);
		
		//This code makes two textareas, one for a log, and another for text input and configures them so that one displays info, and the other sends it to main
		this.logPane = new JPanel(new MigLayout());
		this.logArea = new JTextArea();
		this.logPane.add(new JScrollPane(this.logArea), "push, grow");
		this.inputField = new JTextArea();
		JScrollPane inputPane = new JScrollPane(this.inputField);
		inputPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.logPane.add(inputPane, "push, grow, dock south");
		this.logArea.setFont(defaultFont);
		this.logArea.setEditable(false);
		this.logArea.setLineWrap(true);
		this.frame.add(this.logPane, "dock south, height 25%!");
		this.redirectSystemStreams();
		
		this.optionsPane = new JPanel();
		JLabel optionsTitle = new JLabel("Options");
		optionsTitle.setFont(titleFont);
		optionsTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.optionsPane.add(optionsTitle, "dock north");
		this.frame.add(this.optionsPane, "dock west, width 15%!");
		
		this.inventoryPane = new JPanel();
		JLabel invTitle = new JLabel("Inventory");
		invTitle.setFont(titleFont);
		invTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.inventoryPane.add(invTitle, "dock north");
		this.frame.add(this.inventoryPane, "dock east, width 15%!");
		
		this.mainPane = new JPanel();
		this.frame.add(this.mainPane, "grow, push, width 70%!, height 75%!");
		
		this.frame.setVisible(true);
		this.inputField.requestFocus();
	}
	
	/**
	 * This function just makes it so that all logged text goes to the GUI instead of the java log
	 */
	public void redirectSystemStreams(){
		OutputStream out = new OutputStream(){
			@Override
			public void write(int b) throws IOException {
				updateTextArea(String.valueOf((char) b));
			}
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				updateTextArea(new String(b, off, len));
			}
			@Override
			public void write(byte[] b) throws IOException{
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
	public void updateTextArea(final String text){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				logArea.append(text);
			}
		});
	}
	
}
