/**
 * 
 */
package character;

/**
 * @author Saurabh Totey
 * The class for a standard enemy
 */
public class Enemy extends Character {

	/**
	 * The constructor for an enemy
	 * Makes a character with a random name, race, and level
	 * @param value how good the character should be
	 */
	public Enemy(int value) {
		super(makeRandomName(), getRandomRace(), (int) (0.75 * value + 0.5 * Math.random() * value));
	}
	
	public static String makeRandomName(){
		return "Saurabh";
	}
	
	public static Race getRandomRace(){
		return Race.bird;
	}

}
