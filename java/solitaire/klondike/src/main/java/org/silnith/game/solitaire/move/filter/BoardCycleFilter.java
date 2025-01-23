package org.silnith.game.solitaire.move.filter;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.SolitaireMove;
import org.silnith.util.LinkedNode;

/**
 * Filters moves that introduce a cycle into the board history.
 */
public class BoardCycleFilter implements SolitaireMoveFilter {

	@Override
	public boolean test(final GameState<SolitaireMove, Board> state) {
		assert state != null;
		
		final LinkedNode<Board> boards = state.getBoards();
		final Board currentBoard = boards.getFirst();
		final LinkedNode<Board> boardHistory = boards.getNext();
		if (boardHistory == null) {
			return false;
		}
		return boardHistory.contains(currentBoard);
	}

}
