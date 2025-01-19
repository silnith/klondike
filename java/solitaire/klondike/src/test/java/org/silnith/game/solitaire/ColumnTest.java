package org.silnith.game.solitaire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.silnith.deck.Suit.CLUB;
import static org.silnith.deck.Suit.DIAMOND;
import static org.silnith.deck.Suit.HEART;
import static org.silnith.deck.Suit.SPADE;
import static org.silnith.deck.Value.ACE;
import static org.silnith.deck.Value.EIGHT;
import static org.silnith.deck.Value.FIVE;
import static org.silnith.deck.Value.FOUR;
import static org.silnith.deck.Value.JACK;
import static org.silnith.deck.Value.KING;
import static org.silnith.deck.Value.NINE;
import static org.silnith.deck.Value.QUEEN;
import static org.silnith.deck.Value.SEVEN;
import static org.silnith.deck.Value.SIX;
import static org.silnith.deck.Value.TEN;
import static org.silnith.deck.Value.THREE;
import static org.silnith.deck.Value.TWO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.silnith.deck.Card;
import org.silnith.util.Pair;


public class ColumnTest {
    
    @Test
    public void testColumnEmpty() {
    	final List<Card> faceDown = Collections.emptyList();
    	final List<Card> faceUp = Collections.emptyList();
    	
        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testColumnDownNull() {
    	final List<Card> faceDown = null;
    	final List<Card> faceUp = Collections.emptyList();

        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testColumnUpNull() {
    	final List<Card> faceDown = Collections.emptyList();
    	final List<Card> faceUp = null;

        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testColumnNullNull() {
    	final List<Card> faceDown = null;
    	final List<Card> faceUp = null;

        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testColumnWithDown() {
		final List<Card> faceDown = Arrays.asList(
				new Card(FOUR, SPADE),
				new Card(JACK, HEART),
				new Card(ACE, HEART));
        final List<Card> faceUp = Collections.emptyList();

        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testColumnWithUp() {
        final List<Card> faceDown = Collections.emptyList();
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
                new Card(QUEEN, DIAMOND),
                new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));

        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testColumnWithBoth() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));

        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testHasFaceDownCards() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertTrue(column.hasFaceDownCards());
    }
    
    @Test
    public void testHasFaceDownCardsNull() {
        final List<Card> faceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
                new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));
		final Column column = new Column(null, faceUp);
                
