using System;

namespace Silnith.Game.Deck
{
    /// <summary>
    /// A playing card.
    /// </summary>
    public class Card : IEquatable<Card>
    {
        /// <summary>
        /// The card value.
        /// </summary>
        public Value Value
        {
            get;
        }

        /// <summary>
        /// The card suit.
        /// </summary>
        public Suit Suit
        {
            get;
        }

        /// <summary>
        /// The card color.
        /// </summary>
        public Color Color => Suit.GetColor();

        /// <summary>
        /// Constructs a new playing card with the given value and suit.
        /// </summary>
        /// <param name="value">The value for the card.</param>
        /// <param name="suit">The suit for the card.</param>
        public Card(Value value, Suit suit)
        {
            Value = value;
            Suit = suit;
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as Card);
        }

        /// <inheritdoc/>
        public bool Equals(Card? other)
        {
            return other != null &&
                   Value == other.Value &&
                   Suit == other.Suit;
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            return HashCode.Combine(Value, Suit);
        }

        /// <inheritdoc/>
        public override string ToString()
        {
            return Value + " of " + Suit + "s";
        }
    }
}
