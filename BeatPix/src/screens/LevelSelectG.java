package screens;

import java.awt.Color;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import gui.components.Action;
import gui.components.Button;
import gui.components.Graphic;
import gui.interfaces.Visible;
import gui.userInterfaces.FullFunctionScreen;
import mainGame.MainGUI;
import mainGame.components.CustomText;
import mainGame.components.Song;
import mainGame.components.SongBundle;
import mainGame.components.interfaces.SongInterface;
import mainGame.screens.GameScreen;
import screens.components.ImageButton;
import screens.interfaces.LevelSelectInterface;


/**
 * 
 * @see highscore.TempSongSelect
 */
public class LevelSelectG extends FullFunctionScreen implements LevelSelectInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5861604689835346010L;
	//Justin's Quick Implementation of the level select was used here to retrieve songs
	
	//Justin Yau
	private Button back; //The back button will be stored here
	private HashMap<String, Song> currentSongs; //The list of all the displayed songs will be stored here
	private Graphic background; //The graphic for the background will be stored here
	//Justin Yau
	private ImageButton displaybutton;
	private CustomText displaysong;
	private ArrayList<Button> buttons;
	private ArrayList<Button> unseenbuttons;
	private ImageButton up;
	
	
	/**
	 * Constructor creates a new screen which will load all the unlocked songs currently on the screen
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public LevelSelectG(int width, int height) throws IOException {
		super(width, height);
	}

	@Override
	public void initAllObjects(List<Visible> viewObjects) {
		
		background = updateBackground("resources\\mop.png");
		viewObjects.add(background);
		
		unseenbuttons= new ArrayList<Button>();
		ImageIcon icon = new ImageIcon("resources\\tester.jpg");
		buttons = new ArrayList<Button>();

		//background = new Graphic(0,0, getWidth(), getHeight(), "resources/mop.png");
		//viewObjects.add(background);
		
		currentSongs = new HashMap<String, Song>();
		Button temp;
		back = new Button(getWidth() - 100, 0, 100, 80, "Back", new Action() {
			
			@Override
			public void act() {
				MainGUI.test.setScreen(MainGUI.mainMenu);
			}
		});
		back.setForeground(Color.white);
		viewObjects.add(back);
		
		int count = 0;
		for(int i=0;i<MainGUI.test.songs.size();i++) {
			int tempint=i;
			SongBundle songBundle = MainGUI.test.songs.get(i);
			if((MainGUI.test.songs.get(tempint).isUnlock())) {
				File[] songList = new File(songBundle.getPath()).listFiles(new FileFilter() {
		            @Override
		            public boolean accept(File pathname) {
		                return pathname.getName().toLowerCase().endsWith(".csv") 
		                    || pathname.isDirectory();
		            }
		        });
				for(int j = 0; j < songList.length; j++) {
					Song song = new Song(songList[j].getPath());
					displaybutton = new ImageButton(250,100*count+80,350,200,"resources\\ui\\buttons\\buttonwithrivet.png");
					displaysong = new CustomText(300,100*count+100,songList[j].getName().length()*8,songList[j].getName().length()*10,""+songList[j].getName(),false); 
					temp=new Button(250,100*count,350,200, songList[j].getName(), new Action() {
						@Override
						public void act() {
							MainGUI.test.setScreen(new GameScreen(getWidth(),getHeight(),song,"resources/sample_bg.gif"));
							
						}
					});
					temp.setForeground(Color.WHITE);
					buttons.add(temp);
				 
					currentSongs.put(songList[j].getPath(), song);
					
					viewObjects.add(temp);
					viewObjects.add(displaybutton);
					viewObjects.add(displaysong);
					count++;
					
				}
			}
		}
		
		up = new ImageButton(150,255,125,50, "resources\\\\up.png");
		  viewObjects.add(up);
		up.setEnabled(true);
		up.setAction(new Action() {
			public void act(){				
			
				
				// System.out.println(buttons.get(0).getText());
				unseenbuttons.add(buttons.get(0));
				unseenbuttons.get(0).setY(buttons.get(0).getY());
				buttons.remove(0);
				buttons.add(unseenbuttons.get(0));
				
				int tempI = buttons.get(3).getY();
				buttons.get(3).setY(buttons.get(2).getY());
				buttons.get(2).setY(buttons.get(1).getY());
				buttons.get(1).setY(buttons.get(0).getY());
				buttons.get(0).setY(tempI);
					
				unseenbuttons.remove(0);
				
				
				updateDisplay();
			}			
	});

		
	}
	


	/**
	 * This method searches through the hashmap and returns whether or not the song's title was found among the entries
	 * @param song - The song to search for
	 * @return - Whether or not the song's title was found among the entries
	 * 
	 * @author Justin Yau
	 */
	public boolean isFound(String songPath) {
		for(String s: currentSongs.keySet()) {
			if(s.equalsIgnoreCase(songPath)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method searches through all the songs and displays the unlocked ones
	 * 
	 * 
	 */
	public void spawnButtons() {
		addObject(background);
		addObject(back);
		Button temp;
		int count = 0;
		for(int i=0;i<MainGUI.test.songs.size();i++) {
			int tempint=i;
			SongBundle songBundle = MainGUI.test.songs.get(i);
			if((MainGUI.test.songs.get(tempint).isUnlock())) {
				File[] songList = new File(songBundle.getPath()).listFiles(new FileFilter() {
		            @Override
		            public boolean accept(File pathname) {
		                return pathname.getName().toLowerCase().endsWith(".csv") 
		                    || pathname.isDirectory();
		            }
		        });
				for(int j = 0; j < songList.length; j++) {
					Song song = new Song(songList[j].getPath());
					if(!isFound(songList[j].getPath())) {
						temp=new Button(250,100*count,350,200, songList[j].getName(), new Action() {
							@Override
							public void act() {
								MainGUI.test.setScreen(new GameScreen(getWidth(),getHeight(),song,"resources/sample_bg.gif"));
								
							}
						});
						//displaybutton = new ImageButton(250,100*count+80,350,200,"resources\\ui\\buttons\\buttonwithrivet.png");
						//displaysong = new CustomText(300,100*count+100,songList[j].getName().length()*8,songList[j].getName().length()*10,""+songList[j].getName(),false); 
						//addObject(displaybutton);
						//addObject(displaysong);
						currentSongs.put(songList[j].getPath(), song);
					}
					else {
						int t = j;
						
						temp=new Button(250,100*count,350,200, songList[j].getName(), new Action() {
							@Override
							public void act() {
								MainGUI.test.setScreen(new GameScreen(getWidth(),getHeight(),currentSongs.get(songList[t].getPath()),"resources/sample_bg.gif"));
								
							}
						});
						//displaybutton = new ImageButton(250,100*count+80,350,200,"resources\\ui\\buttons\\buttonwithrivet.png");
						//displaysong = new CustomText(300,100*count+100,songList[t].getName().length()*8,songList[t].getName().length()*10,""+songList[t].getName(),false); 
					}
					
					temp.setForeground(Color.WHITE);
					count++;
					unseenbuttons.add(temp);
					
					
					//addObject(temp);
					//addObject(displaybutton);
					//addObject(displaysong);
				}
			}
		}
	}
	
	
	public void updateList() {
		getObjects().clear();
		removeAll();
		spawnButtons();
		 addObject(up);
		 
		 for(int i =0; i<buttons.size();i++) {
			 addObject(buttons.get(i));
			 flairButton(buttons.get(i));
		 }
	}
	public void updateDisplay() {
		getObjects().clear();
		removeAll();
		addObject(background);
		addObject(back);
		 addObject(up);
		 
		 for(int i =0; i<buttons.size();i++) {
			 addObject(buttons.get(i));
			 flairButton(buttons.get(i));
		 }
	}
	
	public void flairButton(Button button) {
		displaybutton = new ImageButton(button.getX(),button.getY()+80,350,200,"resources\\ui\\buttons\\buttonwithrivet.png");
		displaysong = new CustomText(button.getX()+50,button.getY()+100,button.getText().length()*8,button.getText().length()*10,""+button.getText(),false); 
		addObject(displaybutton);
		addObject(displaysong);
	}
	
	private Graphic updateBackground(String path) {
		ImageIcon icon = new ImageIcon(path);
		int w; int h; // 0 for either will us e original image size/width 
		int x = 0; int y = 0;
		if(background != null) {
			x = background.getX(); y = background.getY();
		}
		w = getWidth();
		//GUIApp scales the height last
/*needs fixing*/h = (int) ((getWidth()/icon.getIconWidth())*icon.getIconHeight()+100); //makes the width of background always match the screen
		return new Graphic(x,y,w,h,path);
	}
	
}