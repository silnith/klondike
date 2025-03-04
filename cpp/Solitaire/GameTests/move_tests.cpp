#include "CppUnitTest.h"

#include "test_move.h"

#include <silnith/game/game_state.h>

#include <memory>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game;

using namespace std::literals::string_literals;

namespace MoveTests
{
	TEST_CLASS(MoveTests)
	{
	private:
		std::shared_ptr<test_move const> move1_ptr{ std::make_shared<test_move>(1) };
		std::shared_ptr<test_move const> move2_ptr{ std::make_shared<test_move>(2) };

	public:
		TEST_METHOD(TestApply)
		{
			std::shared_ptr<test_move const> move{ std::make_shared<test_move>(1) };
			std::shared_ptr<int const> initial_board{ std::make_shared<int>(5) };

			std::shared_ptr<int const> board{ move->apply(initial_board) };

			Assert::AreEqual(1, *board);
		}
	};
}
