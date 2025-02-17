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
    public class ColumnToColumnMove : ISolitaireMove, IEquatable<ColumnToColumnMove?>
    {
        /// <summary>
        /// Finds all moves for a given board where a run is moved from one column to another.
        /// </summary>
        /// <param name="board">The board to examine.</param>
        /// <returns>An enumerable of moves.</returns>
        public static IEnumerable<ColumnToColumnMove> FindMoves(Board board)
        {
            List<ColumnToColumnMove> moves = new List<ColumnToColumnMove>();
            for (int sourceIndex = 0; sourceIndex < board.Columns.Count; sourceIndex++)
            {
                Column sourceColumn = board.Columns[sourceIndex];
                if (!sourceColumn.HasFaceUpCards())
                {
                    continue;
                }
                IReadOnlyList<Card> sourceRun = sourceColumn.FaceUp;
                int sourceRunCount = sourceRun.Count;
                Card sourceTopCard = sourceRun[sourceRunCount - 1];
                Card sourceBottomCard = sourceRun[0];
                int sourceRunMinValue = sourceTopCard.Value.GetValue();
                int sourceRunMaxValue = sourceBottomCard.Value.GetValue();
                for (int destinationIndex = 0; destinationIndex < board.Columns.Count; destinationIndex++)
                {
                    if (sourceIndex == 0)
                    {
                        // Cannot move from a column to itself.
                        continue;
                    }

                    Column destinationColumn = board.Columns[destinationIndex];
                    if (destinationColumn.HasFaceUpCards())
                    {
                        // Destination has cards already.
                        Card destinationTopCard = destinationColumn.GetTopCard();
                        int runStartValue = destinationTopCard.Value.GetValue() - 1;

                        if (sourceRunMinValue <= runStartValue && sourceRunMaxValue >= runStartValue)
                        {
                            int runCount = runStartValue - sourceRunMinValue + 1;

                            IReadOnlyList<Card> run = sourceColumn.GetTopCards(runCount);
                            if (destinationTopCard.Color != run[0].Color)
                            {
                                moves.Add(new ColumnToColumnMove(sourceIndex, destinationIndex, run));
                            }
                        }
                    }
                    else
                    {
                        // Destination is empty, only a King may be moved to it.
                        if (sourceBottomCard.Value == Value.King)
                        {
                            moves.Add(new ColumnToColumnMove(sourceIndex, destinationIndex, sourceRun));
                        }
                    }
                }
            }
            return moves;
        }

        /// <inheritdoc/>
        public IReadOnlyList<Card> Cards
        {
            get;
        }

        /// <inheritdoc/>
        public int SourceColumnIndex
        {
            get;
        }

        /// <inheritdoc/>
        public int DestinationColumnIndex
        {
            get;
        }

        /// <inheritdoc/>
        public bool HasCards => true;

        /// <inheritdoc/>
        public bool IsStockPileModification => false;

        /// <inheritdoc/>
        public bool IsStockPileAdvance => false;

        /// <inheritdoc/>
        public bool IsStockPileRecycle => false;

        /// <inheritdoc/>
        public bool IsFromStockPile => false;

        /// <inheritdoc/>
        public bool IsFromFoundation => false;

        /// <inheritdoc/>
        public bool IsFromColumn => true;

        /// <inheritdoc/>
        public bool IsToFoundation => false;

        /// <inheritdoc/>
        public bool IsToColumn => true;

        /// <summary>
        /// Constructs a new move of a run of cards from one column to another.
        /// </summary>
        /// <param name="sourceColumnIndex">The index into the board of the source column.</param>
        /// <param name="destinationColumnIndex">The index into the board of the destination column.</param>
        /// <param name="cards">The cards being moved.</param>
        /// 
        /// <exception cref="ArgumentException">If <paramref name="sourceColumnIndex"/> equals <paramref name="destinationColumnIndex"/>.</exception>
        public ColumnToColumnMove(int sourceColumnIndex, int destinationColumnIndex, IReadOnlyList<Card> cards)
        {
            if (sourceColumnIndex == destinationColumnIndex)
            {
                throw new ArgumentException("Source and destination column are the same.");
            }

            SourceColumnIndex = sourceColumnIndex;
            DestinationColumnIndex = destinationColumnIndex;
            Cards = cards;
        }

        /// <summary>
        /// Constructs a new move of a run of cards from one column to another.
        /// </summary>
        /// <param name="sourceColumnIndex">The index into the board of the source column.</param>
        /// <param name="destinationColumnIndex">The index into the board of the destination column.</param>
        /// <param name="cardCount">The number of cards being moved.</param>
        /// <param name="board">The board from which to get the cards being moved.</param>
        /// <exception cref="ArgumentOutOfRangeException">If the number of cards is less than <c>1</c>,
        /// or exceeds the available cards, or the source column is out of bounds.</exception>
        public ColumnToColumnMove(int sourceColumnIndex, int destinationColumnIndex, int cardCount, Board board)
            : this(sourceColumnIndex, destinationColumnIndex, board.Columns[sourceColumnIndex].GetTopCards(cardCount))
        {
        }

        /// <inheritdoc/>
        public bool TakesFromColumn(int columnIndex) => columnIndex == SourceColumnIndex;

        /// <inheritdoc/>
        public bool AddsToColumn(int columnIndex) => columnIndex == DestinationColumnIndex;

        /// <inheritdoc/>
        public Board Apply(Board board)
        {
            Column fromColumn = board.Columns[SourceColumnIndex];
            Column toColumn = board.Columns[DestinationColumnIndex];
            Tuple<IReadOnlyList<Card>, Column> tuple = fromColumn.ExtractRun(Cards.Count);
            IReadOnlyList<Card> run = tuple.Item1;
            Column newFromColumn = tuple.Item2;
            Column newToColumn = toColumn.WithCards(run);

            List<Column> newColumns = new List<Column>(board.Columns)
            {
                [SourceColumnIndex] = newFromColumn,
                [DestinationColumnIndex] = newToColumn,
            };
            return new Board(newColumns, board.StockPile, board.StockPileIndex, board.Foundation);
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as ColumnToColumnMove);
        }

        /// <inheritdoc/>
        public bool Equals(ColumnToColumnMove? other)
        {
            return other != null
                && SourceColumnIndex == other.SourceColumnIndex
                && DestinationColumnIndex == other.DestinationColumnIndex
                && Cards.SequenceEqual(other.Cards);
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            HashCode hashCode = new HashCode();
            hashCode.Add(SourceColumnIndex);
            hashCode.Add(DestinationColumnIndex);
            foreach (Card card in Cards)
            {
                hashCode.Add(card);
            }
            return hashCode.ToHashCode();
        }

        /// <inheritdoc/>
        public override string ToString()
        {
            if (Cards.Count == 1)
            {
                return $"Move {Cards[0]} from column {SourceColumnIndex} to column {DestinationColumnIndex}.";
            }
            else
            {
                return $"Move run of [{string.Join(", ", Cards)}] from column {SourceColumnIndex} to column {DestinationColumnIndex}.";
            }
        }
    }
}
