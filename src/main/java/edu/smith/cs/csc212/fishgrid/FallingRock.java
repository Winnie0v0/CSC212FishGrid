package edu.smith.cs.csc212.fishgrid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/**
 * It would be awful nice to have multi-colored rocks at random.
 * This is not <a href="https://en.wikipedia.org/wiki/Dwayne_Johnson">the Rock</a>, but a Rock.
 * @author jfoley
 */
public class FallingRock extends Rock {
	/**
	 * I took these colors from Wikipedia's Cool and Warm Gray sections.
	 * https://en.wikipedia.org/wiki/Shades_of_gray#Cool_grays
	 * https://en.wikipedia.org/wiki/Shades_of_gray#Warm_grays
	 */
	private static Color[] ROCK_COLORS = new Color[] {
			new Color(144,144,192),
			new Color(145,163,176),
			new Color(112,128,144),
			new Color(94,113,106),
			new Color(76,88,102),
			new Color(170,152,169),
			new Color(152,129,123),
			new Color(138,129,141),
			new Color(72,60,50)
	};
	
	int index;
	
	/**
	 * Construct a Rock in our world.
	 * @param world - the grid world.
	 */
	public FallingRock(World world) {
		super(world);
		this.index = rand.nextInt(9);
	}

	/**
	 * Draw a rock!
	 */
	@Override
	public void draw(Graphics2D g) {
		g.setColor(ROCK_COLORS[this.index]);
		RoundRectangle2D rock = new RoundRectangle2D.Double(-.5,-.5,1,1,0.3,0.3);
		g.fill(rock);
	}

	@Override
	public void step() {
		this.moveDown();		
	}

}
