using Silnith.Game.Deck;
using System;
using System.Collections.Generic;

namespace Silnith.Game.Klondike
{
    /// <summary>
    /// Validates that a run of cards conforms to the rules of Klondike solitaire.
    /// </summary>
    public class RunValidator : IValidator<IEnumerable<Card>>
    {
        /// <inheritdoc/>
        public void Validate(IEnumerable<Card> run)
        {
            IEnumerator<Card> enumerator = run.GetEnumerator();
            bool hasFirstElement = enumerator.MoveNext();
            if (!hasFirstElement)
            {
                return;
            }
            Card previousCard = enumerator.Current;
            while (enumerator.MoveNext())
            {
                Card currentCard = enumerator.Current;

                if (currentCard.Value.GetValue() + 1 != previousCard.Value.GetValue())
                {
                    throw new ArgumentException("Cannot stack " + currentCard.Value + " on top of " + previousCard.Value + ".");
                }
                if (currentCard.Suit.GetColor() == previousCard.Suit.GetColor())
                {
                    throw new ArgumentException("Cannot stack " + currentCard.Suit + " on top of " + previousCard.Suit + ".");
                }

                previousCard = currentCard;
            }
        }
    }
}
