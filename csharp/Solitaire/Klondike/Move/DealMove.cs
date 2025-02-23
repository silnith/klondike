﻿using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike.Move
{
    /// <summary>
    /// A move that deals a fresh deck of cards into a Klondike solitaire board.
    /// </summary>
    public class DealMove : ISolitaireMove, IEquatable<DealMove?>
    {
        /// <summary>
        /// The number of columns in the board.  In a typical game, this will be <c>7</c>.
        /// </summary>
        public int ColumnCount
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
        public bool HasCards => true;

        /// <inheritdoc/>
        public bool IsStockPileModification => false;

        /// <inheritdoc/>
        public bool IsStockPileAdvance => false;

        /// <inheritdoc/>
        public bool IsStockPileRecycle => false;

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
        /// Constructs a new move that deals a fresh deck of cards.
        /// </summary>
        /// <param name="deck">The deck of cards to deal.</param>
        /// <param name="columnCount">The number of columns on the board.  This is always <c>7</c>.</param>
        public DealMove(IReadOnlyList<Card> deck, int columnCount)
        {
            int cardsRequired = 0;
            for (int i = 1; i <= columnCount; i++)
            {
                cardsRequired += i;
            }
            if (deck.Count < cardsRequired)
            {
                throw new ArgumentException($"A deck of size {cardsRequired}  is required to deal {columnCount} columns.");
            }
            ColumnCount = columnCount;
            Cards = deck;
        }

        /// <inheritdoc/>
        public bool TakesFromColumn(int columnIndex) => false;

        /// <inheritdoc/>
        public bool AddsToColumn(int columnIndex) => false;

        /// <inheritdoc/>
        public Board Apply(Board board)
        {
            int remaining = Cards.Count;
            List<List<Card>> stacks = new List<List<Card>>(ColumnCount);
            for (int i = 0; i < ColumnCount; i++)
            {
                stacks.Add(new List<Card>(i + 1));
            }
            IEnumerator<Card> enumerator = Cards.GetEnumerator();
            for (int i = 0; i < ColumnCount; i++)
            {
                for (int j = i; j < ColumnCount; j++)
                {
                    _ = enumerator.MoveNext();
                    remaining--;
                    stacks[j].Add(enumerator.Current);
                }
            }

            IReadOnlyList<Column> columns = stacks.Select(stack => new Column(stack, null)).ToList();

            List<Card> stockPile = new List<Card>(remaining);
            while (enumerator.MoveNext())
            {
                stockPile.Add(enumerator.Current);
            }

            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                { Suit.Club, Array.Empty<Card>() },
                { Suit.Diamond, Array.Empty<Card>() },
                { Suit.Heart, Array.Empty<Card>() },
                { Suit.Spade, Array.Empty<Card>() },
            };
            return new Board(columns, stockPile, 0, foundation);
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as DealMove);
        }

        /// <inheritdoc/>
        public bool Equals(DealMove? other)
        {
            return other != null
                && ColumnCount == other.ColumnCount
                && Cards.SequenceEqual(other.Cards);
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            HashCode hashCode = new HashCode();
            hashCode.Add(ColumnCount);
            foreach (Card card in Cards)
            {
                hashCode.Add(card);
            }
            return hashCode.ToHashCode();
        }

        /// <inheritdoc/>
        public override string ToString()
        {
            return "Deal " + ColumnCount + " columns using deck [" + string.Join(", ", Cards) + "].";
        }
    }
}
