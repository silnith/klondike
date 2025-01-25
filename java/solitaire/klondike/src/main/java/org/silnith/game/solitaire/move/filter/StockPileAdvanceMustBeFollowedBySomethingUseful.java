package org.silnith.game.solitaire.move.filter;

import java.util.Iterator;
import java.util.List;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.FoundationToColumnMove;
import org.silnith.game.solitaire.move.SolitaireMove;
import org.silnith.game.solitaire.move.StockPileAdvanceMove;
import org.silnith.game.solitaire.move.StockPileRecycleMove;
import org.silnith.game.solitaire.move.StockPileToColumnMove;
import org.silnith.game.solitaire.move.StockPileToFoundationMove;

public class StockPileAdvanceMustBeFollowedBySomethingUseful implements SolitaireMoveFilter {

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
		    return false;
		}
		
		final GameState<SolitaireMove, Board> previousGameState = iterator.next();
		final SolitaireMove previousMove = previousGameState.getMove();
		
		if (previousMove instanceof StockPileAdvanceMove) {
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
