package org.silnith.game.solitaire.move.filter;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.StockPileAdvanceMove;
import org.silnith.game.solitaire.move.FoundationToColumnMove;
import org.silnith.game.solitaire.move.SolitaireMove;
import org.silnith.game.solitaire.move.StockPileToColumnMove;
import org.silnith.game.solitaire.move.StockPileToFoundationMove;
import org.silnith.util.LinkedNode;

/**
 * Filters draw from stock pile moves if they do not follow a stock pile
 * advance or recycle.
 */
public class DrawFromStockPileFilter implements SolitaireMoveFilter {

	@Override
	public boolean test(final GameState<SolitaireMove, Board> state) {
		assert state != null;
		
		final LinkedNode<SolitaireMove> moves = state.getMoves();
		assert moves != null;
		final SolitaireMove currentMove = moves.getFirst();
		if (currentMove instanceof StockPileToColumnMove
				|| currentMove instanceof StockPileToFoundationMove) {
			// continue
		} else {
			// This filter does not apply.
			return false;
		}
		// The current move draws from the stock pile.
		// Therefore, we know there must be a move in the history that advanced the stock pile,
		// because the initial board has the stock pile index set to zero.
		LinkedNode<SolitaireMove> moveHistory = moves.getNext();
		assert moveHistory != null;
		SolitaireMove previousMove = moveHistory.getFirst();
		assert previousMove != null;
		// There may be a sequence of draws from the stock pile.
		while (previousMove instanceof StockPileToColumnMove
				|| previousMove instanceof StockPileToFoundationMove
				|| previousMove instanceof FoundationToColumnMove) {
			// Walk backwards.
			moveHistory = moveHistory.getNext();
			assert moveHistory != null;
			previousMove = moveHistory.getFirst();
			assert previousMove != null;
		}
		// Theoretically, it should only be possible for the previous move to be a stock pile advance.
		// The recycle should make it impossible to draw from the stock pile.
		if (previousMove instanceof StockPileAdvanceMove) {
			// This is acceptable, no need to filter.
			return false;
		} else {
			// The previous move did not modify the stock pile, so drawing from the stock pile is silly.
			return true;
		}
	}

}
