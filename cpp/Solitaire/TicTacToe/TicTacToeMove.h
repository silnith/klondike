#pragma once

#include <silnith/game/move.h>

#include <memory>

#include "TicTacToeBoard.h"
#include "TicTacToePlayer.h"

class TicTacToeMove :
    public silnith::game::move<TicTacToeBoard>
{
public:
    TicTacToeMove(void) = default;
    TicTacToeMove(TicTacToeMove const&) = default;
    TicTacToeMove& operator=(TicTacToeMove const&) = default;
    TicTacToeMove(TicTacToeMove&&) noexcept = default;
    TicTacToeMove& operator=(TicTacToeMove&&) noexcept = default;
    ~TicTacToeMove(void) = default;

    explicit TicTacToeMove(int row, int column, TicTacToePlayer player);

    [[nodiscard]]
    virtual std::shared_ptr<TicTacToeBoard const> apply(std::shared_ptr<TicTacToeBoard const> const& board) const override;

private:
    int const row_;
    int const column_;
    TicTacToePlayer const player_;
};