        assertFalse(column.hasFaceDownCards());
    }
    
    @Test
    public void testHasFaceDownCardsEmpty() {
        final List<Card> faceDown = Collections.emptyList();
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
                new Card(QUEEN, DIAMOND),
                new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                
        assertFalse(column.hasFaceDownCards());
    }
    
    @Test
    public void testHasFaceUpCards() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertTrue(column.hasFaceUpCards());
    }
    
    @Test
    public void testHasFaceUpCardsNull() {
        final List<Card> faceDown = Collections.emptyList();
		final Column column = new Column(faceDown, null);
        
        assertFalse(column.hasFaceUpCards());
    }
    
    @Test
    public void testHasFaceUpCardsEmpty() {
        final List<Card> faceDown = Collections.emptyList();
        final List<Card> faceUp = Collections.emptyList();
		final Column column = new Column(faceDown, faceUp);
        
        assertFalse(column.hasFaceUpCards());
    }
    
    @Test
    public void testHasFaceUpCardsAfterFlipFromDown() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Collections.emptyList();
		final Column column = new Column(faceDown, faceUp);
                
        assertTrue(column.hasFaceUpCards());
    }
    
    @Test
    public void testGetNumberOfFaceDownCards() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertEquals(3, column.getNumberOfFaceDownCards());
    }
    
    @Test
    public void testGetNumberOfFaceDownCardsNull() {
        final List<Card> faceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
                new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));
		final Column column = new Column(null, faceUp);
                
        assertEquals(0, column.getNumberOfFaceDownCards());
    }
    
    @Test
    public void testGetNumberOfFaceDownCardsEmpty() {
        final List<Card> faceDown = Collections.emptyList();
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
                new Card(QUEEN, DIAMOND),
                new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                
        assertEquals(0, column.getNumberOfFaceDownCards());
    }
    
    @Test
    public void testGetNumberOfFaceUpCards() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertEquals(4, column.getNumberOfFaceUpCards());
    }
    
    @Test
    public void testGetNumberOfFaceUpCardsNull() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final Column column = new Column(faceDown, null);
                
        assertEquals(1, column.getNumberOfFaceUpCards());
    }
    
    @Test
    public void testGetNumberOfFaceUpCardsEmpty() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
        final List<Card> faceUp = Collections.emptyList();
		final Column column = new Column(faceDown, faceUp);
                
        assertEquals(1, column.getNumberOfFaceUpCards());
    }
    
    @Test
    public void testFlippedCardRemovedFromDownNull() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final Column column = new Column(faceDown, null);
                
        assertEquals(2, column.getNumberOfFaceDownCards());
    }
    
    @Test
    public void testFlippedCardRemovedFromDownEmpty() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Collections.emptyList();
		final Column column = new Column(faceDown, faceUp);
                
        assertEquals(2, column.getNumberOfFaceDownCards());
    }
    
    @Test
    public void testGetTopCard() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertEquals(new Card(TEN, DIAMOND), column.getTopCard());
    }
    
    @Test
    public void testGetTopCardNull() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final Column column = new Column(faceDown, null);
        
        assertEquals(new Card(ACE, HEART), column.getTopCard());
    }
    
    @Test
    public void testGetTopCardEmpty() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Collections.emptyList();
		final Column column = new Column(faceDown, faceUp);
        
        assertEquals(new Card(ACE, HEART), column.getTopCard());
    }
    
    @Test
    public void testGetFaceUpCards() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
        
        final List<Card> actual = column.getFaceUpCards();
        
		final List<Card> expected = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetFaceUpCardsNull() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final Column column = new Column(faceDown, null);
                
        final List<Card> actual = column.getFaceUpCards();
        
		final List<Card> expected = Collections.singletonList(
				new Card(ACE, HEART));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetFaceUpCardsEmpty() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Collections.emptyList();
		final Column column = new Column(faceDown, faceUp);
        
        final List<Card> actual = column.getFaceUpCards();
        
		final List<Card> expected = Collections.singletonList(
				new Card(ACE, HEART));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTopCardsOverflow() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertThrows(IllegalArgumentException.class, () -> {
        	column.getTopCards(5);
        });
    }
    
    @Test
    public void testGetTopCards4() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        final List<Card> actual = column.getTopCards(4);
        
		final List<Card> expected = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTopCards3() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        final List<Card> actual = column.getTopCards(3);
        
		final List<Card> expected = Arrays.asList(
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
				new Card(TEN, DIAMOND));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTopCards2() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
		final List<Card> actual = column.getTopCards(2);
		
        final List<Card> expected = Arrays.asList(
        		new Card(JACK, CLUB),
        		new Card(TEN, DIAMOND));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTopCards1() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
		
		final List<Card> actual = column.getTopCards(1);
		
        final List<Card> expected = Collections.singletonList(
        		new Card(TEN, DIAMOND));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTopCardsUnderflow() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertThrows(IllegalArgumentException.class, () -> {
        	column.getTopCards(0);
        });
    }
    
    @Test
    public void testExtractCardEmpty() {
		final Column column = new Column(null, null);
                        
        assertThrows(IllegalArgumentException.class, () -> {
        	column.extractCard();
        });
    }
    
    @Test
    public void testExtractCardCard() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
        
		final Pair<Card, Column> pair = column.extractCard();
		final Card actual = pair.getFirst();

		final Card expected = new Card(TEN, DIAMOND);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testExtractCardColumn() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
        
		final Pair<Card, Column> pair = column.extractCard();
        final Column actual = pair.getSecond();

        final List<Card> expectedFaceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
        		new Card(JACK, CLUB));
		final Column expected = new Column(faceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testExtractRunEmpty() {
    	final Column column = new Column(null, null);
    	
    	assertThrows(IllegalArgumentException.class, () -> {
    		column.extractRun(1);
    	});
    }
    
    @Test
    public void testExtractRunOverflow() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
              
        assertThrows(IllegalArgumentException.class, () -> {
        	column.extractRun(5);
        });
    }
    
    @Test
    public void testExtractRun4Run() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
        
		final Pair<List<Card>, Column> pair = column.extractRun(4);
		final List<Card> actual = pair.getFirst();
        
        final List<Card> expected = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
        assertEquals(expected, actual);
    }
    
    @Test
    public void testExtractRun4Column() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
        
		final Pair<List<Card>, Column> pair = column.extractRun(4);
        final Column actual = pair.getSecond();
        
        final List<Card> expectedFaceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART));
		final List<Card> expectedFaceUp = Collections.singletonList(
				new Card(ACE, HEART));
		final Column expected = new Column(expectedFaceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testExtractRun3Run() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
		
		final Pair<List<Card>, Column> pair = column.extractRun(3);
		final List<Card> actual = pair.getFirst();
        
        final List<Card> expected = Arrays.asList(
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
        assertEquals(expected, actual);
    }
    
    @Test
    public void testExtractRun3Column() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
		
		final Pair<List<Card>, Column> pair = column.extractRun(3);
        final Column actual = pair.getSecond();
        
        final List<Card> expectedFaceUp = Collections.singletonList(
        		new Card(KING, CLUB));
		final Column expected = new Column(faceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testExtractRun2Run() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
        
		final Pair<List<Card>, Column> pair = column.extractRun(2);
		final List<Card> actualRun = pair.getFirst();

		final List<Card> expectedRun = Arrays.asList(
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		assertEquals(expectedRun, actualRun);
    }
    
    @Test
    public void testExtractRun2Column() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
        
		final Pair<List<Card>, Column> pair = column.extractRun(2);
        final Column actual = pair.getSecond();

        final List<Card> expectedFaceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND));
		final Column expected = new Column(faceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testExtractRun1Run() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
        
		final Pair<List<Card>, Column> pair = column.extractRun(1);
		final List<Card> actual = pair.getFirst();

		final List<Card> expected = Arrays.asList(
		        new Card(TEN, DIAMOND));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testExtractRun1Column() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
        
		final Pair<List<Card>, Column> pair = column.extractRun(1);
        final Column actual = pair.getSecond();

        final List<Card> expectedFaceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
        		new Card(JACK, CLUB));
		final Column expected = new Column(faceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testExtractRunUnderflow() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertThrows(IllegalArgumentException.class, () -> {
        	column.extractRun(0);
        });
    }
    
    @Test
    public void testWithCard() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        final Column actual = column.withCard(new Card(NINE, CLUB));
        
        final List<Card> expectedFaceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
        		new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND),
		        new Card(NINE, CLUB));
		final Column expected = new Column(faceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testWithCardNull() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);

        assertThrows(RuntimeException.class, () -> {
        	column.withCard(null);
        });
    }
    
    @Test
    public void testWithCards1() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        final List<Card> run = Collections.singletonList(
        		new Card(NINE, CLUB));
		final Column actual = column.withCards(run);
        
        final List<Card> expectedFaceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
        		new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND),
		        new Card(NINE, CLUB));
		final Column expected = new Column(faceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testWithCards3() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column originalColumn = new Column(faceDown, faceUp);

        final List<Card> run = Arrays.asList(
        		new Card(NINE, CLUB),
        		new Card(EIGHT, DIAMOND),
        		new Card(SEVEN, CLUB));
		final Column actual = originalColumn.withCards(run);

        final List<Card> expectedFaceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
        		new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND),
		        new Card(NINE, CLUB),
		        new Card(EIGHT, DIAMOND),
		        new Card(SEVEN, CLUB));
		final Column expected = new Column(faceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testWithCardsEmpty() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
        
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		
		final Column column = new Column(faceDown, faceUp);
		
        assertThrows(IllegalArgumentException.class, () -> {
        	column.withCards(Collections.emptyList());
        });
    }
    
    @Test
    public void testWithCardsNull() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
        
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		
		final Column column = new Column(faceDown, faceUp);
                       
        assertThrows(IllegalArgumentException.class, () -> {
        	column.withCards(null);
        });
    }
    
    @Test
    public void testCanAddRunEmptyColumnNull() {
    	final Column column = new Column(null, null);
    	
    	assertThrows(IllegalArgumentException.class, () -> {
    		column.canAddRun(null);
    	});
    }
    
    @Test
    public void testCanAddRunEmptyColumnEmptyRun() {
    	final Column column = new Column(null, null);
    	
    	assertThrows(IllegalArgumentException.class, () -> {
    		column.canAddRun(Collections.emptyList());
    	});
    }
    
    @Test
    public void testCanAddRunEmptyColumnKing()
    {
    	final Column column = new Column(null, null);
    	
    	final List<Card> run = Arrays.asList(
    			new Card(KING, CLUB));
    	
    	assertTrue(column.canAddRun(run));
    }
    
    @Test
    public void testCanAddRunEmptyColumnKingRun()
    {
    	final Column column = new Column(null, null);
    	
    	final List<Card> run = Arrays.asList(
    			new Card(KING, CLUB),
    			new Card(QUEEN, HEART),
    			new Card(JACK, CLUB));
    	
    	assertTrue(column.canAddRun(run));
    }
    
    @Test
    public void testCanAddRunEmptyColumnQueen() {
    	final Column column = new Column(null, null);
    	
    	final List<Card> run = Arrays.asList(
    			new Card(QUEEN, HEART));
    	
    	assertFalse(column.canAddRun(run));
    }
    
    @Test
    public void testCanAddRunEmptyColumnQueenRun() {
    	final Column column = new Column(null, null);
    	
    	final List<Card> run = Arrays.asList(
    			new Card(QUEEN, HEART),
    			new Card(JACK, CLUB),
    			new Card(TEN, HEART));
    	
    	assertFalse(column.canAddRun(run));
    }
    
    @Test
    public void testCanAddRunNull() {
    	final List<Card> faceDown = Arrays.asList(
    			new Card(THREE, CLUB),
    			new Card(THREE, DIAMOND),
    			new Card(THREE, HEART),
    			new Card(THREE, SPADE));
    	final List<Card> faceUp = Arrays.asList(
    			new Card(TEN, SPADE),
    			new Card(NINE, HEART),
    			new Card(EIGHT, SPADE));
    	final Column column = new Column(faceDown, faceUp);
    	
    	assertThrows(IllegalArgumentException.class, () -> {
    		column.canAddRun(null);
    	});
    }
    
    @Test
    public void testCanAddRunEmptyRun() {
    	final List<Card> faceDown = Arrays.asList(
    			new Card(THREE, CLUB),
    			new Card(THREE, DIAMOND),
    			new Card(THREE, HEART),
    			new Card(THREE, SPADE));
    	final List<Card> faceUp = Arrays.asList(
    			new Card(TEN, SPADE),
    			new Card(NINE, HEART),
    			new Card(EIGHT, SPADE));
    	final Column column = new Column(faceDown, faceUp);
    	
    	assertThrows(IllegalArgumentException.class, () -> {
    		column.canAddRun(Collections.emptyList());
    	});
    }
    
    @Test
    public void testCanAddRun() {
    	final List<Card> faceDown = Arrays.asList(
    			new Card(THREE, CLUB),
    			new Card(THREE, DIAMOND),
    			new Card(THREE, HEART),
    			new Card(THREE, SPADE));
    	final List<Card> faceUp = Arrays.asList(
    			new Card(TEN, SPADE),
    			new Card(NINE, HEART),
    			new Card(EIGHT, SPADE));
    	final Column column = new Column(faceDown, faceUp);
    	
    	final List<Card> run = Arrays.asList(
    			new Card(SEVEN, HEART),
    			new Card(SIX, SPADE),
    			new Card(FIVE, HEART));
    	
    	assertTrue(column.canAddRun(run));
    }
    
    @Test
    public void testCanAddRunWrongColor() {
    	final List<Card> faceDown = Arrays.asList(
    			new Card(THREE, CLUB),
    			new Card(THREE, DIAMOND),
    			new Card(THREE, HEART),
    			new Card(THREE, SPADE));
    	final List<Card> faceUp = Arrays.asList(
    			new Card(TEN, SPADE),
    			new Card(NINE, HEART),
    			new Card(EIGHT, SPADE));
    	final Column column = new Column(faceDown, faceUp);
    	
    	final List<Card> run = Arrays.asList(
    			new Card(SEVEN, SPADE),
    			new Card(SIX, HEART),
    			new Card(FIVE, SPADE));
    	
    	assertFalse(column.canAddRun(run));
    }
    
    @Test
    public void testCanAddRunTooHigh() {
    	final List<Card> faceDown = Arrays.asList(
    			new Card(THREE, CLUB),
    			new Card(THREE, DIAMOND),
    			new Card(THREE, HEART),
    			new Card(THREE, SPADE));
    	final List<Card> faceUp = Arrays.asList(
    			new Card(TEN, SPADE),
    			new Card(NINE, HEART),
    			new Card(EIGHT, SPADE));
    	final Column column = new Column(faceDown, faceUp);
    	
    	final List<Card> run = Arrays.asList(
    			new Card(EIGHT, HEART),
    			new Card(SEVEN, SPADE),
    			new Card(SIX, HEART));
    	
    	assertFalse(column.canAddRun(run));
    }
    
    @Test
    public void testCanAddRunTooLow() {
    	final List<Card> faceDown = Arrays.asList(
    			new Card(THREE, CLUB),
    			new Card(THREE, DIAMOND),
    			new Card(THREE, HEART),
    			new Card(THREE, SPADE));
    	final List<Card> faceUp = Arrays.asList(
    			new Card(TEN, SPADE),
    			new Card(NINE, HEART),
    			new Card(EIGHT, SPADE));
    	final Column column = new Column(faceDown, faceUp);
    	
    	final List<Card> run = Arrays.asList(
    			new Card(SIX, HEART),
    			new Card(FIVE, SPADE),
    			new Card(FOUR, HEART));
    	
    	assertFalse(column.canAddRun(run));
    }
    
    @Test
    public void testCanAddRunKing() {
    	final List<Card> faceDown = Arrays.asList(
    			new Card(THREE, CLUB),
    			new Card(THREE, DIAMOND),
    			new Card(THREE, HEART),
    			new Card(THREE, SPADE));
    	final List<Card> faceUp = Arrays.asList(
    			new Card(TEN, SPADE),
    			new Card(NINE, HEART),
    			new Card(EIGHT, SPADE));
    	final Column column = new Column(faceDown, faceUp);
    	
    	final List<Card> run = Arrays.asList(
    			new Card(KING, HEART),
    			new Card(QUEEN, SPADE),
    			new Card(JACK, HEART));
    	
    	assertFalse(column.canAddRun(run));
    }
    
    @Test
    public void testEquals() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
		
		final Column column1 = new Column(faceDown, faceUp);
        final Column column2 = new Column(faceDown, faceUp);
                
        assertTrue(column1.equals(column2));
    }
    
    @Test
    public void testHashCode() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
		
		final Column column1 = new Column(faceDown, faceUp);
        final Column column2 = new Column(faceDown, faceUp);
                
        assertEquals(column1.hashCode(), column2.hashCode());
    }
    
    @Test
    public void testEqualsFaceDownDiffer() {
        final List<Card> faceDown1 = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        final List<Card> faceDown2 = Arrays.asList(
        		new Card(TWO, SPADE),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
		
		final Column column1 = new Column(faceDown1, faceUp);
		final Column column2 = new Column(faceDown2, faceUp);
                
        assertFalse(column1.equals(column2));
    }
    
    @Test
    public void testEqualsFaceDownShorter() {
        final List<Card> faceDown1 = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        final List<Card> faceDown2 = Collections.emptyList();
        
		final List<Card> faceUp = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
		
		final Column column1 = new Column(faceDown1, faceUp);
		final Column column2 = new Column(faceDown2, faceUp);
                
        assertFalse(column1.equals(column2));
    }
    
    @Test
    public void testEqualsFaceDownLonger() {
        final List<Card> faceDown1 = Collections.emptyList();
        final List<Card> faceDown2 = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
		
		final Column column1 = new Column(faceDown1, faceUp);
		final Column column2 = new Column(faceDown2, faceUp);
                
        assertFalse(column1.equals(column2));
    }
    
    @Test
    public void testEqualsFaceUpDiffer() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp1 = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
        final List<Card> faceUp2 = Arrays.asList(
        		new Card(EIGHT, SPADE),
        		new Card(SEVEN, HEART),
        		new Card(SIX, CLUB));
        
		final Column column1 = new Column(faceDown, faceUp1);
		final Column column2 = new Column(faceDown, faceUp2);
                
        assertFalse(column1.equals(column2));
    }
    
    @Test
    public void testEqualsFaceUpShorter() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp1 = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
        final List<Card> faceUp2 = Collections.emptyList();
        
		final Column column1 = new Column(faceDown, faceUp1);
		final Column column2 = new Column(faceDown, faceUp2);
                
        assertFalse(column1.equals(column2));
    }
    
    @Test
    public void testEqualsFaceUpLonger() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp1 = Collections.emptyList();
        final List<Card> faceUp2 = Arrays.asList(
        		new Card(EIGHT, SPADE),
        		new Card(SEVEN, DIAMOND),
        		new Card(SIX, CLUB));
        
		final Column column1 = new Column(faceDown, faceUp1);
		final Column column2 = new Column(faceDown, faceUp2);
                
        assertFalse(column1.equals(column2));
    }
    
}
