using Silnith.Game.Deck;
using System;
using System.Collections.Generic;

namespace Silnith.Game.Klondike
{
    /// <summary>
    /// Validates that the foundation conforms to the rules of Klondike solitaire.
    /// </summary>
    /// <remarks>
    /// <para>
    /// The foundation is one stack for each <see cref="Suit"/>.  The stack for each suit
    /// must start with the <see cref="Value.Ace"/> and count up incrementally.
    /// </para>
    /// </remarks>
    public class FoundationValidator : IValidator<IReadOnlyDictionary<Suit, IReadOnlyList<Card>>>
    {
        private readonly IEnumerable<Suit> AllSuits = new List<Suit>()
        {
            Suit.Club,
            Suit.Diamond,
            Suit.Heart,
            Suit.Spade,
        };

        /// <inheritdoc/>
        public void Validate(IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation)
        {
            foreach (Suit suit in AllSuits)
            {
                if (foundation.TryGetValue(suit, out IReadOnlyList<Card> cardsForSuit))
                {
                    IEnumerator<Card> enumerator = cardsForSuit.GetEnumerator();
                    if (enumerator.MoveNext())
                    {
                        // there is something, check it
                        Card previousCard = enumerator.Current;
                        ValidateSuit(suit, previousCard);
                        if (previousCard.Value != Value.Ace)
                        {
                            throw new ArgumentException("First card in foundation for suit " + suit + " must be " + Value.Ace + " instead it is " + previousCard + ".");
                        }
                        while (enumerator.MoveNext())
                        {
                            Card currentCard = enumerator.Current;
                            ValidateSuit(suit, currentCard);

                            if (previousCard.Value.GetValue() + 1 != currentCard.Value.GetValue())
                            {
                                throw new ArgumentException("Cannot put " + currentCard + " on top of " + previousCard + " in foundation for suit " + suit + ".");
                            }

                            previousCard = currentCard;
                        }
                    }
                }
                else
                {
                    throw new ArgumentException("No foundation entry for " + suit);
                }
            }
        }

        private void ValidateSuit(Suit suit, Card card)
        {
            if (card.Suit != suit)
            {
                throw new ArgumentException("Cannot have " + card + " in foundation for suit " + suit + ".");
            }
        }
    }
}
