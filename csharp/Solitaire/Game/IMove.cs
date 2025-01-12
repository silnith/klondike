namespace Silnith.Game
{
    /// <summary>
    /// The common interface for game moves.
    /// </summary>
    /// <typeparam name="T">The board type.</typeparam>
    public interface IMove<T>
    {
        /// <summary>
        /// Applies this move to the given board, returning the new board.
        /// </summary>
        /// <param name="board">The board to which to apply the move.</param>
        /// <returns>The new board after the move has been completed.</returns>
        public T Apply(T board);
    }
}
