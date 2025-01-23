package org.silnith.game.solitaire.move.filter;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.SolitaireMove;
import org.silnith.util.LinkedNode;

/**
 * Filters a move if it is moving the exact same stack of cards as the previous move.
 */
public class RedundantStackMoveFilter implements SolitaireMoveFilter {

	@Override
	public boolean test(GameState<SolitaireMove, Board> state) {
		assert state != null;
		
		final LinkedNode<SolitaireMove> moves = state.getMoves();
		final SolitaireMove currentMove = moves.getFirst();
		LinkedNode<SolitaireMove> moveHistory = moves.getNext();
		if (moveHistory == null) {
			return false;
		}
		
		/*
		 * Should only move a stack if it exposes a face-down card,
		 * or if the subsequent move uses the already face-up card
		 * that became the new top of the run.
		 */
		
		SolitaireMove previousMove = moveHistory.getFirst();
		
		while (!previousMove.hasCards()) {
			// Skip over moves that do not involve cards.
			// Specifically, skip over stock pile manipulations.
			moveHistory = moveHistory.getNext();
			if (moveHistory == null) {
				return false;
			}
			previousMove = moveHistory.getFirst();
		}
		
		return currentMove.hasCards() && previousMove.hasCards()
				&& currentMove.getCards().equals(previousMove.getCards());
	}

}
