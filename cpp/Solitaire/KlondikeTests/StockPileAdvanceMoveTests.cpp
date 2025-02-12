#include "CppUnitTest.h"

#include "BoardToString.h"
#include "CardToString.h"

#include <silnith/game/solitaire/move/StockPileAdvanceMove.h>

#include <memory>
#include <set>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game::deck;
using namespace silnith::game::solitaire;
using namespace silnith::game::solitaire::move;

using namespace std;
using namespace std::literals::string_literals;

struct StockPileAdvanceMoveComparator
{
public:
	bool operator()(shared_ptr<StockPileAdvanceMove> const& lhs, shared_ptr<StockPileAdvanceMove> const& rhs) const
	{
		if (lhs->get_beginning_index() < rhs->get_beginning_index())
		{
			return true;
		}
		else if (lhs->get_beginning_index() == rhs->get_beginning_index())
		{
			return lhs->get_increment() < rhs->get_increment();
		}
		else
		{
			return false;
		}
	}
};

using StockPileAdvanceMoveSet = set<shared_ptr<StockPileAdvanceMove>, StockPileAdvanceMoveComparator>;

bool operator==(StockPileAdvanceMoveSet const& lhs, StockPileAdvanceMoveSet const& rhs)
{
	vector<StockPileAdvanceMove> lhs_moves{};
	vector<StockPileAdvanceMove> rhs_moves{};
	for (shared_ptr<StockPileAdvanceMove> ptr : lhs)
	{
		lhs_moves.emplace_back(*ptr);
	}
	for (shared_ptr<StockPileAdvanceMove> ptr : rhs)
	{
		rhs_moves.emplace_back(*ptr);
	}
	return lhs_moves == rhs_moves;
}

