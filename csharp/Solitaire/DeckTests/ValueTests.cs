using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Diagnostics.CodeAnalysis;

namespace Silnith.Game.Deck.Tests
{
    [TestClass]
    [SuppressMessage("Design", "MSTEST0032:Assertion condition is always true", Justification = "The unit test is making sure the enum is defined properly.")]
    public class ValueTests
    {
        [TestMethod]
        public void TestGetValueAce()
        {
            Assert.AreEqual(1, Value.Ace.GetValue());
        }

        [TestMethod]
        public void TestGetValueTwo()
        {
            Assert.AreEqual(2, Value.Two.GetValue());
        }

        [TestMethod]
        public void TestGetValueThree()
        {
            Assert.AreEqual(3, Value.Three.GetValue());
        }

        [TestMethod]
        public void TestGetValueFour()
        {
            Assert.AreEqual(4, Value.Four.GetValue());
        }

        [TestMethod]
        public void TestGetValueFive()
        {
            Assert.AreEqual(5, Value.Five.GetValue());
        }

        [TestMethod]
        public void TestGetValueSix()
        {
            Assert.AreEqual(6, Value.Six.GetValue());
        }

        [TestMethod]
        public void TestGetValueSeven()
        {
            Assert.AreEqual(7, Value.Seven.GetValue());
        }

        [TestMethod]
        public void TestGetValueEight()
        {
            Assert.AreEqual(8, Value.Eight.GetValue());
        }

        [TestMethod]
        public void TestGetValueNine()
        {
            Assert.AreEqual(9, Value.Nine.GetValue());
        }

        [TestMethod]
        public void TestGetValueTen()
        {
            Assert.AreEqual(10, Value.Ten.GetValue());
        }

        [TestMethod]
        public void TestGetValueJack()
        {
            Assert.AreEqual(11, Value.Jack.GetValue());
        }

        [TestMethod]
        public void TestGetValueQueen()
        {
            Assert.AreEqual(12, Value.Queen.GetValue());
        }

        [TestMethod]
        public void TestGetValueKing()
        {
            Assert.AreEqual(13, Value.King.GetValue());
        }

        [TestMethod]
        public void TestAceLessThanTwo()
        {
            Assert.IsTrue(Value.Ace < Value.Two);
        }

        [TestMethod]
        public void TestTwoGreaterThanAce()
        {
            Assert.IsTrue(Value.Two > Value.Ace);
        }

        [TestMethod]
        public void TestTenLessThanJack()
        {
            Assert.IsTrue(Value.Ten < Value.Jack);
        }

        [TestMethod]
        public void TestJackGreaterThanTen()
        {
            Assert.IsTrue(Value.Jack > Value.Ten);
        }

        [TestMethod]
        public void TestJackLessThanQueen()
        {
            Assert.IsTrue(Value.Jack < Value.Queen);
        }

        [TestMethod]
        public void TestQueenGreaterThanJack()
        {
            Assert.IsTrue(Value.Queen > Value.Jack);
        }

        [TestMethod]
        public void TestQueenLessThanKing()
        {
            Assert.IsTrue(Value.Queen < Value.King);
        }

        [TestMethod]
        public void TestKingGreaterThanQueen()
        {
            Assert.IsTrue(Value.King > Value.Queen);
        }

        [TestMethod]
        public void TestToStringAce()
        {
            Assert.AreEqual("Ace", Value.Ace.ToString());
        }

        [TestMethod]
        public void TestToStringTwo()
        {
            Assert.AreEqual("Two", Value.Two.ToString());
        }

        [TestMethod]
        public void TestToStringThree()
        {
            Assert.AreEqual("Three", Value.Three.ToString());
        }

        [TestMethod]
        public void TestToStringFour()
        {
            Assert.AreEqual("Four", Value.Four.ToString());
        }

        [TestMethod]
        public void TestToStringFive()
        {
            Assert.AreEqual("Five", Value.Five.ToString());
        }

        [TestMethod]
        public void TestToStringSix()
        {
            Assert.AreEqual("Six", Value.Six.ToString());
        }

        [TestMethod]
        public void TestToStringSeven()
        {
            Assert.AreEqual("Seven", Value.Seven.ToString());
        }

        [TestMethod]
        public void TestToStringEight()
        {
            Assert.AreEqual("Eight", Value.Eight.ToString());
        }

        [TestMethod]
        public void TestToStringNine()
        {
            Assert.AreEqual("Nine", Value.Nine.ToString());
        }

        [TestMethod]
        public void TestToStringTen()
        {
            Assert.AreEqual("Ten", Value.Ten.ToString());
        }

        [TestMethod]
        public void TestToStringJack()
        {
            Assert.AreEqual("Jack", Value.Jack.ToString());
        }

        [TestMethod]
        public void TestToStringQueen()
        {
            Assert.AreEqual("Queen", Value.Queen.ToString());
        }

        [TestMethod]
        public void TestToStringKing()
        {
            Assert.AreEqual("King", Value.King.ToString());
        }
    }
}
