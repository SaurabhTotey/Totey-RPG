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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import character.Player;
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
	public JPanel optionsPane;
	public JPanel userPane;
	public JPanel logPane;
	public JTextArea logArea;
	public JTextField inputField;
	public Font titleFont;
	public Font defaultFont;
	public Main mainGame;
	
	/**
	 * These variables keep track of indexes of user inputs
	 */
	public int userLocation;
	
	/**
	 * These are all of the core parts of the User Information pane
	 * These all correspond to some aspect of the player and get updated in the updatePlayerInfoPane() method below
	 * They are initialized as only showing question marks because they are initially unknown, but can get updated with the aforementioned method
	 * After they are initialized, they are all set to uneditable and centered
	 */
	public JTextField user_name = new JTextField("???");
	public JTextField user_race = new JTextField("???");
	public JTextField user_health = new JTextField("???");
	public JTextField user_attack = new JTextField("???");
	public JTextField user_defense = new JTextField("???");
	public JTextField user_speed = new JTextField("???");
	public JTextField user_earthAffinity = new JTextField("???");
	public JTextField user_fireAffinity = new JTextField("???");
	public JTextField user_waterAffinity = new JTextField("???");
	public JTextField user_lightAffinity = new JTextField("???");
	public JTextField user_darknessAffinity = new JTextField("???");
	public JTextField user_orderAffinity = new JTextField("???");
	public JTextField user_chaosAffinity = new JTextField("???");
	public JTextField user_pet = new JTextField("???");
	public JTextField user_armor = new JTextField("???");
	public JTextField user_potions = new JTextField("???");
	public JTextField user_bombs = new JTextField("???");
	{
		user_name.setEditable(false);
		user_race.setEditable(false);
		user_health.setEditable(false);
		user_attack.setEditable(false);
		user_defense.setEditable(false);
		user_defense.setEditable(false);
		user_speed.setEditable(false);
		user_earthAffinity.setEditable(false);
		user_fireAffinity.setEditable(false);
		user_waterAffinity.setEditable(false);
		user_lightAffinity.setEditable(false);
		user_darknessAffinity.setEditable(false);
		user_orderAffinity.setEditable(false);
		user_chaosAffinity.setEditable(false);
		user_pet.setEditable(false);
		user_armor.setEditable(false);
		user_potions.setEditable(false);
		user_bombs.setEditable(false);
		
		user_name.setHorizontalAlignment(SwingConstants.CENTER);
		user_race.setHorizontalAlignment(SwingConstants.CENTER);
		user_health.setHorizontalAlignment(SwingConstants.CENTER);
		user_attack.setHorizontalAlignment(SwingConstants.CENTER);
		user_defense.setHorizontalAlignment(SwingConstants.CENTER);
		user_defense.setHorizontalAlignment(SwingConstants.CENTER);
		user_speed.setHorizontalAlignment(SwingConstants.CENTER);
		user_earthAffinity.setHorizontalAlignment(SwingConstants.CENTER);
		user_fireAffinity.setHorizontalAlignment(SwingConstants.CENTER);
		user_waterAffinity.setHorizontalAlignment(SwingConstants.CENTER);
		user_lightAffinity.setHorizontalAlignment(SwingConstants.CENTER);
		user_darknessAffinity.setHorizontalAlignment(SwingConstants.CENTER);
		user_orderAffinity.setHorizontalAlignment(SwingConstants.CENTER);
		user_chaosAffinity.setHorizontalAlignment(SwingConstants.CENTER);
		user_pet.setHorizontalAlignment(SwingConstants.CENTER);
		user_armor.setHorizontalAlignment(SwingConstants.CENTER);
		user_potions.setHorizontalAlignment(SwingConstants.CENTER);
		user_bombs.setHorizontalAlignment(SwingConstants.CENTER);
		
	}

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
						inputField.setText("");
						userLocation = mainGame.lastEntered.size();
				    }
				});
				inputField.addKeyListener(new KeyListener(){
					@Override
					public void keyTyped(KeyEvent e) {}
					@Override
					public void keyPressed(KeyEvent e) {
						try{
							if(e.getKeyCode() == KeyEvent.VK_DOWN) {
								inputField.setText(mainGame.lastEntered.get(userLocation + 1));
								userLocation++;
				            }else if(e.getKeyCode() == KeyEvent.VK_UP && userLocation > 0){
				            	inputField.setText(mainGame.lastEntered.get(userLocation - 1));
								userLocation--;
				            }
						}catch(Exception err){
							inputField.setText("");
							userLocation = mainGame.lastEntered.size();
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
				optionsPane = new JPanel(new MigLayout("fill"));
				optionsPane.setBackground(Color.LIGHT_GRAY);
				frame.add(new JScrollPane(optionsPane), "dock west, width 25%!");
				setOptionsPane(optionsPaneMenu.DEFAULT);
				
				//Sets all the userdisplay options to the default font
				user_name.setFont(defaultFont);
				user_race.setFont(defaultFont);
				user_health.setFont(defaultFont);
				user_attack.setFont(defaultFont);
				user_defense.setFont(defaultFont);
				user_defense.setFont(defaultFont);
				user_speed.setFont(defaultFont);
				user_earthAffinity.setFont(defaultFont);
				user_fireAffinity.setFont(defaultFont);
				user_waterAffinity.setFont(defaultFont);
				user_lightAffinity.setFont(defaultFont);
				user_darknessAffinity.setFont(defaultFont);
				user_orderAffinity.setFont(defaultFont);
				user_chaosAffinity.setFont(defaultFont);
				user_pet.setFont(defaultFont);
				user_armor.setFont(defaultFont);
				user_potions.setFont(defaultFont);
				user_bombs.setFont(defaultFont);
				
				// TODO this part makes a user pane where the user's information will be displayed
				userPane = new JPanel(new MigLayout("fill"));
				userPane.setBackground(Color.LIGHT_GRAY);
				JLabel userTitle = new JLabel("Player Info");
				userTitle.setFont(titleFont);
				userTitle.setHorizontalAlignment(SwingConstants.CENTER);
				userPane.add(userTitle, "dock north");
				
				JLabel nameLabel = new JLabel("Name: ");
				nameLabel.setFont(defaultFont);
				userPane.add(nameLabel, "gapleft 5%");
				userPane.add(user_name, "growx, pushx, span, wrap, gapright 5%");
				
				JLabel raceLabel = new JLabel("Race: ");
				raceLabel.setFont(defaultFont);
				userPane.add(raceLabel, "gapleft 5%");
				userPane.add(user_race, "growx, pushx, span, wrap, gapright 5%");
				
				JLabel healthLabel = new JLabel("Health: ");
				healthLabel.setFont(defaultFont);
				userPane.add(healthLabel, "gapleft 5%");
				userPane.add(user_health, "growx");
				
				JLabel attackLabel = new JLabel("Attack: ");
				attackLabel.setFont(defaultFont);
				userPane.add(attackLabel, "");
				userPane.add(user_attack, "growx, pushx, wrap, gapright 5%");
				
				JLabel defenseLabel = new JLabel("Defense: ");
				defenseLabel.setFont(defaultFont);
				userPane.add(defenseLabel, "gapleft 5%");
				userPane.add(user_defense, "growx");
				
				JLabel speedLabel = new JLabel("Speed: ");
				speedLabel.setFont(defaultFont);
				userPane.add(speedLabel, "");
				userPane.add(user_speed, "growx, pushx, wrap, gapright 5%");
				
				//TODO add the rest of these
				
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
		this.frame.repaint();
		this.frame.revalidate();
	}

	/**
	 * This is a set of menus of what the optionspane can be set to, the options
	 * pane is always one of these
	 */
	public enum optionsPaneMenu {
		DEFAULT("Options"), RACES("Choose race"), STATS_GAMBLE_BIG("Choose Stat"), STATS_GAMBLE_SMALL("Choose Stat");

		public String title;

		/**
		 * This is the constructor for the possible optionspanes possibilities
		 * @param title what the optionspane title should be with this specific menu
		 */
		private optionsPaneMenu(String title) {
			this.title = title;
		}
	}

	/**
	 * Sets the option pane to show a certain set of options
	 * @param type the set of options to display
	 * @throws InterruptedException 
	 * @throws InvocationTargetException 
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
				switch(type){
					case DEFAULT:
						//TODO add default menu
						JButton invButton = new JButton("Open Inventory");
						invButton.setFont(defaultFont);
						invButton.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e){
								//TODO add inv screen
							}
						});
						optionsPane.add(invButton, "width 90%!, align center, wrap");
						break;
					case RACES:
						JButton[] options = {new JButton("Human"), new JButton("Robot"), new JButton("Shadow"), new JButton("Turtle"), new JButton("Bird")};
						for(JButton button : options){
							button.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent e){
									mainGame.interpretText(((JButton) e.getSource()).getText());
								}
							});
							button.setFont(defaultFont);
							optionsPane.add(button, "width 90%!, align center, wrap");
						}
						break;
					case STATS_GAMBLE_BIG:
						//TODO big stats gambling on optionspane
						break;
					case STATS_GAMBLE_SMALL:
						//TODO small stats gambling on optionspane
						break;
				}
				refreshGUI();
			}
		});
	}
	
	/**
	 * Refreshes the player information pane with the newest information
	 */
	public void updatePlayerInfoPane(){
		Player player = mainGame.mainPlayer;
		//TODO get main object's player info and update all the user_ fields
		if(player != null && player.areStatsVisible){
			this.user_name.setText(player.name);
			String raceString = player.races[0].stringName + " / ";
			raceString += (player.races[1] != null)? player.races[1].stringName : "???";
			this.user_race.setText(raceString);
			this.user_health.setText(player.getHealth());
			this.user_attack.setText(player.getAttack());
			this.user_defense.setText(player.getDefense());
			this.user_speed.setText(player.getSpeed());
		}
		refreshGUI();
	}

}
