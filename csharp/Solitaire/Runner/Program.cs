using Silnith.Game;
using Silnith.Game.Deck;
using Silnith.Game.Klondike;
using Silnith.Game.Klondike.Move;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;

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

            ConcurrentStack<GameState<ISolitaireMove, Board>> stack = new();
            ConcurrentStack<GameState<ISolitaireMove, Board>> wins = new();
            stack.Push(initialGameState);

            Console.WriteLine("Processor count: {0:N}", Environment.ProcessorCount);

            long nodesExamined = 0;
            long gameStatesPruned = 0;
            while (stack.TryPop(out GameState<ISolitaireMove, Board>? currentGameState))
            {
                nodesExamined++;
                foreach (ISolitaireMove move in klondike.FindAllMoves(currentGameState))
                {
                    GameState<ISolitaireMove, Board>? gameState = klondike.PruneGameState(new GameState<ISolitaireMove, Board>(currentGameState, move));
                    if (gameState is null)
                    {
                        gameStatesPruned++;
                        continue;
                    }
                    //Console.WriteLine(gameState.Moves.Value);
                    //gameState.Boards.Value.PrintTo();
                    //Console.WriteLine();
                    if (klondike.IsWin(gameState.Boards.Value))
                    {
                        wins.Push(gameState);
                    }
                    else
                    {
                        stack.Push(gameState);
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

            foreach (GameState<ISolitaireMove, Board> gameState in wins)
            {
                Console.WriteLine(gameState);
                gameState.Boards.Value.PrintTo();
            }

            return 0;
        }
    }
}
