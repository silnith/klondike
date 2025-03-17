using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;

namespace Silnith.Game.Search
{
    /// <summary>
    /// Abstract base class for game tree search algorithms.
    /// This provides a common implementation of the logic to expand a single node
    /// in the game tree.
    /// </summary>
    /// <typeparam name="M">The move type for the game.</typeparam>
    /// <typeparam name="B">The board type for the game.</typeparam>
    public abstract class GameTreeSearcher<M, B> where M : IMove<B>
    {
        private readonly IGame<M, B> game;
        private readonly IEnumerable<IMoveFilter<M, B>> gameFilters;
        private long gameStatesExamined;
        private long boardsGenerated;
        private long movesPrunedTotal;
        private readonly ConcurrentDictionary<object, long> movesPruned;

        /// <summary>
        /// Initializes a game tree search run for the provided game.
        /// </summary>
        /// <param name="game">The game.</param>
        protected GameTreeSearcher(IGame<M, B> game)
        {
            this.game = game ?? throw new ArgumentNullException(nameof(game));
            gameFilters = this.game.GetFilters();
            gameStatesExamined = 0;
            boardsGenerated = 0;
            movesPrunedTotal = 0;
            movesPruned = new ConcurrentDictionary<object, long>();
            foreach (IMoveFilter<M, B> filter in gameFilters)
            {
                movesPruned[filter.StatisticsKey] = 0;
            }
        }

        /// <summary>
        /// Searches the game tree.
        /// </summary>
        /// <returns>All winning game states found.</returns>
        public abstract IEnumerable<IReadOnlyList<GameState<M, B>>> Search();

        /// <summary>
        /// Searches the game tree.
        /// </summary>
        /// <param name="cancellationToken">A cancellation token.</param>
        /// <returns>All winning game states found.</returns>
        public abstract Task<IEnumerable<IReadOnlyList<GameState<M, B>>>> SearchAsync(CancellationToken cancellationToken = default);

        /// <summary>
        /// The current size of the queue of nodes to be searched.
        /// </summary>
        protected abstract int QueueSize
        {
            get;
        }

        /// <summary>
        /// The current count of winning game states found.
        /// </summary>
        protected abstract int WinCount
        {
            get;
        }

        /// <summary>
        /// Prints statistics on the game tree search to the console.
        /// </summary>
        public void PrintStatistics()
        {
            Console.WriteLine(
                "Nodes examined: {0:N0}\n"
                + "Boards generated: {1:N0}\n"
                + "Moves pruned: {2:N0}\n"
                + "Queue size: {3:N0}\n"
                + "Wins: {4:N0}",
                gameStatesExamined,
                boardsGenerated,
                movesPrunedTotal,
                QueueSize,
                WinCount);
            foreach (IMoveFilter<M, B> filter in gameFilters)
            {
                object statisticsKey = filter.StatisticsKey;
                Console.WriteLine(
                    "Moves pruned by filter {0}: {1:N0}",
                    statisticsKey,
                    movesPruned[statisticsKey]);
            }
        }

        /// <summary>
        /// The total number of game states examined.
        /// </summary>
        public long NumberOfGameStatesExamined
        {
            get
            {
                return gameStatesExamined;
            }
        }

        /// <summary>
        /// The total number of boards generated.
        /// </summary>
        public long BoardsGenerated
        {
            get
            {
                return boardsGenerated;
            }
        }

        /// <summary>
        /// Queues a game tree node for searching.
        /// </summary>
        /// <param name="node">The game tree node to search.</param>
        protected abstract void QueueNode(LinkedNode<GameState<M, B>> node);

        /// <summary>
        /// Captures a sequence of game states that culminates in a win.
        /// </summary>
        /// <param name="node">The game state history that results in a win.</param>
        protected abstract void AddWin(IReadOnlyList<GameState<M, B>> node);

        /// <summary>
        /// Examines a single node in the game tree.  This enumerates all possible
        /// moves, applies the move filters, and examines unfiltered moves to see if
        /// they are a winning game state.  Winning game states are passed to
        /// <see cref="AddWin(IReadOnlyList{GameState{M, B}})"/>.  All other unfiltered
        /// game states are passed to
        /// <see cref="QueueNode(LinkedNode{GameState{M, B}})"/>.
        /// </summary>
        /// <param name="node">The node containing the game state to examine.</param>
        protected void ExamineNode(LinkedNode<GameState<M, B>> node)
        {
            Interlocked.Increment(ref gameStatesExamined);
            GameState<M, B> gameState = node.Value;
            B board = gameState.Board;
            IEnumerable<M> moves = game.FindAllMoves(node);
            foreach (M move in moves)
            {
                B newBoard = move.Apply(board);
                Interlocked.Increment(ref boardsGenerated);
                GameState<M, B> newGameState = new GameState<M, B>(move, newBoard);
                LinkedNode<GameState<M, B>> newNode = new LinkedNode<GameState<M, B>>(newGameState, node);
                bool broken = false;
                foreach (IMoveFilter<M, B> filter in gameFilters)
                {
                    if (filter.ShouldFilter(newNode))
                    {
                        Interlocked.Increment(ref movesPrunedTotal);
                        object key = filter.StatisticsKey;
                        long value = movesPruned[key];
                        while (!movesPruned.TryUpdate(key, value + 1, value))
                        {
                            value = movesPruned[key];
                        }
                        broken = true;
                        break;
                    }
                }
                if (broken)
                {
                    continue;
                }

                if (game.IsWin(newNode))
                {
                    AddWin(newNode);
                }
                else
                {
                    QueueNode(newNode);
                }
            }
        }

    }
}
