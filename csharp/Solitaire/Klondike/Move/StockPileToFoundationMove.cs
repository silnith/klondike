using Silnith.Game.Deck;
using System;
using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move
{
    /// <summary>
    /// A move that takes a card from the stock pile and puts it into the foundation.
    /// </summary>
    public class StockPileToFoundationMove : ISolitaireMove, IEquatable<StockPileToFoundationMove?>
    {
        /// <summary>
        /// Finds all moves where a card is drawn from the stock pile to the foundation.
        /// </summary>
        /// <remarks>
        /// <para>
        /// This will either contain one move or zero.
        /// </para>
        /// </remarks>
        /// <param name="board">The board to examine.</param>
        /// <returns>An enumerable of moves.</returns>
        public static IEnumerable<StockPileToFoundationMove> FindMoves(Board board)
        {
            if (board.StockPileIndex > 0)
            {
                Card card = board.GetStockPileCard();
                if (board.CanAddToFoundation(card))
                {
                    return new List<StockPileToFoundationMove>(1)
                    {
                        new StockPileToFoundationMove(board),
                    };
                }
            }

            return Array.Empty<StockPileToFoundationMove>();
        }

        /// <summary>
        /// The index into the stock pile from which the card is being taken.
        /// </summary>
        public int SourceIndex
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
        public bool IsStockPileModification => true;

        /// <inheritdoc/>
        public bool IsFromStockPile => true;

        /// <inheritdoc/>
        public bool IsFromFoundation => false;

        /// <inheritdoc/>
        public bool IsFromColumn => false;

        /// <inheritdoc/>
        public bool TakesFromColumn(int columnIndex) => false;

        /// <inheritdoc/>
        public int FromColumnIndex => throw new ArgumentException("Not a move from a column.");

        /// <inheritdoc/>
        public bool IsToFoundation => true;

        /// <inheritdoc/>
        public bool IsToColumn => false;

        /// <inheritdoc/>
        public bool AddsToColumn(int columnIndex) => false;

        /// <inheritdoc/>
        public int ToColumnIndex => throw new ArgumentException("Not a move to a column.");

        /// <summary>
        /// Constructs a new move that takes a card from the stock pile and puts it into
        /// the foundation.
        /// </summary>
        /// <param name="sourceIndex">The index into the stock pile of the card being moved.</param>
        /// <param name="card">The card being moved.</param>
        public StockPileToFoundationMove(int sourceIndex, Card card)
        {
            SourceIndex = sourceIndex;
            Card = card ?? throw new ArgumentNullException(nameof(card));
            Cards = new List<Card>()
            {
                card,
            };
        }

        /// <summary>
        /// Constructs a new move that takes a card from the stock pile and puts it into
        /// the foundation.
        /// </summary>
        /// <remarks>
        /// <para>
        /// This takes the parameters from the current state of <paramref name="board"/>.
        /// </para>
        /// </remarks>
        /// <param name="board">The board from which to get the <see cref="SourceIndex"/> and <see cref="Card"/>.</param>
        /// <exception cref="IndexOutOfRangeException">If no card is available to be drawn from the stock pile.</exception>
        public StockPileToFoundationMove(Board board) : this(board.StockPileIndex, board.GetStockPileCard())
        {
        }

        /// <inheritdoc/>
        public Board Apply(Board board)
        {
            Tuple<Card, IReadOnlyList<Card>> tuple = board.ExtractStockPileCard();
            Card card = tuple.Item1;
            IReadOnlyList<Card> newStockPile = tuple.Item2;

            int newStockPileIndex = board.StockPileIndex - 1;

            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> newFoundation = board.GetFoundationPlusCard(card);

            return new Board(board.Columns, newStockPile, newStockPileIndex, newFoundation);
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as StockPileToFoundationMove);
        }

        /// <inheritdoc/>
        public bool Equals(StockPileToFoundationMove? other)
        {
            return other != null
                && SourceIndex == other.SourceIndex
                && Card.Equals(other.Card);
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            return HashCode.Combine(SourceIndex, Card);
        }

        /// <inheritdoc/>
        public override string ToString()
        {
            return $"Move {Card} from stock pile {SourceIndex} to foundation.";
        }
    }
}
