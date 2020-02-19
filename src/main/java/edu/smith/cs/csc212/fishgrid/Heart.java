/* 
 * Code in this package are build from existed code.
 * https://github.com/jjfiv/CSC212FishGrid.git
 */

package edu.smith.cs.csc212.fishgrid;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * This class is provided so that you don't have to make any drawing code
 * yourself!
 * @author jfoley
 */
public class Heart extends WorldObject {

	public Heart(World world) {
		super(world);
	}

	@Override
	public void draw(Graphics2D g) {
		int size = 36;
		Graphics2D scale = (Graphics2D) g.create();
		scale.scale(1.0 / (double) size, 1.0 / (double) size);
		scale.translate(-size / 2, -size / 2);
		scale.setColor(Color.red);
		drawHeart(scale, 10, 10, 16, 16);
	}

	@Override
	public void step() {

	}

	/**
	 * Draw a heart. Sourced from: https://stackoverflow.com/a/38191657/1057048
	 * 
	 * @param g      - the graphics object.
	 * @param x      - the position of the heart (center)
	 * @param y      - the position of the heart (center)
	 * @param width  - the width of it
	 * @param height - the height of it
	 */
	private static void drawHeart(Graphics2D g, int x, int y, int width, int height) {
		int[] triangleX = { x - 2 * width / 18, x + width + 2 * width / 18,
				(x - 2 * width / 18 + x + width + 2 * width / 18) / 2 };
		int[] triangleY = { y + height - 2 * height / 3, y + height - 2 * height / 3, y + height };
		g.fillOval(x - width / 12, y, width / 2 + width / 6, height / 2);
		g.fillOval(x + width / 2 - width / 12, y, width / 2 + width / 6, height / 2);
		g.fillPolygon(triangleX, triangleY, triangleX.length);
	}

}
