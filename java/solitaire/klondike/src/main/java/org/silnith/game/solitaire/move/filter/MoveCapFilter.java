package org.silnith.game.solitaire.move.filter;

import java.util.List;

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
	private final String key;

	/**
	 * Creates a new move cap filter with the given cap.
	 * 
	 * @param moveCap the maximum number of moves to allow in a game
	 */
	public MoveCapFilter(final int moveCap) {
		super();
		this.moveCap = moveCap;
		this.key = "Move Cap of " + this.moveCap;
	}

	@Override
    public Object getStatisticsKey() {
        return key;
    }

    @Override
	public boolean shouldFilter(final List<GameState<SolitaireMove, Board>> gameStateHistory) {
		return gameStateHistory.size() > moveCap;
	}

}
