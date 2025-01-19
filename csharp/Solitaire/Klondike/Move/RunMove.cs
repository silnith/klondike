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
        /// Finds all moves for a given board where a run is moved from one column to another.
        /// </summary>
        /// <remarks>
        /// <para>
        /// </para>
        /// </remarks>
        /// <param name="board">The board to examine.</param>
        /// <returns>An enumerable of moves.</returns>
        public static IEnumerable<RunMove> FindMoves(Board board)
        {
            List<RunMove> moves = new List<RunMove>();
            for (int i = 0; i < board.Columns.Count; i++)
            {
                Column sourceColumn = board.Columns[i];
                if (!sourceColumn.HasFaceUpCards())
                {
                    continue;
                }
                IReadOnlyList<Card> sourceFaceUpCards = sourceColumn.FaceUp;
                int sourceRunCount = sourceFaceUpCards.Count;
                Card sourceTopCard = sourceFaceUpCards[sourceRunCount - 1];
                Card sourceBottomCard = sourceFaceUpCards[0];
                int sourceRunMinimumValue = sourceTopCard.Value.GetValue();
                int sourceRunMaximumValue = sourceBottomCard.Value.GetValue();
                for (int j = 0; j < board.Columns.Count; j++)
                {
                    if (i == 0)
                    {
                        // Cannot move from a column to itself.
                        continue;
                    }

                    Column destinationColumn = board.Columns[j];
                    if (destinationColumn.HasFaceUpCards())
                    {
                        // Destination has cards already.
                        Card destinationTopCard = destinationColumn.GetTopCard();
                        int neededRunStartValue = destinationTopCard.Value.GetValue() - 1;

                        if (sourceRunMinimumValue <= neededRunStartValue && sourceRunMaximumValue >= neededRunStartValue)
                        {
                            int runCount = neededRunStartValue - sourceRunMinimumValue + 1;

                            IReadOnlyList<Card> runToMove = sourceColumn.GetTopCards(runCount);
                            if (destinationTopCard.Suit.GetColor() != runToMove[0].Suit.GetColor())
                            {
                                moves.Add(new RunMove(i, j, runCount, runToMove));
                            }
                        }
                    }
                    else
                    {
                        // Destination is empty, only a King may be moved to it.
                        if (sourceBottomCard.Value == Value.King)
                        {
                            moves.Add(new RunMove(i, j, sourceRunCount, sourceFaceUpCards));
                        }
                    }
                }
            }
            return moves;
        }

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
            Tuple<IReadOnlyList<Card>, Column> tuple = fromColumn.ExtractRun(CardCount);
            IReadOnlyList<Card> run = tuple.Item1;
            Column newFromColumn = tuple.Item2;
            Column newToColumn = toColumn.WithCards(run);

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
            HashCode hashCode = new HashCode();
            hashCode.Add(SourceColumn);
            hashCode.Add(DestinationColumn);
            hashCode.Add(CardCount);
            foreach (Card card in Cards)
            {
                hashCode.Add(card);
            }
            return hashCode.ToHashCode();
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
