#include "CppUnitTest.h"

#include "BoardToString.h"
#include "CardToString.h"
#include "CardComparator.h"

#include <silnith/game/solitaire/move/ColumnToFoundationMove.h>

#include <memory>
#include <set>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game::deck;
using namespace silnith::game::solitaire;
using namespace silnith::game::solitaire::move;

using namespace std;
using namespace std::literals::string_literals;

struct ColumnToFoundationMoveComparator
{
public:
	bool operator()(shared_ptr<ColumnToFoundationMove> const& lhs, shared_ptr<ColumnToFoundationMove> const& rhs) const
	{
		if (lhs->get_source_column_index() < rhs->get_source_column_index())
		{
			return true;
		}
		else if (lhs->get_source_column_index() == rhs->get_source_column_index())
		{
			return lhs->get_cards().front() < rhs->get_cards().front();
		}
		else
		{
			return false;
		}
	}
};

using ColumnToFoundationMoveSet = set<shared_ptr<ColumnToFoundationMove>, ColumnToFoundationMoveComparator>;

bool operator==(ColumnToFoundationMoveSet const& lhs, ColumnToFoundationMoveSet const& rhs)
{
	vector<ColumnToFoundationMove> lhs_moves{};
	vector<ColumnToFoundationMove> rhs_moves{};
	for (shared_ptr<ColumnToFoundationMove> ptr : lhs)
	{
		lhs_moves.emplace_back(*ptr);
	}
	for (shared_ptr<ColumnToFoundationMove> ptr : rhs)
	{
		rhs_moves.emplace_back(*ptr);
	}
	return lhs_moves == rhs_moves;
}

