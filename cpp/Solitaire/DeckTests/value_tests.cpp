#include "CppUnitTest.h"

#include <silnith/game/deck/value.h>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game::deck;

using namespace std::literals::string_literals;

template<>
std::wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(value const& value)
{
	return to_wstring(value);
}

namespace DeckTests
{
	TEST_CLASS(ValueTests)
	{
	public:
		TEST_METHOD(TestGetValueAce)
		{
			Assert::AreEqual(1, get_value(value::ace));
		}

		TEST_METHOD(TestGetValueTwo)
		{
			Assert::AreEqual(2, get_value(value::two));
		}

		TEST_METHOD(TestGetValueThree)
		{
			Assert::AreEqual(3, get_value(value::three));
		}

		TEST_METHOD(TestGetValueFour)
		{
			Assert::AreEqual(4, get_value(value::four));
		}

		TEST_METHOD(TestGetValueFive)
		{
			Assert::AreEqual(5, get_value(value::five));
		}

		TEST_METHOD(TestGetValueSix)
		{
			Assert::AreEqual(6, get_value(value::six));
		}

		TEST_METHOD(TestGetValueSeven)
		{
			Assert::AreEqual(7, get_value(value::seven));
		}

		TEST_METHOD(TestGetValueEight)
		{
			Assert::AreEqual(8, get_value(value::eight));
		}

		TEST_METHOD(TestGetValueNine)
		{
			Assert::AreEqual(9, get_value(value::nine));
		}

		TEST_METHOD(TestGetValueTen)
		{
			Assert::AreEqual(10, get_value(value::ten));
		}

		TEST_METHOD(TestGetValueJack)
		{
			Assert::AreEqual(11, get_value(value::jack));
		}

		TEST_METHOD(TestGetValueQueen)
		{
			Assert::AreEqual(12, get_value(value::queen));
		}

		TEST_METHOD(TestGetValueKing)
		{
			Assert::AreEqual(13, get_value(value::king));
		}

		TEST_METHOD(TestToStringAce)
		{
			Assert::AreEqual("ace"s, to_string(value::ace));
		}

		TEST_METHOD(TestToStringTwo)
		{
			Assert::AreEqual("two"s, to_string(value::two));
		}

		TEST_METHOD(TestToStringThree)
		{
			Assert::AreEqual("three"s, to_string(value::three));
		}

		TEST_METHOD(TestToStringFour)
		{
			Assert::AreEqual("four"s, to_string(value::four));
		}

		TEST_METHOD(TestToStringFive)
		{
			Assert::AreEqual("five"s, to_string(value::five));
		}

		TEST_METHOD(TestToStringSix)
		{
			Assert::AreEqual("six"s, to_string(value::six));
		}

		TEST_METHOD(TestToStringSeven)
		{
			Assert::AreEqual("seven"s, to_string(value::seven));
		}

		TEST_METHOD(TestToStringEight)
		{
			Assert::AreEqual("eight"s, to_string(value::eight));
		}

		TEST_METHOD(TestToStringNine)
		{
			Assert::AreEqual("nine"s, to_string(value::nine));
		}

		TEST_METHOD(TestToStringTen)
		{
			Assert::AreEqual("ten"s, to_string(value::ten));
		}

		TEST_METHOD(TestToStringJack)
		{
			Assert::AreEqual("jack"s, to_string(value::jack));
		}

		TEST_METHOD(TestToStringQueen)
		{
			Assert::AreEqual("queen"s, to_string(value::queen));
		}

		TEST_METHOD(TestToStringKing)
		{
			Assert::AreEqual("king"s, to_string(value::king));
		}

		TEST_METHOD(TestToStringInvalidValue)
		{
			Assert::AreEqual("14"s, to_string(static_cast<value>(14)));
		}

		TEST_METHOD(TestToWStringAce)
		{
			Assert::AreEqual(L"ace"s, to_wstring(value::ace));
		}

		TEST_METHOD(TestToWStringTwo)
		{
			Assert::AreEqual(L"two"s, to_wstring(value::two));
		}

		TEST_METHOD(TestToWStringThree)
		{
			Assert::AreEqual(L"three"s, to_wstring(value::three));
		}

		TEST_METHOD(TestToWStringFour)
		{
			Assert::AreEqual(L"four"s, to_wstring(value::four));
		}

		TEST_METHOD(TestToWStringFive)
		{
			Assert::AreEqual(L"five"s, to_wstring(value::five));
		}

		TEST_METHOD(TestToWStringSix)
		{
			Assert::AreEqual(L"six"s, to_wstring(value::six));
		}

		TEST_METHOD(TestToWStringSeven)
		{
			Assert::AreEqual(L"seven"s, to_wstring(value::seven));
		}

		TEST_METHOD(TestToWStringEight)
		{
			Assert::AreEqual(L"eight"s, to_wstring(value::eight));
		}

		TEST_METHOD(TestToWStringNine)
		{
			Assert::AreEqual(L"nine"s, to_wstring(value::nine));
		}

		TEST_METHOD(TestToWStringTen)
		{
			Assert::AreEqual(L"ten"s, to_wstring(value::ten));
		}

		TEST_METHOD(TestToWStringJack)
		{
			Assert::AreEqual(L"jack"s, to_wstring(value::jack));
		}

		TEST_METHOD(TestToWStringQueen)
		{
			Assert::AreEqual(L"queen"s, to_wstring(value::queen));
		}

		TEST_METHOD(TestToWStringKing)
		{
			Assert::AreEqual(L"king"s, to_wstring(value::king));
		}

		TEST_METHOD(TestToWStringInvalidValue)
		{
			Assert::AreEqual(L"14"s, to_wstring(static_cast<value>(14)));
		}
	};
}
