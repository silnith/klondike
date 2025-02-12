#pragma once

#include "TicTacToePlayer.h"

#include <array>
#include <ostream>

class TicTacToeBoard
{
public:
    TicTacToeBoard(void) = default;
    TicTacToeBoard(TicTacToeBoard const&) = default;
    TicTacToeBoard& operator=(TicTacToeBoard const&) = default;
    TicTacToeBoard(TicTacToeBoard&&) noexcept = default;
    TicTacToeBoard& operator=(TicTacToeBoard&&) noexcept = default;
    ~TicTacToeBoard(void) = default;

    explicit TicTacToeBoard(TicTacToePlayer const&);

    explicit TicTacToeBoard(TicTacToePlayer const&, std::array<std::array<TicTacToePlayer, 3>, 3> const&);

    [[nodiscard]]
    TicTacToePlayer getPlayer(void) const;

    [[nodiscard]]
    std::array<std::array<TicTacToePlayer, 3>, 3> getBoard(void) const;

    friend void printTo(std::ostream& out, TicTacToeBoard const& board);
    friend void printTo(std::wostream& out, TicTacToeBoard const& board);

private:
    TicTacToePlayer const _player;
    std::array<std::array<TicTacToePlayer, 3>, 3> const _board;
};
