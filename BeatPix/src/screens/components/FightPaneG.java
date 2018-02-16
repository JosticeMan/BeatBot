package screens.components;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import gui.components.AnimatedComponent;
import gui.components.FullFunctionPane;
import gui.interfaces.FocusController;
import gui.interfaces.Visible;
import mainGame.MainGUI;
import mainGame.components.interfaces.robotAct;

public class FightPaneG extends FullFunctionPane implements robotAct{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6669770711157703541L;
	private ArrayList<EnemyRobot> enemies;
	private AnimatedComponent robotIdle;
	private AnimatedComponent robotHit1;
	private AnimatedComponent robotHit3;
	private AnimatedComponent robotHit2;
	private AnimatedComponent robotMiss;
	private AnimatedComponent enemyIdle;
	private AnimatedComponent enemyHit;
	private AnimatedComponent enemyMiss;

	private Thread idleThread;
	private Thread hit1Thread;
	private Thread hit2Thread;
	private Thread hit3Thread;
	private Thread missThread;
	private Thread enemyIdleThread;
	private Thread enemyHitThread;
	private Thread enemyMissThread;
	
	private boolean animationRunning = false;
	private boolean miss;
	private int pastRand;
	private boolean isPaused;
	private String rsrcFile;
	private String enemyFile;
	private String enemyPicture;
	private String skin;
	private int randX;
	private int randY;

	public FightPaneG(FocusController focusController, int x, int y) {
		super(focusController, x, y, 400, 200);
		enemies = new ArrayList<EnemyRobot>();
		// TODO Auto-generated constructor stub
	}

	public void initAllObjects(List<Visible> viewObjects){
		rsrcFile = "resources/sprites/defaultSprite.bmp";
		enemyFile = "resources/sprites/EnemySprite.png";
		enemyPicture = "resources/sprites/enemyPic_Transparent.png";
		changeSkin();
		robotIdle = new AnimatedComponent(30, 100, 117, 84);
		robotHit1 = new AnimatedComponent(30, 100, 117, 84);
		robotHit3 = new AnimatedComponent(30, 100, 117, 84);
		robotHit2 = new AnimatedComponent(30, 100, 117, 84);
		robotMiss = new AnimatedComponent(30, 100, 117, 84);
		robotIdle.addSequence(rsrcFile, 200, 0, 0, 39, 28, 2);
		robotHit1.addSequence(rsrcFile, 200, 0, 105, 39, 28, 4);
		robotHit1.setVisible(false);
		robotHit2.addSequence(rsrcFile, 200, 0, 34, 39, 27, 5);
		robotHit2.setVisible(false);
		robotHit3 = new AnimatedComponent(30,100,117,84);
		robotHit3.addSequence(rsrcFile, 200, 0, 70, 39, 27, 3);
		robotHit3.setVisible(false);
		robotMiss = new AnimatedComponent(30,100,117,84);
		robotMiss.addSequence(rsrcFile, 200, 0, 146, 39, 27, 2);
		robotMiss.setVisible(false);
		enemyIdle = new AnimatedComponent(240,100,117,84);
		enemyIdle.addSequence(enemyFile, 200, 0, 0, 39, 27, 2);
		enemyHit = new AnimatedComponent(240,100,117,84);
		enemyHit.addSequence(enemyFile, 200, 0, 33, 39, 27, 5);
		enemyMiss = new AnimatedComponent(240,100,117,84);
		enemyMiss.addSequence(enemyFile, 200, 0, 69, 39, 27, 2);
		
		hit1Thread = new Thread(robotHit1);
		hit2Thread = new Thread(robotHit2);
		hit3Thread = new Thread(robotHit3);
		missThread = new Thread(robotMiss);
		idleThread = new Thread(robotIdle);
		enemyIdleThread = new Thread(enemyIdle);
		enemyHitThread = new Thread(enemyHit);
		enemyMissThread = new Thread(enemyMiss);
		
		missThread.start();
		hit1Thread.start();
		hit2Thread.start();
		hit3Thread.start();
		idleThread.start();
		enemyIdleThread.start();
		enemyHitThread.start();
		enemyMissThread.start();
		
		addKeyListener(this);
		setFocusable(true);
		
		viewObjects.add(robotIdle);
		viewObjects.add(robotHit1);
		viewObjects.add(robotHit2);
		viewObjects.add(robotHit3);
		viewObjects.add(robotMiss);
		viewObjects.add(enemyHit);
		viewObjects.add(enemyMiss);
		viewObjects.add(enemyIdle);

		robotIdle.setVisible(true);
		enemyIdle.setVisible(true);
	}
	
