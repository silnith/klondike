// Runner.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <silnith/game/deck/card.h>
#include <silnith/game/deck/suit.h>
#include <silnith/game/deck/value.h>

#include <silnith/game/game.h>
#include <silnith/game/game_state.h>
#include <silnith/game/linked_node.h>
#include <silnith/game/sequential_depth_first_search.h>

#include <silnith/game/solitaire/board.h>
#include <silnith/game/solitaire/klondike.h>

#include <silnith/game/solitaire/move/DealMove.h>

#include <deque>
#include <forward_list>
#include <future>
#include <iostream>
#include <memory>
#include <random>
#include <vector>
#include <list>

#include <cstdlib>

using namespace silnith::game;
using namespace silnith::game::deck;
using namespace silnith::game::solitaire;
using namespace silnith::game::solitaire::move;

using namespace std;
using namespace std::literals::string_literals;

vector<card> shuffle(span<card const> const& span)
{
    vector<card> shuffled{};

    random_device rd{};
    mt19937 gen{ rd() };

    list<card> copy{ span.begin(), span.end() };
    while (!copy.empty())
    {
        uniform_int_distribution<size_t> distribution(0, copy.size() - 1);
        size_t index{ distribution(gen) };
        list<card>::const_iterator iter{ copy.cbegin() };
        for (size_t i{ 0 }; i < index; i++)
        {
            iter++;
        }
        shuffled.emplace_back(*iter);
        copy.erase(iter);
    }

    return shuffled;
}

void run_search_0(shared_ptr<game<solitaire_move, board>> const& engine, game_state<solitaire_move, board> const& initial_state)
{
    span<shared_ptr<move_filter<solitaire_move, board>> const> filters{ engine->get_filters() };

    deque<shared_ptr<linked_node<game_state<solitaire_move, board>>>> pending_nodes{};
    deque<shared_ptr<linked_node<game_state<solitaire_move, board>>>> wins{};

    pending_nodes.emplace_back(make_shared<linked_node<game_state<solitaire_move, board>>>(initial_state));

    unsigned long long nodes_examined{ 0 };
    unsigned long long game_states_pruned{ 0 };
    while (!pending_nodes.empty())
    {
        shared_ptr<linked_node<game_state<solitaire_move, board>>> game_state_history{ pending_nodes.back() };
        pending_nodes.pop_back();
        nodes_examined++;

        vector<shared_ptr<solitaire_move>> moves{ engine->find_all_moves(game_state_history) };
        shared_ptr<board> const& current_board{ game_state_history->get_value().get_board() };
        current_board->print_to(cout);
        /*
        cout << endl;
        cout << "Scenario: "s << endl;
        vector<shared_ptr<linked_node<game_state<solitaire_move, board>>>> copy{ game_state_history->cbegin(), game_state_history->cend() };
        vector<shared_ptr<linked_node<game_state<solitaire_move, board>>>> history{ copy.crbegin(), copy.crend() };
        for (shared_ptr<linked_node<game_state<solitaire_move, board>>> ptr : history)
        {
            cout << ptr->get_value().get_move() << endl;;
        }
        cout << "Moves made: "s << history.size() << endl;
        cout << "Game states to examine: " << pending_nodes.size() << endl;
        current_board->print_to(cout);
        cout << "Choices: "s << endl;
        for (shared_ptr<solitaire_move> move : moves)
        {
            shared_ptr<board> new_board{ move->apply(current_board) };
            game_state<solitaire_move, board> new_game_state{ move, new_board };
            shared_ptr<linked_node<game_state<solitaire_move, board>>> new_history{ make_shared<linked_node<game_state<solitaire_move, board>>>(new_game_state, game_state_history) };
            // TODO: How to make this polymorphic?
            //cout << *move;
            for (shared_ptr<move_filter<solitaire_move, board>> filter : filters)
            {
                if (filter->should_filter(new_history))
                {
                    cout << " (filtered by "s;
                    cout << filter->get_statistics_key();
                    cout << ")"s;
                }
            }
            cout << endl;
        }
        cout << endl;
        */

        for (shared_ptr<solitaire_move> const& move : moves)
        {
            shared_ptr<board> new_board{ move->apply(current_board) };
            game_state<solitaire_move, board> new_game_state{ move, new_board };
            shared_ptr<linked_node<game_state<solitaire_move, board>>> new_history{ make_shared<linked_node<game_state<solitaire_move, board>>>(new_game_state, game_state_history) };
            
            bool broken{ false };
            for (shared_ptr<move_filter<solitaire_move, board>> const& filter : filters)
            {
                if (filter->should_filter(new_history))
                {
                    game_states_pruned++;
                    broken = true;
                    break;
                }
            }
            if (broken)
            {
                continue;
            }

            if (engine->is_win(new_game_state))
            {
                wins.push_back(new_history);
            }
            else
            {
                pending_nodes.push_back(new_history);
            }
        }

        if (nodes_examined % 10000 == 0)
        {
            cout << "Nodes examined: "s;
            cout << nodes_examined << endl;
            cout << "Nodes pruned: "s;
            cout << game_states_pruned << endl;
            cout << "Queue size: "s;
            cout << pending_nodes.size() << endl;
            cout << "Wins: "s;
            cout << wins.size() << endl;
            cout << endl;
        }
    }

    // print out wins
    for (shared_ptr<linked_node<game_state<solitaire_move, board>>> const& node_ptr : wins)
    {
        // TODO: Print entire history.
        //cout << node_ptr->get_value() << endl;
        node_ptr->get_value().get_board()->print_to(cout);
    }
}

