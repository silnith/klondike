using Microsoft.VisualStudio.TestTools.UnitTesting;
using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike.Move.Tests
{
    [TestClass]
    public class StockPileRecycleMoveTests
    {
        private readonly IReadOnlyList<Card> EmptyListOfCards = Array.Empty<Card>();
        private readonly IReadOnlyDictionary<Suit, IReadOnlyList<Card>> EmptyFoundation = new Dictionary<Suit, IReadOnlyList<Card>>()
        {
            { Suit.Club, Array.Empty<Card>() },
            { Suit.Diamond, Array.Empty<Card>() },
            { Suit.Heart, Array.Empty<Card>() },
            { Suit.Spade, Array.Empty<Card>() },
        };
        private readonly IReadOnlyList<Column> EmptyColumns = new List<Column>()
        {
            new Column(null, null),
            new Column(null, null),
            new Column(null, null),
            new Column(null, null),
            new Column(null, null),
            new Column(null, null),
            new Column(null, null),
        };

        #region FindMoves

        [TestMethod]
        public void TestFindMovesEmptyStockPile()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            IEnumerable<StockPileRecycleMove> actual = StockPileRecycleMove.FindMoves(board);

            Assert.IsFalse(actual.Any());
        }

        [TestMethod]
        public void TestFindMovesStockPileAtBeginning()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Four, Suit.Club),
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Heart),
                new Card(Value.Three, Suit.Diamond),
            };
            Board board = new(EmptyColumns, stockPile, 0, EmptyFoundation);

            IEnumerable<StockPileRecycleMove> actual = StockPileRecycleMove.FindMoves(board);

            Assert.IsFalse(actual.Any());
        }

        [TestMethod]
        public void TestFindMovesStockPileInMiddle()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Four, Suit.Club),
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Heart),
                new Card(Value.Three, Suit.Diamond),
            };
            Board board = new(EmptyColumns, stockPile, 2, EmptyFoundation);

            IEnumerable<StockPileRecycleMove> actual = StockPileRecycleMove.FindMoves(board);

            Assert.IsFalse(actual.Any());
        }

        [TestMethod]
        public void TestFindMovesStockPileAtEnd()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Four, Suit.Club),
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Heart),
                new Card(Value.Three, Suit.Diamond),
            };
            Board board = new(EmptyColumns, stockPile, 4, EmptyFoundation);

            IEnumerable<StockPileRecycleMove> actual = StockPileRecycleMove.FindMoves(board);

            IEnumerable<StockPileRecycleMove> expected = new List<StockPileRecycleMove>(1)
            {
                new StockPileRecycleMove(4),
            };
            CollectionAssert.AreEquivalent(expected.ToList(), actual.ToList());
        }

        #endregion

        [TestMethod]
        public void TestSourceIndex()
        {
            StockPileRecycleMove move = new(5);

            Assert.AreEqual(5, move.SourceIndex);
        }

        [TestMethod]
        public void TestHasCards()
        {
            StockPileRecycleMove move = new(5);

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

            StockPileRecycleMove move = new(stockPileIndex);

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
            StockPileRecycleMove move1 = new(5);
            StockPileRecycleMove move2 = new(5);

            Assert.AreEqual(move1, move2);
        }

        [TestMethod]
        public void TestGetHashCode()
        {
            StockPileRecycleMove move1 = new(5);
            StockPileRecycleMove move2 = new(5);

            Assert.AreEqual(move1.GetHashCode(), move2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsDifferentIndex()
        {
            StockPileRecycleMove move1 = new(5);
            StockPileRecycleMove move2 = new(4);

            Assert.AreNotEqual(move1, move2);
        }

        #endregion

    }
}
