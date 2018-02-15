package mainGame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import gui.GUIApplication;
import gui.userInterfaces.*;
import highscore.TempSongSelect;
import mainGame.components.Song;
import mainGame.components.SongBundle;
import screens.InformationContainer;

import screens.MainMenuScreenG;
import screens.OptionsContainer;
import screens.StartScreenG;
import shop.CharacterSelectionScreen;
import shop.ShopScreen;
import screens.LevelSelectG;
import screens.interfaces.Options;

public class MainGUI extends GUIApplication {

	/**
	 * 
	 */
	public static final int screenWidth = 960;
	public static final int screenHeight = 540;
	
	private static final long serialVersionUID = 6557376208612089301L;
	
	private Screen currentScreen; 
	
	public static MainGUI test;
	
	public ArrayList<SongBundle> songs;
	
	public static StartScreenG start;
	public static MainMenuScreenG mainMenu;
	
	public static ShopScreen shop;
	public static LevelSelectG level;
	public static LevelSelectG select;
	public static OptionsContainer options;
	public static InformationContainer information;
	public static CharacterSelectionScreen character;
	
	public int x;
	
	private static String[] keys;
	private static int volume;
	
	public MainGUI(int width, int height) {
		super(width, height);
		setVisible(true);
		
		songs=new ArrayList<SongBundle>();
		
		Timer time = new Timer(); x = 0;
		time.scheduleAtFixedRate(new TimerTask() {
			
			private Screen previousScreen = currentScreen;

			@Override
			public void run() {
				//System.out.println(x+"s");
				x++;
				
				//Need to connect this with setScreen so the options is connected to the right screen
				if(currentScreen instanceof Options && currentScreen != previousScreen) {
					options = new OptionsContainer(getWidth(), getHeight(), (Options) currentScreen);
				}else {
					currentScreen = previousScreen;
				}
			}
		}, 0, 1);
	}

	@Override
	public void initScreen() {
		
		String[] temp = {"D","F","J","K"};
		keys = temp;
		
		setVolume(2);
		
		start = new StartScreenG(getWidth(),getHeight());
		mainMenu = new MainMenuScreenG(getWidth(),getHeight());

		//level = new LevelSelectG(getWidth(),getHeight());
		character = new CharacterSelectionScreen(getWidth(),getHeight());
		
		options = new OptionsContainer(getWidth(), getHeight(), (Options) mainMenu);
		information = new InformationContainer((Options) mainMenu);
		
		setScreen(start);
		start.start();
	}

	public static void main(String[] args) {
		test = new MainGUI(960,540);
		test.setTitle("BeatBot");
		test.addMaps();
		Thread s = new Thread(test);
		s.run();
	}

	public void setFrameTitle(String s) {
		setTitle(s);
	}
	
	public static String[] getKeys() { 
		return keys;
	}
	
	public static String getKeys(int idx) {
		return keys[idx];
	}
	
	public static void setKeys(int idx, String s) {
		MainGUI.keys[idx] = s;
	}
	
	public static int getVolume() {
		return volume;
	}

	public static void setVolume(int volume) {
		MainGUI.volume = volume;
	}
	
	public void setScreen(Screen s) {
		super.setScreen(s);
		if(s instanceof MainMenuScreenG) {
			try {
				MainGUI.start.getMs().getMenuSound().resumeAudio();
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * DO NOT REMOVE!
	 * Adds maps to mySongs
	 * 
	 * @author Steven 
	 * @author Justin Yau
	 */
	public void addMaps() {
		File[] f = new File("resources/maps").listFiles();
		for(int i=0;i< f.length;i++) {
			songs.add(new SongBundle(f[i].getName(), f[i].getPath()));
		}
		shop = new ShopScreen(getWidth(),getHeight());
		try {
			select = new LevelSelectG(getWidth(), getHeight());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//testing
}
