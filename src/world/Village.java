/**
 * 
 */
package world;

/**
 * @author Saurabh Totey
 * A village is a chunk with some extra properties
 */
public class Village extends Chunk {
	
	/**
	 * Extra properties that a village has that a chunk doesn't
	 * Used for TODO find uses
	 */
	
	/**
	 * A village constructor
	 * Makes the village object and gives it the same properties as its would-have-been chunk counterpart, except
	 * it adds extra features and is generally more useful to the player (and thus more rare)
	 * TODO make this
	 */
	public Village(int x, int y, String emptyTile, String wallTile){
		super(makeVillage(emptyTile, wallTile), x, y, emptyTile, wallTile);
	}
	
	/**
	 * Makes village terrain
	 * @param emptyTile
	 * @param wallTile
	 * @return
	 */
	private static String[][] makeVillage(String emptyTile, String wallTile){
		String[][] villageAppearance = null; //TODO make village gen
		return villageAppearance;
	}

}
