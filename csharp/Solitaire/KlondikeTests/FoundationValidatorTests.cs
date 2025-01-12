using Microsoft.VisualStudio.TestTools.UnitTesting;
using Silnith.Game.Deck;
using System;
using System.Collections.Generic;

namespace Silnith.Game.Klondike.Tests
{
    [TestClass]
    public class FoundationValidatorTests
    {
        [TestMethod]
        public void TestValidateEmpty()
        {
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation = new Dictionary<Suit, IReadOnlyList<Card>>();

            FoundationValidator validator = new();

            Assert.ThrowsException<ArgumentException>(() =>
            {
                validator.Validate(foundation);
            });
        }

        [TestMethod]
        public void TestValidateNoCards()
        {
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                { Suit.Club, Array.Empty<Card>() },
                { Suit.Diamond, Array.Empty<Card>() },
                { Suit.Heart, Array.Empty<Card>() },
                { Suit.Spade, Array.Empty<Card>() },
            };

            FoundationValidator validator = new();

            validator.Validate(foundation);
        }

        [TestMethod]
        public void TestValidateAllCards()
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
                        new Card(Value.King, Suit.Club),
                    }
                },
                {
                    Suit.Diamond,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Diamond),
                        new Card(Value.Two, Suit.Diamond),
                        new Card(Value.Three, Suit.Diamond),
                        new Card(Value.Four, Suit.Diamond),
                        new Card(Value.Five, Suit.Diamond),
                        new Card(Value.Six, Suit.Diamond),
                        new Card(Value.Seven, Suit.Diamond),
                        new Card(Value.Eight, Suit.Diamond),
                        new Card(Value.Nine, Suit.Diamond),
                        new Card(Value.Ten, Suit.Diamond),
                        new Card(Value.Jack, Suit.Diamond),
                        new Card(Value.Queen, Suit.Diamond),
                        new Card(Value.King, Suit.Diamond),
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
                        new Card(Value.Five, Suit.Heart),
                        new Card(Value.Six, Suit.Heart),
                        new Card(Value.Seven, Suit.Heart),
                        new Card(Value.Eight, Suit.Heart),
                        new Card(Value.Nine, Suit.Heart),
                        new Card(Value.Ten, Suit.Heart),
                        new Card(Value.Jack, Suit.Heart),
                        new Card(Value.Queen, Suit.Heart),
                        new Card(Value.King, Suit.Heart),
                    }
                },
                {
                    Suit.Spade,
                    new List<Card>()
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
                    }
                },
            };

            FoundationValidator validator = new();

            validator.Validate(foundation);
        }

        [TestMethod]
        public void TestValidateWrongSuitAce()
        {
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                {
                    Suit.Club,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Heart),
                    }
                },
                {
                    Suit.Diamond,
                    Array.Empty<Card>()
                },
                {
                    Suit.Heart,
                    Array.Empty<Card>()
                },
                {
                    Suit.Spade,
                    Array.Empty<Card>()
                },
            };

            FoundationValidator validator = new();

            Assert.ThrowsException<ArgumentException>(() =>
            {
                validator.Validate(foundation);
            });
        }

        [TestMethod]
        public void TestValidateWrongSuitNotAce()
        {
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                {
                    Suit.Club,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Club),
                        new Card(Value.Two, Suit.Heart),
                    }
                },
                {
                    Suit.Diamond,
                    Array.Empty<Card>()
                },
                {
                    Suit.Heart,
                    Array.Empty<Card>()
                },
                {
                    Suit.Spade,
                    Array.Empty<Card>()
                },
            };

            FoundationValidator validator = new();

            Assert.ThrowsException<ArgumentException>(() =>
            {
                validator.Validate(foundation);
            });
        }

        [TestMethod]
        public void TestValidateNotStartWithAce()
        {
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                {
                    Suit.Club,
                    new List<Card>()
                    {
                        new Card(Value.Two, Suit.Club),
                    }
                },
                {
                    Suit.Diamond,
                    Array.Empty<Card>()
                },
                {
                    Suit.Heart,
                    Array.Empty<Card>()
                },
                {
                    Suit.Spade,
                    Array.Empty<Card>()
                },
            };

            FoundationValidator validator = new();

            Assert.ThrowsException<ArgumentException>(() =>
            {
                validator.Validate(foundation);
            });
        }

        [TestMethod]
        public void TestValidateOutOfOrder()
        {
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation = new Dictionary<Suit, IReadOnlyList<Card>>()
            {
                {
                    Suit.Club,
                    new List<Card>()
                    {
                        new Card(Value.Ace, Suit.Club),
                        new Card(Value.Three, Suit.Club),
                    }
                },
                {
                    Suit.Diamond,
                    Array.Empty<Card>()
                },
                {
                    Suit.Heart,
                    Array.Empty<Card>()
                },
                {
                    Suit.Spade,
                    Array.Empty<Card>()
                },
            };

            FoundationValidator validator = new();

            Assert.ThrowsException<ArgumentException>(() =>
            {
                validator.Validate(foundation);
            });
        }
    }
}
