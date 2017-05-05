/**
 * 
 */
package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import main.Main;
import world.Maze.Direction;

/**
 * @author Saurabh Totey
 * This pane displays the main activity that is going on at the moment
 * Is usually either a maze, a battle, or the user's inventory
 */
public class MainPane extends JPanel {
	
	/**
	 * The mode of the main pane
	 * What it is displaying
	 */
	public enum MainPaneMode{
		MAZE, INVENTORY;
	}
	
	/**
	 * Instancedata for the MainPane
	 * Used for formmatting and refreshing data and accessing data
	 */
	public Font defaultFont;
	public Font titleFont;
	public Main mainGame;
	public MainPaneMode mode;

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
		this.requestFocusInWindow();
	}
	
	/**
	 * Sets the mode of the mainpane
	 * Makes necessary changes based on the mode
	 * @param newMode the new mode of the main pane
	 */
	public void setMode(MainPaneMode newMode){
		this.removeAll();
		switch(newMode){
			case MAZE:
				JTextArea mazeToDisp = new JTextArea();
				mazeToDisp.setFont(defaultFont);
				mazeToDisp.setEditable(false);
				this.add(mazeToDisp, "push, grow, fit");
				this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "up");
				this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "left");
				this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "down");
				this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "right");
				this.getActionMap().put("down", new MoveAction(Direction.DOWN));
				this.getActionMap().put("left", new MoveAction(Direction.LEFT));
				this.getActionMap().put("up", new MoveAction(Direction.UP));
				this.getActionMap().put("right", new MoveAction(Direction.RIGHT));
				break;
			case INVENTORY:
				//TODO make this once inventory screen is made
				break;
		}
		this.mode = newMode;
	}
	
	private class MoveAction extends AbstractAction{
		
		public Direction direction;

		public MoveAction(Direction direction){
			this.direction = direction;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			mainGame.world.move(direction);
		}
		
	}

}
