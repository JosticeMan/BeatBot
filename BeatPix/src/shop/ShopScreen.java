package shop;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import gui.GUIApplication;
import gui.components.*;
import gui.interfaces.FocusController;
import gui.interfaces.Visible;
import gui.userInterfaces.FullFunctionScreen;
import mainGame.MainGUI;
import mainGame.components.CustomText;
import mainGame.components.Song;
import mainGame.screens.ImportScreen;
import screens.Test;

public class ShopScreen extends FullFunctionScreen implements CreditChanger
{
	//Daniel Fields~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static final long serialVersionUID = 1L;
	
	private Graphic background;	
	
	private ScrollablePane scroll;
	
	private CustomText songBanner;
	private CustomText credit;
	private CustomText textLine1;
	private CustomText textLine2;
	private CustomText noText;
	private CustomText yesText;
	private CustomText purchasedTextLine1;
	private CustomText purchasedTextLine2;
	private CustomText importText;
	private CustomText text;
	
	private static ArrayList<Song> songs;
	private ArrayList<ImageButton> buttons;
	private ArrayList<CustomText> customText;
	private ArrayList<MultiLineCustomText> multiText;

	private ImageButton yes;
	private ImageButton no;
	private ImageButton clickedButton;
	private ImageButton importButton;
	
	private int credits;
	private int price;
	private int idx;
	
	
	
	private Graphic creditBorder;
	private Graphic scrollBorder;
	private Graphic textBorder;
	
	private String[] texts; 

	
	//Kevin Fields-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	private ArrayList<ImageButton> buttonList;
	private Button confirmButton;
	private ArrayList<Button> yesButton;
	private ArrayList<Integer> indexList;
	private String[] imageNames;
	private ArrayList<ImageButton> images;
	
	private Graphic border; //for the text
	private Graphic border2; //for the unlock
	
	private ScrollablePane charScroll;
	private Button noButton;
	private int numChars;
	private int index;
	
	private CustomText back;
	
	//the yesk, nok, and okayk are the custom text
	private CustomText yesK;
	private CustomText noK;
	private CustomText okayK;
	private CustomText textKev;
	private CustomText unlockedText;
	private CustomText notEnough;
	private CustomText pesos;
	
	private Graphic backBorder;
	private Graphic textBorderK;
	

