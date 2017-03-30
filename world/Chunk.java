/**
 * 
 */
package world;

/**
 * @author Saurabh Totey
 * The building blocks of a maze
 * The maze is built out of a collection of these chunks
 */
public class Chunk {
	
	/**
	 * These are common properties of all of the chunks
	 * These sets of variables store the location as well as the terrain
	 */
	public static int chunkLength;
	public int x;
	public int y;
	public String[] terrain;

	/**
	 * A chunk object
	 * The objects just store their x and y locations as well as their actual terrain
	 * TODO make this
	 */
	public Chunk() {

	}

}
