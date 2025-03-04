#include "TicTacToeMove.h"

#include "TicTacToePlayer.h"

TicTacToeMove::TicTacToeMove(int row, int column, TicTacToePlayer player)
    : row_{ row }, column_{ column }, player_{ player }
{}

std::shared_ptr<TicTacToeBoard const> TicTacToeMove::apply(std::shared_ptr<TicTacToeBoard const> const& board) const
{
    std::array<std::array<TicTacToePlayer, 3>, 3> boardCopy{ board->getBoard() };
    boardCopy[row_][column_] = player_;
    return std::make_shared<TicTacToeBoard>(otherPlayer(player_), boardCopy);
}
