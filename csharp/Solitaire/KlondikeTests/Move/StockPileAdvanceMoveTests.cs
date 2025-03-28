﻿using Microsoft.VisualStudio.TestTools.UnitTesting;
using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike.Move.Tests
{
    [TestClass]
    public class StockPileAdvanceMoveTests
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

            IEnumerable<StockPileAdvanceMove> moves = StockPileAdvanceMove.FindMoves(3, board);

            Assert.IsFalse(moves.Any());
        }

        [TestMethod]
        public void TestFindMovesStockPileAtBeginning()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Club),
                new Card(Value.Five, Suit.Diamond),
                new Card(Value.King, Suit.Spade),
            };
            Board board = new(EmptyColumns, stockPile, 0, EmptyFoundation);

            IEnumerable<StockPileAdvanceMove> moves = StockPileAdvanceMove.FindMoves(3, board);

            List<ISolitaireMove> expected = new()
            {
                new StockPileAdvanceMove(0, 3),
            };
            Assert.IsTrue(expected.SequenceEqual(moves));
        }

        [TestMethod]
        public void TestFindMovesStockPileInMiddle()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Club),
                new Card(Value.Five, Suit.Diamond),
                new Card(Value.King, Suit.Spade),
            };
            Board board = new(EmptyColumns, stockPile, 2, EmptyFoundation);

            IEnumerable<StockPileAdvanceMove> moves = StockPileAdvanceMove.FindMoves(3, board);

            List<ISolitaireMove> expected = new()
            {
                new StockPileAdvanceMove(2, 3),
            };
            Assert.IsTrue(expected.SequenceEqual(moves));
        }

        [TestMethod]
        public void TestFindMovesStockPileAtEnd()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Club),
                new Card(Value.Five, Suit.Diamond),
                new Card(Value.King, Suit.Spade),
            };
            Board board = new(EmptyColumns, stockPile, 3, EmptyFoundation);

            IEnumerable<StockPileAdvanceMove> moves = StockPileAdvanceMove.FindMoves(3, board);

            Assert.IsFalse(moves.Any());
        }

        #endregion

        [TestMethod]
        public void TestZeroIncrement()
        {
            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = new StockPileAdvanceMove(0, 0);
            });
        }

        [TestMethod]
        public void TestNegativeIncrement()
        {
            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = new StockPileAdvanceMove(0, -1);
            });
        }

        [TestMethod]
        public void TestBeginningIndex()
        {
            StockPileAdvanceMove move = new(17, 34);

            Assert.AreEqual(17, move.BeginningIndex);
        }

        [TestMethod]
        public void TestIncrement()
        {
            StockPileAdvanceMove move = new(17, 34);

            Assert.AreEqual(34, move.Increment);
        }

        [TestMethod]
        public void TestCoalesceBeginningIndex()
        {
            StockPileAdvanceMove firstMove = new(20, 4);
            StockPileAdvanceMove secondMove = new(24, 5);

            StockPileAdvanceMove coalescedMove = firstMove.Coalesce(secondMove);

            Assert.AreEqual(20, coalescedMove.BeginningIndex);
        }

        [TestMethod]
        public void TestCoalesceIncrement()
        {
            StockPileAdvanceMove firstMove = new(20, 4);
            StockPileAdvanceMove secondMove = new(24, 5);

            StockPileAdvanceMove coalescedMove = firstMove.Coalesce(secondMove);

            Assert.AreEqual(9, coalescedMove.Increment);
        }

        [TestMethod]
        public void TestHasCards()
        {
            StockPileAdvanceMove move = new(17, 34);

            Assert.IsFalse(move.HasCards);
        }

        #region Apply

        [TestMethod]
        public void TestApply()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Club),
                new Card(Value.Two, Suit.Club),
                new Card(Value.Three, Suit.Club),
                new Card(Value.Four, Suit.Club),
                new Card(Value.Five, Suit.Club),
                new Card(Value.Six, Suit.Club),
            };
            Board board = new(EmptyColumns, stockPile, 2, EmptyFoundation);

            StockPileAdvanceMove move = new(3, board);

            Board actual = move.Apply(board);

            Board expected = new(EmptyColumns, stockPile, 5, EmptyFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyKeepsColumns()
        {
            List<Column> columns = new(EmptyColumns)
            {
                [1] = new Column(
                    null,
                    new List<Card>()
                    {
                        new Card(Value.Four, Suit.Diamond),
                        new Card(Value.Three, Suit.Spade),
                        new Card(Value.Two, Suit.Diamond),
                        new Card(Value.Ace, Suit.Spade),
                    }),
                [2] = new Column(
                    new List<Card>()
                    {
                        new Card(Value.Four, Suit.Club),
                        new Card(Value.Ten, Suit.Diamond),
                    },
                    new List<Card>()
                    {
                        new Card(Value.Ten, Suit.Club),
                        new Card(Value.Nine, Suit.Heart),
                        new Card(Value.Eight, Suit.Club),
                        new Card(Value.Seven, Suit.Heart),
                    }),
            };
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Club),
                new Card(Value.Two, Suit.Club),
                new Card(Value.Three, Suit.Club),
                new Card(Value.Four, Suit.Club),
                new Card(Value.Five, Suit.Club),
                new Card(Value.Six, Suit.Club),
            };
            Board board = new(columns, stockPile, 2, EmptyFoundation);

            StockPileAdvanceMove move = new(3, board);

            Board actual = move.Apply(board);

            Board expected = new(columns, stockPile, 5, EmptyFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyKeepsFoundation()
        {
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Diamond] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Diamond),
                    new Card(Value.Two, Suit.Diamond),
                    new Card(Value.Three, Suit.Diamond),
                },
            };
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Club),
                new Card(Value.Two, Suit.Club),
                new Card(Value.Three, Suit.Club),
                new Card(Value.Four, Suit.Club),
                new Card(Value.Five, Suit.Club),
                new Card(Value.Six, Suit.Club),
            };
            Board board = new(EmptyColumns, stockPile, 2, foundation);

            StockPileAdvanceMove move = new(3, board);

            Board actual = move.Apply(board);

            Board expected = new(EmptyColumns, stockPile, 5, foundation);

            Assert.AreEqual(expected, actual);
        }

        #endregion

        #region Equals & GetHashCode

        [TestMethod]
        public void TestEquals()
        {
            StockPileAdvanceMove move1 = new(17, 34);
            StockPileAdvanceMove move2 = new(17, 34);

            Assert.AreEqual(move1, move2);
        }

        [TestMethod]
        public void TestGetHashCode()
        {
            StockPileAdvanceMove move1 = new(17, 34);
            StockPileAdvanceMove move2 = new(17, 34);

            Assert.AreEqual(move1.GetHashCode(), move2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsDifferentBeginningIndex()
        {
            StockPileAdvanceMove move1 = new(17, 34);
            StockPileAdvanceMove move2 = new(20, 34);

            Assert.AreNotEqual(move1, move2);
        }

        [TestMethod]
        public void TestEqualsDifferentIncrement()
        {
            StockPileAdvanceMove move1 = new(17, 34);
            StockPileAdvanceMove move2 = new(17, 3);

            Assert.AreNotEqual(move1, move2);
        }

        [TestMethod]
        public void TestEqualsDifferent()
        {
            StockPileAdvanceMove move1 = new(17, 34);
            StockPileAdvanceMove move2 = new(20, 3);

            Assert.AreNotEqual(move1, move2);
        }

        #endregion

    }
}
