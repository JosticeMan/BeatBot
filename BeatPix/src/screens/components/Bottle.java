package screens.components;

import java.util.ArrayList;

public class Bottle extends ImageButton {

	ArrayList<ArrayList<Integer>> sequence;
	
	public static int BOTTLE_UP = 1;
	public static int BOTTLE_DOWN = 2;
	public static int BOTTLE_LEFT = 3;
	public static int BOTTLE_RIGHT = 4;
	
	private int currentSequence;
	
	private Bottle me;
	private int finalY;
	private int finalX;
	
	public Bottle(int x, int y, int w, int h, String imageLocation) {
		super(x, y, w, h, imageLocation);
		currentSequence = 0;
		sequence = new ArrayList<ArrayList<Integer>>();
	}
	
	public void addSequence(ArrayList<Integer> seqeunceNumber,int speed) {
		seqeunceNumber.add(speed);
		sequence.add(seqeunceNumber);
	}
	
	public boolean nextSequence() {
		if(currentSequence > sequence.size()-1) {
			return false;
		}
		playSequence();
		currentSequence++;
		return true;
	}
	
	public void setFinalY(int x) {
		finalY = x;
	}
	public int getFinalY() {
		return finalY;
	}
	
	public void playSequence() {
		for(int j = 0; j < sequence.get(currentSequence).size()-1; j++) {
			int i = sequence.get(currentSequence).get(j);
			int speed = sequence.get(currentSequence).get(sequence.get(currentSequence).size()-1);
			if(i == BOTTLE_UP) {
				this.setY(this.getY() - speed);
			}
			if(i == BOTTLE_DOWN) {
				this.setY(this.getY() + speed);
			}
			if(i == BOTTLE_LEFT) {
				this.setX(this.getX() - speed);
			}
			if(i == BOTTLE_RIGHT) {
				this.setX(this.getX() + speed);
			}
		}
	}

	public int getFinalX() {
		return finalX;
	}

	public void setFinalX(int finalX) {
		this.finalX = finalX;
	}

}
