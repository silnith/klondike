# Klondike solitaire solver

This is a program to solve a game of Klondike solitaire, given an initial deck of cards.
There is no graphical interface (yet).  It is not a playable game.  Instead, it is a tool
to search the game tree for possible solutions.

This repository contains the exact same solver program implemented in multiple languages.
The structure and algorithms are identical across the implementations in different languages.
Any differences in performance are strictly due to how each language translates source code
into machine code for execution.

The problem domain of searching a game tree is highly suited for garbage collection.  There
is very high memory turnover, objects are short-lived but do not necessarily get freed in
a predictable order, and memory fragmentation is a real danger.
