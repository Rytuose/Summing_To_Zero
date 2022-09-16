package abilities;

import core.Ability;
import core.BoardRenderer;
import core.Tile;

public class Fuse extends Ability {
	
	private int[] xShift = {0,1,0,-1};
	private int[] yShift = {1,0,-1,0};
	
	public Fuse() {
		super(2,1);
	}
	
	@Override
	public boolean activate(BoardRenderer br) {
		if(super.activate(br)) {
			Tile[][] board = br.getBoard();
			Tile t = br.getTargetTiles().get(0);
			int x = t.getBoardWidth();
			int y = t.getBoardHeight();
			int sum = t.getValue();
			
			for(int i = 0; i < xShift.length; i++) {
				for(int j = 0 ; j < yShift.length; j++) {
					try {
						if(board[x + xShift[i]][y + yShift[i]].getValue() != 0) {
							sum += board[x + xShift[i]][y + yShift[i]].getValue();
							board[x + xShift[i]][y + yShift[i]].setValue(0);
						}
					}
					catch(Exception e) {}
				}
			}
			
			t.setValue(sum);
			br.decreasePoint(cost);
			
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isApplicable(BoardRenderer br, Tile t) {
		Tile[][] board = br.getBoard();
		int x = t.getBoardWidth();
		int y = t.getBoardHeight();
		int sum = t.getValue();
		boolean hasNeighbor = false;
		
//		for(int i = 0; i < xShift.length; i++) {
//			for(int j = 0 ; j < yShift.length; j++) {
//				try {
//					if(board[x + xShift[i]][y + yShift[j]].getValue() != 0) {
//						sum += board[x + xShift[i]][y + yShift[j]].getValue();
//						hasNeighbor = true;
//					}
//				}
//				catch(Exception e) {}
//			}
//		}
		
		for(int i = 0; i < xShift.length; i++) {
			try {
				if(board[x + xShift[i]][y + yShift[i]].getValue() != 0) {
					sum += board[x + xShift[i]][y + yShift[i]].getValue();
					hasNeighbor = true;
				}
			}
			catch(Exception e) {}
		}
		
		return sum >= -9 && sum <= 9 && hasNeighbor;
		
	}
}
