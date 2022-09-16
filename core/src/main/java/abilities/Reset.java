package abilities;

import core.Ability;
import core.BoardRenderer;
import core.Tile;

public class Reset extends Ability {

	public Reset() {
		super(50);
	}
	
	@Override
	public boolean activate(BoardRenderer br) {
		if(super.activate(br)) {
			if(br.getOpenTiles() >= 25) {
				return true;
			}
			Tile[][] board = br.getBoard();
			for(int i = 0 ; i < board.length; i++) {
				for(int j = 0 ; j < board[i].length; j++) {
					if(board[i][j].getValue() != 0) {
						board[i][j].setValue(0);
					}
				}
			}
			br.decreasePoint(cost);
			return true;
		}
		return false;
		
		
	}

}
