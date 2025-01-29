// TicTacToe.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include <memory>

#include <silnith/game/game_state.h>
#include <silnith/game/sequential_depth_first_search.h>

#include "TicTacToe.h"
#include "TicTacToeBoard.h"
#include "TicTacToeMove.h"
#include "TicTacToePlayer.h"

using namespace silnith::game;
using namespace std;

int main()
{
    cout.imbue(locale{ "en_US" });
    shared_ptr<TicTacToe> game{ make_shared<TicTacToe>() };
    shared_ptr<TicTacToeMove> initial_move{ make_shared<TicTacToeMove>(0, 0, TicTacToePlayer::nobody) };
    shared_ptr<TicTacToeBoard> initial_board{ make_shared<TicTacToeBoard>(TicTacToePlayer::X) };
    game_state<TicTacToeMove, TicTacToeBoard> initial_game_state{ initial_move, initial_board };
    printTo(cout, *initial_board);
    sequential_depth_first_search<TicTacToeMove, TicTacToeBoard> searcher{ game, initial_game_state };
    cout << "Hello World!" << endl;
    list<shared_ptr<linked_node<game_state<TicTacToeMove, TicTacToeBoard>>>> results{ searcher.search() };
    cout << results.size() << endl;
    searcher.print_statistics(std::cout);
    for (shared_ptr<linked_node<game_state<TicTacToeMove, TicTacToeBoard>>> result : results)
    {
        game_state<TicTacToeMove, TicTacToeBoard> game_state{ result->get_value() };
        //printTo(cout, *(game_state.get_board()));
    }
    cout << "Done!" << endl;
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
