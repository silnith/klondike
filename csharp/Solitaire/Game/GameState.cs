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
        /// The list of moves.
        /// </summary>
        public virtual M Move
        {
            get
            {
                return Item1;
            }
        }

        /// <summary>
        /// The list of board states.
        /// </summary>
        public virtual B Board
        {
            get
            {
                return Item2;
            }
        }

        /// <summary>
        /// Constructs an initial game state with the given initial move and board.
        /// </summary>
        /// <param name="initialMove">The initial move.  This is often a form of "deal deck".</param>
        /// <param name="initialBoard">The initial board.</param>
        public GameState(M initialMove, B initialBoard) : base(initialMove, initialBoard)
        {
        }
    }
}
