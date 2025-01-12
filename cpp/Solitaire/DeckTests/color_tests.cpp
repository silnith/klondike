#include "CppUnitTest.h"

#include <silnith/game/deck/color.h>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game::deck;

using namespace std::literals::string_literals;

template<>
std::wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(color const& color)
{
	return to_wstring(color);
}

namespace DeckTests
{
	TEST_CLASS(ColorTests)
	{
	public:
		TEST_METHOD(TestToStringBlack)
		{
			Assert::AreEqual("black"s, to_string(color::black));
		}

		TEST_METHOD(TestToStringRed)
		{
			Assert::AreEqual("red"s, to_string(color::red));
		}

		TEST_METHOD(TestToStringInvalidValue)
		{
			Assert::AreEqual("5"s, to_string(static_cast<color>(5)));
		}

		TEST_METHOD(TestToWStringBlack)
		{
			Assert::AreEqual(L"black"s, to_wstring(color::black));
		}

		TEST_METHOD(TestToWStringRed)
		{
			Assert::AreEqual(L"red"s, to_wstring(color::red));
		}

		TEST_METHOD(TestToWStringInvalidValue)
		{
			Assert::AreEqual(L"5"s, to_wstring(static_cast<color>(5)));
		}
	};
}
