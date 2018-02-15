package screens;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

import javax.swing.ImageIcon;

import gui.GUIApplication;
import gui.components.Graphic;
import gui.interfaces.*;
import gui.userInterfaces.*;
import mainGame.MainGUI;
import screens.events.ShipCrash;

public class StartScreenG extends FullFunctionScreen implements MouseListener{

	private static final long serialVersionUID = -6794226819818369625L;
	/**Design:
	 * -Background - will be a basic static image
	 * -Title - will be an image component that fades in
	 * -StartButton - basic button to start the game
	 * 
	 * --Clicking start will scroll down the image to 
	 *   get to Main Menu background image
	 */
	
	private Timer time; // timer for events
	private int screenPhase; // phases for events
	
	public static Graphic background; // background image
	
	private Graphic title; // title graphic
	private Graphic start; // start graphic
	
	//Something old for when user could only click when start fades in
	//basically useless now
	private boolean allowClick = false;
	
	/**Constructor**
	 * 
	 * @param width
	 * @param height
	 */
	public StartScreenG(int width, int height) {
		super(width, height);
		screenPhase = -1;
	}
	
	/**User Clicks to Skip**
	 * 
	 * Each user click will move the user
	 * to the next screen phase
	 * 
	 */
	public void mouseClicked(MouseEvent e) {
		if(!allowClick) {
			if(screenPhase == 0) {
				scrollInEnd();
			}else if(screenPhase == 1) {
				fadeInsEnd();
			}else if(screenPhase == 2) {
				fadeOutsEnd();
			}else if(screenPhase == 3) {
				scrollOutEnd();
			}
		}
		if(allowClick) {
			allowClick = false;
			GUIApplication.mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			fadeInsEnd();
			fadeOuts();
		}
	}
	
	/**Adds Components to Screen**
	 * 
	 */
	public void initAllObjects(List<Visible> viewObjects) {
		
		background = updateBackground("resources\\backgrounds\\start.jpg");
		title = updateTitle("resources\\title.png");
		start = updateStart("resources\\ui\\buttons\\startbutton.png");
		
		title.setAlpha(0.0f);
		start.setAlpha(0.0f);
		
		//scrollIn();
		
		viewObjects.add(background);
		viewObjects.add(title);
		viewObjects.add(start);
	}
	
//--START ( [START] )--//
	private Graphic updateStart(String path) {
		ImageIcon icon = new ImageIcon(path);
		int w; int h; int x; int y;
		w = getWidth()/5;
		h = getHeight()/5;
		x = (getWidth()/2) - w/2;
		y = (getHeight()/2) - h/2 + getHeight()*100/540;
		
		return new Graphic(x,y,w,h,path);
	}
	
//--TITLE (BEATBOT)--//
	private Graphic updateTitle(String path) {
		ImageIcon icon = new ImageIcon(path);
		int w; int h; int x; int y;
		w = getWidth()/4;
		h = getHeight()/4;
		x = (getWidth()/2) - w/2;
		y = (getHeight()/2) - h/2;
		return new Graphic(x,y,w,h,path);
	}
	
//--BACKGROUND ([])--//
	private Graphic updateBackground(String path) {
		ImageIcon icon = new ImageIcon(path);
		int w; int h; // 0 for either will use original image size/width
		int x = 0; int y = 0;
		if(background != null) {
			x = background.getX(); y = background.getY();
		}
		
		w = getWidth();
		h = (int) ((getWidth()/icon.getIconWidth())*icon.getIconHeight()+100); //makes the width of background always match the screen
		
		return new Graphic(x,y,w,h,path);
	}
	
//--EVENTS--//
	/**Scrolls Background SCREENPHASE 0**
	 * 
	 * Initial State:
	 * -Top of background matches top of screen (0,0)
	 * 
	 * Final State:
	 * -Background half matches bottom of screen
	 */
	
	private ShipCrash ms;
	public void start() {
		setMs(new ShipCrash(this)); screenPhase = -1;
		//scrollIn(); screenPhase = 0;
	}
	
