/**
 * 
 */
package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import character.Player;
import character.Race;
import display.MainDisplay;
import display.MainPane.MainPaneMode;
import display.OptionPane.OptionsPaneOptions;
import display.PlayerPane.PlayerStat;
import display.SelectWorldDisplay;
import world.Maze;

/**
 * @author Saurabh Totey
 * This class is the entry class of the program
 * It is all stored in an object so that it can be serialized if needed
 */
public class Main implements Serializable{
	
	/**
	 * The main object is static so that it can be accessed from other classes
	 */
	public static Main main;
	
	/**
	 * This is all the main object's instance data
	 * This is where all of the core components of the game are kept
	 */
	public String gameIdentifier;
	public transient MainDisplay gui;
	public static boolean willInterpretIncoming = true;
	public static String uninterpretedText = null;
	public Player mainPlayer = null;
	public boolean testingMode = true;
	public Maze world;
	
	/**
	 * The main object
	 * this holds the entire game in one object
	 * this can be serialized and saved to later be resumed 
	 * @throws InterruptedException
	 * @throws InvocationTargetException 
	 */
	public Main(String identifier) throws InterruptedException, InvocationTargetException {
		//Sets the game's identifier so that text is written to the correct location and game saves can be diferentiated
		this.gameIdentifier = identifier;
		
		//This part initializes the GUI and passes itself over to it so it can send this object data
		this.gui = new display.MainDisplay(this);
		
		//Makes a new thread to start making the maze
		new Thread(() -> world = new Maze()).start();
		
		//This is where the game actually starts
		//Collects user name and first race
		log("You wake up in a vast dank cave. Your head hurts, your vision is blurry, and you don't remember much. You try to remember your name. You think it might be...");
		String name;
		name = waitForInput(false);
		((JTextField) this.gui.userPane.playerInfoPanes.get(PlayerStat.PLAYER_NAME)[1]).setText(name);
		log("Yes, you remember that your name is \"" + name + "\". Now that you figured that out, you look down at yourself, and are surprised to see that you are a...");
		this.gui.optionsPane.setOptionsPane(OptionsPaneOptions.RACES);
		String race = waitForInput(false);
		boolean isValidRace = race.equals("Human") || race.equals("Robot") || race.equals("Shadow") || race.equals("Turtle") || race.equals("Bird");
		while(!isValidRace){
			log("You remembered that you were being silly, and that \"" + race + "\" wasn't how \"Human\", \"Robot\", \"Shadow\", \"Turtle\", or \"Bird\" were spelled.");
			race = waitForInput(false);
			isValidRace = race.equals("Human") || race.equals("Robot") || race.equals("Shadow") || race.equals("Turtle") || race.equals("Bird");
		}
		log("Of course! You remembered that you resembled your " + race.substring(0, 1).toLowerCase() + race.substring(1) + " parent mostly. You, however, could not remember the race of your other parent.");
		this.gui.optionsPane.setOptionsPane(OptionsPaneOptions.DEFAULT);
		mainPlayer = new Player(name, Race.stringToRace.get(race));
	}

	/**
	 * This is the entry point for the program
	 * @param args default main parameters
	 * @throws InterruptedException
	 * @throws InvocationTargetException 
	 */
	public static void main(String[] args) throws InterruptedException, InvocationTargetException{
		//Gets the user's desired save from the display frame and tries deserializing it; if that fails, it makes a new game (eg. the specified game doesn't exist)
		new SelectWorldDisplay();
		String id = waitForInput(false);
		try{
			FileInputStream fileIn = new FileInputStream(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\My Games\\ToteyRPG\\Saves\\" + id + ".save");
	        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
	        main = (Main) objectIn.readObject();
	        objectIn.close();
	        fileIn.close();
	        main.gui = new MainDisplay(main);
	        BufferedReader bufferedReader = new BufferedReader(new FileReader(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\My Games\\ToteyRPG\\Logs\\" + id + ".txt"));
	        String totalLogText = "";
	        String currentLine;
	        while((currentLine = bufferedReader.readLine()) != null){
	        	totalLogText += currentLine + "\n";
	        }
	        bufferedReader.close();
	        main.gui.logArea.setText(totalLogText);
		}catch(Exception e){
			main = new Main(id);
		}
		
		//Waits for the maze to finish generating
		while(main.world == null){
			Thread.sleep(200);
			//TODO remove below, but make it unecessary first
			//TODO sometimes world is stuck infinitely generating... happens due to maze generation thread suspending due to a nullpointer exception
			System.out.println("Maze not complete");
		}
		main.gui.mainPane.setMode(MainPaneMode.MAZE);
	}
	
	/**
	 * This function logs messages with timestamps
	 * @param message the message that is to be logged
	 */
	public static void log(String message){
		System.out.println("[" + new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(Calendar.getInstance().getTime()) + "] >>> " + message);
	}
	
	/**
	 * Waits until GUI puts something in uninterpreted text and then returns it
	 * @return gives back the string of what the main thread was waiting for
	 * @throws InterruptedException
	 */
	public static String waitForInput(boolean willAllowEmptyInput) throws InterruptedException{
		willInterpretIncoming = false;
		while(uninterpretedText == null || !willAllowEmptyInput && uninterpretedText.isEmpty()){
			Thread.sleep(15);
		}
		String toReturn = uninterpretedText;
		uninterpretedText = null;
		willInterpretIncoming = true;
		return toReturn;
	}
	
	/**
	 * This function handles all incoming text by either saving it as instancedata or by attempting to interpret commands
	 * This allows for cheaty functionality to those who know the commands and are on testing mode
	 * Mostly used for testing, but it is used for getting text input during certain parts of story (if you want to do this, use the waitForInput() function, as it correctly calls on this function)
	 * @param incoming the text to either save or interpret
	 */
	public static void interpretText(String incoming){
		System.out.println("[" + new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(Calendar.getInstance().getTime()) + "] <<< " + incoming);
		if(willInterpretIncoming){
			if(Command.doCommand(incoming)){
				log("The command was successful!");
			}else{
				log("Sorry, the command \'" + incoming +  "\' wasn't understood. Maybe try the 'help' command...");
			}
		}else{
			uninterpretedText = incoming;
		}
	}
	
}
