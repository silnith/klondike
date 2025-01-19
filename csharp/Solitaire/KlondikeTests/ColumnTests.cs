using Microsoft.VisualStudio.TestTools.UnitTesting;
using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike.Tests
{
    [TestClass]
    public class ColumnTests
    {
        #region Constructors

        [TestMethod]
        public void TestConstructorEmpty()
        {
            IReadOnlyList<Card>? faceDown = Array.Empty<Card>();
            IReadOnlyList<Card>? faceUp = Array.Empty<Card>();

            Column _ = new(faceDown, faceUp);
        }

        [TestMethod]
        public void TestConstructorFaceDownNull()
        {
            IReadOnlyList<Card>? faceDown = null;
            IReadOnlyList<Card>? faceUp = Array.Empty<Card>();

            Column _ = new(faceDown, faceUp);
        }

        [TestMethod]
        public void TestConstructorFaceUpNull()
        {
            IReadOnlyList<Card>? faceDown = Array.Empty<Card>();
            IReadOnlyList<Card>? faceUp = null;

            Column _ = new(faceDown, faceUp);
        }

        [TestMethod]
        public void TestConstructorBothNull()
        {
            IReadOnlyList<Card>? faceDown = null;
            IReadOnlyList<Card>? faceUp = null;

            Column _ = new(faceDown, faceUp);
        }

        [TestMethod]
        public void TestConstructorWithFaceUp()
        {
            IReadOnlyList<Card>? faceDown = Array.Empty<Card>();
            IReadOnlyList<Card>? faceUp = new List<Card>() {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column _ = new(faceDown, faceUp);
        }

        [TestMethod]
        public void TestConstructorWithBoth()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column _ = new(faceDown, faceUp);
        }

        #endregion

        [TestMethod]
        public void TestHasFaceDownCards()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Assert.IsTrue(column.HasFaceDownCards());
        }

        [TestMethod]
        public void TestHasFaceDownCardsNull()
        {
            IReadOnlyList<Card>? faceDown = null;
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Assert.IsFalse(column.HasFaceDownCards());
        }

        [TestMethod]
        public void TestHasFaceDownCardsEmpty()
        {
            IReadOnlyList<Card>? faceDown = Array.Empty<Card>();
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Assert.IsFalse(column.HasFaceDownCards());
        }

        [TestMethod]
        public void TestHasFaceUpCards()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Assert.IsTrue(column.HasFaceUpCards());
        }

        [TestMethod]
        public void TestHasFaceUpCardsNull()
        {
            IReadOnlyList<Card>? faceDown = Array.Empty<Card>();
            IReadOnlyList<Card>? faceUp = null;

            Column column = new(faceDown, faceUp);

            Assert.IsFalse(column.HasFaceUpCards());
        }

        [TestMethod]
        public void TestHasFaceUpCardsEmpty()
        {
            IReadOnlyList<Card>? faceDown = Array.Empty<Card>();
            IReadOnlyList<Card>? faceUp = Array.Empty<Card>();

            Column column = new(faceDown, faceUp);

            Assert.IsFalse(column.HasFaceUpCards());
        }

        [TestMethod]
        public void TestHasFaceUpCardsAfterFlipFromDown()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = Array.Empty<Card>();

            Column column = new(faceDown, faceUp);

            Assert.IsTrue(column.HasFaceUpCards());
        }

        [TestMethod]
        public void TestGetCountOfFaceDownCards()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Assert.AreEqual(3, column.GetCountOfFaceDownCards());
        }

        [TestMethod]
        public void TestGetCountOfFaceDownCardsNull()
        {
            IReadOnlyList<Card>? faceDown = null;
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Assert.AreEqual(0, column.GetCountOfFaceDownCards());
        }

        [TestMethod]
        public void TestGetCountOfFaceDownCardsEmpty()
        {
            IReadOnlyList<Card>? faceDown = Array.Empty<Card>();
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Assert.AreEqual(0, column.GetCountOfFaceDownCards());
        }

        [TestMethod]
        public void TestGetCountOfFaceUpCards()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Assert.AreEqual(4, column.GetCountOfFaceUpCards());
        }

        [TestMethod]
        public void TestGetCountOfFaceUpCardsNull()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = null;

            Column column = new(faceDown, faceUp);

            Assert.AreEqual(1, column.GetCountOfFaceUpCards());
        }

        [TestMethod]
        public void TestGetCountOfFaceUpCardsEmpty()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = Array.Empty<Card>();

            Column column = new(faceDown, faceUp);

            Assert.AreEqual(1, column.GetCountOfFaceUpCards());
        }

        [TestMethod]
        public void TestFlippedCardRemovedFromDownNull()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = null;

            Column column = new(faceDown, faceUp);

            Assert.AreEqual(2, column.GetCountOfFaceDownCards());
        }

        [TestMethod]
        public void TestFlippedCardRemovedFromDownEmpty()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = Array.Empty<Card>();

            Column column = new(faceDown, faceUp);

            Assert.AreEqual(2, column.GetCountOfFaceDownCards());
        }

        [TestMethod]
        public void TestGetTopCard()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Assert.AreEqual(new Card(Value.Ten, Suit.Diamond), column.GetTopCard());
        }

        [TestMethod]
        public void TestGetTopCardNull()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = null;

            Column column = new(faceDown, faceUp);

            Assert.AreEqual(new Card(Value.Ace, Suit.Heart), column.GetTopCard());
        }

        [TestMethod]
        public void TestGetTopCardEmpty()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = Array.Empty<Card>();

            Column column = new(faceDown, faceUp);

            Assert.AreEqual(new Card(Value.Ace, Suit.Heart), column.GetTopCard());
        }

        [TestMethod]
        public void TestFaceUp()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            IReadOnlyList<Card> expected = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };
            Assert.IsTrue(Enumerable.SequenceEqual(expected, column.FaceUp));
        }

        [TestMethod]
        public void TestFaceUpNull()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = null;

            Column column = new(faceDown, faceUp);

            IReadOnlyList<Card> expected = new List<Card>()
            {
                new Card(Value.Ace, Suit.Heart),
            };
            Assert.IsTrue(Enumerable.SequenceEqual(expected, column.FaceUp));
        }

        [TestMethod]
        public void TestFaceUpEmpty()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = null;

            Column column = new(faceDown, faceUp);

            IReadOnlyList<Card> expected = new List<Card>()
            {
                new Card(Value.Ace, Suit.Heart),
            };
            Assert.IsTrue(Enumerable.SequenceEqual(expected, column.FaceUp));
        }

        #region GetTopCards

        [TestMethod]
        public void TestGetTopCardsOverflow()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() => {
                _ = column.GetTopCards(5);
            });
        }

        [TestMethod]
        public void TestGetTopCards4()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            IReadOnlyList<Card> actual = column.GetTopCards(4);

            IReadOnlyList<Card> expected = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };
            Assert.IsTrue(Enumerable.SequenceEqual(expected, actual));
        }

        [TestMethod]
        public void TestGetTopCards3()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            IReadOnlyList<Card> actual = column.GetTopCards(3);

            IReadOnlyList<Card> expected = new List<Card>()
            {
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };
            Assert.IsTrue(Enumerable.SequenceEqual(expected, actual));
        }

        [TestMethod]
        public void TestGetTopCards2()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            IReadOnlyList<Card> actual = column.GetTopCards(2);

            IReadOnlyList<Card> expected = new List<Card>()
            {
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };
            Assert.IsTrue(Enumerable.SequenceEqual(expected, actual));
        }

        [TestMethod]
        public void TestGetTopCards1()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            IReadOnlyList<Card> actual = column.GetTopCards(1);

            IReadOnlyList<Card> expected = new List<Card>()
            {
                new Card(Value.Ten, Suit.Diamond),
            };
            Assert.IsTrue(Enumerable.SequenceEqual(expected, actual));
        }

        [TestMethod]
        public void TestGetTopCardsUnderflow()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() => {
                _ = column.GetTopCards(0);
            });
        }

        #endregion

        #region ExtractCard

        [TestMethod]
        public void TestExtractCardEmpty()
        {
            Column column = new(null, null);

            Assert.ThrowsException<ArgumentException>(() =>
            {
                _ = column.ExtractCard();
            });
        }

        [TestMethod]
        public void TestExtractCardCard()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };
            Column column = new(faceDown, faceUp);

            Tuple<Card, Column> tuple = column.ExtractCard();
            Card actual = tuple.Item1;

            Card expected = new(Value.Ten, Suit.Diamond);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestExtractCardColumn()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };
            Column column = new(faceDown, faceUp);

            Tuple<Card, Column> tuple = column.ExtractCard();
            Column actual = tuple.Item2;

            List<Card> expectedFaceUp = new()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
            };
            Column expected = new(faceDown, expectedFaceUp);
            Assert.AreEqual(expected, actual);
        }

        #endregion

        #region ExtractRun

        [TestMethod]
        public void TestExtractRunOverflow()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() => {
                _ = column.ExtractRun(5);
            });
        }

        [TestMethod]
        public void TestExtractRun4Run()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Tuple<IReadOnlyList<Card>, Column> tuple = column.ExtractRun(4);
            IReadOnlyList<Card> actual = tuple.Item1;

            IReadOnlyList<Card> expected = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };
            CollectionAssert.AreEqual(expected.ToList(), actual.ToList());
        }

        [TestMethod]
        public void TestExtractRun4Column()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Tuple<IReadOnlyList<Card>, Column> tuple = column.ExtractRun(4);
            Column actual = tuple.Item2;

            IReadOnlyList<Card> expectedFaceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
            };
            IReadOnlyList<Card> expectedFaceUp = new List<Card>()
            {
                new Card(Value.Ace, Suit.Heart),
            };
            Column expected = new(expectedFaceDown, expectedFaceUp);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestExtractRun3Run()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Tuple<IReadOnlyList<Card>, Column> tuple = column.ExtractRun(3);
            IReadOnlyList<Card> actual = tuple.Item1;

            IReadOnlyList<Card> expected = new List<Card>()
            {
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };
            CollectionAssert.AreEqual(expected.ToList(), actual.ToList());
        }

        [TestMethod]
        public void TestExtractRun3Column()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Tuple<IReadOnlyList<Card>, Column> tuple = column.ExtractRun(3);
            Column actual = tuple.Item2;

            IReadOnlyList<Card> expectedFaceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
            };
            Column expected = new(faceDown, expectedFaceUp);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestExtractRun2Run()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Tuple<IReadOnlyList<Card>, Column> tuple = column.ExtractRun(2);
            IReadOnlyList<Card> actual = tuple.Item1;

            IReadOnlyList<Card> expected = new List<Card>()
            {
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };
            CollectionAssert.AreEqual(expected.ToList(), actual.ToList());
        }

        [TestMethod]
        public void TestExtractRun2Column()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Tuple<IReadOnlyList<Card>, Column> tuple = column.ExtractRun(2);
            Column actual = tuple.Item2;

            IReadOnlyList<Card> expectedFaceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
            };
            Column expected = new(faceDown, expectedFaceUp);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestExtractRun1Run()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Tuple<IReadOnlyList<Card>, Column> tuple = column.ExtractRun(1);
            IReadOnlyList<Card> actual = tuple.Item1;

            IReadOnlyList<Card> expected = new List<Card>()
            {
                new Card(Value.Ten, Suit.Diamond),
            };
            CollectionAssert.AreEqual(expected.ToList(), actual.ToList());
        }

        [TestMethod]
        public void TestExtractRun1Column()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Tuple<IReadOnlyList<Card>, Column> tuple = column.ExtractRun(1);
            Column actual = tuple.Item2;

            IReadOnlyList<Card> expectedFaceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
            };
            Column expected = new(faceDown, expectedFaceUp);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestExtractRunUnderflow()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Assert.ThrowsException<ArgumentOutOfRangeException>(() => {
                _ = column.ExtractRun(0);
            });
        }

        #endregion

        [TestMethod]
        public void TestWithCard()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            Column actual = column.WithCard(new Card(Value.Nine, Suit.Club));

            IReadOnlyList<Card> expectedFaceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
                new Card(Value.Nine, Suit.Club),
            };
            Column expected = new(faceDown, expectedFaceUp);
            Assert.AreEqual(expected, actual);
        }

        #region WithCards

        [TestMethod]
        public void TestWithCards1()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            IEnumerable<Card> run = new List<Card>()
            {
                new Card(Value.Nine, Suit.Club),
            };
            Column actual = column.WithCards(run);

            IReadOnlyList<Card> expectedFaceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
                new Card(Value.Nine, Suit.Club),
            };
            Column expected = new(faceDown, expectedFaceUp);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestWithCards3()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            IEnumerable<Card> run = new List<Card>()
            {
                new Card(Value.Nine, Suit.Club),
                new Card(Value.Eight, Suit.Diamond),
                new Card(Value.Seven, Suit.Club),
            };
            Column actual = column.WithCards(run);

            IReadOnlyList<Card> expectedFaceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
                new Card(Value.Nine, Suit.Club),
                new Card(Value.Eight, Suit.Diamond),
                new Card(Value.Seven, Suit.Club),
            };
            Column expected = new(faceDown, expectedFaceUp);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestWithCardsEmpty()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Four, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
                new Card(Value.Ace, Suit.Heart),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Diamond),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Diamond),
            };

            Column column = new(faceDown, faceUp);

            IEnumerable<Card> run = Array.Empty<Card>();

            Assert.ThrowsException<ArgumentException>(() => {
                _ = column.WithCards(run);
            });
        }

        #endregion

        #region CanAddRun

        [TestMethod]
        public void TestCanAddRunEmptyColumnEmptyRun()
        {
            Column column = new(Array.Empty<Card>(), Array.Empty<Card>());

            Assert.ThrowsException<ArgumentException>(() =>
            {
                _ = column.CanAddRun(Array.Empty<Card>());
            });
        }

        [TestMethod]
        public void TestCanAddRunEmptyColumnKing()
        {
            Column column = new(Array.Empty<Card>(), Array.Empty<Card>());

            List<Card> run = new()
            {
                new Card(Value.King, Suit.Club),
            };

            Assert.IsTrue(column.CanAddRun(run));
        }

        [TestMethod]
        public void TestCanAddRunEmptyColumnKingRun()
        {
            Column column = new(Array.Empty<Card>(), Array.Empty<Card>());

            List<Card> run = new()
            {
                new Card(Value.King, Suit.Club),
                new Card(Value.Queen, Suit.Heart),
                new Card(Value.Jack, Suit.Club),
            };

            Assert.IsTrue(column.CanAddRun(run));
        }

        [TestMethod]
        public void TestCanAddRunEmptyColumnQueen()
        {
            Column column = new(Array.Empty<Card>(), Array.Empty<Card>());

            List<Card> run = new()
            {
                new Card(Value.Queen, Suit.Heart),
            };

            Assert.IsFalse(column.CanAddRun(run));
        }

        [TestMethod]
        public void TestCanAddRunEmptyColumnQueenRun()
        {
            Column column = new(Array.Empty<Card>(), Array.Empty<Card>());

            List<Card> run = new()
            {
                new Card(Value.Queen, Suit.Heart),
                new Card(Value.Jack, Suit.Club),
                new Card(Value.Ten, Suit.Heart),
            };

            Assert.IsFalse(column.CanAddRun(run));
        }

        [TestMethod]
        public void TestCanAddRunEmptyRun()
        {
            List<Card> faceDown = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Three, Suit.Diamond),
                new Card(Value.Three, Suit.Heart),
                new Card(Value.Three, Suit.Spade),
            };
            List<Card> faceUp = new()
            {
                new Card(Value.Ten, Suit.Spade),
                new Card(Value.Nine, Suit.Heart),
                new Card(Value.Eight, Suit.Spade),
            };
            Column column = new(faceDown, faceUp);

            Assert.ThrowsException<ArgumentException>(() =>
            {
                _ = column.CanAddRun(Array.Empty<Card>());
            });
        }

        [TestMethod]
        public void TestCanAddRun()
        {
            List<Card> faceDown = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Three, Suit.Diamond),
                new Card(Value.Three, Suit.Heart),
                new Card(Value.Three, Suit.Spade),
            };
            List<Card> faceUp = new()
            {
                new Card(Value.Ten, Suit.Spade),
                new Card(Value.Nine, Suit.Heart),
                new Card(Value.Eight, Suit.Spade),
            };
            Column column = new(faceDown, faceUp);

            List<Card> run = new()
            {
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Spade),
                new Card(Value.Five, Suit.Heart),
            };

            Assert.IsTrue(column.CanAddRun(run));
        }

        [TestMethod]
        public void TestCanAddRunWrongColor()
        {
            List<Card> faceDown = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Three, Suit.Diamond),
                new Card(Value.Three, Suit.Heart),
                new Card(Value.Three, Suit.Spade),
            };
            List<Card> faceUp = new()
            {
                new Card(Value.Ten, Suit.Spade),
                new Card(Value.Nine, Suit.Heart),
                new Card(Value.Eight, Suit.Spade),
            };
            Column column = new(faceDown, faceUp);

            List<Card> run = new()
            {
                new Card(Value.Seven, Suit.Spade),
                new Card(Value.Six, Suit.Heart),
                new Card(Value.Five, Suit.Spade),
            };

            Assert.IsFalse(column.CanAddRun(run));
        }

        [TestMethod]
        public void TestCanAddRunTooHigh()
        {
            List<Card> faceDown = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Three, Suit.Diamond),
                new Card(Value.Three, Suit.Heart),
                new Card(Value.Three, Suit.Spade),
            };
            List<Card> faceUp = new()
            {
                new Card(Value.Ten, Suit.Spade),
                new Card(Value.Nine, Suit.Heart),
                new Card(Value.Eight, Suit.Spade),
            };
            Column column = new(faceDown, faceUp);

            List<Card> run = new()
            {
                new Card(Value.Eight, Suit.Heart),
                new Card(Value.Seven, Suit.Spade),
                new Card(Value.Six, Suit.Heart),
            };

            Assert.IsFalse(column.CanAddRun(run));
        }

        [TestMethod]
        public void TestCanAddRunTooLow()
        {
            List<Card> faceDown = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Three, Suit.Diamond),
                new Card(Value.Three, Suit.Heart),
                new Card(Value.Three, Suit.Spade),
            };
            List<Card> faceUp = new()
            {
                new Card(Value.Ten, Suit.Spade),
                new Card(Value.Nine, Suit.Heart),
                new Card(Value.Eight, Suit.Spade),
            };
            Column column = new(faceDown, faceUp);

            List<Card> run = new()
            {
                new Card(Value.Six, Suit.Heart),
                new Card(Value.Five, Suit.Spade),
                new Card(Value.Four, Suit.Heart),
            };

            Assert.IsFalse(column.CanAddRun(run));
        }

        [TestMethod]
        public void TestCanAddRunKing()
        {
            List<Card> faceDown = new()
            {
                new Card(Value.Three, Suit.Club),
                new Card(Value.Three, Suit.Diamond),
                new Card(Value.Three, Suit.Heart),
                new Card(Value.Three, Suit.Spade),
            };
            List<Card> faceUp = new()
            {
                new Card(Value.Ten, Suit.Spade),
                new Card(Value.Nine, Suit.Heart),
                new Card(Value.Eight, Suit.Spade),
            };
            Column column = new(faceDown, faceUp);

            List<Card> run = new()
            {
                new Card(Value.King, Suit.Heart),
                new Card(Value.Queen, Suit.Spade),
                new Card(Value.Jack, Suit.Heart),
            };

            Assert.IsFalse(column.CanAddRun(run));
        }

        #endregion

        #region Equals & GetHashCode

        [TestMethod]
        public void TestEquals()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Two, Suit.Heart),
                new Card(Value.King, Suit.Diamond),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Diamond),
                new Card(Value.Six, Suit.Club),
            };

            Column column1 = new(faceDown, faceUp);
            Column column2 = new(faceDown, faceUp);

            Assert.AreEqual(column1, column2);
        }

        [TestMethod]
        public void TestGetHashCode()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Two, Suit.Heart),
                new Card(Value.King, Suit.Diamond),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Diamond),
                new Card(Value.Six, Suit.Club),
            };

            Column column1 = new(faceDown, faceUp);
            Column column2 = new(faceDown, faceUp);

            Assert.AreEqual(column1.GetHashCode(), column2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsFaceDownDiffer()
        {
            IReadOnlyList<Card>? faceDown1 = new List<Card>()
            {
                new Card(Value.Two, Suit.Heart),
                new Card(Value.King, Suit.Diamond),
            };
            IReadOnlyList<Card>? faceDown2 = new List<Card>()
            {
                new Card(Value.Two, Suit.Spade),
                new Card(Value.King, Suit.Diamond),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Diamond),
                new Card(Value.Six, Suit.Club),
            };

            Column column1 = new(faceDown1, faceUp);
            Column column2 = new(faceDown2, faceUp);

            Assert.AreNotEqual(column1, column2);
        }

        [TestMethod]
        public void TestEqualsFaceDownShorter()
        {
            IReadOnlyList<Card>? faceDown1 = new List<Card>()
            {
                new Card(Value.Two, Suit.Heart),
                new Card(Value.King, Suit.Diamond),
            };
            IReadOnlyList<Card>? faceDown2 = Array.Empty<Card>();
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Diamond),
                new Card(Value.Six, Suit.Club),
            };

            Column column1 = new(faceDown1, faceUp);
            Column column2 = new(faceDown2, faceUp);

            Assert.AreNotEqual(column1, column2);
        }

        [TestMethod]
        public void TestEqualsFaceDownLonger()
        {
            IReadOnlyList<Card>? faceDown1 = Array.Empty<Card>();
            IReadOnlyList<Card>? faceDown2 = new List<Card>()
            {
                new Card(Value.Two, Suit.Heart),
                new Card(Value.King, Suit.Diamond),
            };
            IReadOnlyList<Card>? faceUp = new List<Card>()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Diamond),
                new Card(Value.Six, Suit.Club),
            };

            Column column1 = new(faceDown1, faceUp);
            Column column2 = new(faceDown2, faceUp);

            Assert.AreNotEqual(column1, column2);
        }

        [TestMethod]
        public void TestEqualsFaceUpDiffer()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Two, Suit.Heart),
                new Card(Value.King, Suit.Diamond),
            };
            IReadOnlyList<Card>? faceUp1 = new List<Card>()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Diamond),
                new Card(Value.Six, Suit.Club),
            };
            IReadOnlyList<Card>? faceUp2 = new List<Card>()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Heart),
                new Card(Value.Six, Suit.Club),
            };

            Column column1 = new(faceDown, faceUp1);
            Column column2 = new(faceDown, faceUp2);

            Assert.AreNotEqual(column1, column2);
        }

        [TestMethod]
        public void TestEqualsFaceUpShorter()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Two, Suit.Heart),
                new Card(Value.King, Suit.Diamond),
            };
            IReadOnlyList<Card>? faceUp1 = new List<Card>()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Diamond),
                new Card(Value.Six, Suit.Club),
            };
            IReadOnlyList<Card>? faceUp2 = Array.Empty<Card>();

            Column column1 = new(faceDown, faceUp1);
            Column column2 = new(faceDown, faceUp2);

            Assert.AreNotEqual(column1, column2);
        }

        [TestMethod]
        public void TestEqualsFaceUpLonger()
        {
            IReadOnlyList<Card>? faceDown = new List<Card>()
            {
                new Card(Value.Two, Suit.Heart),
                new Card(Value.King, Suit.Diamond),
            };
            IReadOnlyList<Card>? faceUp1 = Array.Empty<Card>();
            IReadOnlyList<Card>? faceUp2 = new List<Card>()
            {
                new Card(Value.Eight, Suit.Spade),
                new Card(Value.Seven, Suit.Diamond),
                new Card(Value.Six, Suit.Club),
            };

            Column column1 = new(faceDown, faceUp1);
            Column column2 = new(faceDown, faceUp2);

            Assert.AreNotEqual(column1, column2);
        }

        #endregion

    }
}
