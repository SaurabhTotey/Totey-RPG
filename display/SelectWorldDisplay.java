/**
 * 
 */
package display;

import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import main.Main;
import net.miginfocom.swing.MigLayout;

/**
 * @author Saurabh Totey
 * The jFrame that selects whether to make a new world or to select an old world
 */
public class SelectWorldDisplay {
	
	private boolean hasSubmittedText;

	/**
	 * Makes a jframe that asks the user to select which world they want
	 * @throws InterruptedException 
	 */
	public SelectWorldDisplay() throws InvocationTargetException, InterruptedException{
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				//This part makes the frame, sizes and centers it, and sets it's layout
				JFrame frame = new JFrame("TOTEY RPG");
				frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice[] allDevices = env.getScreenDevices();
				int topLeftX, topLeftY, screenX, screenY, windowPosX, windowPosY;
				topLeftX = allDevices[0].getDefaultConfiguration().getBounds().x;
				topLeftY = allDevices[0].getDefaultConfiguration().getBounds().y;
				screenX = allDevices[0].getDefaultConfiguration().getBounds().width;
				screenY = allDevices[0].getDefaultConfiguration().getBounds().height;
				frame.setBounds(0, 0, screenX / 2, screenY / 4);
				windowPosX = ((screenX - frame.getWidth()) / 2 + topLeftX);
				windowPosY = ((screenY - frame.getHeight()) / 2 + topLeftY);
				frame.setLocation(windowPosX, windowPosY);
				frame.setLayout(new MigLayout());
				frame.setVisible(true);
				
				//This part makes the new fonts based on screen size
				Font titleFont = new Font(Font.SERIF, Font.ROMAN_BASELINE, screenX / 70);
				Font defaultFont = new Font(Font.MONOSPACED, Font.PLAIN, screenX / 130);
				
				//The title label asking for the game id
				JLabel titleLabel = new JLabel("What is your game ID?");
				titleLabel.setFont(titleFont);
				frame.add(titleLabel, "align center, span, pushx, height 33%, wrap");
				
				//The text field where the user enters an id
				JTextField gameIdInput = new JTextField();
				gameIdInput.setFont(titleFont);
				gameIdInput.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						Main.interpretText(gameIdInput.getText());
						frame.dispose();
					}
				});
				frame.add(gameIdInput, "align center, span, growx, pushx, height 33%, wrap");
				
				//The submit button to create/find the game based on the given string
				JButton submitButton = new JButton("Submit");
				submitButton.setFont(defaultFont);
				submitButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						gameIdInput.getActionListeners()[0].actionPerformed(e);
					}
				});
				frame.add(submitButton, "align center, span, grow, pushx, height 33%, wrap");
			}	
		});
	}

}