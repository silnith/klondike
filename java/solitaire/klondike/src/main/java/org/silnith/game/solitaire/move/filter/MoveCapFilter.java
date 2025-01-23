package org.silnith.game.solitaire.move.filter;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.SolitaireMove;

/**
 * A move filter that caps the total game length.
 * 
 * <p>Realistically there is a finite limit to the number of moves it takes
 * to win a game of Klondike solitaire.  So searching a tree to a depth
 * greater than that number is unproductive.</p>
 */
public class MoveCapFilter implements SolitaireMoveFilter {
	
	private final int moveCap;
	
	public MoveCapFilter(final int moveCap) {
		super();
		this.moveCap = moveCap;
	}

	@Override
	public boolean test(final GameState<SolitaireMove, Board> gameState) {
		return gameState.getBoards().size() > moveCap;
	}

}