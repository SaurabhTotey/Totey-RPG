/**
 * 
 */
package display;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import main.Main;

/**
 * @author Saurabh Totey
 * This pane displays the main activity that is going on at the moment
 * Is usually either a maze, a battle, or the user's inventory
 */
public class MainPane extends JPanel {
	
	/**
	 * Instancedata for the MainPane
	 * Used for formmatting and refreshing data and accessing data
	 */
	public Font defaultFont;
	public Font titleFont;
	public Main mainGame;

	/**
	 * A constructor for the main pane class
	 * @param color the color for the panel
	 * @param defaultFont the font of the normal text
	 * @param titleFont the font of any titles
	 * @param mainGame the main game object so that the pane can send data over to it
	 */
	public MainPane(Color color, Font defaultFont, Font titleFont, Main mainGame) {
		this.setBackground(color);
		this.defaultFont = defaultFont;
		this.titleFont = titleFont;
		this.mainGame = mainGame;
	}

}
