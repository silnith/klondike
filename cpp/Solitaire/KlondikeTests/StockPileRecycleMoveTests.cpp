#include "CppUnitTest.h"

#include "BoardToString.h"

#include <silnith/game/solitaire/move/StockPileRecycleMove.h>

#include <memory>
#include <set>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game::deck;
using namespace silnith::game::solitaire;
using namespace silnith::game::solitaire::move;

using namespace std;
using namespace std::literals::string_literals;

struct StockPileRecycleMoveComparator
{
public:
	bool operator()(shared_ptr<StockPileRecycleMove> const& lhs, shared_ptr<StockPileRecycleMove> const& rhs) const
	{
		return lhs->get_source_index() < rhs->get_source_index();
	}
};

using StockPileRecycleMoveSet = set<shared_ptr<StockPileRecycleMove>, StockPileRecycleMoveComparator>;

bool operator==(StockPileRecycleMoveSet const& lhs, StockPileRecycleMoveSet const& rhs)
{
	vector<StockPileRecycleMove> lhs_moves{};
	vector<StockPileRecycleMove> rhs_moves{};
	for (shared_ptr<StockPileRecycleMove> ptr : lhs)
	{
		lhs_moves.emplace_back(*ptr);
	}
	for (shared_ptr<StockPileRecycleMove> ptr : rhs)
	{
		rhs_moves.emplace_back(*ptr);
	}
	return lhs_moves == rhs_moves;
}

StockPileRecycleMoveSet ToStockPileRecycleMoveSet(vector<shared_ptr<solitaire_move>> const& vec)
{
	vector<shared_ptr<StockPileRecycleMove>> typed_vec{};
	for (shared_ptr<solitaire_move> move : vec)
	{
		typed_vec.emplace_back(dynamic_pointer_cast<StockPileRecycleMove>(move));
	}
	StockPileRecycleMoveSet s{ typed_vec.begin(), typed_vec.end() };
	return s;
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(StockPileRecycleMove const& move)
{
	return to_wstring(move);
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(StockPileRecycleMoveSet const& moves)
{
	wostringstream out{};
	out << L"{"s;
	if (!moves.empty())
	{
		StockPileRecycleMoveSet::const_iterator citer{ moves.cbegin() };
		StockPileRecycleMoveSet::const_iterator cend{ moves.cend() };
		shared_ptr<StockPileRecycleMove> ptr{ *citer };
		out << *ptr;
		citer++;
		while (citer != cend)
		{
			out << L", "s;
			shared_ptr<StockPileRecycleMove> ptr{ *citer };
			out << *ptr;
			citer++;
		}
	}
	out << L"}"s;
	return out.str();
}

namespace SolitaireMoveTests
{
	TEST_CLASS(StockPileRecycleMoveTests)
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

			vector<shared_ptr<solitaire_move>> moves{ StockPileRecycleMove::find_moves(new_board) };
			StockPileRecycleMoveSet actual{ ToStockPileRecycleMoveSet(moves) };

			StockPileRecycleMoveSet expected{};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesStockPileAtBeginning)
		{
			vector<card> stock_pile{
				card{ value::four, suit::club },
				card{ value::seven, suit::heart },
				card{ value::six, suit::heart },
				card{ value::three, suit::diamond },
			};
			board new_board{ empty_columns, stock_pile, 0, empty_foundation };

			vector<shared_ptr<solitaire_move>> moves{ StockPileRecycleMove::find_moves(new_board) };
			StockPileRecycleMoveSet actual{ ToStockPileRecycleMoveSet(moves) };

			StockPileRecycleMoveSet expected{};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesStockPileInMiddle)
		{
			vector<card> stock_pile{
				card{ value::four, suit::club },
				card{ value::seven, suit::heart },
				card{ value::six, suit::heart },
				card{ value::three, suit::diamond },
			};
			board new_board{ empty_columns, stock_pile, 2, empty_foundation };

			vector<shared_ptr<solitaire_move>> moves{ StockPileRecycleMove::find_moves(new_board) };
			StockPileRecycleMoveSet actual{ ToStockPileRecycleMoveSet(moves) };

			StockPileRecycleMoveSet expected{};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesStockPileAtEnd)
		{
			vector<card> stock_pile{
				card{ value::four, suit::club },
				card{ value::seven, suit::heart },
				card{ value::six, suit::heart },
				card{ value::three, suit::diamond },
			};
			board new_board{ empty_columns, stock_pile, 4, empty_foundation };

			vector<shared_ptr<solitaire_move>> moves{ StockPileRecycleMove::find_moves(new_board) };
			StockPileRecycleMoveSet actual{ ToStockPileRecycleMoveSet(moves) };

			StockPileRecycleMoveSet expected{
				make_shared<StockPileRecycleMove>(4),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestGetSourceIndex)
		{
			StockPileRecycleMove move{ 5 };

			Assert::AreEqual(size_t{ 5 }, move.get_source_index());
		}

		TEST_METHOD(TestHasCards)
		{
			StockPileRecycleMove move{ 5 };

			Assert::IsFalse(move.has_cards());
		}

		TEST_METHOD(TestApply)
		{
			vector<column> columns{
				column{
					empty_list_of_cards,
					empty_list_of_cards
				},
				column{
					empty_list_of_cards,
					empty_list_of_cards
				},
				column{
					empty_list_of_cards,
					empty_list_of_cards
				},
				column{
					empty_list_of_cards,
					empty_list_of_cards
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::king, suit::spade },
						card{ value::queen, suit::heart },
					}
				},
				column{
					empty_list_of_cards,
					empty_list_of_cards
				},
				column{
					empty_list_of_cards,
					empty_list_of_cards
				},
			};
			vector<card> stock_pile{
				card{ value::three, suit::club },
				card{ value::jack, suit::spade },
			};
			size_t stock_pile_index{ stock_pile.size() };
			map<suit, vector<card>> foundation{
				{
					suit::club,
					vector<card>{
						card{ value::ace, suit::club },
						card{ value::two, suit::club },
					}
				},
				{
					suit::diamond,
					empty_list_of_cards
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

			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, stock_pile_index, foundation) };

			StockPileRecycleMove move{ stock_pile_index };

			shared_ptr<board> actual{ move.apply(board_ptr) };

			board expected{ columns, stock_pile, 0, foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestEquals)
		{
			StockPileRecycleMove move1{ 5 };
			StockPileRecycleMove move2{ 5 };

			Assert::AreEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentIndex)
		{
			StockPileRecycleMove move1{ 5 };
			StockPileRecycleMove move2{ 4 };

			Assert::AreNotEqual(move1, move2);
		}
	};
}
