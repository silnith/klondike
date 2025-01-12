using Microsoft.VisualStudio.TestTools.UnitTesting;

using Moq;

namespace Silnith.Game.Tests
{
    [TestClass]
    public class GameStateTests
    {
        public interface ITestBoard
        {
        }

        public interface ITestMove : IMove<ITestBoard>
        {
        }

        private readonly Mock<ITestMove> mockMove = new();

        private readonly Mock<ITestBoard> mockBoard = new();

        private LinkedNode<ITestMove> moves;

        private LinkedNode<ITestBoard> boards;

        [TestInitialize]
        public void SetUp()
        {
            moves = new(mockMove.Object);
            boards = new(mockBoard.Object);
        }

        [TestMethod]
        public void TestPrimaryConstructor()
        {
            GameState<ITestMove, ITestBoard> gameState = new(moves, boards);

            Assert.AreSame(moves, gameState.Moves);
            Assert.AreSame(boards, gameState.Boards);
        }

        [TestMethod]
        public void TestSimpleConstructor()
        {
            GameState<ITestMove, ITestBoard> gameState = new(mockMove.Object, mockBoard.Object);

            Assert.AreEqual(moves, gameState.Moves);
            Assert.AreNotSame(moves, gameState.Moves);

            Assert.AreEqual(boards, gameState.Boards);
            Assert.AreNotSame(boards, gameState.Boards);
        }
    }
}
