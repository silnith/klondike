using Silnith.Game.Deck;
using System;
using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move
{
    /// <summary>
    /// A move that advances the stock pile by an increment.
    /// </summary>
    /// <remarks>
    /// <para>
    /// In a typical game, the advance is three unless there are fewer than
    /// three cards left in the stock pile.
    /// </para>
    /// </remarks>
    public class StockPileAdvanceMove : ISolitaireMove, IEquatable<StockPileAdvanceMove?>
    {
        /// <summary>
        /// Finds all stock pile advance moves for a given board.
        /// </summary>
        /// <remarks>
        /// <para>
        /// This will either contain one move or zero.
        /// </para>
        /// </remarks>
        /// <param name="stockPileAdvance">The number of cards to advance the stock pile.</param>
        /// <param name="board">The board to examine.</param>
        /// <returns>An enumerable of moves.</returns>
        public static IEnumerable<StockPileAdvanceMove> FindMoves(int stockPileAdvance, Board board)
        {
            if (board.CanAdvanceStockPile())
            {
                return new List<StockPileAdvanceMove>(1)
                {
                    new StockPileAdvanceMove(stockPileAdvance, board),
                };
            }
            else
            {
                return Array.Empty<StockPileAdvanceMove>();
            }
        }

        /// <summary>
        /// The index into the stock pile before the advance move happens.
        /// </summary>
        public int BeginningIndex
        {
            get;
        }

        /// <summary>
        /// The number of cards that the move advances the stock pile index.
        /// </summary>
        /// <remarks>
        /// <para>
        /// If the advanced index needs to be clamped, that happens when
        /// the move is applied.
        /// </para>
        /// </remarks>
        public int Increment
        {
            get;
        }

        /// <inheritdoc/>
        public IReadOnlyList<Card> Cards
        {
            get;
        }

        /// <inheritdoc/>
        public int SourceColumnIndex => throw new ArgumentException("Not a move from a column.");

        /// <inheritdoc/>
        public int DestinationColumnIndex => throw new ArgumentException("Not a move to a column.");

        /// <inheritdoc/>
        public bool HasCards => false;

        /// <inheritdoc/>
        public bool IsStockPileModification => true;

        /// <inheritdoc/>
        public bool IsFromStockPile => false;

        /// <inheritdoc/>
        public bool IsFromFoundation => false;

        /// <inheritdoc/>
        public bool IsFromColumn => false;

        /// <inheritdoc/>
        public bool IsToFoundation => false;

        /// <inheritdoc/>
        public bool IsToColumn => false;

        /// <summary>
        /// Creates a new move that advances the stock pile index.
        /// </summary>
        /// <param name="beginningIndex">The stock pile index before the move is applied.</param>
        /// <param name="increment">The number of cards that the stock pile index advances.</param>
        /// <exception cref="ArgumentOutOfRangeException">If <paramref name="increment"/> is not positive.</exception>
        public StockPileAdvanceMove(int beginningIndex, int increment)
        {
            if (increment < 1)
            {
                throw new ArgumentOutOfRangeException(nameof(increment), "Must be positive.");
            }

            BeginningIndex = beginningIndex;
            Increment = increment;
            Cards = Array.Empty<Card>();
        }

        /// <summary>
        /// Creates a new move that advances the stock pile index.
        /// </summary>
        /// <param name="increment">The number of cards that the stock pile index advances.</param>
        /// <param name="board">The board from which to take the beginning index.</param>
        public StockPileAdvanceMove(int increment, Board board) : this(board.StockPileIndex, increment)
        {
        }

        /// <summary>
        /// Returns a new move that combines this stock pile advance with another
        /// stock pile advance.  The returned move is the sum of both advances,
        /// as if only one advance occurred.
        /// </summary>
        /// <param name="next">The move to combine with this move.</param>
        /// <returns>A single move that can replace both moves.</returns>
        public StockPileAdvanceMove Coalesce(StockPileAdvanceMove next)
        {
            return new StockPileAdvanceMove(BeginningIndex, Increment + next.Increment);
        }

        /// <inheritdoc/>
        public bool TakesFromColumn(int columnIndex) => false;

        /// <inheritdoc/>
        public bool AddsToColumn(int columnIndex) => false;

        /// <inheritdoc/>
        public Board Apply(Board board)
        {
            IReadOnlyList<Card> stockPile = board.StockPile;
            int newIndex = Math.Min(board.StockPileIndex + Increment, stockPile.Count);
            return new Board(board.Columns, stockPile, newIndex, board.Foundation);
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as StockPileAdvanceMove);
        }

        /// <inheritdoc/>
        public bool Equals(StockPileAdvanceMove? other)
        {
            return other != null
                && BeginningIndex == other.BeginningIndex
                && Increment == other.Increment;
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            return HashCode.Combine(BeginningIndex, Increment);
        }

        /// <inheritdoc/>
        public override string ToString()
        {
            return $"Advance stock pile from {BeginningIndex} by {Increment}.";
        }
    }
}
