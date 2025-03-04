#pragma once

#include "CppUnitTest.h"

#include "test_move.h"

#include <silnith/game/game_state.h>

template<>
std::wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(silnith::game::game_state<test_move, int> const& game_state)
{
	std::wostringstream out;
	out << game_state;
	return out.str();
}
