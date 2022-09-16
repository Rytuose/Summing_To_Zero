package core;

public abstract class Ability {
	
	protected int targets, cost;
	
	public Ability(int c) {
		targets = 0;
		cost = c;
	}
	
	public Ability(int c, int t) {
		targets = t;
		cost = c;
	}
	
	public boolean isApplicable(BoardRenderer br, Tile t) {
		return true;
	}
	
	public boolean activate(BoardRenderer br) {
		if(br.getPoints() < cost || br.getTargetTiles().size() != targets) {
			return false;
		}
		return true;
	}
	
	public int getCost() {
		return cost;
	}
	
	
}
