﻿using Microsoft.VisualStudio.TestTools.UnitTesting;
using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike.Move.Tests
{
    [TestClass]
    public class ColumnToFoundationMoveTests
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

        [TestMethod]
        public void TestConstructorSourceColumnOutOfRange()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = new ColumnToFoundationMove(8, board);
            });
        }

        [TestMethod]
        public void TestConstructorEmptySourceColumn()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = new ColumnToFoundationMove(4, board);
            });
        }

        [TestMethod]
        public void TestSourceColumn()
        {
            ColumnToFoundationMove move = new(3, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(3, move.SourceColumn);
        }

        [TestMethod]
        public void TestCard()
        {
            ColumnToFoundationMove move = new(3, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(new Card(Value.Ace, Suit.Club), move.Card);
        }

        [TestMethod]
        public void TestHasCards()
        {
            ColumnToFoundationMove move = new(3, new Card(Value.Ace, Suit.Club));

            Assert.IsTrue(move.HasCards);
        }

        [TestMethod]
        public void TestCards()
        {
            ColumnToFoundationMove move = new(3, new Card(Value.Ace, Suit.Club));

            List<Card> expectedCards = new() { new Card(Value.Ace, Suit.Club), };
            Assert.IsTrue(expectedCards.SequenceEqual(move.Cards));
        }

        [TestMethod]
        public void TestApply()
        {
            List<Card> run = new()
            {
                new Card(Value.Ace, Suit.Club),
            };
            List<Column> columns = new(EmptyColumns)
            {
                [2] = new Column(null, run),
            };
            Board board = new(columns, EmptyListOfCards, 0, EmptyFoundation);

            ColumnToFoundationMove move = new(2, board);

            Board actual = move.Apply(board);

            Dictionary<Suit, IReadOnlyList<Card>> expectedFoundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                },
            };
            Board expected = new(EmptyColumns, EmptyListOfCards, 0, expectedFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyFromEmptyColumn()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            ColumnToFoundationMove move = new(2, new Card(Value.Ace, Suit.Club));

            Assert.ThrowsException<ArgumentOutOfRangeException>(() => {
                _ = move.Apply(board);
            });
        }

        [TestMethod]
        public void TestApplyNonEmptyFoundation()
        {
            List<Card> run = new()
            {
                new Card(Value.Four, Suit.Club),
            };
            List<Column> columns = new(EmptyColumns)
            {
                [2] = new Column(null, run),
            };
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                    new Card(Value.Three, Suit.Club),
                },
            };
            Board board = new(columns, EmptyListOfCards, 0, foundation);

            ColumnToFoundationMove move = new(2, board);

            Board actual = move.Apply(board);

            Dictionary<Suit, IReadOnlyList<Card>> expectedFoundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                    new Card(Value.Three, Suit.Club),
                    new Card(Value.Four, Suit.Club),
                },
            };
            Board expected = new(EmptyColumns, EmptyListOfCards, 0, expectedFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestEquals()
        {
            ColumnToFoundationMove move1 = new(4, new Card(Value.Ace, Suit.Club));
            ColumnToFoundationMove move2 = new(4, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(move1, move2);
        }

        [TestMethod]
        public void TestGetHashCode()
        {
            ColumnToFoundationMove move1 = new(4, new Card(Value.Ace, Suit.Club));
            ColumnToFoundationMove move2 = new(4, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(move1.GetHashCode(), move2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsDifferentColumn()
        {
            ColumnToFoundationMove move1 = new(5, new Card(Value.Ace, Suit.Club));
            ColumnToFoundationMove move2 = new(4, new Card(Value.Ace, Suit.Club));

            Assert.AreNotEqual(move1, move2);
        }

        [TestMethod]
        public void TestEqualsDifferentCard()
        {
            ColumnToFoundationMove move1 = new(4, new Card(Value.Ace, Suit.Club));
            ColumnToFoundationMove move2 = new(4, new Card(Value.Ace, Suit.Heart));

            Assert.AreNotEqual(move1, move2);
        }
    }
}
