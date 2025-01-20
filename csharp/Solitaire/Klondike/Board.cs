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
        /// Constructs a new board.  All parameters should be immutable.
        /// </summary>
        /// <remarks>
        /// <para>
        /// Parameters should be immutable.  This is not enforced in code.
        /// </para>
        /// </remarks>
        /// <param name="columns">The columns for the new board.</param>
        /// <param name="stockPile">The stock pile.</param>
        /// <param name="stockPileIndex">The index into the stock pile of the current draw card.
        /// <c>0</c> means no card is available to be drawn,
        /// <c>stockPile.Count</c> means all cards have been advanced
        /// and the last card is available to be drawn.</param>
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
        /// Returns a copy of the foundation with the given card added to it.
        /// </summary>
        /// <remarks>
        /// <para>
        /// This does no validation that the move is legal.
        /// </para>
        /// </remarks>
        /// <param name="card">The card to add to the foundation.</param>
        /// <returns>A copy of the foundation with one card added.</returns>
        public IReadOnlyDictionary<Suit, IReadOnlyList<Card>> GetFoundationPlusCard(Card card)
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
        /// Extracts the top card from the foundation for the given suit, and returns
        /// both the card and the remaining foundation missing the card.
        /// </summary>
        /// <param name="suit">The suit of the foundation to remove the top card from.</param>
        /// <returns>A tuple of the card, and the remaining foundation.</returns>
        public Tuple<Card, IReadOnlyDictionary<Suit, IReadOnlyList<Card>>> ExtractCardFromFoundation(Suit suit)
        {
            IReadOnlyList<Card> foundationForSuit = Foundation[suit];
            int suitCountMinusOne = foundationForSuit.Count - 1;
            Card card = foundationForSuit[suitCountMinusOne];
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> newFoundation = new Dictionary<Suit, IReadOnlyList<Card>>(Foundation)
            {
                [suit] = foundationForSuit.Take(suitCountMinusOne).ToList(),
            };
            return new Tuple<Card, IReadOnlyDictionary<Suit, IReadOnlyList<Card>>>(card, newFoundation);
        }

        /// <summary>
        /// Extracts the currently visible card from the stock pile, and returns both the card
        /// and the remaining stock pile missing the card.
        /// </summary>
        /// <returns>A tuple of the card, and the remaining stock pile.</returns>
        public Tuple<Card, IReadOnlyList<Card>> ExtractStockPileCard()
        {
            Card card = StockPile[StockPileIndex - 1];
            /*
             * StockPile: [a, b, c, d]
             * StockPileIndex: 2
             * 
             * Result: [a, c, d]
             */
            IReadOnlyList<Card> remainingStockPile = StockPile.Take(StockPileIndex - 1).Concat(StockPile.Skip(StockPileIndex)).ToList();
            return new Tuple<Card, IReadOnlyList<Card>>(card, remainingStockPile);
        }

        /// <summary>
        /// Checks and returns whether it would be legal to add the given card to the foundation.
        /// </summary>
        /// <param name="card">The card to compare against the foundation.</param>
        /// <returns><see langword="true"/> if it is legal to add the card to the foundation.</returns>
        public bool CanAddToFoundation(Card card)
        {
            return card.Value.GetValue() == 1 + Foundation[card.Suit].Count;
        }

        private void PrintCardTo(Card card)
        {
            Console.Write("{0,2}{1}", card.Value.ToSymbol(), card.Suit.ToSymbol());
        }

        public void PrintTo()
        {
            foreach (KeyValuePair<Suit, IReadOnlyList<Card>> keyValuePair in Foundation)
            {
                Console.Write("{0,2}", string.Empty);
                IReadOnlyList<Card> cards = keyValuePair.Value;
                if (cards.Any())
                {
                    PrintCardTo(cards[cards.Count - 1]);
                }
                else
                {
                    Console.Write("{0,3}", "--");
                }
            }
            Console.Write("{0,3}", string.Empty);
            Console.Write("({0,2:D}/{1,2:D})", StockPileIndex, StockPile.Count);
            Console.Write("{0,2}", string.Empty);
            if (StockPileIndex > 0)
            {
                PrintCardTo(StockPile[StockPileIndex - 1]);
            }
            else
            {
                Console.Write("{0,3}", string.Empty);
            }
            Console.WriteLine();
            List<IEnumerator<Card>> enumerators = new List<IEnumerator<Card>>(Columns.Count);
            foreach (Column column in Columns)
            {
                Console.Write("{0,2}", string.Empty);
                Console.Write("({0:D})", column.GetCountOfFaceDownCards());
                enumerators.Add(column.FaceUp.GetEnumerator());
            }
            Console.WriteLine();
            bool printedSomething;
            do
            {
                printedSomething = false;
                foreach (IEnumerator<Card> enumerator in enumerators)
                {
                    Console.Write("{0,2}", string.Empty);
                    if (enumerator.MoveNext())
                    {
                        PrintCardTo(enumerator.Current);
                        printedSomething = true;
                    }
                    else
                    {
                        Console.Write("{0,3}", string.Empty);
                    }
                }
                Console.WriteLine();
            } while (printedSomething);
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
