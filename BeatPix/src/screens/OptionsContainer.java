package screens;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gui.GUIApplication;
import gui.components.Action;
import gui.components.Button;
import gui.components.Component;
import gui.components.Graphic;
import mainGame.MainGUI;
import mainGame.screens.GameScreen;
import screens.components.CustomText;
import screens.components.ImageButton;
import screens.components.ScalablePixelBack;
import screens.interfaces.Options;

public class OptionsContainer{

	Options parentScreen; // Screen for popup 
	int x; int y; // Width and height of screen
	/**IMPORTANT FOR DIMENSIONS:**
	 * 
	 * x1 and y1 are determined through trial and error on the basic
	 * 960 x 540 dimensions. Multiplying by the new screen's x by the
	 * original ratio would result in proper scaling.
	 * 
	 * WIDTH: x * x1 / 960
	 * HEIGHT: y * y1 / 540
	 */
	private Component blackBack; // Black out previous screen
	private ScalablePixelBack background; // Pop-up background
	private ArrayList<CustomText> labelText; // Basic texts
	
	private ImageButton back; // Back button
	private CustomText backText; // Text for back button
	
	private ArrayList<Graphic> keySelect; // Letters of key select
	private ArrayList<Button> hiddenKeyButtons; // Hidden buttons behind keySelect
	private ArrayList<ScalablePixelBack> keyBackground; //Background for hidden buttons
	
	
	private ImageButton toggleVolume; // Volume toggle button
	
	private ScalablePixelBack selectingKeyScreen; // Pop-up when selecting key
	private ArrayList<CustomText> selectingKeyScreenText; // Text for pop-up
	
	private int selectingKeyPhase; // User not selecting key or selecting (-1,0)
	private int columnButtonSelected; // keySelect user has chosen

	/*public static void main(String[] args) {
		String s = "Dotcor of doom";
		System.out.println(longestWordLength(s));
		System.out.println(indexOfLongestSentence("DOctor",4));
	}*/
	
	/**Constructor**
	 * 
	 * selectingKeyPhase set to default state (-1)
	 * 
	 * All components are created for use later
	 * 
	 * @param x
	 * @param y
	 * @param currentScreen
	 */
	public OptionsContainer(int x , int y, Options currentScreen) {
		this.parentScreen = currentScreen;
		this.x = x;
		this.y = y;
		
		selectingKeyPhase = -1;
		createComponents();
	}
	
