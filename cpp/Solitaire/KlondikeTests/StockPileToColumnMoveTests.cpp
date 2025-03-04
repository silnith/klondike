#include "CppUnitTest.h"

#include "BoardToString.h"
#include "CardToString.h"
#include "CardComparator.h"

#include <silnith/game/solitaire/move/StockPileToColumnMove.h>

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

struct StockPileToColumnMoveComparator
{
public:
	bool operator()(shared_ptr<StockPileToColumnMove> const& lhs, shared_ptr<StockPileToColumnMove> const& rhs) const
	{
		if (lhs->get_source_index() < rhs->get_source_index())
		{
			return true;
		}
		else if (lhs->get_source_index() == rhs->get_source_index())
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
		else
		{
			return false;
		}
	}
};

using StockPileToColumnMoveSet = set<shared_ptr<StockPileToColumnMove>, StockPileToColumnMoveComparator>;

bool operator==(StockPileToColumnMoveSet const& lhs, StockPileToColumnMoveSet const& rhs)
{
	vector<StockPileToColumnMove> lhs_moves{};
	vector<StockPileToColumnMove> rhs_moves{};
	for (shared_ptr<StockPileToColumnMove> ptr : lhs)
	{
		lhs_moves.emplace_back(*ptr);
	}
	for (shared_ptr<StockPileToColumnMove> ptr : rhs)
	{
		rhs_moves.emplace_back(*ptr);
	}
	return lhs_moves == rhs_moves;
}

