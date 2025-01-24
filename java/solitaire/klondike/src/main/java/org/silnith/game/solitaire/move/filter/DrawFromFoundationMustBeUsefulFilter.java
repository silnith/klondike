package org.silnith.game.solitaire.move.filter;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.FoundationToColumnMove;
import org.silnith.game.solitaire.move.SolitaireMove;
import org.silnith.util.LinkedNode;

/**
 * If a card is drawn from the foundation, the following move must make use
 * of the card drawn.  Specifically, something must be put on top of it.
 */
public class DrawFromFoundationMustBeUsefulFilter implements SolitaireMoveFilter {

	@Override
	public boolean test(final GameState<SolitaireMove, Board> state) {
		final SolitaireMove currentMove = state.getMoves().getFirst();
		
		final LinkedNode<SolitaireMove> moveHistory = state.getMoves().getNext();
		
		if (moveHistory == null) {
			return false;
		}
		
		final SolitaireMove previousMove = moveHistory.getFirst();
		
		if (previousMove instanceof FoundationToColumnMove) {
			final FoundationToColumnMove foundationToColumnMove = (FoundationToColumnMove) previousMove;
			final int destinationColumn = foundationToColumnMove.getDestinationColumn();
			// If currentMove adds cards to the same column, this is valid.
			// Otherwise, filter.
			if (currentMove.addsCardsToColumn(destinationColumn)) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

}