ColumnToFoundationMoveSet ToColumnToFoundationMoveSet(vector<shared_ptr<solitaire_move>> const& vec)
{
	vector<shared_ptr<ColumnToFoundationMove>> typed_vec{};
	for (shared_ptr<solitaire_move> move : vec)
	{
		typed_vec.emplace_back(dynamic_pointer_cast<ColumnToFoundationMove>(move));
	}
	ColumnToFoundationMoveSet s{ typed_vec.begin(), typed_vec.end() };
	return s;
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(ColumnToFoundationMove const& move)
{
	return to_wstring(move);
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(ColumnToFoundationMoveSet const& moves)
{
	wostringstream out{};
	out << L"{"s;
	if (!moves.empty())
	{
		ColumnToFoundationMoveSet::const_iterator citer{ moves.cbegin() };
		ColumnToFoundationMoveSet::const_iterator cend{ moves.cend() };
		shared_ptr<ColumnToFoundationMove> ptr{ *citer };
		out << *ptr;
		citer++;
		while (citer != cend)
		{
			out << L", "s;
			shared_ptr<ColumnToFoundationMove> ptr{ *citer };
			out << *ptr;
			citer++;
		}
	}
	out << L"}"s;
	return out.str();
}

namespace SolitaireMoveTests
{
	TEST_CLASS(ColumnToFoundationMoveTests)
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
		TEST_METHOD(TestFindMovesEmptyColumns)
		{
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, empty_list_of_cards, 0, empty_foundation) };

			vector<shared_ptr<solitaire_move>> moves{ ColumnToFoundationMove::find_moves(*board_ptr) };
			ColumnToFoundationMoveSet actual{ ToColumnToFoundationMoveSet(moves) };

			ColumnToFoundationMoveSet expected{};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMoves)
		{
			vector<column> columns{
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ace, suit::club },
					}
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ace, suit::diamond },
					}
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ace, suit::heart },
					}
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ace, suit::spade },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			vector<shared_ptr<solitaire_move>> moves{ ColumnToFoundationMove::find_moves(*board_ptr) };
			ColumnToFoundationMoveSet actual{ ToColumnToFoundationMoveSet(moves) };

			ColumnToFoundationMoveSet expected{
				make_shared<ColumnToFoundationMove>(0, card{ value::ace, suit::club }),
				make_shared<ColumnToFoundationMove>(1, card{ value::ace, suit::diamond }),
				make_shared<ColumnToFoundationMove>(2, card{ value::ace, suit::heart }),
				make_shared<ColumnToFoundationMove>(3, card{ value::ace, suit::spade }),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestConstructorSourceColumnOutOfRange)
		{
			board new_board{ empty_columns, empty_list_of_cards, 0, empty_foundation };

			Assert::ExpectException<out_of_range>([new_board]()
				{
					ColumnToFoundationMove move{ 8, new_board };
				});
		}

		TEST_METHOD(TestConstructorEmptySourceColumn)
		{
			board new_board{ empty_columns, empty_list_of_cards, 0, empty_foundation };

			Assert::ExpectException<out_of_range>([new_board]()
				{
					ColumnToFoundationMove move{ 4, new_board };
				});
		}

		TEST_METHOD(TestGetSourceColumnIndex)
		{
			ColumnToFoundationMove move{ 3, card{ value::ace, suit::club } };

			Assert::AreEqual(size_t{ 3 }, move.get_source_column_index());
		}

		TEST_METHOD(TestHasCards)
		{
			ColumnToFoundationMove move{ 3, card{ value::ace, suit::club } };

			Assert::IsTrue(move.has_cards());
		}

		TEST_METHOD(TestGetCards)
		{
			ColumnToFoundationMove move{ 3, card{ value::ace, suit::club } };

			vector<card> actual{ move.get_cards() };

			vector<card> expected{
				card{ value::ace, suit::club },
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestApply)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ace, suit::club },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			ColumnToFoundationMove move{ 2, board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			map<suit, vector<card>> expected_foundation{
				{
					suit::club,
					vector<card>{
						card{ value::ace, suit::club },
					}
				},
				{ suit::diamond, empty_list_of_cards },
				{ suit::heart, empty_list_of_cards },
				{ suit::spade, empty_list_of_cards },
			};
			board expected{ empty_columns, empty_list_of_cards, 0, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyFromEmptyColumn)
		{
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, empty_list_of_cards, 0, empty_foundation) };

			ColumnToFoundationMove move{ 2, card{ value::ace, suit::club } };

			Assert::ExpectException<out_of_range>([board_ptr, move]()
				{
					shared_ptr<board const> actual{ move.apply(board_ptr) };
				});
		}

		TEST_METHOD(TestApplyNonEmptyFoundation)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::four, suit::club },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
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
			shared_ptr<board> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, foundation) };

			ColumnToFoundationMove move{ 2, board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			map<suit, vector<card>> expected_foundation{
				{
					suit::club,
					vector<card>{
						card{ value::ace, suit::club },
						card{ value::two, suit::club },
						card{ value::three, suit::club },
						card{ value::four, suit::club },
					}
				},
				{ suit::diamond, empty_list_of_cards },
				{ suit::heart, empty_list_of_cards },
				{ suit::spade, empty_list_of_cards },
			};
			board expected{ empty_columns, empty_list_of_cards, 0, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyKeepsStockPile)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ace, suit::club },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> stock_pile{
				card{ value::four, suit::spade },
				card{ value::six, suit::heart },
				card{ value::queen, suit::heart },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, 0, empty_foundation) };

			ColumnToFoundationMove move{ 2, board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			map<suit, vector<card>> expected_foundation{
				{
					suit::club,
					vector<card>{
						card{ value::ace, suit::club },
					}
				},
				{ suit::diamond, empty_list_of_cards },
				{ suit::heart, empty_list_of_cards },
				{ suit::spade, empty_list_of_cards },
			};
			board expected{ empty_columns, stock_pile, 0, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyKeepsStockPileIndex)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ace, suit::club },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> stock_pile{
				card{ value::four, suit::spade },
				card{ value::six, suit::heart },
				card{ value::queen, suit::heart },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, 2, empty_foundation) };

			ColumnToFoundationMove move{ 2, board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			map<suit, vector<card>> expected_foundation{
				{
					suit::club,
					vector<card>{
						card{ value::ace, suit::club },
					}
				},
				{ suit::diamond, empty_list_of_cards },
				{ suit::heart, empty_list_of_cards },
				{ suit::spade, empty_list_of_cards },
			};
			board expected{ empty_columns, stock_pile, 2, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestEquals)
		{
			ColumnToFoundationMove move1{ 4, card{ value::ace, suit::club } };
			ColumnToFoundationMove move2{ 4, card{ value::ace, suit::club } };

			Assert::AreEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentColumn)
		{
			ColumnToFoundationMove move1{ 5, card{ value::ace, suit::club } };
			ColumnToFoundationMove move2{ 4, card{ value::ace, suit::club } };

			Assert::AreNotEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentCard)
		{
			ColumnToFoundationMove move1{ 4, card{ value::ace, suit::club } };
			ColumnToFoundationMove move2{ 4, card{ value::ace, suit::heart } };

			Assert::AreNotEqual(move1, move2);
		}
	};
}
