package screens;

import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

import gui.GUIApplication;
import gui.components.*;
import gui.interfaces.Visible;
import gui.userInterfaces.FullFunctionScreen;

import highscore.TempSongSelect;
import mainGame.MainGUI;
import mainGame.components.Song;
import screens.components.BottleClick;
import screens.components.CustomText;
import screens.components.ImageButton;
import screens.interfaces.Options;

public class MainMenuScreenG extends FullFunctionScreen implements Options{
//
	/**Design:
	 *
	 * 	-Background - based off where StartScreen left its background
	 * 				- scroll down similarly to in StartScreen
	 * 	-Idle Character - will be at the left side of screen
	 * 					- will slide in along with the background
	 * 	-Buttons - will be on the right side of the screen
	 * 			 - takes up most of the screen space
	 * 			 - will slide in along with the background
	 * 			 - will slide in from the right
	 * 
	 *  IMPORTANT FOR DIMENSIONS:
	 *  
	 * x1 and y1 are determined through trial and error on the basic
	 * 960 x 540 dimensions. Multiplying by the new screen's x by the
	 * original ratio would result in proper scaling.
	 * 
	 * WIDTH: x * x1 / 960
	 * HEIGHT: y * y1 / 540
	 */
	
	private static final long serialVersionUID = -7197187517418245951L;
	
	private Timer time; // timer for events
	private int screenPhase; // phases for events
	
	private ImageButton background; // background image

	private AnimatedComponent idleCharacter; // animated character
	
	private static int BUTTON_COUNT = 4;
	private ArrayList<ImageButton> buttons; // main 4 buttons
	private ArrayList<CustomText> buttonTexts; // text for 4 buttons
	private static String[] buttonT = {"Level Select","Character","Unlocks","Options","Custom Song"};
	private static int LEVEL_IDX = 0;
	private static int CHARACTER_IDX = 1;
	private static int UNLOCK_IDX = 2;
	private static int OPTIONS_IDX = 3;
	
	private static ImageButton information;
	
	private static BottleClick bottle;
	
	/**Constructor**
	 * 
	 * screenPhase set to default
	 * 
	 * @param width
	 * @param height
	 */
	public MainMenuScreenG(int width, int height) {
		super(width, height);
		screenPhase = 0; 
		createBottle();
	}
	
	/**Adds Components to Screen**
	 * 
	 */
	public void initAllObjects(List<Visible> viewObjects) {
		createBackground();
		createButtons();
		setButtonsActions();
		
		createIdleCharacter();
		createInformationButton();
		
		//viewObjects adding
		viewObjects.add(background);
		for(int i = 0; i < buttons.size(); i++) {
			viewObjects.add(buttons.get(i));
			viewObjects.add(buttonTexts.get(i));
		}
		viewObjects.add(idleCharacter);
		viewObjects.add(information);
	}
	
	public void createBottle() {
		bottle = new BottleClick(this);	
		bottle.getBottleOnSill().setY(120 + getHeight());
	}
	
//--COMPONENTS--//
	/** --BACKGROUND--
	 * Creates a background that scales to the screen's width
	 * 
	 * The y of the background is set at 2 times the screen's pixel above
	 * the bottom of the background image's height (where StartScreen leaves it off)
	 * 
	 * Since MouseListener will break other buttons, background is created as an
	 * ImageButton and goes to the next phase when clicked (unlike StartScreen which
	 * uses a MouseListener)
	 */
	public void createBackground() {
		ImageIcon icon = new ImageIcon("resources\\backgrounds\\start.jpg");
		background = new ImageButton(0, 
										0, 
											getWidth(), 
												(int) ((getWidth()/icon.getIconWidth())*icon.getIconHeight()+100), 
													"resources\\backgrounds\\start.jpg");
		
		background.setY(-background.getHeight()+getHeight()*2);
		
		background.setEnabled(true);
		background.setHoverAction(null); background.setUnhoverAction(null);
		background.setAction(new Action() {
			public void act() {
				if(screenPhase == 0) {
					scrollDownEnd();
				}
			}
		});
	}
	
	
	/**--IDLE CHARACTER--
	 * Creates an animation of the idle character with SPECIFIC dimensions
	 * that are not scalable
	 * 
	 * The y is set 200 pixels plus the size of the screen in order for it
	 * to be off-screen and move up along with the background
	 * 
	 * _________________
	 * |                |
	 * |                |<-- Visible Screen	
	 * |                |
	 * |________________|
	 * |                |
	 * |                |				
	 * |  X             |<-- Position of idle character
	 * |________________|
	 * 
	 * The number before "+ getHeight()" for the y direction
	 * will eventually be the last position of the component
	 * on the screen when it stops scrolling
	 * 
	 * The x dimension is whatever since the position won't
	 * be modified in any of the events
	 * */
	public void createIdleCharacter() {
		idleCharacter = new AnimatedComponent(
				getWidth()/10, 
					getHeight()*200/540 + getHeight(), 
						getWidth()*400/960, 
							getHeight()*300/540);
		
		idleCharacter.addSequence("resources/sprites/defaultSprite_Transparent.png", 500, 0, 0, 39, 33, 2);
		
		Thread run = new Thread(idleCharacter);
		run.start();
	}
	public void getNewCharacter() {
		//Placeholder method so the idleCharacter changes to the character selected by user, info will be pulled from MainGUI
		//will change the idleCharacter.addSequence part
	}
	
