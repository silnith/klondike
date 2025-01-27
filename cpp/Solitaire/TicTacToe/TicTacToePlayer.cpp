#include "TicTacToePlayer.h"

#include <exception>

TicTacToePlayer otherPlayer(TicTacToePlayer player)
{
	switch (player)
	{
	case TicTacToePlayer::X:
		return TicTacToePlayer::O;
	case TicTacToePlayer::O:
		return TicTacToePlayer::X;
	default:
		throw std::exception{ "Invalid enum constant." };
	}
}