void sequential_dfs(shared_ptr<game<solitaire_move, board>> const& engine, game_state<solitaire_move, board> const& initial_state)
{
    sequential_depth_first_search<solitaire_move, board> searcher{ engine, initial_state };

    future<list<shared_ptr<linked_node<game_state<solitaire_move, board>>>>> future{
        async(launch::async, &sequential_depth_first_search<solitaire_move, board>::search, &searcher)
    };

    long states_examined{ 0 };
    long boards_generated{ 0 };
    while (future.valid())
    {
        searcher.print_statistics(cout);
        long next_states_examined{ searcher.get_number_of_game_states_examined() };
        long next_boards_generated{ searcher.get_boards_generated() };
        long nodes_per_second{ next_states_examined - states_examined };
        long boards_per_second{ next_boards_generated - boards_generated };
        cout << "Nodes per second: "s << nodes_per_second << endl;
        cout << "Boards per second: "s << boards_per_second << endl;
        cout << endl;
        states_examined = next_states_examined;
        boards_generated = next_boards_generated;
        this_thread::sleep_for(1s);
    }

    list<shared_ptr<linked_node<game_state<solitaire_move, board>>>> wins{ future.get() };
    for (shared_ptr<linked_node<game_state<solitaire_move, board>>> const& win : wins)
    {
        ;
    }
    searcher.print_statistics(cout);
}

int main()
{
    system("chcp");
    system("chcp 65001");
    cout.imbue(locale{ "en-US" });

    vector<value> const values{
        value::ace,
        value::two,
        value::three,
        value::four,
        value::five,
        value::six,
        value::seven,
        value::eight,
        value::nine,
        value::ten,
        value::jack,
        value::queen,
        value::king,
    };
    vector<suit> const suits{
        suit::club,
        suit::diamond,
        suit::heart,
        suit::spade,
    };
    vector<card> cards{};
    for (suit const& s : suits)
    {
        for (value const& v : values)
        {
            cards.emplace_back(card{ v, s });
        }
    }
    vector<card> deck{ shuffle(cards) };

    shared_ptr<game<solitaire_move, board>> engine{ make_shared<klondike>() };
    shared_ptr<solitaire_move> deal_move{ make_shared<DealMove>(deck) };
    shared_ptr<board> dealt_board{ deal_move->apply(nullptr) };
    game_state<solitaire_move, board> initial_game_state{ deal_move, dealt_board };

    dealt_board->print_to(std::cout);

    //run_search_0(engine, initial_game_state);
    sequential_dfs(engine, initial_game_state);
}

// Run program: Ctrl + F5 or Debug > Start Without Debugging menu
// Debug program: F5 or Debug > Start Debugging menu

// Tips for Getting Started: 
//   1. Use the Solution Explorer window to add/manage files
//   2. Use the Team Explorer window to connect to source control
//   3. Use the Output window to see build output and other messages
//   4. Use the Error List window to view errors
//   5. Go to Project > Add New Item to create new code files, or Project > Add Existing Item to add existing code files to the project
//   6. In the future, to open this project again, go to File > Open > Project and select the .sln file
