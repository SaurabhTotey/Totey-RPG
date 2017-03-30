/**
 * 
 */
package main;

import java.util.HashMap;

/**
 * @author Saurabh Totey
 * A class that has commands
 * It does nothing on its own, but it has subclasses for commands
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
		allCommands.put(commandName, this);
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
		emptyCommand.new gainExperience();
		String[] args = command.split(" ");
		try{
			allCommands.get(args[0]).executeCommand(args);
			return true;
		}catch(Exception e){
			return false;
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

}
