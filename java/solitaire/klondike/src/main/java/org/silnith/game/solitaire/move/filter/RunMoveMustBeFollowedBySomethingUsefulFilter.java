package org.silnith.game.solitaire.move.filter;

import java.util.Collections;
import java.util.List;

import org.silnith.deck.Card;
import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;
import org.silnith.game.solitaire.move.RunMove;
import org.silnith.game.solitaire.move.SolitaireMove;
import org.silnith.util.LinkedNode;

/**
 * If a run is moved from one column to another, one of two conditions must hold true.
 * Either the entire run is moved, exposing a new face-down card (or emptying a column),
 * or the subsequent move must involve the existing face-up card exposed by moving the run.
 */
public class RunMoveMustBeFollowedBySomethingUsefulFilter implements SolitaireMoveFilter {

	@Override
	public boolean test(final GameState<SolitaireMove, Board> state) {
		final SolitaireMove currentMove = state.getMoves().getFirst();
		assert currentMove != null;
		
		final LinkedNode<SolitaireMove> moveHistory = state.getMoves().getNext();
		if (moveHistory == null) {
			return false;
		}
		final LinkedNode<Board> boardHistory = state.getBoards().getNext();
		assert boardHistory != null : "How can the board history be shorter than the move history?";
		
		final SolitaireMove previousMove = moveHistory.getFirst();
		final Board previousBoard = boardHistory.getFirst();
		/*
		 * This is the board AFTER the move has been applied!
		 */
		
		if (previousMove instanceof RunMove) {
			final RunMove previousRunMove = (RunMove) previousMove;
			// check whether the previous run move used up all the cards
			// if it did, everything is fine
			assert previousRunMove.hasCards();
			
			if (boardHistory.getNext() == null) {
				return false;
			}
			final Board boardTwoStepsBack = boardHistory.getNext().getFirst();
			
			final Column sourceColumn = boardTwoStepsBack.getColumns().get(previousRunMove.getSourceColumn());
			final List<Card> faceUpCards = sourceColumn.getFaceUpCards();
			final int availableCards = faceUpCards.size();
			final int numberOfMovedCards = previousRunMove.getNumberOfCards();
			if (availableCards == numberOfMovedCards) {
				// Took everything, no need to filter.
				return false;
			} else {
				final Card exposedCard = previousBoard.getColumns().get(previousRunMove.getSourceColumn()).getTopCard();
				if (currentMove.hasCards()
						&& Collections.singletonList(exposedCard).equals(currentMove.getCards())) {
					// This move uses the exposed card, meaning the previous run move is allowed.
					// This makes sure the move uses ONLY the exposed card, not as one of a run.
					// TODO: Should the only allowed follow-up move be column to foundation?
					return false;
				} else {
					// This move does not use the exposed card, so filter the sequence of moves.
					return true;
				}
			}
		} else {
			// Not interested in any scenario except where the second-most-recent move is moving a run from one column to another.
			return false;
		}
	}

}