	/** --BUTTONS--
	 * Creates a series of buttons on the screen that is not scaled
	 * using the original image dimensions
	 * 
	 * For the y parameter each button is 100 pixel below the previous button
	 * 
	 * They share all the same x parameter, but is more complicated when
	 * trying to animate it sliding in from the right
	 */
	public void createButtons() {
		buttons = new ArrayList<ImageButton>();
		buttonTexts = new ArrayList<CustomText>();
		for(int i=0; i<BUTTON_COUNT; i++) {
			int buttonX = getHeight()*160/540 + getWidth();
			int buttonY = getHeight()*100/540*(i+1) + getHeight();
			int buttonW = getWidth()*399/960;
			int buttonH = getHeight()*100/540;
			
			buttons.add(new ImageButton(buttonX,buttonY,buttonW,buttonH,"resources\\ui\\buttons\\buttonwithrivet.png"));
			
			buttons.get(i).setIdxArray(i);
			buttonTexts.add(new CustomText(buttonX + getWidth()*70/960,
												buttonY + getHeight()*25/540,
													(buttonW - buttonW*100/399),
														buttonH - buttonH*70/100,
															buttonT[i] ,false , true, false));
		}
		
		setButtonsActions();
		setButtonsHoverAction();
	}
	public void setButtonsActions() {
				buttons.get(LEVEL_IDX).setAction(new Action() {
					public void act(){
						//System.out.println("Select Level Screen Clicked");
						buttons.get(LEVEL_IDX).unhoverAction();

						//MainGUI.setS creen(MainGUI.level);
						//Song song = new Song("resources/maps/Elektronomia Summer Vibes/Elektronomia Summer Vibes.csv");
						/**/			//MainGUI.test.setScreen(new GameScreen(getWidth(), getHeight(), song, "resources/sample_bg.gif"));
										
						MainGUI.test.setScreen(MainGUI.select);
										
										/*try {
											MainGUI.test.setScreen(new TempSongSelect(getWidth(), getHeight()));
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}*/
						MainGUI.start.getMs().getMenuSound().pause();
					}
				});
				buttons.get(CHARACTER_IDX).setAction(new Action() {
					public void act(){
						//System.out.println("Select Character Screen Clicked");
						buttons.get(CHARACTER_IDX).unhoverAction();
						MainGUI.test.setScreen(MainGUI.character);
					}
				});
				buttons.get(UNLOCK_IDX).setAction(new Action() {
					public void act(){
						//System.out.println("Select Unlocks Screen Clicked");
						buttons.get(UNLOCK_IDX).unhoverAction();
						MainGUI.test.setScreen(MainGUI.test.shop);
					}
				});
				
				buttons.get(OPTIONS_IDX).setAction(new Action() {
					public void act(){
						//System.out.println("Select Options Screen Clicked");
						buttons.get(OPTIONS_IDX).unhoverAction();
						
						MainGUI.options.addObjects();
						toggleButtons(false);
					}
				});
				
				/*buttons.get(CUSTOM_SONG).setAction(new Action() {
					public void act() {
						buttons.get(CUSTOM_SONG).unhoverAction();
						toggleButtons(false);
						
						MainGUI.information.addObjects();
					}
				});*/
	}
	
	//
	int x;
	public void setButtonsHoverAction() {
		//Just for aesthetics so when a user hovers over a button the button will change to a depressed version
		x = 0;
		for(int i = 0; i < BUTTON_COUNT; i++) {
			buttons.get(i).setHoverAction(new Action() {
				
				ImageButton b = buttons.get(x);
				
				public void act() {
					GUIApplication.mainFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));
					buttons.get(b.getIdxArray()).setAlpha(0.3f);
				}
			});
			buttons.get(i).setUnhoverAction(new Action() {
				
				ImageButton b = buttons.get(x);
				
				public void act() {
					GUIApplication.mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					buttons.get(b.getIdxArray()).setAlpha(1f);
					//System.out.println("unhover");
				}
			});
			x++;
		}
	}

	/** --INFORMATION--
	 * 
	 * Button that leads to the information popup
	 */
	public void createInformationButton() {
		information = new ImageButton(50,
										50, 
											50,
												50,
													"resources\\WhiteFont\\I_White_Transparent.png");
		information.setAction(new Action() {
			public void act() {
				toggleButtons(false);
				MainGUI.information.addObjects();
			}
		});

		information.setAlpha(0.0f);
	}
	
