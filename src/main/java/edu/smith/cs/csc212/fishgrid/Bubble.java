/* 
 * Code in this package are build from existed code.
 * https://github.com/jjfiv/CSC212FishGrid.git
 */

package edu.smith.cs.csc212.fishgrid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * This class is provided so that you don't have to make any drawing code yourself!
 * @author jfoley
 *
 */
public class Bubble extends WorldObject {

	public Bubble(World world) {
		super(world);
	}

	@Override
	public void draw(Graphics2D g) {
		// Draw a "bubble!"
		Shape circle = new Ellipse2D.Double(-0.6, -0.6, 1.2, 1.2);
		g.setColor(new Color(1f,1f,1f,0.5f));
		g.fill(circle);
	}

	@Override
	public void step() {

	}

}
