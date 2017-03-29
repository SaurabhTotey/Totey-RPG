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
public class Maze {
	
	/**
	 * Properties of the maze
	 * Each chunk is stored in a hashmap with the String key being "([x], [y])" and the chunk being the output
	 * This way chunks can easily be accessed without having to manually iterate through lists
	 */
	public HashMap<String, Chunk> mazeChunks = new HashMap<String, Chunk>();
	
	/**
	 * Constructs the maze object for the player to explore
	 * TODO make this
	 */
	public Maze(){
		
	}

}
