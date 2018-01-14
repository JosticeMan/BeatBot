package mainGame.screens;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;

import gui.interfaces.Visible;
import gui.userInterfaces.ClickableScreen;
import mainGame.components.ColumnLane;
import mainGame.components.Keystroke;
import mainGame.components.Song;

public class GameScreen extends ClickableScreen implements KeyListener, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8016038914105990793L;
	
	//Justin Yau
	private int leftStroke; //Keystroke that the user presses for the left column
	private int leftCStroke; //Keystroke that the user presses for the left center column
	private int rightCStroke; //Keystroke that the user presses for the right center column
	private int rightStroke; //Key stroke that the user presses for the right column
	
	private String title; //Title of the beatmap 
	private int BPM; //Beats per minute from the beatmap
	private String artist; //Artist of the beatmap
	private int offSet; //Offset of the beatmap
	private ArrayList<int[]> beats; //Beats that will be majorly utilized by this screen
	
	public static long startTime; //The starting time in ms
	private boolean playing; //This will be used to determine whether there are more beats to display or not
	
	private ArrayList<Keystroke> strokes ; //All the keystrokes currently on the screen will appear here
	
	public static GameScreen game; //This will be used to make instance calls from other classes
	
	public static final int columnY = 75; //This is the set Y coordinate of the top of the columnLanes
	public static final int columnWidth = 70; //This is the width of the lanes
	public static final int columnHeight = 350; //This is the height of the lanes
	
	public static final int[] arrowX = {100, 170, 240, 310}; //X coordinates of the indicators
	//Justin Yau
	
	public GameScreen(int width, int height, Song song) {
		super(width, height);
		
		game = this;
		
		//Retrieve metadata and beats from the song
		title = song.getTitle();
		BPM = song.getBPM();
		artist = song.getArtist();
		offSet = song.getOffSet();
		beats = song.getBeats();
		
		updateKeyStrokes(KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_UP, KeyEvent.VK_RIGHT);
		
		Thread screen = new Thread(this);
		screen.start();
	}

	@Override
	public void initAllObjects(List<Visible> viewObjects) {
		
		/*
		 * Justin Yau's Notes/Plans:
		 * The object the is added later will appear on top of previous added objects
		 * key.getY() will return the Y-Coordinate of the component
		 * key.setY() will set the Y-Coordinate of the component
		 * Changing the Y through this will automatically update the component
		 * 
		 * viewObjects.remove(object) removes the object 
		 * 
		 * Use setAlpha(float int) to make the lanes transparent
		 * 
		 * - Create the lanes in the initAllObjects 
		 * - Read the map file in this class
		 * - Spawn the keystroke into the lanes in this class
		 * - Left Column - X: 100 Y: 75
		 * - Left Center Column - X: 170 Y: 75
		 * - Right Column - X: 240 Y: 75
		 * - Right Center Column - X: 310 Y: 75
		 * - NEED TO IMPLEMENT A PAUSE:
		 * 
		 * Possible way to make the game pause:
		 * - Utilize wait/notify threads which seems complicated and the wrong application of the methods
		 * - Attempt to have a boolean, pause, and have the threads check for the boolean and sleep when true
		 * 
		 * Place where they must hit keystroke - Y: 425
		*/
	
		//CREATE THE LANES
		//THIS ALREADY MAKES THEM TRANSPARENT TO A SENSE
		addColumnLanes(viewObjects);
		
		/*
		Keystroke leftKey = new Keystroke(100, 75, "resources/arrows/darrow.png");
		viewObjects.add(leftKey);
		
		Keystroke leftCKey = new Keystroke(170, 75, "resources/arrows/darrow.png");
		viewObjects.add(leftCKey);
		
		Keystroke rightCKey = new Keystroke(240, 75, "resources/arrows/darrow.png");
		viewObjects.add(rightCKey);
		
		Keystroke rightKey = new Keystroke(310, 425, "resources/arrows/darrow.png");
		viewObjects.add(rightKey);
		*/
	}
	
	/**
	 * This method will add 4 visuals that will represent the lanes that the strokes will drop down through
	 * 
	 * @param viewObjects - The list of objects that will be visible by the display
	 * 
	 * @author Justin Yau
	 */
	public void addColumnLanes(List<Visible> viewObjects) {
		ColumnLane leftLane = new ColumnLane(arrowX[0] - 3,columnY - 5, columnWidth, columnY + columnHeight + 5);
		leftLane.setAlpha((float)0.3);
		viewObjects.add(leftLane);
		
		ColumnLane leftCLane = new ColumnLane(arrowX[1]- 3, columnY -5, columnWidth, columnY + columnHeight + 5);
		leftCLane.setAlpha((float)0.3);
		viewObjects.add(leftCLane);
		
		ColumnLane rightCLane = new ColumnLane(arrowX[2] - 3, columnY -5, columnWidth, columnY + columnHeight + 5);
		rightCLane.setAlpha((float)0.3);
		viewObjects.add(rightCLane);
		
		ColumnLane rightLane = new ColumnLane(arrowX[3] - 3, columnY -5, columnWidth, columnY + columnHeight + 5);
		rightLane.setAlpha((float)0.3);
		viewObjects.add(rightLane);
		//CREATE THE LANES
	}
	
	/**
	 * This method scales the height of the given image and scale
	 * @param imagePath - Image path of the file you would like to scale
	 * @param scale - Height scale that you would like to apply to the image
	 * @return - Resulting image after scale
	 * @author Justin Yau
	 */
	public BufferedImage heightScaleImage(String imagePath, double scale) {
		ImageIcon icon = new ImageIcon(imagePath);
		int newHeight = (int) (icon.getIconHeight() * scale);
		
		BufferedImage image = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.drawImage(icon.getImage(), 0, 0, null);
		
		AffineTransform scaleT = new AffineTransform();
		scaleT.scale(1, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(scaleT, AffineTransformOp.TYPE_BILINEAR);
		image = scaleOp.filter(image,new BufferedImage(icon.getIconWidth(), newHeight, BufferedImage.TYPE_INT_ARGB));
		return image;
	}
	
	/**
	 * Method to update the buttons that the user has to press to make strokes
	 * @param stroke1 - Key to be pressed for left stroke
	 * @param stroke2 - Key to be pressed for left center stroke
	 * @param stroke3 - Key to be pressed for right center stroke
	 * @param stroke4 - Key to be pressed for right stroke
	 * @author Justin Yau 
	 */
	public void updateKeyStrokes(int stroke1, int stroke2, int stroke3, int stroke4) {
		leftStroke = stroke1;
		leftCStroke = stroke2;
		rightCStroke = stroke3;
		rightStroke = stroke4;
	}
	
	/**
	 * Mainly overrided by Justin Yau
	 * This function will run when the user presses a key.
	 * Use e.getKeyCode() and compare it to a key and it will match if the user pressed that key 
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		
		int[] keys = {leftStroke, leftCStroke, rightCStroke, rightStroke};
		ArrayList<Keystroke> strokesToCheck = strokesAtSameTime();
		boolean correctStroke = false;
		for(Keystroke stroke: strokesToCheck) {
			if(e.getKeyCode() == keys[stroke.getColumnLane() - 1]) {
				//CALCULATE PERFECT/GREAT/ALRIGHT/MEH ACCURACY HERE PLACEHOLDER
				
				removeStroke(stroke); 
				stroke.cancelFall();
				correctStroke = true;
				break;
			} 
		}
		if(!correctStroke && strokes.size() > 0) {
			//CALCULATE MISS ACCURACY HERE PLACEHOLDER 
			
			Keystroke cStroke = strokes.get(0);
			removeStroke(cStroke);
			cStroke.cancelFall();

		}
		
	}
	
	/**
	 * Crucial method to enable the program to register keyboard interactions
	 * DO NOT REMOVE
	 * @return
	 */
	@Override
	public KeyListener getKeyListener(){
		return this;
	}

	/**
	 * This method is to retrieve the next stroke and any other strokes that is supposed to be pressed at the same time
	 * @return - An array list of the next stroke and any strokes meant to be pressed at the same time
	 * 
	 * @author Justin Yau
	 */
	public ArrayList<Keystroke> strokesAtSameTime() {
		ArrayList<Keystroke> list = new ArrayList<Keystroke>(0);
		if(strokes.size() != 0) {
			
			list.add(strokes.get(0));
			
			int i = 1;
			
			while(i < 5) {
				if(strokes.size() > i && strokes.get(i).getStartingTime() == list.get(0).getStartingTime()) {
					list.add(strokes.get(i));
				}
				else {
					break;
				}
				i++;
			}
			
		}
		return list;
	}
	
	//We will use this if we want to have a long hold press for the strokes 
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//We won't be using this
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		startTime = (System.nanoTime());
		playing = true;
		strokes = new ArrayList<Keystroke>(0);
		playMap();
	}
	
	/**
	 * This method will return the time, in milliseconds, that has ellapsed since the game has started running
	 * 
	 * @return - The time that has ellapsed since start of this particular game, in milliseconds.
	 * 
	 * @author Justin Yau
	 */
	public static long timePass() {
		return ((System.nanoTime() - startTime))/1000000;
	}
	
	/**
	 * This method will set the startTime to the appropriate amount of time that has elapsed. <br> 
	 * To be called after resuming the game
	 * 
	 * @author Justin Yau
	 */
	public static void recalculateStartTime(long ellapsedTime) {
		long timeEllapsedNano = ellapsedTime * 1000000;
		startTime = System.nanoTime() - timeEllapsedNano;
	}
	
	/**
	 * This method will remove the given stroke from this arrayList and the visibleObjects
	 * 
	 * @param e - The stroke that you would like to remove
	 * 
	 * @author Justin Yau
	 */
	public void removeStroke(Keystroke e) {
		strokes.remove(e);
		remove(e);
		remove(e); //Just in case it doesn't get removed the first time
	}
	
	/**
	 * This method will be used to spawn the strokes in according to the time that has elapsed. 
	 * 
	 * @author Justin Yau
	 */
	public void playMap() {
		while(playing) {
			if(beats.size() == 0) {
				playing = false;
			}
			else if(timePass() >= beats.get(0)[1]) {
				int[] beat = beats.remove(0);
				int lane = beat[0] - 1;
				Keystroke str = new Keystroke(arrowX[lane], columnY, beat[1], "resources/arrows/darrow.png");
				addObject(str);
				strokes.add(str);
				Thread tr = new Thread(new Runnable() {
					
					@Override
					public void run() {
						str.keystrokeFall();
					}
					
				});
				tr.start();
				//strokes.add(str);
			}
		}
	}
}
