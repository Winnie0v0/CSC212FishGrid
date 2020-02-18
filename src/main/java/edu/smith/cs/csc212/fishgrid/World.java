package edu.smith.cs.csc212.fishgrid;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import me.jjfoley.gfx.IntPoint;

/**
 * A World is a 2d grid, represented as a width, a height, and a list of WorldObjects in that world.
 * @author jfoley
 *
 */
public class World {
	/**
	 * The size of the grid (x-tiles).
	 */
	private int width;
	/**
	 * The size of the grid (y-tiles).
	 */
	private int height;
	/**
	 * A list of objects in the world (Fish, Snail, Rock, etc.).
	 */
	private List<WorldObject> items;
	/**
	 * A reference to a random object, so we can randomize placement of objects in this world.
	 */
	private Random rand = ThreadLocalRandom.current();

	/**
	 * Create a new world of a given width and height.
	 * @param w - width of the world.
	 * @param h - height of the world.
	 */
	public World(int w, int h) {
		items = new ArrayList<>();
		width = w;
		height = h;
	}

	/**
	 * What is under this point?
	 * @param x - the tile-x.
	 * @param y - the tile-y.
	 * @return a list of objects!
	 */
	public List<WorldObject> find(int x, int y) {
		List<WorldObject> found = new ArrayList<>();
		
		// Check out every object in the world to find the ones at a particular point.
		for (WorldObject w : this.items) {
			// But only the ones that match are "found".
			if (x == w.getX() && y == w.getY()) {
				found.add(w);
			}
		}
		
		// Give back the list, even if empty.
		return found;
	}
	
	
	/**
	 * This is used by PlayGame to draw all our items!
	 * @return the list of items.
	 */
	public List<WorldObject> viewItems() {
		// Don't let anybody add to this list!
		// Make them use "register" and "remove".

		// This is kind of an advanced-Java trick to return a list where add/remove crash instead of working.
		return Collections.unmodifiableList(items);
	}

	/**
	 * Add an item to this World.
	 * @param item - the Fish, Rock, Snail, or other WorldObject.
	 */
	public void register(WorldObject item) {
		// Print out what we've added, for our sanity.
		System.out.println("register: "+item);
		items.add(item);
	}
	
	/**
	 * This is the opposite of register. It removes an item (like a fish) from the World.
	 * @param item - the item to remove.
	 */
	public void remove(WorldObject item) {
		// Print out what we've removed, for our sanity.
		System.out.println("remove: "+item.getClass().getSimpleName());
		items.remove(item);
	}
	
	/**
	 * How big is the world we model?
	 * @return the width.
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * How big is the world we model?
	 * @return the height.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Try to find an unused part of the World for a new object!
	 * @return a point (x,y) that has nothing else in the grid.
	 */
	public IntPoint pickUnusedSpace() {
		// Build a set of all available spaces:
		Set<IntPoint> available = new HashSet<>();
		for (int x=0; x<getWidth(); x++) {
			for (int y=0; y<getHeight(); y++) {
				available.add(new IntPoint(x, y));
			}
		}
		// Remove any spaces that are in use:
		for (WorldObject item : this.items) {
			available.remove(item.getPosition());
		}

		// If we get here, we have too much stuff.
		// Let's crash our Java program!
		if (available.size() == 0) {
			throw new IllegalStateException("The world is too small! Trying to pick an unused space but there's nothing left.");
		}

		// Return an unused space at random: Need to copy to a list since sets do not have orders.
		List<IntPoint> unused = new ArrayList<>(available);
		int which = rand.nextInt(unused.size());
		return unused.get(which);
	}
	
	/**
	 * Insert an item randomly into the grid.
	 * @param item - the rock, fish, snail or other WorldObject.
	 */
	public void insertRandomly(WorldObject item) {
		item.setPosition(pickUnusedSpace());
		this.register(item);
		item.checkFindMyself();
	}
	
	/**
	 * Insert a new Rock into the world at random.
	 * @return the Rock.
	 */
	public Rock insertRockRandomly() {
		Rock r = new Rock(this);
		insertRandomly(r);
		return r;
	}
	
	public Rock insertFallingRockRandomly() {
		FallingRock fr = new FallingRock(this);
		insertRandomly(fr);
		return fr;
	}
	
	/**
	 * Insert a new Fish into the world at random of a specific color.
	 * @param color - the color of the fish.
	 * @return the new fish itself.
	 */
	public Fish insertFishRandomly(int color) {
		Fish f = new Fish(color, this);
		insertRandomly(f);
		return f;
	}
	
	public FishHome insertFishHome() {
		FishHome home = new FishHome(this);
		insertRandomly(home);
		return home;
	}
	
	public Bubble insertBubble() {
		Bubble bubble = new Bubble(this);
		insertRandomly(bubble);
		return bubble;
	}
	
	/**
	 * Insert a new Snail at random into the world.
	 * @return the snail!
	 */
	public Snail insertSnailRandomly() {
		Snail snail = new Snail(this);
		insertRandomly(snail);
		return snail;
	}
	
