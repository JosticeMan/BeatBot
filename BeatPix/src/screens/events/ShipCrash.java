package screens.events;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

import gui.components.AnimatedComponent;
import gui.components.Component;
import gui.components.Graphic;
import gui.userInterfaces.FullFunctionScreen;
import mainGame.screens.MainScreen;
import screens.MainMenuScreenG;
import screens.StartScreenG;

public class ShipCrash{

	Timer time;
	StartScreenG s;
	
	ShipRumble sound;
	private ShipRumble menuSound;

	int timeCount = 0;
	public ShipCrash(StartScreenG screen) {
		this.s = screen;
		time = new Timer();
		time.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() { timeCount++;

            ShipRumble audioPlayer = null;
			if(timeCount == 3) {
				createStartTop();
				String filePath = "resources\\backgrounds\\jet.wav";
				try {
					audioPlayer = new ShipRumble(filePath, false);
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
         
					audioPlayer.play();
			}
			
				if(timeCount > 1500 && timeCount < 2000) { //screenshake at 3s
					screenShake(1); 
				}
				if(timeCount > 2000 && timeCount < 3000) { //screenshake at 3s
					screenShake(2); 
				}
				if(timeCount > 3000 && timeCount < 4500) { //screenshake at 3s
					screenShake(3); 
				}
				if(timeCount == 2500) {
					spaceShipFalling();
					createStartTop();
				}
				if(timeCount == 4500){
					s.remove(actualStartTop);
					s.remove(building);
				}
				if(timeCount > 4500) {
					spaceShipCrash();
				}
				if(timeCount == 4700) {
					try {
						setMenuSound(new ShipRumble("resources\\backgrounds\\test.wav", true));
					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        if(getMenuSound()!=null) {
			            if(getMenuSound().isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			                FloatControl volume = (FloatControl) getMenuSound().getControl(FloatControl.Type.MASTER_GAIN);
			                volume.setValue(-20);
			            }
			        }
					getMenuSound().play();
				}
				if(timeCount == 5000) {
					time.cancel();
					afterFlash();
				}
			}
		}, 0, 2);
	}
	
	Graphic actualStartTop; Graphic building;
	public void createStartTop(){
		if(actualStartTop!= null) {
			s.remove(actualStartTop);
			s.remove(building);
		}
		//ImageIcon icon = new ImageIcon("resources\\backgrounds\\start_top.png");
		actualStartTop = new Graphic(300,100,300, 200,
				"resources\\backgrounds\\cloud1.png");
		ImageIcon icon = new ImageIcon("resources\\backgrounds\\building.png");
		building = new Graphic(0,0,s.getWidth(), icon.getIconHeight()*s.getWidth()/icon.getIconWidth()
							,"resources\\backgrounds\\building.png");
		s.addObject(actualStartTop);
		s.addObject(building);
		
	}
	public void screenShake(int x) {
		if(StartScreenG.background.getX()== 0) {
			if(Math.random() < 0.5) {
				StartScreenG.background.setX(-3*x);
			}else {
				StartScreenG.background.setX(-2*x);
				
			}
		}else {
			StartScreenG.background.setX(0);
		}
		if(StartScreenG.background.getY()== -2*x) {
			if(Math.random() < 0.5) {
				StartScreenG.background.setY(-3*x);
			}else {
				StartScreenG.background.setY(-4*x);
			}
		}else {
			StartScreenG.background.setY(-2*x);
		}
		
	}
	
	int fromOrigin;
	public void screenShake2(){
		int direction = (int) Math.random();
		int distance = (int) Math.random()*3+2;
		if(direction < 0.5 || fromOrigin > 0) {
			shakeComponent(actualStartTop, (-1)*distance,0);
			fromOrigin = -1 * distance;
		}else {
			shakeComponent(actualStartTop, (1)*distance,0);
			fromOrigin = 1 * distance;
		}
	}

	public void shakeComponent(Graphic g, int x, int y) {
		g.setX(g.getX()+x);
		g.setY(g.getY()+y);
	}
	
	AnimatedComponent theShip;
	int currentCloud = 1;
	boolean spaceShipFallStart; Timer personal;
	public void spaceShipFalling() {
		if(!spaceShipFallStart) {
			spaceShipFallStart = true;
			theShip = new AnimatedComponent(400, 90, 50, 50);
			theShip.addSequence("resources\\backgrounds\\test.png", 8, 0, 0, 74, 68, 4);
			
			Thread run = new Thread(theShip);
			run.start();
			s.addObject(theShip);
		}
		
		personal = new Timer();
		personal.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				theShip.setY(theShip.getY() + 8);
				theShip.setX(theShip.getX() - 8);
			}
		}, 0, 80);
	}
	
	Component crashFlash; boolean crashActivated;
	public void spaceShipCrash() {
		if(!crashActivated) {
			crashFlash = new Component(0, 0, 960, 540) {
				public void update(Graphics2D g) {
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, s.getWidth(), s.getHeight());
				}
			};
			crashFlash.setAlpha(0.5f);
			crashFlash.update();
			s.addObject(crashFlash);
			crashActivated = true;
		}
		
		if(timeCount > 4500 && timeCount < 4700) {
			if(crashFlash.getAlpha() == 0.5f) {
				crashFlash.setAlpha(1.0f);
			}else {
				crashFlash.setAlpha(0.5f);
			}
		}else if(timeCount == 7200){
			crashFlash.setAlpha(1.0f);
		}
	}
	
	public void afterFlash() {
		s.scrollIn();
		time = new Timer();
		time.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if(crashFlash.getAlpha() > 0.01f) {
					crashFlash.setAlpha(crashFlash.getAlpha() - 0.03f);
				}
				if(crashFlash.getAlpha() < 0.02) {
					this.cancel();
				}
				
			}
		}, 0, 16);
	}
	public ShipRumble getMenuSound() {
		return menuSound;
	}
	public void setMenuSound(ShipRumble menuSound) {
		this.menuSound = menuSound;
	}
	

}
