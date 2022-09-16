package abilities;

import core.Ability;
import core.BoardRenderer;

public class Reroll extends Ability {
	public Reroll() {
		super(3);
	}
	
	@Override
	public boolean activate(BoardRenderer br) {
		if(super.activate(br)) {
			br.updateNextValue();
			br.updateNextValue();
			br.decreasePoint(cost);
			return true;
		}
		return false;
	}
}
