using Silnith.Game.Deck;
using Silnith.Game.Klondike.Move;
using Silnith.Game.Klondike.Move.Filter;
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
        private readonly IEnumerable<ISolitaireMoveFilter> filters = new List<ISolitaireMoveFilter>()
        {
            new MoveCapFilter(150),
            new KingMoveMustExposeFaceDownCardFilter(),
            new StockPileRecycleMustBeFollowedByAdvance(),
            new StockPileAdvanceMustBeFollowedBySomethingUseful(),
            new DrawFromFoundationMustBeUsefulFilter(),
            new DrawFromStockPileFilter(),
            new RunMoveMustBeFollowedBySomethingUsefulFilter(),
            new BoardCycleFilter(),
        };

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
                .Concat(StockPileRecycleMove.FindMoves(board))
                .Concat(StockPileAdvanceMove.FindMoves(DrawAdvance, board))
                .Concat(FoundationToColumnMove.FindMoves(board))
                .Concat(ColumnToColumnMove.FindMoves(board))
                .Concat(StockPileToColumnMove.FindMoves(board))
                .Concat(ColumnToFoundationMove.FindMoves(board))
                .Concat(StockPileToFoundationMove.FindMoves(board));
        }

        /// <inheritdoc/>
        public IEnumerable<ISolitaireMove> FindAllMoves(IReadOnlyList<GameState<ISolitaireMove, Board>> state)
        {
            return FindAllMoves(state[0].Board);
        }

        /// <summary>
        /// Returns whether the given board is a winning board for this game.
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

        /// <summary>
        /// Returns whether the given game state is a winning game state for this game.
        /// </summary>
        /// <param name="state">The game state to check.</param>
        /// <returns><see langword="true"/> if the game state represents a win.</returns>
        public bool IsWin(GameState<ISolitaireMove, Board> state)
        {
            return IsWin(state.Board);
        }

        /// <inheritdoc/>
        public bool IsWin(IReadOnlyList<GameState<ISolitaireMove, Board>> gameStates)
        {
            return IsWin(gameStates[0]);
        }

        /// <inheritdoc/>
        public IEnumerable<IMoveFilter<ISolitaireMove, Board>> GetFilters()
        {
            return filters;
        }
    }
}
