#pragma once

#include <silnith/game/game.h>
#include <silnith/game/game_state.h>
#include <silnith/game/linked_node.h>
#include <silnith/game/move_filter.h>

#include "TicTacToeBoard.h"
#include "TicTacToeMove.h"

#include <memory>
#include <vector>

class TicTacToe :
    public silnith::game::game<TicTacToeMove, TicTacToeBoard>
{
public:
    TicTacToe(void) = default;
    TicTacToe(TicTacToe const&) = default;
    TicTacToe& operator=(TicTacToe const&) = default;
    TicTacToe(TicTacToe&&) noexcept = default;
    TicTacToe& operator=(TicTacToe&&) noexcept = default;
    virtual ~TicTacToe(void) = default;

    /// <inheritdoc/>
    virtual bool is_win(TicTacToeBoard const& board) const;

    /// <inheritdoc/>
    virtual bool is_win(silnith::game::game_state<TicTacToeMove, TicTacToeBoard> const& game_state) const;

    /// <inheritdoc/>
    virtual std::vector<std::shared_ptr<TicTacToeMove>> find_all_moves(
        std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<TicTacToeMove, TicTacToeBoard>>> const& game_state_history) const;

    /// <inheritdoc/>
    virtual std::vector<std::shared_ptr<silnith::game::move_filter<TicTacToeMove, TicTacToeBoard>>> get_filters(void) const;

private:
};
