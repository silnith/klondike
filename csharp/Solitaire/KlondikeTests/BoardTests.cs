using Microsoft.VisualStudio.TestTools.UnitTesting;
using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Silnith.Game.Klondike.Tests
{
    [TestClass]
    public class BoardTests
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
        public void TestEqualsEmpty()
        {
            Board board1 = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);
            Board board2 = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.AreEqual(board1, board2);
        }

        [TestMethod]
        public void TestGetHashCodeEmpty()
        {
            Board board1 = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);
            Board board2 = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.AreEqual(board1.GetHashCode(), board2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsOneStack()
        {
            List<Card> run = new()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Spade),
                new Card(Value.Ten, Suit.Heart),
            };
            List<Column> columns = new(EmptyColumns)
            {
                [6] = new Column(null, run),
            };
            Board board1 = new(columns, EmptyListOfCards, 0, EmptyFoundation);
            Board board2 = new(columns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.AreEqual(board1, board2);
        }

        [TestMethod]
        public void TestGetHashCodeOneStack()
        {
            List<Card> run = new()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Spade),
                new Card(Value.Ten, Suit.Heart),
            };
            List<Column> columns = new(EmptyColumns)
            {
                [6] = new Column(null, run),
            };
            Board board1 = new(columns, EmptyListOfCards, 0, EmptyFoundation);
            Board board2 = new(columns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.AreEqual(board1.GetHashCode(), board2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsTwoStacks()
        {
            List<Card> run = new()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Spade),
                new Card(Value.Ten, Suit.Heart),
            };
            List<Card> stack = new()
            {
                new Card(Value.Two, Suit.Spade),
                new Card(Value.Five, Suit.Diamond),
            };

            List<Column> columns1 = new(EmptyColumns)
            {
                [2] = new Column(stack, null),
                [6] = new Column(null, run),
            };
            List<Column> columns2 = new(EmptyColumns)
            {
                [2] = new Column(stack.Take(1).ToList(), stack.Skip(1).Take(1).ToList()),
                [6] = new Column(null, run),
            };

            Board board1 = new(columns1, EmptyListOfCards, 0, EmptyFoundation);
            Board board2 = new(columns2, EmptyListOfCards, 0, EmptyFoundation);

            Assert.AreEqual(board1, board2);
        }

        [TestMethod]
        public void TestGetHashCodeTwoStacks()
        {
            List<Card> run = new()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Spade),
                new Card(Value.Ten, Suit.Heart),
            };
            List<Card> stack = new()
            {
                new Card(Value.Two, Suit.Spade),
                new Card(Value.Five, Suit.Diamond),
            };

            List<Column> columns1 = new(EmptyColumns)
            {
                [2] = new Column(stack, null),
                [6] = new Column(null, run),
            };
            List<Column> columns2 = new(EmptyColumns)
            {
                [2] = new Column(stack.Take(1).ToList(), stack.Skip(1).Take(1).ToList()),
                [6] = new Column(null, run),
            };

            Board board1 = new(columns1, EmptyListOfCards, 0, EmptyFoundation);
            Board board2 = new(columns2, EmptyListOfCards, 0, EmptyFoundation);

            Assert.AreEqual(board1.GetHashCode(), board2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsTwoStacksDifferentColumns()
        {
            List<Card> run = new()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Spade),
                new Card(Value.Ten, Suit.Heart),
            };
            List<Card> stack = new()
            {
                new Card(Value.Two, Suit.Spade),
                new Card(Value.Five, Suit.Diamond),
            };

            List<Column> columns1 = new(EmptyColumns)
            {
                [2] = new Column(stack, null),
                [6] = new Column(null, run),
            };
            List<Column> columns2 = new(EmptyColumns)
            {
                [3] = new Column(stack.Take(1).ToList(), stack.Skip(1).Take(1).ToList()),
                [6] = new Column(null, run),
            };

            Board board1 = new(columns1, EmptyListOfCards, 0, EmptyFoundation);
            Board board2 = new(columns2, EmptyListOfCards, 0, EmptyFoundation);

            Assert.AreNotEqual(board1, board2);
        }

        [TestMethod]
        public void TestEqualsStockPile()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Two, Suit.Spade),
                new Card(Value.Three, Suit.Spade),
                new Card(Value.Eight, Suit.Heart),
                new Card(Value.Eight, Suit.Club),
                new Card(Value.Eight, Suit.Diamond),
                new Card(Value.King, Suit.Diamond),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Ace, Suit.Heart),
            };

            Board board1 = new(EmptyColumns, stockPile, 0, EmptyFoundation);
            Board board2 = new(EmptyColumns, stockPile, 0, EmptyFoundation);

            Assert.AreEqual(board1, board2);
        }

        [TestMethod]
        public void TestGetHashCodeStockPile()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Two, Suit.Spade),
                new Card(Value.Three, Suit.Spade),
                new Card(Value.Eight, Suit.Heart),
                new Card(Value.Eight, Suit.Club),
                new Card(Value.Eight, Suit.Diamond),
                new Card(Value.King, Suit.Diamond),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Ace, Suit.Heart),
            };

            Board board1 = new(EmptyColumns, stockPile, 0, EmptyFoundation);
            Board board2 = new(EmptyColumns, stockPile, 0, EmptyFoundation);

            Assert.AreEqual(board1.GetHashCode(), board2.GetHashCode());
        }

        [TestMethod]
        public void TestNotEqualsStockPile()
        {
            List<Card> stockPile1 = new()
            {
                new Card(Value.Two, Suit.Spade),
                new Card(Value.Three, Suit.Spade),
                new Card(Value.Eight, Suit.Heart),
                new Card(Value.Eight, Suit.Club),
                new Card(Value.Eight, Suit.Diamond),
                new Card(Value.King, Suit.Diamond),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Ace, Suit.Heart),
            };
            List<Card> stockPile2 = new()
            {
                new Card(Value.Two, Suit.Spade),
                new Card(Value.Three, Suit.Spade),
                new Card(Value.Eight, Suit.Club),
                new Card(Value.Eight, Suit.Diamond),
                new Card(Value.King, Suit.Diamond),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Ace, Suit.Heart),
            };

            Board board1 = new(EmptyColumns, stockPile1, 0, EmptyFoundation);
            Board board2 = new(EmptyColumns, stockPile2, 0, EmptyFoundation);

            Assert.AreNotEqual(board1, board2);
        }

        [TestMethod]
        public void TestEqualsStockPileIndex()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Two, Suit.Spade),
                new Card(Value.Three, Suit.Spade),
                new Card(Value.Eight, Suit.Heart),
                new Card(Value.Eight, Suit.Club),
                new Card(Value.Eight, Suit.Diamond),
                new Card(Value.King, Suit.Diamond),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Ace, Suit.Heart),
            };

            Board board1 = new(EmptyColumns, stockPile, 8, EmptyFoundation);
            Board board2 = new(EmptyColumns, stockPile, 8, EmptyFoundation);

            Assert.AreEqual(board1, board2);
        }

        [TestMethod]
        public void TestGetHashCodeStockPileIndex()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Two, Suit.Spade),
                new Card(Value.Three, Suit.Spade),
                new Card(Value.Eight, Suit.Heart),
                new Card(Value.Eight, Suit.Club),
                new Card(Value.Eight, Suit.Diamond),
                new Card(Value.King, Suit.Diamond),
                new Card(Value.Queen, Suit.Club),
                new Card(Value.Ace, Suit.Heart),
            };

            Board board1 = new(EmptyColumns, stockPile, 8, EmptyFoundation);
            Board board2 = new(EmptyColumns, stockPile, 8, EmptyFoundation);

            Assert.AreEqual(board1.GetHashCode(), board2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsFoundation()
        {
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                {
                    Suit.Club,
                    new List<Card>()
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
                    }
                },
                {
                    Suit.Diamond,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Diamond),
                    }
                },
                {
                    Suit.Heart,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Heart),
                        new Card(Value.Two, Suit.Heart),
                        new Card(Value.Three, Suit.Heart),
                        new Card(Value.Four, Suit.Heart),
                    }
                },
                {
                    Suit.Spade,
                    EmptyListOfCards
                },
            };

            Board board1 = new(EmptyColumns, EmptyListOfCards, 0, foundation);
            Board board2 = new(EmptyColumns, EmptyListOfCards, 0, foundation);

            Assert.AreEqual(board1, board2);
        }

        [TestMethod]
        public void TestGetHashCodeFoundation()
        {
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                {
                    Suit.Club,
                    new List<Card>()
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
                    }
                },
                {
                    Suit.Diamond,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Diamond),
                    }
                },
                {
                    Suit.Heart,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Heart),
                        new Card(Value.Two, Suit.Heart),
                        new Card(Value.Three, Suit.Heart),
                        new Card(Value.Four, Suit.Heart),
                    }
                },
                {
                    Suit.Spade,
                    EmptyListOfCards
                },
            };

            Board board1 = new(EmptyColumns, EmptyListOfCards, 0, foundation);
            Board board2 = new(EmptyColumns, EmptyListOfCards, 0, foundation);

            Assert.AreEqual(board1.GetHashCode(), board2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsFoundationCopy()
        {
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation1 = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                {
                    Suit.Club,
                    new List<Card>()
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
                    }
                },
                {
                    Suit.Diamond,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Diamond),
                    }
                },
                {
                    Suit.Heart,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Heart),
                        new Card(Value.Two, Suit.Heart),
                        new Card(Value.Three, Suit.Heart),
                        new Card(Value.Four, Suit.Heart),
                    }
                },
                {
                    Suit.Spade,
                    EmptyListOfCards
                },
            };
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation2 = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                {
                    Suit.Club,
                    new List<Card>()
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
                    }
                },
                {
                    Suit.Diamond,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Diamond),
                    }
                },
                {
                    Suit.Heart,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Heart),
                        new Card(Value.Two, Suit.Heart),
                        new Card(Value.Three, Suit.Heart),
                        new Card(Value.Four, Suit.Heart),
                    }
                },
                {
                    Suit.Spade,
                    EmptyListOfCards
                },
            };

            Board board1 = new(EmptyColumns, EmptyListOfCards, 0, foundation1);
            Board board2 = new(EmptyColumns, EmptyListOfCards, 0, foundation2);

            Assert.AreEqual(board1, board2);
        }

        [TestMethod]
        public void TestGetHashCodeFoundationCopy()
        {
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation1 = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                {
                    Suit.Club,
                    new List<Card>()
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
                    }
                },
                {
                    Suit.Diamond,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Diamond),
                    }
                },
                {
                    Suit.Heart,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Heart),
                        new Card(Value.Two, Suit.Heart),
                        new Card(Value.Three, Suit.Heart),
                        new Card(Value.Four, Suit.Heart),
                    }
                },
                {
                    Suit.Spade,
                    EmptyListOfCards
                },
            };
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation2 = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                {
                    Suit.Club,
                    new List<Card>()
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
                    }
                },
                {
                    Suit.Diamond,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Diamond),
                    }
                },
                {
                    Suit.Heart,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Heart),
                        new Card(Value.Two, Suit.Heart),
                        new Card(Value.Three, Suit.Heart),
                        new Card(Value.Four, Suit.Heart),
                    }
                },
                {
                    Suit.Spade,
                    EmptyListOfCards
                },
            };

            Board board1 = new(EmptyColumns, EmptyListOfCards, 0, foundation1);
            Board board2 = new(EmptyColumns, EmptyListOfCards, 0, foundation2);

            Assert.AreEqual(board1.GetHashCode(), board2.GetHashCode());
        }

        [TestMethod]
        public void TestNotEqualsFoundation()
        {
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation1 = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                {
                    Suit.Club,
                    new List<Card>()
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
                    }
                },
                {
                    Suit.Diamond,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Diamond),
                    }
                },
                {
                    Suit.Heart,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Heart),
                        new Card(Value.Two, Suit.Heart),
                        new Card(Value.Three, Suit.Heart),
                        new Card(Value.Four, Suit.Heart),
                    }
                },
                {
                    Suit.Spade,
                    EmptyListOfCards
                },
            };
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation2 = new Dictionary<Suit, IReadOnlyList<Card>>(foundation1)
            {
                [Suit.Diamond] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Diamond),
                    new Card(Value.Two, Suit.Diamond),
                },
            };

            Board board1 = new(EmptyColumns, EmptyListOfCards, 0, foundation1);
            Board board2 = new(EmptyColumns, EmptyListOfCards, 0, foundation2);

            Assert.AreNotEqual(board1, board2);
        }

        [TestMethod]
        public void TestMoveRun()
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

            Board actual = board.MoveRun(0, 1, 1);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [1] = new Column(null, run),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, EmptyFoundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestMoveRunTooManyCards()
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

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = board.MoveRun(0, 1, 2);
            });
        }

        [TestMethod]
        public void TestMoveRunTooFewCards()
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

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = board.MoveRun(0, 1, 0);
            });
        }

        [TestMethod]
        public void TestMoveRunSameFromTo()
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

            Assert.ThrowsException<ArgumentException>(() =>
            {
                _ = board.MoveRun(0, 0, 1);
            });
        }

        [TestMethod]
        public void TestMoveRunBig()
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

            Board actual = board.MoveRun(0, 1, 5);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [1] = new Column(null, run),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, EmptyFoundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestMoveRunOntoAnother()
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

            Board actual = board.MoveRun(4, 2, 3);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [2] = new Column(null, combinedRun),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, EmptyFoundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestMoveRunPartialOntoAnother()
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

            Board actual = board.MoveRun(1, 5, 3);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [1] = new Column(null, remainingRun),
                [5] = new Column(null, combinedRun),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, EmptyFoundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestMoveRunKeepsStockPile()
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

            Board actual = board.MoveRun(0, 1, 1);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [1] = new Column(null, run),
            };
            Board expected = new(expectedColumns, stockPile, 0, EmptyFoundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestMoveRunKeepsStockPileIndex()
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

            Board actual = board.MoveRun(0, 1, 1);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [1] = new Column(null, run),
            };
            Board expected = new(expectedColumns, stockPile, 4, EmptyFoundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestMoveRunKeepsFoundation()
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

            Board actual = board.MoveRun(0, 1, 1);

            List<Column> expectedColumns = new(EmptyColumns)
            {
                [1] = new Column(null, run),
            };
            Board expected = new(expectedColumns, EmptyListOfCards, 0, foundation);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestMoveCardToFoundation()
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

            Board actual = board.MoveCardToFoundation(2);

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
        public void TestMoveCardToFoundationFromEmptyColumn()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() => {
                _ = board.MoveCardToFoundation(2);
            });
        }

        [TestMethod]
        public void TestMoveCardToFoundationNonEmptyFoundation()
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

            Board actual = board.MoveCardToFoundation(2);

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
        public void TestDrawStockPileCardToColumn()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.King, Suit.Club),
            };
            Board board = new(EmptyColumns, stockPile, 1, EmptyFoundation);

            Board actual = board.DrawStockPileCardToColumn(4);

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
        public void TestDrawStockPileCardToColumnEmpty()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = board.DrawStockPileCardToColumn(4);
            });
        }

        [TestMethod]
        public void TestDrawStockPileCardToColumnUnderflow()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.King, Suit.Club),
            };
            Board board = new(EmptyColumns, stockPile, 0, EmptyFoundation);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = board.DrawStockPileCardToColumn(4);
            });
        }

        [TestMethod]
        public void TestDrawStockPileCardToColumnNonEmpty()
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

            Board actual = board.DrawStockPileCardToColumn(4);

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
        public void TestDrawStockPileCardToColumnFromBeginningNonEmpty()
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

            Board actual = board.DrawStockPileCardToColumn(4);

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
        public void TestDrawStockPileCardToColumnFromMiddleNonEmpty()
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

            Board actual = board.DrawStockPileCardToColumn(4);

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
        public void TestDrawStockPileCardToColumnFromEndNonEmpty()
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

            Board actual = board.DrawStockPileCardToColumn(4);

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
        public void TestDrawStockPileCardToColumnKeepsFoundation()
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

            Board actual = board.DrawStockPileCardToColumn(4);

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
        public void TestDrawStockPileCardToFoundation()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Spade),
            };
            Board board = new(EmptyColumns, stockPile, 1, EmptyFoundation);

            Board actual = board.DrawStockPileCardToFoundation();

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
        public void TestDrawStockPileCardToFoundationEmpty()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = board.DrawStockPileCardToFoundation();
            });
        }

        [TestMethod]
        public void TestDrawStockPileCardToFoundationUnderflow()
        {
            List<Card> stockPile = new()
            {
                new Card(Value.Ace, Suit.Spade),
            };
            Board board = new(EmptyColumns, stockPile, 0, EmptyFoundation);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = board.DrawStockPileCardToFoundation();
            });
        }

        [TestMethod]
        public void TestDrawStockPileCardToFoundationNonEmpty()
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

            Board actual = board.DrawStockPileCardToFoundation();

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
        public void TestDrawStockPileCardToFoundationFromBeginningNonEmpty()
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

            Board actual = board.DrawStockPileCardToFoundation();

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
        public void TestDrawStockPileCardToFoundationFromMiddleNonEmpty()
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

            Board actual = board.DrawStockPileCardToFoundation();

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
        public void TestDrawStockPileCardToFoundationFromEndNonEmpty()
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

            Board actual = board.DrawStockPileCardToFoundation();

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
        public void TestDrawStockPileCardToFoundationKeepsColumns()
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

            Board actual = board.DrawStockPileCardToFoundation();

            Board expected = new(columns, EmptyListOfCards, 0, foundation);

            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestMoveCardFromFoundation()
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

            Board actual = board.MoveCardFromFoundation(Suit.Club, 3);

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
        public void TestMoveCardFromFoundationEmpty()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() =>
            {
                _ = board.MoveCardFromFoundation(Suit.Club, 3);
            });
        }

        [TestMethod]
        public void TestMoveCardFromFoundationNonEmptyColumn()
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

            Board actual = board.MoveCardFromFoundation(Suit.Club, 3);

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
        public void TestMoveCardFromFoundationKeepsStockPile()
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

            Board actual = board.MoveCardFromFoundation(Suit.Club, 3);

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
        public void TestMoveCardFromFoundationKeepsStockPileIndex()
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

            Board actual = board.MoveCardFromFoundation(Suit.Club, 3);

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
    }
}
