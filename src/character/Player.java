/**
 * 
 */
package character;

/**
 * @author Saurabh Totey
 *
 */
public class Player extends Character {

	public int bombs;
	public boolean areStatsVisible = true;
	
	/**
	 * This is the constructor for a player
	 * It overrides the character constructor because players can only ever be level 1
	 * @param name
	 * @param races
	 */
	public Player(String name, Race initialRace) {
		super(name, initialRace, 1);
	}
	
	
	/**
	 * TODO THIS METHOD IS INCOMPLETE
	 * Using a bomb decrements how many bombs the user has, and it destroys part of the maze
	 */
	public void useBomb(){
		if(this.bombs > 0){
			//TODO destroy surroundings in maze
			this.bombs--;
		}
	}

}
