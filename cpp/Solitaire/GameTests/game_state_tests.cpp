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
		return id;
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

namespace GameTests
{
	TEST_CLASS(GameStateTests)
	{
	private:
		std::shared_ptr<test_move> move1_ptr{ std::make_shared<test_move>(1) };
		std::shared_ptr<test_move> move2_ptr{ std::make_shared<test_move>(2) };

	public:
		TEST_METHOD(TestLinkedNodeConstructor)
		{
			// Set up the parameters to the game_state constructor.
			// The parameters are two linked_node pointers.
			std::shared_ptr<linked_node<std::shared_ptr<test_move>>> moves{ std::make_shared<linked_node<std::shared_ptr<test_move>>>(move1_ptr) };
			std::shared_ptr<linked_node<int>> boards{ std::make_shared<linked_node<int>>(5) };

			// The object under test.
			game_state<test_move, int> game_state{ moves, boards };

			// Fetch the members out of the object under test.
			std::shared_ptr<linked_node<std::shared_ptr<test_move>>> moves_history_ptr{ game_state.get_moves() };
			std::shared_ptr<linked_node<int>> boards_history_ptr{ game_state.get_boards() };

			// Validate the linked_node for the moves.
			Assert::IsTrue(bool{ moves_history_ptr });
			std::shared_ptr<test_move> first_move_actual_ptr{ moves_history_ptr->get_value() };
			Assert::AreEqual(test_move{ 1 }, *first_move_actual_ptr);
			Assert::IsFalse(bool{ moves_history_ptr->get_next() });

			// Validate the linked_node for the boards.
			Assert::IsTrue(bool{ boards_history_ptr });
			Assert::AreEqual(5, boards_history_ptr->get_value());
			Assert::IsFalse(bool{ boards_history_ptr->get_next() });
		}

		TEST_METHOD(TestInitialConstructor)
		{
			// Set up the parameters to the game_state constructor.
			// The parameters are a pointer to a move, and a direct object for the board.
			std::shared_ptr<test_move> initial_move_ptr{ move1_ptr };
			int initial_board{ 5 };
			// The object under test.
			game_state<test_move, int> game_state{ initial_move_ptr, initial_board };

			// Fetch the members out of the object under test.
			std::shared_ptr<linked_node<std::shared_ptr<test_move>>> moves_history_ptr{ game_state.get_moves() };
			std::shared_ptr<linked_node<int>> boards_history_ptr{ game_state.get_boards() };

			// Validate the linked_node for the moves.
			Assert::IsTrue(bool{ moves_history_ptr });
			std::shared_ptr<test_move> first_move_actual_ptr{ moves_history_ptr->get_value() };
			Assert::AreEqual(test_move{ 1 }, *first_move_actual_ptr);
			Assert::IsFalse(bool{ moves_history_ptr->get_next() });

			// Validate the linked_node for the boards.
			Assert::IsTrue(bool{ boards_history_ptr });
			Assert::AreEqual(5, boards_history_ptr->get_value());
			Assert::IsFalse(bool{ boards_history_ptr->get_next() });
		}

		TEST_METHOD(TestGameStateConstructor)
		{
			// Set up the parameters to the game_state constructor.
			// The parameters are a prior game state, a pointer to a move, and a direct object for the board.
			game_state<test_move, int> prior_game_state{ move2_ptr, 10 };
			std::shared_ptr<test_move> initial_move_ptr{ move1_ptr };
			int initial_board{ 5 };
			// The object under test.
			game_state<test_move, int> game_state{ prior_game_state, initial_move_ptr, initial_board };

			// Fetch the members out of the object under test.
			std::shared_ptr<linked_node<std::shared_ptr<test_move>>> moves_history_ptr{ game_state.get_moves() };
			std::shared_ptr<linked_node<int>> boards_history_ptr{ game_state.get_boards() };

			// Validate the linked_node for the moves.
			Assert::IsTrue(bool{ moves_history_ptr });
			std::shared_ptr<test_move> first_move_actual_ptr{ moves_history_ptr->get_value() };
			Assert::AreEqual(test_move{ 1 }, *first_move_actual_ptr);
			Assert::IsTrue(bool{ moves_history_ptr->get_next() });
			std::shared_ptr<test_move> second_move_actual_ptr{ moves_history_ptr->get_next()->get_value() };
			Assert::AreEqual(test_move{ 2 }, *second_move_actual_ptr);
			Assert::IsFalse(bool{ moves_history_ptr->get_next()->get_next() });

			// Validate the linked_node for the boards.
			Assert::IsTrue(bool{ boards_history_ptr });
			Assert::AreEqual(5, boards_history_ptr->get_value());
			Assert::IsTrue(bool{ boards_history_ptr->get_next() });
			Assert::AreEqual(10, boards_history_ptr->get_next()->get_value());
			Assert::IsFalse(bool{ boards_history_ptr->get_next()->get_next() });
		}

		TEST_METHOD(TestGameStateSimplifiedConstructor)
		{
			// Set up the parameters to the game_state constructor.
			// The parameters are a prior game state and a pointer to a move.
			game_state<test_move, int> prior_game_state{ move2_ptr, 10 };
			std::shared_ptr<test_move> initial_move_ptr{ move1_ptr };
			// The object under test.
			game_state<test_move, int> game_state{ prior_game_state, initial_move_ptr };

			// Fetch the members out of the object under test.
			std::shared_ptr<linked_node<std::shared_ptr<test_move>>> moves_history_ptr{ game_state.get_moves() };
			std::shared_ptr<linked_node<int>> boards_history_ptr{ game_state.get_boards() };

			// Validate the linked_node for the moves.
			Assert::IsTrue(bool{ moves_history_ptr });
			std::shared_ptr<test_move> first_move_actual_ptr{ moves_history_ptr->get_value() };
			Assert::AreEqual(test_move{ 1 }, *first_move_actual_ptr);
			Assert::IsTrue(bool{ moves_history_ptr->get_next() });
			std::shared_ptr<test_move> second_move_actual_ptr{ moves_history_ptr->get_next()->get_value() };
			Assert::AreEqual(test_move{ 2 }, *second_move_actual_ptr);
			Assert::IsFalse(bool{ moves_history_ptr->get_next()->get_next() });

			// Validate the linked_node for the boards.
			Assert::IsTrue(bool{ boards_history_ptr });
			Assert::AreEqual(1, boards_history_ptr->get_value());
			Assert::IsTrue(bool{ boards_history_ptr->get_next() });
			Assert::AreEqual(10, boards_history_ptr->get_next()->get_value());
			Assert::IsFalse(bool{ boards_history_ptr->get_next()->get_next() });
		}
	};
}
