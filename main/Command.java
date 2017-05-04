/**
 * 
 */
package main;

import java.util.HashMap;

import character.Race;
import world.Maze;

/**
 * @author Saurabh Totey
 * A class that has commands
 * It does nothing on its own in terms of a command, but it has subclasses for commands and handles constuction features of all subcommands
 * Each of the command subclasses have specific actions they fulfill
 * Some of the commands are only for testing purposes, and can't be used otherwise
 * TODO add more commands
 */
public class Command {
	
	/**
	 * This is the list of all commands
	 * When a command is made, it adds itself to the hashmap
	 */
	public static HashMap<String, Command> allCommands = new HashMap<String, Command>();
	
	/**
	 * The name of the command and its parameters and its description
	 * If a called command doesn't match this name, the command doesn't execute
	 */
	public String commandName;
	public String params;
	public String description;

	/**
	 * The basic command constructor
	 * This one does nothing except set its name and put itself in the list of all commands and sets its parameters
	 * @param commandName the name of the command
	 * @param description what the command does
	 * @param parameters the parameters for the command
	 */
	public Command(String commandName, String description, String parameters) {
		this.commandName = commandName;
		allCommands.putIfAbsent(commandName, this);
		this.params = parameters;
		this.description = description;
	}
	
	/**
	 * This is the constructor used if there are no parameters
	 * @param commandName the name of the command
	 * @param description what the command does
	 */
	public Command(String commandName, String description){
		this(commandName, description, null);
	}
	
	/**
	 * This is a method that executes the class's command
	 * It doesn't do anything except check that the first parameter of the command is the commandname
	 * @param command an array of all of the parts of the command split by words
	 */
	public void executeCommand(String[] command){
		if(!command[0].equals(this.commandName)){
			throw new Error();
		}
	}
	
	/**
	 * Executes a command based on the command it was given
	 * If the command can't execute (can't be found or incorrect parameters), the error is caught and a message is sent to the user
	 * @param command the command to execute
	 * @return whether the command successfully executed or not
	 */
	public static boolean doCommand(String command){
		Command emptyCommand = new Command("doNothing", "Does nothing.");
		allCommands.remove("doNothing", emptyCommand);
		emptyCommand.new help();
		emptyCommand.new getLocation();
		if(Main.main.testingMode){
			emptyCommand.new refresh();
			emptyCommand.new gainExperience();
			emptyCommand.new setRace();
			emptyCommand.new buffStats();
		}
		String[] args = command.split(" ");
		try{
			allCommands.get(args[0]).executeCommand(args);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * The command to view all existing commands
	 * USAGE: "help"
	 */
	public class help extends Command{
		public help() {
			super("help", "Displays available commands");
		}
		@Override
		public void executeCommand(String[] command){
			super.executeCommand(command);
			int i = 0;
			String output = "\n\nDisplaying All Available Commands\n\n";
			for(String commandName : allCommands.keySet()){
				output += i + " : " + commandName;
				if(allCommands.get(commandName).params != null){
					for(String parameter : allCommands.get(commandName).params.split(", ")){
						output += " [" + parameter + "]";
					}
				}
				output += " -> " + allCommands.get(commandName).description + "\n";
				i++;
			}
			Main.log(output);
		}
	}
	
	/**
	 * The command to set the user's current stats to their max stats
	 * USAGE: "refresh"
	 */
	public class refresh extends Command{
		public refresh() {
			super("refresh", "Sets your current stats to your maximum stats");
		}
		@Override
		public void executeCommand(String[] command){
			super.executeCommand(command);
			Main.main.mainPlayer.restoreStats();
		}
	}
	
	/**
	 * The command to give the user experience
	 * USAGE: "gainExperience [experienceToGive]"
	 */
	public class gainExperience extends Command{
		public gainExperience() {
			super("gainExperience", "Gives you however much experience you want", "desired experience");
		}
		@Override
		public void executeCommand(String[] command){
			super.executeCommand(command);
			Main.main.mainPlayer.getLevel(Integer.parseInt(command[1]));
		}
	}
	
	/**
	 * The command to set or change the user's current race
	 * USAGE: "setRace [raceNumber] [race]"
	 */
	public class setRace extends Command{
		public setRace() {
			super("setRace", "Sets your race to whatever you want (if it is spelled right)", "race slot, desired race");
		}
		@Override
		public void executeCommand(String[] command){
			super.executeCommand(command);
			Main.main.mainPlayer.races[Integer.parseInt(command[1])] = Race.stringToRace.get(command[2]);
		}
	}
	
	/**
	 * The command to buff the user's stats
	 * USAGE: "buffStats [buffFactor]"
	 */
	public class buffStats extends Command{
		public buffStats() {
			super("buffStats", "Gives you stats as if you had levelled up as many times as you command", "buff magnitude");
		}
		@Override
		public void executeCommand(String[] command){
			super.executeCommand(command);
			int runNum = 1;
			try{
				runNum = Integer.parseInt(command[1]);
			}catch(Exception e){
				
			}
			for(int i = 0; i < runNum; i++){
				Main.main.mainPlayer.gainLevelUpStats();
			}
		}
	}
	
	/**
	 * USAGE: "getLocation"
	 */
	public class getLocation extends Command{
		public getLocation(){
			super("getLocation", "Gets the player's location");
		}
		@Override
		public void executeCommand(String[] command){
			super.executeCommand(command);
			int[] playerCoordinates = Maze.chunkCoordinatesToAbsolute(Main.main.world.playerLocation);
			Main.log("Your location is (" + playerCoordinates[0] + ", " + playerCoordinates[1] + ")");
		}
	}
	
	/**
	 */
		}
		@Override
		public void executeCommand(String[] command){
			super.executeCommand(command);
			int xCoordinate = Integer.parseInt(command[1]);
			int yCoordinate = Integer.parseInt(command[2]);
			Main.main.world.movePlayerTo(xCoordinate, yCoordinate);
		}
	}

}
