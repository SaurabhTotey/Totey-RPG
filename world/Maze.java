/**
 * 
 */
package world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

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
	public int[] playerLocation = new int[4];
	public static int displayWidth = 40;
	public static int displayHeight = 20;
	public static String emptyTile = "□"; //TODO pass different on/off tiles to chunks based on distance from center
	public static String wallTile = "■";
	public static String playerTile = "P";
	public static String enemyTile = "E";
	public static String bossTile = "B";
	public static String portalTile = "O";
	public static String itemTile = "I";
	public static String villagerTile = "V";
	
	/**
	 * Constructs the maze object for the player to explore
	 * TODO make this
	 */
	public Maze(){
		super();
		for(int i = 0; i < Chunk.chunkLength / 5; i++){
			for(int j = 0; j < Chunk.chunkLength / 5; j++){
				this.generateChunk(i, j);
			}
		}
	}
	
	/**
	 * Gets a chunk at a specified x and y coordinate
	 * @param xCoordinate the x coordinate of the chunk to get
	 * @param yCoordinate the y coordinate of the chunk to get
	 * @return the chunk at the specified x and y coordinates
	 */
	public Chunk get(int xCoordinate, int yCoordinate){
		return this.get(xCoordinate + ", " + yCoordinate);
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
		HashMap<Direction, ArrayList<Point>> needToTouch = new HashMap<Direction, ArrayList<Point>>();
		for(Direction direction : Direction.values()){
			needToTouch.put(direction, new ArrayList<Point>());
			for(int i = 0; i < Chunk.chunkLength; i++){
				if(touching.get(direction) == null || isTraversable(touching.get(direction).terrain[Math.abs(direction.xModifier) * (-i) + i + (-1 + direction.xModifier) * direction.xModifier * (Chunk.chunkLength - 1) / 2][Math.abs(direction.yModifier) * (-i) + i + (-1 + direction.yModifier) * direction.yModifier * (Chunk.chunkLength - 1) / 2])){
					needToTouch.get(direction).add(new Point(Math.abs(direction.xModifier) * (-i) + i + (1 + direction.xModifier) * direction.xModifier * (Chunk.chunkLength - 1) / 2, Math.abs(direction.yModifier) * (-i) + i + (1 + direction.yModifier) * direction.yModifier * (Chunk.chunkLength - 1) / 2));
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
			ArrayList<Point> branchCoords = new ArrayList<Point>();
			ArrayList<Point> edgeCoords = new ArrayList<Point>();
			branchCoords.add(new Point((int) (Math.random() * Chunk.chunkLength), (int) (Math.random() * Chunk.chunkLength)));
			LinkedList<Direction> directionToTouch = new LinkedList<Direction>(Arrays.asList(Direction.values()));
			do{
				Point placeToStart = branchCoords.get((int) (Math.random() * branchCoords.size()));
				Direction directionToGo = Direction.values()[(int) (Math.random() * Direction.values().length)];
				int distanceFromStart = 0;
				while(Math.random() > 0.2){
					Point newLocationCoords = new Point((int) (placeToStart.getX() + directionToGo.xModifier * distanceFromStart), (int) (placeToStart.getY() + directionToGo.yModifier * distanceFromStart));
					if(newLocationCoords.getX() < Chunk.chunkLength && newLocationCoords.getY() < Chunk.chunkLength && newLocationCoords.getX() >= 0 && newLocationCoords.getY() >= 0){
						terrain[(int) newLocationCoords.getX()][(int) newLocationCoords.getY()] = getEmptyTile();
						if(Math.random() < 0.05){
							branchCoords.add(new Point((int) newLocationCoords.getX(), (int) newLocationCoords.getY()));
						}
						if(newLocationCoords.getX() == Chunk.chunkLength - 1 || newLocationCoords.getX() == 0 || newLocationCoords.getY() == Chunk.chunkLength - 1 || newLocationCoords.getY() == 0){
							edgeCoords.add((Point) newLocationCoords.clone());
						}
						distanceFromStart++;
					}else{
						break;
					}
				}
				Iterator<Direction> directionIter = directionToTouch.iterator();
				while(directionIter.hasNext()){
					Direction directionToCheck = directionIter.next();
					for(Point point : edgeCoords){
						if(needToTouch.get(directionToCheck).contains(point)){
							directionIter.remove();
							break;
						}
					}
				}
			}while(directionToTouch.size() > 0);
		}else if(chunkType == 2){	//Gens boss chunk
			for(int i = 0; i < Chunk.chunkLength; i++){
				for(int j = 0; j < Chunk.chunkLength; j++){
					terrain[i][j] = enemyTile;
				}
			}
			terrain[Chunk.chunkLength / 2][Chunk.chunkLength / 2] = bossTile;
		}else if(chunkType == 1){	//Gens item chunk
			for(int i = 1; i < Chunk.chunkLength - 1; i++){
				for(int j = 1; j < Chunk.chunkLength - 1; j++){
					terrain[i][j] = itemTile;
				}
			}
		}else if(chunkType == 0){	//Gens village
			this.put(x + ", " + y, new Village(x, y, emptyTile, wallTile));
			return;
		}
		this.put(x + ", " + y, new Chunk(terrain, x, y, emptyTile, wallTile));
	}
	
	/**
	 * Makes an empty tile that is usually an emptytile, but could also be an item tile or an enemy tile
	 * @return the empty tile that is almost anything except a boss or wall tile
	 */
	public String getEmptyTile(){
		double randSelection = Math.random();
		if(randSelection > 0.15){
			return emptyTile;
		}else if(randSelection > 0.1){
			return enemyTile;
		}else if(randSelection > 0.025){
			return itemTile;
		}else{
			return portalTile;
		}
	}
	
	/**
	 * Returns if the tile is a traversable tile (so not a wall or villager tile)
	 * @param tile the tile to check for traversability
	 * @return whether or whether not the tile is traversable
	 */
	public static boolean isTraversable(String tile){
		return (emptyTile + itemTile + enemyTile + portalTile + bossTile + playerTile).contains(tile);
	}
	 
	/**
	 * Gets the part of the maze that will be displayed to user
	 * Is based off of the static displayWidth and displayHeight properties
	 * @return the string to display
	 */
	public String toString(){
		return this.get("0, 0").toString();
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
