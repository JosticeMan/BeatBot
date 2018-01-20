package StevenTestingHolds;

import gui.GUIApplication;
import mainGame.MainGUI;

public class Main extends GUIApplication {

	public static void main(String[] args) {
		MainGUI s = new MainGUI(960,540);
		Thread runner = new Thread(s);
		runner.start();
	}
	
	public Main(int width, int height) {
		super(width, height);
		setVisible(true);
	}

	@Override
	public void initScreen() {
		Screen1 s = new Screen1(getWidth(), getHeight());
		setScreen(s);
	}

}
