using System.Collections.Generic;

namespace Silnith.Game
{
    /// <summary>
    /// A common interface for predicates used to filter the game search tree.
    /// </summary>
    /// <typeparam name="M">The move type for the game.</typeparam>
    /// <typeparam name="B">The board type for the game.</typeparam>
    public interface IMoveFilter<M, B> where M : IMove<B>
    {
        /// <summary>
        /// A key that can be used in a <see cref="System.Collections.Generic.IDictionary{TKey, TValue}"/>
        /// for gathering statistics about how this filter performs.
        /// It must not change over the lifetime of the object.
        /// </summary>
        /// <remarks>
        /// <para>
        /// It is helpful if the object converts nicely to a <see cref="System.String"/>.
        /// </para>
        /// <para>
        /// A static string works very well for implementations.
        /// </para>
        /// </remarks>
        object StatisticsKey
        {
            get;
        }

        /// <summary>
        /// Returns <see langword="true"/> if the game state should be pruned from the
        /// search tree of possible moves for the game.
        /// </summary>
        /// <remarks>
        /// <para>
        /// The list provided as the parameter will be a singly-linked list
        /// with linear access time for elements.  Implementations are strongly
        /// encouraged to use an <see cref="IEnumerator{T}"/> to access elements,
        /// and to only traverse the list once if possible.
        /// </para>
        /// <para>
        /// Element <c>gameStateHistory[0]</c> will be the current move and resulting board.
        /// Element <c>gameStateHistory[gameStateHistory.Count - 1]</c> will be the
        /// very first move of the game.
        /// </para>
        /// </remarks>
        /// <param name="gameStateHistory">A sequence of game states, beginning with
        /// the most recent move and resulting board.</param>
        /// <returns><see langword="true"/> if the node should be pruned from the search tree.</returns>
        bool ShouldFilter(IReadOnlyList<GameState<M, B>> gameStateHistory);
    }
}
