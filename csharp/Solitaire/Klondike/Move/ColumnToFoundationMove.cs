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
        /// Finds all moves of a card from a column run to the foundation.
        /// </summary>
        /// <remarks>
        /// <para>
        /// This could potentially find a move for each suit.
        /// </para>
        /// </remarks>
        /// <param name="board">The board to examine.</param>
        /// <returns>An enumerable of moves.</returns>
        public static IEnumerable<ColumnToFoundationMove> FindMoves(Board board)
        {
            List<ColumnToFoundationMove> moves = new List<ColumnToFoundationMove>();
            for (int i = 0; i < board.Columns.Count; i++)
            {
                Column column = board.Columns[i];
                if (column.HasFaceUpCards())
                {
                    Card card = column.GetTopCard();
                    if (board.CanAddToFoundation(card))
                    {
                        moves.Add(new ColumnToFoundationMove(i, board));
                    }
                }
            }
            return moves;
        }

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
        public bool HasCards => true;

        /// <inheritdoc/>
        public IReadOnlyList<Card> Cards
        {
            get;
        }

        /// <inheritdoc/>
        public bool IsStockPileModification => false;

        /// <inheritdoc/>
        public bool IsFromStockPile => false;

        /// <inheritdoc/>
        public bool IsFromFoundation => false;

        /// <inheritdoc/>
        public bool IsFromColumn => true;

        /// <inheritdoc/>
        public bool TakesFromColumn(int columnIndex) => columnIndex == FromColumnIndex;

        /// <inheritdoc/>
        public int FromColumnIndex => SourceColumn;

        /// <inheritdoc/>
        public bool IsToFoundation => true;

        /// <inheritdoc/>
        public bool IsToColumn => false;

        /// <inheritdoc/>
        public bool AddsToColumn(int columnIndex) => false;

        /// <inheritdoc/>
        public int ToColumnIndex => throw new ArgumentException("Not a move to a column.");

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
            Cards = new List<Card>()
            {
                card,
            };
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
            Tuple<Card, Column> tuple = columns[SourceColumn].ExtractCard();
            Card card = tuple.Item1;
            Column newColumn = tuple.Item2;

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
