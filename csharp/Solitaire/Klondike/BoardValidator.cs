using Silnith.Game.Deck;
using System;
using System.Collections.Generic;

namespace Silnith.Game.Klondike
{
    /// <summary>
    /// A validator for the entire board in a game of Klondike solitaire.
    /// </summary>
    /// <remarks>
    /// <para>
    /// This validates that the foundation is properly ordered, that runs
    /// in each column conform to the rules, and that the total number of
    /// cards matches the expected size of a deck.
    /// </para>
    /// </remarks>
    public class BoardValidator : IValidator<Board>
    {
        private readonly int columnCount;
        private readonly int deckCount;
        private readonly ColumnValidator columnValidator;
        private readonly FoundationValidator foundationValidator;

        /// <summary>
        /// Constructs a new board validator.
        /// </summary>
        /// <param name="columnCount">The number of columns on the board.</param>
        /// <param name="deckCount">The number of cards in a complete deck.</param>
        /// <param name="columnValidator">A column validator.</param>
        /// <param name="foundationValidator">A foundation validator.</param>
        public BoardValidator(int columnCount, int deckCount, ColumnValidator columnValidator, FoundationValidator foundationValidator)
        {
            this.columnValidator = columnValidator ?? throw new ArgumentNullException(nameof(columnValidator));
            this.foundationValidator = foundationValidator ?? throw new ArgumentNullException(nameof(foundationValidator));
            this.deckCount = deckCount;
            this.columnCount = columnCount;
        }

        /// <inheritdoc/>
        public void Validate(Board board)
        {
            foundationValidator.Validate(board.Foundation);
            if (board.Columns.Count != columnCount)
            {
                throw new ArgumentException("Board must have " + columnCount + " columns, instead has " + board.Columns.Count + ".");
            }
            foreach (Column column in board.Columns)
            {
                columnValidator.Validate(column);
            }
            if (board.StockPileIndex < 0)
            {
                throw new ArgumentException("Stock pile index must be non-negative.");
            }
            if (board.StockPileIndex > board.StockPile.Count)
            {
                throw new ArgumentException("Stock pile index must not be larger than the stock pile size.  Stock pile has "
                    + board.StockPile.Count + " cards, stock pile index is " + board.StockPileIndex + ".");
            }
            int numCards = board.StockPile.Count;
            foreach (IReadOnlyList<Card> cards in board.Foundation.Values)
            {
                numCards += cards.Count;
            }
            foreach (Column column in board.Columns)
            {
                numCards += column.GetCountOfFaceDownCards();
                numCards += column.GetCountOfFaceUpCards();
            }
            if (numCards != deckCount)
            {
                throw new ArgumentException("Must have " + deckCount + " cards on the board, instead has " + numCards + ".");
            }
        }
    }
}
