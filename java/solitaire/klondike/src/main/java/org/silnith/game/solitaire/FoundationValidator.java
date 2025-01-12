package org.silnith.game.solitaire;

import static org.silnith.deck.Value.ACE;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.game.Validator;


/**
 * Validates that the foundation conforms to the rules of Klondike solitaire.
 * 
 * <p>The foundation is one stack for each {@link Suit}.  The stack for each suit
 * must start with the {@link org.silnith.deck.Value#ACE} and count up incrementally.</p>
 */
public class FoundationValidator implements Validator<Map<Suit, List<Card>>> {
    
    /**
     * Creates a new foundation validator.
     */
    public FoundationValidator() {
        super();
    }
    
    @Override
    public void validate(final Map<Suit, List<Card>> foundation) {
        for (final Suit suit : Suit.values()) {
            final List<Card> cardsForSuit = foundation.get(suit);
            if (cardsForSuit == null) {
                throw new IllegalArgumentException("No list of cards for suit " + suit);
            }
            final Iterator<Card> iterator = cardsForSuit.iterator();
            if (iterator.hasNext()) {
                Card previousCard = iterator.next();
                validateSuit(suit, previousCard);
                
                if (previousCard.getValue() != ACE) {
                    throw new IllegalArgumentException("First card in foundation for suit " + suit + " must be an " + ACE + ", not a " + previousCard + ".");
                }
                while (iterator.hasNext()) {
                    final Card currentCard = iterator.next();
                    validateSuit(suit, currentCard);
                    
                    if (previousCard.getValue().getValue() + 1 != currentCard.getValue().getValue()) {
                        throw new IllegalArgumentException("Cannot put " + currentCard + " on top of " + previousCard + " in foundation for suit " + suit + ".");
                    }
                    previousCard = currentCard;
                }
            }
        }
    }
    
    private void validateSuit(final Suit suit, final Card card) {
        if (card.getSuit() != suit) {
            throw new IllegalArgumentException("Cannot have " + card + " in foundation for suit " + suit + ".");
        }
    }
    
}
