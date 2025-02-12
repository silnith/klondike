#include <silnith/game/solitaire/move/ColumnToColumnMove.h>

#include <sstream>

using namespace silnith::game::deck;

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move
{
    vector<shared_ptr<solitaire_move>> ColumnToColumnMove::find_moves(board const& board)
    {
        vector<shared_ptr<solitaire_move>> moves{};
        for (size_t source_index{ 0 }; source_index < board::num_columns; source_index++)
        {
            column source_column{ board.get_column(source_index) };
            if (!source_column.has_face_up_cards())
            {
                continue;
            }
            vector<card> source_run{ source_column.get_face_up_cards() };
            size_t source_run_length{ source_run.size() };
            card source_top_card{ source_run.back() };
            card source_bottom_card{ source_run.front() };
            int source_run_min_value{ get_value(source_top_card.get_value()) };
            int source_run_max_value{ get_value(source_bottom_card.get_value()) };

            for (size_t destination_index{ 0 }; destination_index < board::num_columns; destination_index++)
            {
                if (source_index == destination_index)
                {
                    // Cannot move from a column to itself.
                    continue;
                }
                column destination_column{ board.get_column(destination_index) };

                if (destination_column.has_face_up_cards())
                {
                    // Destination has cards already.
                    card destination_top_card{ destination_column.get_top_card() };
                    int run_start_value{ get_value(destination_top_card.get_value()) - 1 };

                    if (source_run_min_value <= run_start_value && source_run_max_value >= run_start_value)
                    {
                        int run_length{ run_start_value - source_run_min_value + 1 };

                        vector<card> run{ source_column.get_top_cards(run_length) };
                        if (get_color(destination_top_card.get_suit()) != get_color(run.front().get_suit()))
                        {
                            moves.emplace_back(
                                static_cast<shared_ptr<solitaire_move>>(
                                    make_shared<ColumnToColumnMove>(source_index, destination_index, run)));
                            //moves.emplace_back(make_shared<ColumnToColumnMove>(source_index, destination_index, run));
                        }
                    }
                }
                else
                {
                    // Destination is empty, only a king may be moved to it.
                    if (source_bottom_card.get_value() == value::king)
                    {
                        moves.emplace_back(
                            static_cast<shared_ptr<solitaire_move>>(
                                make_shared<ColumnToColumnMove>(source_index, destination_index, source_run)));
                    }
                }
            }
        }
        return moves;
    }

    ColumnToColumnMove::ColumnToColumnMove(size_t source_column_index,
        size_t destination_column_index,
        span<card const> const& cards)
        : source_column_index{ source_column_index },
        destination_column_index{ destination_column_index },
        cards{ cards.begin(), cards.end() }
    {
        if (source_column_index < 0)
        {
            throw out_of_range("Source column index must be non-negative."s);
        }
        if (source_column_index >= board::num_columns)
        {
            throw out_of_range("Source column index is outside of board."s);
        }
        if (destination_column_index < 0)
        {
            throw out_of_range("Destination column index must be non-negative."s);
        }
        if (destination_column_index >= board::num_columns)
        {
            throw out_of_range("Destination column index is outside of board."s);
        }
        if (source_column_index == destination_column_index)
        {
            throw invalid_argument("SOurce and destination column are the same."s);
        }
    }

    ColumnToColumnMove::ColumnToColumnMove(size_t source_column_index,
        size_t destination_column_index,
        size_t number_of_cards,
        board const& board)
        : ColumnToColumnMove(source_column_index, destination_column_index, board.get_column(source_column_index).get_top_cards(number_of_cards))
    {}

    ColumnToColumnMove::ColumnToColumnMove(size_t source_column_index,
        size_t destination_column_index,
        size_t number_of_cards,
        shared_ptr<board> const& board)
        : ColumnToColumnMove(source_column_index, destination_column_index, number_of_cards, *board)
    {}

    bool ColumnToColumnMove::has_cards(void) const
    {
        return true;
    }

    vector<card> ColumnToColumnMove::get_cards(void) const
    {
        return cards;
    }

    bool ColumnToColumnMove::is_stock_pile_modification(void) const
    {
        return false;
    }

    bool ColumnToColumnMove::is_from_stock_pile(void) const
    {
        return false;
    }

    bool ColumnToColumnMove::is_from_foundation(void) const
    {
        return false;
    }

    bool ColumnToColumnMove::is_from_column(void) const
    {
        return true;
    }

    bool ColumnToColumnMove::is_from_column(size_t column_index) const
    {
        return column_index == source_column_index;
    }

    size_t ColumnToColumnMove::get_source_column_index(void) const
    {
        return source_column_index;
    }

    bool ColumnToColumnMove::is_to_foundation(void) const
    {
        return false;
    }

    bool ColumnToColumnMove::is_to_column(void) const
    {
        return true;
    }

    bool ColumnToColumnMove::is_to_column(size_t column_index) const
    {
        return column_index == destination_column_index;
    }

    size_t ColumnToColumnMove::get_destination_column_index(void) const
    {
        return destination_column_index;
    }

    shared_ptr<board> ColumnToColumnMove::apply(shared_ptr<board> const& b) const
    {
        vector<column> columns{ b->get_columns() };
        vector<card> stock_pile{ b->get_stock_pile() };
        size_t stock_pile_index{ b->get_stock_pile_index() };
        map<suit, vector<card>> foundation{ b->get_foundation() };

        column from_column{ columns.at(source_column_index) };
        column to_column{ columns.at(destination_column_index) };
        pair<vector<card>, column> pair{ from_column.extract_run(cards.size()) };
        vector<card> run{ pair.first };
        column new_from_column{ pair.second };
        column new_to_column{ to_column.with_cards(run) };

        vector<column> new_columns{};
        for (size_t index{ 0 }; index < board::num_columns; index++)
        {
            if (index == source_column_index)
            {
                new_columns.emplace_back(new_from_column);
            }
            else if (index == destination_column_index)
            {
                new_columns.emplace_back(new_to_column);
            }
            else
            {
                new_columns.emplace_back(columns.at(index));
            }
        }
        return make_shared<board>(
            new_columns,
            stock_pile,
            stock_pile_index,
            foundation);
    }

    bool ColumnToColumnMove::operator==(ColumnToColumnMove const& other) const
    {
        return source_column_index == other.source_column_index
            && destination_column_index == other.destination_column_index
            && cards == other.cards;
    }

    string to_string(ColumnToColumnMove const& move)
    {
        vector<card> run{ move.get_cards() };
        if (run.size() == 1)
        {
            return "Move "s
                + to_string(run.front())
                + " from column "s
                + std::to_string(move.get_source_column_index())
                + " to "s
                + std::to_string(move.get_destination_column_index())
                + "."s;
        }
        else
        {
            ostringstream out{};
            out << run;
            return "Move run "s
                + out.str()
                + " from column "s
                + std::to_string(move.get_source_column_index())
                + " to "s
                + std::to_string(move.get_destination_column_index())
                + "."s;
        }
    }

    wstring to_wstring(ColumnToColumnMove const& move)
    {
        vector<card> run{ move.get_cards() };
        if (run.size() == 1)
        {
            return L"Move "s
                + to_wstring(run.front())
                + L" from column "s
                + std::to_wstring(move.get_source_column_index())
                + L" to "s
                + std::to_wstring(move.get_destination_column_index())
                + L"."s;
        }
        else
        {
            wostringstream out{};
            out << run;
            return L"Move run "s
                + out.str()
                + L" from column "s
                + std::to_wstring(move.get_source_column_index())
                + L" to "s
                + std::to_wstring(move.get_destination_column_index())
                + L"."s;
        }
    }

    ostream& operator<<(ostream& out, ColumnToColumnMove const& move)
    {
        out << to_string(move);
        return out;
    }

    wostream& operator<<(wostream& out, ColumnToColumnMove const& move)
    {
        out << to_wstring(move);
        return out;
    }
}
