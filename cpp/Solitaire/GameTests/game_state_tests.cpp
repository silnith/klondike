#include "CppUnitTest.h"

#include "test_move.h"
#include "GameStateToString.h"

#include <silnith/game/game_state.h>

#include <memory>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game;

using namespace std::literals::string_literals;

namespace GameTests
{
	TEST_CLASS(GameStateTests)
	{
	private:
		std::shared_ptr<test_move const> move1_ptr{ std::make_shared<test_move>(1) };
		std::shared_ptr<test_move const> move2_ptr{ std::make_shared<test_move>(2) };

	public:
		TEST_METHOD(TestInitialConstructor)
		{
			// Set up the parameters to the game_state constructor.
			// The parameters are a pointer to a move, and a direct object for the board.
			test_move initial_move{ 1 };
			int initial_board{ 5 };
			// The object under test.
			game_state<test_move, int> game_state{ std::make_shared<test_move>(initial_move), std::make_shared<int>(initial_board) };

			// Fetch the members out of the object under test.
			std::shared_ptr<test_move const> move{ game_state.get_move() };
			std::shared_ptr<int const> board{ game_state.get_board() };
		}

		TEST_METHOD(TestGetMove)
		{
			// Set up the parameters to the game_state constructor.
			// The parameters are a pointer to a move, and a direct object for the board.
			test_move initial_move{ 1 };
			int initial_board{ 5 };
			// The object under test.
			game_state<test_move, int> game_state{ std::make_shared<test_move>(initial_move), std::make_shared<int>(initial_board) };

			// Fetch the members out of the object under test.
			std::shared_ptr<test_move const> const& move{ game_state.get_move() };

			Assert::AreEqual(test_move{ 1 }, *move);
		}

		TEST_METHOD(TestGetBoard)
		{
			// Set up the parameters to the game_state constructor.
			// The parameters are a pointer to a move, and a direct object for the board.
			test_move initial_move{ 1 };
			int initial_board{ 5 };
			// The object under test.
			game_state<test_move, int> game_state{ std::make_shared<test_move>(initial_move), std::make_shared<int>(initial_board) };

			// Fetch the members out of the object under test.
			std::shared_ptr<int const> const& board{ game_state.get_board() };

			Assert::AreEqual(5, *board);
		}
	};
}
