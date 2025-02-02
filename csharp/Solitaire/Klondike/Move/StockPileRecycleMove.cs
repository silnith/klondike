using Silnith.Game.Deck;
using System;
using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move
{
    /// <summary>
    /// A move that recycles the stock pile.  This sets the current index into the
    /// stock pile back to zero.
    /// </summary>
    public class StockPileRecycleMove : ISolitaireMove, IEquatable<StockPileRecycleMove?>
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
        public static IEnumerable<StockPileRecycleMove> FindMoves(Board board)
        {
            if (board.CanRecycleStockPile())
            {
                return new List<StockPileRecycleMove>(1)
                {
                    new StockPileRecycleMove(board),
                };
            }
            else
            {
                return Array.Empty<StockPileRecycleMove>();
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
        public bool HasCards => false;

        /// <inheritdoc/>
        public IReadOnlyList<Card> Cards
        {
            get;
        }

        /// <inheritdoc/>
        public bool IsStockPileModification => true;

        /// <inheritdoc/>
        public bool IsFromStockPile => false;

        /// <inheritdoc/>
        public bool IsFromFoundation => false;

        /// <inheritdoc/>
        public bool IsFromColumn => false;

        /// <inheritdoc/>
        public bool TakesFromColumn(int columnIndex) => false;

        /// <inheritdoc/>
        public int FromColumnIndex => throw new ArgumentException("Not a move from a column.");

        /// <inheritdoc/>
        public bool IsToFoundation => false;

        /// <inheritdoc/>
        public bool IsToColumn => false;

        /// <inheritdoc/>
        public bool AddsToColumn(int columnIndex) => false;

        /// <inheritdoc/>
        public int ToColumnIndex => throw new ArgumentException("Not a move to a column.");

        /// <summary>
        /// Constructs a new move that recycles the stock pile.
        /// </summary>
        /// <param name="sourceIndex">The index into the stock pile before the move.</param>
        public StockPileRecycleMove(int sourceIndex)
        {
            SourceIndex = sourceIndex;
            Cards = Array.Empty<Card>();
        }

        /// <summary>
        /// Constructs a new move that recycles the stock pile.
        /// </summary>
        /// <param name="board">The board from which to get the stock pile index.</param>
        public StockPileRecycleMove(Board board) : this(board.StockPileIndex)
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
            return Equals(obj as StockPileRecycleMove);
        }

        /// <inheritdoc/>
        public bool Equals(StockPileRecycleMove? other)
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

