using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Silnith.Game.Deck.Tests
{
    [TestClass]
    public class CardTests
    {
        [TestMethod]
        public void TestCard()
        {
            Card _ = new(Value.Eight, Suit.Heart);
        }

        [TestMethod]
        public void TestGetValue()
        {
            Card card = new(Value.Eight, Suit.Heart);

            Assert.AreEqual(Value.Eight, card.Value);
        }

        [TestMethod]
        public void TestGetSuit()
        {
            Card card = new(Value.Eight, Suit.Heart);

            Assert.AreEqual(Suit.Heart, card.Suit);
        }

        [TestMethod]
        public void TestEquals()
        {
            Card card1 = new(Value.Eight, Suit.Heart);
            Card card2 = new(Value.Eight, Suit.Heart);

            Assert.AreEqual(card1, card2);
        }

        [TestMethod]
        public void TestHashCode()
        {
            Card card1 = new(Value.Eight, Suit.Heart);
            Card card2 = new(Value.Eight, Suit.Heart);

            Assert.AreEqual(card1.GetHashCode(), card2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsDifferentValue()
        {
            Card card1 = new(Value.Eight, Suit.Heart);
            Card card2 = new(Value.Seven, Suit.Heart);

            Assert.AreNotEqual(card1, card2);
        }

        [TestMethod]
        public void TestHashCodeDifferentValue()
        {
            Card card1 = new(Value.Eight, Suit.Heart);
            Card card2 = new(Value.Seven, Suit.Heart);

            Assert.AreNotEqual(card1.GetHashCode(), card2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsDifferentSuit()
        {
            Card card1 = new(Value.Eight, Suit.Heart);
            Card card2 = new(Value.Eight, Suit.Diamond);

            Assert.AreNotEqual(card1, card2);
        }

        [TestMethod]
        public void TestHashCodeDifferentSuit()
        {
            Card card1 = new(Value.Eight, Suit.Heart);
            Card card2 = new(Value.Eight, Suit.Diamond);

            Assert.AreNotEqual(card1.GetHashCode(), card2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsDifferentValueAndSuit()
        {
            Card card1 = new(Value.Eight, Suit.Heart);
            Card card2 = new(Value.Seven, Suit.Diamond);

            Assert.AreNotEqual(card1, card2);
        }

        [TestMethod]
        public void TestHashCodeDifferentValueAndSuit()
        {
            Card card1 = new(Value.Eight, Suit.Heart);
            Card card2 = new(Value.Seven, Suit.Diamond);

            Assert.AreNotEqual(card1.GetHashCode(), card2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsDifferentType()
        {
            Card card = new(Value.Eight, Suit.Heart);

            Assert.AreNotEqual<object>(card, "Eight of Hearts");
        }

        [TestMethod]
        public void TestToString()
        {
            Card card = new(Value.Eight, Suit.Heart);

            Assert.AreEqual("Eight of Hearts", card.ToString());
        }
    }
}
