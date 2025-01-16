using Microsoft.VisualStudio.TestTools.UnitTesting;
using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

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
        public void TestCanAddToFoundationEmptyAce()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.IsTrue(board.CanAddToFoundation(new Card(Value.Ace, Suit.Club)));
        }

        [TestMethod]
        public void TestCanAddToFoundationEmptyTwo()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.IsFalse(board.CanAddToFoundation(new Card(Value.Two, Suit.Club)));
        }

        [TestMethod]
        public void TestCanAddToFoundationEmptyKing()
        {
            Board board = new(EmptyColumns, EmptyListOfCards, 0, EmptyFoundation);

            Assert.IsFalse(board.CanAddToFoundation(new Card(Value.King, Suit.Club)));
        }

        [TestMethod]
        public void TestCanAddToFoundationLessThan()
        {
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                    new Card(Value.Three, Suit.Club),
                    new Card(Value.Four, Suit.Club),
                },
            };
            Board board = new(EmptyColumns, EmptyListOfCards, 0, foundation);

            Assert.IsFalse(board.CanAddToFoundation(new Card(Value.Three, Suit.Club)));
        }

        [TestMethod]
        public void TestCanAddToFoundationEquals()
        {
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                    new Card(Value.Three, Suit.Club),
                    new Card(Value.Four, Suit.Club),
                },
            };
            Board board = new(EmptyColumns, EmptyListOfCards, 0, foundation);

            Assert.IsFalse(board.CanAddToFoundation(new Card(Value.Four, Suit.Club)));
        }

        [TestMethod]
        public void TestCanAddToFoundationValid()
        {
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                    new Card(Value.Three, Suit.Club),
                    new Card(Value.Four, Suit.Club),
                },
            };
            Board board = new(EmptyColumns, EmptyListOfCards, 0, foundation);

            Assert.IsTrue(board.CanAddToFoundation(new Card(Value.Five, Suit.Club)));
        }

        [TestMethod]
        public void TestCanAddToFoundationGreaterThan()
        {
            Dictionary<Suit, IReadOnlyList<Card>> foundation = new(EmptyFoundation)
            {
                [Suit.Club] = new List<Card>()
                {
                    new Card(Value.Ace, Suit.Club),
                    new Card(Value.Two, Suit.Club),
                    new Card(Value.Three, Suit.Club),
                    new Card(Value.Four, Suit.Club),
                },
            };
            Board board = new(EmptyColumns, EmptyListOfCards, 0, foundation);

            Assert.IsFalse(board.CanAddToFoundation(new Card(Value.Six, Suit.Club)));
        }

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
        public void TestEqualsStockPileDiffer()
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
        public void TestEqualsFoundationDiffer()
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
    }
}
