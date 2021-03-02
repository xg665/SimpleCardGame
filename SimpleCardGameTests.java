
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

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
	public void testShuffle() throws InvalidDeckException {
		
		Map<String,Map<Integer,Integer>> count = new HashMap<>();
		
		int iter = 2000000;
		
		for(int i=0;i<iter;i++) {
			
			Deck simpleDeck = Factory.createDeck("SimpleDeck");
		
			simpleDeck.shuffle();
			
			for(int j=0;j<52;j++) {
				
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
				if(map.get(i)!=null) {
					double prob = (map.get(i)+0.0)/iter;
					assertTrue( prob>=(1.0/54) && prob <=(1.0/50) );
				}
			}
			
		}
		
		
	}
	
	@Test
	public void testSizeAndRank() throws InvalidDeckException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {
		
		Deck simpleDeck = Factory.createDeck("SimpleDeck");
		
		boolean ordered = true;
		
		assertEquals(52,simpleDeck.cards.size());
		
		for(int i=1;i<simpleDeck.cards.size();i++) {
			
			if(simpleDeck.cards.get(i).getRank().ordinal()<simpleDeck.cards.get(i-1).getRank().ordinal()) {
				
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
