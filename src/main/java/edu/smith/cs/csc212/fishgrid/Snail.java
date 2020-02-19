/* 
 * Code in this package are build from existed code.
 * https://github.com/jjfiv/CSC212FishGrid.git
 */

package edu.smith.cs.csc212.fishgrid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * A Snail moves left to right and bumps into things!
 * @author jfoley
 */
public class Snail extends WorldObject {
	
	/**
	 * This is the color of the Snail's body.
	 */
	public Color bodyColor = Color.magenta;
	/**
	 * This is the color of the snail shell.
	 */
	public Color shellColor = Color.white;
	/**
	 * This is pupil of the snail color.
	 */
	public Color eyeColor = Color.black;
	/**
	 * Does the snail have its eyes open?
	 */
	public boolean eyesOpen = false;
	/**
	 * Is the snail going to the left?
	 */
	public boolean movingLeft = false;
	
	/**
	 * Create a new Snail in a part of this world.
	 * @param world - the world where the snail moves/lives.
	 */
	public Snail(World world) {
		super(world);
	}

	/**
	 * Polishing up my Snail draw method...
	 * This is kind of a mess, but that's graphics for you.
	 */
	@Override
	public void draw(Graphics2D input) {
		Graphics2D g = (Graphics2D) input.create();
		g.scale(1.0/100.0, 1.0/100.0);
		g.translate(-30, 0);
		Shape body = new Rectangle2D.Double(0, 0, 40, 50);
		Shape tentacleL = new Rectangle2D.Double(0, -20, 5, 20);
		Shape eyeWhiteL = new Ellipse2D.Double(-4, -28, 12, 12);
		Shape eyePupilL = new Ellipse2D.Double(-2, -26, 4, 4);
		Shape tentacleR = new Rectangle2D.Double(35, -20, 5, 20);
		Shape eyeWhiteR = new Ellipse2D.Double(35 - 4, -28, 12, 12);
		Shape eyePupilR = new Ellipse2D.Double(35 + 2, -26 + 4, 4, 4);
		
		bodyColor = Color.magenta;
		g.setColor(bodyColor);
		g.fill(body);
		g.fill(tentacleL);

		g.setColor(bodyColor);
		g.fill(tentacleR);

		if (!eyesOpen) {
			g.setColor(bodyColor);
			g.fill(eyeWhiteL);
			g.fill(eyeWhiteR);
		} else {
			g.setColor(Color.white);
			g.fill(eyeWhiteL);
			g.setColor(eyeColor);
			g.fill(eyePupilL);
			g.setColor(Color.white);
			g.fill(eyeWhiteR);
			g.setColor(eyeColor);
			g.fill(eyePupilR);
		}

		Shape shell3 = new Ellipse2D.Double(45, 20, 10, 10);
		Shape shell2 = new Ellipse2D.Double(35, 10, 30, 30);
		Shape shell1 = new Ellipse2D.Double(25, 0, 50, 50);

		g.setColor(shellColor);
		g.fill(shell1);
		g.setColor(Color.black);
		g.draw(shell1);
		g.setColor(Color.black);
		g.draw(shell2);
		g.setColor(Color.black);
		g.draw(shell3);
	}

	/**
	 * Move the snail left until it hits an obstacle. 
	 * Then move it right until it hits an obstacle.
	 * Alternate eyes open/closed as it moves.
	 */
	@Override
	public void step() {
		eyesOpen = !eyesOpen;
		if (movingLeft) {
			if (!moveLeft()) {
				movingLeft = false;
			}
		} else {
			if (!moveRight()) {
				movingLeft = true;
			}
		}
	}

}
