namespace Silnith.Game.Klondike.Move.Filter
{
    /// <summary>
    /// A filter for Klondike solitaire moves.
    /// </summary>
    /// <remarks>
    /// <para>
    /// Game states that pass this filter are problematic and should be pruned
    /// from the search tree rather than searched.
    /// </para>
    /// </remarks>
    public interface ISolitaireMoveFilter : IMoveFilter<ISolitaireMove, Board>
    {
    }
}
