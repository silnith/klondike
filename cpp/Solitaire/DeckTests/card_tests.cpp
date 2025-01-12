#include "CppUnitTest.h"

#include <silnith/game/deck/card.h>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game::deck;

using namespace std::literals::string_literals;

template<>
std::wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(value const& value)
{
	return to_wstring(value);
}

template<>
std::wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(suit const& suit)
{
	return to_wstring(suit);
}

template<>
std::wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(card const& card)
{
	return to_wstring(card);
}

namespace DeckTests
{
	TEST_CLASS(CardTests)
	{
	public:
		TEST_METHOD(TestGetValue)
		{
			card card{ value::eight, suit::heart };

			Assert::AreEqual(value::eight, card.get_value());
		}

		TEST_METHOD(TestGetSuit)
		{
			card card{ value::eight, suit::heart };

			Assert::AreEqual(suit::heart, card.get_suit());
		}

		TEST_METHOD(TestEquals)
		{
			card card1{ value::eight, suit::heart };
			card card2{ value::eight, suit::heart };

			Assert::AreEqual(card1, card2);
		}

		TEST_METHOD(TestEqualsDifferentValue)
		{
			card card1{ value::eight, suit::heart };
			card card2{ value::seven, suit::heart };

			Assert::AreNotEqual(card1, card2);
		}

		TEST_METHOD(TestEqualsDifferentSuit)
		{
			card card1{ value::eight, suit::heart };
			card card2{ value::eight, suit::diamond };

			Assert::AreNotEqual(card1, card2);
		}

		TEST_METHOD(TestEqualsDifferentValueAndSuit)
		{
			card card1{ value::eight, suit::heart };
			card card2{ value::seven, suit::diamond };

			Assert::AreNotEqual(card1, card2);
		}

		TEST_METHOD(TestToString)
		{
			card card{ value::ace, suit::spade };

			Assert::AreEqual("ace of spades"s, to_string(card));
		}
	};
}
