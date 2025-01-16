﻿using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Silnith.Game.Klondike.Move
{
    /// <summary>
    /// A move that takes a card from the stock pile and puts it onto a column run.
    /// </summary>
    /// <remarks>
    /// <para>
    /// If the column is empty, the card must be a <see cref="Silnith.Game.Deck.Value.King"/>.
    /// If the column is not empty, the card most follow the rules of a run.
    /// </para>
    /// </remarks>
    public class StockPileToColumnMove : ISolitaireMove, IEquatable<StockPileToColumnMove?>
    {
        /// <summary>
        /// The index into the stock pile from which the card is taken.
        /// </summary>
        public int SourceIndex
        {
            get;
        }

        /// <summary>
        /// The index into the board of the column to which the card is being moved.
        /// </summary>
        public int DestinationColumn
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
            get
            {
                return new List<Card>() { Card };
            }
        }

        /// <summary>
        /// Constructs a new move of a card from the stock pile to a column on the board.
        /// </summary>
        /// <param name="sourceIndex">The stock pile index of the card being moved.</param>
        /// <param name="destinationColumn">The index into the board of the destination column.</param>
        /// <param name="card">The card being moved.</param>
        public StockPileToColumnMove(int sourceIndex, int destinationColumn, Card card)
        {
            SourceIndex = sourceIndex;
            DestinationColumn = destinationColumn;
            Card = card;
        }

        /// <summary>
        /// Constructs a new move of a card from the stock pile to a column on the board.
        /// </summary>
        /// <param name="destinationColumn">The index into the board of the destination column.</param>
        /// <param name="board">The board from which to get the card.</param>
        /// <exception cref="ArgumentOutOfRangeException">If no card is available to be drawn from the board stock pile.</exception>
        public StockPileToColumnMove(int destinationColumn, Board board) : this(board.StockPileIndex, destinationColumn, board.GetStockPileCard())
        {
        }

        /// <inheritdoc/>
        public Board Apply(Board board)
        {
            Tuple<Card, IReadOnlyList<Card>> tuple = board.ExtractStockPileCard();
            Card card = tuple.Item1;
            IReadOnlyList<Card> newStockPile = tuple.Item2;

            int newStockPileIndex = board.StockPileIndex - 1;

            IReadOnlyList<Column> columns = board.Columns;
            IReadOnlyList<Column> newColumns = new List<Column>(columns)
            {
                [DestinationColumn] = columns[DestinationColumn].GetWithCard(card),
            };

            return new Board(newColumns, newStockPile, newStockPileIndex, board.Foundation);
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as StockPileToColumnMove);
        }

        /// <inheritdoc/>
        public bool Equals(StockPileToColumnMove? other)
        {
            return other != null
                && SourceIndex == other.SourceIndex
                && DestinationColumn == other.DestinationColumn
                && Card.Equals(other.Card);
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            return HashCode.Combine(SourceIndex, DestinationColumn, Card);
        }

        /// <inheritdoc/>
        public override string ToString()
        {
            return $"Move {Card} from stock pile index {SourceIndex} to column {DestinationColumn}.";
        }
    }
}
