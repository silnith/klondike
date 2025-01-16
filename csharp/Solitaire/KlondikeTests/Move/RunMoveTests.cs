using Microsoft.VisualStudio.TestTools.UnitTesting;
using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike.Move.Tests
{
    [TestClass]
    public class RunMoveTests
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
        public void TestSourceAndDestinationSame()
        {
            List<Card> run = new()
            {
                new Card(Value.Ace, Suit.Club),
            };

            Assert.ThrowsException<ArgumentException>(() =>
            {
                _ = new RunMove(2, 2, 1, run);
            });
        }

        [TestMethod]
        public void TestSourceColumn()
        {
            List<Card> run = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Ace, Suit.Club),
            };
            RunMove move = new(2, 5, 3, run);

            Assert.AreEqual(2, move.SourceColumn);
        }

        [TestMethod]
        public void TestDestinationColumn()
        {
            List<Card> run = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Ace, Suit.Club),
            };
            RunMove move = new(2, 5, 3, run);

            Assert.AreEqual(5, move.DestinationColumn);
        }

        [TestMethod]
        public void TestCardCount()
        {
            List<Card> run = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Ace, Suit.Club),
            };
            RunMove move = new(2, 5, 3, run);

            Assert.AreEqual(3, move.CardCount);
        }

        [TestMethod]
        public void TestCards()
        {
            List<Card> run = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Ace, Suit.Club),
            };
            RunMove move = new(2, 5, 3, run);

            Assert.IsTrue(run.SequenceEqual(move.Cards));
        }

        [TestMethod]
        public void TestConstructorCards()
        {
            List<Card> run = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Ace, Suit.Club),
            };
            List<Column> columns = new(EmptyColumns)
            {
                [2] = new Column(null, run),
            };
            Board board = new(columns, EmptyListOfCards, 0, EmptyFoundation);
            RunMove move = new(2, 5, 3, board);

            Assert.IsTrue(run.SequenceEqual(move.Cards));
        }

        [TestMethod]
        public void TestApply()
        {
            List<Card> run = new()
            {
                new Card(Value.King, Suit.Spade),
            };

            List<Column> columns = new(EmptyColumns)
            {
                [0] = new Column(null, run),
            };
            Board board = new(columns, EmptyListOfCards, 0, EmptyFoundation);

            RunMove move = new(0, 1, 1, board);

            Board actual = move.Apply(board);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [1] = new Column(null, run),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, EmptyFoundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyTooManyCards()
        {
            List<Card> run = new()
            {
                new Card(Value.King, Suit.Spade),
            };

            List<Column> columns = new(EmptyColumns)
            {
                [0] = new Column(null, run),
            };
            Board board = new(columns, EmptyListOfCards, 0, EmptyFoundation);

            RunMove move = new(0, 1, 2, run);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = move.Apply(board);
            });
        }

        [TestMethod]
        public void TestApplyTooFewCards()
        {
            List<Card> run = new()
            {
                new Card(Value.King, Suit.Spade),
            };

            List<Column> columns = new(EmptyColumns)
            {
                [0] = new Column(null, run),
            };
            Board board = new(columns, EmptyListOfCards, 0, EmptyFoundation);

            RunMove move = new(0, 1, 0, run);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = move.Apply(board);
            });
        }

        [TestMethod]
        public void TestApplyBig()
        {
            List<Card> run = new()
            {
                new Card(Value.King, Suit.Heart),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ten, Suit.Club),
                new Card(Value.Nine, Suit.Heart),
            };

            List<Column> columns = new(EmptyColumns)
            {
                [0] = new Column(null, run),
            };
            Board board = new(columns, EmptyListOfCards, 0, EmptyFoundation);

            RunMove move = new(0, 1, 5, board);

            Board actual = move.Apply(board);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [1] = new Column(null, run),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, EmptyFoundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyOntoAnother()
        {
            List<Card> topRun = new()
            {
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Club),
            };
            List<Card> bottomRun = new()
            {
                new Card(Value.Five, Suit.Heart),
                new Card(Value.Four, Suit.Club),
                new Card(Value.Three, Suit.Heart),
            };
            List<Card> combinedRun = new()
            {
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Club),
                new Card(Value.Five, Suit.Heart),
                new Card(Value.Four, Suit.Club),
                new Card(Value.Three, Suit.Heart),
            };

            List<Column> columns = new(EmptyColumns)
            {
                [2] = new Column(null, topRun),
                [4] = new Column(null, bottomRun),
            };
            Board board = new(columns, EmptyListOfCards, 0, EmptyFoundation);

            RunMove move = new(4, 2, 3, board);

            Board actual = move.Apply(board);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [2] = new Column(null, combinedRun),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, EmptyFoundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyPartialOntoAnother()
        {
            List<Card> topRun = new()
            {
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Club),
            };
            List<Card> bottomRun = new()
            {
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Five, Suit.Heart),
                new Card(Value.Four, Suit.Club),
                new Card(Value.Three, Suit.Heart),
            };
            List<Card> combinedRun = new()
            {
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Club),
                new Card(Value.Five, Suit.Heart),
                new Card(Value.Four, Suit.Club),
                new Card(Value.Three, Suit.Heart),
            };
            List<Card> remainingRun = new()
            {
                new Card(Value.Six, Suit.Spade),
            };

            List<Column> columns = new(EmptyColumns)
            {
                [1] = new Column(null, bottomRun),
                [5] = new Column(null, topRun),
            };
            Board board = new(columns, EmptyListOfCards, 0, EmptyFoundation);

            RunMove move = new(1, 5, 3, board);

            Board actual = move.Apply(board);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [1] = new Column(null, remainingRun),
                [5] = new Column(null, combinedRun),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, EmptyFoundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyKeepsStockPile()
        {
            List<Card> run = new()
            {
                new Card(Value.King, Suit.Spade),
            };
            List<Card> stockPile = new()
            {
                new Card(Value.Two, Suit.Club),
                new Card(Value.Ten, Suit.Spade),
                new Card(Value.Three, Suit.Spade),
                new Card(Value.Three, Suit.Heart),
            };

            List<Column> columns = new(EmptyColumns)
            {
                [0] = new Column(null, run),
            };
            Board board = new(columns, stockPile, 0, EmptyFoundation);

            RunMove move = new(0, 1, 1, board);

            Board actual = move.Apply(board);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [1] = new Column(null, run),
            };
            Board expected = new(expectedColumns, stockPile, 0, EmptyFoundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyKeepsStockPileIndex()
        {
            List<Card> run = new()
            {
                new Card(Value.King, Suit.Spade),
            };
            List<Card> stockPile = new()
            {
                new Card(Value.Two, Suit.Club),
                new Card(Value.Ten, Suit.Spade),
                new Card(Value.Three, Suit.Spade),
                new Card(Value.Three, Suit.Heart),
            };

            List<Column> columns = new(EmptyColumns)
            {
                [0] = new Column(null, run),
            };
            Board board = new(columns, stockPile, 4, EmptyFoundation);

            RunMove move = new(0, 1, 1, board);

            Board actual = move.Apply(board);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [1] = new Column(null, run),
            };
            Board expected = new(expectedColumns, stockPile, 4, EmptyFoundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyKeepsFoundation()
        {
            List<Card> run = new()
            {
                new Card(Value.King, Suit.Spade),
            };
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Diamond] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Diamond),
                    new Card(Value.Two, Suit.Diamond),
                    new Card(Value.Three, Suit.Diamond),
                },
                [Suit.Spade] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Spade),
                    new Card(Value.Two, Suit.Spade),
                },
            };

            List<Column> columns = new(EmptyColumns)
            {
                [0] = new Column(null, run),
            };
            Board board = new(columns, EmptyListOfCards, 0, foundation);

            RunMove move = new(0, 1, 1, board);

            Board actual = move.Apply(board);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [1] = new Column(null, run),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, foundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestEquals()
        {
            List<Card> run = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Ace, Suit.Club),
            };
            RunMove move1 = new(2, 5, 3, run);
            RunMove move2 = new(2, 5, 3, run);

            Assert.AreEqual(move1, move2);
        }

        [TestMethod]
        public void TestGetHashCode()
        {
            List<Card> run = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Ace, Suit.Club),
            };
            RunMove move1 = new(2, 5, 3, run);
            RunMove move2 = new(2, 5, 3, run);

            Assert.AreEqual(move1.GetHashCode(), move2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsDifferentSourceColumn()
        {
            List<Card> run = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Ace, Suit.Club),
            };
            RunMove move1 = new(2, 5, 3, run);
            RunMove move2 = new(1, 5, 3, run);

            Assert.AreNotEqual(move1, move2);
        }

        [TestMethod]
        public void TestEqualsDifferentDestinationColumn()
        {
            List<Card> run = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Ace, Suit.Club),
            };
            RunMove move1 = new(2, 5, 3, run);
            RunMove move2 = new(2, 4, 3, run);

            Assert.AreNotEqual(move1, move2);
        }

        [TestMethod]
        public void TestEqualsDifferentCardCount()
        {
            List<Card> run = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Ace, Suit.Club),
            };
            RunMove move1 = new(2, 5, 3, run);
            // Note that the card count and the run length differ.
            RunMove move2 = new(2, 5, 2, run);

            Assert.AreNotEqual(move1, move2);
        }

        [TestMethod]
        public void TestEqualsDifferentCards()
        {
            List<Card> run1 = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Ace, Suit.Club),
            };
            List<Card> run2 = new()
            {
                new Card(Value.Three, Suit.Spade),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Ace, Suit.Club),
            };
            RunMove move1 = new(2, 5, 3, run1);
            RunMove move2 = new(2, 5, 3, run2);

            Assert.AreNotEqual(move1, move2);
        }
    }
}
