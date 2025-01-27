#include "TicTacToeBoard.h"

#include <string>

TicTacToeBoard::TicTacToeBoard(TicTacToePlayer const& p)
    : _player{ p }, _board{}
{}

TicTacToeBoard::TicTacToeBoard(TicTacToePlayer const& p, std::array<std::array<TicTacToePlayer, 3>, 3> const& b)
    : _player{ p }, _board{ b }
{}

TicTacToePlayer TicTacToeBoard::getPlayer(void) const
{
    return _player;
}

std::array<std::array<TicTacToePlayer, 3>, 3> TicTacToeBoard::getBoard(void) const
{
    return _board;
}

std::string to_char(TicTacToePlayer const& player)
{
    using namespace std::literals::string_literals;
    switch (player)
    {
    case TicTacToePlayer::nobody:
        return " "s;
    case TicTacToePlayer::X:
        return "X"s;
    case TicTacToePlayer::O:
        return "O"s;
    default:
        return std::to_string((int) player);
    }
}

std::wstring to_wchar(TicTacToePlayer const& player)
{
    using namespace std::literals::string_literals;
    switch (player)
    {
    case TicTacToePlayer::nobody:
        return L" "s;
    case TicTacToePlayer::X:
        return L"X"s;
    case TicTacToePlayer::O:
        return L"O"s;
    default:
        return std::to_wstring((int)player);
    }
}

void printTo(std::ostream& out, TicTacToeBoard const& board)
{
    using namespace std::literals::string_literals;
    std::array<std::array<TicTacToePlayer, 3>, 3> const& b{ board.getBoard() };
    out << " "s << to_char(b[0][0]) << " | "s << to_char(b[0][1]) << " | "s << to_char(b[0][2]) << " "s << std::endl;
    out << "---|---|---"s << std::endl;
    out << " "s << to_char(b[1][0]) << " | "s << to_char(b[1][1]) << " | "s << to_char(b[1][2]) << " "s << std::endl;
    out << "---|---|---"s << std::endl;
    out << " "s << to_char(b[2][0]) << " | "s << to_char(b[2][1]) << " | "s << to_char(b[2][2]) << " "s << std::endl;
}

void printTo(std::wostream& out, TicTacToeBoard const& board)
{
    using namespace std::literals::string_literals;
    std::array<std::array<TicTacToePlayer, 3>, 3> const& b{ board.getBoard() };
    out << L" "s << to_wchar(b[0][0]) << L" \u2551 "s << to_wchar(b[0][1]) << L" \u2551 "s << to_wchar(b[0][2]) << L" "s << std::endl;
    out << L"\u2550\u2550\u2550\u256c\u2550\u2550\u2550\u256c\u2550\u2550\u2550"s << std::endl;
    out << L" "s << to_wchar(b[1][0]) << L" \u2551 "s << to_wchar(b[1][1]) << L" \u2551 "s << to_wchar(b[1][2]) << L" "s << std::endl;
    out << L"\u2550\u2550\u2550\u256c\u2550\u2550\u2550\u256c\u2550\u2550\u2550"s << std::endl;
    out << L" "s << to_wchar(b[2][0]) << L" \u2551 "s << to_wchar(b[2][1]) << L" \u2551 "s << to_wchar(b[2][2]) << L" "s << std::endl;
}
