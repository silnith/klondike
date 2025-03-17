using System;

namespace Silnith.Game
{
    /// <summary>
    /// A single node in a game tree.
    /// </summary>
    /// <typeparam name="M">The type of the game moves.  This can simply be <see cref="IMove{B}"/>,
    /// but the interface allows specifying a subtype in the case that a game-specific interface is needed.</typeparam>
    /// <typeparam name="B">The type of the game board.</typeparam>
    public class GameState<M, B> : Tuple<M, B>
        where M : IMove<B>
    {
        /// <summary>
        /// The move.
        /// </summary>
        public virtual M Move
        {
            get
            {
                return Item1;
            }
        }

        /// <summary>
        /// The board.
        /// </summary>
        public virtual B Board
        {
            get
            {
                return Item2;
            }
        }

        /// <summary>
        /// Constructs a game state with the given move and board.
        /// </summary>
        /// <param name="initialMove">The move.</param>
        /// <param name="initialBoard">The board.</param>
        public GameState(M initialMove, B initialBoard) : base(initialMove, initialBoard)
        {
        }
    }
}
