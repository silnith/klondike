#include "CppUnitTest.h"

#include "BoardToString.h"
#include "CardToString.h"
#include "CardComparator.h"

#include <silnith/game/solitaire/move/FoundationToColumnMove.h>

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

struct FoundationToColumnMoveComparator
{
public:
	bool operator()(shared_ptr<FoundationToColumnMove> const& lhs, shared_ptr<FoundationToColumnMove> const& rhs) const
	{
		if (lhs->get_destination_column_index() < rhs->get_destination_column_index())
		{
			return true;
		}
		else if (lhs->get_destination_column_index() == rhs->get_destination_column_index())
		{
			return lhs->get_cards().front() < rhs->get_cards().front();
		}
		else
		{
			return false;
		}
	}
};

using FoundationToColumnMoveSet = set<shared_ptr<FoundationToColumnMove>, FoundationToColumnMoveComparator>;

bool operator==(FoundationToColumnMoveSet const& lhs, FoundationToColumnMoveSet const& rhs)
{
	vector<FoundationToColumnMove> lhs_moves{};
	vector<FoundationToColumnMove> rhs_moves{};
	for (shared_ptr<FoundationToColumnMove> ptr : lhs)
	{
		lhs_moves.emplace_back(*ptr);
	}
	for (shared_ptr<FoundationToColumnMove> ptr : rhs)
	{
		rhs_moves.emplace_back(*ptr);
	}
	return lhs_moves == rhs_moves;
}

