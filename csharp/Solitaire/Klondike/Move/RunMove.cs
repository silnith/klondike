using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike.Move
{
    /// <summary>
    /// A move that takes a run of cards from one colum and puts it on top of
    /// another run on a different column.
    /// </summary>
    /// <remarks>
    /// <para>
    /// A run can consists of a single card, or it can consist of many cards.
    /// </para>
    /// </remarks>
    public class RunMove : ISolitaireMove, IEquatable<RunMove?>
    {
        /// <summary>
        /// The index into the board of the source column.
        /// </summary>
        public int SourceColumn
        {
            get;
        }

        /// <summary>
        /// The index into the board of the destination column.
        /// </summary>
        public int DestinationColumn
        {
            get;
        }

        /// <summary>
        /// The number of cards being moved.
        /// </summary>
        public int CardCount
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
            get;
        }

        /// <summary>
        /// Constructs a new move of a run of cards from one column to another.
        /// </summary>
        /// <param name="sourceColumn">The index into the board of the source column.</param>
        /// <param name="destinationColumn">The index into the board of the destination column.</param>
        /// <param name="cardCount">The number of cards being moved.</param>
        /// <param name="cards">The cards being moved.</param>
        /// <exception cref="ArgumentException">If <paramref name="sourceColumn"/> equals <paramref name="destinationColumn"/>.</exception>
        public RunMove(int sourceColumn, int destinationColumn, int cardCount, IReadOnlyList<Card> cards)
        {
            if (sourceColumn == destinationColumn)
            {
                throw new ArgumentException("Source and destination column are the same.");
            }

            SourceColumn = sourceColumn;
            DestinationColumn = destinationColumn;
            CardCount = cardCount;
            Cards = cards;
        }

        /// <summary>
        /// Constructs a new move of a run of cards from one column to another.
        /// </summary>
        /// <param name="sourceColumn">The index into the board of the source column.</param>
        /// <param name="destinationColumn">The index into the board of the destination column.</param>
        /// <param name="cardCount">The number of cards being moved.</param>
        /// <param name="board">The board from which to get the cards being moved.</param>
        /// <exception cref="ArgumentOutOfRangeException">If the number of cards is less than <c>1</c>,
        /// or exceeds the available cards, or the source column is out of bounds.</exception>
        public RunMove(int sourceColumn, int destinationColumn, int cardCount, Board board)
            : this(sourceColumn, destinationColumn, cardCount, board.Columns[sourceColumn].GetTopCards(cardCount))
        {
        }

        /// <inheritdoc/>
        public Board Apply(Board board)
        {
            Column fromColumn = board.Columns[SourceColumn];
            Column toColumn = board.Columns[DestinationColumn];
            IReadOnlyList<Card> run = fromColumn.GetTopCards(CardCount);
            Column newFromColumn = fromColumn.GetColumnMissingTopCards(CardCount);
            Column newToColumn = toColumn.AddNewCards(run);

            List<Column> newColumns = new List<Column>(board.Columns)
            {
                [SourceColumn] = newFromColumn,
                [DestinationColumn] = newToColumn,
            };
            return new Board(newColumns, board.StockPile, board.StockPileIndex, board.Foundation);
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as RunMove);
        }

        /// <inheritdoc/>
        public bool Equals(RunMove? other)
        {
            return other != null
                && SourceColumn == other.SourceColumn
                && DestinationColumn == other.DestinationColumn
                && CardCount == other.CardCount
                && Cards.SequenceEqual(other.Cards);
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            return HashCode.Combine(SourceColumn, DestinationColumn, CardCount, Cards);
        }

        /// <inheritdoc/>
        public override string ToString()
        {
            if (CardCount == 1)
            {
                return $"Move {Cards[0]} from column {SourceColumn} to column {DestinationColumn}.";
            }
            else
            {
                return $"Move run of [{string.Join(", ", Cards)}] from column {SourceColumn} to column {DestinationColumn}.";
            }
        }
    }
}
