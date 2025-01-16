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
        /// Finds all recycle stock pile moves for a given board.
        /// </summary>
        /// <remarks>
        /// <para>
        /// This will either contain one move or zero.
        /// </para>
        /// </remarks>
        /// <param name="board">The board to examine.</param>
        /// <returns>An enumerable of moves.</returns>
        public static IEnumerable<ISolitaireMove> FindAllMovesForBoard(Board board)
        {
            if (board.CanRecycleStockPile())
            {
                return new List<ISolitaireMove>(1)
                {
                    new RecycleStockPileMove(board),
                };
            }
            else
            {
                return Array.Empty<ISolitaireMove>();
            }
        }

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

        /// <summary>
        /// Constructs a new move that recycles the stock pile.
        /// </summary>
        /// <param name="board">The board from which to get the stock pile index.</param>
        public RecycleStockPileMove(Board board) : this(board.StockPileIndex)
        {
        }

        /// <inheritdoc/>
        public Board Apply(Board board)
        {
            return new Board(board.Columns, board.StockPile, 0, board.Foundation);
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

