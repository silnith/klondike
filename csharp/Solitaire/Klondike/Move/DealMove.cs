using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike.Move
{
    /// <summary>
    /// A move that deals a fresh deck of cards into a Klondike solitaire board.
    /// </summary>
    public class DealMove : ISolitaireMove, IEquatable<DealMove?>
    {
        /// <summary>
        /// The number of columns in the board.  In a typical game, this will be <c>7</c>.
        /// </summary>
        public int ColumnCount
        {
            get;
        }

        /// <summary>
        /// The deck of cards to deal.
        /// </summary>
        public IReadOnlyList<Card> Deck
        {
            get;
        }

        /// <inheritdoc/>
        public bool HasCards
        {
            get
            {
                return true;
            }
        }

        /// <inheritdoc/>
        public IReadOnlyList<Card> Cards
        {
            get
            {
                return Deck;
            }
        }

        /// <summary>
        /// Constructs a new move that deals a fresh deck of cards.
        /// </summary>
        /// <param name="deck">The deck of cards to deal.</param>
        /// <param name="columnCount">The number of columns on the board.  This is always <c>7</c>.</param>
        public DealMove(IReadOnlyList<Card> deck, int columnCount)
        {
            ColumnCount = columnCount;
            Deck = deck;
        }

        /// <inheritdoc/>
        public Board Apply(Board board)
        {
            return new Board(Deck, ColumnCount);
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as DealMove);
        }

        /// <inheritdoc/>
        public bool Equals(DealMove? other)
        {
            return other != null
                && ColumnCount == other.ColumnCount
                && Deck.SequenceEqual(other.Deck);
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            HashCode hashCode = new HashCode();
            hashCode.Add(ColumnCount);
            foreach (Card card in Deck)
            {
                hashCode.Add(card);
            }
            return hashCode.ToHashCode();
        }

        /// <inheritdoc/>
        public override string ToString()
        {
            return "Deal " + ColumnCount + " columns using deck [" + string.Join(", ", Deck) + "].";
        }
    }
}
