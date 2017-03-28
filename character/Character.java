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
	public Race[] races = new Race[2];
	public String name = "";
	private int experience = 0;
	private int[] health = new int[2];
	private int[] attack = new int[2];
	private int[] defense = new int[2];
	private int[] speed = new int[2];
	public HashMap<Affinity, Integer> affinities = new HashMap<Affinity, Integer>();{
		for(Affinity affinity : Affinity.values()){
			affinities.put(affinity, 0);
		}
	}
	public Item armor;
	public Item pet;
	public int potions;
	
	/**
	 * The constructor for any character object
	 * Constructor automatically handles character stats based on given level
	 * @param name the name of the character
	 * @param races the character's races
	 * @param level what level the character should start at (affects stat generation on construction)
	 */
	public Character(String name, Race[] races, int level){
		this.name = name;
		this.races = races;
		this.experience = (level - 1) * (level - 1) / 4;
		this.gainLevelUpStats();
		this.getLevel(0);
		this.restoreStats();
	}
	
	/**
	 * The constructor for any character object
	 * Constructor automatically handles chracter stats based on given level
	 * This constructor only takes in one race as opposed to an array of races
	 * @param name the name of the character
	 * @param race one of the character's races
	 * @param level what level the character should start at (affects stat generation on construction)
	 */
	public Character(String name, Race race, int level){
		this.name = name;
		this.races[0] = race;
		this.experience = (level - 1) * (level - 1) / 4;
		this.gainLevelUpStats();
		this.getLevel(0);
		this.restoreStats();
	}
	
	/**
	 * This method takes race object(s) and returns a multiplier based on race affinities
	 * Race affinities are defined above
	 * @param opponentRaces races to find affinity with and return multiplier for
	 * @return multiplier for actions used against the races given in parameter
	 */
	public int getRaceMultiplierAgainst(Race[] opponentRaces){
		int multiplier = 1;
		for(Race opposingRace : opponentRaces){
			for(Race selfRace : this.races){
				for(int i = 0; i < 2; i++){
					if(opposingRace != selfRace && opposingRace != null && selfRace != null){
						if(opposingRace.name == selfRace.strongAgainst[i]){
							multiplier *= Race.strongAgainstMultiplier;
						}else if(opposingRace.name == selfRace.weakAgainst[i]){
							multiplier *= Race.weakAgainstMultiplier;
						}
					}
				}
			}
		}
		return multiplier;
	}
	
	/**
	 * The method adds experience to the character and returns the new level of the character
	 * If the experience gain was enough to push the character to a new level, it gains stats accordingly
	 * @param experienceGained how much experience to add to the character
	 * @return the level of the character after experience gain
	 */
	public int getLevel(int experienceGained){
		int level = (int) (2 * Math.sqrt(this.experience)) + 1;
		this.experience += experienceGained;
		for(int i = level; i < (int) (2 * Math.sqrt(this.experience)) + 1; i++){
			this.gainLevelUpStats();
		}
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
	
	/**
	 * Using a potion restores 1/10 of the user's max health and reduces the number of potions the user has
	 * If the user's use of a potion would put them over maximum health, instead, they are healed to maximum health
	 */
	public void usePotion(){
		if(this.potions > 0){
			this.health[0] = (this.health[0] + ((int) (this.health[1] / 10)) > health[1])? this.health[1] : (int) (this.health[1] / 10);
			this.potions--;
		}
	}
	
	/**
	 * Getter method for character health
	 * @return a string of the user's current health / max health
	 */
	public String getHealth(){
		return this.health[0] + " / " + this.health[1];
	}
	
	/**
	 * Getter method for character attack
	 * @return a string of the user's current attack / max attack
	 */
	public String getAttack(){
		return this.attack[0] + " / " + this.attack[1];
	}
	
	/**
	 * Getter method for character defense
	 * @return a string of the user's current defense / max defense
	 */
	public String getDefense(){
		return this.defense[0] + " / " + this.defense[1];
	}
	
	/**
	 * Getter method for character speed
	 * @return a string of the user's current speed / max speed
	 */
	public String getSpeed(){
		return this.speed[0] + " / " + this.speed[1];
	}

}
