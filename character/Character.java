/**
 * 
 */
package character;

import java.util.HashMap;

/**
 * @author Saurabh Totey
 * A class of all characters (players, enemies, bosses, etc) with all their common attributes
 */
public class Character {	
	
	/**
	 * These are all of the common characteristics and attributes of characters
	 * The health, attack, defense, and speed stats are stored as arrays: the first index of the array is the stat's current value, and the second index is the stat's max value
	 * Every character initially starts off with 0 of each affinity
	 */
	public Race race;
	public String name = "";
	private int experience = 0;
	public int[] health = new int[2];
	public int[] attack = new int[2];
	public int[] defense = new int[2];
	public int[] speed = new int[2];
	public HashMap<Affinity, Integer> affinities = new HashMap<Affinity, Integer>();{
		for(Affinity affinity : Affinity.values()){
			affinities.put(affinity, 0);
		}
	}
	
	/**
	 * The constructor for any character object
	 * Constructor automatically handles character stats based on given level
	 * @param name the name of the character
	 * @param race the character's race
	 * @param level what level the character should start at (affects stat generation on construction)
	 */
	public Character(String name, Race race, int level){
		this.name = name;
		this.race = race;
		this.experience = (level - 1) * (level - 1) / 4;
		for(int i = 0; i < getLevel(0); i++){
			this.gainLevelUpStats();
		}
		this.restoreStats();
	}
	
	/**
	 * The method adds experience to the character and returns the new level of the character
	 * @param experienceGained how much experience to add to the character
	 * @return the level of the character after experience gain
	 */
	public int getLevel(int experienceGained){
		this.experience += experienceGained;
		return (int) (2 * Math.sqrt(this.experience)) + 1;
	}
	
	/**
	 * This method adds stats to the character as if it had leveled up
	 */
	public void gainLevelUpStats(){
		health[1] += ((int) (3 * Math.random())) + 3;
		attack[1] += ((int) (3 * Math.random())) + 3;
		defense[1] += ((int) (3 * Math.random())) + 3;
		speed[1] += ((int) (3 * Math.random())) + 3;
	}
	
	/**
	 * This method restores current stats to max stats and basically undoes any affects of battle
	 */
	public void restoreStats(){
		health[0] = health[1];
		attack[0] = attack[1];
		defense[0] = defense[1];
		speed[0] = speed[1];
	}
}
