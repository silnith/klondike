using Silnith.Game.Deck;
using System;
using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move
{
    /// <summary>
    /// A move that takes a card from the foundation and puts it onto a column run.
    /// </summary>
    /// <remarks>
    /// <para>
    /// If the column is empty, only a <see cref="Value.King"/> may be
    /// moved.  If the column is not empty, then the card must adhere to the rules of
    /// a run.
    /// </para>
    /// </remarks>
    public class FoundationToColumnMove : ISolitaireMove, IEquatable<FoundationToColumnMove?>
    {
        /// <summary>
        /// The equivalent of the Java values() provided for enumerations.
        /// </summary>
        private static readonly IEnumerable<Suit> suits = new List<Suit>()
        {
            Suit.Club,
            Suit.Diamond,
            Suit.Heart,
            Suit.Spade,
        };

        /// <summary>
        /// Finds all moves of a card from the foundation to a column run.
        /// </summary>
        /// <remarks>
        /// <para>
        /// </para>
        /// </remarks>
        /// <param name="board">The board to examine.</param>
        /// <returns>An enumerable of moves.</returns>
        public static IEnumerable<FoundationToColumnMove> FindMoves(Board board)
        {
            List<FoundationToColumnMove> moves = new List<FoundationToColumnMove>();
            foreach (Suit suit in suits)
            {
                IReadOnlyList<Card> foundationForSuit = board.Foundation[suit];
                int suitCount = foundationForSuit.Count;
                if (suitCount > 0)
                {
                    Card topOfFoundation = foundationForSuit[suitCount - 1];
                    List<Card> run = new List<Card>()
                    {
                        topOfFoundation,
                    };
                    for (int i = 0; i < board.Columns.Count; i++)
                    {
                        Column column = board.Columns[i];
                        if (column.CanAddRun(run))
                        {
                            moves.Add(new FoundationToColumnMove(i, topOfFoundation));
                        }
                    }
                }
            }
            return moves;
        }

        /// <summary>
        /// The index in the board of the destination column for the card.
        /// </summary>
        public int DestinationColumn
        {
            get;
        }

        /// <summary>
        /// The card being moved.
        /// </summary>
        public Card Card
        {
            get;
        }

        /// <inheritdoc/>
        public bool HasCards
        {
            get
            {
                return true;
            }
        }

        /// <inheritdoc/>
        public IReadOnlyList<Card> Cards
        {
            get;
        }

        /// <summary>
        /// Constructs a move that takes a card from the foundation and puts it on top
        /// of a column.
        /// </summary>
        /// <param name="destinationColumn">The index of the column into the board.</param>
        /// <param name="card">The card being moved.</param>
        public FoundationToColumnMove(int destinationColumn, Card card)
        {
            DestinationColumn = destinationColumn;
            Card = card;
            Cards = new List<Card>()
            {
                card,
            };
        }

        /// <summary>
        /// Constructs a move that takes a card from the foundation and puts it on top
        /// of a column.
        /// </summary>
        /// <param name="destinationColumn">The index of the column into the board.</param>
        /// <param name="suit">The suit of card to pull from the foundation.</param>
        /// <param name="board">The board to get the card from.</param>
        /// <exception cref="ArgumentOutOfRangeException">If the board foundation has no cards for <paramref name="suit"/>.</exception>
        public FoundationToColumnMove(int destinationColumn, Suit suit, Board board) : this(destinationColumn, board.GetTopOfFoundation(suit))
        {
        }

        /// <inheritdoc/>
        public bool AddsCardsToColumn(int column)
        {
            return column == DestinationColumn;
        }

        /// <inheritdoc/>
        public Board Apply(Board board)
        {
            Tuple<Card, IReadOnlyDictionary<Suit, IReadOnlyList<Card>>> tuple = board.ExtractCardFromFoundation(Card.Suit);
            Card card = tuple.Item1;
            IReadOnlyDictionary<Suit, IReadOnlyList<Card>> newFoundation = tuple.Item2;

            IReadOnlyList<Column> newColumns = new List<Column>(board.Columns)
            {
                [DestinationColumn] = board.Columns[DestinationColumn].WithCard(card),
            };

            return new Board(newColumns, board.StockPile, board.StockPileIndex, newFoundation);
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as FoundationToColumnMove);
        }

        /// <inheritdoc/>
        public bool Equals(FoundationToColumnMove? other)
        {
            return other != null
                && DestinationColumn == other.DestinationColumn
                && Card.Equals(other.Card);
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            return HashCode.Combine(DestinationColumn, Card);
        }

        /// <inheritdoc/>
        public override string ToString()
        {
            return $"Move {Card} from foundation to column {DestinationColumn}.";
        }
    }
}
