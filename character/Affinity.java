/**
 * 
 */
package character;

import java.io.Serializable;

/**
 * @author Saurabh Totey
 * These affinities are all the possible affinities a character has
 * These affect the strength of certain spells and actions
 */
public class Affinity implements Serializable{
	
	/**
	 * The possible names for affinities
	 */
	public enum affinityName {
		EARTH, FIRE, WATER, LIGHT, DARKNESS, ORDER, CHAOS;
		
		/**
		 * This method turns the enum for the possible name into a nicely formatted string
		 */
		@Override
		public String toString(){
			return super.toString().substring(0, 1).toUpperCase() + super.toString().substring(1).toLowerCase();
		}
	}
	
	/**
	 * Instance data for individual affinities
	 * Contains affinity amount and the type of affinity
	 */
	public affinityName name;
	public int amount = 0;
	
	private Affinity(affinityName type, int amount){
		this.name = type;
		this.amount = amount;
	}
	
	/**
	 * Makes an array of affinities and sets them to the given starting amounts for each affinity
	 * @param startingAmts the starting amount of each affinity
	 * @return an array containing each possible affinity with its given amounts
	 */
	public static Affinity[] makeAffinities(int[] startingAmts){
		Affinity[] toReturn = new Affinity[affinityName.values().length];
		for(int i = 0; i < toReturn.length; i++){
			toReturn[i] = new Affinity(affinityName.values()[i], startingAmts[i]);
		}
		return toReturn;
	}
	
	/**
	 * Makes an array of affinities for each affinity, and makes each affinity start at 0
	 * This calls on the above method and passes an array of all 0s, but the code from the above
	 * method could have been copied with 0s hardcoded in; this was a computationally more expensive
	 * style choice
	 * @return
	 */
	public static Affinity[] makeAffinities(){
		int[] startingAmts = new int[affinityName.values().length];
		for(int i = 0; i < startingAmts.length; i++){
			startingAmts[i] = 0;
		}
		return makeAffinities(startingAmts);
	}

}