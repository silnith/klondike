﻿using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike
{
    /// <summary>
    /// A column on the solitaire board.
    /// </summary>
    /// <remarks>
    /// <para>
    /// A column consists of a number of face-down cards and a number of face-up
    /// cards.  The face-down cards are initialized when the board is first dealt.
    /// When there are no face-up cards, the top face-down card is flipped to face up.
    /// If there are no cards remaining, the column is emplty and any <see cref="Value.King"/>
    /// may be moved to it.
    /// </para>
    /// <para>
    /// Face-up cards in a column must obey the rules of a run.  Cards may be
    /// stacked on top of other face-up cards to make a run provided that a card is
    /// only placed on top of a card of the opposite color and of one value higher.
    /// For example, a <see cref="Value.Two"/> of <see cref="Suit.Club"/>
    /// may be placed on top of a <see cref="Value.Three"/>
    /// of <see cref="Suit.Heart"/>.
    /// </para>
    /// </remarks>
    public class Column : IEquatable<Column>
    {
        /// <summary>
        /// The face-down cards.
        /// </summary>
        /// <remarks>
        /// <para>
        /// If there are no face-down cards, this is an empty list.
        /// </para>
        /// </remarks>
        public IReadOnlyList<Card> FaceDown
        {
            get;
        }

        /// <summary>
        /// The face-up cards.
        /// </summary>
        /// <remarks>
        /// <para>
        /// If there are more than one, they must obey the rules of a run.
        /// </para>
        /// <para>
        /// If there are no face-up cards, this is an empty list.
        /// </para>
        /// </remarks>
        public IReadOnlyList<Card> FaceUp
        {
            get;
        }

        /// <summary>
        /// Constructs a new column with the provided face-down and face-up cards.
        /// </summary>
        /// <param name="faceDown">The face-down cards.</param>
        /// <param name="faceUp">The face-up run of cards.</param>
        public Column(IReadOnlyList<Card>? faceDown, IReadOnlyList<Card>? faceUp)
        {
            if (!(faceDown is null) && faceDown.Any()
                && (faceUp is null || !faceUp.Any()))
            {
                /*
                 * This is the special case where there are face-down cards,
                 * but no face-up cards.  This flips the top face-down card
                 * to make it face-up.
                 */
                int shortenedFaceDownCount = faceDown.Count - 1;
                Card newTopCard = faceDown[shortenedFaceDownCount];
                FaceDown = faceDown.Take(shortenedFaceDownCount).ToList();
                FaceUp = new List<Card>() { newTopCard };
            }
            else
            {
                if (faceDown is null)
                {
                    FaceDown = Array.Empty<Card>();
                }
                else
                {
                    FaceDown = new List<Card>(faceDown);
                }
                if (faceUp is null)
                {
                    FaceUp = Array.Empty<Card>();
                }
                else
                {
                    FaceUp = new List<Card>(faceUp);
                }
            }
        }

        /// <summary>
        /// Returns <see langword="true"/> if the column has any face-down cards.
        /// </summary>
        /// <returns><see langword="true"/> if the column has any face-down cards.</returns>
        public bool HasFaceDownCards()
        {
            return FaceDown.Any();
        }

        /// <summary>
        /// Returns <see langword="true"/> if the column has any face-up cards.
        /// </summary>
        /// <returns><see langword="true"/> if the column has any face-up cards.</returns>
        public bool HasFaceUpCards()
        {
            return FaceUp.Any();
        }

        /// <summary>
        /// Returns the count of face-down cards in the column.
        /// </summary>
        /// <returns>The count of face-down cards.</returns>
        public int GetCountOfFaceDownCards()
        {
            return FaceDown.Count;
        }

        /// <summary>
        /// Returns the count of face-up cards in the column.
        /// </summary>
        /// <returns>The count of face-up cards.</returns>
        public int GetCountOfFaceUpCards()
        {
            return FaceUp.Count;
        }

        /// <summary>
        /// Returns the top face-up card in the column.
        /// </summary>
        /// <returns>The top card.</returns>
        /// <exception cref="ArgumentOutOfRangeException">If there are no face-up cards in the column.</exception>
        public Card GetTopCard()
        {
            return FaceUp[FaceUp.Count - 1];
        }

        /// <summary>
        /// Returns a run of cards from the column.
        /// </summary>
        /// <param name="count">The number of cards to take from the current column run.</param>
        /// <returns>The run of cards.</returns>
        /// <exception cref="ArgumentOutOfRangeException">If the number of cards is less than <c>1</c>,
        /// or exceeds the number of face-up cards.</exception>
        public IReadOnlyList<Card> GetTopCards(int count)
        {
            if (count < 1)
            {
                throw new ArgumentOutOfRangeException(nameof(count), "Must be at least 1.");
            }
            if (count > FaceUp.Count)
            {
                throw new ArgumentOutOfRangeException(nameof(count), "Larger than the available cards.");
            }

            int start = FaceUp.Count - count;
            return FaceUp.Skip(start).ToList();
        }

        /// <summary>
        /// Returns a tuple containing the top card from the column,
        /// and a copy of the column missing that card.
        /// </summary>
        /// <returns>The card and a new column.</returns>
        /// <exception cref="ArgumentException">If the column does not have any cards.</exception>
        public Tuple<Card, Column> ExtractCard()
        {
            if (!HasFaceUpCards())
            {
                throw new ArgumentException("No card available in the column.");
            }

            int index = FaceUp.Count - 1;
            Card card = FaceUp[index];
            Column column = new Column(FaceDown, FaceUp.Take(index).ToList());
            return new Tuple<Card, Column>(card, column);
        }

        /// <summary>
        /// Returns a tuple containing the top <paramref name="count"/> cards from the column,
        /// and a copy of the column missing those cards.
        /// </summary>
        /// <param name="count">The number of cards to take from the current column run.</param>
        /// <returns>The run and a new column.</returns>
        /// <exception cref="ArgumentOutOfRangeException">If <paramref name="count"/> is less than <c>1</c>,
        /// or greater than the number of face-up cards.</exception>
        public Tuple<IReadOnlyList<Card>, Column> ExtractRun(int count)
        {
            if (count < 1)
            {
                throw new ArgumentOutOfRangeException(nameof(count), "Cannot be less than one.");
            }
            if (count > FaceUp.Count)
            {
                throw new ArgumentOutOfRangeException(nameof(count), "Greater than number of face-up cards.");
            }

            int runStart = FaceUp.Count - count;
            IReadOnlyList<Card> run = FaceUp.Skip(runStart).ToList();
            Column column = new Column(FaceDown, FaceUp.Take(runStart).ToList());
            return new Tuple<IReadOnlyList<Card>, Column>(run, column);
        }

        /// <summary>
        /// Returns a copy of this column with the given run of cards added.
        /// </summary>
        /// <param name="newCards">The new run of cards.</param>
        /// <returns>A copy of the column with the new run of cards added.</returns>
        /// <exception cref="ArgumentException">If <paramref name="newCards"/> is empty.</exception>
        public Column WithCards(IEnumerable<Card> newCards)
        {
            if (!newCards.Any())
            {
                throw new ArgumentException("Cannot add empty run.", nameof(newCards));
            }

            IReadOnlyList<Card> newFaceUp = FaceUp.Concat(newCards).ToList();
            return new Column(FaceDown, newFaceUp);
        }

        /// <summary>
        /// Returns a copy of this column with the given card added to the run.
        /// </summary>
        /// <param name="newCard">The card to add.</param>
        /// <returns>A copy of the column with the added card.</returns>
        public Column WithCard(Card newCard)
        {
            IReadOnlyList<Card> newFaceUp = FaceUp.Append(newCard).ToList();
            return new Column(FaceDown, newFaceUp);
        }

        /// <summary>
        /// Checks whether it is legal to add the given run of cards to this column.
        /// </summary>
        /// <remarks>
        /// <para>
        /// If the column is empty, this checks whether the first card in the run
        /// is a <see cref="Value.King"/>.
        /// </para>
        /// <para>
        /// If the column is not empty, this checks whether the first card in the run
        /// is one lower in value and of the opposite color to the top card on the column.
        /// </para>
        /// </remarks>
        /// <param name="run">The run of cards to add to this column.</param>
        /// <returns><see langword="true"/> if it is legal to add the given run of cards to this column.</returns>
        /// <exception cref="ArgumentException">If <paramref name="run"/> is empty.</exception>
        public bool CanAddRun(IReadOnlyList<Card> run)
        {
            if (!run.Any())
            {
                throw new ArgumentException("Cannot be empty.", nameof(run));
            }

            Card firstCardOfRunToAdd = run[0];
            if (HasFaceUpCards())
            {
                Card topCardOfColumn = FaceUp[FaceUp.Count - 1];
                return topCardOfColumn.Value.GetValue() == 1 + firstCardOfRunToAdd.Value.GetValue()
                    && topCardOfColumn.Color != firstCardOfRunToAdd.Color;
            }
            else
            {
                return firstCardOfRunToAdd.Value == Value.King;
            }
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as Column);
        }

        /// <inheritdoc/>
        public bool Equals(Column? other)
        {
            return other != null
                && FaceDown.SequenceEqual(other.FaceDown)
                && FaceUp.SequenceEqual(other.FaceUp);
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            HashCode hash = new HashCode();
            foreach (var card in FaceDown)
            {
                hash.Add(card);
            }
            foreach (var card in FaceUp)
            {
                hash.Add(card);
            }
            return hash.ToHashCode();
        }

        /// <inheritdoc/>
        public override string ToString()
        {
            return "Column (down: " + FaceDown + ", up: " + FaceUp + ")";
        }
    }
}
