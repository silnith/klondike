using Silnith.Game.Deck;
using Silnith.Game.Klondike.Move;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike
{
    /// <summary>
    /// An implementation of Klondike solitaire.
    /// </summary>
    public class Klondike : IGame<ISolitaireMove, Board>
    {
        /// <summary>
        /// The number of columns on the board for this game of Klondike solitaire.
        /// </summary>
        /// <remarks>
        /// <para>
        /// This is always <c>7</c>.
        /// </para>
        /// </remarks>
        public int ColumnCount
        {
            get;
        }

        /// <summary>
        /// The number of cards to advance the stock pile.
        /// </summary>
        /// <remarks>
        /// <para>
        /// This is typically <c>3</c>, but could be <c>1</c>.
        /// </para>
        /// </remarks>
        public int DrawAdvance
        {
            get;
        }

        /// <summary>
        /// Constructs a new game instance for exploring the search space of Klondike solitaire.
        /// </summary>
        public Klondike()
        {
            ColumnCount = 7;
            DrawAdvance = 3;
        }

        /// <summary>
        /// Returns all the legal moves for the given board.
        /// </summary>
        /// <param name="board">The board to examine for legal moves.</param>
        /// <returns>All the legal moves.</returns>
        public IEnumerable<ISolitaireMove> FindAllMoves(Board board)
        {
            return Array.Empty<ISolitaireMove>()
                .Concat(StockPileToFoundationMove.FindMoves(board))
                .Concat(StockPileToColumnMove.FindMoves(board))
                .Concat(ColumnToFoundationMove.FindMoves(board))
                .Concat(FoundationToColumnMove.FindMoves(board))
                .Concat(RunMove.FindMoves(board))
                .Concat(AdvanceStockPileMove.FindMoves(DrawAdvance, board))
                .Concat(RecycleStockPileMove.FindMoves(board));
        }

        /// <inheritdoc/>
        public IEnumerable<ISolitaireMove> FindAllMoves(GameState<ISolitaireMove, Board> state)
        {
            return FindAllMoves(state.Boards[0]);
        }

        /// <summary>
        /// Returns whether the given board is a winning game state for this game.
        /// </summary>
        /// <param name="board">The board to check.</param>
        /// <returns><see langword="true"/> if the board represents a win.</returns>
        public bool IsWin(Board board)
        {
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> foundation = board.Foundation;
            return foundation[Suit.Club].Count == 13
                && foundation[Suit.Diamond].Count == 13
                && foundation[Suit.Heart].Count == 13
                && foundation[Suit.Spade].Count == 13;
        }

        /// <inheritdoc/>
        public bool IsWin(GameState<ISolitaireMove, Board> state)
        {
            return IsWin(state.Boards.Value);
        }

        /// <inheritdoc/>
        public GameState<ISolitaireMove, Board>? PruneGameState(GameState<ISolitaireMove, Board> state)
        {
            ISolitaireMove currentMove = state.Moves.Value;
            LinkedNode<ISolitaireMove>? pastMoves = state.Moves.Next;

            Board currentBoard = state.Boards.Value;
            LinkedNode<Board>? pastBoards = state.Boards.Next;

            if (pastMoves is null || pastBoards is null)
            {
                // No past history to check, allow the search to continue.
                return state;
            }

            if (pastBoards.Count > 150)
            {
                return null;
            }

            if (pastBoards.Contains(currentBoard))
            {
                /*
                 * This move introduces a cycle into the history of boards.
                 */
                return null;
            }

            ISolitaireMove previousMove = pastMoves.Value;
            Board previousBoard = pastBoards.Value;

            if (currentMove.HasCards && previousMove.HasCards && currentMove.Cards.SequenceEqual(previousMove.Cards))
            {
                /*
                 * If two consecutive moves are moving the same stack of cards, they are
                 * redundant and the second may be pruned.
                 */
                return null;
            }

            /*
             * This is where all the complicated logic to check for silly
             * or redundant moves should go.
             */

            // The game state was not pruned, return it to allow the search to proceed.
            return state;
        }
    }
}
