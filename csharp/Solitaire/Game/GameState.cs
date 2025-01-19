using System;

namespace Silnith.Game
{
    /// <summary>
    /// A single node in a game tree.
    /// </summary>
    /// <remarks>
    /// <para>
    /// This keeps track of the history of a game,
    /// both the sequence of moves made from the beginning and the state of the board after every move.
    /// </para>
    /// <para>
    /// It is assumed that <c>Moves.Count</c> equals <c>Boards.Count</c>.
    /// </para>
    /// </remarks>
    /// <typeparam name="M">The type of the game moves.  This can simply be <see cref="IMove{B}"/>,
    /// but the interface allows specifying a subtype in the case that a game-specific interface is needed.</typeparam>
    /// <typeparam name="B">The type of the game board.</typeparam>
    public class GameState<M, B> : Tuple<LinkedNode<M>, LinkedNode<B>>
        where M : IMove<B>
    {
        /// <summary>
        /// The list of moves.
        /// </summary>
        public virtual LinkedNode<M> Moves
        {
            get
            {
                return Item1;
            }
        }

        /// <summary>
        /// The list of board states.
        /// </summary>
        public virtual LinkedNode<B> Boards
        {
            get
            {
                return Item2;
            }
        }

        /// <summary>
        /// Constructs a new game state with the given list of moves and associated board states.
        /// </summary>
        /// <param name="moves">The list of moves.</param>
        /// <param name="boards">The list of board states.</param>
        public GameState(LinkedNode<M> moves, LinkedNode<B> boards) : base(moves, boards)
        {
        }

        /// <summary>
        /// Constructs an initial game state with the given initial move and board.
        /// </summary>
        /// <param name="initialMove">The initial move.  This is often a form of "deal deck".</param>
        /// <param name="initialBoard">The initial board.</param>
        public GameState(M initialMove, B initialBoard) : this(new LinkedNode<M>(initialMove), new LinkedNode<B>(initialBoard))
        {
        }

        /// <summary>
        /// Constructs a new game state by appending the given move and board to the
        /// previous game state represented by <paramref name="parent"/>.
        /// </summary>
        /// <param name="parent">The previous game state.</param>
        /// <param name="move">The new move to append.</param>
        /// <param name="board">The new board state to append.</param>
        public GameState(GameState<M, B> parent, M move, B board) : this(new LinkedNode<M>(move, parent.Moves), new LinkedNode<B>(board, parent.Boards))
        {
        }

        /// <summary>
        /// Constructs a new game state by applying the given move to the most recent board in the given <paramref name="parent"/> game state.
        /// </summary>
        /// <param name="parent">The previous game state.</param>
        /// <param name="move">The new move to apply to the game state.</param>
        public GameState(GameState<M, B> parent, M move) : this(parent, move, move.Apply(parent.Boards.Value))
        {
        }
    }
}
