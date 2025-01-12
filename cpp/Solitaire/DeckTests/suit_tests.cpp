#include "CppUnitTest.h"

#include <silnith/game/deck/suit.h>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game::deck;

using namespace std::literals::string_literals;

template<>
std::wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(color const& color)
{
	return to_wstring(color);
}

template<>
std::wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(suit const& suit)
{
	return to_wstring(suit);
}

namespace DeckTests
{
	TEST_CLASS(SuitTests)
	{
	public:

		TEST_METHOD(TestGetColorClub)
		{
			Assert::AreEqual(color::black, get_color(suit::club));
		}

		TEST_METHOD(TestGetColorDiamond)
		{
			Assert::AreEqual(color::red, get_color(suit::diamond));
		}

		TEST_METHOD(TestGetColorHeart)
		{
			Assert::AreEqual(color::red, get_color(suit::heart));
		}

		TEST_METHOD(TestGetColorSpade)
		{
			Assert::AreEqual(color::black, get_color(suit::spade));
		}

		TEST_METHOD(TestToStringClub)
		{
			Assert::AreEqual("club"s, to_string(suit::club));
		}

		TEST_METHOD(TestToStringDiamond)
		{
			Assert::AreEqual("diamond"s, to_string(suit::diamond));
		}

		TEST_METHOD(TestToStringHeart)
		{
			Assert::AreEqual("heart"s, to_string(suit::heart));
		}

		TEST_METHOD(TestToStringSpade)
		{
			Assert::AreEqual("spade"s, to_string(suit::spade));
		}

		TEST_METHOD(TestToStringInvalidValue)
		{
			Assert::AreEqual("17"s, to_string(static_cast<suit>(17)));
		}

		TEST_METHOD(TestToWStringClub)
		{
			Assert::AreEqual(L"\u2663"s, to_wstring(suit::club));
		}

		TEST_METHOD(TestToWStringDiamond)
		{
			Assert::AreEqual(L"\u2666"s, to_wstring(suit::diamond));
		}

		TEST_METHOD(TestToWStringHeart)
		{
			Assert::AreEqual(L"\u2665"s, to_wstring(suit::heart));
		}

		TEST_METHOD(TestToWStringSpade)
		{
			Assert::AreEqual(L"\u2660"s, to_wstring(suit::spade));
		}

		TEST_METHOD(TestToWStringInvalidValue)
		{
			Assert::AreEqual(L"17"s, to_wstring(static_cast<suit>(17)));
		}
	};
}
