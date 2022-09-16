package abilities;

import core.Ability;
import core.BoardRenderer;
import core.Tile;

public class Swap extends Ability {

	public Swap() {
		super(4,2);
	}
	
	@Override
	public boolean activate(BoardRenderer br) {
		if(br.getOpenTiles() > 23) {
			return true;
		}
		if(super.activate(br)) {
			int tempValue = br.getTargetTiles().get(0).getValue();
			int tempTime = br.getTargetTiles().get(0).getTime();
			
			br.getTargetTiles().get(0).setValue(br.getTargetTiles().get(1).getValue());
			br.getTargetTiles().get(0).setTime(br.getTargetTiles().get(1).getTime());
			
			br.getTargetTiles().get(1).setValue(tempValue);
			br.getTargetTiles().get(1).setTime(tempTime);
			
			br.decreasePoint(cost);
			br.endTurn();
			
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isApplicable(BoardRenderer br, Tile t) {
		return t.getValue() != 0;
	}

}
