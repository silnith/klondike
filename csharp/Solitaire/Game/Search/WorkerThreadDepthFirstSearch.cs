using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Silnith.Game.Search
{
    public class WorkerThreadDepthFirstSearch<M, B> where M : IMove<B>
    {
        private readonly IGame<M, B> game;
        private readonly List<Thread> threads;
        private readonly System.Collections.Concurrent.ConcurrentStack<LinkedNode<GameState<M, B>>> stack;
        private readonly System.Collections.Concurrent.ConcurrentQueue<IReadOnlyList<GameState<M, B>>> wins;
        private long gameStatesExamined;
        private long gameStatesPrunedTotal;
        private readonly ConcurrentDictionary<object, long> gameStatesPruned;
        private volatile bool cancelled;

        public WorkerThreadDepthFirstSearch(IGame<M, B> game, GameState<M, B> initialGameState, int numThreads)
        {
            this.game = game ?? throw new ArgumentNullException(nameof(game));
            this.threads = new List<Thread>(numThreads);
            this.stack = new System.Collections.Concurrent.ConcurrentStack<LinkedNode<GameState<M, B>>>();
            this.wins = new System.Collections.Concurrent.ConcurrentQueue<IReadOnlyList<GameState<M, B>>>();
            this.gameStatesExamined = 0;
            this.gameStatesPrunedTotal = 0;
            this.gameStatesPruned = new ConcurrentDictionary<object, long>();
            foreach (IMoveFilter<M, B> filter in this.game.GetFilters())
            {
                gameStatesPruned[filter.StatisticsKey] = 0;
            }
            this.cancelled = false;

            this.stack.Push(new LinkedNode<GameState<M, B>>(initialGameState));

            for (int i = 0; i < numThreads; i++)
            {
                threads.Add(new Thread(Work));
            }
        }

        public void PrintStatistics()
        {
            Console.WriteLine(
                "Nodes examined: {0:N0}\n"
                + "Nodes pruned: {1:N0}\n"
                + "Queue size: {2:N0}\n"
                + "Wins: {3:N0}",
                gameStatesExamined,
                gameStatesPrunedTotal,
                stack.Count,
                wins.Count);
            foreach (KeyValuePair<object, long> keyValuePair in gameStatesPruned)
            {
                Console.WriteLine(
                    "Nodes pruned by filter {0}: {1:N0}",
                    keyValuePair.Key,
                    keyValuePair.Value);
            }
        }

        public long NumberOfGameStatesExamined
        {
            get
            {
                return gameStatesExamined;
            }
        }

        public async Task<IEnumerable<IReadOnlyList<GameState<M, B>>>> SearchAsync()
        {
            foreach (Thread thread in threads)
            {
                thread.Start();
            }

            foreach (Thread thread in threads)
            {
                thread.Join();
            }

            return wins;
        }

        public void Work()
        {
            IEnumerable<IMoveFilter<M, B>> filters = game.GetFilters();

            do
            {
                while (stack.TryPop(out LinkedNode<GameState<M, B>> gameStateHistory))
                {
                    Interlocked.Increment(ref gameStatesExamined);
                    GameState<M, B> gameState = gameStateHistory.Value;
                    B board = gameState.Board;
                    IEnumerable<M> moves = game.FindAllMoves(gameStateHistory);
                    bool broken = false;
                    foreach (M move in moves)
                    {
                        B newBoard = move.Apply(board);
                        GameState<M, B> newGameState = new GameState<M, B>(move, newBoard);
                        LinkedNode<GameState<M, B>> newHistory = new LinkedNode<GameState<M, B>>(newGameState, gameStateHistory);
                        foreach (IMoveFilter<M, B> filter in filters)
                        {
                            if (filter.ShouldFilter(newHistory))
                            {
                                Interlocked.Increment(ref gameStatesPrunedTotal);
                                object key = filter.StatisticsKey;
                                long value = gameStatesPruned[key];
                                while (!gameStatesPruned.TryUpdate(key, value + 1, value))
                                {
                                    value = gameStatesPruned[key];
                                }
                                broken = true;
                                break;
                            }
                        }
                        if (broken)
                        {
                            continue;
                        }

                        if (game.IsWin(newGameState))
                        {
                            wins.Enqueue(newHistory);
                        }
                        else
                        {
                            stack.Push(newHistory);
                        }
                    }
                }
                Thread.Sleep(TimeSpan.FromSeconds(1));
            } while (!cancelled);
        }
    }
}
