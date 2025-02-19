#include <silnith/game/solitaire/column.h>

#include <ranges>
#include <sstream>
#include <stdexcept>

using namespace silnith::game::deck;
using namespace silnith::game::solitaire;

using namespace std;
using namespace std::literals::string_literals;

column::column(span<card const> face_down_cards, span<card const> face_up_cards)
    : face_down{ face_down_cards.begin(), face_down_cards.end() },
    face_up{ face_up_cards.begin(), face_up_cards.end() }
{
    if (!face_down_cards.empty() && face_up_cards.empty())
    {
        face_up.emplace_back(face_down.back());
        face_down.pop_back();
    }
    else
    {
        // Already handled by member initializers.
    }
}

bool column::has_face_down_cards(void) const
{
    return !face_down.empty();
}

bool column::has_face_up_cards(void) const
{
    return !face_up.empty();
}

size_t column::get_number_of_face_down_cards(void) const
{
    return face_down.size();
}

size_t column::get_number_of_face_up_cards(void) const
{
    return face_up.size();
}

vector<card> const& column::get_face_up_cards(void) const
{
    return face_up;
}

card const& column::get_top_card(void) const
{
    if (face_up.empty())
    {
        throw out_of_range("No cards available in the column.");
    }
    return face_up.back();
}

span<card const> column::get_top_cards(size_t number_of_cards) const
{
    if (number_of_cards < 1)
    {
        throw out_of_range("Must take at least one card.");
    }
    if (number_of_cards > face_up.size())
    {
        throw out_of_range("Not enough cards available in the column.");
    }

    span<card const> face_up_span{ face_up };
    return face_up_span.last(number_of_cards);
}

pair<card, column> column::extract_card(void) const
{
    if (face_up.empty())
    {
        throw out_of_range("No cards available in the column.");
    }

    card top_card{ face_up.back() };
    column new_column{ span<card const>{ face_down }, span<card const>{ face_up }.first(face_up.size() - 1) };
    return pair<card, column>{ top_card, new_column };
}

pair<vector<card>, column> column::extract_run(size_t number_of_cards) const
{
    if (number_of_cards < 1)
    {
        throw out_of_range("Must take at least one card.");
    }
    if (number_of_cards > face_up.size())
    {
        throw out_of_range("Not enough cards available in the column.");
    }

    span<card const> face_up_span{ face_up };
    span<card const> run_span{ face_up_span.last(number_of_cards) };
    vector<card> run{ run_span.begin(), run_span.end() };
    column new_column{ span<card const>{ face_down }, face_up_span.first(face_up.size() - number_of_cards) };
    return pair<vector<card>, column>{ run, new_column };
}

column column::with_cards(span<card const> new_cards) const
{
    if (new_cards.empty())
    {
        throw invalid_argument("Run must not be empty.");
    }

    vector<card> new_face_up{ face_up };
    for (card c : new_cards)
    {
        new_face_up.emplace_back(c);
    }
    return column{ face_down, new_face_up };
}

column column::with_card(card new_card) const
{
    vector<card> new_face_up{ face_up };
    new_face_up.emplace_back(new_card);
    return column{ face_down, new_face_up };
}

bool column::can_add_run(span<card const> run) const
{
    if (run.empty())
    {
        throw invalid_argument("Run cannot be empty.");
    }

    card first_card_of_run_to_add{ run.front() };
    if (has_face_up_cards())
    {
        card top_card_of_column{ get_top_card() };
        return get_value(top_card_of_column.get_value()) == 1 + get_value(first_card_of_run_to_add.get_value())
            && top_card_of_column.get_color() != first_card_of_run_to_add.get_color();
    }
    else
    {
        return first_card_of_run_to_add.get_value() == value::king;
    }
}

bool column::operator==(column const& other) const
{
    return face_up == other.face_up && face_down == other.face_down;
}

string silnith::game::solitaire::to_string(column const& column)
{
    ostringstream out{};
    out << "Column (down: ["s;
    if (vector<card>::const_iterator citer{ column.face_down.cbegin() },
        cend{ column.face_down.cend() };
        citer != cend)
    {
        out << *citer;
        citer++;
        while (citer != cend)
        {
            out << ", "s;
            out << *citer;
            citer++;
        }
    }
    out << "], up: ["s;
    if (vector<card>::const_iterator citer{ column.face_up.cbegin() },
        cend{ column.face_up.cend() };
        citer != cend)
    {
        out << *citer;
        citer++;
        while (citer != cend)
        {
            out << ", "s;
            out << *citer;
            citer++;
        }
    }
    out << "])"s;
    return out.str();
}

wstring silnith::game::solitaire::to_wstring(column const& column)
{
    wostringstream out{};
    out << L"Column (down: ["s;
    if (vector<card>::const_iterator citer{ column.face_down.cbegin() },
        cend{ column.face_down.cend() };
        citer != cend)
    {
        out << *citer;
        citer++;
        while (citer != cend)
        {
            out << L", "s;
            out << *citer;
            citer++;
        }
    }
    out << L"], up: ["s;
    if (vector<card>::const_iterator citer{ column.face_up.cbegin() },
        cend{ column.face_up.cend() };
        citer != cend)
    {
        out << *citer;
        citer++;
        while (citer != cend)
        {
            out << L", "s;
            out << *citer;
            citer++;
        }
    }
    out << L"])"s;
    return out.str();
}

std::ostream& silnith::game::solitaire::operator<<(std::ostream& out, column const& column)
{
    out << to_string(column);
    return out;
}

std::wostream& silnith::game::solitaire::operator<<(std::wostream& out, column const& column)
{
    out << to_wstring(column);
    return out;
}
