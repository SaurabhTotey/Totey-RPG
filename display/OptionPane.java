/**
 * 
 */
package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.DefaultFormatter;

import main.Main;
import net.miginfocom.swing.MigLayout;
import world.Maze;

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
	public OptionsPaneOptions mode;

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
		DEFAULT("Options"), RACES("Choose race"), STATS_GAMBLE_BIG("Choose Stat"), STATS_GAMBLE_SMALL("Choose Stat"), TELEPORT_OPTIONS("Teleport where?");

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
				mode = type;
				removeAll();
				JLabel optionsTitle = new JLabel(type.title);
				optionsTitle.setFont(titleFont);
				optionsTitle.setHorizontalAlignment(SwingConstants.CENTER);
				add(optionsTitle, "dock north");
				switch(type){
					case DEFAULT:
						//TODO finish default menu
						JButton invButton = new JButton("Open Inventory");
						invButton.setFont(defaultFont);
						invButton.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e){
								//TODO add inv screen
							}
						});
						add(invButton, "width 90%!, align center, wrap");
						JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(Maze.displayWidth, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
						JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(Maze.displayHeight, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
						JLabel titleForWidthSpinner = new JLabel("Adjust your maze width");
						JLabel titleForHeightSpinner = new JLabel("Adjust your maze height");
						titleForWidthSpinner.setFont(defaultFont);
						titleForHeightSpinner.setFont(defaultFont);
						titleForWidthSpinner.setHorizontalAlignment(SwingConstants.CENTER);
						titleForHeightSpinner.setHorizontalAlignment(SwingConstants.CENTER);
						widthSpinner.setFont(defaultFont);
						heightSpinner.setFont(defaultFont);
						((DefaultEditor) widthSpinner.getEditor()).getTextField().setHorizontalAlignment(SwingConstants.CENTER);
						((DefaultEditor) heightSpinner.getEditor()).getTextField().setHorizontalAlignment(SwingConstants.CENTER);
						((DefaultFormatter) ((JFormattedTextField) widthSpinner.getEditor().getComponent(0)).getFormatter()).setCommitsOnValidEdit(true);
						((DefaultFormatter) ((JFormattedTextField) heightSpinner.getEditor().getComponent(0)).getFormatter()).setCommitsOnValidEdit(true);
						widthSpinner.addChangeListener(new ChangeListener(){
							@Override
					        public void stateChanged(ChangeEvent e) {
					            Maze.displayWidth = (Integer) ((JSpinner) e.getSource()).getValue();
					        }
						});
						heightSpinner.addChangeListener(new ChangeListener(){
							@Override
					        public void stateChanged(ChangeEvent e) {
					            Maze.displayHeight = (Integer) ((JSpinner) e.getSource()).getValue();
					        }
						});
						add(titleForWidthSpinner, "width 90%, align center, wrap");
						add(widthSpinner, "width 90%, align center, wrap");
						add(titleForHeightSpinner, "width 90%, align center, wrap");
						add(heightSpinner, "width 90%, align center, wrap");
						JButton saveButton = new JButton("Save game");
						saveButton.setFont(defaultFont);
						saveButton.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e) {
								Main.log("Saving game!");
								try{
									File whereToSave = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\My Games\\ToteyRPG\\Saves\\" + mainGame.gameIdentifier + ".save");
									if(!whereToSave.exists()){
										whereToSave.getParentFile().mkdirs();
									}
									FileOutputStream fileOut = new FileOutputStream(whereToSave.getPath());
									ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
									objectOut.writeObject(mainGame);
									Main.log("Game has been saved!");
									objectOut.close();
									fileOut.close();
								}catch(IOException er){
									er.printStackTrace();
								}
							}
						});
						add(saveButton, "width 90%, align center, wrap");
						//TODO add save and quit button
						break;
					case RACES:
						JButton[] options = {new JButton("Human"), new JButton("Robot"), new JButton("Shadow"), new JButton("Turtle"), new JButton("Bird")};
						for(JButton button : options){
							button.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent e){
									Main.interpretText(((JButton) e.getSource()).getText());
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
					case TELEPORT_OPTIONS:
						ActionListener sendContents = new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e) {
								Main.interpretText(((JButton) e.getSource()).getText());
							}
						};
						for(Point portalCoords : mainGame.world.discoveredPortals){
							JButton possibleLocation = new JButton("(" + (int) portalCoords.getX() + ", " + (int) portalCoords.getY() + ")");
							possibleLocation.setFont(defaultFont);
							possibleLocation.addActionListener(sendContents);
							add(possibleLocation, "width 90%, align center, wrap");
						}
						JButton cancelButton = new JButton("Cancel");
						cancelButton.setFont(defaultFont);
						cancelButton.addActionListener(sendContents);
						add(cancelButton, "width 90%, align center, wrap");
						break;
				}
			}
		});
	}

}