	public void keyPressed(KeyEvent e)
	{	
		/*String pressedKey = "" + (char) e.getKeyCode(); 
		if((pressedKey.equalsIgnoreCase(MainGUI.getKeys(0)) || pressedKey.equalsIgnoreCase(MainGUI.getKeys(1)) || pressedKey.equalsIgnoreCase(MainGUI.getKeys(2)) || pressedKey.equalsIgnoreCase(MainGUI.getKeys(3))) && (animationRunning == false) && (isPaused == false)) {
			if(!miss) {
				robotIdle.setVisible(false);
				robotHit1.setVisible(false);
				robotHit2.setVisible(false);
				robotHit3.setVisible(false);
				robotMiss.setVisible(false);
				enemyIdle.setVisible(false);
				enemyHit.setVisible(false);
				enemyMiss.setVisible(false);
				animationRunning = true;
				
				
				int rand = (int) (Math.random()*3);
				while(rand == pastRand)
					rand = (int) (Math.random()*3);
				pastRand = rand;
				
				if(rand == 0) {
					setAnimation(robotHit1, 700);
					setAnimation(enemyMiss, 700);
				} 
				else if(rand == 1) {
					setAnimation(robotHit2, 900);
					setAnimation(enemyMiss, 900);
				} 
				else if(rand == 2) {
					setAnimation(robotHit3, 500);
					setAnimation(enemyMiss, 900);
				}
			}
			else
			{
				setAnimation(robotMiss, 300);
				setAnimation(enemyHit, 300);
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
			updateScreen();
		}*/
	}
	
	//Adds an enemy robot to the "army" and adds it to the screen as a gimmick
	public void updateScreen() { 
		//pane size is 480 x 180
		randX = (int) (Math.random() * 90 + 250); 
		randY = (int) (Math.random() * 160 + 10); 
		EnemyRobot enemyCopy = new EnemyRobot(randX, randY, 58, 42, enemyPicture);
		enemies.add(enemyCopy);
		for(int i = 0; i < enemies.size(); i++)
		{
			System.out.print(randX + ", " + randY);
			this.addObject(enemies.get(i));
		}
		
	}

	//Changes the visibility of the threads as a way to enable the player to "attack" the enemy robot
	public void setAnimation(AnimatedComponent a, int s) {
		a.setVisible(true);
		Thread show = new Thread(a);
		show.start();
		Timer time = new Timer();
		time.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				enemyIdle.setVisible(true);
				robotIdle.setVisible(true);
				a.setVisible(false);
				a.setRunning(false);
				animationRunning = false;
			}
		}, s);
	}
	 
	//corresponds with the pause option used in the main game
	public void pause() {
		robotIdle.setVisible(true);
		robotHit1.setVisible(false);
		robotHit2.setVisible(false);
		robotHit3.setVisible(false);
		robotMiss.setVisible(false);
		robotIdle.setRunning(false);
		robotIdle.setVisible(true);
		enemyIdle.setRunning(false);
		enemyHit.setVisible(false);
		enemyMiss.setVisible(false);
		isPaused = true;
	}
	
	//corresponds with the resume option used in the main game
	public void resume()
	{
		isPaused = false;
		robotIdle.setRunning(true);
		enemyIdle.setRunning(true);
	}
	
	public void changeSkin()
	{
		skin = MainGUI.test.character.getSkin();
		if(skin == "default")
			rsrcFile = "resources/sprites/defaultSprite.bmp";
		if(skin == "red")
			rsrcFile = "resources/sprites/redSprite.png";
		if(skin == "white")
			rsrcFile = "resources/sprites/whiteSprite.png";
		if(skin == "green")
			rsrcFile = "resources/sprites/greenSprite.png";
	}

	@Override
	public void hit(double score) {
		if(score!=0) {
			robotIdle.setVisible(false);
			robotHit1.setVisible(false);
			robotHit2.setVisible(false);
			robotHit3.setVisible(false);
			robotMiss.setVisible(false);
			enemyIdle.setVisible(false);
			enemyHit.setVisible(false);
			enemyMiss.setVisible(false);
			animationRunning = true;
			
			int rand = (int) (Math.random()*3);
			while(rand == pastRand)
				rand = (int) (Math.random()*3);
			pastRand = rand;
			
			if(rand == 0) {
				setAnimation(robotHit1, 700);
				setAnimation(enemyMiss, 700);
			} 
			else if(rand == 1) {
				setAnimation(robotHit2, 900);
				setAnimation(enemyMiss, 900);
			} 
			else if(rand == 2) {
				setAnimation(robotHit3, 500);
				setAnimation(enemyMiss, 900);
			}
		}else {
			setAnimation(robotMiss, 300);
			setAnimation(enemyHit, 300);
			if(Math.random() < 0.1)
				updateScreen();
		}
	}

	@Override
	public void miss(double score) {
		// TODO Auto-generated method stub
		setAnimation(robotMiss, 300);
		setAnimation(enemyHit, 300);
		updateScreen();
	}
	
}