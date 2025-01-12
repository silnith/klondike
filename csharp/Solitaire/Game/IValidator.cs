using System;

namespace Silnith.Game
{
    /// <summary>
    /// A common interface for game state validators.
    /// Implementations will check that a game object is in a valid state,
    /// and throw an <see cref="ArgumentException"/> if it is not.
    /// </summary>
    /// <typeparam name="T">The type of object to validate.</typeparam>
    public interface IValidator<T>
    {
        /// <summary>
        /// Validates that the given game object is in a valid state.
        /// </summary>
        /// <param name="t">the game object to validate.</param>
        /// <exception cref="ArgumentException">If the object is in an invalid state.</exception>
        public void Validate(T t);
    }
}
