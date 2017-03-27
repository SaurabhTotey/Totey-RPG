/**
 * 
 */
package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
	
	/**
	 * The main object
	 * this holds the entire game in one object
	 * this can be serialized and saved to later be resumed
	 */
	public Main() throws InterruptedException{
		
		//This part initializes the GUI and sends incoming text to the interpretText funcion
		this.gui = new display.MainDisplay();
		this.gui.inputField.addActionListener(new ActionListener(){
			@Override
		    public void actionPerformed(ActionEvent e){
				if(!gui.inputField.getText().isEmpty()){
					interpretText(gui.inputField.getText());
					gui.inputField.setText("");
				}
		    }
		});
		
		//This is where the game actually starts
		log("You wake up in a vast dank cave. Your head hurts, and you don't remember much. You try to remember your name. You think it might be");
		while(this.uninterpretedText.isEmpty()){
			Thread.sleep(15);
		}
		String name = uninterpretedText;
		log("Yes, you remember that your name is \"" + name + "\". Now that you figured that out, you look down at yourself, and are surprised to see that you are a");
		this.uninterpretedText = "";
		
		this.gui.setOptionsPane(optionsPaneMenu.RACES);
	}

	/**
	 * This is the entry point for the program
	 * @param args default main parameters
	 */
	public static void main(String[] args) throws InterruptedException{
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
