using Silnith.Game;
using Silnith.Game.Deck;
using Silnith.Game.Klondike;
using Silnith.Game.Klondike.Move;
using Silnith.Game.Klondike.Move.Filter;
using Silnith.Game.Search;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;

namespace Runner
{
    class Program
    {
        static int Main(string[] args)
        {
            List<Value> values = new(13)
            {
                Value.Ace,
                Value.Two,
                Value.Three,
                Value.Four,
                Value.Five,
                Value.Six,
                Value.Seven,
                Value.Eight,
                Value.Nine,
                Value.Ten,
                Value.Jack,
                Value.Queen,
                Value.King,
            };
            List<Suit> suits = new(4)
            {
                Suit.Club,
                Suit.Diamond,
                Suit.Heart,
                Suit.Spade,
            };
            List<Card> deck = new(52);
            foreach (Suit suit in suits)
            {
                foreach (Value value in values)
                {
                    deck.Add(new Card(value, suit));
                }
            }
            // shuffle
            Random random = new();
            for (int i = 0; i < deck.Count; i++)
            {
                int j = random.Next(0, deck.Count);
                (deck[i], deck[j]) = (deck[j], deck[i]);
            }

            Console.WriteLine($"[{string.Join(", ", deck)}]");

            Board emptyBoard = new(Array.Empty<Column>(), Array.Empty<Card>(), 0, new Dictionary<Suit, IReadOnlyList<Card>>());
            Klondike klondike = new();
            DealMove deal = new(deck, klondike.ColumnCount);
            Board dealtBoard = deal.Apply(emptyBoard);
            GameState<ISolitaireMove, Board> initialGameState = new(deal, dealtBoard);

            dealtBoard.PrintTo();

            Console.WriteLine("Processor count: {0:N}", Environment.ProcessorCount);

            //RunSearch0(klondike, initialGameState);
            SequentialDFS(klondike, initialGameState);

            return 0;
        }

        private static void RunSearch0(IGame<ISolitaireMove, Board> klondike, GameState<ISolitaireMove, Board> initialGameState)
        {
            Stack<LinkedNode<GameState<ISolitaireMove, Board>>> stack = new();
            Stack<IReadOnlyList<GameState<ISolitaireMove, Board>>> wins = new();
            stack.Push(new LinkedNode<GameState<ISolitaireMove, Board>>(initialGameState));

            IEnumerable<IMoveFilter<ISolitaireMove, Board>> filters = klondike.GetFilters();

            long nodesExamined = 0;
            long gameStatesPruned = 0;
            while (stack.TryPop(out LinkedNode<GameState<ISolitaireMove, Board>>? gameStateHistory))
            {
                nodesExamined++;
                IEnumerable<ISolitaireMove> moves = klondike.FindAllMoves(gameStateHistory);

                Console.WriteLine();
                Console.WriteLine("Scenario:");
                List<GameState<ISolitaireMove, Board>> history = new(gameStateHistory);
                history.Reverse();
                foreach (GameState<ISolitaireMove, Board> gameState in history)
                {
                    Console.WriteLine(gameState.Move);
                }
                Console.WriteLine("Moves made: {0:N0}", history.Count);
                Console.WriteLine("Game states to examine: {0:N0}", stack.Count);
                Board currentBoard = gameStateHistory.Value.Board;
                currentBoard.PrintTo();
                foreach (ISolitaireMove move in moves)
                {
                    Board newBoard = move.Apply(currentBoard);
                    GameState<ISolitaireMove, Board> newGameState = new(move, newBoard);
                    LinkedNode<GameState<ISolitaireMove, Board>> newHistory = new(newGameState, gameStateHistory);
                    Console.Write(move);
                    foreach (IMoveFilter<ISolitaireMove, Board> filter in filters)
                    {
                        if (filter.ShouldFilter(newHistory))
                        {
                            Console.Write(" (filtered by {0})", filter.StatisticsKey);
                        }
                    }
                    Console.WriteLine();
                }
                Console.WriteLine();

                foreach (ISolitaireMove move in moves)
                {
                    Board newBoard = move.Apply(currentBoard);
                    GameState<ISolitaireMove, Board> newGameState = new(move, newBoard);
                    LinkedNode<GameState<ISolitaireMove, Board>> newHistory = new(newGameState, gameStateHistory);
                    bool broken = false;
                    foreach (IMoveFilter<ISolitaireMove, Board> filter in filters)
                    {
                        if (filter.ShouldFilter(newHistory))
                        {
                            gameStatesPruned++;
                            broken = true;
                            break;
                        }
                    }
                    if (broken)
                    {
                        continue;
                    }

                    if (klondike.IsWin(newGameState))
                    {
                        wins.Push(newHistory);
                    }
                    else
                    {
                        stack.Push(newHistory);
                    }
                }

                if (nodesExamined % 10_000 == 0)
                {
                    Console.WriteLine("Nodes examined: {0:N0}", nodesExamined);
                    Console.WriteLine("Nodes pruned: {0:N0}", gameStatesPruned);
                    Console.WriteLine("Stack count: {0:N0}", stack.Count);
                    Console.WriteLine("Wins: {0:N0}", wins.Count);
                    Console.WriteLine();
                }
            }

            foreach (IReadOnlyList<GameState<ISolitaireMove, Board>> gameState in wins)
            {
                Console.WriteLine(gameState);
                gameState[0].Board.PrintTo();
            }
        }

