package abilities;

import core.Ability;
import core.BoardRenderer;
import core.Tile;

public class Split extends Ability {
	
	private int[] xShift = {0,1,0,-1};
	private int[] yShift = {1,0,-1,0};
	
	public Split() {
		super(10, 1);
	}
	
	@Override
	public boolean activate(BoardRenderer br) {
		if(super.activate(br)) {
			Tile[][] board = br.getBoard();
			Tile t = br.getTargetTiles().get(0);
			int x = t.getBoardWidth();
			int y = t.getBoardHeight();
			int toggle = (t.getValue() < 0)? -1:1;
			int splitCount = 0;
			
			for(int i = 0; i < xShift.length; i++) {
				for(int j = 0 ; j < yShift.length; j++) {
					try {
						if(board[x + xShift[i]][y + yShift[i]].getValue() == 0) {
							board[x + xShift[i]][y + yShift[i]].setValue(toggle);
							board[x + xShift[i]][y + yShift[i]].changeTime(-1);
							br.addToOpenTile();
							splitCount++;
						}
					}
					catch(Exception e) {}
				}
			}
			
			t.setValue(t.getValue() - toggle * splitCount);
			br.decreasePoint(cost);
			br.endTurn();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isApplicable(BoardRenderer br, Tile t) {
		return t.getValue() != 0 && t.getTime() > 0;
	}

}
