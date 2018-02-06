package mainGame.components;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import gui.components.Component;

import gui.interfaces.Visible;
import mainGame.components.interfaces.JustinTimingInterface;
import mainGame.components.interfaces.Stroke;
import mainGame.screens.GameScreen;

public class Timing extends Component implements JustinTimingInterface {
	
	private String img="";
	private float lastTiming=0;

	public Timing(int x, int y, int w, int h) {
		super(x, y, w, h);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(Graphics2D g) {
		super.clear();
		try {
			ImageIcon icon = new ImageIcon(img);
			g.drawImage(icon.getImage(), 0, 0, null);		
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void changeImg(String type) {
		img=type;
		update();
	}

	@Override
	public void calculateAccuracy(Keystroke stroke) {
		checkAcc((Stroke) stroke,true);
	}

	public float getLastTiming() {
		return lastTiming;
	}

	@Override
	public void calculateFirstAccuracy(Holdstroke stroke) {
		checkAcc((Stroke) stroke,true);
		
	}

	@Override
	public void calculateEndAccuracy(Holdstroke stroke) {
		checkAcc((Stroke) stroke,false);
		
	}
	public void checkAcc(Stroke stroke, boolean start) {
		if(start) {
			if(Math.abs(GameScreen.game.timePass()-stroke.getFirstClickTime())<16) {
				changeImg("resources/perfect.png");
				update();
				GameScreen.game.calcAcc(1);
				GameScreen.game.calcScore(1);
				GameScreen.game.calcCombo(false);
				//GameScreen.game.getTimings().add(new Timing2(1,1,100,100,"resources/perfect.png"));
				return ;
			}
			if(Math.abs(GameScreen.game.timePass()-stroke.getFirstClickTime())<40) {
				changeImg("resources/great.png");
				update();
				GameScreen.game.calcAcc(.95);
				GameScreen.game.calcScore(.95);
				GameScreen.game.calcCombo(false);
				return ;
			}
			if(Math.abs(GameScreen.game.timePass()-stroke.getFirstClickTime())<73) {
				changeImg("resources/good.png");
				update();
				GameScreen.game.calcAcc(.66);
				GameScreen.game.calcScore(.66);
				GameScreen.game.calcCombo(false);
				return ;
			}
			if(Math.abs(GameScreen.game.timePass()-stroke.getFirstClickTime())<103) {
				changeImg("resources/ok.png");
				update();
				GameScreen.game.calcAcc(.5);
				GameScreen.game.calcScore(.5);
				GameScreen.game.calcCombo(false);
				return ;
			}
			if(Math.abs(GameScreen.game.timePass()-stroke.getFirstClickTime())<127) {
				changeImg("resources/bad.png");
				update();
				GameScreen.game.calcAcc(.33);
				GameScreen.game.calcScore(.33);
				GameScreen.game.calcCombo(false);
				return ;
			}
			if(Math.abs(GameScreen.game.timePass()-stroke.getFirstClickTime())<164) {
				changeImg("resources/miss.png");
				update();
				GameScreen.game.calcAcc(0);
				GameScreen.game.calcScore(0);
				GameScreen.game.calcCombo(true);
				return ;
			}
		}else {
			if(Math.abs(GameScreen.game.timePass()-stroke.getEndClickTime())<16) {
				changeImg("resources/perfect.png");
				update();
				GameScreen.game.calcAcc(1);
				GameScreen.game.calcScore(1);
				GameScreen.game.calcCombo(false);
				return ;
			}
			if(Math.abs(GameScreen.game.timePass()-stroke.getEndClickTime())<40) {
				changeImg("resources/great.png");
				update();
				GameScreen.game.calcAcc(.95);
				GameScreen.game.calcScore(.95);
				GameScreen.game.calcCombo(false);
				return ;
			}
			if(Math.abs(GameScreen.game.timePass()-stroke.getEndClickTime())<73) {
				changeImg("resources/good.png");
				update();
				GameScreen.game.calcAcc(.66);
				GameScreen.game.calcScore(.66);
				GameScreen.game.calcCombo(false);
				return ;
			}
			if(Math.abs(GameScreen.game.timePass()-stroke.getEndClickTime())<103) {
				changeImg("resources/ok.png");
				update();
				GameScreen.game.calcAcc(.5);
				GameScreen.game.calcScore(.5);
				GameScreen.game.calcCombo(false);
				return ;
			}
			if(Math.abs(GameScreen.game.timePass()-stroke.getEndClickTime())<127) {
				changeImg("resources/bad.png");
				update();
				GameScreen.game.calcAcc(.33);
				GameScreen.game.calcScore(.33);
				GameScreen.game.calcCombo(false);
				return ;
			}
			if(Math.abs(GameScreen.game.timePass()-stroke.getEndClickTime())<164) {
				changeImg("resources/miss.png");
				update();
				GameScreen.game.calcAcc(0);
				GameScreen.game.calcScore(0);
				GameScreen.game.calcCombo(true);
				return ;
			}
		}
	}
	
	@Override
	public void missAccuracy() {
		changeImg("resources/miss.png");
		update();
		GameScreen.game.calcAcc(0);
		GameScreen.game.calcCombo(true);
	}

}
