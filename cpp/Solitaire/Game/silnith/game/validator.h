#pragma once

namespace silnith
{
    namespace game
    {
        /// <summary>
        /// A common interface for game state validators.
        /// Implementations will check that a game object is in a valid state,
        /// and throw an exception if it is not.
        /// </summary>
        /// <typeparam name="T">The type of object to validate.</typeparam>
        template<class T>
        class validator
        {
        public:
            /// <summary>
            /// Validates that the given object is in a valid state.
            /// </summary>
            /// <param name="t">The object to validate.</param>
            virtual void validate(T const& t) const = 0;
        };
    }
}