//--EVENTS--//
	/**SCROLL DOWN METHODS COMMENTED HERE**
	 * 
	 * Returns true if the final status is incomplete
	 * Returns false when its done
	 * 
	 * moveBackground()
	 * 	Will move background until it is shown as the bottom
	 * 	of the background image matches with the bottom of the screen
	 * 
	 * moveIdleCharacter()
	 * 	Moves the idleCharacter along with the background until it
	 * 	reaches the designated final y coordinate(which is set in the
	 * 	y parameter of when idleCharacter is made as an AnimatedComponent)
	 * 
	 * moveButtonsY()
	 * 	Similar to that of moveidleCharacter, but stops when the first button
	 * 	reaches its final position, which should move the rest at the same rate
	 * 
	 * moveButtonsX()
	 * 	The more complicated movement as it doesn't move relative to any other
	 * 	component on the screen(unlike the components moving along with background
	 * 	through the y position)
	 * 	
	 */
	public void scrollDown() {
		time = new Timer();
		time.scheduleAtFixedRate(new TimerTask() {
			public void run() {
					if(!isBackgroundFinal()
							|| !isIdleFinal()
								|| !isButtonXFinal()
									|| !isButtonYFinal()) {
						moveBackground();
						moveIdleCharacter();
						moveButtonsX(); 
						moveButtonsY();
					}else {
						scrollDownEnd();
					}
			}
		}, 0, 2);
	}
	
	//--SCROLL DOWN METHODS--//
	public void moveBackground() {
		if(!isBackgroundFinal()) {
			background.setY(background.getY() - 1);
		}
	}
	public boolean isBackgroundFinal() {
		return !(background.getY() > -background.getHeight()+getHeight());
	}
	
	public void moveIdleCharacter() {
		if(!isIdleFinal()) {
			idleCharacter.setY(idleCharacter.getY() - 1);
			if(bottle.getBottleOnSill()!=null)
				bottle.getBottleOnSill().setY(bottle.getBottleOnSill().getY() - 1);
		}
	}
	public boolean isIdleFinal() {
		return !(idleCharacter.getY() > getHeight()*200/540); // DIMENSIONS!!!!!!!!
	}
	
	public void moveButtonsX() {
		if(!isButtonXFinal()) {
			for(int i = 0; i < buttons.size(); i++) {
				buttons.get(i).setX(buttons.get(i).getX()-1);
				buttonTexts.get(i).setX(buttonTexts.get(i).getX()-1);
			}
		}
	}
	public boolean isButtonXFinal() {
		return !(buttons.get(0).getX() > getWidth()*480/960); // DIMENSIONS!!!!!!!!
	}
	
	public void moveButtonsY() {
		if(!isButtonYFinal()) {
			for(int i = 0; i < buttons.size(); i++) {
				buttons.get(i).setY(buttons.get(i).getY()-1);
				buttonTexts.get(i).setY(buttonTexts.get(i).getY()-1);
			}
		}
	}
	public boolean isButtonYFinal() {
		return !(buttons.get(0).getY() > getHeight()*100/540); // DIMENSIONS!!!!!!!!
	}
	
	public void scrollDownEnd() {
		time.cancel();
		
		background.setY(-background.getHeight()+getHeight());
		background.setEnabled(false); 
		
		information.setAlpha(1.0f);
		
		idleCharacter.setY(getHeight()*200/540); // DIMENSIONS!!!!!!!!
		this.bottle.getBottleOnSill().setY(getHeight()*120/540);
		
		for(int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setY(getHeight()*100/540*(i+1)); // DIMENSIONS!!!!!!!!
			buttons.get(i).setX(getWidth()*480/960); // DIMENSIONS!!!!!!!!
			buttonTexts.get(i).setY(getHeight()*100/540*(i+1) + getHeight()*25/540); // DIMENSIONS!!!!!!!!
			buttonTexts.get(i).setX(getWidth()*480/960 + getWidth()*70/960); // DIMENSIONS!!!!!!!!
		}
		
		toggleButtons(true);
		
		screenPhase = 1;
		
		
	}
	
	public void changeIdle() {
		this.remove(idleCharacter);
		idleCharacter = new AnimatedComponent(
				getWidth()/10, 
					getHeight()*200/540, 
						getWidth()*400/960, 
							getHeight()*300/540);
		String sprite = "resources//sprites//" + MainGUI.test.character.getSkin() + "Sprite_Transparent.png";
		//String sprite = "resources//sprites//" + "green" + "Sprite_Transparent.png";
		idleCharacter.addSequence(sprite, 500, 0, 0, 39, 33, 2);
		
		Thread run = new Thread(idleCharacter);
		run.start();
		this.addObject(idleCharacter);
	}

//--OPTIONS INTERFACE METHODS--//
	public void toggleButtons(boolean b) {
		if(bottle != null) {
			bottle.getBottleOnSill().setEnabled(b);
		}
		information.setEnabled(b);
		for(ImageButton button: buttons) {
			button.setEnabled(b);
		}
	}
	public void passKeyCodeIntoOptions(KeyEvent e) {
		MainGUI.options.readKey(e);
	}
	public void keyPressed(KeyEvent e) {
		passKeyCodeIntoOptions(e);
	}
	/**
	 * This method is optional only if the user is using
	 * keyPressed for anything else, if not, something
	 * simple such as above will suffice
	 */
	public boolean inOptions() {
		return false;
	}
}
