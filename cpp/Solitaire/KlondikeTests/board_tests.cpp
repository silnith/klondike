#include "CppUnitTest.h"

#include "BoardToString.h"
#include "CardToString.h"

#include <silnith/game/solitaire/board.h>

#include <memory>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game::deck;
using namespace silnith::game::solitaire;

using namespace std;
using namespace std::literals::string_literals;

namespace SolitaireTests
{
	TEST_CLASS(BoardTests)
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
		map<suit, std::vector<card>> empty_foundation{
			{ suit::club, empty_list_of_cards },
			{ suit::diamond, empty_list_of_cards },
			{ suit::heart, empty_list_of_cards },
			{ suit::spade, empty_list_of_cards },
		};

	public:
		TEST_METHOD(TestConstructorWithVectorsAndMap)
		{
			vector<column> columns{ empty_columns };
			vector<card> stock_pile{ empty_list_of_cards };
			size_t stock_pile_index{ 0 };
			map<suit, vector<card>> foundation{ empty_foundation };

			board new_board{ columns, stock_pile, stock_pile_index, foundation };
		}

		TEST_METHOD(TestConstructorWithSpansAndMap)
		{
			span<column const> columns{ empty_columns };
			span<card const> stock_pile{ empty_list_of_cards };
			size_t stock_pile_index{ 0 };
			map<suit, vector<card>> foundation{ empty_foundation };

			board new_board{ columns, stock_pile, stock_pile_index, foundation };
		}

		TEST_METHOD(TestCanAddToFoundationEmptyAce)
		{
			board new_board{ empty_columns, empty_list_of_cards, 0, empty_foundation };

			Assert::IsTrue(new_board.can_add_to_foundation(card{ value::ace, suit::club }));
		}

		TEST_METHOD(TestCanAddToFoundationEmptyTwo)
		{
			board new_board{ empty_columns, empty_list_of_cards, 0, empty_foundation };

			Assert::IsFalse(new_board.can_add_to_foundation(card{ value::two, suit::club }));
		}

		TEST_METHOD(TestCanAddToFoundationEmptyKing)
		{
			board new_board{ empty_columns, empty_list_of_cards, 0, empty_foundation };

			Assert::IsFalse(new_board.can_add_to_foundation(card{ value::king, suit::club }));
		}

