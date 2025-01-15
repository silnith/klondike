using Microsoft.VisualStudio.TestTools.UnitTesting;
using Silnith.Game.Deck;
using Silnith.Game.Klondike.Move;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Silnith.Game.Klondike.Move.Tests
{
    [TestClass]
    public class StockPileToColumnMoveTests
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
        public void TestSourceIndex()
        {
            StockPileToColumnMove move = new(2, 5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(2, move.SourceIndex);
        }

        [TestMethod]
        public void TestConstructorSourceIndex()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Club),
                new Card(Value.Two, Suit.Heart),
                new Card(Value.Three, Suit.Spade),
            };
            Board board = new(EmptyColumns, stockPile, 2, EmptyFoundation);
            StockPileToColumnMove move = new(5, board);

            Assert.AreEqual(2, move.SourceIndex);
        }

        [TestMethod]
        public void TestDestinationColumn()
        {
            StockPileToColumnMove move = new(2, 5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(5, move.DestinationColumn);
        }

        [TestMethod]
        public void TestCard()
        {
            StockPileToColumnMove move = new(2, 5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(new Card(Value.Ace, Suit.Club), move.Card);
        }

        [TestMethod]
        public void TestConstructorCard()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Club),
                new Card(Value.Two, Suit.Heart),
                new Card(Value.Three, Suit.Spade),
            };
            Board board = new(EmptyColumns, stockPile, 2, EmptyFoundation);
            StockPileToColumnMove move = new(5, board);

            Assert.AreEqual(new Card(Value.Two, Suit.Heart), move.Card);
        }

        [TestMethod]
        public void TestHasCards()
        {
            StockPileToColumnMove move = new(2, 5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(new Card(Value.Ace, Suit.Club), move.Card);
        }

        [TestMethod]
        public void TestCards()
        {
            StockPileToColumnMove move = new(2, 5, new Card(Value.Ace, Suit.Club));

            List<Card> expectedCards = new()
            {
                new Card(Value.Ace, Suit.Club),
            };
            Assert.IsTrue(expectedCards.SequenceEqual(move.Cards));
        }

        [TestMethod]
        public void TestConstructorCards()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Club),
                new Card(Value.Two, Suit.Heart),
                new Card(Value.Three, Suit.Spade),
            };
            Board board = new(EmptyColumns, stockPile, 2, EmptyFoundation);
            StockPileToColumnMove move = new(5, board);

            List<Card> expectedCards = new()
            {
                new Card(Value.Two, Suit.Heart),
            };
            Assert.IsTrue(expectedCards.SequenceEqual(move.Cards));
        }

        [TestMethod]
        public void TestApply()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.King, Suit.Club),
            };
            Board board = new(EmptyColumns, stockPile, 1, EmptyFoundation);

            StockPileToColumnMove move = new(4, board);

            Board actual = move.Apply(board);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [4] = new Column(null, new List<Card>()
                {
                    new Card(Value.King, Suit.Club),
                }),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyEmpty()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            StockPileToColumnMove move = new(0, 4, new Card(Value.Ace, Suit.Club));

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = move.Apply(board);
            });
        }

        [TestMethod]
        public void TestApplyUnderflow()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.King, Suit.Club),
            };
            Board board = new(EmptyColumns, stockPile, 0, EmptyFoundation);

            StockPileToColumnMove move = new(0, 4, new Card(Value.King, Suit.Club));

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = move.Apply(board);
            });
        }

        [TestMethod]
        public void TestApplyNonEmpty()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Four, Suit.Spade),
            };
            List<Card> run = new()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Club),
                new Card(Value.Five, Suit.Diamond),
            };
            List<Column> columns = new(EmptyColumns)
            {
                [4] = new Column(null, run),
            };
            Board board = new(columns, stockPile, 1, EmptyFoundation);

            StockPileToColumnMove move = new(4, board);

            Board actual = move.Apply(board);

            List<Card> expectedRun = new()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Club),
                new Card(Value.Five, Suit.Diamond),
                new Card(Value.Four, Suit.Spade),
            };
            List<Column> expectedColumns = new(EmptyColumns)
            {
                [4] = new Column(null, expectedRun),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyFromBeginningNonEmpty()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Queen, Suit.Club),
            };
            List<Card> run = new()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Club),
                new Card(Value.Five, Suit.Diamond),
            };
            List<Column> columns = new(EmptyColumns)
            {
                [4] = new Column(null, run),
            };
            Board board = new(columns, stockPile, 1, EmptyFoundation);

            StockPileToColumnMove move = new(4, board);

            Board actual = move.Apply(board);

            List<Card> expectedStockPile = new()
            {
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Queen, Suit.Club),
            };
            List<Card> expectedRun = new()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Club),
                new Card(Value.Five, Suit.Diamond),
                new Card(Value.Four, Suit.Spade),
            };
            List<Column> expectedColumns = new(EmptyColumns)
            {
                [4] = new Column(null, expectedRun),
            };
            Board expected = new(expectedColumns, expectedStockPile, 0, EmptyFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyFromMiddleNonEmpty()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Queen, Suit.Club),
            };
            List<Card> run = new()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Club),
                new Card(Value.Five, Suit.Diamond),
            };
            List<Column> columns = new(EmptyColumns)
            {
                [4] = new Column(null, run),
            };
            Board board = new(columns, stockPile, 2, EmptyFoundation);

            StockPileToColumnMove move = new(4, board);

            Board actual = move.Apply(board);

            List<Card> expectedStockPile = new()
            {
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Queen, Suit.Club),
            };
            List<Card> expectedRun = new()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Club),
                new Card(Value.Five, Suit.Diamond),
                new Card(Value.Four, Suit.Spade),
            };
            List<Column> expectedColumns = new(EmptyColumns)
            {
                [4] = new Column(null, expectedRun),
            };
            Board expected = new(expectedColumns, expectedStockPile, 1, EmptyFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyFromEndNonEmpty()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Four, Suit.Spade),
            };
            List<Card> run = new()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Club),
                new Card(Value.Five, Suit.Diamond),
            };
            List<Column> columns = new(EmptyColumns)
            {
                [4] = new Column(null, run),
            };
            Board board = new(columns, stockPile, 4, EmptyFoundation);

            StockPileToColumnMove move = new(4, board);

            Board actual = move.Apply(board);

            List<Card> expectedStockPile = new()
            {
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Queen, Suit.Club),
            };
            List<Card> expectedRun = new()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Club),
                new Card(Value.Five, Suit.Diamond),
                new Card(Value.Four, Suit.Spade),
            };
            List<Column> expectedColumns = new(EmptyColumns)
            {
                [4] = new Column(null, expectedRun),
            };
            Board expected = new(expectedColumns, expectedStockPile, 3, EmptyFoundation);

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
                new Card(Value.King, Suit.Club),
            };
            Board board = new(EmptyColumns, stockPile, 1, foundation);

            StockPileToColumnMove move = new(4, board);

            Board actual = move.Apply(board);

            List<Card> expectedRun = new()
            {
                new Card(Value.King, Suit.Club),
            };
            List<Column> expectedColumns = new(EmptyColumns)
            {
                [4] = new Column(null, expectedRun),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, foundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestEquals()
        {
            StockPileToColumnMove move1 = new(2, 5, new Card(Value.Ace, Suit.Club));
            StockPileToColumnMove move2 = new(2, 5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(move1, move2);
        }

        [TestMethod]
        public void TestGetHashCode()
        {
            StockPileToColumnMove move1 = new(2, 5, new Card(Value.Ace, Suit.Club));
            StockPileToColumnMove move2 = new(2, 5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(move1.GetHashCode(), move2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsDifferentSourceIndex()
        {
            StockPileToColumnMove move1 = new(2, 5, new Card(Value.Ace, Suit.Club));
            StockPileToColumnMove move2 = new(1, 5, new Card(Value.Ace, Suit.Club));

            Assert.AreNotEqual(move1, move2);
        }

        [TestMethod]
        public void TestEqualsDifferentDestinationColumn()
        {
            StockPileToColumnMove move1 = new(2, 5, new Card(Value.Ace, Suit.Club));
            StockPileToColumnMove move2 = new(2, 4, new Card(Value.Ace, Suit.Club));

            Assert.AreNotEqual(move1, move2);
        }

        [TestMethod]
        public void TestEqualsDifferentCard()
        {
            StockPileToColumnMove move1 = new(2, 5, new Card(Value.Ace, Suit.Club));
            StockPileToColumnMove move2 = new(2, 5, new Card(Value.Ace, Suit.Heart));

            Assert.AreNotEqual(move1, move2);
        }
    }
}
