#include "CppUnitTest.h"

#include <silnith/game/game_state.h>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game;

using namespace std::literals::string_literals;

class test_move : public move<int>
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

	virtual int apply(int const& board) const
	{
		return 0;
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
std::wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(game_state<test_move, int> const& game_state)
{
	return L"game_state<test_move, int>"s;
}

namespace GameTests
{
	TEST_CLASS(GameStateTests)
	{
	private:
		test_move move1{ 1 };
		test_move move2{ 2 };

	public:
		TEST_METHOD(TestInitialConstructor)
		{
			game_state<test_move, int> game_state{ move1, 1 };

			std::shared_ptr<linked_node<test_move>> moves{ game_state.get_moves() };
			std::shared_ptr<linked_node<int>> boards{ game_state.get_boards() };

			Assert::AreEqual(move1, moves->get_value());
			Assert::IsNull(moves->get_next().get());

			Assert::AreEqual(1, boards->get_value());
			Assert::IsNull(boards->get_next().get());
		}
	};
}
