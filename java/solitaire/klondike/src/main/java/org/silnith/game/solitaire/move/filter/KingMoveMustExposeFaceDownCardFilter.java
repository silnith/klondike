package org.silnith.game.solitaire.move.filter;

import java.util.List;

import org.silnith.deck.Card;
import org.silnith.deck.Value;
import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;
import org.silnith.game.solitaire.move.SolitaireMove;

/**
 * If a run starting with a king is moved to a new column, it must be
 * for the purpose of exposing a face-down card.
 */
public class KingMoveMustExposeFaceDownCardFilter implements SolitaireMoveFilter {

    @Override
    public Object getStatisticsKey() {
        return "King Move Must Expose Card";
    }

	@Override
	public boolean shouldFilter(final List<GameState<SolitaireMove, Board>> gameStateHistory) {
	    final GameState<SolitaireMove, Board> currentGameState = gameStateHistory.get(0);
		final SolitaireMove currentMove = currentGameState.getMove();
		final Board currentBoard = currentGameState.getBoard();
        /*
         * The current board is the state after the move has been applied.
         */
		
		assert currentMove != null;
		assert currentBoard != null;
		
		if (currentMove.isFromColumn() && currentMove.isToColumn()) {
		    assert currentMove.hasCards();
			final List<Card> run = currentMove.getCards();
			final Card firstCard = run.get(0);
			if (firstCard.getValue() == Value.KING) {
				/*
				 * This is the column after the move.
				 */
				final Column sourceColumn = currentBoard.getColumn(currentMove.getSourceColumnIndex());
				if (sourceColumn.hasFaceUpCards()) {
				    /*
				     * Something was left behind, therefore the move has value.
				     */
					return false;
				} else {
					/*
					 * The king was moved and now the column is empty.
					 * Since we know the destination must have been an empty column,
					 * we moved from an empty column to an empty column.
					 * Therefore, the move has no purpose.
					 */
					return true;
				}
			} else {
				/*
				 * This filter only cares about runs that start with a king.
				 */
				return false;
			}
		} else {
			/*
			 * Only interested in moves from a column to another column
			 * where the run begins with a king.
			 */
			return false;
		}
	}

}
