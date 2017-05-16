/**
 * 
 */
package world;

import java.awt.Point;
import java.util.HashMap;

import display.OptionPane.OptionsPaneOptions;
import main.Main;
import world.Maze.Tile;

/**
 * @author Saurabh Totey
 * An action class that is the action that is performed after a certain tile is stepped on
 * Not an abstract class, because tiles may need empty actions, and thus there would be no reason to
 * overwrite this class to provide a method that does nothing
 */
public class StepAction{
	
	/**
	 * A list of tiles that maps to the appropriate action
	 */
	public static HashMap<String, StepAction> tileActions = new HashMap<String, StepAction>();

	/**
	 * A constructor for an action which, given a tile, will put itself in the hashmap of tiles to actions
	 */
	public StepAction(String tile) {
		tileActions.putIfAbsent(tile, this);
	}
	
	/**
	 * What the actual action is based on the given x and y coordinates of where to perform the action
	 * Most actions should scale from the distance from the center
	 * If not overwritten, performAction will just set the pain mode to the default pane
	 * @param xCoordinate the x coordinate of where the action should occur
	 * @param yCoordinate the y coordinate of where the action should occur
	 * @throws InterruptedException 
	 */
	public void performAction(int xCoordinate, int yCoordinate) throws InterruptedException{
		try{
			Main.main.gui.optionsPane.setOptionsPane(OptionsPaneOptions.DEFAULT);
			Main.willInterpretIncoming = true;
		}catch(Exception e){
			
		}
	}
	
	/**
	 * The set of actions for what should happen if the player steps on an enemy tile
	 * Will start a battle with an enemy
	 */
	public class battleEnemy extends StepAction{
		public battleEnemy(){
			super(Tile.ENEMY.representation);
		}
		@Override
		public void performAction(int xCoordinate, int yCoordinate) throws InterruptedException{
			super.performAction(xCoordinate, yCoordinate);
			//TODO start battle with enemy
		}
	}
	
	/**
	 * The set of actions for what should happen if the player attempts to walk on a wall
	 * Will decrement the player's health
	 */
	public class bonkHead extends StepAction{
		public bonkHead(){
			super(Tile.WALL.representation);
		}
		@Override
		public void performAction(int xCoordinate, int yCoordinate) throws InterruptedException{
			super.performAction(xCoordinate, yCoordinate);
			//TODO decrement player's health
		}
	}
	
	/**
	 * The set of actions for what should happen if the player walks on an item tile
	 * Will give the player an item
	 */
	public class gatherItem extends StepAction{
		public gatherItem(){
			super(Tile.ITEM.representation);
		}
		@Override
		public void performAction(int xCoordinate, int yCoordinate) throws InterruptedException{
			super.performAction(xCoordinate, yCoordinate);
			//TODO give the player an item
		}
	}
	
	/**
	 * The set of actions for what should happen if the player walks on a boss tile
	 * Initiates a boss fight
	 */
	public class startBossFight extends StepAction{
		public startBossFight(){
			super(Tile.BOSS.representation);
		}
		@Override
		public void performAction(int xCoordinate, int yCoordinate) throws InterruptedException{
			super.performAction(xCoordinate, yCoordinate);
			//TODO start boss fight
		}
	}
	
	/**
	 * The set of actions for what should happen if the player walks on a portal tile
	 * Will show players all of their visited teleporters and allow them to travel to them
	 */
	public class teleportWithPortal extends StepAction{
		public teleportWithPortal(){
			 super(Tile.PORTAL.representation);
		}
		@Override
		public void performAction(int xCoordinate, int yCoordinate) throws InterruptedException{
			super.performAction(xCoordinate, yCoordinate);
			new Thread(new Runnable(){
				@Override
				public void run(){
					if(Main.main == null || Main.main.world == null){
						return;
					}
					Point portalLocation = new Point(xCoordinate, yCoordinate);
					if(!Main.main.world.discoveredPortals.contains(portalLocation)){
						Main.main.world.discoveredPortals.add(portalLocation);
					}
					Main.log("To which coordinates will you teleport to?");
					Main.main.gui.optionsPane.setOptionsPane(OptionsPaneOptions.TELEPORT_OPTIONS);
					String desiredLocationToGo;
					try {
						desiredLocationToGo = Main.waitForInput(true);
					} catch (InterruptedException e1) {
						desiredLocationToGo = "Cancel";
					}
					try{
						String[] coords = desiredLocationToGo.substring(1, desiredLocationToGo.length() - 1).split(", ");
						Point toTeleportTo = new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
						if(Main.main.world.discoveredPortals.contains(toTeleportTo)){
							Main.main.world.movePlayerTo(new int[]{(int) toTeleportTo.getX(), (int) toTeleportTo.getY()});
							Main.log("You teleported to the teleporter at (" + (int) toTeleportTo.getX() + ", " + (int) toTeleportTo.getY() + ")");
						}else{
							throw new Exception();
						}
					}catch(Exception e){
						Main.log("You decided not to teleport");
					}
					Main.main.gui.optionsPane.setOptionsPane(OptionsPaneOptions.DEFAULT);
				}
			}).start();
		}
	}
	
	/**
	 * The set of actions for what should happen if the player walks on a villager tile
	 * Starts a dialogue with a villager
	 */
	public class dialogueWithVillager extends StepAction{
		public dialogueWithVillager(){
			super(Tile.VILLAGER.representation);
		}
		@Override
		public void performAction(int xCoordinate, int yCoordinate) throws InterruptedException{
			super.performAction(xCoordinate, yCoordinate);
			//TODO set up dialogue with villager
		}
	}
	
	/**
	 * Constructs all of the step actions which automatically register themselves to the hashmap of tiles to actions
	 */
	public static void initAllStepActions(){
		StepAction emptyAction = new StepAction(Tile.EMPTY.representation);
		emptyAction.new battleEnemy();
		emptyAction.new bonkHead();
		emptyAction.new gatherItem();
		emptyAction.new startBossFight();
		emptyAction.new teleportWithPortal();
		emptyAction.new dialogueWithVillager();
	}

}