        private static void SequentialDFS(IGame<ISolitaireMove, Board> game, GameState<ISolitaireMove, Board> initialState)
        {
            SequentialDepthFirstSearch<ISolitaireMove, Board> searcher = new(game, initialState);
            Task<IEnumerable<IReadOnlyList<GameState<ISolitaireMove, Board>>>> task = Task.Run(searcher.Search);

            long statesExamined = 0;
            long boardsGenerated = 0;
            while (!task.IsCompleted)
            {
                searcher.PrintStatistics();
                long nextStatesExamined = searcher.NumberOfGameStatesExamined;
                long nextBoardsGenerated = searcher.BoardsGenerated;
                long nodesPerSecond = nextStatesExamined - statesExamined;
                long boardsPerSecond = nextBoardsGenerated - boardsGenerated;
                Console.WriteLine("Nodes per second: {0:N0}", nodesPerSecond);
                Console.WriteLine("Boards per second: {0:N0}", boardsPerSecond);
                Console.WriteLine();
                statesExamined = nextStatesExamined;
                boardsGenerated = nextBoardsGenerated;
                Thread.Sleep(TimeSpan.FromSeconds(1));
            }

            IEnumerable<IReadOnlyList<GameState<ISolitaireMove, Board>>> wins = task.GetAwaiter().GetResult();

            foreach (IReadOnlyList<GameState<ISolitaireMove, Board>> win in wins)
            {
                Console.WriteLine(win);
                win[0].Board.PrintTo();
            }

            searcher.PrintStatistics();
        }

        private static void ParallelDFS(IGame<ISolitaireMove, Board> game, GameState<ISolitaireMove, Board> initialState, int numThreads)
        {
            WorkerThreadDepthFirstSearch<ISolitaireMove, Board> searcher = new(game, initialState, numThreads);
            async Task RunAsync()
            {
                IEnumerable<IReadOnlyList<GameState<ISolitaireMove, Board>>> wins = await searcher.SearchAsync();

                foreach (IReadOnlyList<GameState<ISolitaireMove, Board>> win in wins)
                {
                    Console.WriteLine(win);
                    win[0].Board.PrintTo();
                }
            }
            Task task = Task.Run(RunAsync);

            long statesExamined = 0;
            while (!task.IsCompleted)
            {
                searcher.PrintStatistics();
                long nextStatesExamined = searcher.NumberOfGameStatesExamined;
                long nodesPerSecond = nextStatesExamined - statesExamined;
                Console.WriteLine("Nodes per second: {0:N0}", nodesPerSecond);
                statesExamined = nextStatesExamined;
                Thread.Sleep(TimeSpan.FromSeconds(1));
            }

            task.GetAwaiter().GetResult();
            searcher.PrintStatistics();
        }
    }
}
