
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.*;

public class SimpleCardGameTests {

	static Player p1;
	
	static Player p2;
	
	@BeforeClass  
    public static void setUp() throws InvalidDeckException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {  

        p1 = Factory.createPlayer();
        
        p2 = Factory.createPlayer();
        
	}  
	
	@Test
	public void testSizeAndRank() throws InvalidDeckException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {
		
		Deck testDeck = Factory.createDeck("SimpleDeck");
		
		boolean ordered = true;
		
		assertEquals(52,testDeck.cards.size());
		
		for(int i=1;i<testDeck.cards.size();i++) {
			
			if(testDeck.cards.get(i).getRank().ordinal()<testDeck.cards.get(i-1).getRank().ordinal()) {
				
				ordered = false;
				
			}
			
		}
		
		assertEquals(true,ordered);
        
		
	}

	@Test
	public void testGameTie() throws NullPointerException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidHandException, InvalidDeckException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {
		
		Deck testDeck = Factory.createDeck("TestDeck");
        
        Dealer simpleDealer = Factory.createDealer("SimpleDealer",testDeck, Factory.createHandCons("SimpleHand"), Class.forName("SimpleHand").getDeclaredField("capacity"));
		
        simpleDealer.dealHand(p1);
	    
  	  	simpleDealer.dealHand(p2);
  	  	
		assertEquals("Tie",simpleDealer.determineWinner(p1,p2)); 
		
	}
	
	@Test
	public void testGameP1Win() throws NullPointerException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidHandException, InvalidDeckException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {
		
		Deck testDeck = Factory.createDeck("TestDeck1");
        
        Dealer simpleDealer = Factory.createDealer("SimpleDealer",testDeck, Factory.createHandCons("SimpleHand"), Class.forName("SimpleHand").getDeclaredField("capacity"));
		
		simpleDealer.dealHand(p1);
	    
  	  	simpleDealer.dealHand(p2);
  	  	
		assertEquals("Player 1 wins",simpleDealer.determineWinner(p1,p2)); 
		
	}
	
	@Test
	public void testGameP2Win() throws NullPointerException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidHandException, InvalidDeckException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {
		
		Deck testDeck = Factory.createDeck("TestDeck2");
        
        Dealer simpleDealer = Factory.createDealer("SimpleDealer",testDeck, Factory.createHandCons("SimpleHand"), Class.forName("SimpleHand").getDeclaredField("capacity"));
		
		simpleDealer.dealHand(p1);
	    
  	  	simpleDealer.dealHand(p2);
  	  	
		assertEquals("Player 2 wins",simpleDealer.determineWinner(p1,p2)); 
		
	}

}