	/**Adds and Removes Components to Screen**
	 * 
	 * The method that is called whenever a button or
	 * key is clicked to access the options
	 * 
	 * Works by adding all the components to the currentScreen
	 * of the GUIApp by utilizing the FullFunctionScreen's
	 * addObject() and remove()
	 */
	public void addObjects() {
		parentScreen.addObject(blackBack);
		parentScreen.addObject(background);
		for(CustomText c: labelText) {
			parentScreen.addObject(c);
		}
		
		parentScreen.addObject(back);
		parentScreen.addObject(backText);
		
		for(int i = 0; i<4; i++) {
			parentScreen.addObject(hiddenKeyButtons.get(i));
			parentScreen.addObject(keyBackground.get(i));
			parentScreen.addObject(keySelect.get(i));
		}
		
		parentScreen.addObject(toggleVolume);
		
		toggleButtons(true);
	}
	public void removeObjects() {
		if(parentScreen instanceof GameScreen) {
			((GameScreen)parentScreen).setInOptions(false);
		}
		parentScreen.remove(blackBack);
		parentScreen.remove(background);
		for(CustomText c: labelText) {
			parentScreen.remove(c);
		}
		
		parentScreen.remove(back);
		parentScreen.remove(backText);
		
		for(int i = 0; i<4; i++) {
			parentScreen.remove(hiddenKeyButtons.get(i));
			parentScreen.remove(keyBackground.get(i));
			parentScreen.remove(keySelect.get(i));
		}
		
		parentScreen.remove(toggleVolume);
		
		//Reenables clickables on parentScreen and returns cursor to arrow
		parentScreen.toggleButtons(true);
		GUIApplication.mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	/**Short Helper Method for Organization
	 *
	 * Creates black out(tinted) background as well as
	 * the pixel background for the options
	 */
	public void createComponents() {
		blackBack = new Component(0, 0, x, y) {
			public void update(Graphics2D g) {
				g.setColor(Color.BLACK);
				g.fillRect(0,0, x, y);
			}
		};
		blackBack.update();
		blackBack.setAlpha(0.7f);
		
		background = new ScalablePixelBack(x/10,
												y/10,
													x*8/10,
														y*8/10,
															1.5);
		createLabelText();
		
		createBackButton();
		
		createKeySelects();
		
		updateVolumeToggle();
	}
	
	/**--LABEL TEXTS
	 * 
	 * Static texts that are part of the background
	 * 
	 */
	public void createLabelText() {
		labelText = new ArrayList<CustomText>();
		
		String s1 = "KEY SELECT";
		CustomText temp = new CustomText(x/960*	150, 
											y/540*	100, 
												x/960*	400, 
													y/540*	580,
														"KEY SELECT",
															true);
		labelText.add(temp);
		
		String s2 = "VOLUME CONTROL";
		CustomText temp2 = new CustomText(x/960*	150, 
												y/540*	280, 
													x/960*	500, 
														y/540*	700,
															"VOLUME CONTROL", 
																true);
		labelText.add(temp2);
	}
	
	 /**--BACK BUTTON--
	  * 
	  * Same design as mainMenu's buttons
	  * 
	  * Will call removeObjects() in order to remove all of
	  * Option's components
	  */
	public void createBackButton() {
		back = new ImageButton(x*630/960,
									y*420/540, 
										x*200/960, 
											y*50/540,
												"resources\\ui\\buttons\\buttongeneral.png");
		
		backText = new CustomText(x*670/960, 
									y*430/540, 
										x*100/960, 
											y*80/540,
												"Back",
													true, true, false);
		back.setAction(new Action() {
			public void act() {
				if(GameScreen.game != null) {
					GameScreen.game.getPlayer().updateVolume();
				}
				removeObjects();
			}
		});
		back.setEnabled(true);
	}
	
	/**--KEY SELECT BUTTONS--
	 * **Since I'm Lazy**
	 * Three Parts:
	 * -ScalablePixelBack - for aesthetics, looks like a button
	 * -ImageButton - only used to show letters
	 * -Button - the real button hidden behind ScalablePixelBack
	 *  
	 * Letters aren't aligned with ScalablePixelBack
	 */
	public void createKeySelects() {
		
		keyBackground = new ArrayList<ScalablePixelBack>();
		keySelect = new ArrayList<Graphic>();
		hiddenKeyButtons = new ArrayList<Button>();
		
		for(int i = 0; i < 4; i ++) {
			keyBackground.add(new ScalablePixelBack(x*i*130/960 + x*180/960,
														y*170/540,
															x*100/960,
																x*100/960, 
																	1.3));
			
			hiddenKeyButtons.add(new Button(x*i*130/960 + x*180/960,
												y*170/540,
													x*100/960,
														x*100/960, 
															"", null));
			
/**/		keySelect.add(new Graphic(x*i*130/960 + x*205/960,
												y*190/540,
													x*80/960,
														x*40/960, 
															"resources\\text\\" + MainGUI.getKeys(i) + ".png"));
		}
		setKeySelectActions();
	}
	
	/**Replaces Old Key with New**
	 * 
	 * Removes old letter image from parentScreen,
	 * recreates it and adds it back
	 */
	public void recreateKey(int x1) {
		parentScreen.remove(keySelect.get(x1));
		keySelect.set(x1, new ImageButton(x*x1*130/960 + x*205/960,
												y*190/540, 
													x*80/960, 
														x*40/960, 
															"resources\\text\\" + MainGUI.getKeys(x1) + ".png"));
		parentScreen.addObject(keySelect.get(x1));
	}

	/**Set keySelect Actions Manually**
	 * 
	 * Sets the keySelect actions one by one because within
	 * Action() there can't be any outside influence (such as i)
	 * that will be saved onto the button itself
	 * 
	 * selectingKeyPhase is set to 0 (selecting a key) and
	 * the specific columnButton selected has to be tracked
	 * 
	 * Creates pop up asking for a key and the
	 * other Option's buttons have to be disabled
	 */
	public void setKeySelectActions() {
		hiddenKeyButtons.get(0).setAction(new Action() {
			public void act() {
				selectingKeyPhase = 0; 
				columnButtonSelected = 0;
				
				createSelectingKeyPopUp("Awaiting key input...");
				toggleButtons(false);
			}
		});
		hiddenKeyButtons.get(1).setAction(new Action() {
			public void act() {
				selectingKeyPhase = 0; 
				columnButtonSelected = 1;
				
				createSelectingKeyPopUp("Awaiting key input...");
				toggleButtons(false);
			}
		});
		hiddenKeyButtons.get(2).setAction(new Action() {
			public void act() {
				selectingKeyPhase = 0; 
				columnButtonSelected = 2;
				
				createSelectingKeyPopUp("Awaiting key input...");
				toggleButtons(false);
			}
		});
		hiddenKeyButtons.get(3).setAction(new Action() {
			public void act() {
				selectingKeyPhase = 0; 
				columnButtonSelected = 3;
				
				createSelectingKeyPopUp("Awaiting key input...");
				toggleButtons(false);
			}
		});

		
	}
	
	/**
	 * This method looks at the string an detects if there are any special characters in the string
	 * @param s - The string to check
	 * @return - Whether or not there are any special characters in the string
	 * 
	 * @author Justin Yau
	 */
	public boolean anySpecialCharacters(String s) {
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(s);
		return m.find();
	}
	
	/**Method Called by Other Screens**
	 * 
	 * Method called by other screens whenever
	 * kepPress/keyReleased is called in order
	 * to determine which key the user chose
	 * 
	 * Example:
	   	public void keyPressed(KeyEvent e) {
			passKeyCodeIntoOptions(e);
		}
	 * 
	 * Checks if key pressed is not in use:
	 * -If in unavailable, it will stay phase 0
	 *  where it will wait for another key press
	 * -If available, it will auto set it and
	 *  go into the non selecting phase
	 */
	public void readKey(KeyEvent e) {
		int tempX = e.getKeyCode();
		
		if(selectingKeyPhase == 0) {
			boolean validKey = true;
			for(String s: MainGUI.getKeys()) {
				if((char) tempX == s.charAt(0)) {
					validKey = false; break;
				}
			}
			
		if(tempX > 64 && tempX < 91) {
		}else {
			validKey = false;
		}
		
			if(validKey) {
				String tempS = ""+ (char) tempX;
				String oldKey = MainGUI.getKeys(columnButtonSelected);
				MainGUI.setKeys(columnButtonSelected, tempS);
				if(GameScreen.game != null) {
					GameScreen.game.rebindKey(tempS, oldKey);
				}
				
				selectingKeyPhase = -1;
				
				createSelectingKeyPopUp("Key set to " + (char) tempX);
				
				// will remove the popup after specified time and re-enable options
				Timer t = new Timer();
				t.schedule(new TimerTask() {
					public void run() {
						removeSelectingKeyPopUp();
						toggleButtons(true);

						recreateKey(columnButtonSelected); // needs to recreate that specific letter
					}
				}, 1000);
				
			}else {
				selectingKeyPhase = 0;
				
				createSelectingKeyPopUp("Reselect key");
			}
		}
	}
	
	/** --KEY SELECTION SCREEN--
	 * 
	 * Two Parts:
	 * -ScalablePixelBack - same as Option's overall background
	 * -ArrayList<CustomText> - CustomText with multiple lines
	 *                          in order to scale by height
	 * 
	 * Pop-up that appears when user is choosing a new key to
	 * bind to a column then displays if user successfully 
	 * binded or they have to reselect a valid key
	 * 
	 * The ArrayList is built by determining the string width
	 * and dividing it into rows in order to fit a specific
	 * width and height
	 * 
	 * Also adds it onto the parentScreen
	 */
	public void createSelectingKeyPopUp(String s) {
		
		removeSelectingKeyPopUp();

		int rowLength = 10;
		if(rowLength <longestWordLength(s))
			rowLength = longestWordLength(s);
		
		selectingKeyScreenText = new ArrayList<CustomText>();
		ArrayList<String> temp = arrayOfBrokenUpStrings(s, rowLength);
		for(int i = 0; i< temp.size(); i++) {
			CustomText ct = new CustomText(x/960*	340,
														50*i  + y/540*	120, 
															x/960*	280, 
																y/540*	300, 
																	temp.get(i), true, true, false);
			selectingKeyScreenText.add(ct);
		}
		
		selectingKeyScreen = new ScalablePixelBack(x/960*	330,
														y/540*	100, 
															x/960*	28*rowLength, 
																y/540*	60*selectingKeyScreenText.size(), 
																	1);
		
		parentScreen.addObject(selectingKeyScreen);
		for(CustomText c: selectingKeyScreenText) {
			parentScreen.addObject(c);
		}
	}
	public void removeSelectingKeyPopUp() {
		parentScreen.remove(selectingKeyScreen);
		if(selectingKeyScreenText != null)
			for(CustomText c: selectingKeyScreenText) {
				parentScreen.remove(c);
			}
	}
//--HELPER METHODS FOR STRING MANIPULATION TO TRANSLATE TO CUSTOMTEXT--	
	public static int longestWordLength(String s) {
		int longest = 0;
		String[] s1= s.split(" ");
		for(String x: s1) {
			if(x.length() > longest) {
				longest = x.length();
			}
		}
		return longest;
	}
	/**Returns idx of the last word where there are full words
	 * within a certain boundary:
	 * 
	 * indexOfLongestSentence("Doctor of Doom", 10)
	 * should return 8, -> "Doctor of" is the longest
	 * full word combo within 10 letters
	 * 
	 * @param s
	 * @param boundary
	 * @return
	 */
	public static int indexOfLongestSentence(String s, int boundary) {
		int idx = boundary;
		if(s.length() <= boundary) {
			return s.length()-1;
		}
		for(int i = boundary; i > 0; i--) {
			if(s.substring(i, i+1).equals(" ") || i == 0) {
				idx = i;
				break;
			}else {
				idx --;
			}
		}
		return idx;
	}
	/**Breaks up the string to create strings of a certain length
	 * 
	 * @param s
	 * @param boundary
	 * @return
	 */
	public static ArrayList<String> arrayOfBrokenUpStrings(String s, int boundary) {
		String temps = s;
		ArrayList<String> arrayTemp = new ArrayList<String>();
		while(temps.length() > 1) {
			if(indexOfLongestSentence(temps, boundary) == 0) {
				System.out.println(temps); break;
			}
			
			int tempx = indexOfLongestSentence(temps, boundary);
			String tempSub = temps.substring(0,tempx+1);

			if(tempSub.substring(0, 1).equals(" "))
				tempSub = tempSub.substring(1,tempSub.length());

			while(tempSub.length() < boundary) {
				tempSub += " ";
			}
				
			arrayTemp.add(tempSub);
			temps = temps.substring(tempx,temps.length());
		}
		return arrayTemp;
	}
	
	/** --VOLUME TOGGLE--
	 *
	 * User clicks button to toggle between 4 stages
	 * (Mute, V1, V2, Max)
	 * 
	 * Each click removes the previous toggleVolume, changes the
	 * volume in MainGUI, then recreates toggleVolume based off the
	 * new volume
	 */
	public void updateVolumeToggle() {
		
		toggleVolume = new ImageButton(x*200/960, 
											y*350/540, 
												x*75/960, 
													y*75/540, 
														"resources\\ui\\volume\\v" + MainGUI.getVolume() + ".png");
		toggleVolume.setEnabled(true);
		
		toggleVolume.setAction(new Action() {
			public void act() {
				if(MainGUI.getVolume() - 1 == -1) {
					MainGUI.setVolume(3);
				}else {
					MainGUI.setVolume(MainGUI.getVolume()-1);
				}

				parentScreen.remove(toggleVolume);
				updateVolumeToggle(); // When clicked it recreates toggleVolume
				parentScreen.addObject(toggleVolume);

				MainGUI.start.getMs().getMenuSound().updateVolume();
			}
		});
	}
	
	/**Changes Enabled of Option's Clickables
	 * 
	 * Sets the enabled for all of OptionContainer's
	 * clickable components to b
	 * 
	 * @param b
	 */
	public void toggleButtons(boolean b) {
		
		back.setEnabled(b);
		
		toggleVolume.setEnabled(b);
		
		for(int i = 0; i < 4; i++) {
			hiddenKeyButtons.get(i).setEnabled(b);
		}
	}
	
	/**Set ParentScreen Whenever SetScreen is caleld**
	 * 
	 */
	public void setParentScreen(Options screen) {
		this.parentScreen = screen;
	}
}
