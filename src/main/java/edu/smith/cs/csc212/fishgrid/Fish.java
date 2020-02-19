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
 * Most Fish behavior lives up in WorldObject (a Fish just looks special!).
 * Or it is in Main, where the missing/found and player fish all act different!
 * 
 * @author jfoley
 */
public class Fish extends WorldObject {
	/**
	 * A fish is only special because of its color now!
	 */
	Boolean fastScare = false;
	
	static Color[] COLORS = {
			Color.red,
			Color.green,
			Color.yellow,
			Color.magenta,
			Color.orange,
			Color.pink,
			
			// Black fish is special that have more points!
			Color.black
	};
	
	/**
	 * This is an index into the {@link #COLORS} array.
	 */
	int color;
	
	/**
	 * Whether or not this is the player;
	 */
	boolean player = false;
	
	/**
	 * Called only on the Fish that is the player!
	 */
	public void markAsPlayer() {
		this.player = true;
	}

	/**
	 * A Fish knows what World it belongs to, because all WorldObjects do.
	 * @param color Color by number.
	 * @param world The world itself.
	 */
	public Fish(int color, World world) {
		super(world);
		
		this.color = color;
		
		this.fastScare = rand.nextBoolean();	
	}
	
	/**
	 * What actual color is this fish? We store an index, so get it here.
	 * @return the Color object from our array.
	 */
	public Color getColor() {
		return COLORS[this.color];
	}
	
	/**
	 * Animate our fish by facing left and then right over time.
	 */
	private int dt = 0;
	
	/**
	 * Go ahead and ignore this method if you're not into graphics.
	 * We use "dt" as a trick to make the fish change directions every second or so; this makes them feel a little more alive.
	 */
	@Override
	public void draw(Graphics2D g) {
		// Draw a small fish that is fastScare.
		if (this.fastScare) {
			dt += 1;
			if (dt > 100) {
				dt = 0;
			}
			Shape circle = new Ellipse2D.Double(-.3, -.3, .6, .6);
			Shape body = new Ellipse2D.Double(-.2, -.1, .4, .2);
			Shape tail = new Ellipse2D.Double(+.1, -.15, .1, .3);
			Shape eye = new Ellipse2D.Double(-.12, -.05, .05, .05);
			
			Color color = getColor();
			Color tailColor = color.darker();

			
			Graphics2D flipped = (Graphics2D) g.create();
			if (dt < 50) {
				flipped.scale(-1, 1);
			}
			
			if (this.player) {
				flipped.setColor(new Color(1f,1f,1f,0.5f));
				flipped.fill(circle);
			}

			// Draw the fish of size (1x1, roughly, at 0,0).
			flipped.setColor(color);
			flipped.fill(body);

			flipped.setColor(Color.black);
			flipped.fill(eye);

			// draw tail:
			flipped.setColor(tailColor);
			flipped.fill(tail);
			
			flipped.dispose();
		}
		// Draw regular fish.
		else {
			dt += 1;
			if (dt > 100) {
				dt = 0;
			}
			Shape circle = new Ellipse2D.Double(-.6, -.6, 1.2, 1.2);
			Shape body = new Ellipse2D.Double(-.4, -.2, .8, .4);
			Shape tail = new Ellipse2D.Double(+.2, -.3, .2, .6);
			Shape eye = new Ellipse2D.Double(-.25, -.1, .1, .1);
			
			Color color = getColor();
			Color tailColor = color.darker();

			
			Graphics2D flipped = (Graphics2D) g.create();
			if (dt < 50) {
				flipped.scale(-1, 1);
			}
			
			if (this.player) {
				flipped.setColor(new Color(1f,1f,1f,0.5f));
				flipped.fill(circle);
			}

			// Draw the fish of size (1x1, roughly, at 0,0).
			flipped.setColor(color);
			flipped.fill(body);

			flipped.setColor(Color.black);
			flipped.fill(eye);

			// draw tail:
			flipped.setColor(tailColor);
			flipped.fill(tail);
			
			flipped.dispose();
		}
	}
		
	@Override
	public void step() {
		// Fish are controlled at a higher level; see FishGame.
	}
}
