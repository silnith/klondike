// Runner.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <silnith/game/deck/card.h>
#include <silnith/game/deck/suit.h>
#include <silnith/game/deck/value.h>

#include <silnith/game/game_state.h>

#include <silnith/game/solitaire/board.h>
#include <silnith/game/solitaire/klondike.h>

#include <silnith/game/solitaire/move/DealMove.h>

#include <forward_list>
#include <iostream>
#include <memory>
#include <random>
#include <vector>
#include <list>

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

int main()
{
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
    for (suit s : suits)
    {
        for (value v : values)
        {
            cards.emplace_back(card{ v, s });
        }
    }
    vector<card> deck{ shuffle(cards) };

    klondike game{};
    shared_ptr<solitaire_move> deal_move{ make_shared<DealMove>(deck) };
    shared_ptr<board> dealt_board{ deal_move->apply(nullptr) };
    game_state<solitaire_move, board> initial_game_state{ deal_move, dealt_board };

    dealt_board->print_to(std::cout);

    std::cout << "Hello World!\n";
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