	public void scrollIn() {
		screenPhase = 0;
		background.setX(0); background.setY(0);
		
		time = new Timer();
		time.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if(background.getY() > -background.getHeight()/2+getHeight()) {
					background.setY(background.getY() - 1);
				}else {
					scrollInEnd();
				}
			}
		}, 0, 8); //set FPS
	}
	public void scrollInEnd() {
		time.cancel();
		fadeIns();
		background.setY(-background.getHeight()/2+getHeight());
		screenPhase = 1;
	}
	
	/**Fade In Title and Start SCREENPHASE 1**
	 * 
	 * Initial State:
	 * -SCREENPHASE 0 final state
	 * -Title and start are invisible (0.0f alpha)
	 * 
	 * Final State:
	 * -Title and start are solid (1.0f alpha)
	 * 
	 * Start starts to fade in once Title is 
	 * 70% solid (0.7f alpha)
	 */
	public void fadeIns() {
		
		background.setY(-background.getHeight()/2+getHeight());
		title.setAlpha(0.0f);start.setAlpha(0.0f);
		
		time = new Timer();
		time.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if(title.getAlpha() > 0.7f) {
					if(start.getAlpha() < 0.99f) { //begins the fade in for startbutton

						allowClick = true; GUIApplication.mainFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));
						start.setAlpha(start.getAlpha() + 0.01f);
					}
				}
				if(title.getAlpha() <= 0.99f) {
					title.setAlpha(title.getAlpha() + 0.01f);
				}
				if(title.getAlpha() >= 0.99f && start.getAlpha() >= 0.99f) {
					fadeInsEnd();
				}
			}
		}, 0, 6); //set FPS
	}
	public void fadeInsEnd() {
		time.cancel();
		title.setAlpha(1f);start.setAlpha(1f);
		allowClick = true;
		screenPhase = 2;
	}

	/**Fades Out Title and Start SCREENPHASE 2**
	 * 
	 * Initial State:
	 * -SCREENPHASE 1 final state
	 * -Title and start are solid (1.0f alpha)
	 * 
	 * Final State:
	 * -Title and start are invisible (0,0f alpha)
	 * 
	 */
	public void fadeOuts() {
		
		title.setAlpha(1f);start.setAlpha(1f);
		allowClick = false;

		time = new Timer();
		time.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if(title.getAlpha() <= 0.01f && start.getAlpha() <= 0.01f) {
					fadeOutsEnd();
				}else {
					start.setAlpha(start.getAlpha() - 0.01f);
					title.setAlpha(title.getAlpha() - 0.01f);
				}
			}
		}, 0, 7); //set FPS
	}
	public void fadeOutsEnd() {
		time.cancel();
		scrollOut();
		start.setAlpha(0.0f);title.setAlpha(0.0f);
		screenPhase = 3;
	}
	
	/**Scrolls Down SCREENPHASE 3**
	 * 
	 * Initial State:
	 * -SCREENPHASE 2 final state
	 * -Title and start are invisible (0,0f alpha)
	 * 
	 * Final State:
	 * -Background is set so the screen is two
	 *  of the screen height up (bottom of background image
	 *  + 2 of the screen height)
	 *  
	 *  Once it reaches its final state the screen switches
	 *  to the main menu screen
	 * 
	 */
	public void scrollOut() {
		
		title.setAlpha(0f);start.setAlpha(0f);
		
		time = new Timer();
		time.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if(background.getY() > -background.getHeight() + getHeight()*2) {
					background.setY(background.getY() - 1);
				}else {
					scrollOutEnd();
				}
			}
		}, 0, 4); //100fps
	}
	public void scrollOutEnd() {
		screenPhase = 4;
		time.cancel();
		background.setY(-background.getHeight() + getHeight());
		background.setY(-background.getHeight() + getHeight()*2);

						
/*Screen switch*/		MainGUI.test.setScreen(MainGUI.test.mainMenu);
						MainGUI.mainMenu.scrollDown();
	}

	public ShipCrash getMs() {
		return ms;
	}

	public void setMs(ShipCrash ms) {
		this.ms = ms;
	}
}
