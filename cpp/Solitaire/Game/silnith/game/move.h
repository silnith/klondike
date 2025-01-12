#pragma once

namespace silnith
{
    namespace game
    {
        /// <summary>
        /// The common interface for game moves.
        /// </summary>
        /// <typeparam name="board">The board type.</typeparam>
        template<class board>
        class move
        {
        public:
            /// <summary>
            /// Applies this move to the given board, returning the new board.
            /// </summary>
            /// <param name="t"></param>
            /// <returns></returns>
            virtual board apply(board const& board) const = 0;
        };
    }
}
