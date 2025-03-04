#include "CppUnitTest.h"

#include "BoardToString.h"
#include "CardToString.h"
#include "CardComparator.h"

#include <silnith/game/solitaire/move/ColumnToColumnMove.h>

#include <memory>
#include <set>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game::deck;
using namespace silnith::game::solitaire;
using namespace silnith::game::solitaire::move;

using namespace std;
using namespace std::literals::string_literals;

struct ColumnToColumnMoveComparator
{
public:
	bool operator()(shared_ptr<ColumnToColumnMove const> const& lhs, shared_ptr<ColumnToColumnMove const> const& rhs) const
	{
		if (lhs->get_source_column_index() < rhs->get_source_column_index())
		{
			return true;
		}
		else if (lhs->get_source_column_index() == rhs->get_source_column_index())
		{
			if (lhs->get_destination_column_index() < rhs->get_destination_column_index())
			{
				return true;
			}
			else if (lhs->get_destination_column_index() == rhs->get_destination_column_index())
			{
				return lhs->get_cards() < rhs->get_cards();
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
};

using ColumnToColumnMoveSet = set<shared_ptr<ColumnToColumnMove const>, ColumnToColumnMoveComparator>;

bool operator==(ColumnToColumnMoveSet const& lhs, ColumnToColumnMoveSet const& rhs)
{
	vector<ColumnToColumnMove> lhs_moves{};
	vector<ColumnToColumnMove> rhs_moves{};
	for (shared_ptr<ColumnToColumnMove const> ptr : lhs)
	{
		lhs_moves.emplace_back(*ptr);
	}
	for (shared_ptr<ColumnToColumnMove const> ptr : rhs)
	{
		rhs_moves.emplace_back(*ptr);
	}
	return lhs_moves == rhs_moves;
}

ColumnToColumnMoveSet ToColumnToColumnMoveSet(vector<shared_ptr<solitaire_move const>> const& vec)
{
	vector<shared_ptr<ColumnToColumnMove const>> typed_vec{};
	for (shared_ptr<solitaire_move const> move : vec)
	{
		typed_vec.emplace_back(dynamic_pointer_cast<ColumnToColumnMove const>(move));
	}
	ColumnToColumnMoveSet s{ typed_vec.begin(), typed_vec.end() };
	return s;
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(ColumnToColumnMove const& move)
{
	return to_wstring(move);
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(ColumnToColumnMoveSet const& moves)
{
	wostringstream out{};
	out << L"{"s;
	if (!moves.empty())
	{
		ColumnToColumnMoveSet::const_iterator citer{ moves.cbegin() };
		ColumnToColumnMoveSet::const_iterator cend{ moves.cend() };
		shared_ptr<ColumnToColumnMove const> ptr{ *citer };
		out << *ptr;
		citer++;
		while (citer != cend)
		{
			out << L", "s;
			shared_ptr<ColumnToColumnMove const> ptr{ *citer };
			out << *ptr;
			citer++;
		}
	}
	out << L"}"s;
	return out.str();
}

namespace SolitaireMoveTests
{
	TEST_CLASS(ColumnToColumnMoveTests)
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
		TEST_METHOD(TestFindMovesKing)
		{
			vector<card> run{
				card{ value::king, suit::spade },
			};
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					run
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board const> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			vector<shared_ptr<solitaire_move const>> moves{ ColumnToColumnMove::find_moves(*board_ptr) };
			ColumnToColumnMoveSet actual{ ToColumnToColumnMoveSet(moves) };

			ColumnToColumnMoveSet expected{
				make_shared<ColumnToColumnMove>(3, 0, run),
				make_shared<ColumnToColumnMove>(3, 1, run),
				make_shared<ColumnToColumnMove>(3, 2, run),
				make_shared<ColumnToColumnMove>(3, 4, run),
				make_shared<ColumnToColumnMove>(3, 5, run),
				make_shared<ColumnToColumnMove>(3, 6, run),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesKingRun)
		{
			vector<card> run{
				card{ value::king, suit::spade },
				card{ value::queen, suit::heart },
				card{ value::jack, suit::spade },
			};
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					run
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board const> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			vector<shared_ptr<solitaire_move const>> moves{ ColumnToColumnMove::find_moves(*board_ptr) };
			ColumnToColumnMoveSet actual{ ToColumnToColumnMoveSet(moves) };

			ColumnToColumnMoveSet expected{
				make_shared<ColumnToColumnMove>(3, 0, run),
				make_shared<ColumnToColumnMove>(3, 1, run),
				make_shared<ColumnToColumnMove>(3, 2, run),
				make_shared<ColumnToColumnMove>(3, 4, run),
				make_shared<ColumnToColumnMove>(3, 5, run),
				make_shared<ColumnToColumnMove>(3, 6, run),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesAdjoining)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ten, suit::club },
						card{ value::nine, suit::diamond },
						card{ value::eight, suit::club },
					}
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::seven, suit::heart },
						card{ value::six, suit::spade },
						card{ value::five, suit::heart },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board const> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			vector<shared_ptr<solitaire_move const>> moves{ ColumnToColumnMove::find_moves(*board_ptr) };
			ColumnToColumnMoveSet actual{ ToColumnToColumnMoveSet(moves) };

			ColumnToColumnMoveSet expected{
				make_shared<ColumnToColumnMove>(2, 1, vector<card>{
					card{ value::seven, suit::heart },
					card{ value::six, suit::spade },
					card{ value::five, suit::heart },
				}),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesAdjoiningWrongColor)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ten, suit::club },
						card{ value::nine, suit::diamond },
						card{ value::eight, suit::club },
					}
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::seven, suit::spade },
						card{ value::six, suit::heart },
						card{ value::five, suit::spade },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board const> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			vector<shared_ptr<solitaire_move const>> moves{ ColumnToColumnMove::find_moves(*board_ptr) };
			ColumnToColumnMoveSet actual{ ToColumnToColumnMoveSet(moves) };

			ColumnToColumnMoveSet expected{};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesAdjoiningMultipleDestination)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ten, suit::club },
						card{ value::nine, suit::diamond },
						card{ value::eight, suit::club },
					}
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::seven, suit::heart },
						card{ value::six, suit::spade },
						card{ value::five, suit::heart },
					}
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ten, suit::spade },
						card{ value::nine, suit::heart },
						card{ value::eight, suit::spade },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board const> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			vector<shared_ptr<solitaire_move const>> moves{ ColumnToColumnMove::find_moves(*board_ptr) };
			ColumnToColumnMoveSet actual{ ToColumnToColumnMoveSet(moves) };

			ColumnToColumnMoveSet expected{
				make_shared<ColumnToColumnMove>(2, 1, vector<card>{
					card{ value::seven, suit::heart },
					card{ value::six, suit::spade },
					card{ value::five, suit::heart },
				}),
				make_shared<ColumnToColumnMove>(2, 3, vector<card>{
					card{ value::seven, suit::heart },
					card{ value::six, suit::spade },
					card{ value::five, suit::heart },
				}),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesAdjoiningMultipleSource)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ten, suit::club },
						card{ value::nine, suit::diamond },
						card{ value::eight, suit::club },
					}
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::seven, suit::heart },
						card{ value::six, suit::spade },
						card{ value::five, suit::heart },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::seven, suit::diamond },
						card{ value::six, suit::club },
						card{ value::five, suit::diamond },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board const> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			vector<shared_ptr<solitaire_move const>> moves{ ColumnToColumnMove::find_moves(*board_ptr) };
			ColumnToColumnMoveSet actual{ ToColumnToColumnMoveSet(moves) };

			ColumnToColumnMoveSet expected{
				make_shared<ColumnToColumnMove>(2, 1, vector<card>{
					card{ value::seven, suit::heart },
					card{ value::six, suit::spade },
					card{ value::five, suit::heart },
				}),
				make_shared<ColumnToColumnMove>(4, 1, vector<card>{
					card{ value::seven, suit::diamond },
					card{ value::six, suit::club },
					card{ value::five, suit::diamond },
				}),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesOverlapping)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ten, suit::club },
						card{ value::nine, suit::diamond },
						card{ value::eight, suit::club },
					}
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::eight, suit::spade },
						card{ value::seven, suit::heart },
						card{ value::six, suit::spade },
						card{ value::five, suit::heart },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board const> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			vector<shared_ptr<solitaire_move const>> moves{ ColumnToColumnMove::find_moves(*board_ptr) };
			ColumnToColumnMoveSet actual{ ToColumnToColumnMoveSet(moves) };

			ColumnToColumnMoveSet expected{
				make_shared<ColumnToColumnMove>(2, 1, vector<card>{
					card{ value::seven, suit::heart },
					card{ value::six, suit::spade },
					card{ value::five, suit::heart },
				}),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesOverlappingWrongColor)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ten, suit::club },
						card{ value::nine, suit::diamond },
						card{ value::eight, suit::club },
					}
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::eight, suit::heart },
						card{ value::seven, suit::spade },
						card{ value::six, suit::heart },
						card{ value::five, suit::spade },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board const> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			vector<shared_ptr<solitaire_move const>> moves{ ColumnToColumnMove::find_moves(*board_ptr) };
			ColumnToColumnMoveSet actual{ ToColumnToColumnMoveSet(moves) };

			ColumnToColumnMoveSet expected{};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesDisjoint)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ten, suit::club },
						card{ value::nine, suit::diamond },
						card{ value::eight, suit::club },
					}
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::six, suit::heart },
						card{ value::five, suit::spade },
						card{ value::four, suit::heart },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board const> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			vector<shared_ptr<solitaire_move const>> moves{ ColumnToColumnMove::find_moves(*board_ptr) };
			ColumnToColumnMoveSet actual{ ToColumnToColumnMoveSet(moves) };

			ColumnToColumnMoveSet expected{};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestConstructorSourceAndDestinationSame)
		{
			vector<card> run{
				card{ value::ace, suit::club },
			};
			Assert::ExpectException<invalid_argument>([run]()
				{
					ColumnToColumnMove move{ 2, 2, run };
				});
		}

		TEST_METHOD(TestGetSourceColumnIndex)
		{
			vector<card> run{
				card{ value::three, suit::club },
				card{ value::two, suit::diamond },
				card{ value::ace, suit::club },
			};
			ColumnToColumnMove move{ 2, 5, run };

			Assert::AreEqual(size_t{ 2 }, move.get_source_column_index());
		}

		TEST_METHOD(TestGetDestinationColumnIndex)
		{
			vector<card> run{
				card{ value::three, suit::club },
				card{ value::two, suit::diamond },
				card{ value::ace, suit::club },
			};
			ColumnToColumnMove move{ 2, 5, run };

			Assert::AreEqual(size_t{ 5 }, move.get_destination_column_index());
		}

		TEST_METHOD(TestGetCards)
		{
			vector<card> run{
				card{ value::three, suit::club },
				card{ value::two, suit::diamond },
				card{ value::ace, suit::club },
			};
			ColumnToColumnMove move{ 2, 5, run };

			Assert::AreEqual(run, move.get_cards());
		}

		TEST_METHOD(TestConstructorGetCards)
		{
			vector<card> run{
				card{ value::three, suit::club },
				card{ value::two, suit::diamond },
				card{ value::ace, suit::club },
			};
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					run
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };
			ColumnToColumnMove move{ 2, 5, 3, *board_ptr };

			Assert::AreEqual(run, move.get_cards());
		}

		TEST_METHOD(TestApply)
		{
			vector<card> run{
				card{ value::king, suit::spade },
			};
			vector<column> columns{
				column{
					empty_list_of_cards,
					run
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			ColumnToColumnMove move{ 0, 1, 1, *board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					run
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			board expected{ expected_columns, empty_list_of_cards, 0, empty_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyBig)
		{
			vector<card> run{
				card{ value::king, suit::heart },
				card{ value::queen, suit::club },
				card{ value::jack, suit::heart },
				card{ value::ten, suit::club },
				card{ value::nine, suit::heart },
			};
			vector<column> columns{
				column{
					empty_list_of_cards,
					run
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			ColumnToColumnMove move{ 0, 1, 5, *board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					run
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			board expected{ expected_columns, empty_list_of_cards, 0, empty_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyOntoAnother)
		{
			vector<card> topRun{
				card{ value::seven, suit::heart },
				card{ value::six, suit::club },
			};
			vector<card> bottomRun{
				card{ value::five, suit::heart },
				card{ value::four, suit::club },
				card{ value::three, suit::heart },
			};
			vector<card> combinedRun{
				card{ value::seven, suit::heart },
				card{ value::six, suit::club },
				card{ value::five, suit::heart },
				card{ value::four, suit::club },
				card{ value::three, suit::heart },
			};
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					topRun
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					bottomRun
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			ColumnToColumnMove move{ 4, 2, 3, *board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					combinedRun
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			board expected{ expected_columns, empty_list_of_cards, 0, empty_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyPartialOntoAnother)
		{
			vector<card> topRun{
				card{ value::seven, suit::heart },
				card{ value::six, suit::club },
			};
			vector<card> bottomRun{
				card{ value::six, suit::spade },
				card{ value::five, suit::heart },
				card{ value::four, suit::club },
				card{ value::three, suit::heart },
			};
			vector<card> combinedRun{
				card{ value::seven, suit::heart },
				card{ value::six, suit::club },
				card{ value::five, suit::heart },
				card{ value::four, suit::club },
				card{ value::three, suit::heart },
			};
			vector<card> remainingRun{
				card{ value::six, suit::spade },
			};
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					bottomRun
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					topRun
				},
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, empty_foundation) };

			ColumnToColumnMove move{ 1, 5, 3, *board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					remainingRun
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					combinedRun
				},
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			board expected{ expected_columns, empty_list_of_cards, 0, empty_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyKeepsStockPile)
		{
			vector<card> run{
				card{ value::king, suit::spade },
			};
			vector<column> columns{
				column{
					empty_list_of_cards,
					run
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> stock_pile{
				card{ value::two, suit::club },
				card{ value::ten, suit::spade },
				card{ value::three,suit::spade },
				card{ value::three, suit::heart },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, 0, empty_foundation) };

			ColumnToColumnMove move{ 0, 1, 1, *board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					run
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			board expected{ expected_columns, stock_pile, 0, empty_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyKeepsStockPileIndex)
		{
			vector<card> run{
				card{ value::king, suit::spade },
			};
			vector<column> columns{
				column{
					empty_list_of_cards,
					run
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> stock_pile{
				card{ value::two, suit::club },
				card{ value::ten, suit::spade },
				card{ value::three,suit::spade },
				card{ value::three, suit::heart },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, 4, empty_foundation) };

			ColumnToColumnMove move{ 0, 1, 1, *board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					run
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			board expected{ expected_columns, stock_pile, 4, empty_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyKeepsFoundation)
		{
			vector<card> run{
				card{ value::king, suit::spade },
			};
			vector<column> columns{
				column{
					empty_list_of_cards,
					run
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			map<suit, vector<card>> foundation{
				{ suit::club, empty_list_of_cards },
				{
					suit::diamond,
					vector<card>{
						card{ value::ace, suit::diamond },
						card{ value::two, suit::diamond },
						card{ value::three, suit::diamond },
					}
				},
				{ suit::heart, empty_list_of_cards },
				{
					suit::spade,
					vector<card>{
						card{ value::ace, suit::spade },
						card{ value::two, suit::spade },
					}
				},
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, foundation) };

			ColumnToColumnMove move{ 0, 1, 1, *board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					run
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			board expected{ expected_columns, empty_list_of_cards, 0, foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestEquals)
		{
			vector<card> run{
				card{ value::three, suit::club },
				card{ value::two, suit::diamond },
				card{ value::ace, suit::club },
			};
			ColumnToColumnMove move1{ 2, 5, run };
			ColumnToColumnMove move2{ 2, 5, run };

			Assert::AreEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentSourceColumn)
		{
			vector<card> run{
				card{ value::three, suit::club },
				card{ value::two, suit::diamond },
				card{ value::ace, suit::club },
			};
			ColumnToColumnMove move1{ 2, 5, run };
			ColumnToColumnMove move2{ 1, 5, run };

			Assert::AreNotEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentDestinationColumn)
		{
			vector<card> run{
				card{ value::three, suit::club },
				card{ value::two, suit::diamond },
				card{ value::ace, suit::club },
			};
			ColumnToColumnMove move1{ 2, 5, run };
			ColumnToColumnMove move2{ 2, 4, run };

			Assert::AreNotEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentCards)
		{
			vector<card> run1{
				card{ value::three, suit::club },
				card{ value::two, suit::diamond },
				card{ value::ace, suit::club },
			};
			vector<card> run2{
				card{ value::three, suit::spade },
				card{ value::two, suit::diamond },
				card{ value::ace, suit::club },
			};
			ColumnToColumnMove move1{ 2, 5, run1 };
			ColumnToColumnMove move2{ 2, 5, run2 };

			Assert::AreNotEqual(move1, move2);
		}
	};
}
