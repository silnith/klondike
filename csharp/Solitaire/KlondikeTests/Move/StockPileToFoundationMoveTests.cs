using Microsoft.VisualStudio.TestTools.UnitTesting;
using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike.Move.Tests
{
    [TestClass]
    public class StockPileToFoundationMoveTests
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
            StockPileToFoundationMove move = new(5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(5, move.SourceIndex);
        }

        [TestMethod]
        public void TestSourceIndexFromBoard()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Spade),
            };
            Board board = new(EmptyColumns, stockPile, 1, EmptyFoundation);

            StockPileToFoundationMove move = new(board);

            Assert.AreEqual(1, move.SourceIndex);
        }

        [TestMethod]
        public void TestCard()
        {
            StockPileToFoundationMove move = new(5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(new Card(Value.Ace, Suit.Club), move.Card);
        }

        [TestMethod]
        public void TestCardFromBoard()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Spade),
            };
            Board board = new(EmptyColumns, stockPile, 1, EmptyFoundation);

            StockPileToFoundationMove move = new(board);

            Assert.AreEqual(new Card(Value.Ace, Suit.Spade), move.Card);
        }

        [TestMethod]
        public void TestHasCards()
        {
            StockPileToFoundationMove move = new(5, new Card(Value.Ace, Suit.Club));

            Assert.IsTrue(move.HasCards);
        }

        [TestMethod]
        public void TestCards()
        {
            StockPileToFoundationMove move = new(5, new Card(Value.Ace, Suit.Club));

            List<Card> expectedCards = new()
            {
                new Card(Value.Ace, Suit.Club),
            };
            Assert.IsTrue(expectedCards.SequenceEqual(move.Cards));
        }

        #region Apply

        [TestMethod]
        public void TestApply()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Spade),
            };
            Board board = new(EmptyColumns, stockPile, 1, EmptyFoundation);

            StockPileToFoundationMove move = new(board);

            Board actual = move.Apply(board);

            Dictionary<Suit, IReadOnlyList<Card>> expectedFoundation = new(EmptyFoundation)
            {
                [Suit.Spade] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Spade),
                },
            };
            Board expected = new(EmptyColumns, EmptyListOfCards, 0, expectedFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyEmpty()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            StockPileToFoundationMove move = new(0, new Card(Value.Ace, Suit.Club));

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
                new Card(Value.Ace, Suit.Spade),
            };
            Board board = new(EmptyColumns, stockPile, 0, EmptyFoundation);

            // This is not actually the correct card.
            StockPileToFoundationMove move = new(0, new Card(Value.Ace, Suit.Spade));

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = move.Apply(board);
            });
        }

        [TestMethod]
        public void TestApplyNonEmpty()
        {
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Diamond] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Diamond),
                    new Card(Value.Two, Suit.Diamond),
                },
                [Suit.Spade] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Spade),
                    new Card(Value.Two, Suit.Spade),
                    new Card(Value.Three, Suit.Spade),
                    new Card(Value.Four, Suit.Spade),
                },
            };
            List<Card> stockPile = new()
            {
                new Card(Value.Five, Suit.Spade),
            };
            Board board = new(EmptyColumns, stockPile, 1, foundation);

            StockPileToFoundationMove move = new(board);

            Board actual = move.Apply(board);

            Dictionary<Suit, IReadOnlyList<Card>> expectedFoundation = new(EmptyFoundation)
            {
                [Suit.Diamond] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Diamond),
                    new Card(Value.Two, Suit.Diamond),
                },
                [Suit.Spade] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Spade),
                    new Card(Value.Two, Suit.Spade),
                    new Card(Value.Three, Suit.Spade),
                    new Card(Value.Four, Suit.Spade),
                    new Card(Value.Five, Suit.Spade),
                },
            };
            Board expected = new(EmptyColumns, EmptyListOfCards, 0, expectedFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyFromBeginningNonEmpty()
        {
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Diamond] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Diamond),
                    new Card(Value.Two, Suit.Diamond),
                },
                [Suit.Spade] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Spade),
                    new Card(Value.Two, Suit.Spade),
                    new Card(Value.Three, Suit.Spade),
                    new Card(Value.Four, Suit.Spade),
                },
            };
            List<Card> stockPile = new()
            {
                new Card(Value.Five, Suit.Spade),
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Two, Suit.Heart),
                new Card(Value.Ten, Suit.Diamond),

            };
            Board board = new(EmptyColumns, stockPile, 1, foundation);

            StockPileToFoundationMove move = new(board);

            Board actual = move.Apply(board);

            Dictionary<Suit, IReadOnlyList<Card>> expectedFoundation = new(EmptyFoundation)
            {
                [Suit.Diamond] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Diamond),
                    new Card(Value.Two, Suit.Diamond),
                },
                [Suit.Spade] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Spade),
                    new Card(Value.Two, Suit.Spade),
                    new Card(Value.Three, Suit.Spade),
                    new Card(Value.Four, Suit.Spade),
                    new Card(Value.Five, Suit.Spade),
                },
            };
            List<Card> expectedStockPile = new()
            {
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Two, Suit.Heart),
                new Card(Value.Ten, Suit.Diamond),
            };
            Board expected = new(EmptyColumns, expectedStockPile, 0, expectedFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyFromMiddleNonEmpty()
        {
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Diamond] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Diamond),
                    new Card(Value.Two, Suit.Diamond),
                },
                [Suit.Spade] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Spade),
                    new Card(Value.Two, Suit.Spade),
                    new Card(Value.Three, Suit.Spade),
                    new Card(Value.Four, Suit.Spade),
                },
            };
            List<Card> stockPile = new()
            {
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Five, Suit.Spade),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Two, Suit.Heart),
                new Card(Value.Ten, Suit.Diamond),

            };
            Board board = new(EmptyColumns, stockPile, 3, foundation);

            StockPileToFoundationMove move = new(board);

            Board actual = move.Apply(board);

            Dictionary<Suit, IReadOnlyList<Card>> expectedFoundation = new(EmptyFoundation)
            {
                [Suit.Diamond] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Diamond),
                    new Card(Value.Two, Suit.Diamond),
                },
                [Suit.Spade] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Spade),
                    new Card(Value.Two, Suit.Spade),
                    new Card(Value.Three, Suit.Spade),
                    new Card(Value.Four, Suit.Spade),
                    new Card(Value.Five, Suit.Spade),
                },
            };
            List<Card> expectedStockPile = new()
            {
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Two, Suit.Heart),
                new Card(Value.Ten, Suit.Diamond),
            };
            Board expected = new(EmptyColumns, expectedStockPile, 2, expectedFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyFromEndNonEmpty()
        {
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Diamond] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Diamond),
                    new Card(Value.Two, Suit.Diamond),
                },
                [Suit.Spade] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Spade),
                    new Card(Value.Two, Suit.Spade),
                    new Card(Value.Three, Suit.Spade),
                    new Card(Value.Four, Suit.Spade),
                },
            };
            List<Card> stockPile = new()
            {
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Two, Suit.Heart),
                new Card(Value.Ten, Suit.Diamond),
                new Card(Value.Five, Suit.Spade),

            };
            Board board = new(EmptyColumns, stockPile, 6, foundation);

            StockPileToFoundationMove move = new(board);

            Board actual = move.Apply(board);

            Dictionary<Suit, IReadOnlyList<Card>> expectedFoundation = new(EmptyFoundation)
            {
                [Suit.Diamond] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Diamond),
                    new Card(Value.Two, Suit.Diamond),
                },
                [Suit.Spade] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Spade),
                    new Card(Value.Two, Suit.Spade),
                    new Card(Value.Three, Suit.Spade),
                    new Card(Value.Four, Suit.Spade),
                    new Card(Value.Five, Suit.Spade),
                },
            };
            List<Card> expectedStockPile = new()
            {
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Two, Suit.Heart),
                new Card(Value.Ten, Suit.Diamond),
            };
            Board expected = new(EmptyColumns, expectedStockPile, 5, expectedFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyKeepsColumns()
        {
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Spade] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Spade),
                },
            };
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
                new Card(Value.Ace, Suit.Spade),
            };
            Board board = new(columns, stockPile, 1, EmptyFoundation);

            StockPileToFoundationMove move = new(board);

            Board actual = move.Apply(board);

            Board expected = new(columns, EmptyListOfCards, 0, foundation);

            Assert.AreEqual(expected, actual);
        }

        #endregion

        #region Equals & GetHashCode

        [TestMethod]
        public void TestEquals()
        {
            StockPileToFoundationMove move1 = new(5, new Card(Value.Ace, Suit.Club));
            StockPileToFoundationMove move2 = new(5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(move1, move2);
        }

        [TestMethod]
        public void TestGetHashCode()
        {
            StockPileToFoundationMove move1 = new(5, new Card(Value.Ace, Suit.Club));
            StockPileToFoundationMove move2 = new(5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(move1.GetHashCode(), move2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsDifferentIndex()
        {
            StockPileToFoundationMove move1 = new(5, new Card(Value.Ace, Suit.Club));
            StockPileToFoundationMove move2 = new(4, new Card(Value.Ace, Suit.Club));

            Assert.AreNotEqual(move1, move2);
        }

        [TestMethod]
        public void TestEqualsDifferentCard()
        {
            StockPileToFoundationMove move1 = new(5, new Card(Value.Ace, Suit.Club));
            StockPileToFoundationMove move2 = new(5, new Card(Value.Ace, Suit.Heart));

            Assert.AreNotEqual(move1, move2);
        }

        #endregion

    }
}
