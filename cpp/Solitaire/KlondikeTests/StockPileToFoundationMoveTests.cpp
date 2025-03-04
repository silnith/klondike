#include "CppUnitTest.h"

#include "BoardToString.h"
#include "CardToString.h"
#include "CardComparator.h"

#include <silnith/game/solitaire/move/StockPileToFoundationMove.h>

#include <array>
#include <functional>
#include <map>
#include <memory>
#include <set>
#include <string>
#include <vector>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game::deck;
using namespace silnith::game::solitaire;
using namespace silnith::game::solitaire::move;

using namespace std;
using namespace std::literals::string_literals;

struct StockPileToFoundationMoveComparator
{
public:
	bool operator()(shared_ptr<StockPileToFoundationMove> const& lhs, shared_ptr<StockPileToFoundationMove> const& rhs) const
	{
		if (lhs->get_source_index() < rhs->get_source_index())
		{
			return true;
		}
		else if (lhs->get_source_index() == rhs->get_source_index())
		{
			return lhs->get_cards().front() < rhs->get_cards().front();
		}
		else
		{
			return false;
		}
	}
};

using StockPileToFoundationMoveSet = set<shared_ptr<StockPileToFoundationMove>, StockPileToFoundationMoveComparator>;

bool operator==(StockPileToFoundationMoveSet const& lhs, StockPileToFoundationMoveSet const& rhs)
{
	vector<StockPileToFoundationMove> lhs_moves{};
	vector<StockPileToFoundationMove> rhs_moves{};
	for (shared_ptr<StockPileToFoundationMove> ptr : lhs)
	{
		lhs_moves.emplace_back(*ptr);
	}
	for (shared_ptr<StockPileToFoundationMove> ptr : rhs)
	{
		rhs_moves.emplace_back(*ptr);
	}
	return lhs_moves == rhs_moves;
}

