using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Silnith.Game.Deck.Tests
{
    [TestClass]
    public class SuitTests
    {
        [TestMethod]
        public void TestGetColorClub()
        {
            Assert.AreEqual(Color.Black, Suit.Club.GetColor());
        }

        [TestMethod]
        public void TestGetColorDiamond()
        {
            Assert.AreEqual(Color.Red, Suit.Diamond.GetColor());
        }

        [TestMethod]
        public void TestGetColorHeart()
        {
            Assert.AreEqual(Color.Red, Suit.Heart.GetColor());
        }

        [TestMethod]
        public void TestGetColorSpade()
        {
            Assert.AreEqual(Color.Black, Suit.Spade.GetColor());
        }

        [TestMethod]
        public void TestToStringClub()
        {
            Assert.AreEqual("Club", Suit.Club.ToString());
        }

        [TestMethod]
        public void TestToStringDiamond()
        {
            Assert.AreEqual("Diamond", Suit.Diamond.ToString());
        }

        [TestMethod]
        public void TestToStringHeart()
        {
            Assert.AreEqual("Heart", Suit.Heart.ToString());
        }

        [TestMethod]
        public void TestToStringSpade()
        {
            Assert.AreEqual("Spade", Suit.Spade.ToString());
        }
    }
}
