package org.silnith.game.solitaire.move.filter;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.StockPileAdvanceMove;
import org.silnith.game.solitaire.move.RecycleStockPileMove;
import org.silnith.game.solitaire.move.SolitaireMove;
import org.silnith.util.LinkedNode;

public class StockPileRecycleMustBeFollowedByAdvance implements SolitaireMoveFilter {

	@Override
	public boolean test(GameState<SolitaireMove, Board> state) {
		final SolitaireMove currentMove = state.getMoves().getFirst();
		
		final LinkedNode<SolitaireMove> moveHistory = state.getMoves().getNext();
		if (moveHistory == null) {
			return false;
		}
		
		final SolitaireMove previousMove = moveHistory.getFirst();
		
		if (previousMove instanceof RecycleStockPileMove) {
			if (currentMove instanceof StockPileAdvanceMove) {
				// This is acceptable, no need to filter.
				return false;
			} else {
				// Why do something not involving the stock pile after recycling it?
				return true;
			}
		} else {
			// This filter doesn't apply.
			return false;
		}
	}

}
