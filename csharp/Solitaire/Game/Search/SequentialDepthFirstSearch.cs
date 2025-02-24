using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;

namespace Silnith.Game.Search
{
    /// <summary>
    /// A game tree search algorithm implementation that performs a depth-first
    /// search running sequentially on a single thread.
    /// </summary>
    /// <typeparam name="M">The move type for the game.</typeparam>
    /// <typeparam name="B">The board type for the game.</typeparam>
    public class SequentialDepthFirstSearch<M, B> : GameTreeSearcher<M, B> where M : IMove<B>
    {
        private readonly Stack<LinkedNode<GameState<M, B>>> stack;
        private readonly ICollection<IReadOnlyList<GameState<M, B>>> wins;

        /// <summary>
        /// Initializes a sequential single-threaded depth-first search.
        /// </summary>
        /// <param name="game">The game.</param>
        /// <param name="initialGameState">The initial game state.</param>
        public SequentialDepthFirstSearch(IGame<M, B> game, GameState<M, B> initialGameState)
            : base(game)
        {
            stack = new Stack<LinkedNode<GameState<M, B>>>();
            wins = new List<IReadOnlyList<GameState<M, B>>>();

            stack.Push(new LinkedNode<GameState<M, B>>(initialGameState));
        }

        /// <inheritdoc/>
        public override IEnumerable<IReadOnlyList<GameState<M, B>>> Search()
        {
            while (stack.TryPop(out LinkedNode<GameState<M, B>> gameStateHistory))
            {
                ExamineNode(gameStateHistory);
            }

            return wins;
        }

        /// <inheritdoc/>
        public override Task<IEnumerable<IReadOnlyList<GameState<M, B>>>> SearchAsync(CancellationToken cancellationToken)
        {
            return Task.FromResult(Search());
        }

        /// <inheritdoc/>
        protected override int QueueSize => stack.Count;

        /// <inheritdoc/>
        protected override int WinCount => wins.Count;

        /// <inheritdoc/>
        protected override void AddWin(IReadOnlyList<GameState<M, B>> node)
        {
            wins.Add(node);
        }

        /// <inheritdoc/>
        protected override void QueueNode(LinkedNode<GameState<M, B>> node)
        {
            stack.Push(node);
        }
    }
}