	public ShopScreen(int width, int height) 
	{
		super(width, height);

	}
	
	
	
	
	@Override
	public void initAllObjects(List<Visible> viewObjects) 
	{
	
		//sets starting amount of credits
		changeCredits(getCredits()+9000);
		credits = getCredits();
		//Daniel components~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		
		buttons = new ArrayList<ImageButton>();
		customText = new ArrayList<CustomText>();
		multiText = new ArrayList<MultiLineCustomText>();
		songs = new ArrayList<Song>();
		
		songs.add(new Song("Dreadnought Mastermind(xi+nora2r)-NM"));

		//graphics
			
 		background = new Graphic(0,0,getWidth(),getHeight(),"resources//backgrounds//shop_bg1.png");
 		viewObjects.add(background);
		
 		creditBorder = new Graphic(90,40,260,195,"resources//TransparentButtonA.png");
		viewObjects.add(creditBorder);
		
		
		
		// text for credits
		
		songBanner = new CustomText(123,135,200,150,"Songs",true);
		viewObjects.add(songBanner);
		
		String s = "Credits:"+getCredits(); 
		credit = new CustomText(110,58,200,200, s,true);
		credit.update();
		viewObjects.add(credit);
		
		
		
		textLine1 = new CustomText(370,195,250,280, "Do you want to",true); 			
		textLine2 = new CustomText(370,220,250,280,"buy this song?",true);
		
		purchasedTextLine1 = new CustomText(417,200,150,120, "Song",true);
		purchasedTextLine2 = new CustomText(355,245,276,276, "Purchased",true);
		
		//scroll bar, contains the songs
		scroll = new ScrollablePane(this, 110,195,220,260);		
		scroll.setBackground(new Color(0,0,0));
		scroll.update();
		textBorder = new Graphic(340,160,305,405,"resources//shop//TransparentButtonB.png");	
		viewObjects.add(textBorder);
		//when user clicks yes to buy song
		yes = new ImageButton(360,255,125,50, "resources\\ui\\buttons\\buttonwithrivet.png","");
		yesText = new CustomText(390,258,60,60, "yes",false); 	
		
		yes.setAction(new Action() 
		{			
			@Override
			public void act() 
			{
				int x = (getCredits() - 1500);
				if (x >= 0)
				{	
					new Thread()
					{

						public void run()
						{
							try
							{
								//purchase of song
								credits -= 1500;
								credit.setText("Credits: "+ credits);
								credit.update();
								
								//set components invisible after pressing button
								setInvis(false);
								
								
								viewObjects.add(purchasedTextLine1);
								viewObjects.add(purchasedTextLine2);
								
								//after a second, the text and border will disappear
								Thread.sleep(1000);
								textBorder.setVisible(false);
								purchasedTextLine1.setVisible(false);
								purchasedTextLine2.setVisible(false);
								
								//removes the song selected
								removeButton();
								
								scroll.update();	
								
									
							}
							catch (InterruptedException e)
							{
								
							}
							
						}

					} .start();
				}
				else
				{
					new Thread()
					{
						public void run()
						{
							try
							{
								
								CustomText warningLine1 = new CustomText(363,200,260,260, "Not Enough",true);
								CustomText warningLine2 = new CustomText(372,240,246,246, "Credits",true);
								
								viewObjects.add(warningLine1);
								viewObjects.add(warningLine2);
								
								setInvis(false);
								
								//remove text after a second
								Thread.sleep(1000);
								
								textBorder.setVisible(false);
								warningLine1.setVisible(false);		
								warningLine2.setVisible(false);
								
								
							}
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							
						}
					} .start();
				}
			}
		});
		
		//enables buttons, adds text, and removes visibility until user clicks a button to activate them
		yes.setEnabled(true);
		viewObjects.add(yes);
		viewObjects.add(yesText);
		yes.setVisible(false);		
		yesText.setVisible(false);
		
		viewObjects.add(textLine1);
		viewObjects.add(textLine2);
		
		textBorder.setVisible(false);
		textLine1.setVisible(false);
		textLine2.setVisible(false);
		
		// creation of no button
		no = new ImageButton(500,255,125,50, "resources\\ui\\buttons\\buttonwithrivet.png","");
		noText = new CustomText(540,258,50,40, "no",false);
		no.setAction(new Action()
		{
			@Override
			public void act() 
			{
				//sets popup invisible after pressing no
				setInvis(false);
				textBorder.setVisible(false); //
			}
		});
		
		//enables buttons, adds text, and removes visibility until user clicks a button to activate them
		
		no.setEnabled(true);
		viewObjects.add(no);
		viewObjects.add(noText);
		no.setVisible(false);
		noText.setVisible(false);
			
		addButtons();
		
		
			
		scroll.update();
		viewObjects.add(scroll);
		
		
		//button for import song option
		
		importButton = new ImageButton(360, 430, 260,195,"resources//TransparentButtonA.png","");
		importButton.setAction(new Action() {
			
			@Override
			public void act() {
				MainGUI.test.setScreen(new ImportScreen(getWidth(), getHeight()));
				
			}
		});
		importButton.setEnabled(true);
		viewObjects.add(importButton);
		
		importText = new CustomText(370, 445, 243, 243, "Import Song", true);
		viewObjects.add(importText);
		
//		scrollBorder = new Graphic(90,175,350,325,"resources//shop//border.png");
//		viewObjects.add(scrollBorder);
		
		//Kevin Components------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		
		//
		buttonList = new ArrayList<ImageButton>();
		yesButton = new ArrayList<Button>();
		indexList = new ArrayList<Integer>();
		images = new ArrayList<ImageButton>();
		
		back = new CustomText(750, 60, 95, 50, "Back", true);
		okayK = new CustomText(463, 277, 50, 30,"Ok",true);
		yesK = new CustomText(405,405,50,75,"Yes", true);
		noK = new CustomText(507,405,45,50,"No", false);
		notEnough = new CustomText(363,190,260,260, "Not Enough",true);
		pesos  = new CustomText(372,230,246,246, "Credits",false);
		
		imageNames = new String[] {"resources/sprites/redGuy.png", "resources/sprites/greenGuy.png", "resources/sprites/whiteGuy.png"};
		for(int i = 0; i < imageNames.length; i ++) {
			images.add(new ImageButton(390, 180, 190, 300, imageNames[i],""));
		}
		backBorder = new Graphic(700,45,200,200,"resources//TransparentButtonA.png");
		//the ten should be number chars that the player should unlock
		numChars = 3;
		//create the back button
		createIntList(numChars);
		Button backButton = new Button(705, 50, 190, 30, null, Color.GRAY, new Action() {
			
			@Override
			public void act() {
				
				MainGUI.test.setScreen(MainGUI.mainMenu);
				setThings1Vis(false);
				setAllYesButVisFalse();
				setThings2Vis(false);
				setConfirmVis(false);
				setThings3Vis(false);
				enableButton(true);
			
			}
		});
		
		//create the panel 
		charScroll = new ScrollablePane(this, 650, 195, 220, 260);	
		charScroll.setBackground(new Color(0,0,0));
		charScroll.setBorderWidth(3);
		
		//create the list of Buttons and add it to panel
		for(int i=0; i < numChars; i++){ 
			//got the index number
			final int x = i;
			/*buttonList.add(new ImageButton(5,30*i,100,25,"Skin "+ (i + 1), new Action() {
				int j = x;
				@Override
				public void act() {
					setThings1Vis(true);
					setYesButVisExceptThis(j);
					index = indexList.indexOf(j);
					enableButton(false);
				}
			}));*/
			ImageButton b = new ImageButton(0,(i*52)+5,220,70,"resources\\ui\\buttons\\buttonwithrivet.png","");
			b.setAction(new Action() 
			{
				
				@Override
				public void act() 
				{
					int j = x;
					setThings1Vis(true);
					setYesButVisExceptThis(j);
					index = indexList.indexOf(j);
					enableButton(false);
				}
			});
			
			b.setEnabled(true);
			buttonList.add(b);
			charScroll.addObject(buttonList.get(i));
		}
		
		//create arrayList of YesButtons
		for(int k = 0; k < numChars; k++) {
			final int z = k;
			yesButton.add(new Button(400, 400, 55, 35, "", Color.GRAY, new Action() {
				int j = z;
				@Override
				public void act() {
					// i guess to have the images, and set the a boolean to true and a textfield says unlock
					if(credits - 1000 > 0 ) {
						for (int i = index; i < buttonList.size(); i++){
							if (buttonList.get(i).getY() != 0){
								buttonList.get(i).move(buttonList.get(i).getX(), (buttonList.get(i).getY()-50), 10);
								charScroll.update();
							}

						}
						setThings2Vis(true);
						setThings1Vis(false);
						setAllYesButVisFalse();
						setConfirmVis(true);
						charScroll.remove(buttonList.get(j));
						charScroll.update();
						indexList.remove(index);
						MainGUI.character.unlock(j);
						credits = credits - 1000;
						credit.setText("Credits: "+ credits);
						credit.update();						
					}
					else {
						setThings1Vis(false);
						setAllYesButVisFalse();
						setThings3Vis(true);
						setConfirmVis(true);
						
					}
					 
						
				}
			}));
		}
		
		 //create all the things 
		 border2 = new Graphic(337, 180, 305,305,"resources//shop//TransparentButtonB.png");
		 unlockedText = new CustomText(390, 210, 200, 370, "UnLocked",false);
		 textBorderK =  new Graphic(340,150,305,435,"resources//shop//TransparentButtonB.png");
		 
		 border = new Graphic(380, 80, 230, 430, "resources//TransparentButtonC.png");
		 textKev = new CustomText(390, 110, 200, 370, "Unlock" ,true);

		 noButton = new Button(500, 400, 55, 35, "", Color.GRAY, new Action() {
			
			@Override
			public void act() {
				 setThings1Vis(false);
				 setAllYesButVisFalse();
				 enableButton(true);
			}
		});
		confirmButton = new Button(460, 270, 50, 30, "", Color.GRAY, new Action() {
			
			@Override
			public void act() {
				setThings2Vis(false);
				setConfirmVis(false);
				enableButton(true);
				setThings3Vis(false);
			}
		});
		 
		 //visible all the things
		 setThings1Vis(false);
		 setAllYesButVisFalse();
		 setThings2Vis(false);
		 setConfirmVis(false);
		 setThings3Vis(false);
		 
		
		//add the objects
		viewObjects.add(backButton);
		viewObjects.add(backBorder);
		viewObjects.add(border);
		viewObjects.add(border2);
		viewObjects.add(textBorderK);
		viewObjects.add(textKev);
		charScroll.update();
		viewObjects.add(charScroll);
		viewObjects.add(noButton);
		for(int a = 0; a < numChars; a++) {
			viewObjects.add(images.get(a));
			viewObjects.add(yesButton.get(a));
		}
		viewObjects.add(unlockedText);
		viewObjects.add(back);
		viewObjects.add(yesK);
		viewObjects.add(noK);
		viewObjects.add(notEnough);
		viewObjects.add(pesos);
		viewObjects.add(confirmButton);
		viewObjects.add(okayK);
		
	}	
	//daniel methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void removeButton()
	{
		//sets index
		idx = buttons.indexOf(clickedButton);

		// unlocks the song if the title corresponds
		for (int i = 0; i<MainGUI.test.songs.size(); i++)
		{
			// if the button's song title is equal to the song array's title then unlock it and update the list
			if (buttons.get(idx).getSong().toLowerCase().equals(MainGUI.test.songs.get(i).getTitle().toLowerCase()))
			{
					MainGUI.test.songs.get(i).setUnlock(true);
					MainGUI.select.updateList();
			}
		}

		//remove button+text from arraylist and scrollpane 
		scroll.remove(clickedButton);				
		buttons.remove(clickedButton);
		scroll.remove(customText.get(idx));
		customText.remove(idx);
		scroll.update();
		
		//moves the buttons up the scrollpane after removal
		for (int i = idx; i < buttons.size(); i++)
		{
			
			if (buttons.get(i).getY() != 0)
			{
				buttons.get(i).move(buttons.get(i).getX(), (buttons.get(i).getY()-50), 10);
				scroll.update();
				customText.get(i).move(customText.get(i).getX(), (customText.get(i).getY()-50), 10);
				scroll.update();
			}
			scroll.update();
		}
		
		
	}
	public void addButtons()
	{
		//creates arraylist for titles of songs
		ArrayList<String> texts = new ArrayList<String>();
		//adds titles into arraylist
		for(int i = 0; i < MainGUI.test.songs.size(); i++)
		{
			//if it's not unlocked already then add title
			if(!(MainGUI.test.songs.get(i).isUnlock())) 
			{
				texts.add(MainGUI.test.songs.get(i).getTitle());
			}
		}
		
		//creates buttons
		for(int i = 0; i < texts.size(); i++)
		{ 
				ImageButton b = new ImageButton(0,(i*52)+5,220,70,"resources\\ui\\buttons\\buttonwithrivet.png",texts.get(i));
				b.setAction(new Action() 
				{
					
					@Override
					public void act() 
					{
						clickedButton = b;
						setInvis(true);	
					}
				});
				
				b.setEnabled(true);
				buttons.add(b);
				
				//depending on the length of the text, change its width and height
				if (texts.get(i).length() < 8)
				{
					customText.add(new CustomText(-25 + getWidth()*55/960, (i*52) + getHeight()*17/540, 160, 157, texts.get(i),false));
				}
				else
				{
					if (texts.get(i).length() < 13)
					{
						customText.add(new CustomText(-25 + getWidth()*55/960, (i*52) + getHeight()*17/540, 150, 150, texts.get(i),false));
					}
					else
					{
						customText.add(new CustomText(-25 + getWidth()*55/960, (i*52) + getHeight()*17/540, 190,190, texts.get(i),false));
					}
				}
			
				buttons.get(i).setUnhoverAction(new Action()
				{
					public void act() 
					{
						GUIApplication.mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				});
				
				buttons.get(i).setHoverAction(new Action()
				{
					public void act() 
					{
						GUIApplication.mainFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}
				});
		}
		
		for (int i = 0; i < buttons.size(); i++)
		{
			scroll.addObject(buttons.get(i));
			scroll.addObject(customText.get(i));
		//	multiText.get(i).addToScreen();
		}
	}




	public void setInvis(boolean b)
	{			
		if (b)
		{
			textBorder.setVisible(true);
			noText.setVisible(true);
			yesText.setVisible(true);
			textLine1.setVisible(true);
			textLine2.setVisible(true);
			yes.setVisible(true);				
			no.setVisible(true);
		}
		else
		{
			noText.setVisible(false);
			yesText.setVisible(false);
			textLine1.setVisible(false);
			textLine2.setVisible(false);
			yes.setVisible(false);				
			no.setVisible(false);
		}
	}
	
	
	
	
	
	
	// kevin methods----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void createIntList(int a) {
		for(int i = 0; i < a; i++) {
			indexList.add(i);
		}
	}
	//helper methods

