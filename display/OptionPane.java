/**
 * 
 */
package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import main.Main;
import net.miginfocom.swing.MigLayout;

/**
 * @author Saurabh Totey
 * The option pane is the pane where a user can click a button to get a desired action rather
 * than typing out the command: the command will just be automatically typed in the chat for consistency's sake
 */
public class OptionPane extends JPanel {
	
	/**
	 * Instancedata for the OptionPane
	 * Used for formmatting and refreshing data and accessing data
	 */
	public Font defaultFont;
	public Font titleFont;
	public Main mainGame;

	/**
	 * Makes this optionpane a jpanel with miglayout and with the given color
	 * Was split off into its own class for readability
	 * @param color the color for the panel
	 * @param defaultFont the font of the normal text
	 * @param titleFont the font of any titles
	 * @param mainGame the main game object so that the pane can send data over to it
	 */
	public OptionPane(Color color, Font defaultFont, Font titleFont, Main mainGame) {
		super(new MigLayout("fill"));
		this.setBackground(color);
		this.defaultFont = defaultFont;
		this.titleFont = titleFont;
		this.mainGame = mainGame;
		this.setOptionsPane(OptionsPaneOptions.DEFAULT);
	}
	
	/**
	 * This is a set of menus of what the optionspane can be set to, the options
	 * pane is always one of these
	 */
	public enum OptionsPaneOptions {
		DEFAULT("Options"), RACES("Choose race"), STATS_GAMBLE_BIG("Choose Stat"), STATS_GAMBLE_SMALL("Choose Stat");

		public String title;

		/**
		 * This is the constructor for the possible optionspanes possibilities
		 * @param title what the optionspane title should be with this specific menu
		 */
		private OptionsPaneOptions(String title) {
			this.title = title;
		}
	}
	
	/**
	 * Sets the option pane to show a certain set of options
	 * @param type the set of options to display
	 * @throws InterruptedException 
	 * @throws InvocationTargetException 
	 */
	public void setOptionsPane(OptionsPaneOptions type) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				removeAll();
				JLabel optionsTitle = new JLabel(type.title);
				optionsTitle.setFont(titleFont);
				optionsTitle.setHorizontalAlignment(SwingConstants.CENTER);
				add(optionsTitle, "dock north");
				switch(type){
					case DEFAULT:
						//TODO add default menu
						JButton invButton = new JButton("Open Inventory");
						invButton.setFont(defaultFont);
						invButton.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e){
								//TODO add inv screen
							}
						});
						add(invButton, "width 90%!, align center, wrap");
						break;
					case RACES:
						JButton[] options = {new JButton("Human"), new JButton("Robot"), new JButton("Shadow"), new JButton("Turtle"), new JButton("Bird")};
						for(JButton button : options){
							button.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent e){
									mainGame.interpretText(((JButton) e.getSource()).getText());
								}
							});
							button.setFont(defaultFont);
							add(button, "width 90%!, align center, wrap");
						}
						break;
					case STATS_GAMBLE_BIG:
						//TODO big stats gambling on optionspane
						break;
					case STATS_GAMBLE_SMALL:
						//TODO small stats gambling on optionspane
						break;
				}
			}
		});
	}

}
