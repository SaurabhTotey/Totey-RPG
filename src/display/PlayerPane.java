/**
 * 
 */
package display;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import main.Main;
import net.miginfocom.swing.MigLayout;

/**
 * @author Saurabh Totey
 * The player pane is the pane that displays the player's information
 */
public class PlayerPane extends JPanel {
	
	/**
	 * An enum set for all stats that will be displayed about the player
	 */
	public enum PlayerStat{
		PLAYER_NAME("Name"), PLAYER_RACES("Race"), PLAYER_EXPERIENCE("Experience"), PLAYER_LEVEL("Level"), PLAYER_HEALTH("Health"), PLAYER_DEFENSE("Defense"), PLAYER_ATTACK("Attack"), PLAYER_SPEED("Speed"),
		PLYAER_EARTH_AFFINITY("Earth"), PLAYER_FIRE_AFFINITY("Fire"), PLAYER_WATER_AFFINITY("Water"), PLAYER_DARKNESS_AFFINITY("Darkness"),
		PLAYER_LIGHT_AFFINITY("Light"), PLAYER_CHAOS_AFFINITY("Chaos"), PLAYER_ORDER_AFFINITY("Order"), PLAYER_PET("Pet"), PLAYER_POTIONS("Potions"),
		PLAYER_ARMOR("Armor"), PLAYER_BOMBS("Bombs");
		
		/**
		 * What the value and titles for the enums will be
		 * this is what the enum will be displayed as
		 */
		public String title;
		private String value;
		
		/**
		 * Constructor for a PlayerStat enum
		 * Takes in the title and automatically sets the value to ??? until changed otherwise
		 * @param title what the name of the enum is
		 */
		private PlayerStat(String title){
			this.title = title;
			this.value = "???";
		}
		
		/**
		 * Sets the value of a PlayerStat enum
		 * If unset, the value will default to "???"
		 * @param newValue the value to set the enum to
		 */
		public void setValue(String newValue){
			this.value = newValue;
		}
		
	}
	
	/**
	 * Instance data for a player pane; includes
	 * A hashmap that maps each PlayerStat enum to list of components
	 * the default font used for regular text
	 * the default font used for titles
	 * the player for which the pane will display info
	 */
	public HashMap<PlayerStat, Component[]> playerInfoPanes = new HashMap<PlayerStat, Component[]>();
	public Font defaultFont;
	public Font titleFont;
	public Main mainGame;
	
	/**
	 * A panel that displays and handles player information
	 */
	public PlayerPane(Color color, Font defaultFont, Font titleFont, Main mainGame) {
		super(new MigLayout("fill"));
		JLabel userTitle = new JLabel("Player Info");
		PlayerStat.PLAYER_RACES.value = "??? / ???";
		userTitle.setFont(titleFont);
		userTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(userTitle, "dock north");
		this.setBackground(color); 
		this.defaultFont = defaultFont;
		this.titleFont = titleFont;
		this.mainGame = mainGame;
		for(PlayerStat playerStatistic : PlayerStat.values()){
			playerInfoPanes.put(playerStatistic, new Component[2]);
			playerInfoPanes.get(playerStatistic)[0] = new JLabel(playerStatistic.title + " ");
			playerInfoPanes.get(playerStatistic)[1] = new JTextField(playerStatistic.value);
			((JLabel) playerInfoPanes.get(playerStatistic)[0]).setHorizontalAlignment(SwingConstants.CENTER);
			((JTextField) playerInfoPanes.get(playerStatistic)[1]).setHorizontalAlignment(SwingConstants.CENTER);
			for(Component item : playerInfoPanes.get(playerStatistic)){
				item.setFont(defaultFont);
			}
			((JTextField) playerInfoPanes.get(playerStatistic)[1]).setFocusable(false);
		}
		
		this.playerInfoPanes.get(PlayerStat.PLAYER_EXPERIENCE)[1] = new JProgressBar(SwingConstants.HORIZONTAL);
		((JProgressBar) this.playerInfoPanes.get(PlayerStat.PLAYER_EXPERIENCE)[1]).setFont(defaultFont);
		((JProgressBar) playerInfoPanes.get(PlayerStat.PLAYER_EXPERIENCE)[1]).setStringPainted(true);
		((JProgressBar) playerInfoPanes.get(PlayerStat.PLAYER_EXPERIENCE)[1]).setString("???");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_NAME)[0], "gapleft 5%");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_NAME)[1], "growx, pushx, span, wrap, gapright 5%");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_EXPERIENCE)[0], "gapleft 5%");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_EXPERIENCE)[1], "growx, pushx, span, wrap, gapright 5%");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_RACES)[0], "gapleft 5%");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_RACES)[1], "growx, pushx, span, wrap, gapright 5%");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_HEALTH)[0], "gapleft 5%");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_HEALTH)[1], "growx");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_ATTACK)[0], "");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_ATTACK)[1], "growx, pushx, wrap, gapright 5%");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_DEFENSE)[0], "gapleft 5%");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_DEFENSE)[1], "growx");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_SPEED)[0], "");
		this.add(playerInfoPanes.get(PlayerStat.PLAYER_SPEED)[1], "growx, pushx, wrap, gapright 5%");
	}
	
	/**
	 * Updates the player pane with the most recent information on the player
	 */
	public void updatePane(){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try{
					((JTextField) (playerInfoPanes.get(PlayerStat.PLAYER_NAME))[1]).setText(mainGame.mainPlayer.name);
					((JProgressBar) playerInfoPanes.get(PlayerStat.PLAYER_EXPERIENCE)[1]).setMinimum(mainGame.mainPlayer.getTotalExpForLevel(mainGame.mainPlayer.getLevel(0)));
					((JProgressBar) playerInfoPanes.get(PlayerStat.PLAYER_EXPERIENCE)[1]).setMaximum(mainGame.mainPlayer.getTotalExpForLevel(mainGame.mainPlayer.getLevel(0) + 1));
					((JProgressBar) playerInfoPanes.get(PlayerStat.PLAYER_EXPERIENCE)[1]).setValue(mainGame.mainPlayer.experience);
					((JProgressBar) playerInfoPanes.get(PlayerStat.PLAYER_EXPERIENCE)[1]).setString("Level " + mainGame.mainPlayer.getLevel(0));
					((JTextField) (playerInfoPanes.get(PlayerStat.PLAYER_RACES))[1]).setText(mainGame.mainPlayer.races[0].stringName + " / " + ((mainGame.mainPlayer.races[1] != null)? mainGame.mainPlayer.races[1].stringName : "???"));
					((JTextField) (playerInfoPanes.get(PlayerStat.PLAYER_HEALTH))[1]).setText(mainGame.mainPlayer.health[0] + " / " + mainGame.mainPlayer.health[1]);
					((JTextField) (playerInfoPanes.get(PlayerStat.PLAYER_ATTACK))[1]).setText(mainGame.mainPlayer.attack[0] + " / " + mainGame.mainPlayer.attack[1]);
					((JTextField) (playerInfoPanes.get(PlayerStat.PLAYER_DEFENSE))[1]).setText(mainGame.mainPlayer.defense[0] + " / " + mainGame.mainPlayer.defense[1]);
					((JTextField) (playerInfoPanes.get(PlayerStat.PLAYER_SPEED))[1]).setText(mainGame.mainPlayer.speed[0] + " / " + mainGame.mainPlayer.speed[1]);
				}catch(NullPointerException e){
					
				}
			}
		});
	}

}
