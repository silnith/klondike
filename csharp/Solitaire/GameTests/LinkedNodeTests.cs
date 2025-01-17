using Microsoft.VisualStudio.TestTools.UnitTesting;

using System;
using System.Collections.Generic;

namespace Silnith.Game.Tests
{
    [TestClass]
    public class LinkedNodeTests
    {
        #region Equals & GetHashCode

        [TestMethod]
        public void TestEquals()
        {
            LinkedNode<string> list1 = new("foo");
            LinkedNode<string> list2 = new("foo");

            Assert.AreEqual(list1, list2);
        }

        [TestMethod]
        public void TestHashCode()
        {
            LinkedNode<string> list1 = new("foo");
            LinkedNode<string> list2 = new("foo");

            Assert.AreEqual(list1.GetHashCode(), list2.GetHashCode());
        }

        [TestMethod]
        public void TestEqualsUsingDifferentConstructor()
        {
            LinkedNode<string> list1 = new("foo");
            LinkedNode<string> list2 = new("foo", null);

            Assert.AreEqual(list1, list2);
        }

        [TestMethod]
        public void TestHashCodeUsingDifferentConstructor()
        {
            LinkedNode<string> list1 = new("foo");
            LinkedNode<string> list2 = new("foo", null);

            Assert.AreEqual(list1.GetHashCode(), list2.GetHashCode());
        }

        #endregion

        [TestMethod]
        public void TestEnumeratorExists()
        {
            LinkedNode<string> list = new("foo");

            IEnumerator<string> enumerator = list.GetEnumerator();

            Assert.IsNotNull(enumerator);
        }

        [TestMethod]
        public void TestEnumeratorMoveNext()
        {
            LinkedNode<string> list = new("foo");

            IEnumerator<string> enumerator = list.GetEnumerator();

            Assert.IsTrue(enumerator.MoveNext());
            Assert.AreEqual("foo", enumerator.Current);
        }

        [TestMethod]
        public void TestEnumeratorSecondMoveNext()
        {
            LinkedNode<string> list = new("foo");

            IEnumerator<string> enumerator = list.GetEnumerator();

            Assert.IsTrue(enumerator.MoveNext());
            Assert.IsFalse(enumerator.MoveNext());
        }

        [TestMethod]
        public void TestCount()
        {
            LinkedNode<string> list = new("foo");

            Assert.AreEqual(1, list.Count);
        }

        [TestMethod]
        public void TestIndex()
        {
            LinkedNode<string> list = new("foo");

            Assert.AreEqual("foo", list[0]);
        }

        [TestMethod]
        public void TestIndexNegative()
        {
            LinkedNode<string> list = new("foo");

            void action()
            {
                string _ = list[-1];
            }
            Assert.ThrowsException<ArgumentOutOfRangeException>(action);
        }

        [TestMethod]
        public void TestIndexTooLarge()
        {
            LinkedNode<string> list = new("foo");

            void action()
            {
                string _ = list[1];
            }
            Assert.ThrowsException<ArgumentOutOfRangeException>(action);
        }
    }
}
