package org.silnith.game.solitaire.move.filter;

import java.util.Iterator;
import java.util.List;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.SolitaireMove;
import org.silnith.game.solitaire.move.StockPileAdvanceMove;
import org.silnith.game.solitaire.move.StockPileRecycleMove;

public class StockPileRecycleMustBeFollowedByAdvance implements SolitaireMoveFilter {

    @Override
    public Object getStatisticsKey() {
        return "Stock Pile Recycle Must Be Followed By Advance";
    }

	@Override
	public boolean shouldFilter(final List<GameState<SolitaireMove, Board>> state) {
        final Iterator<GameState<SolitaireMove, Board>> iterator = state.iterator();
        assert iterator.hasNext();
        final GameState<SolitaireMove, Board> currentGameState = iterator.next();
        final SolitaireMove currentMove = currentGameState.getMove();
        
        if (!iterator.hasNext()) {
            return false;
        }
        
        final GameState<SolitaireMove, Board> previousGameState = iterator.next();
        final SolitaireMove previousMove = previousGameState.getMove();
		
		if (previousMove instanceof StockPileRecycleMove) {
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
