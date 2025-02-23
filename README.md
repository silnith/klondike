# Klondike solitaire solver

This is a program to solve a game of Klondike solitaire, given an initial deck
of cards.  There is no graphical interface (yet).  It is not a playable game.
Instead, it is a tool to search the game tree for possible solutions.

This repository contains the exact same solver program implemented in multiple
languages.  The structure and algorithms are identical across the implementations
in different languages.  Any differences in performance are strictly due to how
each language translates source code into machine code for execution.

The problem domain of searching a game tree is highly suited for garbage
collection.  There is very high memory turnover, objects are short-lived but do
not necessarily get freed in a predictable order, and memory fragmentation is a
real danger.

## Performance

The original implementation was in Java.  The C# version was initially for the
purpose of learning the language.  As a side-effect, it also ended up
demonstrating an order-of-magnitude performance difference between the two
language platforms (JVM and CLR).  This was over a decade ago.  I resurrected
the project recently for a few reasons, and updated the .NET version to work on
.NET Core instead of .NET Framework.  I expected some performance improvement,
but the exact same performance disparity still existed even after a decade of
.NET improvements.

Even more recently I became curious about whether a C++ implementation was
feasible, and how manual memory management would perform against garbage
collection.  Standard shared pointers turned out to be sufficient for safely
allocating and releasing memory, and so far no test runs have failed due to
memory fragmentation.

### Spoilers

If you are curious what the exact performance difference between the
implementations is, I encourage you to clone the repository and run the code
yourself on your own machine.  For those just looking for spoilers, or who do
not have Visual Studio and a decent Java IDE installed, following is what I
observed on my machine.  Each one is running the game tree search on a single
dedicated thread, with statistics captured in atomicly-updated counters that
are displayed in a separate thread.

The C++ version currently can generate and evaluate about 150,000 boards per second
on a single CPU, sometimes getting up to 180,000 boards per second on good game
trees.  (This is for a Release build, Debug never broke 9,000.)

The C# version varies between 130,000 and 300,000 boards per second, depending
on the specific game tree it is searching.  A couple times I saw it reach as
high as 400,000 boards per second.  (Again, Release build.  Debug build capped
out at 160,000 and sometimes fell to 80,000.)  Most runs were in the 150,000
to 200,000 range.

The Java version ranges between 750,000 and 1,000,000 boards per second,
sometimes rising as high as 1,300,000 boards per second on good game trees.
(There is no such thing as a "Release" build for Java, all builds are Debug
builds.  When I set a conditional breakpoint it would fall to 200 boards per
second, when I disabled the breakpoint it was back up to 800,000 and higher.)
