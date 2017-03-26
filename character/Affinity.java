/**
 * 
 */
package character;

/**
 * @author Saurabh Totey
 * These affinities are all the possible affinities a character has
 * These affect the strength of certain spells
 */
public enum Affinity{
	EARTH, FIRE, WATER, LIGHT, DARKNESS, ORDER, CHAOS;
	
	@Override
	public String toString(){
		return super.toString().substring(0, 1).toUpperCase() + super.toString().substring(1).toLowerCase();
	}
}
