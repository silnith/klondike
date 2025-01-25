package org.silnith.game.solitaire.move.filter;

import java.util.Iterator;
import java.util.List;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.FoundationToColumnMove;
import org.silnith.game.solitaire.move.SolitaireMove;
import org.silnith.game.solitaire.move.StockPileAdvanceMove;
import org.silnith.game.solitaire.move.StockPileToColumnMove;
import org.silnith.game.solitaire.move.StockPileToFoundationMove;

/**
 * Filters draw from stock pile moves if they do not follow a stock pile advance
 * or recycle.
 */
public class DrawFromStockPileFilter implements SolitaireMoveFilter {

    @Override
    public Object getStatisticsKey() {
        return "Draw From Stock Pile Must Follow Advance";
    }

    @Override
    public boolean shouldFilter(final List<GameState<SolitaireMove, Board>> state) {
        assert state != null;
        final Iterator<GameState<SolitaireMove, Board>> iterator = state.iterator();
        final GameState<SolitaireMove, Board> currentGameState = iterator.next();
        final SolitaireMove currentMove = currentGameState.getMove();

        if (currentMove instanceof StockPileToColumnMove
                || currentMove instanceof StockPileToFoundationMove) {
            // continue
        } else {
            // This filter does not apply.
            return false;
        }
        
        if (!iterator.hasNext()) {
            return false;
        }
        
        GameState<SolitaireMove, Board> previousGameState = iterator.next();
        SolitaireMove previousMove = previousGameState.getMove();
        // There may be a sequence of draws from the stock pile.
        while (previousMove instanceof StockPileToColumnMove
                || previousMove instanceof StockPileToFoundationMove
                || previousMove instanceof FoundationToColumnMove) {
            // Walk backwards.
            assert iterator.hasNext() : state;
            previousGameState = iterator.next();
            previousMove = previousGameState.getMove();
        }
        // Theoretically, it should only be possible for the previous move to be a stock
        // pile advance.
        // The recycle should make it impossible to draw from the stock pile.
        if (previousMove instanceof StockPileAdvanceMove) {
            // This is acceptable, no need to filter.
            return false;
        } else {
            // The previous move did not modify the stock pile, so drawing from the stock
            // pile is silly.
            return true;
        }
    }

}
