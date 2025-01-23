package org.silnith.game.solitaire.move.filter;

import java.util.function.Predicate;

import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.SolitaireMove;

/**
 * A filter for Klondike solitaire move states.
 * 
 * <p>Game states that pass this filter are problematic and should be pruned from the search tree
 * rather than searched.</p>
 */
public interface SolitaireMoveFilter extends Predicate<GameState<SolitaireMove, Board>> {
	
}