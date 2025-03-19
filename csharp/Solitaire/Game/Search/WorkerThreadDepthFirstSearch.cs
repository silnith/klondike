using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace Silnith.Game.Search
{
    /// <summary>
    /// A game tree search algorithm implementation that performs a depth-first
    /// search running in parallel across multiple threads.
    /// </summary>
    /// <typeparam name="M">The move type for the game.</typeparam>
    /// <typeparam name="B">The board type for the game.</typeparam>
    public class WorkerThreadDepthFirstSearch<M, B> : GameTreeSearcher<M, B> where M : IMove<B>
    {
        private readonly int numWorkers;
        private readonly BlockingCollection<LinkedNode<GameState<M, B>>> stack;
        private readonly ConcurrentQueue<IReadOnlyList<GameState<M, B>>> wins;

        /// <summary>
        /// Constructs a depth-first search across the specified number of threads.
        /// </summary>
        /// <param name="game">The game.</param>
        /// <param name="initialGameState">The initial game state from which to begin the search.</param>
        /// <param name="numThreads">The number of threads to use for searching the game tree.</param>
        public WorkerThreadDepthFirstSearch(IGame<M, B> game, GameState<M, B> initialGameState, int numThreads)
            : base(game)
        {
            numWorkers = numThreads;
            stack = new BlockingCollection<LinkedNode<GameState<M, B>>>(new ConcurrentStack<LinkedNode<GameState<M, B>>>());
            wins = new ConcurrentQueue<IReadOnlyList<GameState<M, B>>>();

            stack.Add(new LinkedNode<GameState<M, B>>(initialGameState));
        }

        /// <inheritdoc/>
        protected override int QueueSize => stack.Count;

        /// <inheritdoc/>
        protected override int WinCount => wins.Count;

        /// <inheritdoc/>
        public override IEnumerable<IReadOnlyList<GameState<M, B>>> Search()
        {
            return SearchAsync(CancellationToken.None).GetAwaiter().GetResult();
        }

        /// <inheritdoc/>
        public override async Task<IEnumerable<IReadOnlyList<GameState<M, B>>>> SearchAsync(CancellationToken cancellationToken)
        {
            async Task DoWorkAsync()
            {
                await WorkAsync(cancellationToken);
            }

            List<Task> tasks = new List<Task>();
            for (int i = 0; i < numWorkers; i++)
            {
                tasks.Add(Task.Run(DoWorkAsync, cancellationToken));
            }

            await Task.WhenAll(tasks);

            return wins;
        }

        private Task WorkAsync(CancellationToken cancellationToken = default)
        {
            int millis = (int) TimeSpan.FromSeconds(1).TotalMilliseconds;
            while (stack.TryTake(out LinkedNode<GameState<M, B>> node, millis, cancellationToken))
            {
                ExamineNode(node);
            }
            return Task.CompletedTask;
        }

        /// <inheritdoc/>
        protected override void AddWin(IReadOnlyList<GameState<M, B>> node)
        {
            wins.Enqueue(node);
        }

        /// <inheritdoc/>
        protected override void QueueNode(LinkedNode<GameState<M, B>> node)
        {
            stack.Add(node);
        }
    }
}
