/**
 * 
 */
package main;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import character.Player;
import character.Race;
import display.MainDisplay.optionsPaneMenu;

/**
 * @author Saurabh Totey
 * This class is the entry class of the program
 * It is all stored in an object so that it can be serialized if needed
 */
public class Main {
	
	/**
	 * The main object is static so that it can be accessed from other classes
	 */
	public static Main main;
	
	/**
	 * This is all the main object's instance data
	 * This is where all of the core components of the game are kept
	 */
	public display.MainDisplay gui;
	public boolean willInterpretIncoming = true;
	public String uninterpretedText = "";
	public transient ArrayList<String> lastEntered = new ArrayList<String>();
	public Player mainPlayer = null;
	
	/**
	 * The main object
	 * this holds the entire game in one object
	 * this can be serialized and saved to later be resumed 
	 * @throws InterruptedException
	 * @throws InvocationTargetException 
	 */
	public Main() throws InterruptedException, InvocationTargetException {
		//This part puts an empty string into the lastEntered, so when the user presses an arrow key, it won't error, but it would rather put an empty string in
		lastEntered.add("");
		
		//This part initializes the GUI and passes itself over to it so it can send this object data
		this.gui = new display.MainDisplay(this);
		
		//This is where the game actually starts
		log("You wake up in a vast dank cave. Your head hurts, your vision is blurry, and you don't remember much. You try to remember your name. You think it might be...");
		String name;
		name = this.waitForInput();
		this.gui.user_name.setText(name);
		log("Yes, you remember that your name is \"" + name + "\". Now that you figured that out, you look down at yourself, and are surprised to see that you are a...");
		this.gui.setOptionsPane(optionsPaneMenu.RACES);
		String race = this.waitForInput();
		boolean isValidRace = race.equals("Human") || race.equals("Robot") || race.equals("Shadow") || race.equals("Turtle") || race.equals("Bird");
		while(!isValidRace){
			log("You remembered that you were being silly, and that \"" + race + "\" wasn't how \"Human\", \"Robot\", \"Shadow\", \"Turtle\", or \"Bird\" were spelled.");
			race = this.waitForInput();
			isValidRace = race.equals("Human") || race.equals("Robot") || race.equals("Shadow") || race.equals("Turtle") || race.equals("Bird");
		}
		log("Of course! You remembered that you resembled your " + race.substring(0, 1).toLowerCase() + race.substring(1) + " parent mostly. You, however, could not remember the race of your other parent.");
		this.gui.setOptionsPane(optionsPaneMenu.DEFAULT);
		mainPlayer = new Player(name, Race.stringToRace.get(race));
		this.gui.updatePlayerInfoPane();
	}

	/**
	 * This is the entry point for the program
	 * @param args default main parameters
	 * @throws InterruptedException
	 * @throws InvocationTargetException 
	 */
	public static void main(String[] args) throws InterruptedException, InvocationTargetException{
		main = new Main();
		//TODO
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
	public String waitForInput() throws InterruptedException{
		this.willInterpretIncoming = false;
		while(this.uninterpretedText.isEmpty()){
			Thread.sleep(15);
		}
		String toReturn = this.uninterpretedText;
		this.uninterpretedText = "";
		this.willInterpretIncoming = true;
		return toReturn;
	}
	
	/**
	 * This function handles all incoming text by either saving it as instancedata or by attempting to interpret commands
	 * This allows for cheaty functionality to those who know the commands
	 * Mostly used for testing, but it is used for getting text input during certain parts of story (if you want to do this, use the waitForInput() function, as it correctly calls on this function)
	 * @param incoming the text to either save or interpret
	 */
	public void interpretText(String incoming){
		System.out.println("[" + new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(Calendar.getInstance().getTime()) + "] <<< " + incoming);
		this.lastEntered.add(incoming);
		if(this.willInterpretIncoming){
			if(Command.doCommand(incoming)){
				log("The command was successful!");
				this.gui.updatePlayerInfoPane();
			}else{
				log("Sorry, the command \"" + incoming +  "\" wasn't understood...");
			}
		}else{
			this.uninterpretedText = incoming;
		}
	}
	
}
