package org.silnith.game.solitaire.move.filter;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.silnith.deck.Card;
import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.SolitaireMove;

/**
 * If a run is moved from one column to another, one of two conditions must hold true.
 * Either the entire run is moved, exposing a new face-down card (or emptying a column),
 * or the subsequent move must involve the existing face-up card exposed by moving the run.
 */
public class RunMoveMustBeFollowedBySomethingUsefulFilter implements SolitaireMoveFilter {

    @Override
    public Object getStatisticsKey() {
        return "Run Move Must Be Useful";
    }

	@Override
	public boolean shouldFilter(final List<GameState<SolitaireMove, Board>> gameStateHistory) {
	    final Iterator<GameState<SolitaireMove, Board>> iterator = gameStateHistory.iterator();
	    assert iterator.hasNext();
	    final GameState<SolitaireMove, Board> currentGameState = iterator.next();
		final SolitaireMove currentMove = currentGameState.getMove();
		assert currentMove != null;
		
		if (!iterator.hasNext()) {
            /*
             * This can only happen at the very beginning of the game.
             * In that case, this filter is not helpful, so just let everything pass.
             */
		    return false;
		}
		
		final GameState<SolitaireMove, Board> previousGameState = iterator.next();
		final SolitaireMove previousMove = previousGameState.getMove();
		final Board previousBoard = previousGameState.getBoard();
		/*
		 * This is the board AFTER the move has been applied!
		 */
		
		if (previousMove.isFromColumn()) {
            assert previousMove.hasCards();
			// check whether the previous run move used up all the cards
			// if it did, everything is fine

			if (!iterator.hasNext()) {
			    /*
			     * This can only happen at the very beginning of the game.
			     * In that case, this filter is not helpful, so just let everything pass.
			     */
			    return false;
			}
			final GameState<SolitaireMove, Board> gameStateTwoStepsBack = iterator.next();
			final Board boardTwoStepsBack = gameStateTwoStepsBack.getBoard();
			
            /*
             * Check whether the previous move took all the available cards from the source column.
             */
            final int sourceColumnIndex = previousMove.getFromColumnIndex();
            final int numberOfMovedCards = previousMove.getCards().size();
			if (numberOfMovedCards == boardTwoStepsBack.getColumn(sourceColumnIndex).getNumberOfFaceUpCards()) {
			    /*
			     * The previous move took all face-up cards from the source column.
			     * Therefore, it is not suspicious.
			     * (Shenanigans with the king are handled by other filters.)
			     */
				return false;
			} else {
			    /*
			     * The previous move took only a portion of the available run.
			     * Therefore, we want to make sure the top-most card NOT taken
			     * is involved with the current move.  Otherwise, the previous
			     * move has no value.
			     */
				final Card cardExposedByRunMove = previousBoard.getColumn(sourceColumnIndex).getTopCard();
				if (Collections.singletonList(cardExposedByRunMove).equals(currentMove.getCards())) {
					// This move uses the exposed card, meaning the previous run move is allowed.
					// This makes sure the move uses ONLY the exposed card, not as one of a run.
					// TODO: Should the only allowed follow-up move be column to foundation?
					return false;
				} else {
					/*
					 * This move does not use the exposed card, so filter the sequence of moves.
					 */
					return true;
				}
			}
		} else {
			/*
			 * Not interested in any scenario except where the second-most-recent move is moving a run from one column to another.
			 */
			return false;
		}
	}

}
