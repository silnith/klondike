using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike
{
    /// <summary>
    /// The board for Klondike solitaire.
    /// </summary>
    /// <remarks>
    /// <para>
    /// The board consists of seven columns, the foundation, and the stock pile.
    /// </para>
    /// </remarks>
    public class Board : IEquatable<Board?>
    {
        /// <summary>
        /// The columns of the board.  Each column consists of a number of face-down cards,
        /// and a run of face-up cards.
        /// </summary>
        public IReadOnlyList<Column> Columns
        {
            get;
        }

        /// <summary>
        /// The stock pile.
        /// </summary>
        public IReadOnlyList<Card> StockPile
        {
            get;
        }

        /// <summary>
        /// The current index into the stock pile.
        /// This is <c>0</c> if the stock pile has not been advanced,
        /// meaning no cards are available to be drawn.
        /// When this reaches the size of the stock pile, it is
        /// eligible to be recycled.
        /// </summary>
        public int StockPileIndex
        {
            get;
        }

        /// <summary>
        /// The foundation.
        /// When all cards are in the foundation, the game is won.
        /// </summary>
        public IReadOnlyDictionary<Suit, IReadOnlyList<Card>> Foundation
        {
            get;
        }

        /// <summary>
        /// Deals a deck of cards into a new solitaire board.
        /// </summary>
        /// <param name="deck">The deck of cards to deal.</param>
        /// <param name="columnCount">The number of columns on the board.  In any standard game,
        /// this is <c>7</c>.</param>
        /// <exception cref="ArgumentException">If the deck does not have enough cards to fill
        /// all of the columns.  This could happen if the deck is partial, or if
        /// the number of columns is greater than <c>7</c>.</exception>
        public Board(IReadOnlyList<Card> deck, int columnCount)
        {
            int remaining = deck.Count;
            List<List<Card>> stacks = new List<List<Card>>(columnCount);
            for (int i = 0; i < columnCount; i++)
            {
                stacks.Add(new List<Card>(i + 1));
            }
            IEnumerator<Card> enumerator = deck.GetEnumerator();
            for (int i = 0; i < columnCount; i++)
            {
                for (int j = i; j < columnCount; j++)
                {
                    if (!enumerator.MoveNext())
                    {
                        throw new ArgumentException("Deck does not have enough cards to fill all columns.", nameof(deck));
                    }
                    remaining--;
                    stacks[j].Add(enumerator.Current);
                }
            }

            Columns = stacks.Select(stack => new Column(stack, null)).ToList();

            List<Card> tempStockPile = new List<Card>(remaining);
            while (enumerator.MoveNext())
            {
                tempStockPile.Add(enumerator.Current);
            }
            StockPile = tempStockPile;
            StockPileIndex = 0;

            Foundation = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                { Suit.Club, Array.Empty<Card>() },
                { Suit.Diamond, Array.Empty<Card>() },
                { Suit.Heart, Array.Empty<Card>() },
                { Suit.Spade, Array.Empty<Card>() },
            };
        }

        /// <summary>
        /// Constructs a new board.  All parameters should be immutable.
        /// </summary>
        /// <param name="columns">The columns.</param>
        /// <param name="stockPile">The stock pile.</param>
        /// <param name="stockPileIndex">The index into the stock pile.</param>
        /// <param name="foundation">The foundation.</param>
        public Board(IReadOnlyList<Column> columns, IReadOnlyList<Card> stockPile, int stockPileIndex, IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation)
        {
            if (stockPileIndex < 0)
            {
                throw new ArgumentOutOfRangeException(nameof(stockPileIndex), "Stock pile index must be non-negative.");
            }
            if (stockPileIndex > stockPile.Count)
            {
                throw new ArgumentOutOfRangeException(nameof(stockPileIndex), "Stock pile index outside of stock pile.");
            }
            Columns = columns;
            StockPile = stockPile;
            StockPileIndex = stockPileIndex;
            Foundation = foundation;
        }

        /// <summary>
        /// Returns <see langword="true"/> if the stock pile can be advanced.
        /// If the stock pile is empty, or if the stock pile index
        /// is beyond the end of the stock pile, this is <see langword="false"/>.
        /// </summary>
        /// <remarks>
        /// <para>
        /// If the stock pile is empty, it cannot be advanced or recycled.
        /// Otherwise, either this or <see cref="CanRecycleStockPile"/>
        /// will be <see langword="true"/>, and the other <see langword="false"/>.
        /// </para>
        /// </remarks>
        /// <returns><see langword="true"/> if the stock pile can be advanced.</returns>
        public bool CanAdvanceStockPile()
        {
            return StockPileIndex < StockPile.Count;
        }

        /// <summary>
        /// Returns <see langword="true"/> if the stock pile can be recycled.
        /// If the stock pile is empty, or if the stock pile index
        /// is not currently beyond the end of the stock pile, this is <see langword="false"/>.
        /// </summary>
        /// <remarks>
        /// <para>
        /// If the stock pile is empty, it cannot be advanced or recycled.
        /// Otherwise, either this or <see cref="CanAdvanceStockPile"/>
        /// will be <see langword="true"/>, and the other <see langword="false"/>.
        /// </para>
        /// </remarks>
        /// <returns><see langword="true"/> if the stock pile can be recycled.</returns>
        public bool CanRecycleStockPile()
        {
            // 0 represents no cards flipped, so offset by one.
            return StockPileIndex > 0 && StockPileIndex >= StockPile.Count;
        }

        /// <summary>
        /// Returns the current card that can be drawn from the stock pile.
        /// </summary>
        /// <returns>The current card available from the stock pile.</returns>
        /// <exception cref="ArgumentOutOfRangeException">If no card is available to be drawn from the stock pile.</exception>
        public Card GetStockPileCard()
        {
            return StockPile[StockPileIndex - 1];
        }

        /// <summary>
        /// Returns the top card on the foundation for the given suit.
        /// </summary>
        /// <param name="suit">The suit.</param>
        /// <returns>The current top card for the suit in the foundation.</returns>
        /// <exception cref="ArgumentOutOfRangeException">If the foundation has no cards for <paramref name="suit"/>.</exception>
        public Card GetTopOfFoundation(Suit suit)
        {
            IReadOnlyList<Card> foundationForSuit = Foundation[suit];
            return foundationForSuit[foundationForSuit.Count - 1];
        }

        /// <summary>
        /// Returns a new board with a run of cards moved from one column to another.
        /// </summary>
        /// <param name="fromIndex">The index of the column that the run of cards will be taken from.</param>
        /// <param name="toIndex">The index of the column that the run of cards will be moved to.</param>
        /// <param name="cardCount">The number of cards in the run to move.</param>
        /// <returns>The new board.</returns>
        /// <exception cref="ArgumentException">If <paramref name="fromIndex"/> equals <paramref name="toIndex"/>.</exception>
        /// <exception cref="IndexOutOfRangeException">If <paramref name="fromIndex"/> or <paramref name="toIndex"/>
        /// is invalid.</exception>
        /// <exception cref="ArgumentOutOfRangeException">If <paramref name="cardCount"/> is less than <c>1</c>,
        /// or more than the number of cards available in the source column.</exception>
        public Board MoveRun(int fromIndex, int toIndex, int cardCount)
        {
            if (fromIndex == toIndex)
            {
                throw new ArgumentException("Source and destination column are the same.");
            }

            Column fromColumn = Columns[fromIndex];
            Column toColumn = Columns[toIndex];
            IReadOnlyList<Card> run = fromColumn.GetTopCards(cardCount);
            Column newFromColumn = fromColumn.GetColumnMissingTopCards(cardCount);
            Column newToColumn = toColumn.AddNewCards(run);

            List<Column> newColumns = new List<Column>(Columns)
            {
                [fromIndex] = newFromColumn,
                [toIndex] = newToColumn,
            };
            return new Board(newColumns, StockPile, StockPileIndex, Foundation);
        }

        /// <summary>
        /// Returns a copy of the currenet board with one card removed from
        /// a column run and put into the foundation.
        /// </summary>
        /// <param name="index">The index of the column from which to remove a card.</param>
        /// <returns>A copy of the board with one card moved.</returns>
        public Board MoveCardToFoundation(int index)
        {
            Column column = Columns[index];
            Card card = column.GetTopCard();
            Column newColumn = column.GetColumnMissingTopCards(1);

            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> newFoundation = AddToFoundation(card);

            IReadOnlyList<Column> newColumns = new List<Column>(Columns)
            {
                [index] = newColumn,
            };

            return new Board(newColumns, StockPile, StockPileIndex, newFoundation);
        }

        /// <summary>
        /// Returns a copy of the current board with one card drawn from the stock pile
        /// and put on the specified column run.
        /// </summary>
        /// <param name="index">The index of the column to receive the card.</param>
        /// <returns>A copy of the board with one card moved.</returns>
        public Board DrawStockPileCardToColumn(int index)
        {
            Card card = GetStockPileCard();
            IReadOnlyList<Card> newStockPile = ExtractStockPileCard();

            int newStockPileIndex = StockPileIndex - 1;

            IReadOnlyList<Column> newColumns = new List<Column>(Columns)
            {
                [index] = Columns[index].AddNewCard(card),
            };

            return new Board(newColumns, newStockPile, newStockPileIndex, Foundation);
        }

        /// <summary>
        /// Returns a copy of the current board with one card drawn from the stock pile
        /// and put into the foundation.
        /// </summary>
        /// <returns>A copy of the board with one card moved.</returns>
        public Board DrawStockPileCardToFoundation()
        {
            Card card = GetStockPileCard();
            IReadOnlyList<Card> newStockPile = ExtractStockPileCard();

            int newStockPileIndex = StockPileIndex - 1;

            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> newFoundation = AddToFoundation(card);

            return new Board(Columns, newStockPile, newStockPileIndex, newFoundation);
        }

        /// <summary>
        /// Returns a copy of this board with one card moved from the foundation
        /// to the specified column run.
        /// </summary>
        /// <param name="suit">The suit from which to take a card from the foundation.</param>
        /// <param name="index">The index of the column that will receive the card.</param>
        /// <returns>A copy of this board with one card moved from the foundation to a column.</returns>
        public Board MoveCardFromFoundation(Suit suit, int index)
        {
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> newFoundation = RemoveFromFoundation(suit);

            IReadOnlyList<Column> newColumns = new List<Column>(Columns)
            {
                [index] = Columns[index].AddNewCard(GetTopOfFoundation(suit)),
            };

            return new Board(newColumns, StockPile, StockPileIndex, newFoundation);
        }

        /// <summary>
        /// Returns a copy of the foundation with the given card added to it.
        /// </summary>
        /// <remarks>
        /// <para>
        /// This does no validation that the move is legal.
        /// </para>
        /// </remarks>
        /// <param name="card">The card to add to the foundation.</param>
        /// <returns>A copy of the foundation with one card added.</returns>
        private IReadOnlyDictionary<Suit, IReadOnlyList<Card>> AddToFoundation(Card card)
        {
            Suit suit = card.Suit;

            return new Dictionary<Suit, IReadOnlyList<Card>>(Foundation)
            {
                [suit] = new List<Card>(Foundation[suit])
                {
                    card,
                },
            };
        }

        /// <summary>
        /// Returns a copy of the foundation with the top element removed from the specified suit.
        /// </summary>
        /// <param name="suit">The suit from which to remove one card.</param>
        /// <returns>A copy of the foundation with one card missing.</returns>
        private IReadOnlyDictionary<Suit, IReadOnlyList<Card>> RemoveFromFoundation(Suit suit)
        {
            IReadOnlyList<Card> stackForSuit = Foundation[suit];

            return new Dictionary<Suit, IReadOnlyList<Card>>(Foundation)
            {
                [suit] = stackForSuit.Take(stackForSuit.Count - 1).ToList(),
            };
        }

        /// <summary>
        /// Returns a copy of the stock pile with the currently indexed card removed.
        /// </summary>
        /// <returns>A copy of the stock pile with one card missing.</returns>
        private IReadOnlyList<Card> ExtractStockPileCard()
        {
            /*
             * StockPile: [a, b, c, d]
             * StockPileIndex: 2
             * 
             * Result: [a, c, d]
             */
            return StockPile.Take(StockPileIndex - 1).Concat(StockPile.Skip(StockPileIndex)).ToList();
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as Board);
        }

        /// <inheritdoc/>
        public bool Equals(Board? other)
        {
            return other != null
                && Columns.SequenceEqual(other.Columns)
                && StockPile.SequenceEqual(other.StockPile)
                && StockPileIndex == other.StockPileIndex
                && Foundation[Suit.Club].SequenceEqual(other.Foundation[Suit.Club])
                && Foundation[Suit.Diamond].SequenceEqual(other.Foundation[Suit.Diamond])
                && Foundation[Suit.Heart].SequenceEqual(other.Foundation[Suit.Heart])
                && Foundation[Suit.Spade].SequenceEqual(other.Foundation[Suit.Spade]);
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            HashCode hashCode = new HashCode();
            foreach (Column column in Columns)
            {
                hashCode.Add(column);
            }
            foreach (Card card in StockPile)
            {
                hashCode.Add(card);
            }
            hashCode.Add(StockPileIndex);
            foreach (Card card in Foundation[Suit.Club])
            {
                hashCode.Add(card);
            }
            foreach (Card card in Foundation[Suit.Diamond])
            {
                hashCode.Add(card);
            }
            foreach (Card card in Foundation[Suit.Heart])
            {
                hashCode.Add(card);
            }
            foreach (Card card in Foundation[Suit.Spade])
            {
                hashCode.Add(card);
            }
            return hashCode.ToHashCode();
        }
    }
}