	//things that are not buttons doesnt not consist of the unlock and confirm button and soon images, visibility = b
	public void setThings1Vis(boolean b) {
		 border.setVisible(b);
		 textKev.setVisible(b);
		 noButton.setVisible(b);
		 yesK.setVisible(b);
		 noK.setVisible(b);
	}
	//things that things1 doesnt consist of, visibility = b
	public void setThings2Vis(boolean b) {
		 border2.setVisible(b);
		 unlockedText.setVisible(b);
	}
	//this is for the not enough pesos
	public void setThings3Vis(boolean b) {
		textBorderK.setVisible(b);
		notEnough.setVisible(b);
		pesos.setVisible(b);
	}
	
	 //turn all yesButtons with the buttons, visibility = false;
	public void setAllYesButVisFalse() {
		for(int i = 0; i < yesButton.size(); i ++) {
			yesButton.get(i).setVisible(false);
			images.get(i).setVisible(false);
		}
	}
	//turn all  yes button visibility that are false except for the  chosen one
	public void setYesButVisExceptThis(int i) {
		for(int x = 0; x < yesButton.size(); x++) {
			if(x != i) {
				yesButton.get(x).setVisible(false);
				images.get(x).setVisible(false);
			}
			yesButton.get(i).setVisible(true);
			images.get(i).setVisible(true);
		}
	}
	
	 //turn all confirmButtons with the images, visibility = false;
	public void setConfirmVis(boolean b) {
		confirmButton.setVisible(b);
		okayK.setVisible(b);
	}
		
	public void enableButton(boolean b) {
		for(int i = 0; i < buttons.size(); i ++) {
			buttons.get(i).setEnabled(b);
		}
		for(int z = 0; z < buttonList.size(); z++) {
			buttonList.get(z).setEnabled(b);
		}
		importButton.setEnabled(b);
	}
	
	
	//interface methods
	public ArrayList<Song> getSongs()
	{
		return songs;
	}
	
	public int getCredits()
	{
		return credits;
	}
	
	public void changeCredits(int c)
	{
		credits = c;
	}

	public void updateCredits()
	{
		credit.setText("Credits: " + credits);
		credit.update();
		update();
	}
}
