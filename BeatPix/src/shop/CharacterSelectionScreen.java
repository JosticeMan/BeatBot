package shop;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import gui.components.*;
import gui.interfaces.Visible;
import gui.userInterfaces.FullFunctionScreen;
import mainGame.MainGUI;
import mainGame.components.CustomText;
public class CharacterSelectionScreen extends FullFunctionScreen implements unlocker{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ImageButton> imagesButton;
	private ArrayList<ImageButton> selectImage;
	private ArrayList<Button> buttons;
	
	private int numChars;
	
	private String[] imageNames;
	private Boolean[] unlock;
	private String[] colorArray;
	
	private CustomText back;
	private CustomText lockedText;
	private CustomText selectText;
	
	private String  color;
	private Graphic backGround;
	private Graphic border;
	private Button backButton;
	
	public CharacterSelectionScreen(int width, int height) {
		// TODO Auto-generated constructor stub
		super(width, height);
	}
	
	@Override
	public void initAllObjects(List<Visible> viewObjects) {
		numChars = 4;
		imagesButton = new ArrayList<ImageButton>();
		selectImage = new ArrayList<ImageButton>();
		buttons = new ArrayList<Button>();
		
		imageNames = new String[] {"resources/sprites/defaultGuy.png","resources/sprites/redGuy.png", "resources/sprites/greenGuy.png", "resources/sprites/whiteGuy.png"};
		unlock = new Boolean[] {true, false, false, false};
		colorArray = new String[] {"default", "red", "green" , "white"};
		
		color = colorArray[0];
		
		backGround = new Graphic(0, 0, getWidth(), getHeight(), "resources/charaBG.png");
		border = new Graphic(212, 180, 305,305,"resources//shop//TransparentButtonB.png");
		
		lockedText = new CustomText(265, 210, 200, 370, "Locked",false);
		selectText = new CustomText(265, 210, 200, 370, "Selected",true);
		
		for(int i = 0; i < imageNames.length; i ++) {
			final int x = i;	
			selectImage.add(new ImageButton(700, 180, 200, 300, imageNames[i], ""));
			imagesButton.add(new ImageButton((125*i)+125, 50, 100, 300, imageNames[i], ""));
			
			buttons.add(new Button((125*i)+125, 50, 100, 300, "", new Action() {
				
				@Override
				public void act() {
					int j = x;
					if(unlock[j] == true) {
						new Thread() {
							public void run() {
								try {
									notDisableBut(false);
									color = colorArray[j];
									for(int z = 0; z < numChars; z++) {
										selectImage.get(z).setVisible(false);
									}
									selectImage.get(j).setVisible(true);
									displayText(selectText, true);
									
									Thread.sleep(750);
									
									displayText(selectText, false);
									notDisableBut(true);
								}catch(InterruptedException e){
									
								}
							}
						} .start();
					}else {
						new Thread() {
							public void run() {
								try {
									notDisableBut(false);
									displayText(lockedText, true);
									
									Thread.sleep(750);
									
									displayText(lockedText, false);
									notDisableBut(true);
									
								}catch(InterruptedException e){
									
								}
							}
						}.start();
					}
				}
			}));
		}
		 back = new CustomText(805, 60, 95, 50, "Back", true);
		 backButton = new Button(800, 50, 100, 30, "", Color.GRAY, new Action() {
			@Override
			public void act() {
			MainGUI.test.setScreen(MainGUI.mainMenu);
			MainGUI.mainMenu.changeIdle();
			}
		});
		 
		//set things to visible 
		for(int z = 0; z < numChars; z++) {
			selectImage.get(z).setVisible(false);
		}
		selectImage.get(0).setVisible(true);
		backGround.setVisible(true);
		border.setVisible(false);
		lockedText.setVisible(false);
		selectText.setVisible(false);
		
		//viewobjects
		viewObjects.add(backGround);
		viewObjects.add(backButton);
		viewObjects.add(border);
		viewObjects.add(lockedText);
		viewObjects.add(selectText);
		for(int k = 0; k < numChars; k++) {
			viewObjects.add(buttons.get(k));
			viewObjects.add(imagesButton.get(k));
			viewObjects.add(selectImage.get(k));
		}
		viewObjects.add(back);
	}
	//helper methods
	public void notDisableBut(boolean b) {
		for(int i  = 0; i < numChars; i++) {
			buttons.get(i).setEnabled(b);
		}
	}
	public void displayText(CustomText a, boolean b) {
		border.setVisible(b);
		a.setVisible(b);
	}
	//interfaceMethods
	@Override
	public void unlock(int i) {
		unlock[i+1] = true;
		
	}

	@Override
	public String getSkin() {
		// TODO Auto-generated method stub
		return color;
	}

}