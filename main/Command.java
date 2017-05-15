/**
 * 
 */
package main;

import java.util.HashMap;

import character.Race;
import world.Maze;
import world.Maze.Tile;

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
		allCommands.put(commandName, this);
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
	 * @throws InterruptedException 
	 */
	public void executeCommand(String[] command) throws InterruptedException{
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
			emptyCommand.new betterSetLocation();
			emptyCommand.new betterGetLocation();
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
		public void executeCommand(String[] command) throws InterruptedException{
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
		public void executeCommand(String[] command) throws InterruptedException{
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
		public void executeCommand(String[] command) throws InterruptedException{
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
		public void executeCommand(String[] command) throws InterruptedException{
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
		public void executeCommand(String[] command) throws InterruptedException{
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
	 * The command to get the player's location
	 * USAGE: "getLocation"
	 */
	public class getLocation extends Command{
		public getLocation(){
			super("getLocation", "Gets the player's location");
		}
		@Override
		public void executeCommand(String[] command) throws InterruptedException{
			super.executeCommand(command);
			int[] playerCoordinates = Maze.chunkCoordinatesToAbsolute(Main.main.world.playerLocation);
			Main.log("Your location is (" + playerCoordinates[0] + ", " + playerCoordinates[1] + ")");
		}
	}
	
	/**
	 * The command to set the player's location
	 * USAGE: "setLocation [xCoordinate] [yCoordinate]"
	 */
	public class setLocation extends Command{
		public setLocation(){
			super("setLocation", "Moves the player to the specified location", "x coordinate, y coordinate");
		}
		@Override
		public void executeCommand(String[] command) throws InterruptedException{
			super.executeCommand(command);
			int[] coordsToMove = {Integer.parseInt(command[1]), Integer.parseInt(command[2])};
			Main.main.world.movePlayerTo(coordsToMove);
		}
	}
	
	/**
	 * The command to either get the player's location or the location of a tile at specified coordinates
	 * USAGE: "getLocation [xCoordinateOfTileToGet] [yCoordinateOfTileToGet]"
	 * If coordinates aren't given, the console will output the coordinates of the player
	 */
	public class betterGetLocation extends Command{
		public betterGetLocation(){
			super("getLocation", "Gets the player's location if given no coordinates, or get the tile at the specified coordinates", "x coordinate, y coordinate");
		}
		@Override
		public void executeCommand(String[] command) throws InterruptedException{
			super.executeCommand(command);
			if(command.length == 1){
				new getLocation().executeCommand(command);
				new betterGetLocation();
			}else{
				int[] coords = {Integer.parseInt(command[1]), Integer.parseInt(command[2])};
				Main.log("The tile at (" + coords[0] + ", " + coords[1] + ") is \""  + Main.main.world.getStrAt(coords) + "\"");
			}
		}
	}
	
	/**
	 * The command to either set the player's location or to set the location of another tile
	 * USAGE: "setLocation [xCoordinate] [yCoordinate] [tile]"
	 * If tile isn't given, it just teleports the player
	 */
	public class betterSetLocation extends Command{
		public betterSetLocation(){
			super("setLocation", "Either teleports the player to the given coordinates or sets the tile at the given coordinates", "x coordinate, y coordinate, tile");
		}
		@Override
		public void executeCommand(String[] command) throws InterruptedException{
			super.executeCommand(command);
			if(command.length == 3){
				new setLocation().executeCommand(command);
				new betterSetLocation();
			}else if(command.length > 3 && Maze.tileNameToTile.get(command[3]) != Tile.PLAYER){
				int[] chunkCoords = Maze.absoluteToChunkCoordinates(new int[]{Integer.parseInt(command[1]), Integer.parseInt(command[2])});
				Main.main.world.get(chunkCoords[0], chunkCoords[1]).terrain[chunkCoords[2]][chunkCoords[3]] = Maze.tileNameToTile.get(command[3]).representation; //Made it get the string of the tile from the hashmap given the string to make sure that an actual tile was placed
			}else{
				throw new Error();
			}
		}
	}

}
