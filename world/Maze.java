/**
 * 
 */
package world;

import java.util.HashMap;

/**
 * @author Saurabh Totey
 * The maze is where the player can explore
 * The maze generates chunks as the player explores (so to the player, it seems like the maze is only ever completely generated once)
 * Each chunk of the maze gets saved so that the user can re-explore previously viewed areas
 * The maze object handles its generation as well as chunks and keeping track of movement
 */
public class Maze extends HashMap<String, Chunk>{
	
	/**
	 * These are all of the movable and generatable directions
	 * @author Saurabh Totey
	 */
	public enum Direction{
		UP(0, 1), RIGHT(1, 0), LEFT(-1, 0), DOWN(0, -1);
		
		public int xModifier;
		public int yModifier;
		
		/**
		 * The constructor for a direction
		 * It contains the newer position relative to the original position if the direction is used in terms of (x, y)
		 * (eg. UP from (5, 3) would (5 + xModifier, 3 + yModifier) -> (5 + 0, 3 + 1) -> (5, 4)
		 * @param xModifier the newer x position relative to the original x position
		 * @param yModifier the newer y position relative to the original y position
		 */
		private Direction(int xModifier, int yModifier){
			this.xModifier = xModifier;
			this.yModifier = yModifier;
		}
	}
	
	/**
	 * Properties of the maze such as how things would be represented
	 */
	public static int displayWidth = 40;
	public static int displayHeight = 20;
	public static String emptyTile = "□"; //TODO pass different on/off tiles to chunks based on distance from center
	public static String wallTile = "■";
	public static String playerTile = "P";
	public static String enemyTile = "E";
	public static String portalTile = "O";
	public static String itemTile = "I";
	public static String villageTile = "V";
	
	/**
	 * Constructs the maze object for the player to explore
	 * TODO make this
	 */
	public Maze(){
		super();
	}
	
	/**
	 * Generates a chunk object and adds it the maze
	 * Each chunk is stored in a hashmap with the String key being "[x], [y]" and the chunk being the output
	 * This way chunks can easily be accessed without having to manually iterate through lists
	 * TODO make this
	 */
	public void generateChunk(int x, int y){
		HashMap<Direction, Chunk> touching = new HashMap<Direction, Chunk>();
		touching.put(Direction.UP, this.get(x + ", " + (y + 1)));
		touching.put(Direction.DOWN, this.get(x + ", " + (y - 1)));
		touching.put(Direction.LEFT, this.get((x - 1) + ", " + y));
		touching.put(Direction.RIGHT, this.get((x + 1) + ", " + y));
		HashMap<Direction, int[]> needToTouch = new HashMap<Direction, int[]>();
		for(Direction direction : touching.keySet()){
			if(touching.get(direction) != null){
				for(int i = 0; i < Chunk.chunkLength; i++){
					int[] tempNeedToTouch = new int[2];
					tempNeedToTouch[0] = direction.xModifier * i;
					tempNeedToTouch[1] = direction.yModifier * i;
					needToTouch.put(direction, tempNeedToTouch);
				}
			}
		}
		this.put(x + ", " + y, null);
	}
	 
	/**
	 * Gets the part of the maze that will be displayed to user
	 * Is based off of the static displayWidth and displayHeight properties
	 * @return the string to display
	 */
	public String getToDisplay(){
		return "";
	}
	
	/**
	 * Moves the player in the specified direction
	 * Actually just moves the map relative to the player in the opposite of the specified direction
	 * (eg if the player wants to move UP, instead the map will move DOWN, so it looks like the user went up)
	 * Will generate new chunks if it needs to
	 * @param direction where to move the player, or the opposite of which direction to move the map
	 */
	public void move(Direction direction){
		
	}

}
