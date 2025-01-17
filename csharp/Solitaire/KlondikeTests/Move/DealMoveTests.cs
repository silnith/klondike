using Microsoft.VisualStudio.TestTools.UnitTesting;
using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike.Move.Tests
{
    [TestClass]
    public class DealMoveTests
    {
        private readonly int numberOfColumns = 7;

        private readonly List<Card> deck = new()
        {
            new Card(Value.Seven, Suit.Heart),
            new Card(Value.Eight, Suit.Diamond),
            new Card(Value.Nine, Suit.Club),
            new Card(Value.King, Suit.Club),
            new Card(Value.King, Suit.Diamond),
            new Card(Value.King, Suit.Heart),
            new Card(Value.King, Suit.Spade),
            new Card(Value.Seven, Suit.Diamond),
            new Card(Value.Eight, Suit.Club),
            new Card(Value.Queen, Suit.Club),
            new Card(Value.Queen, Suit.Diamond),
            new Card(Value.Queen, Suit.Heart),
            new Card(Value.Queen, Suit.Spade),
            new Card(Value.Seven, Suit.Club),
            new Card(Value.Jack, Suit.Club),
            new Card(Value.Jack, Suit.Diamond),
            new Card(Value.Jack, Suit.Heart),
            new Card(Value.Jack, Suit.Spade),
            new Card(Value.Ten, Suit.Club),
            new Card(Value.Ten, Suit.Diamond),
            new Card(Value.Ten, Suit.Heart),
            new Card(Value.Ten, Suit.Spade),
            new Card(Value.Nine, Suit.Diamond),
            new Card(Value.Nine, Suit.Heart),
            new Card(Value.Nine, Suit.Spade),
            new Card(Value.Eight, Suit.Heart),
            new Card(Value.Eight, Suit.Spade),
            new Card(Value.Seven, Suit.Spade),
            new Card(Value.Six, Suit.Club),
            new Card(Value.Six, Suit.Diamond),
            new Card(Value.Six, Suit.Heart),
            new Card(Value.Six, Suit.Spade),
            new Card(Value.Five, Suit.Club),
            new Card(Value.Five, Suit.Diamond),
            new Card(Value.Five, Suit.Heart),
            new Card(Value.Five, Suit.Spade),
            new Card(Value.Four, Suit.Club),
            new Card(Value.Four, Suit.Diamond),
            new Card(Value.Four, Suit.Heart),
            new Card(Value.Four, Suit.Spade),
            new Card(Value.Three, Suit.Club),
            new Card(Value.Three, Suit.Diamond),
            new Card(Value.Three, Suit.Heart),
            new Card(Value.Three, Suit.Spade),
            new Card(Value.Two, Suit.Club),
            new Card(Value.Two, Suit.Diamond),
            new Card(Value.Two, Suit.Heart),
            new Card(Value.Two, Suit.Spade),
            new Card(Value.Ace, Suit.Club),
            new Card(Value.Ace, Suit.Diamond),
            new Card(Value.Ace, Suit.Heart),
            new Card(Value.Ace, Suit.Spade),
        };

        [TestMethod]
        public void TestDeck()
        {
            DealMove move = new(deck, numberOfColumns);

            Assert.IsTrue(deck.SequenceEqual(move.Deck));
        }

        [TestMethod]
        public void TestColumnCount()
        {
            DealMove move = new(deck, numberOfColumns);

            Assert.AreEqual(numberOfColumns, move.ColumnCount);
        }

        #region Apply

        [TestMethod]
        public void TestApply()
        {
            Board board = new(Array.Empty<Column>(), Array.Empty<Card>(), 0, new Dictionary<Suit, IReadOnlyList<Card>>());

            DealMove move = new(deck, numberOfColumns);

            Board actual = move.Apply(board);

            List<Column> expectedColumns = new(numberOfColumns)
            {
                new Column(
                    new List<Card>(),
                    new List<Card>()
                    {
                        new Card(Value.Seven, Suit.Heart),
                    }),
                new Column(
                    new List<Card>()
                    {
                        new Card(Value.Eight, Suit.Diamond),
                    },
                    new List<Card>()
                    {
                        new Card(Value.Seven, Suit.Diamond),
                    }),
                new Column(
                    new List<Card>()
                    {
                        new Card(Value.Nine, Suit.Club),
                        new Card(Value.Eight, Suit.Club),
                    },
                    new List<Card>()
                    {
                        new Card(Value.Seven, Suit.Club),
                    }),
                new Column(
                    new List<Card>()
                    {
                        new Card(Value.King, Suit.Club),
                        new Card(Value.Queen, Suit.Club),
                        new Card(Value.Jack, Suit.Club),
                    },
                    new List<Card>()
                    {
                        new Card(Value.Ten, Suit.Club),
                    }),
                new Column(
                    new List<Card>()
                    {
                        new Card(Value.King, Suit.Diamond),
                        new Card(Value.Queen, Suit.Diamond),
                        new Card(Value.Jack, Suit.Diamond),
                        new Card(Value.Ten, Suit.Diamond),
                    },
                    new List<Card>()
                    {
                        new Card(Value.Nine, Suit.Diamond),
                    }),
                new Column(
                    new List<Card>()
                    {
                        new Card(Value.King, Suit.Heart),
                        new Card(Value.Queen, Suit.Heart),
                        new Card(Value.Jack, Suit.Heart),
                        new Card(Value.Ten, Suit.Heart),
                        new Card(Value.Nine, Suit.Heart),
                    },
                    new List<Card>()
                    {
                        new Card(Value.Eight, Suit.Heart),
                    }),
                new Column(
                    new List<Card>()
                    {
                        new Card(Value.King, Suit.Spade),
                        new Card(Value.Queen, Suit.Spade),
                        new Card(Value.Jack, Suit.Spade),
                        new Card(Value.Ten, Suit.Spade),
                        new Card(Value.Nine, Suit.Spade),
                        new Card(Value.Eight, Suit.Spade),
                    },
                    new List<Card>()
                    {
                        new Card(Value.Seven, Suit.Spade),
                    }),
            };
            List<Card> expectedStockPile = new()
            {
                new Card(Value.Six, Suit.Club),
                new Card(Value.Six, Suit.Diamond),
                new Card(Value.Six, Suit.Heart),
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Five, Suit.Club),
                new Card(Value.Five, Suit.Diamond),
                new Card(Value.Five, Suit.Heart),
                new Card(Value.Five, Suit.Spade),
                new Card(Value.Four, Suit.Club),
                new Card(Value.Four, Suit.Diamond),
                new Card(Value.Four, Suit.Heart),
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Three, Suit.Club),
                new Card(Value.Three, Suit.Diamond),
                new Card(Value.Three, Suit.Heart),
                new Card(Value.Three, Suit.Spade),
                new Card(Value.Two, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Two, Suit.Heart),
                new Card(Value.Two, Suit.Spade),
                new Card(Value.Ace, Suit.Club),
                new Card(Value.Ace, Suit.Diamond),
                new Card(Value.Ace, Suit.Heart),
                new Card(Value.Ace, Suit.Spade),
            };
            Dictionary<Suit, IReadOnlyList<Card>> expectedFoundation = new()
            {
                [Suit.Club] = Array.Empty<Card>(),
                [Suit.Diamond] = Array.Empty<Card>(),
                [Suit.Heart] = Array.Empty<Card>(),
                [Suit.Spade] = Array.Empty<Card>(),
            };
            Board expected = new(expectedColumns, expectedStockPile, 0, expectedFoundation);

            Assert.AreEqual(expected, actual);
        }

        #endregion

        #region Equals & GetHashCode

        [TestMethod]
        public void TestEquals()
        {
            DealMove move1 = new(deck, numberOfColumns);
            DealMove move2 = new(deck, numberOfColumns);

            Assert.AreEqual(move1, move2);
        }

        [TestMethod]
        public void TestGetHashCode()
        {
            DealMove move1 = new(deck, numberOfColumns);
            DealMove move2 = new(deck, numberOfColumns);

            Assert.AreEqual(move1.GetHashCode(), move2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsDifferentDeck()
        {
            int halfCount = deck.Count / 2;
            DealMove move1 = new(deck, numberOfColumns);
            DealMove move2 = new(deck.Skip(halfCount).Concat(deck.Take(halfCount)).ToList(), numberOfColumns);

            Assert.AreNotEqual(move1, move2);
        }

        [TestMethod]
        public void TestEqualsDifferentColumnCount()
        {
            DealMove move1 = new(deck, numberOfColumns);
            DealMove move2 = new(deck, numberOfColumns - 1);

            Assert.AreNotEqual(move1, move2);
        }

        #endregion

    }
}