StockPileToColumnMoveSet ToStockPileRecycleMoveSet(vector<shared_ptr<solitaire_move>> const& vec)
{
	vector<shared_ptr<StockPileToColumnMove>> typed_vec{};
	for (shared_ptr<solitaire_move> move : vec)
	{
		typed_vec.emplace_back(dynamic_pointer_cast<StockPileToColumnMove>(move));
	}
	StockPileToColumnMoveSet s{ typed_vec.begin(), typed_vec.end() };
	return s;
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(StockPileToColumnMove const& move)
{
	return to_wstring(move);
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(StockPileToColumnMoveSet const& moves)
{
	wostringstream out{};
	out << L"{"s;
	if (!moves.empty())
	{
		StockPileToColumnMoveSet::const_iterator citer{ moves.cbegin() };
		StockPileToColumnMoveSet::const_iterator cend{ moves.cend() };
		shared_ptr<StockPileToColumnMove> ptr{ *citer };
		out << *ptr;
		citer++;
		while (citer != cend)
		{
			out << L", "s;
			shared_ptr<StockPileToColumnMove> ptr{ *citer };
			out << *ptr;
			citer++;
		}
	}
	out << L"}"s;
	return out.str();
}

namespace SolitaireMoveTests
{
	TEST_CLASS(StockPileToColumnMoveTests)
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
		TEST_METHOD(TestFindMovesEmptyBoard)
		{
			board new_board{ empty_columns, empty_list_of_cards, 0, empty_foundation };

			vector<shared_ptr<solitaire_move>> moves{ StockPileToColumnMove::find_moves(new_board) };
			StockPileToColumnMoveSet actual{ ToStockPileRecycleMoveSet(moves) };

			StockPileToColumnMoveSet expected{};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMovesKingToEmptyBoard)
		{
			vector<card> stock_pile{
				card{ value::king, suit::club },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, stock_pile, 1, empty_foundation) };

			vector<shared_ptr<solitaire_move>> moves{ StockPileToColumnMove::find_moves(*board_ptr) };
			StockPileToColumnMoveSet actual{ ToStockPileRecycleMoveSet(moves) };

			StockPileToColumnMoveSet expected{
				make_shared<StockPileToColumnMove>(1, 0, card{ value::king, suit::club }),
				make_shared<StockPileToColumnMove>(1, 1, card{ value::king, suit::club }),
				make_shared<StockPileToColumnMove>(1, 2, card{ value::king, suit::club }),
				make_shared<StockPileToColumnMove>(1, 3, card{ value::king, suit::club }),
				make_shared<StockPileToColumnMove>(1, 4, card{ value::king, suit::club }),
				make_shared<StockPileToColumnMove>(1, 5, card{ value::king, suit::club }),
				make_shared<StockPileToColumnMove>(1, 6, card{ value::king, suit::club }),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestFindMoves)
		{
			vector<column> columns{
				column{
					empty_list_of_cards,
					empty_list_of_cards
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::eight, suit::club },
					}
				},
				column{
					empty_list_of_cards,
					empty_list_of_cards
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::nine, suit::club },
						card{ value::eight, suit::diamond },
					}
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::ten, suit::heart },
						card{ value::nine, suit::spade },
						card{ value::eight, suit::heart },
					}
				},
				column{
					empty_list_of_cards,
					empty_list_of_cards
				},
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::six, suit::diamond },
						card{ value::five, suit::club },
					}
				},
			};

			vector<card> stock_pile{
				card{ value::four, suit::diamond },
				card{ value::seven, suit::club },
				card{ value::three, suit::diamond },
			};

			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, 2, empty_foundation) };

			vector<shared_ptr<solitaire_move>> moves{ StockPileToColumnMove::find_moves(*board_ptr) };
			StockPileToColumnMoveSet actual{ ToStockPileRecycleMoveSet(moves) };

			StockPileToColumnMoveSet expected{
				make_shared<StockPileToColumnMove>(2, 3, card{ value::seven, suit::club }),
				make_shared<StockPileToColumnMove>(2, 4, card{ value::seven, suit::club }),
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestGetSourceIndex)
		{
			StockPileToColumnMove move{ 2, 5, card{ value::ace, suit::club } };

			Assert::AreEqual(size_t{ 2 }, move.get_source_index());
		}

		TEST_METHOD(TestConstructorGetSourceIndex)
		{
			vector<card> stock_pile{
				card{ value::ace, suit::club },
				card{ value::two, suit::heart },
				card{ value::three, suit::spade },
			};
			board b{ empty_columns, stock_pile, 2, empty_foundation };

			StockPileToColumnMove move{ 5, b };

			Assert::AreEqual(size_t{ 2 }, move.get_source_index());
		}

		TEST_METHOD(TestGetDestinationColumnIndex)
		{
			StockPileToColumnMove move{ 2, 5, card{ value::ace, suit::club } };

			Assert::AreEqual(size_t{ 5 }, move.get_destination_column_index());
		}

		TEST_METHOD(TestHasCard)
		{
			StockPileToColumnMove move{ 2, 5, card{ value::ace, suit::club } };

			Assert::IsTrue(move.has_cards());
		}

		TEST_METHOD(TestGetCards)
		{
			StockPileToColumnMove move{ 2, 5, card{ value::ace, suit::club } };

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

			StockPileToColumnMove move{ 5, b };

			vector<card> expected{
				card{ value::two, suit::heart },
			};
			Assert::AreEqual(expected, move.get_cards());
		}

		TEST_METHOD(TestApply)
		{
			vector<card> stock_pile{
				card{ value::king, suit::club },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, stock_pile, 1, empty_foundation) };

			StockPileToColumnMove move{ 4, *board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::king, suit::club },
					},
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			board expected{ expected_columns, empty_list_of_cards, 0, empty_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyEmpty)
		{
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, empty_list_of_cards, 0, empty_foundation) };

			StockPileToColumnMove move{ 0, 4, card{ value::ace, suit::club } };

			Assert::ExpectException<out_of_range>([move, board_ptr]()
				{
					shared_ptr<board const> actual{ move.apply(board_ptr) };
				});
		}

		TEST_METHOD(TestApplyUnderflow)
		{
			vector<card> stock_pile{
				card{ value::king, suit::club },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, stock_pile, 0, empty_foundation) };

			StockPileToColumnMove move{ 0, 4, card{ value::king, suit::club } };

			Assert::ExpectException<out_of_range>([move, board_ptr]()
				{
					shared_ptr<board const> actual{ move.apply(board_ptr) };
				});
		}

		TEST_METHOD(TestApplyNonEmpty)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::eight, suit::spade },
						card{ value::seven, suit::heart },
						card{ value::six, suit::club },
						card{ value::five, suit::diamond },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> stock_pile{
				card{ value::four, suit::spade },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, 1, empty_foundation) };

			StockPileToColumnMove move{ 4, board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::eight, suit::spade },
						card{ value::seven, suit::heart },
						card{ value::six, suit::club },
						card{ value::five, suit::diamond },
						card{ value::four, suit::spade },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			board expected{ expected_columns, empty_list_of_cards, 0, empty_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyFromBeginningNonEmpty)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::eight, suit::spade },
						card{ value::seven, suit::heart },
						card{ value::six, suit::club },
						card{ value::five, suit::diamond },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> stock_pile{
				card{ value::four, suit::spade },
				card{ value::six, suit::spade },
				card{ value::jack, suit::club },
				card{ value::queen, suit::club },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, 1, empty_foundation) };

			StockPileToColumnMove move{ 4, board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::eight, suit::spade },
						card{ value::seven, suit::heart },
						card{ value::six, suit::club },
						card{ value::five, suit::diamond },
						card{ value::four, suit::spade },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> expected_stock_pile{
				card{ value::six, suit::spade },
				card{ value::jack, suit::club },
				card{ value::queen, suit::club },
			};
			board expected{ expected_columns, expected_stock_pile, 0, empty_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyFromMiddleNonEmpty)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::eight, suit::spade },
						card{ value::seven, suit::heart },
						card{ value::six, suit::club },
						card{ value::five, suit::diamond },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> stock_pile{
				card{ value::six, suit::spade },
				card{ value::four, suit::spade },
				card{ value::jack, suit::club },
				card{ value::queen, suit::club },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, 2, empty_foundation) };

			StockPileToColumnMove move{ 4, board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::eight, suit::spade },
						card{ value::seven, suit::heart },
						card{ value::six, suit::club },
						card{ value::five, suit::diamond },
						card{ value::four, suit::spade },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> expected_stock_pile{
				card{ value::six, suit::spade },
				card{ value::jack, suit::club },
				card{ value::queen, suit::club },
			};
			board expected{ expected_columns, expected_stock_pile, 1, empty_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyFromEndNonEmpty)
		{
			vector<column> columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::eight, suit::spade },
						card{ value::seven, suit::heart },
						card{ value::six, suit::club },
						card{ value::five, suit::diamond },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> stock_pile{
				card{ value::six, suit::spade },
				card{ value::jack, suit::club },
				card{ value::queen, suit::club },
				card{ value::four, suit::spade },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, 4, empty_foundation) };

			StockPileToColumnMove move{ 4, board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::eight, suit::spade },
						card{ value::seven, suit::heart },
						card{ value::six, suit::club },
						card{ value::five, suit::diamond },
						card{ value::four, suit::spade },
					}
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			vector<card> expected_stock_pile{
				card{ value::six, suit::spade },
				card{ value::jack, suit::club },
				card{ value::queen, suit::club },
			};
			board expected{ expected_columns, expected_stock_pile, 3, empty_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestApplyKeepsFoundation)
		{
			vector<card> stock_pile{
				card{ value::king, suit::club },
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
				{ suit::spade, empty_list_of_cards },
			};
			shared_ptr<board> board_ptr{ make_shared<board>(empty_columns, stock_pile, 1, foundation) };

			StockPileToColumnMove move{ 4, *board_ptr };

			shared_ptr<board const> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
				column{
					empty_list_of_cards,
					vector<card>{
						card{ value::king, suit::club },
					},
				},
				column{ empty_list_of_cards, empty_list_of_cards },
				column{ empty_list_of_cards, empty_list_of_cards },
			};
			board expected{ expected_columns, empty_list_of_cards, 0, foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestEquals)
		{
			StockPileToColumnMove move1{ 2, 5, card{ value::ace, suit::club } };
			StockPileToColumnMove move2{ 2, 5, card{ value::ace, suit::club } };

			Assert::AreEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentSourceIndex)
		{
			StockPileToColumnMove move1{ 2, 5, card{ value::ace, suit::club } };
			StockPileToColumnMove move2{ 1, 5, card{ value::ace, suit::club } };

			Assert::AreNotEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentDestinationColumn)
		{
			StockPileToColumnMove move1{ 2, 5, card{ value::ace, suit::club } };
			StockPileToColumnMove move2{ 2, 4, card{ value::ace, suit::club } };

			Assert::AreNotEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentCard)
		{
			StockPileToColumnMove move1{ 2, 5, card{ value::ace, suit::club } };
			StockPileToColumnMove move2{ 2, 5, card{ value::ace, suit::heart } };

			Assert::AreNotEqual(move1, move2);
		}
	};
}
