package org.silnith.game;

/**
 * A common interface for game state validators. Implementations will check that
 * a game object is in a valid state, and throw a {@link RuntimeException} if it
 * is not.
 *
 * @param <T> the type of object to validate
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public interface Validator<T> {
    
    /**
     * Validates that the given game object is in a valid state.
     * 
     * @param t the game object to validate
     * @throws RuntimeException if the object is in an invalid state
     */
    void validate(T t);
    
}
