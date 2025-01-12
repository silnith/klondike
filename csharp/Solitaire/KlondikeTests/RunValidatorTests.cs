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
    public class RunValidatorTests
    {
        [TestMethod]
        public void TestValidate()
        {
            IReadOnlyList<Card> run = new List<Card>()
            {
                new Card(Value.King, Suit.Spade),
                new Card(Value.Queen, Suit.Heart),
                new Card(Value.Jack, Suit.Spade),
                new Card(Value.Ten, Suit.Heart),
                new Card(Value.Nine, Suit.Spade),
                new Card(Value.Eight, Suit.Heart),
                new Card(Value.Seven, Suit.Spade),
                new Card(Value.Six, Suit.Heart),
                new Card(Value.Five, Suit.Spade),
                new Card(Value.Four, Suit.Heart),
                new Card(Value.Three, Suit.Spade),
                new Card(Value.Two, Suit.Heart),
                new Card(Value.Ace, Suit.Spade),
            };

            RunValidator validator = new();

            validator.Validate(run);
        }

        [TestMethod]
        public void TestRedOnRed()
        {
            IReadOnlyList<Card> run = new List<Card>()
            {
                new Card(Value.Two, Suit.Heart),
                new Card(Value.Ace, Suit.Diamond),
            };

            RunValidator validator = new();

            Assert.ThrowsException<ArgumentException>(() =>
            {
                validator.Validate(run);
            });
        }

        [TestMethod]
        public void TestBackwards()
        {
            IReadOnlyList<Card> run = new List<Card>()
            {
                new Card(Value.Two, Suit.Heart),
                new Card(Value.Three, Suit.Spade),
            };

            RunValidator validator = new();

            Assert.ThrowsException<ArgumentException>(() =>
            {
                validator.Validate(run);
            });
        }

        [TestMethod]
        public void TestSameValue()
        {
            IReadOnlyList<Card> run = new List<Card>()
            {
                new Card(Value.Three, Suit.Heart),
                new Card(Value.Three, Suit.Spade),
            };

            RunValidator validator = new();

            Assert.ThrowsException<ArgumentException>(() =>
            {
                validator.Validate(run);
            });
        }
    }
}
