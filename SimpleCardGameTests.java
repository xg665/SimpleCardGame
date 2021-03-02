
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.junit.*;


/**
 *  This class uses the JUnit testing framework
 *  Players can be used throughout the testing process
 *  Other instances are customized by the factory 
 */
public class SimpleCardGameTests {

	static Player p1;
	
	static Player p2;
	
	/**
	   *  Create two players used by some of
	   *  the testing methods
	   */
	@BeforeClass  
    public static void setUp() throws InvalidDeckException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {  

        p1 = Factory.createPlayer();
        
        p2 = Factory.createPlayer();
        
	}  
	
	/**
	   * Basic implementation of testing each card having
	   * approximately 1/52 probability of being positioned
	   * in any of the 52 positions.
	   */
	@Test
	public void testShuffle() throws InvalidDeckException {
		
		Map<String,Map<Integer,Integer>> count = new HashMap<>(); //Count: key is String rep of a Card
																  //value is a map that maps from position to count
		int iter = 2000000;
		
		for(int i=0;i<iter;i++) {
			
			Deck simpleDeck = Factory.createDeck("SimpleDeck");
		
			simpleDeck.shuffle();
			
			for(int j=0;j<52;j++) {					//Count how many times a card positioned in jth slot
				
				Card c = simpleDeck.cards.get(j);
				String str = c.toString();
				
				if(!count.containsKey(str)) {
					count.put(str,new HashMap<>());
				}
				
				Map<Integer,Integer> map = count.get(str);
				
				map.put(j, map.getOrDefault(j, 0)+1);
				
			}
			
		}
		
		for(Map<Integer,Integer> map: count.values()) {
			
			for(int i=0;i<52;i++) {
				if(map.get(i)!=null) {								//calculate prob of each card
					double prob = (map.get(i)+0.0)/iter;			//appearing in ith position
					assertTrue( prob>=(1.0/54) && prob <=(1.0/50) );//target should be 1/52
				}
			}
			
		}
		
		
	}
	
	/**
	   *  Test size of a simple deck after populate()
	   *  and the order the cards is correct
	   */
	@Test
	public void testSizeAndRank() throws InvalidDeckException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {
		
		Deck simpleDeck = Factory.createDeck("SimpleDeck");
		
		boolean ordered = true;
		
		assertEquals(52,simpleDeck.cards.size());
		
		for(int i=1;i<simpleDeck.cards.size();i++) {
			
			if(simpleDeck.cards.get(i).getRank().ordinal()<simpleDeck.cards.get(i-1).getRank().ordinal()) {
				
				ordered = false;			//rank of current card cannot be smaller than the previous
				
			}
			
		}
		
		assertEquals(true,ordered);
        
		
	}
	
	/**
	   *  Test whether two hands with same ranks can
	   *  be tied
	   */
	@Test
	public void testGameTie() throws NullPointerException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidHandException, InvalidDeckException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {
		
		Deck testDeck = Factory.createDeck("TestDeck");
        
        Dealer simpleDealer = Factory.createDealer("SimpleDealer",testDeck, Factory.createHandCons("SimpleHand"), Class.forName("SimpleHand").getDeclaredField("capacity"));
		
        simpleDealer.dealHand(p1);
	    
  	  	simpleDealer.dealHand(p2);
  	  	
		assertEquals("Tie",simpleDealer.determineWinner(p1,p2)); 
		
	}
	
	/**
	   *  Test whether the hands with the second highest rank can
	   *  win
	   */
	@Test
	public void testGameP1Win() throws NullPointerException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidHandException, InvalidDeckException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {
		
		Deck testDeck = Factory.createDeck("TestDeck1");
        
        Dealer simpleDealer = Factory.createDealer("SimpleDealer",testDeck, Factory.createHandCons("SimpleHand"), Class.forName("SimpleHand").getDeclaredField("capacity"));
		
		simpleDealer.dealHand(p1);
	    
  	  	simpleDealer.dealHand(p2);
  	  	
		assertEquals("Player 1 wins",simpleDealer.determineWinner(p1,p2)); 
		
	}
	
	/**
	   *  Test whether the hands with the second highest rank can
	   *  win
	   */
	@Test
	public void testGameP2Win() throws NullPointerException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidHandException, InvalidDeckException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {
		
		Deck testDeck = Factory.createDeck("TestDeck2");
        
        Dealer simpleDealer = Factory.createDealer("SimpleDealer",testDeck, Factory.createHandCons("SimpleHand"), Class.forName("SimpleHand").getDeclaredField("capacity"));
		
		simpleDealer.dealHand(p1);
	    
  	  	simpleDealer.dealHand(p2);
  	  	
		assertEquals("Player 2 wins",simpleDealer.determineWinner(p1,p2)); 
		
	}

}
