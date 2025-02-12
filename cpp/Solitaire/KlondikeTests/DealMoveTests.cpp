#include "CppUnitTest.h"

#include "BoardToString.h"
#include "CardToString.h"
#include "CardComparator.h"

#include <silnith/game/solitaire/move/DealMove.h>

#include <memory>
#include <set>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game::deck;
using namespace silnith::game::solitaire;
using namespace silnith::game::solitaire::move;

using namespace std;
using namespace std::literals::string_literals;

struct DealMoveComparator
{
public:
	bool operator()(shared_ptr<DealMove> const& lhs, shared_ptr<DealMove> const& rhs) const
	{
		return lhs->get_cards() < rhs->get_cards();
	}
};

using DealMoveSet = set<shared_ptr<DealMove>, DealMoveComparator>;

bool operator==(DealMoveSet const& lhs, DealMoveSet const& rhs)
{
	vector<DealMove> lhs_moves{};
	vector<DealMove> rhs_moves{};
	for (shared_ptr<DealMove> ptr : lhs)
	{
		lhs_moves.emplace_back(*ptr);
	}
	for (shared_ptr<DealMove> ptr : rhs)
	{
		rhs_moves.emplace_back(*ptr);
	}
	return lhs_moves == rhs_moves;
}

DealMoveSet ToDealMoveSet(vector<shared_ptr<solitaire_move>> const& vec)
{
	vector<shared_ptr<DealMove>> typed_vec{};
	for (shared_ptr<solitaire_move> move : vec)
	{
		typed_vec.emplace_back(dynamic_pointer_cast<DealMove>(move));
	}
	DealMoveSet s{ typed_vec.begin(), typed_vec.end() };
	return s;
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(DealMove const& move)
{
	return to_wstring(move);
}

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(DealMoveSet const& moves)
{
	wostringstream out{};
	out << L"{"s;
	if (!moves.empty())
	{
		DealMoveSet::const_iterator citer{ moves.cbegin() };
		DealMoveSet::const_iterator cend{ moves.cend() };
		shared_ptr<DealMove> ptr{ *citer };
		out << *ptr;
		citer++;
		while (citer != cend)
		{
			out << L", "s;
			shared_ptr<DealMove> ptr{ *citer };
			out << *ptr;
			citer++;
		}
	}
	out << L"}"s;
	return out.str();
}

namespace SolitaireMoveTests
{
	TEST_CLASS(DealMoveTests)
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

		vector<card> deck{
			card{ value::seven, suit::heart },
			card{ value::eight, suit::diamond },
			card{ value::nine, suit::club },
			card{ value::king, suit::club },
			card{ value::king, suit::diamond },
			card{ value::king, suit::heart },
			card{ value::king, suit::spade },
			card{ value::seven, suit::diamond },
			card{ value::eight, suit::club },
			card{ value::queen, suit::club },
			card{ value::queen, suit::diamond },
			card{ value::queen, suit::heart },
			card{ value::queen, suit::spade },
			card{ value::seven, suit::club },
			card{ value::jack, suit::club },
			card{ value::jack, suit::diamond },
			card{ value::jack, suit::heart },
			card{ value::jack, suit::spade },
			card{ value::ten, suit::club },
			card{ value::ten, suit::diamond },
			card{ value::ten, suit::heart },
			card{ value::ten, suit::spade },
			card{ value::nine, suit::diamond },
			card{ value::nine, suit::heart },
			card{ value::nine, suit::spade },
			card{ value::eight, suit::heart },
			card{ value::eight, suit::spade },
			card{ value::seven, suit::spade },
			card{ value::six, suit::club },
			card{ value::six, suit::diamond },
			card{ value::six, suit::heart },
			card{ value::six, suit::spade },
			card{ value::five, suit::club },
			card{ value::five, suit::diamond },
			card{ value::five, suit::heart },
			card{ value::five, suit::spade },
			card{ value::four, suit::club },
			card{ value::four, suit::diamond },
			card{ value::four, suit::heart },
			card{ value::four, suit::spade },
			card{ value::three, suit::club },
			card{ value::three, suit::diamond },
			card{ value::three, suit::heart },
			card{ value::three, suit::spade },
			card{ value::two, suit::club },
			card{ value::two, suit::diamond },
			card{ value::two, suit::heart },
			card{ value::two, suit::spade },
			card{ value::ace, suit::club },
			card{ value::ace, suit::diamond },
			card{ value::ace, suit::heart },
			card{ value::ace, suit::spade },
		};

	public:
		TEST_METHOD(TestConstructorDeckTooSmall)
		{
			Assert::ExpectException<out_of_range>([]()
				{
					DealMove move{ vector<card>{} };
				});
		}

		TEST_METHOD(TestHasCards)
		{
			DealMove move(deck);

			Assert::IsTrue(move.has_cards());
		}

		TEST_METHOD(TestGetCards)
		{
			DealMove move(deck);

			Assert::AreEqual(deck, move.get_cards());
		}

		TEST_METHOD(TestApply)
		{
			vector<column> columns{};
			vector<card> stock_pile{};
			size_t stock_pile_index{ 0 };
			map<suit, vector<card>> foundation{};
			shared_ptr<board> board_ptr{ make_shared<board>(columns, stock_pile, stock_pile_index, foundation) };

			DealMove move{ deck };

			shared_ptr<board> actual{ move.apply(board_ptr) };

			vector<column> expected_columns{
				column{
					vector<card>{
					},
					vector<card>{
						card{ value::seven, suit::heart },
					},
				},
				column{
					vector<card>{
						card{ value::eight, suit::diamond },
					},
					vector<card>{
						card{ value::seven, suit::diamond },
					},
				},
				column{
					vector<card>{
						card{ value::nine, suit::club },
						card{ value::eight, suit::club },
					},
					vector<card>{
						card{ value::seven, suit::club },
					},
				},
				column{
					vector<card>{
						card{ value::king, suit::club },
						card{ value::queen, suit::club },
						card{ value::jack, suit::club },
					},
					vector<card>{
						card{ value::ten, suit::club },
					},
				},
				column{
					vector<card>{
						card{ value::king, suit::diamond },
						card{ value::queen, suit::diamond },
						card{ value::jack, suit::diamond },
						card{ value::ten, suit::diamond },
					},
					vector<card>{
						card{ value::nine, suit::diamond },
					},
				},
				column{
					vector<card>{
						card{ value::king, suit::heart },
						card{ value::queen, suit::heart },
						card{ value::jack, suit::heart },
						card{ value::ten, suit::heart },
						card{ value::nine, suit::heart },
					},
					vector<card>{
						card{ value::eight, suit::heart },
					},
				},
				column{
					vector<card>{
						card{ value::king, suit::spade },
						card{ value::queen, suit::spade },
						card{ value::jack, suit::spade },
						card{ value::ten, suit::spade },
						card{ value::nine, suit::spade },
						card{ value::eight, suit::spade },
					},
					vector<card>{
						card{ value::seven, suit::spade },
					},
				},
			};
			vector<card> expected_stock_pile{
				card{ value::six, suit::club },
				card{ value::six, suit::diamond },
				card{ value::six, suit::heart },
				card{ value::six, suit::spade },
				card{ value::five, suit::club },
				card{ value::five, suit::diamond },
				card{ value::five, suit::heart },
				card{ value::five, suit::spade },
				card{ value::four, suit::club },
				card{ value::four, suit::diamond },
				card{ value::four, suit::heart },
				card{ value::four, suit::spade },
				card{ value::three, suit::club },
				card{ value::three, suit::diamond },
				card{ value::three, suit::heart },
				card{ value::three, suit::spade },
				card{ value::two, suit::club },
				card{ value::two, suit::diamond },
				card{ value::two, suit::heart },
				card{ value::two, suit::spade },
				card{ value::ace, suit::club },
				card{ value::ace, suit::diamond },
				card{ value::ace, suit::heart },
				card{ value::ace, suit::spade },
			};
			map<suit, vector<card>> expected_foundation{
				{ suit::club, vector<card>{} },
				{ suit::diamond, vector<card>{} },
				{ suit::heart, vector<card>{} },
				{ suit::spade, vector<card>{} },
			};
			board expected{ expected_columns, expected_stock_pile, 0, expected_foundation };
			Assert::AreEqual(expected, *actual);
		}

		TEST_METHOD(TestEquals)
		{
			DealMove move1{ deck };
			DealMove move2{ deck };

			Assert::AreEqual(move1, move2);
		}

		TEST_METHOD(TestEqualsDifferentDeck)
		{
			span<card const> deck_span{ deck };
			size_t half_size{ deck.size() / 2 };
			span<card const> first_half{ deck_span.subspan(0, half_size) };
			span<card const> second_half{ deck_span.subspan(half_size, deck.size() - half_size) };

			vector<card> other_deck{ second_half.begin(), second_half.end() };
			for (card c : first_half)
			{
				other_deck.emplace_back(c);
			}
			DealMove move1{ deck };
			DealMove move2{ other_deck };

			Assert::AreNotEqual(move1, move2);
		}
	};
}
