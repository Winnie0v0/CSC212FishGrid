package edu.smith.cs.csc212.fishgrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class manages our model of gameplay: missing and found fish, etc.
 * @author jfoley
 *
 */
public class FishGame {
	/**
	 * This is the world in which the fish are missing. (It's mostly a List!).
	 */
	World world;
	/**
	 * The player (a Fish.COLORS[0]-colored fish) goes seeking their friends.
	 */
	Fish player;
	/**
	 * The home location.
	 */
	FishHome home;
	
	Heart heart;
	
	Bubble bubble;
	/**
	 * These are the missing fish!
	 */
	List<Fish> missing;
	
	/**
	 * These are fish we've found!
	 */
	List<Fish> found;
	
	List<Fish> fishhome;
	
	/**
	 * Number of steps!
	 */
	int stepsTaken;
	
	/**
	 * Score!
	 */
	int score;
	
	/**
	 * Create a FishGame of a particular size.
	 * @param w how wide is the grid?
	 * @param h how tall is the grid?
	 */
	
	int NUM_ROCKS = 10;
	
	int NUM_SNAIL = 2;
	

	
	Random rand = ThreadLocalRandom.current();
	
	public FishGame(int w, int h) {
		world = new World(w, h);
		
		missing = new ArrayList<Fish>();
		found = new ArrayList<Fish>();
		fishhome = new ArrayList<Fish>();
		
		// Add a home!
		home = world.insertFishHome();
		heart =  world.insertHeart();
		bubble = world.insertBubble();
		
		for (int i=0; i<NUM_ROCKS; i++) {
			if (rand.nextDouble() < 0.5) {
				world.insertRockRandomly();
			}
			else {
				world.insertFallingRockRandomly();
			}
		}
		
		for (int i=0; i<NUM_SNAIL; i++) {
			world.insertSnailRandomly();
		}
		
		// Make the player out of the 0th fish color.
		player = new Fish(0, world);
		// Start the player at "home".
		player.setPosition(home.getX(), home.getY());
		player.markAsPlayer();
		world.register(player);
		
		// Generate fish of all the colors but the first into the "missing" List.
		for (int ft = 1; ft < Fish.COLORS.length; ft++) {
			Fish friend = world.insertFishRandomly(ft);
			missing.add(friend);
		}		
	}
	
	
	/**
	 * How we tell if the game is over: if missingFishLeft() == 0.
	 * @return the size of the missing list.
	 */
	public int missingFishLeft() {
		return missing.size();
	}
	
	/**
	 * This method is how the Main app tells whether we're done.
	 * @return true if the player has won (or maybe lost?).
	 */
	public boolean gameOver() {
		if (missing.isEmpty() && found.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
		
	}

	/**
	 * Update positions of everything (the user has just pressed a button).
	 */
	public void step() {
		// Keep track of how long the game has run.
		if (rand.nextDouble() < 0.02) {
			heart =  world.insertHeart();
		}
		if (rand.nextDouble() < 0.02) {
			bubble =  world.insertBubble();
		}
		
		this.stepsTaken += 1;
		lostFish();
				
		// These are all the objects in the world in the same cell as the player.
		List<WorldObject> overlap = this.player.findSameCell();
		// The player is there, too, let's skip them.
		overlap.remove(this.player);
		
		// If we find a fish, remove it from missing.
		for (WorldObject wo : overlap) {
			// It is missing if it's in our missing list.
			if (missing.contains(wo)) {
				if (!(wo instanceof Fish)) {
					throw new AssertionError("wo must be a Fish since it was in missing!");
				}
				// Convince Java it's a Fish (we know it is!)
				Fish justFound = (Fish) wo;
				
				// Remove from world.
				
				//justFound.remove();
				found.add(justFound);
				missing.remove(justFound);
				// Earn 10 points when you find a fish!
				// Earn extra 10 points when a fastScare fish is found!
				// Earn extra 100 points when a fastScare fish is found!!
				if (justFound.color == 6) {
					score += 110;
				}
				else {
					score += 10;
				}
				if (justFound.fastScare) {
					score += 10;
				} 
			}
			if (wo instanceof Heart) {
				score += 520;
				world.remove(wo);
			}
			if (wo instanceof Bubble) {
				world.remove(wo);
			}
			if (wo instanceof FishHome) {
				 fishhome.addAll(found);
				 found.removeAll(found);
				 for (WorldObject o : fishhome) {
					 world.remove(o);
				 }
			}
		}
		
		// Make sure missing fish *do* something.
		wanderMissingFish();
		
		//homeFish();
		// When fish get added to "found" they will follow the player around.
		World.objectsFollow(player, found);
		// Step any world-objects that run themselves.
		world.stepAll();
	}
	
	private void lostFish() {
		if (found.size()>1 && this.stepsTaken>=20) {
			if (rand.nextDouble() < 0.1) {
				missing.add(found.get(found.size()-1));
				found.remove(found.size()-1);
			}	
		}
	}	
	
	/**
	 * Call moveRandomly() on all of the missing fish to make them seem alive.
	 */
	private void wanderMissingFish() {
		Random rand = ThreadLocalRandom.current();
		List<WorldObject> overlapfish;
		List<Fish> ff = new ArrayList<Fish>();
		//List<Fish> remove = null;
		for (Fish lost : missing) {
			if (lost.fastScare) {
				if (rand.nextDouble() < 0.8) {
					lost.moveRandomly();
				}	
			}
			else {
				if (rand.nextDouble() < 0.3) {
					lost.moveRandomly();
				}
			}
			
			overlapfish = lost.findSameCell();
			overlapfish.remove(lost);
			
			for (WorldObject hf : overlapfish) {
				if (hf instanceof FishHome) {
					fishhome.add(lost);
					ff.add(lost);
					world.remove(lost);
				}
				if (hf instanceof Heart) {
					world.remove(hf);
				}
				if (hf instanceof Bubble) {
					world.remove(hf);
				}
				// if fish bump into a bubble
			}
			
		}
		missing.removeAll(ff);
	}

	/**
	 * This gets a click on the grid. We want it to destroy rocks that ruin the game.
	 * @param x - the x-tile.
	 * @param y - the y-tile.
	 */
	public void click(int x, int y) {
		System.out.println("Clicked on: "+x+","+y+ " world.canSwim(player,...)="+world.canSwim(player, x, y));
		List<WorldObject> atPoint = world.find(x, y);

		for (WorldObject it : atPoint) {
			if (it instanceof Rock) {
				world.remove(it);
			}
			else if (it instanceof Bubble) {
				world.remove(it);
			}
		}

	}
	
}
