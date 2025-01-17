using Microsoft.VisualStudio.TestTools.UnitTesting;
using Silnith.Game.Deck;
using System;
using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move.Tests
{
    [TestClass]
    public class RecycleStockPileMoveTests
    {
        [TestMethod]
        public void TestSourceIndex()
        {
            RecycleStockPileMove move = new(5);

            Assert.AreEqual(5, move.SourceIndex);
        }

        [TestMethod]
        public void TestHasCards()
        {
            RecycleStockPileMove move = new(5);

            Assert.IsFalse(move.HasCards);
        }

        #region Apply

        [TestMethod]
        public void TestApply()
        {
            List<Column> columns = new()
            {
                new Column(null, null),
                new Column(null, null),
                new Column(null, null),
                new Column(null, null),
                new Column(
                    null,
                    new List<Card>()
                    {
                        new Card(Value.King, Suit.Spade),
                        new Card(Value.Queen, Suit.Heart),
                    }),
                new Column(null, null),
                new Column(null, null),
            };
            List<Card> stockPile = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Jack, Suit.Spade),
            };
            int stockPileIndex = stockPile.Count;
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new()
            {
                {
                    Suit.Club,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Club),
                        new Card(Value.Two, Suit.Club),
                    }
                },
                {
                    Suit.Diamond,
                    Array.Empty<Card>()
                },
                {
                    Suit.Heart,
                    Array.Empty<Card>()
                },
                {
                    Suit.Spade,
                    Array.Empty<Card>()
                },
            };
            Board board = new(columns, stockPile, stockPileIndex, foundation);

            RecycleStockPileMove move = new(stockPileIndex);

            Board actual = move.Apply(board);

            Board expected = new(columns, stockPile, 0, foundation);

            Assert.AreEqual(expected, actual);
        }

        // TODO: Add tests that illegal recycles throw exceptions.

        #endregion

        #region Equals & GetHashCode

        [TestMethod]
        public void TestEquals()
        {
            RecycleStockPileMove move1 = new(5);
            RecycleStockPileMove move2 = new(5);

            Assert.AreEqual(move1, move2);
        }

        [TestMethod]
        public void TestGetHashCode()
        {
            RecycleStockPileMove move1 = new(5);
            RecycleStockPileMove move2 = new(5);

            Assert.AreEqual(move1.GetHashCode(), move2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsDifferentIndex()
        {
            RecycleStockPileMove move1 = new(5);
            RecycleStockPileMove move2 = new(4);

            Assert.AreNotEqual(move1, move2);
        }

        #endregion

    }
}
