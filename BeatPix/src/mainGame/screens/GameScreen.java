package mainGame.screens;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import gui.components.Action;
import gui.components.Graphic;
import gui.components.TextArea;
import gui.interfaces.Clickable;
import gui.interfaces.FocusController;
import gui.interfaces.Visible;
import gui.userInterfaces.ClickableScreen;
import mainGame.MainGUI;
import mainGame.actions.Escape;
import mainGame.actions.Press;
import mainGame.actions.ReleasePress;
import mainGame.components.*;
import mainGame.screens.interfaces.ResizableScreen;
import screens.components.FightPaneG;

public class GameScreen extends ResizableScreen implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8016038914105990793L;
	
	//Justin Yau
	private String[] bindings; //The keystrokes that the user presses for the left column, left center column, right center column, and right column, respectively.
	
	private String title; //Title of the beatmap 
	private int BPM; //Beats per minute from the beatmap
	private String artist; //Artist of the beatmap
	private int offSet; //Offset of the beatmap
	private ArrayList<int[]> beats; //Beats that will be majorly utilized by this screen
	private Song mainSong; //The song of the game will be stored here
	
	private long startTime; //The starting time in ms
	private boolean playing; //This will be used to determine whether there are more beats to display or not
	
	private ArrayList<Visible> strokes ; //All the keystrokes currently on the screen will appear here
	private ArrayList<Holdstroke> holds; //All the holdstrokes currently being held down will appear here
	private ArrayList<Holdstroke> tooLongHolds; //All the holdstrokes that user overheld for
	
	private ArrayList<OptionButton> optBTN; //All the option buttons will be on the screen here
	
	private ColoredRectangle pauseRect; //This rectangle will represent the rectangle spawned in when the escape button is pressed
	private Gear escapeGear; //The gear the user can press to open the escape menu will be stored here
	
	private boolean pause; //This boolean will be used to keep track if the game is paused or not
	private int fallTime; //The single call fall time calculated from BPM will be stored here
	
	public static GameScreen game; //This will be used to make instance calls from other classes
	
	public static final int columnY = 75; //This is the set Y coordinate of the top of the columnLanes
	public static final int columnWidth = 70; //This is the width of the lanes
	public static final int columnHeight = 350; //This is the height of the lanes
	public static final int distanceG = 100; //Distance from the goal before the user can make a press for a stroke
	public static final int distanceAAfterGoal = 10; //Distance after goal the keystrokes will stay on the screen
	
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW; //Register input when the user is in the window
    private InputMap imap; //This input map enables us to do bindings 
    private ActionMap amap; //This action map enables us to put actions into bindings
    
    private Thread gameThread; //This is the thread that will make the game spawn objects
    private boolean gameRunning; //This boolean will tell us if the game is currently running or not
    
    private String backgroundPath; //The path to the background image will be stored here
	
	public static final String[] arrowPaths = {"larrow", "darrow", "uarrow","rarrow"}; //Img file names for the sprite sheets
	public static final int[] arrowX = {100, 170, 240, 310}; //X coordinates of the indicators
	//Justin Yau
	
	//Steven
	private Timing timing;
	private TextArea visual;
	private Accuracy accDisplay;
	private float[] totalAcc;
	private float accuracy;
	private CustomText combo;
	private CustomText ctext;
	private int comboCount;
	private ArrayList<Timing2> timings;

	//Steven
	//tyler
	private Scoring gamescore;
	private float score =0;
	private float health = 100;
	int ra;
	private CustomText displayScore;
	//tyler
	public GameScreen(int width, int height, Song song) {
		super(width, height);
		
		setFixedSize(false);
		setPreferredSize(new Dimension(width, height));
		
		game = this;
		
		//Retrieve metadata and beats from the song
		mainSong = song;
		title = song.getTitle();
		BPM = song.getBPM();
		artist = song.getArtist();
		offSet = song.getOffSet();
		beats = song.getBeats();
		
		setUpBindings();
		
		totalAcc=new float[beats.size()];
		for(int i=0;i<totalAcc.length;i++) {
			totalAcc[i]=-1;
		}
		
		accuracy=100;
		
		gameRunning = false;
		start();
	}
	
	public GameScreen(int width, int height, Song song, String backPath) {
		super(width, height);
		
		setFixedSize(false);
		setPreferredSize(new Dimension(width, height));
		
		game = this;
		backgroundPath = backPath;
		
		//Retrieve metadata and beats from the song
		mainSong = song;
		title = song.getTitle();
		BPM = song.getBPM();
		artist = song.getArtist();
		offSet = song.getOffSet();
		beats = song.getBeats();
		
		setUpBindings();
		
		totalAcc=new float[beats.size()];
		for(int i=0;i<totalAcc.length;i++) {
			totalAcc[i]=-1;
		}
		
		accuracy=100;
		
		gameRunning = false;
		start();
	}

	/**
	 * This method creates a new thread and starts it
	 * 
	 * @author Justin Yau
	 */
	public void start() {
		if(gameRunning) { return; }
		gameThread = new Thread(this);
		gameRunning = true;
		gameThread.start();
	}
	
	/**
	 * This method stops the current thread 
	 * 
	 * @author Justin Yau
	 */
	public void stop() {
		if(!gameRunning) { return; } 
		gameRunning = false;
		playing = false;
		resumeGame();
		cancelAllFalls();
	}
	
	/**
	 * This method goes through each stroke and cancels their fall
	 * 
	 * @author Justin Yau
	 */
	public void cancelAllFalls() {
		for(Visible str: strokes) {
			if(str instanceof Keystroke) {
				((Keystroke)str).cancelFall();
			}
			else if(str instanceof Holdstroke) {
				((Holdstroke)str).cancelFall();
			}
		}
		for(Holdstroke str1: holds) {
			str1.cancelFall();
		}
		for(Holdstroke str2: tooLongHolds) {
			str2.cancelFall();
		}
	}
	
	/**
	 * This method overrides the default adapter to resize clickable components 
	 * @return The New Component Adapter to suit our needs
	 * 
	 * @author Justin Yau
	 */
	public ComponentAdapter getComponentAdapter() {
		return new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				Component c = (Component)arg0.getSource();
				int height = c.getHeight();
				int width = c.getWidth();
				setXScale(((double) width)/getOWidth());
				setYScale(((double) height)/getOHeight());
				escapeGear.setY((int) (escapeGear.getOY() - (6 * getYScale())));
				escapeGear.updateScales(getXScale(), getYScale());
				for(OptionButton btn: optBTN) {
					btn.updateScales(getXScale(), getYScale());
				}
			}
			
		};
	}
	
	/**
	 * This method sets up the default bindings for the game
	 * 
	 * @author Justin Yau
	 */
	public void setUpBindings() {
		bindings = new String[4];
		updateKeyStrokes("D", "F", "J", "K");
		imap = getInputMap(IFW);
		amap = getActionMap();
		for(int i = 0; i < bindings.length; i++) {
			KeyStroke key = KeyStroke.getKeyStroke(bindings[i]); 
			KeyStroke releasedKey = KeyStroke.getKeyStroke("released " + bindings[i]);
			amap.put(key, new Press(i+1));
			amap.put(releasedKey, new ReleasePress(i+1));
			imap.put(key, key);
			imap.put(releasedKey, releasedKey);
		}
		//imap.put(leftKey, actionMapKey);
		amap.put("ESCAPE", new Escape());
		
		imap.put(KeyStroke.getKeyStroke("ESCAPE"), "ESCAPE");
		
		this.requestFocus();
	}
	
	/**
	 * This method removes the specified stroke from the arraylist of strokes
	 * @param str - The stroke you would like to remove
	 * 
	 * @author Justin Yau
	 */
	public void removeFromStrokes(Visible str) {
		strokes.remove(str);
	}
	
	/**
	 * This method removes the specified stroke from the array list of holds that were held too long
	 * @param str - The hold stroke you would like to remove
	 * 
	 * @author Justin Yau
	 */
	public void removeFromTooLongHolds(Holdstroke str) {
		tooLongHolds.remove(str);
	}
	
	/**
	 * This method returns an array list of the current holds
	 * @return - Returns an array list of the current holds
	 * 
	 * @author Justin Yau
	 */
	public ArrayList<Holdstroke> getHolds() {
		return holds;
	}
	
	/**
	 * This method returns an array list of holds that were held too long
	 * @return - Returns an array list of holds that were held too long
	 * 
	 * @author Justin Yau
	 */
	public ArrayList<Holdstroke> getTooLongHolds() {
		return tooLongHolds;
	}
	
	/**
	 * This method adds the specified hold stroke to the array list of hold strokes
	 * @param str - The stroke you would like to add
	 * 
	 * @author Justin Yau
	 */
	public void addHold(Holdstroke str) {
		holds.add(str);
	}
	
	/**
	 * This method returns whether or not the current game is paused
	 * 
	 * @return Returns whether or not the current game is paused
	 * 
	 * @author Justin Yau
	 */
	public boolean getPause() {
		return pause;
	}
	
	/**
	 * Call this method to rebind one of the bindings 
	 * @param newKey - The new key you would like to bind the action to
	 * @param oldKey - The old key that you would like to change the action from
	 * 
	 * @author Justin Yau
	 */
	public void rebindKey(String newKey, String oldKey) {
		getInputMap(IFW).put(KeyStroke.getKeyStroke(newKey), getInputMap(IFW).get(KeyStroke.getKeyStroke(oldKey)));
		getInputMap(IFW).remove(KeyStroke.getKeyStroke(oldKey));
	}
	
	/**
	 * Method to update the buttons that the user has to press to make strokes
	 * @param stroke1 - Key to be pressed for left stroke
	 * @param stroke2 - Key to be pressed for left center stroke
	 * @param stroke3 - Key to be pressed for right center stroke
	 * @param stroke4 - Key to be pressed for right stroke
	 * @author Justin Yau 
	 */
	public void updateKeyStrokes(String stroke1, String stroke2, String stroke3, String stroke4) {
		String[] temp = {stroke1, stroke2, stroke3, stroke4};
		bindings = temp;
	}
	
	/**
	 * This method returns the first stroke in the given lane IF the first stroke in the lane has the same starting time as the first stroke in the list. <br> 
	 * Returns null if there are no strokes. 
	 * 
	 * @param lane - The lane you would like to find the first stroke to. 
	 * @return - Returns the first stroke in the given lane. Null if there are no strokes.
	 * 
	 * @author Justin Yau
	 */
	public Visible getFirstInLane(int lane) {
		int startingTime = getFirstStrokeStartingTime();
		if(strokes.size() > 0 && startingTime >= 0) {
			Visible firstStroke = strokes.get(0);
			for(Visible stroke: strokes) {
				if(stroke instanceof Keystroke) {
					Keystroke str = ((Keystroke)stroke);
					if(str.getColumnLane() == lane && str.getStartingTime() == startingTime) {
						return stroke;
					}
				}
				if(stroke instanceof Holdstroke) {
					Holdstroke str = ((Holdstroke)stroke);
					if(str.getColumnLane() == lane && str.getStartingTime() == startingTime) {
						return stroke;
					}
				}
			}
			return firstStroke;
		}
		return null;
	}
	
	/**
	 * This method returns the lanes currently being held
	 * 
	 * @return Return the lanes currently being held
	 * 
	 * @author Justin Yau
	 */
	public ArrayList<Integer> currentlyHeldLanes() {
		ArrayList<Integer> list = new ArrayList<Integer>(0);
		for(Holdstroke stroke: holds) {
			list.add(stroke.getColumnLane());
		}
		return list;
	}
	
	/**
	 * This method returns the lanes that have been held too long
	 * 
	 * @return Returns the lanes that have been held too long
	 * 
	 * @author Justin Yau
	 */
	public ArrayList<Integer> currentTooLongLanes() {
		ArrayList<Integer> list = new ArrayList<Integer>(0);
		for(Holdstroke stroke: tooLongHolds) {
			list.add(stroke.getColumnLane());
		}
		return list;
	}
	
	/**
	 * This method retrieves the starting time of the first stroke
	 * 
	 * @return Returns the starting time of the first stroke
	 * 
	 * @author Justin Yau
	 */
	public int getFirstStrokeStartingTime() {
		if(strokes.size() > 0) {
			Visible firstStroke = strokes.get(0);
			if(strokes.get(0) instanceof Keystroke) {
				return ((Keystroke)firstStroke).getStartingTime(); 
			}
			if(strokes.get(0) instanceof Holdstroke) {
				return ((Holdstroke)firstStroke).getStartingTime();
			}
		}
		return -1;
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
		 * - Create the lanes in the initAllObjects  - Completed
		 * - Read the map file in this class - Completed
		 * - Spawn the keystroke into the lanes in this class - Completed
		 * - Left Column - X: 100 Y: 75
		 * - Left Center Column - X: 170 Y: 75
		 * - Right Column - X: 240 Y: 75
		 * - Right Center Column - X: 310 Y: 75
		 * - Pause - Completed
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
		addKeystrokeIndicator(viewObjects);
		setUpGearButton(viewObjects);
		spawnOptionButtons();
		spawnRobot();
		
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
		
		timing=new Timing(175,300, 128, 128);
		viewObjects.add(timing);
		timing.update();
		/*accDisplay=new Accuracy(600,30,400,400);
		viewObjects.add(accDisplay);
		accDisplay.update();
		*/
		combo=new CustomText(215,100, 50, 50,"0");
		viewObjects.add(combo);

		ctext=new CustomText(550,450,200,200,"100%");
		viewObjects.add(ctext);
		gamescore = new Scoring(500,40,400,400);
		viewObjects.add(gamescore);

		displayScore = new CustomText(550,400,200,200,"0000000");
		viewObjects.add(displayScore);
		gamescore.update();  
		
		
	}
	
	/**
	 * This method spawns the animated robot that'll attack according to the keys
	 * 
	 * @author Justin Yau
	 */
	public void spawnRobot() {
		//FightPaneG robot = new FightPaneG(, 600, 200);
		//addObject(robot);
	}
	
	/**
	 * This method adds the background if there was one specified in the constructor
	 * 
	 * @author Justin Yau
	 */
	public void addBackground() {
		if(backgroundPath != null && backgroundPath != "") {
			Graphic g = new Graphic(0,0, determineScale(backgroundPath), backgroundPath);
			addObject(g);
			moveToBack(g);
		}
	}
	
	/**
	 * Determines the appropriate scale for the background image such that it fits entirely on the screen
	 * @param path - Path of the background file
	 * @return - The appropriate scale to apply to the image to make it fit the screen
	 * 
	 * @author Justin Yau
	 */
	public double determineScale(String path) {
		double scale = 1;
		try {
			BufferedImage img = ImageIO.read(new File(backgroundPath));
			scale = ((double)getWidth())/img.getWidth();
		} catch (IOException e) {
		}
		return scale;
	}
	
	/**
	 * This method adds the gear button to the screen
	 * @param viewObjects - the list of objects that will be visible by the display
	 * 
	 * @author Justin Yau
	 */
	public void setUpGearButton(List<Visible> viewObjects) {
		escapeGear = new Gear(2, 25, 50, 50);
		viewObjects.add(escapeGear);
	}

	/**
	 * This method will create 4 visuals that indicate where the user will want to time their presses to
	 * 
	 * @param viewObjects -  The list of objects that will be visible by the display
	 * 
	 * @author Justin Yau
	 */
	
	public void addKeystrokeIndicator(List<Visible> viewObjects) {
		
		for(int i = 0; i < arrowX.length; i++) {
			KeystrokeIndicator indicator = new KeystrokeIndicator(arrowX[i], columnY + columnHeight, "resources/arrows/" + arrowPaths[i] + "ph.png");
			indicator.setAlpha((float)0.3);
			viewObjects.add(indicator);
		}
		
	}
	
	/**
	 * This method will add 4 visuals that will represent the lanes that the strokes will drop down through
	 * 
	 * @param viewObjects - The list of objects that will be visible by the display
	 * 
	 * @author Justin Yau
	 */
	public void addColumnLanes(List<Visible> viewObjects) {
		
		for(int i = 0; i < arrowX.length; i++) {
			ColumnLane lane = new ColumnLane(arrowX[i] - 3,columnY - 5, columnWidth, columnY + columnHeight + GameScreen.distanceAAfterGoal);
			lane.setAlpha((float)0.3);
			viewObjects.add(lane);
		}
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
 	 * This method will handle the registering of normal stroke
 	 * 
 	 * @param strokes - The list of strokes to check
 	 * @param keys - The legal key presses the user can make for the strokes
 	 * @param e - The key press event information
 	 * 
 	 * @author Justin Yau
 	 */
	/*
 	public void handleNormalStroke(ArrayList<Keystroke> strokes, int[] keys, KeyEvent e) {
		boolean correctStroke = false;
		
		for(Keystroke stroke: strokes) {
			if(e.getKeyCode() == keys[stroke.getColumnLane() - 1]) {
				displayAcc(stroke);
				
				removeStroke(stroke); 
				stroke.cancelFall();
				correctStroke = true;
				break;
			
			} 
		}
		if(!correctStroke && madeLegalStroke(e)) {
			//CALCULATE MISS ACCURACY HERE PLACEHOLDER 
			
			Keystroke cStroke = strokes.get(0);
			removeStroke(cStroke);
			cStroke.cancelFall();
		}
 		
 	}
 	*/
	

	/*private void displayAcc(Keystroke stroke) {
		//System.out.println(timePass());
		//System.out.println(stroke.getClickTime());
		//System.out.println(Math.abs(timePass()-stroke.getClickTime()));
		if(Math.abs(timePass()-stroke.getClickTime())<16) {
			timing.changeImg("resources/perfect.png");
			combo.add();
			calcAcc(1);
			return ;
		}
		if(Math.abs(timePass()-stroke.getClickTime())<40) {
			timing.changeImg("resources/great.png");
			combo.add();
			calcAcc(.95);
			return ;
		}
		if(Math.abs(timePass()-stroke.getClickTime())<73) {
			timing.changeImg("resources/good.png");
			combo.add();
			calcAcc(.66);
			return ;
		}
		if(Math.abs(timePass()-stroke.getClickTime())<103) {
			timing.changeImg("resources/ok.png");
			combo.add();
			calcAcc(.5);
			return ;
		}
		if(Math.abs(timePass()-stroke.getClickTime())<127) {
			timing.changeImg("resources/bad.png");
			combo.add();
			calcAcc(.33);
			return ;
		}
		if(Math.abs(timePass()-stroke.getClickTime())<164) {
			timing.changeImg("resources/miss.png");
			combo.set();
			calcAcc(0);
			return ;
		}
	}*/

	public void calcScore(double timing) {
		if(timing==1) {
			score+=1000000/beats.size()*1;
		}
		if(timing==.95) {
			score+=1000000/beats.size()*.95;
		}
		if(timing==.66) {
			score+=1000000/beats.size()*.66;
		}
		if(timing==.5) {
			score+=1000000/beats.size()*.5;
		}
		if(timing==.33) {
			score+=1000000/beats.size()*.33;
			health= health-1;
		//	System.out.println(health);			
		}
		if(timing==0) {
			score+=0;
			health = health-3;
		//	System.out.println(health);	
		}
		int tScore = (int)Math.round(score);
		String display = String.valueOf(tScore);
		//display = 300;
		
		int[] scoreA = {0,0,0,0,0,0,0,0,0};

		for(int i = 0; i<display.length();i++) {
		
			ra = Integer.parseInt(display.substring((display.length()-1)-i,(display.length())-i ));
			
			System.out.println(ra);
			//System.out.println(scoreA.length-i);
			scoreA[scoreA.length-i-1] = ra;
			
		}
	//	scoreA[scoreA.length-2] = 2;
		//scoreA[scoreA.length-2] = display.charAt();
		//System.out.println(Arrays.toString(scoreA));
		ArrayList<Integer> intList = new ArrayList<Integer>();
		for (int i : scoreA)
		{
		    intList.add(i);
		    
		}
		/*for (int i : intList) {
			System.out.println(i);
		}*/
		
	//	System.out.println(Arrays.deepToString(intList));
		displayScore.setText(display);
	} 

	public void calcAcc(double timing) {
		int totalHit=0;
		for(int i=0;i<totalAcc.length;i++) {
			if(totalAcc[i]==-1) {
				totalAcc[i]=(float) timing;
				break;
			}
		}
		double acc=0;
		for(double a:totalAcc) {
			if(a!=-1) {
				totalHit++;
				acc+=a;
			}
		}
		
		acc=acc/totalHit;
		accuracy=((float)Math.round(acc*10000)/100);
		//System.out.println(accuracy);
		int temp=(int) (accuracy*100);
		String set=accuracy+"%";
		if(temp%10==0) {
			set=accuracy+"0%";
		}
		if(temp<1000) {
			set="0"+set;
		}
		ctext.setText(set);
	}
	
	public void calcCombo(boolean isMiss) {
		if(isMiss) {
			comboCount=0;
		}else {
			comboCount++;
		}
		combo.setText(comboCount+"");
	}

	/**
	 * This methods returns whether the user pressed one of the keys that represented a stroke. 
	 * 
	 * @param e - The KeyEvent that contains what key the user pressed
	 * @return - Whether the key pressed was one of the keys that the user was suppose to press to remove a stroke
	 * 
	 * @author Justin Yau
	 */
	/*
	public boolean madeLegalStroke(KeyEvent e) {
		int[] keys = bindings;
		for(int key: keys) {
			if(e.getKeyCode() == key) {
				return true;
			}
		}
		return false;
	}
	*/

	@Override
	public void run() {
		startTime = (System.nanoTime());
		playing = true;
		pause = false;
		addBackground();
		initializeStrokeArrayList();
		calculateAndSetFallTimeFromBeats();
		playMap();
	}
	
	/**
	 * This method resets/initializes the arraylists
	 * 
	 * @author Justin Yau
	 */
	public void initializeStrokeArrayList() {
		strokes = new ArrayList<Visible>(0);
		holds = new ArrayList<Holdstroke>(0);
		tooLongHolds = new ArrayList<Holdstroke>(0);
	}
	
	/**
	 * This method will return the time, in milliseconds, that has ellapsed since the game has started running
	 * 
	 * @return - The time that has ellapsed since start of this particular game, in milliseconds.
	 * 
	 * @author Justin Yau
	 */
	public long timePass() {
		return ((System.nanoTime() - startTime))/1000000;
	}
	
	/**
	 * This method will set the startTime to the appropriate amount of time that has elapsed. <br> 
	 * To be called after resuming the game
	 * 
	 * @author Justin Yau
	 */
	public void recalculateStartTime(long ellapsedTime) {
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
		e.cancelFall();
		remove(e);
		remove(e); //Just in case it doesn't get removed the first time
	}
	
	/**
	 * This method will remove the given stroke from this arrayList and the visibleObjects
	 * 
	 * @param e - The stroke that you would like to remove
	 * 
	 * @author Justin Yau
	 */
	public void removeHoldStroke(Holdstroke e) {
		strokes.remove(e);
		if(holds.contains(e)) {
			tooLongHolds.add(e);
			holds.remove(e);
		}
		e.cancelFall();
		remove(e);
		remove(e);
	}
	
	/**
	 * This method will change a boolean that will halt all game operations and bring up the escape menu
	 * 
	 * @author Justin Yau
	 */
	public void pauseGame() {
		pause = true;
		ColoredRectangle rect = new ColoredRectangle(0,0, getOWidth(), getOHeight(), ((float)0.3), Color.GRAY);
		pauseRect = rect;
		addObject(pauseRect);
		if(escapeGear != null) {
			remove(escapeGear);
		}
		for(OptionButton btn: optBTN) {
			addObject(btn);
		}
	}
	
	/**
	 * This method will change a boolean that will resume all game operations and remove the escape menu
	 * 
	 * @author Justin Yau
	 */
	public void resumeGame() {
		pause = false;
		if(pauseRect != null) {
			remove(pauseRect);
			pauseRect = null;
		}
		if(escapeGear != null) {
			addObject(escapeGear);
		}
		for(OptionButton btn: optBTN) {
			remove(btn);
		}
	}
	
	/**
	 * This method creates the 3 option buttons that will appear when the game is escaped or paused
	 * 
	 * @author Justin Yau
	 */
	public void spawnOptionButtons() {
		optBTN = new ArrayList<OptionButton>();
		String[] btnTypes = {"Continue", "Options", "Exit"};
		Action[] act = {new Action() {
			
			@Override
			public void act() {
				//Continue Action Button will be here
				resumeGame();
			}
		}, new Action() {
			
			@Override
			public void act() {
				//Options Action Button will be here
				System.out.println("Options");
			}
		}, new Action() {
			
			@Override
			public void act() {
				//Exit Action Button will be here
				stop();
				//Switch to a different screen below
				MainGUI.test.setScreen(MainGUI.test.mainMenu);
			}
		}};
		for(int i = 0; i < btnTypes.length; i++) {
			OptionButton bt = new OptionButton(350, 150 + (75*i), 200,50,btnTypes[i]);
			bt.setAction(act[i]);
			optBTN.add(bt);
		}
	}
	
	/**
	 * This method pauses all the current strokes that are on the screen
	 * 
	 * @author Justin Yau
	 */
	public void pauseFalls() {
		for(Visible stroke: strokes) {
			if(stroke instanceof Keystroke) {
				((Keystroke)stroke).pauseFall();
			}
			if(stroke instanceof Holdstroke) {
				((Holdstroke)stroke).pauseFall();
			}
		}
	}
	
	/**
	 * This method resumes the fall of all the current strokes that are on the screen
	 * 
	 * @author Justin Yau
	 */
	public void resumeFalls() {
		for(Visible stroke: strokes) {
			if(stroke instanceof Keystroke) {
				((Keystroke)stroke).resumeFall();
			}
			if(stroke instanceof Holdstroke) {
				((Holdstroke)stroke).resumeFall();
			}
		}
	}
	
	/**
	 * This method will pause all operations until it is resumed by setting the pause boolean to false. <br>
	 * All operations will continue running after things are resumed.
	 * 
	 * @author Justin Yau
	 */
	public void handlePause() {
		long time = timePass();
		pauseFalls();
		while(pause) {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		resumeFalls();
		recalculateStartTime(time);
	}
	
	/**
	 * Calculates the total fall time based on column height and fall time. <br>
	 * CALCULATE FALL TIME BEFORE CALLING THIS METHOD
	 * @return Returns the total fall time based on column height and fall time
	 * @author Justin Yau
	 */
	public int calculateTotalFallTime() {
		return fallTime * columnHeight;
	}
	
	/**
	 * This method calculates the fall time from BPM and sets it to the fall time variable
	 * 
	 * @author Justin Yau
	 */
	public void calculateAndSetFallTimeFromBeats() {
		if(BPM == 0 || BPM <= 100) {
			fallTime = 5;
		}
		else {
			fallTime = (int) (((float)1/BPM) * 800);
		}
	}
	
	/**
	 * This method handles the addition of a new keystroke to the gameboard. <br>
	 * Makes the the stroke start falling aswell.
	 * 
	 * @param s - The keystroke you would like to add to the game
	 * 
	 * @author Justin Yau
	 */
	public void handleKeystroke(Keystroke s) {
		strokes.add(s);
		addObject(s);
		Thread tr = new Thread(new Runnable() {
			
			@Override
			public void run() {

				s.keystrokeFall();
					
			}
			
		});
		tr.start();
	}
	
	/**
	 * This method handles the addition of a new holdstroke to the gameboard. <br>
	 * Makes the the stroke start falling aswell.
	 * 
	 * @param s - The holdstroke you would like to add to the game
	 * 
	 * @author Justin Yau
	 */
	public void handleHoldstroke(Holdstroke s) {
		strokes.add(s);
		addObject(s);
		Thread tr = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				s.holdstrokeFall();
				
			}
			
		});
		tr.start();
	}
	
	/**
	 * This method will be used to spawn the strokes in according to the time that has elapsed. 
	 * 
	 * @author Justin Yau
	 */
	public void playMap() {
		while(playing) {
			if(pause) {
				handlePause();
			}
			if(beats.size() == 0) {
				playing = false;
			}
			else if(timePass() >= beats.get(0)[1] && !pause) {
				int[] beat = beats.remove(0);
				int lane = beat[0] - 1;
				if(beat[2] != 0) {
					int height = Holdstroke.determineHeight(beat[2] - beat[1], fallTime);
					if(height >= columnHeight - 20) {
						height = columnHeight - 20;
					}
					Holdstroke str = new Holdstroke(arrowX[lane], columnY, height, beat[1], 
							"resources/arrows/"+ arrowPaths[lane] + "h.png");
					str.updateFallSpeed(fallTime);
					handleHoldstroke(str);
				}
				else {
					Keystroke str = new Keystroke(arrowX[lane], columnY, beat[1], "resources/arrows/" + arrowPaths[lane] + ".png");
					str.updateFallSpeed(fallTime);
					handleKeystroke(str);
				}
				//strokes.add(str);
			}
		}
		mainSong.addScoreAndAccuracy((int) score, accuracy);
	}

	public Timing getTiming() {
		// TODO Auto-generated method stub
		return timing;
	}

	public int getCombo() {
		// TODO Auto-generated method stub
		return comboCount;
	}

	public ArrayList<Timing2> getTimings() {
		// TODO Auto-generated method stub
		return timings;
	}
	
	public void addTimings() {
		for(int i=0;i<getObjects().size();i++) {
			if(getObjects().get(i) instanceof Timing2) {
				getObjects().remove(i);
			}
		}
		for(int i=0;i<timings.size();i++) {
			addObject(timings.get(i));
		}
	}
	
}