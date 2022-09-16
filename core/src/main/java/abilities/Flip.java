package abilities;

import core.Ability;
import core.BoardRenderer;

public class Flip extends Ability {
	public Flip() {
		super(1);
	}
	
	@Override
	public boolean activate(BoardRenderer br) {
		if(super.activate(br)) {
			System.out.println("Flipping");
			br.changeCurrentValue(br.getCurrentValue() * -1);
			br.decreasePoint(cost);
			return true;
		}
		return false;
	}
}
