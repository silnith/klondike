using Silnith.Game.Deck;
using System;
using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move
{
    /// <summary>
    /// A move that takes a face-up card from a column and puts it into the
    /// foundation.
    /// </summary>
    public class ColumnToFoundationMove : ISolitaireMove, IEquatable<ColumnToFoundationMove?>
    {
        /// <summary>
        /// The index in the board of the column from which the card is taken.
        /// </summary>
        public int SourceColumn
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
                return new List<Card>() { Card };
            }
        }

        /// <summary>
        /// Constructs a new move that takes a face-up card from a column and puts it
        /// into the foundation.
        /// </summary>
        /// <param name="sourceColumn">The index into the board of the column from which the card is taken.</param>
        /// <param name="card">The card being moved.</param>
        public ColumnToFoundationMove(int sourceColumn, Card card)
        {
            SourceColumn = sourceColumn;
            Card = card;
        }

        /// <summary>
        /// Constructs a new move that takes a face-up card from a column and puts it
        /// into the foundation.
        /// </summary>
        /// <param name="sourceColumn">The index into the board of the column from which the card is taken.</param>
        /// <param name="board">The board containing the card to move.</param>
        /// <exception cref="ArgumentOutOfRangeException">If <paramref name="sourceColumn"/> is out of range,
        /// or the source column is empty.</exception>
        public ColumnToFoundationMove(int sourceColumn, Board board) : this(sourceColumn, board.Columns[sourceColumn].GetTopCard())
        {
        }

        /// <inheritdoc/>
        public Board Apply(Board board)
        {
            IReadOnlyList<Column> columns = board.Columns;
            Column column = columns[SourceColumn];
            Card card = column.GetTopCard();
            Column newColumn = column.GetColumnMissingTopCards(1);

            IReadOnlyList<Column> newColumns = new List<Column>(columns)
            {
                [SourceColumn] = newColumn,
            };

            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> newFoundation = board.GetFoundationPlusCard(card);

            return new Board(newColumns, board.StockPile, board.StockPileIndex, newFoundation);
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as ColumnToFoundationMove);
        }

        /// <inheritdoc/>
        public bool Equals(ColumnToFoundationMove? other)
        {
            return other != null
                && SourceColumn == other.SourceColumn
                && Card.Equals(other.Card);
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            return HashCode.Combine(SourceColumn, Card);
        }

        /// <inheritdoc/>
        public override string ToString()
        {
            return $"Move {Card} from column {SourceColumn} to foundation.";
        }
    }
}