StockPileAdvanceMoveSet ToStockPileAdvanceMoveSet(vector<shared_ptr<solitaire_move>> const& vec)
{
	vector<shared_ptr<StockPileAdvanceMove>> typed_vec{};
	for (shared_ptr<solitaire_move> move : vec)
	{
		typed_vec.emplace_back(dynamic_pointer_cast<StockPileAdvanceMove>(move));
	}
	StockPileAdvanceMoveSet s{ typed_vec.begin(), typed_vec.end() };
	return s;
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(StockPileAdvanceMove const& move)
{
	return to_wstring(move);
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(StockPileAdvanceMoveSet const& moves)
{
	wostringstream out{};
	out << L"{"s;
	if (!moves.empty())
	{
		StockPileAdvanceMoveSet::const_iterator citer{ moves.cbegin() };
		StockPileAdvanceMoveSet::const_iterator cend{ moves.cend() };
		shared_ptr<StockPileAdvanceMove> ptr{ *citer };
		out << *ptr;
		citer++;
		while (citer != cend)
		{
			out << L", "s;
			shared_ptr<StockPileAdvanceMove> ptr{ *citer };
			out << *ptr;
			citer++;
		}
	}
	out << L"}"s;
	return out.str();
}

namespace SolitaireMoveTests
{
	TEST_CLASS(StockPileAdvanceMoveTests)
	{
	private:
		vector<card> empty_list_of_cards{};
		vector<column> empty_columns{
			column{ empty_list_of_cards, empty_list_of_cards },
			column{ empty_list_of_cards, empty_list_of_cards },
			column{ empty_list_of_cards, empty_list_of_cards },
			column{ empty_list_of_cards, empty_list_of_cards },
			column{ empty_list_of_cards, empty_list_of_cards },
			column{ empty_list_of_cards, empty_list_of_cards },
			column{ empty_list_of_cards, empty_list_of_cards },
		};
		map<suit, vector<card>> empty_foundation{
			{ suit::club, empty_list_of_cards },
			{ suit::diamond, empty_list_of_cards },
			{ suit::heart, empty_list_of_cards },
			{ suit::spade, empty_list_of_cards },
		};

	public:
		TEST_METHOD(TestFindMovesEmptyStockPile)
		{
			board new_board{ empty_columns, empty_list_of_cards, 0, empty_foundation };

			vector<shared_ptr<solitaire_move>> moves{ StockPileAdvanceMove::find_moves(3, new_board) };
			StockPileAdvanceMoveSet actual{ ToStockPileAdvanceMoveSet(moves) };

			StockPileAdvanceMoveSet expected{};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesStockPileAtBeginning)
		{
			vector<card> stock_pile{
				card{ value::ace, suit::club },
				card{ value::five, suit::diamond },
				card{ value::king, suit::spade },
			};
			board new_board{ empty_columns, stock_pile, 0, empty_foundation };

			vector<shared_ptr<solitaire_move>> moves{ StockPileAdvanceMove::find_moves(3, new_board) };
			StockPileAdvanceMoveSet actual{ ToStockPileAdvanceMoveSet(moves) };

			StockPileAdvanceMoveSet expected{
				make_shared<StockPileAdvanceMove>(0, 3),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesStockPileInMiddle)
		{
			vector<card> stock_pile{
				card{ value::ace, suit::club },
				card{ value::five, suit::diamond },
				card{ value::king, suit::spade },
			};
			board new_board{ empty_columns, stock_pile, 2, empty_foundation };

			vector<shared_ptr<solitaire_move>> moves{ StockPileAdvanceMove::find_moves(3, new_board) };
			StockPileAdvanceMoveSet actual{ ToStockPileAdvanceMoveSet(moves) };

			StockPileAdvanceMoveSet expected{
				make_shared<StockPileAdvanceMove>(2, 3),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesStockPileAtEnd)
		{
			vector<card> stock_pile{
				card{ value::ace, suit::club },
				card{ value::five, suit::diamond },
				card{ value::king, suit::spade },
			};
			board new_board{ empty_columns, stock_pile, 3, empty_foundation };

			vector<shared_ptr<solitaire_move>> moves{ StockPileAdvanceMove::find_moves(3, new_board) };
			StockPileAdvanceMoveSet actual{ ToStockPileAdvanceMoveSet(moves) };

			StockPileAdvanceMoveSet expected{};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestZeroIncrement)
		{
			Assert::ExpectException<out_of_range>([]()
				{
					StockPileAdvanceMove unused{ 0, 0 };
				});
		}

		TEST_METHOD(TestGetBeginningIndex)
		{
			StockPileAdvanceMove move{ 17, 34 };

			Assert::AreEqual(size_t{ 17 }, move.get_beginning_index());
		}

		TEST_METHOD(TestGetIncrement)
		{
			StockPileAdvanceMove move{ 17, 34 };

			Assert::AreEqual(size_t{ 34 }, move.get_increment());
		}

		// TODO: coalesce

		TEST_METHOD(TestHasCards)
		{
			StockPileAdvanceMove move{ 17,34 };

			Assert::IsFalse(move.has_cards());
		}

		TEST_METHOD(TestGetCards)
		{
			StockPileAdvanceMove move{ 17,34 };

			vector<card> expected{};
			Assert::AreEqual(expected, move.get_cards());
		}

		TEST_METHOD(TestApply)
		{
			vector<card> stock_pile{
				card{ value::ace, suit::club },
				card{ value::two, suit::club },
				card{ value::three, suit::club },
				card{ value::four, suit::club },
				card{ value::five, suit::club },
				card{ value::six, suit::club },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, stock_pile, 2, empty_foundation) };

			StockPileAdvanceMove move{ 3, *board_ptr };

			shared_ptr<board> actual{ move.apply(board_ptr) };

			board expected{ empty_columns, stock_pile, 5, empty_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyKeepsColumns)
		{
			vector<column> columns{ empty_columns };
			columns.at(1) = column{
				vector<card>{},
				vector<card>{
					card{ value::four, suit::diamond },
					card{ value::three, suit::spade },
					card{ value::two, suit::diamond },
					card{ value::ace, suit::spade },
				}
			};
			columns.at(2) = column{
				vector<card>{
					card{ value::four, suit::club },
					card{ value::ten, suit::diamond },
				},
				vector<card>{
					card{ value::ten, suit::club },
					card{ value::nine, suit::heart },
					card{ value::eight, suit::club },
					card{ value::seven, suit::heart },
				}
			};
			vector<card> stock_pile{
				card{ value::ace, suit::club },
				card{ value::two, suit::club },
				card{ value::three, suit::club },
				card{ value::four, suit::club },
				card{ value::five, suit::club },
				card{ value::six, suit::club },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, 2, empty_foundation) };

			StockPileAdvanceMove move{ 3, *board_ptr };

			shared_ptr<board> actual{ move.apply(board_ptr) };

			board expected{ columns, stock_pile, 5, empty_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyKeepsFoundation)
		{
			map<suit, vector<card>> foundation{
				{
					suit::club,
					empty_list_of_cards
				},
				{
					suit::diamond,
					vector<card>{
						card{ value::ace, suit::diamond },
						card{ value::two, suit::diamond },
						card{ value::three, suit::diamond },
					}
				},
				{
					suit::heart,
					empty_list_of_cards
				},
				{
					suit::spade,
					empty_list_of_cards
				},
			};
			vector<card> stock_pile{
				card{ value::ace, suit::club },
				card{ value::two, suit::club },
				card{ value::three, suit::club },
				card{ value::four, suit::club },
				card{ value::five, suit::club },
				card{ value::six, suit::club },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, stock_pile, 2, foundation) };

			StockPileAdvanceMove move{ 3, *board_ptr };

			shared_ptr<board> actual{ move.apply(board_ptr) };

			board expected{ empty_columns, stock_pile, 5, foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestEquals)
		{
			StockPileAdvanceMove move1{ 17, 34 };
			StockPileAdvanceMove move2{ 17, 34 };

			Assert::AreEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentBeginningIndex)
		{
			StockPileAdvanceMove move1{ 17, 34 };
			StockPileAdvanceMove move2{ 20, 34 };

			Assert::AreNotEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentIncrement)
		{
			StockPileAdvanceMove move1{ 17, 34 };
			StockPileAdvanceMove move2{ 17, 3 };

			Assert::AreNotEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferent)
		{
			StockPileAdvanceMove move1{ 17, 34 };
			StockPileAdvanceMove move2{ 20, 3 };

			Assert::AreNotEqual(move1, move2);
		}
	};
}
