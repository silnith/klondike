#pragma once

enum class TicTacToePlayer
{
    nobody,
    X,
    O
};

[[nodiscard]]
TicTacToePlayer otherPlayer(TicTacToePlayer);
