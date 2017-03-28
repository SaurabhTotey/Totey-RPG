/**
 * 
 */
package main;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import character.Player;
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
	public boolean willInterpretIncoming = false;
	public String uninterpretedText = "";
	public Player mainPlayer = null;
	
	/**
	 * The main object
	 * this holds the entire game in one object
	 * this can be serialized and saved to later be resumed 
	 * @throws InterruptedException
	 * @throws InvocationTargetException 
	 */
	public Main() throws InterruptedException, InvocationTargetException {
		//This part initializes the GUI and passes itself over to it so it can send this object data
		this.gui = new display.MainDisplay(this);
		
		//This is where the game actually starts
		log("You wake up in a vast dank cave. Your head hurts, your vision is blurry, and you don't remember much. You try to remember your name. You think it might be...");
		String name = this.waitForInput();
		this.gui.user_name.setText(name);
		log("Yes, you remember that your name is \"" + name + "\". Now that you figured that out, you look down at yourself, and are surprised to see that you are a...");
		willInterpretIncoming = true;
		this.gui.setOptionsPane(optionsPaneMenu.RACES);
		String race = this.waitForInput();
		this.gui.user_race.setText(race); //TODO once character constructing is handled, delete this, and instead call this.gui.updatePlayerInfoPane(); to update the player information pane
		log("Of course! You remembered that you resembled your " + race.substring(0, 1).toLowerCase() + race.substring(1) + " parent mostly. You, however, could not remember the race of your other parent.");
		this.gui.setOptionsPane(optionsPaneMenu.DEFAULT);
		//TODO
	}

	/**
	 * This is the entry point for the program
	 * @param args default main parameters
	 * @throws InterruptedException
	 * @throws InvocationTargetException 
	 */
	public static void main(String[] args) throws InterruptedException, InvocationTargetException{
		main = new Main();
	}
	
	/**
	 * This function logs messages with timestamps
	 * @param message the message that is to be logged
	 */
	public static void log(String message){
		System.out.println("[" + new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(Calendar.getInstance().getTime()) + "] : " + message);
	}
	
	/**
	 * Waits until GUI puts something in uninterpreted text and then returns it
	 * @return gives back the string of what the main thread was waiting for
	 * @throws InterruptedException
	 */
	public String waitForInput() throws InterruptedException{
		while(this.uninterpretedText.isEmpty()){
			Thread.sleep(15);
		}
		String toReturn = this.uninterpretedText;
		this.uninterpretedText = "";
		return toReturn;
	}
	
	/**
	 * This function handles all incoming text by either saving it as instancedata or by attempting to interpret commands
	 * @param incoming the text to either save or interpret
	 */
	public void interpretText(String incoming){
		if(this.willInterpretIncoming){
			//TODO interpret incoming text
		}else{
			this.uninterpretedText = incoming;
		}
	}

}
