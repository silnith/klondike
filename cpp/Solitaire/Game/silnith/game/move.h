#pragma once

#include <memory>

namespace silnith
{
    namespace game
    {
        /// <summary>
        /// The common interface for game moves.
        /// </summary>
        /// <typeparam name="B">The board type.</typeparam>
        template<class B>
        class move
        {
        public:
            move(void) = default;
            move(move const&) = default;
            move& operator=(move const&) = default;
            move(move&&) noexcept = default;
            move& operator=(move&&) noexcept = default;
            virtual ~move(void) = default;

            /// <summary>
            /// Applies this move to the given board, returning the new board.
            /// </summary>
            /// <returns>The new board after the move has been completed.</returns>
            [[nodiscard]]
            virtual std::shared_ptr<B> apply(std::shared_ptr<B> const& board) const = 0;
        };
    }
}
