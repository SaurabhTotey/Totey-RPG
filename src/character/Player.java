/**
 * 
 */
package character;

import java.io.Serializable;
import java.util.HashMap;
import java.util.function.Function;
import display.OptionPane.OptionsPaneOptions;
import main.Main;
import world.Maze;
import world.Maze.Direction;
import world.Maze.Tile;
import world.StepAction;
import world.StepAction.teleportWithPortal;

/**
 * @author Saurabh Totey
 *
 */
public class Player extends Character {

    /**
     * A set of all possible actions one can do to the player's items
     * The number of items can be ascertained with GET, an item can be consumed with USE, or an item can be received with ADD
     */
	public enum ItemUsage{
		GET, USE, ADD
	}

	private int bombs;
	private int tpPotions;
	private int potions;
	public HashMap<String, Function<ItemUsage, Integer>> stringToItem = new HashMap<String, Function<ItemUsage, Integer>>();
	
	/**
	 * This is the constructor for a player
	 * It overrides the character constructor because players can only ever be level 1
	 * @param name the name of the player
	 * @param initialRace what race the player should start as
	 */
	public Player(String name, Race initialRace) {
		super(name, initialRace, 1);
		stringToItem.put("bomb", (Function<ItemUsage, Integer> & Serializable)(ItemUsage use) -> {
			switch(use){
                case ADD:
                    bombs++;
                    break;
                case USE:
                    if(bombs > 0){
                        for(Direction direction : Direction.values()){
                            int[] chunkCoords = Maze.absoluteToChunkCoordinates(new int[]{Maze.chunkCoordinatesToAbsolute(Main.main.world.playerLocation)[0] + direction.xModifier, Maze.chunkCoordinatesToAbsolute(Main.main.world.playerLocation)[1] + direction.yModifier});
                            Main.main.world.get(chunkCoords[0], chunkCoords[1]).terrain[chunkCoords[2]][chunkCoords[3]] = Maze.getEmptyTile();
                        }
                        bombs--;
                    }
                    break;
                case GET:
                    break;
            }
			return bombs;
		});
		stringToItem.put("tppotion", (Function<ItemUsage, Integer> & Serializable)(ItemUsage use) -> {
			switch(use){
                case ADD:
                    tpPotions++;
                    break;
                case USE:
                    if(tpPotions > 0){
                        try {
                            StepAction.initAllStepActions();
                            ((teleportWithPortal) StepAction.tileActions.get(Tile.PORTAL.representation)).performAction();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        tpPotions--;
                    }
                    break;
                case GET:
                    break;
            }
			return tpPotions;
		});
		stringToItem.put("potion", (Function<ItemUsage, Integer> & Serializable)(ItemUsage use) -> {
			switch(use){
                case ADD:
                    potions++;
                    break;
                case USE:
                    if(potions > 0){
                        health[0] = (health[0] + health[1] / 5 > health[1])? health[1] : health[0] + health[1] / 5;
                        potions--;
                    }
                    break;
                case GET:
                    break;
            }
			return potions;
		});
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
					Thread.sleep(50);
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
