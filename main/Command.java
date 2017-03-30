/**
 * 
 */
package main;

import java.util.HashMap;

import character.Race;

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
	 * The name of the command
	 * If a called command doesn't match this name, the command doesn't execute
	 */
	public String commandName;

	/**
	 * The command constructor
	 * This one does nothing except set its name and put itself in the list of all commands
	 */
	public Command(String commandName) {
		this.commandName = commandName;
		allCommands.putIfAbsent(commandName, this);
	}
	
	/**
	 * This is a method that executes the class's command
	 * It doesn't do anything except check that the first parameter of the command is the commandname
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
		Command emptyCommand = new Command("doNothing");
		allCommands.remove("doNothing", emptyCommand);
		emptyCommand.new help();
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
			super("help");
		}
		@Override
		public void executeCommand(String[] command){
			super.executeCommand(command);
			int i = 0;
			String output = "Displaying All Available Commands\n";
			for(String commandName : allCommands.keySet()){
				output += i + " : " + commandName + "\n";
				i++;
			}
			Main.log(output.substring(0, output.length() - 1));
		}
	}
	
	/**
	 * The command to set the user's current stats to their max stats
	 * USAGE: "refresh"
	 */
	public class refresh extends Command{
		public refresh() {
			super("refresh");
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
			super("gainExperience");
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
			super("setRace");
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
			super("buffStats");
		}
		@Override
		public void executeCommand(String[] command){
			super.executeCommand(command);
			Main.main.mainPlayer.gainLevelUpStats();
		}
	}

}
