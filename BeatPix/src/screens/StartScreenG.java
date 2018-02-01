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

public class StartScreenG extends FullFunctionScreen implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6794226819818369625L;
	/**Design:
	 * -Background - will be a basic static image
	 * -Title - will be an image component that fades in
	 * -StartButton - basic button to start the game
	 * 
	 * --Clicking start will scroll down the image to 
	 *   get to Main Menu background image
	 */
	
	private Timer time;
	private int screenPhase;
	
	private Graphic background;
	
	private Graphic title;
	private Graphic start;
	
	private boolean allowClick = false;

	public StartScreenG(int width, int height) {
		super(width, height);
		screenPhase = 0;
	}

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
			allowClick = false; GUIApplication.mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			fadeOuts();
		}
	}
	
	@Override
	public void initAllObjects(List<Visible> viewObjects) {
		
		background = updateBackground("resources\\backgrounds\\start.jpg");
		title = updateTitle("resources\\title.png");
		start = updateStart("resources\\ui\\buttons\\startbutton.png");
		
		title.setAlpha(0.0f);
		start.setAlpha(0.0f);
		
		scrollIn();
		
		viewObjects.add(background);
		viewObjects.add(title);
		viewObjects.add(start);
	}
//--Start ( [START] )--//
	private Graphic updateStart(String path) {
		ImageIcon icon = new ImageIcon(path);
		int w; int h; int x; int y;
		w = getWidth()/5;
		h = getHeight()/5;
		x = (getWidth()/2) - w/2;
		y = (getHeight()/2) - h/2 + 100; //will have to modify 100 in order to scale
		
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
	
//--BACKGROUND (background)--//
	private Graphic updateBackground(String path) {
		ImageIcon icon = new ImageIcon(path);
		int w; int h; // 0 for either will use original image size/width
		int x = 0; int y = 0;
		if(background != null) {
			x = background.getX(); y = background.getY();
		}
		w = getWidth();
		//GUIApp scales the height last
/*needs fixing*/h = (int) ((getWidth()/icon.getIconWidth())*icon.getIconHeight()+100); //makes the width of background always match the screen
		return new Graphic(x,y,w,h,path);
	}
	
//--EVENTS--//
	public void scrollIn() {
		
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
		}, 0, 10); //100fps
	}
	public void scrollInEnd() {
		time.cancel();
		fadeIns();
		background.setY(-background.getHeight()/2+getHeight());
		screenPhase = 1;
	}
	
	public void fadeIns() {
		
		background.setY(-background.getHeight()/2+getHeight());
		title.setAlpha(0.0f);start.setAlpha(0.0f);
		
		time = new Timer();
		time.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if(title.getAlpha() > 0.7f) {
					if(start.getAlpha() < 0.99f) { //begins the fade in for startbutton
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
		}, 0, 10); //100fps
	}
	public void fadeInsEnd() {
		time.cancel();
		allowClick = true; GUIApplication.mainFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));
		title.setAlpha(1f);start.setAlpha(1f);
		screenPhase = 2;
	}
	
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
		}, 0, 10); //100fps
	}
	public void fadeOutsEnd() {
		time.cancel();
		scrollOut();
		start.setAlpha(0.0f);title.setAlpha(0.0f);
		screenPhase = 3;
	}
	
	public void scrollOut() {
		
		title.setAlpha(0f);start.setAlpha(0f);
		
		time = new Timer();
		time.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if(background.getY() > -background.getHeight() + getHeight()*2) {
					background.setY(background.getY() - 1);
				}else {
					scrollOutEnd();
/**/				//Test.test.setScreen(new MainMenuScreenG(getWidth(),getHeight()));
					//this.cancel();
				}
			}
		}, 0, 10); //100fps
	}
	public void scrollOutEnd() {
		screenPhase = 4;
		time.cancel();
		background.setY(-background.getHeight()*2 + getHeight());
		Test.test.setScreen(new MainMenuScreenG(getWidth(),getHeight()));
	}
	
	
	//--Create setDimensions method which will resize/redraw the images based off window size changes--//
}
