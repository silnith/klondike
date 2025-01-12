#include <silnith/game/deck/value.h>

using namespace silnith::game::deck;

int silnith::game::deck::get_value(value const value)
{
    return static_cast<int>(value);
}

std::string silnith::game::deck::to_string(value const value)
{
	using namespace std::literals::string_literals;
	switch (value)
	{
	case value::ace:
		return "ace"s;
	case value::two:
		return "two"s;
	case value::three:
		return "three"s;
	case value::four:
		return "four"s;
	case value::five:
		return "five"s;
	case value::six:
		return "six"s;
	case value::seven:
		return "seven"s;
	case value::eight:
		return "eight"s;
	case value::nine:
		return "nine"s;
	case value::ten:
		return "ten"s;
	case value::jack:
		return "jack"s;
	case value::queen:
		return "queen"s;
	case value::king:
		return "king"s;
	default:
		return std::to_string(static_cast<int>(value));
	}
}

std::wstring silnith::game::deck::to_wstring(value const value)
{
	using namespace std::literals::string_literals;
	switch (value)
	{
	case value::ace:
		return L"ace"s;
	case value::two:
		return L"two"s;
	case value::three:
		return L"three"s;
	case value::four:
		return L"four"s;
	case value::five:
		return L"five"s;
	case value::six:
		return L"six"s;
	case value::seven:
		return L"seven"s;
	case value::eight:
		return L"eight"s;
	case value::nine:
		return L"nine"s;
	case value::ten:
		return L"ten"s;
	case value::jack:
		return L"jack"s;
	case value::queen:
		return L"queen"s;
	case value::king:
		return L"king"s;
	default:
		return std::to_wstring(static_cast<int>(value));
	}
}

std::ostream& silnith::game::deck::operator<<(std::ostream& out, value const value)
{
	out << to_string(value);
	return out;
}

std::wostream& silnith::game::deck::operator<<(std::wostream& out, value const value)
{
	out << to_wstring(value);
	return out;
}
