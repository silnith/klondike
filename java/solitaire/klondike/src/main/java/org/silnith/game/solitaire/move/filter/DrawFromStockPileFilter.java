package org.silnith.game.solitaire.move.filter;

import java.util.Iterator;
import java.util.List;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.SolitaireMove;

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

        if (currentMove.isFromStockPile()) {
            // continue
        } else {
            // This filter does not apply.
            return false;
        }
        
        if (!iterator.hasNext()) {
            /*
             * This can only happen at the very beginning of the game.
             * In that case, this filter is not helpful, so just let everything pass.
             */
            return false;
        }
        
        GameState<SolitaireMove, Board> previousGameState = iterator.next();
        SolitaireMove previousMove = previousGameState.getMove();
        // There may be a sequence of draws from the stock pile.
        while (previousMove.isFromStockPile()
                || previousMove.isFromFoundation()) {
            /*
             * Walk backwards.
             * Ignoring moves from the foundation is a special-case.
             * In general moves from the foundation are filtered, but
             * they are allowed to provide a destination for column-to-column
             * moves or stock pile draws in the event that no other destination
             * is available.
             */
            assert iterator.hasNext() : "There must always be a stock pile advance before it is possible to draw from the stock pile.  (Or foundation.)";
            previousGameState = iterator.next();
            previousMove = previousGameState.getMove();
        }
        // Theoretically, it should only be possible for the previous move to be a stock
        // pile advance.
        // The recycle should make it impossible to draw from the stock pile.
        if (previousMove.isStockPileModification()) {
            /*
             * This is acceptable, no need to filter.
             */
            return false;
        } else {
            /*
             * The previous move did not modify the stock pile,
             * so drawing from the stock pile is silly.
             */
            return true;
        }
    }

}
