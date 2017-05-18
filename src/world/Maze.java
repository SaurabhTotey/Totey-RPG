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
	 * All the types of tiles and their properties
	 * Used for defining maze appearance and player movement through maze
	 */
	public enum Tile{
		EMPTY(" ", true, false), WALL("|", false, true), PLAYER("P", false, true),
		ENEMY("E", true, true), BOSS("B", true, true), PORTAL("O", true, false), ITEM("I", true, true),
		VILLAGER("V", false, false);
		
		//░ for possible emptyTile
		//█ for possible wallTile
		
		public String representation;
		public boolean isTraversable;
		public boolean getsReplacedWhenSteppedOn;
		
		/**
		 * The constructor for a tile
		 * The constructor specifies its properties
		 * @param representation how the tile appears in the map
		 * @param isTraversable whether the player can move on this tile
		 * @param getsReplacedWhenSteppedOn whether the tile should get destroyed after the player walks on it
		 */
		private Tile(String representation, boolean isTraversable, boolean getsReplacedWhenSteppedOn){
			this.representation = representation;
			this.isTraversable = isTraversable;
			this.getsReplacedWhenSteppedOn = getsReplacedWhenSteppedOn;
			tileNameToTile.put(representation, this);
		}
	}
	
	/**
	 * Properties of the maze such as how things would be represented
	 * Player location is stored in an array of length 4 where the location is represented as
	 * {chunkX, chunkY, playerX, playerY}
	 */
	public volatile int[] playerLocation = new int[4];
	public Tile lastSteppedOn = Tile.EMPTY;
	public ArrayList<Point> discoveredPortals = new ArrayList<Point>();
	public static int displayWidth = 51;
	public static int displayHeight = 23;
	public static HashMap<String, Tile> tileNameToTile = new HashMap<String, Tile>();
	
	/**
	 * Constructs the maze object for the player to explore
	 * @throws InterruptedException 
	 */
	public Maze() throws InterruptedException{
		super();
		for(int i = 0; i < Chunk.chunkLength / 5; i++){
			for(int j = 0; j < Chunk.chunkLength / 5; j++){
				this.generateChunk(i, j);
			}
		}
		while(this.get(0, 0).terrain[Chunk.chunkLength / 2][Chunk.chunkLength / 2].equals(Tile.WALL.representation)){
			this.generateChunk(0, 0);
		}
		this.get(0, 0).terrain[Chunk.chunkLength / 2][Chunk.chunkLength / 2] = Tile.PORTAL.representation;
		this.movePlayerTo(new int[]{Chunk.chunkLength / 2, Chunk.chunkLength / 2});
		this.discoveredPortals.add(new Point(Chunk.chunkLength / 2, Chunk.chunkLength / 2));
	}
	
	/**
	 * Gets a chunk at a specified x and y coordinate
	 * If the chunk doesn't exist, it generates the chunk and then returns it
	 * @param xCoordinate the x coordinate of the chunk to get
	 * @param yCoordinate the y coordinate of the chunk to get
	 * @return the chunk at the specified x and y coordinates
	 */
	public synchronized Chunk get(int xCoordinate, int yCoordinate){
		if(this.get(xCoordinate + ", " + yCoordinate) == null){
			this.generateChunk(xCoordinate, yCoordinate);
		}
		return this.get(xCoordinate + ", " + yCoordinate);
	}
	
	/**
	 * Generates a chunk object and adds it the maze
	 * Each chunk is stored in a hashmap with the String key being "[x], [y]" and the chunk being the output
	 * This way chunks can easily be accessed without having to manually iterate through lists
	 * TODO pass different on/off tiles to chunks based on distance from center
	 */
	public synchronized void generateChunk(int x, int y){
		//Gets the parts of the adjacent chunks the new chunk should touch
		HashMap<Direction, ArrayList<Point>> needToTouch = new HashMap<Direction, ArrayList<Point>>();
		for(Direction direction : Direction.values()){
			needToTouch.put(direction, new ArrayList<Point>());
			for(int i = 0; i < Chunk.chunkLength; i++){
				Chunk touching = this.get((x + direction.xModifier) + ", " + (y + direction.yModifier));
				if(touching == null || tileNameToTile.get(touching.terrain[Math.abs(direction.xModifier) * (-i) + i + (-1 + direction.xModifier) * direction.xModifier * (Chunk.chunkLength - 1) / 2][Math.abs(direction.yModifier) * (-i) + i + (-1 + direction.yModifier) * direction.yModifier * (Chunk.chunkLength - 1) / 2]).isTraversable){
					needToTouch.get(direction).add(new Point(Math.abs(direction.xModifier) * (-i) + i + (1 + direction.xModifier) * direction.xModifier * (Chunk.chunkLength - 1) / 2, Math.abs(direction.yModifier) * (-i) + i + (1 + direction.yModifier) * direction.yModifier * (Chunk.chunkLength - 1) / 2));
				}
			}
		}
		int chunkType = (int) (Math.random() * 501);
		String[][] terrain = new String[Chunk.chunkLength][Chunk.chunkLength];
		for(int i = 0; i < Chunk.chunkLength; i++){
			for(int j = 0; j < Chunk.chunkLength; j++){
				terrain[i][j] = Tile.WALL.representation;
			}
		}
		if(chunkType >= 3){			//Gens normal chunk
			ArrayList<Point> branchCoords = new ArrayList<Point>();
			ArrayList<Point> edgeCoords = new ArrayList<Point>();
			branchCoords.add((x == 0 && y == 0)? new Point() : new Point((int) (Math.random() * Chunk.chunkLength), (int) (Math.random() * Chunk.chunkLength)));
			LinkedList<Direction> directionToTouch = new LinkedList<Direction>(Arrays.asList(Direction.values()));
			do{
				Point placeToStart = branchCoords.get((int) (Math.random() * branchCoords.size()));
				Direction directionToGo = Direction.values()[(int) (Math.random() * Direction.values().length)];
				int distanceFromStart = 0;
				while(Math.random() > 0.2){
					Point newLocationCoords = new Point((int) (placeToStart.getX() + directionToGo.xModifier * distanceFromStart), (int) (placeToStart.getY() + directionToGo.yModifier * distanceFromStart));
					if(newLocationCoords.getX() < Chunk.chunkLength && newLocationCoords.getY() < Chunk.chunkLength && newLocationCoords.getX() >= 0 && newLocationCoords.getY() >= 0){
						terrain[(int) newLocationCoords.getX()][(int) newLocationCoords.getY()] = getEmptyTile();
						if(Math.random() < 0.025){
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
					terrain[i][j] = Tile.ENEMY.representation;
				}
			}
			terrain[Chunk.chunkLength / 2][Chunk.chunkLength / 2] = Tile.BOSS.representation;
		}else if(chunkType == 1){	//Gens item chunk
			for(int i = 1; i < Chunk.chunkLength - 1; i++){
				for(int j = 1; j < Chunk.chunkLength - 1; j++){
					terrain[i][j] = Tile.ITEM.representation;
				}
			}
		}else if(chunkType == 0){	//Gens village
			this.put(x + ", " + y, new Village(x, y, Tile.EMPTY.representation, Tile.WALL.representation));
			return;
		}
		this.put(x + ", " + y, new Chunk(terrain, x, y, Tile.EMPTY.representation, Tile.WALL.representation));
	}
	
	/**
	 * Makes an empty tile that is usually an emptytile, but could also be an item tile or an enemy tile
	 * @return the empty tile that is almost anything except a boss or wall tile
	 */
	public static String getEmptyTile(){
		int randSelection = (int) (Math.random() * 50001);
		if(randSelection <= 2500){
			return Tile.ENEMY.representation; // 1/20 chance
		}else if(randSelection <= 2510){
			return Tile.ITEM.representation; // 1/5000 chance
		}else if(randSelection == 2511){
			return Tile.PORTAL.representation; // 1/50000 chance
		}else{
			return Tile.EMPTY.representation;
		}
	}
	 
	/**
	 * Gets the part of the maze that will be displayed to user
	 * Is based off of the static displayWidth and displayHeight properties
	 * @return the string to display
	 */
	public String toString(){
		String toReturn = "";
		for(int y = 0; y < displayHeight; y++){
			for(int x = 0; x < displayWidth; x++){
				int[] locationToGet = chunkCoordinatesToAbsolute(this.playerLocation.clone());
				locationToGet[0] += x - displayWidth / 2;
				locationToGet[1] += y - displayHeight / 2;
				toReturn += getStrAt(locationToGet);
			}
			toReturn += "\n";
		}
		return toReturn;
	}
	
	/**
	 * Moves the player in the specified direction
	 * Actually just moves the map relative to the player in the opposite of the specified direction
	 * (eg if the player wants to move UP, instead the map will move DOWN, so it looks like the user went up)
	 * Will generate new chunks if it needs to
	 * @param direction where to move the player, or the opposite of which direction to move the map
	 * @throws InterruptedException 
	 */
	public synchronized void move(Direction direction) throws InterruptedException{
		int[] playerCoords = chunkCoordinatesToAbsolute(this.playerLocation);
		playerCoords[0] += direction.xModifier;
		playerCoords[1] += direction.yModifier;
		this.movePlayerTo(playerCoords);
	}
	
	/**
	 * Sends the player to a specific location
	 * @param absoluteCoordinates where to move the player
	 * @throws InterruptedException 
	 */
	public synchronized void movePlayerTo(int[] absoluteCoordinates) throws InterruptedException{
		StepAction.initAllStepActions();
		String steppedOn = getStrAt(absoluteCoordinates);
		if(tileNameToTile.get(steppedOn).isTraversable){
			this.get(this.playerLocation[0], this.playerLocation[1]).terrain[this.playerLocation[2]][this.playerLocation[3]] = (lastSteppedOn.getsReplacedWhenSteppedOn)? Tile.EMPTY.representation : lastSteppedOn.representation;
			this.playerLocation = absoluteToChunkCoordinates(absoluteCoordinates);
			this.get(this.playerLocation[0], this.playerLocation[1]).terrain[this.playerLocation[2]][this.playerLocation[3]] = Tile.PLAYER.representation;
			this.lastSteppedOn = tileNameToTile.get(steppedOn);
		}
		StepAction.tileActions.get(steppedOn).performAction(absoluteCoordinates[0], absoluteCoordinates[1]);
	}
	
	/**
	 * Takes in absolute coordinates and gives coordinates for a chunk and innerchunk coordinates
	 * @param absoluteCoordinates absolute coordinates for a point
	 * @return the coordinates for the chunk and the coordinates within the chunk
	 */
	public static int[] absoluteToChunkCoordinates(int[] absoluteCoordinates) {
		int[] toReturn = new int[4];
		toReturn[0] = Math.floorDiv(absoluteCoordinates[0], Chunk.chunkLength);
		toReturn[1] = Math.floorDiv(absoluteCoordinates[1], Chunk.chunkLength);
		toReturn[2] = (((int) Math.round(Math.ceil(Math.abs((0.0 + absoluteCoordinates[0]) / Chunk.chunkLength)))) * Chunk.chunkLength + absoluteCoordinates[0]) % Chunk.chunkLength;
		toReturn[3] = (((int) Math.round(Math.ceil(Math.abs((0.0 + absoluteCoordinates[1]) / Chunk.chunkLength)))) * Chunk.chunkLength + absoluteCoordinates[1]) % Chunk.chunkLength;
		return toReturn;
	}
	
	/**
	 * Takes in chunk coordinates and relative coordinates to give back absolute coordinates
	 * @param chunkCoordinates the coordinates for the chunk and the coordinates within the chunk
	 * @return absolute coordinates for that point
	 */
	public static int[] chunkCoordinatesToAbsolute(int[] chunkCoordinates){
		return new int[]{chunkCoordinates[0] * Chunk.chunkLength + chunkCoordinates[2], chunkCoordinates[1] * Chunk.chunkLength + chunkCoordinates[3]};
	}
	
	/**
	 * Gets a part of a chunk with absolute coordinates
	 * @param absoluteCoordinates
	 * @return
	 */
	public String getStrAt(int[] absoluteCoordinates){
		int[] coordinates = absoluteToChunkCoordinates(absoluteCoordinates);
		return this.get(coordinates[0], coordinates[1]).terrain[coordinates[2]][coordinates[3]];
	}

}
