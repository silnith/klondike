package org.silnith.game.solitaire.move.filter;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.StockPileAdvanceMove;
import org.silnith.game.solitaire.move.StockPileRecycleMove;
import org.silnith.game.solitaire.move.FoundationToColumnMove;
import org.silnith.game.solitaire.move.SolitaireMove;
import org.silnith.game.solitaire.move.StockPileToColumnMove;
import org.silnith.game.solitaire.move.StockPileToFoundationMove;
import org.silnith.util.LinkedNode;

public class StockPileAdvanceMustBeFollowedBySomethingUseful implements SolitaireMoveFilter {

	@Override
	public boolean test(GameState<SolitaireMove, Board> state) {
		final SolitaireMove currentMove = state.getMoves().getFirst();
		
		final LinkedNode<SolitaireMove> moveHistory = state.getMoves().getNext();
		if (moveHistory == null) {
			return false;
		}
		
		final SolitaireMove previousMove = moveHistory.getFirst();
		
		if (previousMove instanceof StockPileAdvanceMove) {
			// check stuff
			if (currentMove instanceof StockPileAdvanceMove
					|| currentMove instanceof StockPileRecycleMove
					|| currentMove instanceof StockPileToColumnMove
					|| currentMove instanceof StockPileToFoundationMove
					|| currentMove instanceof FoundationToColumnMove) {
				// This is fine.
				return false;
			} else {
				// No need to advance the stock pile simply to do something unrelated to it.
				return true;
			}
		} else {
			// Don't care.
			return false;
		}
	}

}
