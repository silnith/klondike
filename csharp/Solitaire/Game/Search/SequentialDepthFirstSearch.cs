using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;

namespace Silnith.Game.Search
{
    public class SequentialDepthFirstSearch<M, B> where M : IMove<B>
    {
        private readonly IGame<M, B> game;
        private readonly Stack<LinkedNode<GameState<M, B>>> stack;
        private readonly ICollection<IReadOnlyList<GameState<M, B>>> wins;
        private long gameStatesExamined;
        private long boardsGenerated;
        private long movesPrunedTotal;
        private readonly ConcurrentDictionary<object, long> movesPruned;

        public SequentialDepthFirstSearch(IGame<M, B> game, GameState<M, B> initialGameState)
        {
            this.game = game;
            stack = new Stack<LinkedNode<GameState<M, B>>>();
            wins = new List<IReadOnlyList<GameState<M, B>>>();
            gameStatesExamined = 0;
            boardsGenerated = 0;
            movesPrunedTotal = 0;
            movesPruned = new ConcurrentDictionary<object, long>();
            foreach (IMoveFilter<M, B> filter in this.game.GetFilters())
            {
                movesPruned[filter.StatisticsKey] = 0;
            }

            stack.Push(new LinkedNode<GameState<M, B>>(initialGameState));
        }

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
                stack.Count,
                wins.Count);
            foreach (IMoveFilter<M, B> filter in game.GetFilters())
            {
                object statisticsKey = filter.StatisticsKey;
                Console.WriteLine(
                    "Moves pruned by filter {0}: {1:N0}",
                    statisticsKey,
                    movesPruned[statisticsKey]);
            }
        }

        public long NumberOfGameStatesExamined
        {
            get
            {
                return gameStatesExamined;
            }
        }

        public long BoardsGenerated
        {
            get
            {
                return boardsGenerated;
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
                    Interlocked.Increment(ref boardsGenerated);
                    GameState<M, B> newGameState = new GameState<M, B>(move, newBoard);
                    LinkedNode<GameState<M, B>> newHistory = new LinkedNode<GameState<M, B>>(newGameState, gameStateHistory);
                    bool broken = false;
                    foreach (IMoveFilter<M, B> filter in filters)
                    {
                        if (filter.ShouldFilter(newHistory))
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
