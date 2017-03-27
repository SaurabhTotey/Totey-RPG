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
	
	public Player(String name, Race[] races) {
		super(name, races, 1);
	}
	
	
	/**
	 * THIS METHOD IS INCOMPLETE
	 * Using a bomb decrements how many bombs the user has, and it destroys part of the maze
	 */
	public void useBomb(){
		if(this.bombs > 0){
			//TODO destroy surroundings in maze
			this.bombs--;
		}
	}

}
