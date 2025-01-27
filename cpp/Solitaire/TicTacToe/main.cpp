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

int main()
{
    std::shared_ptr<TicTacToe> game{ std::make_shared<TicTacToe>() };
    std::shared_ptr<TicTacToeMove> initial_move{ std::make_shared<TicTacToeMove>(0, 0, TicTacToePlayer::nobody) };
    std::shared_ptr<TicTacToeBoard> initial_board{ std::make_shared<TicTacToeBoard>(TicTacToePlayer::X) };
    game_state<TicTacToeMove, TicTacToeBoard> initial_game_state{ initial_move, initial_board };
    printTo(std::cout, *initial_board);
    sequential_depth_first_search<TicTacToeMove, TicTacToeBoard> searcher{ game, initial_game_state };
    std::cout << "Hello World!" << std::endl;
    std::list<std::shared_ptr<linked_node<game_state<TicTacToeMove, TicTacToeBoard>>>> results{ searcher.search() };
    std::cout << results.size() << std::endl;
    for (std::shared_ptr<linked_node<game_state<TicTacToeMove, TicTacToeBoard>>> result : results)
    {
        game_state<TicTacToeMove, TicTacToeBoard> game_state{ result->get_value() };
        //printTo(std::cout, *(game_state.get_board()));
    }
    std::cout << "Done!" << std::endl;
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
