/**
 * 
 */
package character;

import java.util.ArrayList;
import java.util.Arrays;

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
	
	/**
	 * A method for making random names
	 * It doesn't work very well and the names come out very alien
	 * Yet it is still a way to generate random names
	 * Is needed for making random enemies that have some name
	 * TODO make this better
	 * @return a random name
	 */
	public static String makeRandomName(){
		String toReturn = "";
		ArrayList<String> vowels = new ArrayList<String>(Arrays.asList("a", "e", "i", "o", "u"));
		ArrayList<String> consonants = new ArrayList<String>(Arrays.asList("b", "c", "d", "f", "g", "h", "i", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z"));
		boolean isPrevSyllableVowel = (int) (Math.random() * 2) == 1;
		boolean shouldContinue;
		do{
			isPrevSyllableVowel ^= true;
			shouldContinue = (int) (Math.random() * 5) < 2;
			boolean toCont;
			do{
				toCont = (int) (Math.random() * 2) == 1;
				toReturn += (isPrevSyllableVowel)? consonants.get((int)(Math.floor(Math.random() * consonants.size()))) : vowels.get((int) (Math.random() * vowels.size()));
			}while(toCont);
		}while(shouldContinue);
		return toReturn.substring(0, 1).toUpperCase() + toReturn.substring(1);
	}
	
	/**
	 * Returns a random race from all available races
	 * @return a random race
	 */
	public static Race getRandomRace(){
		String[] listOfRaces = new String[Race.getStringsToRaces().keySet().size()];
		int counter = 0;
		for(String raceName : Race.getStringsToRaces().keySet()){
			listOfRaces[counter] = raceName;
			counter++;
		}
		return Race.getStringsToRaces().get(listOfRaces[(int) (Math.random() * listOfRaces.length)]);
	}

}