StockPileToFoundationMoveSet ToStockPileRecycleMoveSet(vector<shared_ptr<solitaire_move>> const& vec)
{
	vector<shared_ptr<StockPileToFoundationMove>> typed_vec{};
	for (shared_ptr<solitaire_move> move : vec)
	{
		typed_vec.emplace_back(dynamic_pointer_cast<StockPileToFoundationMove>(move));
	}
	StockPileToFoundationMoveSet s{ typed_vec.begin(), typed_vec.end() };
	return s;
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(StockPileToFoundationMove const& move)
{
	return to_wstring(move);
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(StockPileToFoundationMoveSet const& moves)
{
	wostringstream out{};
	out << L"{"s;
	if (!moves.empty())
	{
		StockPileToFoundationMoveSet::const_iterator citer{ moves.cbegin() };
		StockPileToFoundationMoveSet::const_iterator cend{ moves.cend() };
		shared_ptr<StockPileToFoundationMove> ptr{ *citer };
		out << *ptr;
		citer++;
		while (citer != cend)
		{
			out << L", "s;
			shared_ptr<StockPileToFoundationMove> ptr{ *citer };
			out << *ptr;
			citer++;
		}
	}
	out << L"}"s;
	return out.str();
}

namespace SolitaireMoveTests
{
	TEST_CLASS(StockPileToFoundationMoveTests)
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

			vector<shared_ptr<solitaire_move>> moves{ StockPileToFoundationMove::find_moves(new_board) };
			StockPileToFoundationMoveSet actual{ ToStockPileRecycleMoveSet(moves) };

			StockPileToFoundationMoveSet expected{};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesEmptyFoundation)
		{
			vector<card> stock_pile{
				card{ value::ace, suit::club },
			};
			board new_board{ empty_columns, stock_pile, 1, empty_foundation };

			vector<shared_ptr<solitaire_move>> moves{ StockPileToFoundationMove::find_moves(new_board) };
			StockPileToFoundationMoveSet actual{ ToStockPileRecycleMoveSet(moves) };

			StockPileToFoundationMoveSet expected{
				make_shared<StockPileToFoundationMove>(1, card{ value::ace, suit::club }),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMoves)
		{
			vector<card> stock_pile{
				card{ value::four, suit::club },
				card{ value::four, suit::diamond },
				card{ value::four, suit::heart },
				card{ value::four, suit::spade },
			};
			map<suit, vector<card>> foundation{
				{
					suit::club,
					vector<card>{
						card{ value::ace, suit::club },
						card{ value::two, suit::club },
						card{ value::three, suit::club },
					}
				},
				{ suit::diamond, empty_list_of_cards },
				{ suit::heart, empty_list_of_cards },
				{ suit::spade, empty_list_of_cards },
			};
			board new_board{ empty_columns, stock_pile, 1, foundation };

			vector<shared_ptr<solitaire_move>> moves{ StockPileToFoundationMove::find_moves(new_board) };
			StockPileToFoundationMoveSet actual{ ToStockPileRecycleMoveSet(moves) };

			StockPileToFoundationMoveSet expected{
				make_shared<StockPileToFoundationMove>(1, card{ value::four, suit::club }),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestGetSourceIndex)
		{
			StockPileToFoundationMove move{ 5, card{ value::ace, suit::club } };

			Assert::AreEqual(size_t{ 5 }, move.get_source_index());
		}

		TEST_METHOD(TestGetSourceIndexFromBoard)
		{
			vector<card> stock_pile{
				card{ value::ace, suit::spade },
			};
			board b{ empty_columns, stock_pile, 1, empty_foundation };

			StockPileToFoundationMove move{ b };

			Assert::AreEqual(size_t{ 1 }, move.get_source_index());
		}

		TEST_METHOD(TestHasCard)
		{
			StockPileToFoundationMove move{ 5, card{ value::ace, suit::club } };

			Assert::IsTrue(move.has_cards());
		}

		TEST_METHOD(TestGetCards)
		{
			StockPileToFoundationMove move{ 5, card{ value::ace, suit::club } };

			vector<card> expected{
				card{ value::ace, suit::club },
			};
			Assert::AreEqual(expected, move.get_cards());
		}

		TEST_METHOD(TestConstructorGetCards)
		{
			vector<card> stock_pile{
				card{ value::ace, suit::club },
				card{ value::two, suit::heart },
				card{ value::three, suit::spade },
			};
			board b{ empty_columns, stock_pile, 2, empty_foundation };

			StockPileToFoundationMove move{ b };

			vector<card> expected{
				card{ value::two, suit::heart },
			};
			Assert::AreEqual(expected, move.get_cards());
		}

		TEST_METHOD(TestApply)
		{
			vector<card> stock_pile{
				card{ value::ace, suit::spade },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, stock_pile, 1, empty_foundation) };

			StockPileToFoundationMove move{ board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			map<suit, vector<card>> expected_foundation{
				{ suit::club, empty_list_of_cards },
				{ suit::diamond, empty_list_of_cards },
				{ suit::heart, empty_list_of_cards },
				{
					suit::spade,
					vector<card>{
						card{ value::ace, suit::spade },
					}
				},
			};
			board expected{ empty_columns, empty_list_of_cards, 0, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyEmpty)
		{
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, empty_list_of_cards, 0, empty_foundation) };

			StockPileToFoundationMove move{ 0, card{ value::ace, suit::club } };

			Assert::ExpectException<out_of_range>([board_ptr, move]()
				{
					shared_ptr<board const> actual{ move.apply(board_ptr) };
				});
		}

		TEST_METHOD(TestApplyUnderflow)
		{
			vector<card> stock_pile{
				card{ value::ace, suit::spade },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, stock_pile, 0, empty_foundation) };

			// This is not actually the correct card.
			StockPileToFoundationMove move{ 0, card{ value::ace, suit::spade } };

			Assert::ExpectException<out_of_range>([board_ptr, move]()
				{
					shared_ptr<board const> actual{ move.apply(board_ptr) };
				});
		}

		TEST_METHOD(TestApplyNonEmpty)
		{
			vector<card> stock_pile{
				card{ value::five, suit::spade },
			};
			map<suit, vector<card>> foundation{
				{ suit::club, empty_list_of_cards },
				{
					suit::diamond,
					vector<card>{
						card{ value::ace, suit::diamond },
						card{ value::two, suit::diamond },
					}
				},
				{ suit::heart, empty_list_of_cards },
				{
					suit::spade,
					vector<card>{
						card{ value::ace, suit::spade },
						card{ value::two, suit::spade },
						card{ value::three, suit::spade },
						card{ value::four, suit::spade },
					}
				},
			};
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, stock_pile, 1, foundation) };

			StockPileToFoundationMove move{ board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			map<suit, vector<card>> expected_foundation{
				{ suit::club, empty_list_of_cards },
				{
					suit::diamond,
					vector<card>{
						card{ value::ace, suit::diamond },
						card{ value::two, suit::diamond },
					}
				},
				{ suit::heart, empty_list_of_cards },
				{
					suit::spade,
					vector<card>{
						card{ value::ace, suit::spade },
						card{ value::two, suit::spade },
						card{ value::three, suit::spade },
						card{ value::four, suit::spade },
						card{ value::five, suit::spade },
					}
				},
			};
			board expected{ empty_columns, empty_list_of_cards, 0, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyFromBeginningNonEmpty)
		{
			vector<card> stock_pile{
				card{ value::five, suit::spade },
				card{ value::six, suit::spade },
				card{ value::jack, suit::club },
				card{ value::queen, suit::club },
				card{ value::two, suit::heart },
				card{ value::ten, suit::diamond },
			};
			map<suit, vector<card>> foundation{
				{ suit::club, empty_list_of_cards },
				{
					suit::diamond,
					vector<card>{
						card{ value::ace, suit::diamond },
						card{ value::two, suit::diamond },
					}
				},
				{ suit::heart, empty_list_of_cards },
				{
					suit::spade,
					vector<card>{
						card{ value::ace, suit::spade },
						card{ value::two, suit::spade },
						card{ value::three, suit::spade },
						card{ value::four, suit::spade },
					}
				},
			};
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, stock_pile, 1, foundation) };

			StockPileToFoundationMove move{ board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<card> expected_stock_pile{
				card{ value::six, suit::spade },
				card{ value::jack, suit::club },
				card{ value::queen, suit::club },
				card{ value::two, suit::heart },
				card{ value::ten, suit::diamond },
			};
			map<suit, vector<card>> expected_foundation{
				{ suit::club, empty_list_of_cards },
				{
					suit::diamond,
					vector<card>{
						card{ value::ace, suit::diamond },
						card{ value::two, suit::diamond },
					}
				},
				{ suit::heart, empty_list_of_cards },
				{
					suit::spade,
					vector<card>{
						card{ value::ace, suit::spade },
						card{ value::two, suit::spade },
						card{ value::three, suit::spade },
						card{ value::four, suit::spade },
						card{ value::five, suit::spade },
					}
				},
			};
			board expected{ empty_columns, expected_stock_pile, 0, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyFromMiddleNonEmpty)
		{
			vector<card> stock_pile{
				card{ value::six, suit::spade },
				card{ value::jack, suit::club },
				card{ value::five, suit::spade },
				card{ value::queen, suit::club },
				card{ value::two, suit::heart },
				card{ value::ten, suit::diamond },
			};
			map<suit, vector<card>> foundation{
				{ suit::club, empty_list_of_cards },
				{
					suit::diamond,
					vector<card>{
						card{ value::ace, suit::diamond },
						card{ value::two, suit::diamond },
					}
				},
				{ suit::heart, empty_list_of_cards },
				{
					suit::spade,
					vector<card>{
						card{ value::ace, suit::spade },
						card{ value::two, suit::spade },
						card{ value::three, suit::spade },
						card{ value::four, suit::spade },
					}
				},
			};
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, stock_pile, 3, foundation) };

			StockPileToFoundationMove move{ board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<card> expected_stock_pile{
				card{ value::six, suit::spade },
				card{ value::jack, suit::club },
				card{ value::queen, suit::club },
				card{ value::two, suit::heart },
				card{ value::ten, suit::diamond },
			};
			map<suit, vector<card>> expected_foundation{
				{ suit::club, empty_list_of_cards },
				{
					suit::diamond,
					vector<card>{
						card{ value::ace, suit::diamond },
						card{ value::two, suit::diamond },
					}
				},
				{ suit::heart, empty_list_of_cards },
				{
					suit::spade,
					vector<card>{
						card{ value::ace, suit::spade },
						card{ value::two, suit::spade },
						card{ value::three, suit::spade },
						card{ value::four, suit::spade },
						card{ value::five, suit::spade },
					}
				},
			};
			board expected{ empty_columns, expected_stock_pile, 2, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyFromEndNonEmpty)
		{
			vector<card> stock_pile{
				card{ value::six, suit::spade },
				card{ value::jack, suit::club },
				card{ value::queen, suit::club },
				card{ value::two, suit::heart },
				card{ value::ten, suit::diamond },
				card{ value::five, suit::spade },
			};
			map<suit, vector<card>> foundation{
				{ suit::club, empty_list_of_cards },
				{
					suit::diamond,
					vector<card>{
						card{ value::ace, suit::diamond },
						card{ value::two, suit::diamond },
					}
				},
				{ suit::heart, empty_list_of_cards },
				{
					suit::spade,
					vector<card>{
						card{ value::ace, suit::spade },
						card{ value::two, suit::spade },
						card{ value::three, suit::spade },
						card{ value::four, suit::spade },
					}
				},
			};
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, stock_pile, 6, foundation) };

			StockPileToFoundationMove move{ board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<card> expected_stock_pile{
				card{ value::six, suit::spade },
				card{ value::jack, suit::club },
				card{ value::queen, suit::club },
				card{ value::two, suit::heart },
				card{ value::ten, suit::diamond },
			};
			map<suit, vector<card>> expected_foundation{
				{ suit::club, empty_list_of_cards },
				{
					suit::diamond,
					vector<card>{
						card{ value::ace, suit::diamond },
						card{ value::two, suit::diamond },
					}
				},
				{ suit::heart, empty_list_of_cards },
				{
					suit::spade,
					vector<card>{
						card{ value::ace, suit::spade },
						card{ value::two, suit::spade },
						card{ value::three, suit::spade },
						card{ value::four, suit::spade },
						card{ value::five, suit::spade },
					}
				},
			};
			board expected{ empty_columns, expected_stock_pile, 5, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyKeepsColumns)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::four, suit::diamond },
						card{ value::three, suit::spade },
						card{ value::two, suit::diamond },
						card{ value::ace, suit::spade },
					}
				},
				column{
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
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> stock_pile{
				card{ value::ace, suit::spade },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, 1, empty_foundation) };

			StockPileToFoundationMove move{ board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			map<suit, vector<card>> expected_foundation{
				{ suit::club, empty_list_of_cards },
				{ suit::diamond, empty_list_of_cards },
				{ suit::heart, empty_list_of_cards },
				{
					suit::spade,
					vector<card>{
						card{ value::ace, suit::spade },
					}
				},
			};
			board expected{ columns, empty_list_of_cards, 0, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestEquals)
		{
			StockPileToFoundationMove move1{ 5, card{ value::ace, suit::club } };
			StockPileToFoundationMove move2{ 5, card{ value::ace, suit::club } };

			Assert::AreEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentSourceIndex)
		{
			StockPileToFoundationMove move1{ 5, card{ value::ace, suit::club } };
			StockPileToFoundationMove move2{ 4, card{ value::ace, suit::club } };

			Assert::AreNotEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentCard)
		{
			StockPileToFoundationMove move1{ 5, card{ value::ace, suit::club } };
			StockPileToFoundationMove move2{ 5, card{ value::ace, suit::heart } };

			Assert::AreNotEqual(move1, move2);
		}
	};
}
