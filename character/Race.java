/**
 * 
 */
package character;

import java.util.HashMap;

/**
 * @author Saurabh Totey
 * A class to define a set of specific objects that are attributes for all characters
 * the specific race objects are static and are human, robot, shadow, turtle, and bird
 */
public class Race {
	
	/**
	 * This enumeration type is just a list of all racenames
	 * it's only ever used internally
	 * this was used instead of strings or an infinitely recursive race definition
	 */
	private enum RaceName{
		HUMAN, ROBOT, SHADOW, TURTLE, BIRD;
		
		/**
		 * Gives a string representation of a raceName
		 * @return Gives back a perfectly formatted version of the race name
		 */
		@Override
		public String toString(){
			return super.toString().substring(0, 1).toUpperCase() + super.toString().substring(1).toLowerCase();
		}
		
	}
	
	/**
	 * This is a set of static races
	 * These are five race objects, each has a name, two races they are strong against, and two races they are weak against
	 * Below is a hashmap that returns one of the races based on a given string
	 */
	public static Race human = new Race(RaceName.HUMAN, RaceName.ROBOT, RaceName.TURTLE, RaceName.SHADOW, RaceName.BIRD);
	public static Race robot = new Race(RaceName.ROBOT, RaceName.SHADOW, RaceName.BIRD, RaceName.HUMAN, RaceName.TURTLE);
	public static Race shadow = new Race(RaceName.SHADOW, RaceName.TURTLE, RaceName.HUMAN, RaceName.ROBOT, RaceName.BIRD);
	public static Race turtle = new Race(RaceName.TURTLE, RaceName.BIRD, RaceName.ROBOT, RaceName.HUMAN, RaceName.SHADOW);
	public static Race bird = new Race(RaceName.BIRD, RaceName.HUMAN, RaceName.SHADOW, RaceName.ROBOT, RaceName.TURTLE);
	public static HashMap<String, Race> stringToRace = getStringsToRaces();
	
	/**
	 * These are multipliers for damage and other things
	 * if a character of a race is fighting another character of a race, this determines the multipliers based on their race affinities
	 */
	public static int strongAgainstMultiplier = 2;
	public static int weakAgainstMultiplier = 1 / strongAgainstMultiplier;
	public static int neutralAgainstMultplier = 1;
	
	/**
	 * Instance data for race objects
	 * these just store the race affinities and race names
	 */
	public RaceName name;
	public String stringName;
	public RaceName[] strongAgainst = new RaceName[2];
	public RaceName[] weakAgainst = new RaceName[2];
	
	/**
	 * A constructor for a simple race object, should never be called outside of this class
	 * Instead, the static race objects above should be used
	 * @param selfName the name of the constructed race
	 * @param strongAgainst one other race the current race is strong against
	 * @param secondStrength a second other race the current race is strong against
	 * @param weakAgainst one other race the current race is weak against
	 * @param secondWeakness a second other race the current race is weak against
	 */
	private Race(RaceName selfName, RaceName strongAgainst, RaceName secondStrength, RaceName weakAgainst, RaceName secondWeakness){
		this.name = selfName;
		this.stringName = name.toString();
		this.strongAgainst[0] = strongAgainst;
		this.strongAgainst[1] = secondStrength;
		this.weakAgainst[0] = weakAgainst;
		this.weakAgainst[1] = secondWeakness;
	}
	
	/**
	 * To string method for getting race as a string
	 * @return the race as a string
	 */
	@Override
	public String toString(){
		return this.name.toString();
	}
	
	/**
	 * The entry point for the initializing the stringToRace hashmap
	 * If a new race is added, to add a string value to it, it needs to be put here
	 */
	public static HashMap<String, Race> getStringsToRaces(){
		HashMap<String, Race> toReturn = new HashMap<String, Race>();
		toReturn.put(human.stringName, human);
		toReturn.put(robot.stringName, robot);
		toReturn.put(shadow.stringName, shadow);
		toReturn.put(turtle.stringName, turtle);
		toReturn.put(bird.stringName, bird);
		return toReturn;
	}

}
