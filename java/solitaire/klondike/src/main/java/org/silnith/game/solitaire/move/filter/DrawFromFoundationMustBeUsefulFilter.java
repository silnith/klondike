package org.silnith.game.solitaire.move.filter;

import java.util.Iterator;
import java.util.List;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.FoundationToColumnMove;
import org.silnith.game.solitaire.move.SolitaireMove;

/**
 * If a card is drawn from the foundation, the following move must make use
 * of the card drawn.  Specifically, something must be put on top of it.
 */
public class DrawFromFoundationMustBeUsefulFilter implements SolitaireMoveFilter {

    @Override
    public Object getStatisticsKey() {
        return "Draw From Foundation Must Be Used";
    }

	@Override
	public boolean shouldFilter(final List<GameState<SolitaireMove, Board>> gameStateHistory) {
	    final Iterator<GameState<SolitaireMove, Board>> iterator = gameStateHistory.iterator();
	    assert iterator.hasNext();
	    final GameState<SolitaireMove, Board> currentGameState = iterator.next();
		final SolitaireMove currentMove = currentGameState.getMove();
		
		if (!iterator.hasNext()) {
		    return false;
		}
		
		final GameState<SolitaireMove, Board> previousGameState = iterator.next();
		final SolitaireMove previousMove = previousGameState.getMove();
		
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
