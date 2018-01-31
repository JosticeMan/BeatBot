package mainGame.components;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import gui.components.Component;

public class Scoring extends Component {
	int score;
	int offset;
	String img;
	public Scoring(int x, int y, int w, int h) {
		super(x, y, w, h);
		score = 1000;
		update();
	}

	public static void main(String[] args) {
		

	}
	public void scoring() {
		if(offset<16) {
			if(score<500000) {
				score += 10000;
			}	
					score += 15000;
		}
		if(offset<40 && offset>16) {
			score += 5000;
		}
		if(offset>40 && offset<73) {
			score += 1000;
		}
		if(offset>73 && offset<103) {
			score += 100;
		}
		if(offset>103 && offset<127) {
			score -= 200;
		}
		if(offset>127) {
			score -= 2000;
		}
			
	}


	@Override
	public void update(Graphics2D g) {
		String img1="";
		String img2="";
		String img3="";
		String img4="";
		String img5="";
		String img6="";
		String scoreStr = String.valueOf(score);

		if(scoreStr.length()==3) {
			img1="resources/score-"+scoreStr.substring(0,1)+ ".png";
			img2="resources/score-"+scoreStr.substring(1,0)+ ".png";
			img3="resources/score-"+scoreStr.substring(2)+ ".png";
			try {
				ImageIcon icon = new ImageIcon(img1);
				g.drawImage(icon.getImage(), -10, 100, null);
				icon = new ImageIcon(img2);
				g.drawImage(icon.getImage(), 44, 100, null);
				icon = new ImageIcon(img3);
				g.drawImage(icon.getImage(), 104, 100, null);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(scoreStr.length()==4) {
			img1="resources/score-"+scoreStr.substring(0,1)+ ".png";
			img2="resources/score-"+scoreStr.substring(1,0)+ ".png";
			img3="resources/score-"+scoreStr.substring(2,3)+ ".png";
			img4="resources/score-"+scoreStr.substring(3)+ ".png";
			try {
				ImageIcon icon = new ImageIcon(img1);
				g.drawImage(icon.getImage(), -10, 100, null);
				icon = new ImageIcon(img2);
				g.drawImage(icon.getImage(), 44, 100, null);
				icon = new ImageIcon(img3);
				g.drawImage(icon.getImage(), 104, 100, null);
				icon = new ImageIcon(img4);
				g.drawImage(icon.getImage(), 164, 100, null);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(scoreStr.length()==5) {
			img1="resources/score-"+scoreStr.substring(0,1)+ ".png";
			img2="resources/score-"+scoreStr.substring(1,0)+ ".png";
			img3="resources/score-"+scoreStr.substring(2,3)+ ".png";
			img4="resources/score-"+scoreStr.substring(3,4)+ ".png";
			img5="resources/score-"+scoreStr.substring(4)+ ".png";
			try {
				ImageIcon icon = new ImageIcon(img1);
				g.drawImage(icon.getImage(), -10, 100, null);
				icon = new ImageIcon(img2);
				g.drawImage(icon.getImage(), 44, 100, null);
				icon = new ImageIcon(img3);
				g.drawImage(icon.getImage(), 104, 100, null);
				icon = new ImageIcon(img4);
				g.drawImage(icon.getImage(), 164, 100, null);
				icon = new ImageIcon(img5);
				g.drawImage(icon.getImage(), 224, 100, null);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}	
		if(scoreStr.length()==6) {
			img1="resources/score-"+scoreStr.substring(0,1)+ ".png";
			img2="resources/score-"+scoreStr.substring(1,0)+ ".png";
			img3="resources/score-"+scoreStr.substring(2,3)+ ".png";
			img4="resources/score-"+scoreStr.substring(3,4)+ ".png";
			img5="resources/score-"+scoreStr.substring(4,5)+ ".png";
			img6="resources/score-"+scoreStr.substring(5)+ ".png";
			try {
				ImageIcon icon = new ImageIcon(img1);
				g.drawImage(icon.getImage(), -10, 100, null);
				icon = new ImageIcon(img2);
				g.drawImage(icon.getImage(), 44, 100, null);
				icon = new ImageIcon(img3);
				g.drawImage(icon.getImage(), 104, 100, null);
				icon = new ImageIcon(img4);
				g.drawImage(icon.getImage(), 164, 100, null);
				icon = new ImageIcon(img5);
				g.drawImage(icon.getImage(), 224, 100, null);
				icon = new ImageIcon(img6);
				g.drawImage(icon.getImage(), 284, 0, null);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			 
		}



			   
		}
		
		
		
	}
