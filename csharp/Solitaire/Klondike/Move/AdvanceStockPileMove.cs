using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Text;

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
    public class AdvanceStockPileMove : ISolitaireMove, IEquatable<AdvanceStockPileMove?>
    {
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
        public bool HasCards
        {
            get
            {
                return false;
            }
        }

        /// <inheritdoc/>
        public IReadOnlyList<Card> Cards
        {
            get
            {
                throw new NotImplementedException();
            }
        }

        /// <summary>
        /// Creates a new move that advances the stock pile index.
        /// </summary>
        /// <param name="beginningIndex">The stock pile index before the move is applied.</param>
        /// <param name="increment">The number of cards that the stock pile index advances.</param>
        /// <exception cref="ArgumentOutOfRangeException">If <paramref name="increment"/> is not positive.</exception>
        public AdvanceStockPileMove(int beginningIndex, int increment)
        {
            if (increment < 1)
            {
                throw new ArgumentOutOfRangeException(nameof(increment), "Must be positive.");
            }

            BeginningIndex = beginningIndex;
            Increment = increment;
        }

        /// <summary>
        /// Creates a new move that advances the stock pile index.
        /// </summary>
        /// <param name="increment">The number of cards that the stock pile index advances.</param>
        /// <param name="board">The board from which to take the beginning index.</param>
        public AdvanceStockPileMove(int increment, Board board) : this(board.StockPileIndex, increment)
        {
        }

        /// <summary>
        /// Returns a new move that combines this stock pile advance with another
        /// stock pile advance.  The returned move is the sum of both advances,
        /// as if only one advance occurred.
        /// </summary>
        /// <param name="next">The move to combine with this move.</param>
        /// <returns>A single move that can replace both moves.</returns>
        public AdvanceStockPileMove Coalesce(AdvanceStockPileMove next)
        {
            return new AdvanceStockPileMove(BeginningIndex, Increment + next.Increment);
        }

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
            return Equals(obj as AdvanceStockPileMove);
        }

        /// <inheritdoc/>
        public bool Equals(AdvanceStockPileMove? other)
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
