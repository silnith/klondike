package org.silnith.game.solitaire.move.filter;

import java.util.Iterator;
import java.util.List;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
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
            /*
             * This can only happen at the very beginning of the game.
             * In that case, this filter is not helpful, so just let everything pass.
             */
		    return false;
		}
		
		final GameState<SolitaireMove, Board> previousGameState = iterator.next();
		final SolitaireMove previousMove = previousGameState.getMove();
		
		if (previousMove.isFromFoundation() && previousMove.isToColumn()) {
		    assert previousMove.hasCards();
		    if (currentMove.isFromFoundation()) {
		        /*
		         * Chained moves from the foundation could be for a purpose,
		         * so allow the chain to unfold.
		         */
		        return false;
		    }
			if (currentMove.isToColumn(previousMove.getToColumnIndex())) {
			    assert !currentMove.isFromFoundation();
			    /*
			     * The current move puts a card on top of the card taken
			     * from the foundation, so the foundation move has value.
			     */
			    return false;
			} else {
			    /*
			     * The current move does not make use of the card taken
			     * from the foundation, so the foundation move was worthless.
			     */
			    return true;
			}
		} else {
		    /*
		     * This filter only cares about moves that take cards from the foundation.
		     */
			return false;
		}
	}

}
