using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Threading;

namespace Silnith.Game.Search
{
    public class SequentialDepthFirstSearch<M, B> where M : IMove<B>
    {
        private readonly IGame<M, B> game;
        private readonly Stack<LinkedNode<GameState<M, B>>> stack;
        private readonly ICollection<IReadOnlyList<GameState<M, B>>> wins;
        private long gameStatesExamined;
        private long gameStatesPrunedTotal;
        private readonly ConcurrentDictionary<object, long> gameStatesPruned;

        public SequentialDepthFirstSearch(IGame<M, B> game, GameState<M, B> initialGameState)
        {
            this.game = game;
            stack = new Stack<LinkedNode<GameState<M, B>>>();
            wins = new List<IReadOnlyList<GameState<M, B>>>();
            gameStatesExamined = 0;
            gameStatesPrunedTotal = 0;
            gameStatesPruned = new ConcurrentDictionary<object, long>();
            foreach (IMoveFilter<M, B> filter in this.game.GetFilters())
            {
                gameStatesPruned[filter.StatisticsKey] = 0;
            }

            stack.Push(new LinkedNode<GameState<M, B>>(initialGameState));
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

        public IEnumerable<IReadOnlyList<GameState<M, B>>> Search()
        {
            IEnumerable<IMoveFilter<M, B>> filters = game.GetFilters();

            while (stack.TryPop(out LinkedNode<GameState<M, B>> gameStateHistory))
            {
                Interlocked.Increment(ref gameStatesExamined);
                GameState<M, B> gameState = gameStateHistory.Value;
                B board = gameState.Board;
                foreach (M move in game.FindAllMoves(gameStateHistory))
                {
                    B newBoard = move.Apply(board);
                    GameState<M, B> newGameState = new GameState<M, B>(move, newBoard);
                    LinkedNode<GameState<M, B>> newHistory = new LinkedNode<GameState<M, B>>(newGameState, gameStateHistory);
                    bool broken = false;
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
                        wins.Add(newHistory);
                    }
                    else
                    {
                        stack.Push(newHistory);
                    }
                }
            }
            return wins;
        }
    }
}
