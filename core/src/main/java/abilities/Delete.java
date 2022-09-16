package abilities;

import core.Ability;
import core.BoardRenderer;
import core.Tile;

public class Delete extends Ability {
	
	public Delete() {
		super(8,1);
	}
	
	@Override
	public boolean activate(BoardRenderer br) {
		if(super.activate(br)) {
			br.getTargetTiles().get(0).setValue(0);
			br.decreasePoint(cost);
			br.endTurn();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isApplicable(BoardRenderer br, Tile t) {
		return t.getValue() != 0 || t.getTime() <= 0;
	}

}
