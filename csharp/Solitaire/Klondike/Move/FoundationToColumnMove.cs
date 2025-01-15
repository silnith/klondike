using Silnith.Game.Deck;
using System;
using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move
{
    /// <summary>
    /// A move that takes a card from the foundation and puts it onto a column run.
    /// </summary>
    /// <remarks>
    /// <para>
    /// If the column is empty, only a <see cref="Value.King"/> may be
    /// moved.  If the column is not empty, then the card must adhere to the rules of
    /// a run.
    /// </para>
    /// </remarks>
    public class FoundationToColumnMove : ISolitaireMove, IEquatable<FoundationToColumnMove?>
    {
        /// <summary>
        /// The index in the board of the destination column for the card.
        /// </summary>
        public int DestinationColumn
        {
            get;
        }

        /// <summary>
        /// The card being moved.
        /// </summary>
        public Card Card
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
                return new List<Card>() { Card, };
            }
        }

        /// <summary>
        /// Constructs a move that takes a card from the foundation and puts it on top
        /// of a column.
        /// </summary>
        /// <param name="destinationColumn">The index of the column into the board.</param>
        /// <param name="card">The card being moved.</param>
        public FoundationToColumnMove(int destinationColumn, Card card)
        {
            DestinationColumn = destinationColumn;
            Card = card;
        }

        /// <summary>
        /// Constructs a move that takes a card from the foundation and puts it on top
        /// of a column.
        /// </summary>
        /// <param name="destinationColumn">The index of the column into the board.</param>
        /// <param name="suit">The suit of card to pull from the foundation.</param>
        /// <param name="board">The board to get the card from.</param>
        /// <exception cref="ArgumentOutOfRangeException">If the board foundation has no cards for <paramref name="suit"/>.</exception>
        public FoundationToColumnMove(int destinationColumn, Suit suit, Board board) : this(destinationColumn, board.GetTopOfFoundation(suit))
        {
        }

        /// <inheritdoc/>
        public Board Apply(Board board)
        {
            return board.MoveCardFromFoundation(Card.Suit, DestinationColumn);
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as FoundationToColumnMove);
        }

        /// <inheritdoc/>
        public bool Equals(FoundationToColumnMove? other)
        {
            return other != null
                && DestinationColumn == other.DestinationColumn
                && Card.Equals(other.Card);
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            return HashCode.Combine(DestinationColumn, Card);
        }

        /// <inheritdoc/>
        public override string ToString()
        {
            return $"Move {Card} from foundation to column {DestinationColumn}.";
        }
    }
}
