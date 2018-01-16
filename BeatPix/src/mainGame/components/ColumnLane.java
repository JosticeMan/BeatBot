package mainGame.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import gui.components.Component;

/*
 * This class will be mainly coded by Justin Yau
 * PERIOD 4 & 5 - AP Computer Science Java
 */

/**
 * Visual Lane Component <br>
 * It will be a lane where keystrokes will appear and go down through.
 * There will be a keystroke indicator at the end.
 * @author Justin Yau
 *
 */
public class ColumnLane extends Component {

	/*
	 * PLANNING:
	 * This component will be a visual component
	 * It will include a lane
	 * Based on the input the lane will have a keystroke indicator at the end
	 */

	/**
	 * This constructor is to make a custom lane according to the dimensions
	 * 
	 * @param x - X coordinate of the lane
	 * @param y - Y coordinate of the lane
	 * @param w - Width of the column lane
	 * @param h - Height of the column lane
	 */
	public ColumnLane(int x, int y, int w, int h) {
		super(x, y, w, h);
		update();
	}

	/**
	 * Creates a gray lane with 2 red lines across the sides to indicate the border
	 * 
	 * @author Justin Yau
	 */
	@Override
	public void update(Graphics2D g) {
		super.clear();
		Rectangle rect = new Rectangle(0,0,getWidth(),getHeight());
		g.setColor(Color.LIGHT_GRAY);
		g.draw(rect);
		g.fill(rect);
		g.setColor(Color.RED);
		g.drawLine(0, 0, 0, getHeight());
		g.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
	}

}