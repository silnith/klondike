package org.silnith.game.solitaire.move.filter;

import org.silnith.game.move.MoveFilter;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.SolitaireMove;

/**
 * A filter for Klondike solitaire moves.
 * 
 * <p>Game states that pass this filter are problematic and should be pruned
 * from the search tree rather than searched.</p>
 */
public interface SolitaireMoveFilter extends MoveFilter<SolitaireMove, Board> {
}
