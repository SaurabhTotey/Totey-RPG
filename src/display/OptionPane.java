/**
 * 
 */
package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

import display.MainPane.MainPaneMode;
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
	public volatile boolean canSwitchMode = true;

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
		DEFAULT("Options"), RACES("Choose race"), STATS_GAMBLE("Choose Stat"), TELEPORT_OPTIONS("Teleport where?");

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
		try{
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if(!canSwitchMode){
						return;
					}
					canSwitchMode = false;
					mode = type;
					removeAll();
					JLabel optionsTitle = new JLabel(type.title);
					optionsTitle.setFont(titleFont);
					optionsTitle.setHorizontalAlignment(SwingConstants.CENTER);
					add(optionsTitle, "dock north");
					switch(type){
						case DEFAULT:
							JButton invButton = new JButton(((mainGame.gui.mainPane.mode == MainPaneMode.MAZE)? "Open" : "Close") + " Inventory");
							invButton.setFont(defaultFont);
							invButton.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent e){
									mainGame.gui.mainPane.mode = (mainGame.gui.mainPane.mode == MainPaneMode.MAZE)? MainPaneMode.INVENTORY : MainPaneMode.MAZE;
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
							JButton saveButton = new JButton("Save & Quit");
							saveButton.setFont(defaultFont);
							saveButton.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent e) {
									mainGame.gui.quitProcedure.run();
								}
							});
							add(saveButton, "width 90%, align center, wrap");
							canSwitchMode = true;
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
							canSwitchMode = true;
							break;
						case STATS_GAMBLE:
							canSwitchMode = false;
							JLabel[] statLabels = {new JLabel("Health"), new JLabel("Attack"), new JLabel("Defense"), new JLabel("Speed")};
							JTextField[] stats = new JTextField[statLabels.length];
							JButton[] confirmStatButtons = new JButton[statLabels.length];
							for(int i = 0; i < statLabels.length; i++){
								stats[i] = new JTextField();
								confirmStatButtons[i] = new JButton("Confirm");
								confirmStatButtons[i].addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										int index = -1;
										for(int i = 0; i < statLabels.length; i++){
											if(arg0.getSource().equals(confirmStatButtons[i])){
												index = i;
											}
										}
										int toAdd = Integer.parseInt(stats[index].getText());
										int[] stat = mainGame.mainPlayer.getStat(statLabels[index].getText());
										stat[0] += toAdd;
										stat[1] += toAdd;
										canSwitchMode = true;
										setOptionsPane(OptionsPaneOptions.DEFAULT);
									}
								});
								stats[i].setEditable(false);
								statLabels[i].setFont(defaultFont);
								stats[i].setFont(defaultFont);
								add(statLabels[i], "gapleft 5%, growx");
								add(stats[i], "growx");
								add(confirmStatButtons[i], "gapright 5%, growx, wrap");
							}
							Point[] weightsVsRolls = {new Point(2, 10), new Point(3, 8), new Point(4, 6), new Point(5, 4), new Point(-1, 2), new Point(10, 1)}; // Point x is roll, point y is weight
							new Thread(() -> 
							{
								int largestWeight = 0;
								for(Point weightAndRoll : weightsVsRolls){
									largestWeight = ((int) weightAndRoll.getY() > largestWeight)? (int) weightAndRoll.getY() : largestWeight;
								}
								while(mode == OptionsPaneOptions.STATS_GAMBLE){
									long startTime = System.currentTimeMillis();
									for(int i = 0; i < stats.length; i++){
										int[] roll;
										do{
											roll = new int[]{(int) (Math.random() * stats.length), (int) (Math.random() * largestWeight)};
										}while(weightsVsRolls[roll[0]].getY() < roll[1]);
										stats[i].setText("" + (int) weightsVsRolls[roll[0]].getX());
									}
									long endTime = System.currentTimeMillis();
									try{
										Thread.sleep(300 - endTime + startTime);
									}catch(InterruptedException e){
										
									}
								}
							}).start();
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
							canSwitchMode = true;
							break;
					}
				}
			});
		}catch(Exception e){
			this.setOptionsPane(OptionsPaneOptions.DEFAULT);
		}
	}

}
