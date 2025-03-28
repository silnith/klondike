﻿using System.Collections.Generic;

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
        /// Returns whether the given reverse list of game states culminates in a win for this game.
        /// The list begins with the most recent move and resulting board, and ends with the initial
        /// move and board of the game.
        /// </summary>
        /// <remarks>
        /// <para>
        /// The most recent game state is <c>gameStates[0]</c>.
        /// The current board is <c>gameStates[0].Board</c>.
        /// </para>
        /// </remarks>
        /// <param name="gameStates">The reverse list of game states to check.
        /// The list is guaranteed to have at least one game state in it.</param>
        /// <returns><see langword="true"/> if the reverse sequence of game states culminates in a win.</returns>
        bool IsWin(IReadOnlyList<GameState<M, B>> gameStates);

        /// <summary>
        /// Returns all the legal moves for the provided game state.  The current game
        /// board can be retrieved using <c>state.Boards[0]</c>.
        /// </summary>
        /// <param name="state">The game state to search for legal moves.</param>
        /// <returns>An enumerable of legal moves for the given game state.</returns>
        IEnumerable<M> FindAllMoves(IReadOnlyList<GameState<M, B>> state);

        /// <summary>
        /// Returns filters for pruning the game search space.
        /// </summary>
        /// <remarks>
        /// <para>
        /// The search space for any non-trivial game is massive.
        /// Realistically no search will ever complete unless the search space
        /// is pruned in some way.This method provides a way for an implementation
        /// of a game&#x2019;s logic to provide filters for pruning the search
        /// space, in a way that is meaningful for the specifics of the game.
        /// </para>
        /// <para>
        /// The search engine will run all the provided filters on every game state
        /// in the search tree.If any filter returns <see langword="true"/>, that game state
        /// will be pruned and no further search of it will happen.
        /// </para>
        /// <para>
        /// The collection of filters will only be queried when the search begins,
        /// so there is no value in altering the collection of returned filters
        /// beyond their initial creation.
        /// </para>
        /// <para>
        /// If the game implementation does not wish to provide any filters,
        /// it should return an empty enumerable.
        /// </para>
        /// </remarks>
        /// <returns>A collection of game state filters for pruning the search space.</returns>
        IEnumerable<IMoveFilter<M, B>> GetFilters();
    }
}
