package org.silnith.game.solitaire;

import java.util.Iterator;
import java.util.List;

import org.silnith.deck.Card;
import org.silnith.game.Validator;


/**
 * Validates that a run of cards conforms to the rules of Klondike solitaire.
 */
public class RunValidator implements Validator<List<Card>> {

	/**
	 * Creates a new validator for runs of cards in Solitaire.
	 */
	public RunValidator() {
		super();
	}

	@Override
	public void validate(final List<Card> run) {
		if (run == null) {
			return;
		}
		if (run.isEmpty()) {
			return;
		}
        assert run != null;
        assert !run.isEmpty();
        
        final Iterator<Card> iterator = run.iterator();
        Card previousCard = iterator.next();
        while (iterator.hasNext()) {
            final Card currentCard = iterator.next();
            
            if (currentCard.getValue().getValue() + 1 != previousCard.getValue().getValue()) {
                throw new IllegalArgumentException(
                        "Cannot stack " + currentCard.getValue() + " on top of " + previousCard.getValue() + ".");
            }
            if (currentCard.getColor() == previousCard.getColor()) {
                throw new IllegalArgumentException(
                        "Cannot stack " + currentCard.getSuit() + " on top of " + previousCard.getSuit() + ".");
            }
            
            previousCard = currentCard;
        }
	}

}
