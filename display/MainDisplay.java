/**
 * 
 */
package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

/**
 * @author Saurabh Totey
 *
 */
public class MainDisplay {
	
	public JFrame frame;
	public JPanel mainPane;
	public JPanel optionsPane;
	public JPanel inventoryPane;
	public JScrollPane logPane;
	public JTextArea logArea;
	
	public Font titleFont;
	public Font defaultFont;
	
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
		
		titleFont = new Font("titleFont", Font.ROMAN_BASELINE, screenX / 70);
		defaultFont = new Font("defaultFont", Font.PLAIN, screenX / 80);
		
		this.logArea = new JTextArea();
		this.logPane = new JScrollPane(this.logArea);
		this.logArea.setFont(defaultFont);
		this.logArea.setFocusable(false);
		this.logArea.setBackground(Color.WHITE);
		this.frame.add(this.logPane, "dock south, height 25%!");
		
		this.optionsPane = new JPanel();
		JLabel optionsTitle = new JLabel("Options");
		optionsTitle.setFont(titleFont);
		optionsTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.optionsPane.add(optionsTitle, "dock north");
		this.frame.add(this.optionsPane, "dock west, width 25%!");
		
		this.inventoryPane = new JPanel();
		JLabel invTitle = new JLabel("Inventory");
		invTitle.setFont(titleFont);
		invTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.inventoryPane.add(invTitle, "dock north");
		this.frame.add(this.inventoryPane, "dock east, width 25%!");
		
		this.mainPane = new JPanel();
		this.frame.add(this.mainPane, "grow, push, width 50%!, height 75%!");
		
		this.frame.setVisible(true);
	}
}