	public Heart insertHeart() {
		Heart heart = new Heart(this);
		insertRandomly(heart);
		return heart;
	}
	
	/**
	 * Determine if a WorldObject can swim to a particular point.
	 * 
	 * @param whoIsAsking - the object (not just the player!)
	 * @param x - the x-tile.
	 * @param y - the y-tile.
	 * @return true if they can move there.
	 */
	public boolean canSwim(WorldObject whoIsAsking, int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}
		
		// This will be important.
		boolean isPlayer = whoIsAsking.isPlayer();
		
		// We will need to look at who all is in the spot to determine if we can move there.
		List<WorldObject> inSpot = this.find(x, y);
		
		for (WorldObject it : inSpot) {
			// The other fish shouldn't step "on" the player, the player should step on the other fish.
						// The other fish shouldn't step "on" the player, the player should step on the other fish.
			if (it instanceof Snail) {
				// This if-statement doesn't let anyone step on the Snail.
				// The Snail(s) are not gonna take it.
				return false;
			}
			if (it instanceof Rock) {
				// This if-statement doesn't let anyone step on the Snail.
				// The Snail(s) are not gonna take it.
				return false;
			}
			if (it instanceof Fish) {
				if (isPlayer) {
					return true;
				}
				else {
					// This if-statement doesn't let anyone step on the Snail.
					// The Snail(s) are not gonna take it.
					return false;
				}
			}
			if (it instanceof FishHome) {
				if (isPlayer || whoIsAsking instanceof Fish) {
					return true;
				}
				else {
					// This if-statement doesn't let anyone step on the Snail.
					// The Snail(s) are not gonna take it.
					return false;
				}
			}
			if (it instanceof Bubble) {
				if (isPlayer || whoIsAsking instanceof Fish) {
					return true;
				}
				else {
					// This if-statement doesn't let anyone step on the Snail.
					// The Snail(s) are not gonna take it.
					return false;
				}
			}
			if (it instanceof Heart) {
				if (isPlayer || whoIsAsking instanceof Fish) {
					return true;
				}
				else {
					// This if-statement doesn't let anyone step on the Snail.
					// The Snail(s) are not gonna take it.
					return false;
				}
			}
			
		}
					
					// If we didn't see an obstacle, we can move there!
			return true;
		}
	
	/*
			if (isPlayer) {
				
				if (it instanceof Fish||it instanceof FishHome||it instanceof Heart) {
					// This if-statement doesn't let anyone step on the rock.
					// The Snail(s) are not gonna take it.
					return true;
				}
				else {
					// This if-statement doesn't let anyone step on the rock.
					// The Snail(s) are not gonna take it.
					return false;
				}
			}
			else if (it instanceof Fish){
				if (it instanceof FishHome||it instanceof Heart) {
					// This if-statement doesn't let anyone step on the rock.
					// The Snail(s) are not gonna take it.
					return true;
				}
				else {
					// This if-statement doesn't let anyone step on the rock.
					// The Snail(s) are not gonna take it.
					return false;
				}
			}
			else {
				return false;
			}
		}
			// If we didn't see an obstacle, we can move there!
		return true;
	}*/
	
	/**
	 * This is how objects may move. Only Snails do right now.
	 */
	public void stepAll() {
		for (WorldObject it : this.items) {
			it.step();
		}
	}
	
	/**
	 * This signature is a little scary, but we need to support any subclass of WorldObject.
	 * We don't know followers is a {@code List<Fish>} but it should work no matter what!
	 * @param target the leader.
	 * @param followers a set of objects to follow the leader.
	 */
	public static void objectsFollow(WorldObject target, List<? extends WorldObject> followers) {
		// TODO(FishGrid) Comment this method!
		// Q1. What is recentPositions?
		// The recentPosition is a Deque keep up to NUM_RECENT_POSITIONS locations for every fish.
		// Q2. What is followers?
		// Followers is a set of objects to follow the leader, to be specific those fishes that the player collected.
		// Q3. What is target?
		// The target is the leader, which is the player.
		// Q4. Why is past = putWhere[i+1]? Why not putWhere[i]?
		// Because if we use i to index the List putWhere, we get where the player is now,
		// However, in order to let the followers follow the target, we need to know where it was the previous step,
		// which is index (i+1)
		List<IntPoint> putWhere = new ArrayList<>(target.recentPositions);
		for (int i=0; i < followers.size() && i+1 < putWhere.size(); i++) {
			// Q5. What is the deal with the two conditions in this for-loop?
			// Conditions are in the "while" part of this loop.
			// First of all, if we want to decide where each follower should be at, 
			// we want to index into the list where followers are stored, which is <followers>.
			// However, we cannot index out of the size of the list, there for i < followers.size()
			// Then, we also use i+1 to index into the List putWhere to get where the target or the player was,
			// indexing out of that list will cause error too.
			IntPoint past = putWhere.get(i+1);
			followers.get(i).setPosition(past.x, past.y);
		}
	}
}
