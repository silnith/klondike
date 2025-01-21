using System.Collections.Generic;

namespace Silnith.Game
{
    /// <summary>
    /// A generic interface for any type of game.
    /// </summary>
    /// <typeparam name="M">The move type for the game.  This can simply be <see cref="IMove{B}"/>,
    /// but the interface allows specifying a subtype in the case that a game-specific interface is needed.</typeparam>
    /// <typeparam name="B">The board type for the game.</typeparam>
    public interface IGame<M, B> where M : IMove<B>
    {
        /// <summary>
        /// Returns whether the given game state is a winning game state for this game.
        /// </summary>
        /// <param name="state">The game state to check.</param>
        /// <returns><see langword="true"/> if the game state represents a win.</returns>
        bool IsWin(GameState<M, B> state);

        /// <summary>
        /// Returns all the legal moves for the provided game state.  The current game
        /// board can be retrieved using <c>state.Boards[0]</c>.
        /// </summary>
        /// <param name="state">The game state to search for legal moves.</param>
        /// <returns>An enumerable of legal moves for the given game state.</returns>
        IEnumerable<M> FindAllMoves(GameState<M, B> state);

        /// <summary>
        /// Possibly prunes or modifies the given game state based on the state
        /// history.  The returned value may be a different object than the input,
        /// so callers should always use the return value if it is not <see langword="null"/>.
        /// </summary>
        /// <param name="state">The game state to check.</param>
        /// <returns><see langword="null"/> if the game state was pruned, otherwise a valid game state.  This might not be the same game state as the parameter.</returns>
        GameState<M, B>? PruneGameState(GameState<M, B> state);
    }
}
