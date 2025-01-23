package org.silnith.game.solitaire.move.filter;

import java.util.List;
import java.util.Map;

import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.deck.Value;
import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.move.FoundationToColumnMove;
import org.silnith.game.solitaire.move.SolitaireMove;

/**
 * A filter that blocks moving out of the foundation once all suits have
 * reached a certain value.
 */
public class LockFoundationFilter implements SolitaireMoveFilter {

	@Override
	public boolean test(final GameState<SolitaireMove, Board> state) {
		assert state != null;
		
		final SolitaireMove currentMove = state.getMoves().getFirst();
		if (currentMove instanceof FoundationToColumnMove) {
			final FoundationToColumnMove foundationToColumnMove = (FoundationToColumnMove) currentMove;
			
			// check if the move is silly
			final Map<Suit, List<Card>> foundation = state.getBoards().getFirst().getFoundation();
			int minValue = Value.KING.getValue();
			for (final List<Card> foundationForSuit : foundation.values()) {
				if (foundationForSuit.isEmpty()) {
					// At least one suit has no cards in the foundation.
					minValue = 1;
					break;
				}
				final Card highestCardForSuit = foundationForSuit.get(foundationForSuit.size() - 1);
				minValue = Math.min(minValue, highestCardForSuit.getValue().getValue());
			}
			
			if (foundationToColumnMove.getCard().getValue().getValue() <= minValue + 2) {
				return true;
			} else {
				return false;
			}
		} else {
			// This move is not moving a card out of the foundation, this filter does not apply.
			return false;
		}
	}

}
