package org.silnith.game.solitaire.move.filter;

import java.util.Iterator;
import java.util.List;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.SolitaireMove;

public class StockPileAdvanceMustBeFollowedBySomethingUsefulFilter implements SolitaireMoveFilter {

    @Override
    public Object getStatisticsKey() {
        return "Stock Pile Draw Must Follow Advance";
    }

	@Override
	public boolean shouldFilter(final List<GameState<SolitaireMove, Board>> state) {
	    final Iterator<GameState<SolitaireMove, Board>> iterator = state.iterator();
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
		
		if (previousMove.isStockPileAdvance()) {
			if (currentMove.isStockPileModification()
                    || currentMove.isFromStockPile()
                    || currentMove.isFromFoundation()) {
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
