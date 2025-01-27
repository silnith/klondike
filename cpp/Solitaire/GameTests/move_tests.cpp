#include "CppUnitTest.h"

#include <silnith/game/game_state.h>

#include <memory>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game;

using namespace std::literals::string_literals;

class test_move : public virtual move<int>
{
public:
	test_move(void) = delete;
	test_move(test_move const&) = default;
	test_move& operator=(test_move const&) = default;
	test_move(test_move&&) noexcept = default;
	test_move& operator=(test_move&&) noexcept = default;
	virtual ~test_move(void) = default;

	test_move(int id) : id{ id }
	{}

	virtual std::shared_ptr<int> apply(std::shared_ptr<int> const& board) const
	{
		return std::make_shared<int>(*board);
	}

	bool operator==(test_move const& other) const
	{
		return id == other.id;
	}

	friend std::wstring to_wstring(test_move const& test_move)
	{
		return L"test_move{ "s + std::to_wstring(test_move.id) + L" }"s;
	}

private:
	int id;
};

template<>
std::wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(test_move const& test_move)
{
	return to_wstring(test_move);
}

template<>
std::wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(std::shared_ptr<test_move> const& test_move_ptr)
{
	return to_wstring(*test_move_ptr);
}

template<>
std::wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(game_state<test_move, int> const& game_state)
{
	return L"game_state<test_move, int>"s;
}

namespace MoveTests
{
	TEST_CLASS(MoveTests)
	{
	private:
		std::shared_ptr<test_move> move1_ptr{ std::make_shared<test_move>(1) };
		std::shared_ptr<test_move> move2_ptr{ std::make_shared<test_move>(2) };

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
			std::shared_ptr<test_move> move{ game_state.get_move() };
			std::shared_ptr<int> board{ game_state.get_board() };
		}
	};
}
