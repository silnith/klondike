package org.silnith.game.solitaire.move.filter;

import java.util.Iterator;
import java.util.List;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.SolitaireMove;

/**
 * Filters moves that introduce a cycle into the board history.
 */
public class BoardCycleFilter implements SolitaireMoveFilter {

    @Override
    public Object getStatisticsKey() {
        return "Board Cycle";
    }

    @Override
    public boolean shouldFilter(final List<GameState<SolitaireMove, Board>> gameStateHistory) {
        assert gameStateHistory != null;

        final Iterator<GameState<SolitaireMove, Board>> iterator = gameStateHistory.iterator();
        assert iterator.hasNext();
        final GameState<SolitaireMove, Board> currentGameState = iterator.next();
        final Board currentBoard = currentGameState.getBoard();

        while (iterator.hasNext()) {
            final GameState<SolitaireMove, Board> gameState = iterator.next();
            if (currentBoard.equals(gameState.getBoard())) {
                return true;
            }
        }
        return false;
    }

}