		TEST_METHOD(TestCanAddToFoundationLessThan)
		{
			map<suit, vector<card>> foundation{
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
			board new_board{ empty_columns, empty_list_of_cards, 0, foundation };

			Assert::IsFalse(new_board.can_add_to_foundation(card{ value::three, suit::club }));
		}

		TEST_METHOD(TestCanAddToFoundationEquals)
		{
			map<suit, vector<card>> foundation{
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
			board new_board{ empty_columns, empty_list_of_cards, 0, foundation };

			Assert::IsFalse(new_board.can_add_to_foundation(card{ value::four, suit::club }));
		}

		TEST_METHOD(TestCanAddToFoundationValid)
		{
			map<suit, vector<card>> foundation{
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
			board new_board{ empty_columns, empty_list_of_cards, 0, foundation };

			Assert::IsTrue(new_board.can_add_to_foundation(card{ value::five, suit::club }));
		}

		TEST_METHOD(TestCanAddToFoundationGreaterThan)
		{
			map<suit, vector<card>> foundation{
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
			board new_board{ empty_columns, empty_list_of_cards, 0, foundation };

			Assert::IsFalse(new_board.can_add_to_foundation(card{ value::six, suit::club }));
		}

		TEST_METHOD(TestEqualsEmpty)
		{
			board board1{ empty_columns, empty_list_of_cards, 0, empty_foundation };
			board board2{ empty_columns, empty_list_of_cards, 0, empty_foundation };

			Assert::AreEqual(board1, board2);
		}

		TEST_METHOD(TestEqualsOneStack)
		{
			vector<card> stack{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond },
				card{ value::jack, suit::spade },
				card{ value::ten, suit::heart },
			};
			vector<column> columns{ empty_columns };
			columns[6] = column{ empty_list_of_cards, stack };
			board board1{ columns, empty_list_of_cards, 0, empty_foundation };
			board board2{ columns, empty_list_of_cards, 0, empty_foundation };

			Assert::AreEqual(board1, board2);
		}

		TEST_METHOD(TestEqualsTwoStacks)
		{
			vector<card> stack1{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond },
				card{ value::jack, suit::spade },
				card{ value::ten, suit::heart },
			};
			vector<card> stack2{
				card{ value::two, suit::spade },
				card{ value::five, suit::diamond },
			};

			vector<column> columns1{ empty_columns };
			columns1[2] = column{ stack2, empty_list_of_cards };
			columns1[6] = column{ empty_list_of_cards, stack1 };

			vector<column> columns2{ empty_columns };
			columns2[2] = column{ std::span{ stack2 }.first(1), std::span{ stack2 }.last(1) };
			columns2[6] = column{ empty_list_of_cards, stack1 };

			board board1{ columns1, empty_list_of_cards, 0, empty_foundation };
			board board2{ columns2, empty_list_of_cards, 0, empty_foundation };

			Assert::AreEqual(board1, board2);
		}

		TEST_METHOD(TestEqualsTwoStacksDifferentColumns)
		{
			vector<card> stack1{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond },
				card{ value::jack, suit::spade },
				card{ value::ten, suit::heart },
			};
			vector<card> stack2{
				card{ value::two, suit::spade },
				card{ value::five, suit::diamond },
			};

			vector<column> columns1{ empty_columns };
			columns1[2] = column{ stack2, empty_list_of_cards };
			columns1[6] = column{ empty_list_of_cards, stack1 };

			vector<column> columns2{ empty_columns };
			columns2[3] = column{ std::span{ stack2 }.first(1), std::span{ stack2 }.last(1) };
			columns2[6] = column{ empty_list_of_cards, stack1 };

			board board1{ columns1, empty_list_of_cards, 0, empty_foundation };
			board board2{ columns2, empty_list_of_cards, 0, empty_foundation };

			Assert::AreNotEqual(board1, board2);
		}

		TEST_METHOD(TestEqualsStockPile)
		{
			vector<card> stock_pile{
				card{ value::two, suit::spade },
				card{ value::three, suit::spade },
				card{ value::eight, suit::heart },
				card{ value::eight, suit::club },
				card{ value::eight, suit::diamond },
				card{ value::king, suit::diamond },
				card{ value::queen, suit::club },
				card{ value::ace, suit::heart },
			};

			board board1{ empty_columns, stock_pile, 0, empty_foundation };
			board board2{ empty_columns, stock_pile, 0, empty_foundation };

			Assert::AreEqual(board1, board2);
		}

		TEST_METHOD(TestNotEqualsStockPile)
		{
			vector<card> stock_pile1{
				card{ value::two, suit::spade },
				card{ value::three, suit::spade },
				card{ value::eight, suit::heart },
				card{ value::eight, suit::club },
				card{ value::eight, suit::diamond },
				card{ value::king, suit::diamond },
				card{ value::queen, suit::club },
				card{ value::ace, suit::heart },
			};
			vector<card> stock_pile2{
				card{ value::two, suit::spade },
				card{ value::three, suit::spade },
				card{ value::eight, suit::club },
				card{ value::eight, suit::diamond },
				card{ value::king, suit::diamond },
				card{ value::queen, suit::club },
				card{ value::ace, suit::heart },
			};

			board board1{ empty_columns, stock_pile1, 0, empty_foundation };
			board board2{ empty_columns, stock_pile2, 0, empty_foundation };

			Assert::AreNotEqual(board1, board2);
		}

		TEST_METHOD(TestEqualsStockPileIndex)
		{
			vector<card> stock_pile{
				card{ value::two, suit::spade },
				card{ value::three, suit::spade },
				card{ value::eight, suit::heart },
				card{ value::eight, suit::club },
				card{ value::eight, suit::diamond },
				card{ value::king, suit::diamond },
				card{ value::queen, suit::club },
				card{ value::ace, suit::heart },
			};

			board board1{ empty_columns, stock_pile, 8, empty_foundation };
			board board2{ empty_columns, stock_pile, 8, empty_foundation };

			Assert::AreEqual(board1, board2);
		}

		TEST_METHOD(TestEqualsFoundation)
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
					},
				},
				{
					suit::diamond,
					vector<card>{
						card{ value::ace, suit::diamond },
					},
				},
				{
					suit::heart,
					vector<card>{
						card{ value::ace, suit::heart },
						card{ value::two, suit::heart },
						card{ value::three, suit::heart },
						card{ value::four, suit::heart },
					},
				},
				{
					suit::spade,
					empty_list_of_cards,
				},
			};

			board board1{ empty_columns, empty_list_of_cards, 0, foundation };
			board board2{ empty_columns, empty_list_of_cards, 0, foundation };

			Assert::AreEqual(board1, board2);
		}

		TEST_METHOD(TestNotEqualsFoundation)
		{
			map<suit, vector<card>> foundation1{
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
					},
				},
				{
					suit::diamond,
					vector<card>{
						card{ value::ace, suit::diamond },
					},
				},
				{
					suit::heart,
					vector<card>{
						card{ value::ace, suit::heart },
						card{ value::two, suit::heart },
						card{ value::three, suit::heart },
						card{ value::four, suit::heart },
					},
				},
				{
					suit::spade,
					empty_list_of_cards,
				},
			};
			map<suit, vector<card>> foundation2{ foundation1 };
			foundation2[suit::diamond] = std::vector<card>{
				card{ value::ace, suit::diamond },
				card{ value::two, suit::diamond },
			};

			board board1{ empty_columns, empty_list_of_cards, 0, foundation1 };
			board board2{ empty_columns, empty_list_of_cards, 0, foundation2 };

			Assert::AreNotEqual(board1, board2);
		}
	};
}
