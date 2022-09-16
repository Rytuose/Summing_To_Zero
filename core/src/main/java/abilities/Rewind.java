package abilities;

import core.Ability;
import core.BoardRenderer;
import core.Constants;
import core.Tile;

public class Rewind extends Ability {
	public Rewind() {
		super(5);
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
					if(board[i][j].getTime() < Constants.tileTime) {
						board[i][j].changeTime(1);
					}
					if(board[i][j].getTime() == 1 || board[i][j].getTime() == 0) {// 0 to 1
						br.decreasePoint(-1);
					}
				}
			}
			br.decreasePoint(cost);
			return true;
		}
		return false;
	}
}
