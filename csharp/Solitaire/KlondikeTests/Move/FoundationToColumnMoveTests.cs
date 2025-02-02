using Microsoft.VisualStudio.TestTools.UnitTesting;
using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike.Move.Tests
{
    [TestClass]
    public class FoundationToColumnMoveTests
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
        public void TestFindMoves()
        {
            List<Column> columns = new(EmptyColumns)
            {
                [3] = new Column(
                    null,
                    new List<Card>()
                    {
                        new Card(Value.Six, Suit.Club),
                        new Card(Value.Five, Suit.Diamond),
                        new Card(Value.Four, Suit.Club),
                    }),
                [4] = new Column(
                    null,
                    new List<Card>()
                    {
                        // Please ignore that this card is duplicated.
                        new Card(Value.Four, Suit.Spade),
                    }),
            };
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                },
                [Suit.Diamond] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Diamond),
                    new Card(Value.Two, Suit.Diamond),
                    new Card(Value.Three, Suit.Diamond),
                },
                [Suit.Heart] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Heart),
                    new Card(Value.Two, Suit.Heart),
                    new Card(Value.Three, Suit.Heart),
                },
                [Suit.Spade] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Spade),
                    new Card(Value.Two, Suit.Spade),
                    new Card(Value.Three, Suit.Spade),
                    new Card(Value.Four, Suit.Spade),
                    new Card(Value.Five, Suit.Spade),
                    new Card(Value.Six, Suit.Spade),
                    new Card(Value.Seven, Suit.Spade),
                    new Card(Value.Eight, Suit.Spade),
                    new Card(Value.Nine, Suit.Spade),
                    new Card(Value.Ten, Suit.Spade),
                    new Card(Value.Jack, Suit.Spade),
                    new Card(Value.Queen, Suit.Spade),
                    new Card(Value.King, Suit.Spade),
                },
            };
            Board board = new(columns, EmptyListOfCards, 0, foundation);

            IEnumerable<FoundationToColumnMove> actual = FoundationToColumnMove.FindMoves(board);

            IEnumerable<FoundationToColumnMove> expected = new List<FoundationToColumnMove>()
            {
                new FoundationToColumnMove(0, new Card(Value.King, Suit.Spade)),
                new FoundationToColumnMove(1, new Card(Value.King, Suit.Spade)),
                new FoundationToColumnMove(2, new Card(Value.King, Suit.Spade)),
                new FoundationToColumnMove(3, new Card(Value.Three, Suit.Diamond)),
                new FoundationToColumnMove(3, new Card(Value.Three, Suit.Heart)),
                new FoundationToColumnMove(4, new Card(Value.Three, Suit.Diamond)),
                new FoundationToColumnMove(4, new Card(Value.Three, Suit.Heart)),
                new FoundationToColumnMove(5, new Card(Value.King, Suit.Spade)),
                new FoundationToColumnMove(6, new Card(Value.King, Suit.Spade)),
            };
            CollectionAssert.AreEquivalent(expected.ToList(), actual.ToList());
        }

        #endregion

        [TestMethod]
        public void TestConstructorEmptyFoundation()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = new FoundationToColumnMove(3, Suit.Club, board);
            });
        }

        [TestMethod]
        public void TestDestinationColumnIndex()
        {
            FoundationToColumnMove move = new(5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(5, move.DestinationColumnIndex);
        }

        [TestMethod]
        public void TestCard()
        {
            FoundationToColumnMove move = new(5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(new Card(Value.Ace, Suit.Club), move.Card);
        }

        [TestMethod]
        public void TestHasCards()
        {
            FoundationToColumnMove move = new(5, new Card(Value.Ace, Suit.Club));

            Assert.IsTrue(move.HasCards);
        }

        [TestMethod]
        public void TestCards()
        {
            FoundationToColumnMove move = new(5, new Card(Value.Ace, Suit.Club));

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
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                    new Card(Value.Three, Suit.Club),
                    new Card(Value.Four, Suit.Club),
                    new Card(Value.Five, Suit.Club),
                    new Card(Value.Six, Suit.Club),
                    new Card(Value.Seven, Suit.Club),
                    new Card(Value.Eight, Suit.Club),
                    new Card(Value.Nine, Suit.Club),
                    new Card(Value.Ten, Suit.Club),
                    new Card(Value.Jack, Suit.Club),
                    new Card(Value.Queen, Suit.Club),
                    new Card(Value.King, Suit.Club),
                },
            };
            Board board = new(EmptyColumns, EmptyListOfCards, 0, foundation);

            FoundationToColumnMove move = new(3, Suit.Club, board);

            Board actual = move.Apply(board);

            Dictionary<Suit, IReadOnlyList<Card>> expectedFoundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                    new Card(Value.Three, Suit.Club),
                    new Card(Value.Four, Suit.Club),
                    new Card(Value.Five, Suit.Club),
                    new Card(Value.Six, Suit.Club),
                    new Card(Value.Seven, Suit.Club),
                    new Card(Value.Eight, Suit.Club),
                    new Card(Value.Nine, Suit.Club),
                    new Card(Value.Ten, Suit.Club),
                    new Card(Value.Jack, Suit.Club),
                    new Card(Value.Queen, Suit.Club),
                },
            };
            List<Column> expectedColumns = new(EmptyColumns)
            {
                [3] = new Column(null, new List<Card>()
                {
                    new Card(Value.King, Suit.Club),
                }),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, expectedFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyEmpty()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            FoundationToColumnMove move = new(3, new Card(Value.Ace, Suit.Club));

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = move.Apply(board);
            });
        }

        [TestMethod]
        public void TestApplyNonEmptyColumn()
        {
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                    new Card(Value.Three, Suit.Club),
                },
            };
            List<Column> columns = new(EmptyColumns)
            {
                [3] = new Column(
                    new List<Card>()
                    {
                        new Card(Value.Four, Suit.Spade),
                    },
                    new List<Card>()
                    {
                        new Card(Value.Six, Suit.Heart),
                        new Card(Value.Five, Suit.Spade),
                        new Card(Value.Four, Suit.Heart),
                    }),
            };
            Board board = new(columns, EmptyListOfCards, 0, foundation);

            FoundationToColumnMove move = new(3, Suit.Club, board);

            Board actual = move.Apply(board);

            Dictionary<Suit, IReadOnlyList<Card>> expectedFoundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                },
            };
            List<Column> expectedColumns = new(EmptyColumns)
            {
                [3] = new Column(
                    new List<Card>()
                    {
                        new Card(Value.Four, Suit.Spade),
                    },
                    new List<Card>()
                    {
                        new Card(Value.Six, Suit.Heart),
                        new Card(Value.Five, Suit.Spade),
                        new Card(Value.Four, Suit.Heart),
                        new Card(Value.Three, Suit.Club),
                    }),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, expectedFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyKeepsStockPile()
        {
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                    new Card(Value.Three, Suit.Club),
                },
            };
            List<Column> columns = new(EmptyColumns)
            {
                [3] = new Column(
                    new List<Card>()
                    {
                        new Card(Value.Four, Suit.Spade),
                    },
                    new List<Card>()
                    {
                        new Card(Value.Six, Suit.Heart),
                        new Card(Value.Five, Suit.Spade),
                        new Card(Value.Four, Suit.Heart),
                    }),
            };
            List<Card> stockPile = new()
            {
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Six, Suit.Heart),
            };
            Board board = new(columns, stockPile, 0, foundation);

            FoundationToColumnMove move = new(3, Suit.Club, board);

            Board actual = move.Apply(board);

            Dictionary<Suit, IReadOnlyList<Card>> expectedFoundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                },
            };
            List<Column> expectedColumns = new(EmptyColumns)
            {
                [3] = new Column(
                    new List<Card>()
                    {
                        new Card(Value.Four, Suit.Spade),
                    },
                    new List<Card>()
                    {
                        new Card(Value.Six, Suit.Heart),
                        new Card(Value.Five, Suit.Spade),
                        new Card(Value.Four, Suit.Heart),
                        new Card(Value.Three, Suit.Club),
                    }),
            };
            Board expected = new(expectedColumns, stockPile, 0, expectedFoundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestApplyKeepsStockPileIndex()
        {
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                    new Card(Value.Three, Suit.Club),
                },
            };
            List<Column> columns = new(EmptyColumns)
            {
                [3] = new Column(
                    new List<Card>()
                    {
                        new Card(Value.Four, Suit.Spade),
                    },
                    new List<Card>()
                    {
                        new Card(Value.Six, Suit.Heart),
                        new Card(Value.Five, Suit.Spade),
                        new Card(Value.Four, Suit.Heart),
                    }),
            };
            List<Card> stockPile = new()
            {
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Two, Suit.Diamond),
                new Card(Value.Six, Suit.Heart),
            };
            Board board = new(columns, stockPile, 2, foundation);

            FoundationToColumnMove move = new(3, Suit.Club, board);

            Board actual = move.Apply(board);

            Dictionary<Suit, IReadOnlyList<Card>> expectedFoundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                },
            };
            List<Column> expectedColumns = new(EmptyColumns)
            {
                [3] = new Column(
                    new List<Card>()
                    {
                        new Card(Value.Four, Suit.Spade),
                    },
                    new List<Card>()
                    {
                        new Card(Value.Six, Suit.Heart),
                        new Card(Value.Five, Suit.Spade),
                        new Card(Value.Four, Suit.Heart),
                        new Card(Value.Three, Suit.Club),
                    }),
            };
            Board expected = new(expectedColumns, stockPile, 2, expectedFoundation);

            Assert.AreEqual(expected, actual);
        }

        #endregion

        #region Equals & GetHashCode

        [TestMethod]
        public void TestEquals()
        {
            FoundationToColumnMove move1 = new(5, new Card(Value.Ace, Suit.Club));
            FoundationToColumnMove move2 = new(5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(move1, move2);
        }

        [TestMethod]
        public void TestGetHashCode()
        {
            FoundationToColumnMove move1 = new(5, new Card(Value.Ace, Suit.Club));
            FoundationToColumnMove move2 = new(5, new Card(Value.Ace, Suit.Club));

            Assert.AreEqual(move1.GetHashCode(), move2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsDifferentColumn()
        {
            FoundationToColumnMove move1 = new(5, new Card(Value.Ace, Suit.Club));
            FoundationToColumnMove move2 = new(4, new Card(Value.Ace, Suit.Club));

            Assert.AreNotEqual(move1, move2);
        }

        [TestMethod]
        public void TestEqualsDifferentCard()
        {
            FoundationToColumnMove move1 = new(5, new Card(Value.Ace, Suit.Club));
            FoundationToColumnMove move2 = new(5, new Card(Value.Ace, Suit.Heart));

            Assert.AreNotEqual(move1, move2);
        }

        #endregion

    }
}
