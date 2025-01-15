using Silnith.Game.Deck;
using System;
using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move
{
    /// <summary>
    /// A move that recycles the stock pile.  This sets the current index into the
    /// stock pile back to zero.
    /// </summary>
    public class RecycleStockPileMove : ISolitaireMove, IEquatable<RecycleStockPileMove?>
    {
        /// <summary>
        /// The index into the stock pile before the move is applied.
        /// </summary>
        /// <remarks>
        /// <para>
        /// For a legal move, this will be equal to the size of the stock pile.
        /// </para>
        /// </remarks>
        public int SourceIndex
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
        /// Constructs a new move that recycles the stock pile.
        /// </summary>
        /// <param name="sourceIndex">The index into the stock pile before the move.</param>
        public RecycleStockPileMove(int sourceIndex)
        {
            SourceIndex = sourceIndex;
        }

        /// <inheritdoc/>
        public Board Apply(Board board)
        {
            return board.RecycleStockPile();
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as RecycleStockPileMove);
        }

        /// <inheritdoc/>
        public bool Equals(RecycleStockPileMove? other)
        {
            return other != null &&
                   SourceIndex == other.SourceIndex;
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            return HashCode.Combine(SourceIndex);
        }

        /// <inheritdoc/>
        public override string ToString()
        {
            return $"Recycle stock pile from index {SourceIndex} to the beginning.";
        }
    }
}

