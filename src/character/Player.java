/**
 * 
 */
package character;

import display.OptionPane.OptionsPaneOptions;
import main.Main;

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
	
	/**
	 * An altered version of the getLevel function that not only adds experience to the player and
	 * accordingly updates their stats, but it also automatically displays the stat gambling page
	 * for each level they got
	 * @param experienceToAdd the amount of experience to add to the player
	 * @return the level of the player after adding the given experience
	 */
	@Override
	public int getLevel(int experienceToAdd){
		int levelBefore = super.getLevel(0);
		int levelAfter = super.getLevel(experienceToAdd);
		new Thread(() -> {
			for(int i = levelBefore; i < levelAfter; i++){
				Main.main.gui.optionsPane.setOptionsPane(OptionsPaneOptions.STATS_GAMBLE);
				try {
					while(Main.main.gui.optionsPane.canSwitchMode == false){
						Thread.sleep(20);
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}).start();
		return levelAfter;
	}

}