FoundationToColumnMoveSet ToFoundationToColumnMoveSet(vector<shared_ptr<solitaire_move>> const& vec)
{
	vector<shared_ptr<FoundationToColumnMove>> typed_vec{};
	for (shared_ptr<solitaire_move> move : vec)
	{
		typed_vec.emplace_back(dynamic_pointer_cast<FoundationToColumnMove>(move));
	}
	FoundationToColumnMoveSet s{ typed_vec.begin(), typed_vec.end() };
	return s;
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(FoundationToColumnMove const& move)
{
	return to_wstring(move);
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(FoundationToColumnMoveSet const& moves)
{
	wostringstream out{};
	out << L"{"s;
	if (!moves.empty())
	{
		FoundationToColumnMoveSet::const_iterator citer{ moves.cbegin() };
		FoundationToColumnMoveSet::const_iterator cend{ moves.cend() };
		shared_ptr<FoundationToColumnMove> ptr{ *citer };
		out << *ptr;
		citer++;
		while (citer != cend)
		{
			out << L", "s;
			shared_ptr<FoundationToColumnMove> ptr{ *citer };
			out << *ptr;
			citer++;
		}
	}
	out << L"}"s;
	return out.str();
}

namespace SolitaireMoveTests
{
	TEST_CLASS(FoundationToColumnMoveTests)
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
		TEST_METHOD(TestFindMoves)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::six, suit::club },
						card{ value::five, suit::diamond },
						card{ value::four, suit::club },
					}
				},
				column{
					empty_list_of_cards,
					vector<card>{
						// Please ignore that this card is duplicated.
						card{ value::four, suit::spade },
					}
				},
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
				{
					suit::heart,
					vector<card>{
						card{ value::ace, suit::heart },
						card{ value::two, suit::heart },
						card{ value::three, suit::heart },
					}
				},
				{
					suit::spade,
					vector<card>{
						card{ value::ace, suit::spade },
						card{ value::two, suit::spade },
						card{ value::three, suit::spade },
						card{ value::four, suit::spade },
						card{ value::five, suit::spade },
						card{ value::six, suit::spade },
						card{ value::seven, suit::spade },
						card{ value::eight, suit::spade },
						card{ value::nine, suit::spade },
						card{ value::ten, suit::spade },
						card{ value::jack, suit::spade },
						card{ value::queen, suit::spade },
						card{ value::king, suit::spade },
					}
				},
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, empty_list_of_cards, 0, foundation) };

			vector<shared_ptr<solitaire_move>> moves{ FoundationToColumnMove::find_moves(*board_ptr) };
			FoundationToColumnMoveSet actual{ ToFoundationToColumnMoveSet(moves) };

			FoundationToColumnMoveSet expected{
				make_shared<FoundationToColumnMove>(0, card{ value::king, suit::spade }),
				make_shared<FoundationToColumnMove>(1, card{ value::king, suit::spade }),
				make_shared<FoundationToColumnMove>(2, card{ value::king, suit::spade }),
				make_shared<FoundationToColumnMove>(3, card{ value::three, suit::diamond }),
				make_shared<FoundationToColumnMove>(3, card{ value::three, suit::heart }),
				make_shared<FoundationToColumnMove>(4, card{ value::three, suit::diamond }),
				make_shared<FoundationToColumnMove>(4, card{ value::three, suit::heart }),
				make_shared<FoundationToColumnMove>(5, card{ value::king, suit::spade }),
				make_shared<FoundationToColumnMove>(6, card{ value::king, suit::spade }),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestConstructorEmptyFoundation)
		{
			board new_board{ empty_columns, empty_list_of_cards, 0, empty_foundation };

			Assert::ExpectException<out_of_range>([new_board]()
				{
					FoundationToColumnMove move{ 3, suit::club, new_board };
				});
		}

		TEST_METHOD(TestGetDestinationColumnIndex)
		{
			FoundationToColumnMove move{ 5, card{ value::ace, suit::club } };

			Assert::AreEqual(size_t{ 5 }, move.get_destination_column_index());
		}

		TEST_METHOD(TestHasCard)
		{
			FoundationToColumnMove move{ 5, card{ value::ace, suit::club } };

			Assert::IsTrue(move.has_cards());
		}

		TEST_METHOD(TestGetCards)
		{
			FoundationToColumnMove move{ 5, card{ value::ace, suit::club } };

			vector<card> expected{
				card{ value::ace, suit::club },
			};
			Assert::AreEqual(expected, move.get_cards());
		}

		TEST_METHOD(TestApply)
		{
			map<suit, vector<card>> foundation{
				{
					suit::club,
					vector<card>{
						card{ value::ace, suit::club },
						card{ value::two, suit::club },
						card{ value::three, suit::club },
						card{ value::four, suit::club },
						card{ value::five, suit::club },
						card{ value::six, suit::club },
						card{ value::seven, suit::club },
						card{ value::eight, suit::club },
						card{ value::nine, suit::club },
						card{ value::ten, suit::club },
						card{ value::jack, suit::club },
						card{ value::queen, suit::club },
						card{ value::king, suit::club },
					}
				},
				{ suit::diamond, empty_list_of_cards },
				{ suit::heart, empty_list_of_cards },
				{ suit::spade, empty_list_of_cards },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, empty_list_of_cards, 0, foundation) };

			FoundationToColumnMove move{ 3, suit::club, board_ptr };

			shared_ptr<board> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::king, suit::club },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			map<suit, vector<card>> expected_foundation{
				{
					suit::club,
					vector<card>{
						card{ value::ace, suit::club },
						card{ value::two, suit::club },
						card{ value::three, suit::club },
						card{ value::four, suit::club },
						card{ value::five, suit::club },
						card{ value::six, suit::club },
						card{ value::seven, suit::club },
						card{ value::eight, suit::club },
						card{ value::nine, suit::club },
						card{ value::ten, suit::club },
						card{ value::jack, suit::club },
						card{ value::queen, suit::club },
					}
				},
				{ suit::diamond, empty_list_of_cards },
				{ suit::heart, empty_list_of_cards },
				{ suit::spade, empty_list_of_cards },
			};
			board expected{ expected_columns, empty_list_of_cards, 0, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyEmpty)
		{
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, empty_list_of_cards, 0, empty_foundation) };

			FoundationToColumnMove move{ 3, card{ value::ace, suit::club } };

			Assert::ExpectException<out_of_range>([board_ptr, move]()
				{
					shared_ptr<board> actual{ move.apply(board_ptr) };
				});
		}

		TEST_METHOD(TestApplyNonEmptyColumn)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					vector<card>{
						card{ value::four, suit::spade },
					},
					vector<card>{
						card{ value::six, suit::heart },
						card{ value::five, suit::spade },
						card{ value::four, suit::heart },
					}
				},
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

			FoundationToColumnMove move{ 3, suit::club, board_ptr };

			shared_ptr<board> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					vector<card>{
						card{ value::four, suit::spade },
					},
					vector<card>{
						card{ value::six, suit::heart },
						card{ value::five, suit::spade },
						card{ value::four, suit::heart },
						card{ value::three, suit::club },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			map<suit, vector<card>> expected_foundation{
				{
					suit::club,
					vector<card>{
						card{ value::ace, suit::club },
						card{ value::two, suit::club },
					}
				},
				{ suit::diamond, empty_list_of_cards },
				{ suit::heart, empty_list_of_cards },
				{ suit::spade, empty_list_of_cards },
			};
			board expected{ expected_columns, empty_list_of_cards, 0, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyKeepsStockPile)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					vector<card>{
						card{ value::four, suit::spade },
					},
					vector<card>{
						card{ value::six, suit::heart },
						card{ value::five, suit::spade },
						card{ value::four, suit::heart },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> stock_pile{
				card{ value::jack, suit::club },
				card{ value::two, suit::diamond },
				card{ value::six, suit::heart },
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
			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, 0, foundation) };

			FoundationToColumnMove move{ 3, suit::club, board_ptr };

			shared_ptr<board> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					vector<card>{
						card{ value::four, suit::spade },
					},
					vector<card>{
						card{ value::six, suit::heart },
						card{ value::five, suit::spade },
						card{ value::four, suit::heart },
						card{ value::three, suit::club },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			map<suit, vector<card>> expected_foundation{
				{
					suit::club,
					vector<card>{
						card{ value::ace, suit::club },
						card{ value::two, suit::club },
					}
				},
				{ suit::diamond, empty_list_of_cards },
				{ suit::heart, empty_list_of_cards },
				{ suit::spade, empty_list_of_cards },
			};
			board expected{ expected_columns, stock_pile, 0, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyKeepsStockPileIndex)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					vector<card>{
						card{ value::four, suit::spade },
					},
					vector<card>{
						card{ value::six, suit::heart },
						card{ value::five, suit::spade },
						card{ value::four, suit::heart },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> stock_pile{
				card{ value::jack, suit::club },
				card{ value::two, suit::diamond },
				card{ value::six, suit::heart },
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
			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, 2, foundation) };

			FoundationToColumnMove move{ 3, suit::club, board_ptr };

			shared_ptr<board> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					vector<card>{
						card{ value::four, suit::spade },
					},
					vector<card>{
						card{ value::six, suit::heart },
						card{ value::five, suit::spade },
						card{ value::four, suit::heart },
						card{ value::three, suit::club },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			map<suit, vector<card>> expected_foundation{
				{
					suit::club,
					vector<card>{
						card{ value::ace, suit::club },
						card{ value::two, suit::club },
					}
				},
				{ suit::diamond, empty_list_of_cards },
				{ suit::heart, empty_list_of_cards },
				{ suit::spade, empty_list_of_cards },
			};
			board expected{ expected_columns, stock_pile, 2, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestEquals)
		{
			FoundationToColumnMove move1{ 5, card{ value::ace, suit::club } };
			FoundationToColumnMove move2{ 5, card{ value::ace, suit::club } };

			Assert::AreEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentDestinationColumn)
		{
			FoundationToColumnMove move1{ 5, card{ value::ace, suit::club } };
			FoundationToColumnMove move2{ 4, card{ value::ace, suit::club } };

			Assert::AreNotEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentCard)
		{
			FoundationToColumnMove move1{ 5, card{ value::ace, suit::club } };
			FoundationToColumnMove move2{ 5, card{ value::ace, suit::heart } };

			Assert::AreNotEqual(move1, move2);
		}
	};
}
