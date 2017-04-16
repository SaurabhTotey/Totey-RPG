/**
 * 
 */
package world;

import java.awt.Point;
import java.util.ArrayList;
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
		UP(0, -1), RIGHT(1, 0), LEFT(-1, 0), DOWN(0, 1);
		
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
		//Gets the parts of the adjacent chunks the new chunk should touch
		HashMap<Direction, Chunk> touching = new HashMap<Direction, Chunk>();
		touching.put(Direction.UP, this.get(x + ", " + (y + 1)));
		touching.put(Direction.DOWN, this.get(x + ", " + (y - 1)));
		touching.put(Direction.LEFT, this.get((x - 1) + ", " + y));
		touching.put(Direction.RIGHT, this.get((x + 1) + ", " + y));
		HashMap<Direction, Point> needToTouch = new HashMap<Direction, Point>();
		for(Direction direction : Direction.values()){
			for(int i = 0; i < Chunk.chunkLength; i++){
				if(touching == null || touching.get(direction).terrain[Math.abs(direction.xModifier) * (-i) + i + (-1 + direction.xModifier) * direction.xModifier * Chunk.chunkLength / 2][Math.abs(direction.yModifier) * (-i) + i + (-1 + direction.yModifier) * direction.yModifier * Chunk.chunkLength / 2].equals(touching.get(direction).emptyTile)){
					needToTouch.put(direction, new Point(Math.abs(direction.xModifier) * (-i) + i + (-1 + direction.xModifier) * direction.xModifier * Chunk.chunkLength / 2, Math.abs(direction.yModifier) * (-i) + i + (-1 + direction.yModifier) * direction.yModifier * Chunk.chunkLength / 2));
				}
			}
		}
		int chunkType = (int) (Math.random() * 501);
		String[][] terrain = new String[Chunk.chunkLength][Chunk.chunkLength];
		for(int i = 0; i < Chunk.chunkLength; i++){
			for(int j = 0; j < Chunk.chunkLength; j++){
				terrain[i][j] = wallTile;
			}
		}
		if(chunkType >= 3){			//Gens normal chunk
			boolean touchesAllAdjacent;
			ArrayList<Point> branchCoords = new ArrayList<Point>();
			ArrayList<Point> edgeCoords = new ArrayList<Point>();
			branchCoords.add(new Point(Chunk.chunkLength / 2, Chunk.chunkLength / 2));
			do{
				Point placeToStart = branchCoords.get((int) (Math.random() * branchCoords.size()));
				Direction directionToGo = Direction.values()[(int) (Math.random() * Direction.values().length)];
				int distanceFromStart = 0;
				while(Math.random() > 0.3){
					Point newLocationCoords = new Point((int) (placeToStart.getX() + directionToGo.xModifier * distanceFromStart), (int) (placeToStart.getY() + directionToGo.yModifier * distanceFromStart));
					if(newLocationCoords.getX() < Chunk.chunkLength && newLocationCoords.getY() < Chunk.chunkLength && newLocationCoords.getX() >= 0 && newLocationCoords.getY() >= 0){
						terrain[(int) newLocationCoords.getX()][(int) newLocationCoords.getY()] = emptyTile;
						if(Math.random() < 0.15){
							branchCoords.add((Point) newLocationCoords.clone());
							//add break here?
						}
						if(newLocationCoords.getX() == Chunk.chunkLength || newLocationCoords.getX() == 0 || newLocationCoords.getY() == Chunk.chunkLength || newLocationCoords.getY() == 0){
							edgeCoords.add((Point) newLocationCoords.clone());
						}
						distanceFromStart++;
					}else{
						break;
					}
				}
				touchesAllAdjacent = true;
				//evaluate touchesAllAdjacent with edgeCoords and needToTouch by making sure there is a matching edgeCoord for each direction in needToTouch
			}while(!touchesAllAdjacent);
		}else if(chunkType == 2){	//Gens boss chunk
			
		}else if(chunkType == 1){	//Gens item chunk
			
		}else if(chunkType == 0){	//Gens village
			
		}
		this.put(x + ", " + y, new Chunk(terrain, x, y, emptyTile, wallTile));
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
	
	/**
	 * Sends the player to a certain chunk
	 * Always puts the player in the center of the chunk
	 * @param xChunk the x-coordinate of the chunk to send the player
	 * @param yChunk the y-coordinate of the chunk to send the player
	 */
	public void setCoords(int xChunk, int yChunk){
		
	}

}
