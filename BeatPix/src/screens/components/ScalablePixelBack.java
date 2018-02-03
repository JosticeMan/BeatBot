package screens.components;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import gui.components.Component;

public class ScalablePixelBack extends Component {

	/**
	 * Since using the defaultbutton as a background (different ratio than default)
	 * will cause stretching when scaling, the background will be recreated piece by
	 * piece in square images in order to prevent ugly scaling.
	 * 
	 * SCALABLE - means different ratios, not size scaling. but different sizes can work
	 * 
	 * @param x
	 * @param y
	 * @param w - the background will be exactly this wide in pixels
	 * @param h - will be exactly this tall in pixels
	 */
	
	private static String testFile = "resources\\ui\\buttons\\test";
	
	private String[][] gridOfImages;
	private double scale;
	
	public ScalablePixelBack(int x, int y, int w, int h, double scale) {
		super(x, y, w, h);
		
		determineGridOfImagesSize(w,h,scale);
		populateGridOfImages();
		
		this.scale = scale;
		
		/*for(int row = 0; row < gridOfImages.length; row++) {
			String printLine = "";
			for(int column = 0; column < gridOfImages[row].length; column++) {
				printLine += " "+gridOfImages[row][column];
			}
			System.out.println(printLine);
		}*/
		update();
	}

	/**
	 * Function will determine how many images will be required in order
	 * to fulfill the user desired width and height pixel background
	 * 
	 * If given a scale, the amount of images will be determined by
	 * how many of a scaled 12x12 pixel will be required to fulfill
	 * the width and height of the image, will not be exact.
	 * 
	 * By default (no scale), the individual images are 12x12 pixels
	 * 
	 * @param width
	 * @param height
	 * @param scale2
	 */
	public void determineGridOfImagesSize(int width, int height, double scale2) {
		int xCount; int yCount;
		if(scale2 > 0) {
			width -= width%(12*scale2);
			height -= height%(12*scale2);
			xCount = (int) (width/(12*scale2));
			yCount = (int) (height/(12*scale2));
		}else {
			width -= width%12; height -= height%12;
			xCount = width/12; yCount = height/12;
		}
		System.out.println("Original Dimensions: " + width/12 + ", " + height/12);
		System.out.println("New Dimensions: " + xCount + ", " + yCount);
		gridOfImages = new String[yCount][xCount];
	}
	
	/**
	 * Fills a string grid array of the images locations in order for it
	 * to be recreated in update() easily:
	 * [topleft.png]....[top.png]....[topright.png]
	 * [left.png].......[center.png].[right.png]
	 * [left.png].......[center.png].[right.png]
	 * [left.png].......[center.png].[right.png]
	 * [bottomleft.png].[bottom.png].[bottomright.png]
	 */
	public void populateGridOfImages() {
		for(int row = 0; row < gridOfImages.length; row++) {
			for(int column = 0; column < gridOfImages[row].length; column++) {
				if(row == 0) {
					if(column == 0)
						gridOfImages[row][column] = testFile + "\\topleft.png";
					else if(column == gridOfImages[row].length -1)
						gridOfImages[row][column] = testFile + "\\topright.png";
					else 
						gridOfImages[row][column] = testFile + "\\top.png";
					
				}else if(row == gridOfImages.length - 1) {
					if(column == 0) 
						gridOfImages[row][column] = testFile + "\\bottomleft.png";
					else if(column == gridOfImages[row].length -1)
						gridOfImages[row][column] = testFile + "\\bottomright.png";
					else
						gridOfImages[row][column] = testFile + "\\bottom.png";
				}else {
					if(column == 0)
						gridOfImages[row][column] = testFile + "\\left.png";
					else if(column == gridOfImages[row].length -1)
						gridOfImages[row][column] = testFile + "\\right.png";
					else
						gridOfImages[row][column] = testFile + "\\center.png";
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public void update(Graphics2D g) {
		super.clear();
			try {
				int currentX = 0; int currentY = 0;
				for(int row = 0; row < gridOfImages.length; row++) {
					for(int column = 0; column < gridOfImages[row].length; column++) {
						ImageIcon icon = new ImageIcon(gridOfImages[row][column]);
						Image image = icon.getImage();
						//SCALING THE IMAGE
						if(scale > 0) {
							int side = (int)(12*scale);
							Image image2 = image.getScaledInstance(side,side, java.awt.Image.SCALE_SMOOTH);
							g.drawImage(image2,currentX,currentY,null);
						}else
							g.drawImage(image,currentX,currentY,null);
/*D*/					currentX+= 12;
						//System.out.println(currentX + " " + currentY);
					}
					currentX = 0;
/*D*/				currentY+=12;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
	}

}
