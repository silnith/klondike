#include "TicTacToe.h"

using namespace silnith::game;
using namespace std;

bool isWinForPlayer(TicTacToePlayer player, TicTacToeBoard const& board);

class GameLostFilter :
    public move_filter<TicTacToeMove, TicTacToeBoard>
{
public:
    GameLostFilter(void) = default;
    GameLostFilter(GameLostFilter const&) = default;
    GameLostFilter& operator=(GameLostFilter const&) = default;
    GameLostFilter(GameLostFilter&&) noexcept = default;
    GameLostFilter& operator=(GameLostFilter&&) noexcept = default;
    virtual ~GameLostFilter(void) = default;

    [[nodiscard]]
    virtual string get_statistics_key() const override
    {
        using namespace std::literals::string_literals;
        return "Game Lost"s;
    }

    [[nodiscard]]
    virtual bool should_filter(shared_ptr<linked_node<game_state<TicTacToeMove, TicTacToeBoard>> const> const& game_state_history) const override
    {
        game_state<TicTacToeMove, TicTacToeBoard> const& game_state{ game_state_history->get_value() };
        shared_ptr<TicTacToeBoard const> const& board_ptr{ game_state.get_board() };
        return isWinForPlayer(TicTacToePlayer::O, *board_ptr);
    }

private:
};

TicTacToe::TicTacToe(void) : filters{ make_shared<GameLostFilter>(), }
{}

bool isWinForPlayer(TicTacToePlayer player, TicTacToeBoard const& board)
{
    array<array<TicTacToePlayer, 3>, 3> b{ board.getBoard() };
    if (b[0][0] == player
        && b[1][1] == player
        && b[2][2] == player)
    {
        return true;
    }
    if (b[0][2] == player
        && b[1][1] == player
        && b[2][0] == player)
    {
        return true;
    }
    for (int i{ 0 }; i < 3; i++)
    {
        if (b[0][i] == player
            && b[1][i] == player
            && b[2][i] == player)
        {
            return true;
        }
        if (b[i][0] == player
            && b[i][1] == player
            && b[i][2] == player)
        {
            return true;
        }
    }
    return false;
}

bool TicTacToe::is_win(shared_ptr<linked_node<game_state<TicTacToeMove, TicTacToeBoard>> const> const& node) const
{
    game_state<TicTacToeMove, TicTacToeBoard> const& game_state{ node->get_value() };
    shared_ptr<TicTacToeBoard const> const& board{ game_state.get_board() };
    return isWinForPlayer(TicTacToePlayer::X, *board);
}

vector<shared_ptr<TicTacToeMove const>> TicTacToe::find_all_moves(
    shared_ptr<linked_node<game_state<TicTacToeMove, TicTacToeBoard>> const> const& game_state_history) const
{
    vector<shared_ptr<TicTacToeMove const>> moves{};

    game_state<TicTacToeMove, TicTacToeBoard> const& game_state{ game_state_history->get_value() };
    shared_ptr<TicTacToeBoard const> const& board{ game_state.get_board() };
    array<array<TicTacToePlayer, 3>, 3> const& b{ board->getBoard() };

    for (int row{ 0 }; row < 3; row++)
    {
        for (int column{ 0 }; column < 3; column++)
        {
            if (b[row][column] == TicTacToePlayer::nobody)
            {
                moves.emplace_back(make_shared<TicTacToeMove>(row, column, board->getPlayer()));
            }
        }
    }
    return moves;
}

span<shared_ptr<move_filter<TicTacToeMove, TicTacToeBoard> const> const> TicTacToe::get_filters(void) const
{
    return filters;
}
