
/*
 * Click `Run` to execute the snippet below!
 */

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/*
 * To execute Java, please define "static void main" on a class
 * named Solution.
 *
 * If you need more classes, simply define them inline.
 */

/**
* This is a simple card game that practices Dependency Injection and 
* Loose Coupling between classes. Compile with "javac Solution.java" and 
* run with "java Solution" on the cmd line. A simple deck of card consists of
* 52 distinct cards ranked by their face values, i.e TWO,THREE,..,ACE. Dealer
* gives each of two players a hand of five cards during each round. A hand 
* with the highest rank wins that round. To continue playing the same deck, Enter
* "Y" on the cmd line. The game ends when there is not enough cards to deal two hands
* 
* @author Peter Guo
* 
*/

/**
* 
* Factory class uses factory pattern to create different types of instances
* specified by strings as parameter. For example, to start a simple card game,
* create Hand constructor, Dealer,Player and Deck
*/
class Factory {
	
	/**
	   * This static method is used to creates the constructors of Hand class or 
	   * its child classes. 
	   * @param type Type of Hand 
	   * @return Constructor<?> Hand constructor
	   */
	public static Constructor<?> createHandCons(String type) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		
		switch(type) {
			case "SimpleHand":
				return Class.forName("SimpleHand").getConstructor(List.class);
			default:
				return Class.forName("SimpleHand").getConstructor(List.class);
				
		}
	}
	
	/**
	   * This static method is used to create a deck of cards
	   * @param type Type of Deck
	   * @return Deck instance that extends Deck
	   */
	public static Deck createDeck(String type) throws InvalidDeckException {
		
		switch(type) {
			case "SimpleDeck":return new SimpleDeck();
			case "TestDeck": return new TestDeck();
			case "TestDeck1": return new TestDeck(1);
			case "TestDeck2": return new TestDeck(2);
			default: return new SimpleDeck();
		}
		
	}
	/**
	   * This static method is used to create a dealer 
	   * @param type Type of Dealer
	   * @return Dealer instance that extends Dealer
	   */
	public static Dealer createDealer(String type, Deck deck, Constructor<?> handCons, Field capacityField) throws IllegalArgumentException, IllegalAccessException, InvalidDeckException {
		
		switch(type) {
		
			case "SimpleDealer": return new SimpleDealer(deck,handCons,capacityField.getInt(null));
			
			default: return new SimpleDealer(deck,handCons,capacityField.getInt(null));
		}

	}
	/**
	   * This static method is used to create a player
	   * @return Player Player instance
	   */
	public static Player createPlayer() {
		return new Player();
	}
	
}


/**
* Driver class that has the main method that starts this program
* A simple card game uses adheres to the rules specified by instructions
*/
class Solution {
	
  public static void main(String[] args) {
	  
	
    Scanner input = null;
    
    String ans = null;
    
    try {    
    	
    	input = new Scanner(System.in);
    	
    	Deck simpleDeck = Factory.createDeck("SimpleDeck");     		//Create a simple deck
    																	//Dealer requires a deck, hand, and hand size
    	Dealer simpleDealer = Factory.createDealer("SimpleDealer",simpleDeck, Factory.createHandCons("SimpleHand"), Class.forName("SimpleHand").getDeclaredField("capacity"));
    	
    	Player p1 = Factory.createPlayer();			//Create two players
    	
    	Player p2 = Factory.createPlayer();
    	
    	simpleDealer.shuffle();						//shuffle cards before playing
    	
    	do {
    		
    	  simpleDealer.dealHand(p1);				//deal two hands to two players
    
    	  simpleDealer.dealHand(p2);
    
    	  System.out.println(simpleDealer.determineWinner(p1,p2)+"\n");
    
    	  System.out.println(p1.showHand());		// Show results
    
    	  System.out.println(p2.showHand());
    	  
    	  if(!simpleDealer.canPlay()) {				// End game if not enough cards
    		  break;
    	  }
    	  System.out.println("\n"+"Deal Again?(Y/N)");  	// Ask for User cmdline input
    	  
    	  ans = input.nextLine();

    	}
    	while(ans.equals("Y"));
    	
    	System.out.println("\n"+"Thank you for playing");
    
    }
    catch(Exception e) {
    	e.printStackTrace();
    }
    
    
  }
}

/**
 *  Suits that default cards use
 */
enum Suit {
    
  SPADES,
  CLUBS,
  DIAMONDS,
  HEARTS;
  
}

/**
 *  Ranks that default cards use
 */
enum Rank {
  
  TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,TEN,JACK,QUEEN,KING,ACE;
  
}

/**
 *  Custome Exception class that is thrown regarding exceptions with invalid hands
 */
@SuppressWarnings("serial")
class InvalidHandException extends Exception {

	public InvalidHandException(String msg) {
		super(msg);
	}
	
}
/**
 *  Custome Exception class that is thrown regarding exceptions with invalid deck
 */
@SuppressWarnings("serial")
class InvalidDeckException extends Exception {
	
	public InvalidDeckException(String msg) {
		super(msg);
	}
	
}

/**
 *  This is an abstract Hand class that implements comparable
 *  Any sub class has at least a empty List of cards and an
 *  implementation to compare hands of cards
 */
abstract class Hand implements Comparable<Hand>{
	
	protected List<Card> cards;
	
}

/**
 *  This is an implementation of Hand class that compare hands 
 *  by their ranks. It should be instantiated with List of cards
 *  that is of the same size as capacity, 5. 
 */

class SimpleHand extends Hand  {
    
  public final static int capacity = 5;
  
  public SimpleHand(List<Card> cards) throws InvalidHandException{
    
	if(cards.size()!=SimpleHand.capacity) {
		
		throw new InvalidHandException("Invalid number of cards in hand");
	}
	  
    this.cards = cards;
  }
  
  
  /**
   * This method is required by comparable interface. Both hands are sorted, then compare
   * each cards from highest to lowest ranked. 
   * @param Hand a Hand to compare with
   * @return int negative number is return if this has higher priority
   */
  @Override
  public int compareTo(Hand h){
	  
	
    Collections.sort(this.cards);	//Sort in Descending order
      
    Collections.sort(h.cards);
    
    for(int i=0;i<SimpleHand.capacity;i++){
    								//the next highest determines winner
      int ret = this.cards.get(i).compareTo(h.cards.get(i));
      
      if(ret!=0){
        return ret;
      }
      
    }
    
	
    return 0;
    
  }
  
  /**
   * This method returns string representation of current hand
   * @return String string representation of current hand
   */
  public String toString(){
	  
    return this.cards.toString();
  
  }
 
}
/**
 * This class represents a player who has one hand.
 * Could be extended to implement more services, or more
 * fields.
 */
class Player {
  
  private Hand hand;
  
  public Player(){

  }
  
  /**
   * Inject hand dependency by parameter. 
   * @param Hand a Hand to reference
   */
  public void receiveHand(Hand hand) throws NullPointerException{
    
	if(hand==null) {
		throw new NullPointerException("Received Null Hand");
	}
	
    this.hand = hand;
    
  }
  
  /**
   * Utility method to get players' hand reference
   * @return Hand player's hand
   */
  public Hand gethand() {
    
	if(this.hand==null) {
		throw new NullPointerException("Player's hand not initialized");
	}
	
    return this.hand;
    
  }
  
  /**
   * Utility method that shows player's hand as a string
   * @return String a hand's string representation
   */
  public String showHand() throws NullPointerException{
	
	if(this.hand==null) {
		throw new NullPointerException("Player's hand not initialized");
	}
	
    return this.hand.toString();
  }
  
}
/**
 *  This is an abstract Dealer class has a Deck, a way to create
 *  a hand, and size of a hand by default. Any type of dealer has 
 *  to implement how to dealHand, how to choose winner, if can play
 *  one more round with current deck, and how to shuffle the deck 
 */
abstract class Dealer {
	
	protected Deck deck;
	
	protected Constructor<?> handCons;
	
	protected int handCap;
	
	public abstract void dealHand(Player p) throws InvalidHandException, InvalidDeckException, NullPointerException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
	
	public abstract String determineWinner(Player p1, Player p2) throws NullPointerException, InvalidHandException;
	
	public abstract void shuffle();
	
	public abstract boolean canPlay();
	
} 
/**
 *  This is an implementation of Dealer that deals hands, determine winners,
 *  and shuffle according to the rules of instruction. Simple Dealer determines
 *  the game continues if deck has at least two more hands.
 */
class SimpleDealer extends Dealer {

  
  public SimpleDealer(Deck deck, Constructor<?> handCons, int handCap) throws InvalidDeckException{
    this.deck = deck;
    this.handCons = handCons;
    this.handCap = handCap;
  }
  /**
   *  Simple dealer uses deck's implementation of
   *  shuffle method.
   */
  public void shuffle(){
	  
    this.deck.shuffle();
  
  }
  
  /**
   *  this method uses the injected constructor to create 
   *  a hand and passes it to the player
   *  @param Player to whom the hand goes
   */
  public void dealHand(Player p) throws InvalidHandException, InvalidDeckException, NullPointerException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
    
	if(p==null) {
	  throw new NullPointerException("Player not initialized");
	}
	  
    if(this.deck.getSize()<this.handCap){
      throw new InvalidDeckException("Not Enough Cards to Deal");
    }
    
    List<Card> cards = new ArrayList<>();
    
    for(int i=0;i<this.handCap;i++){
      
      cards.add(this.deck.removeTop());							//Remove from top of deck
      
    }
    
    p.receiveHand((Hand)this.handCons.newInstance(cards));		//Use passed in constructor to create hand
    
    
  }
  /**
   *  Simple Dealer determines winner by comparing players'
   *  hands. One with higher ranked cards wins
   *  @param Player players to compare
   *  @return String string representation of who wins
   *  
   */
  public String determineWinner(Player p1, Player p2) throws NullPointerException, InvalidHandException{
	  
    int ret = compareHands(p1,p2);
    
    if(ret==0){
      return "Tie";
    }
    else if(ret<0){
      return "Player 1 wins";
    }
    else{
      return "Player 2 wins";
    }
    
  }
  /**
   *  This method compares players' hands. It uses Hand's implementation
   *  of comparing method.
   *  @param Player players to compare
   *  @return int Higher ranked hand has priority
   */
  private int compareHands(Player p1, Player p2) throws InvalidHandException{
    
	if(p1==null || p2==null) {
		throw new NullPointerException("Player Not Initialized");
	}
	
	if(p1.gethand()==null || p2.gethand()==null) {
		throw new InvalidHandException("Player Hand is Null");
	}
	
    return p1.gethand().compareTo(p2.gethand());
    
  }
  /**
   * This method determines if game can continue with
   * this deck of cards. Game continues if deck has
   * at least 2 more hands
   * @return boolean true if can continue
   */
  @Override
  public boolean canPlay() {
	  // TODO Auto-generated method stub
	  return this.deck.getSize()>=(2*this.handCap);
  }
  
  /**
   *  Util method that returns Dealer's
   *  deck reference
   */
  public Deck getDeck(){
    return this.deck;
  }

}

/**
 *  This is an abstract Deck class that uses List as the 
 *  basic data structure to store Cards. Sub classes can 
 *  choose how to populate the List by overriding populate()
 *  method. The Set makes sure each card is unique up to
 *  its unique string identifier
 */
abstract class Deck {
	
	protected List<Card> cards;
  
	protected Set<String> ids;
	
	public Deck() {
		this.cards = new ArrayList<>();
		this.ids = new HashSet<>();
	}
	
	public abstract void addCard(Card card) throws InvalidDeckException;
	
	public abstract Card removeTop() throws InvalidDeckException;
	
	public abstract void shuffle();
	
	public abstract void populate() throws InvalidDeckException;
	
	public abstract int getSize();
	
}

/**
 *  This is an implementation of Deck class. A simple Deck 
 *  has 52 cards, guaranteed by the populate() method. Adding 
 *  cards will put the card to the bottom of deck. removeTop will
 *  remove the top card from the current deck 
 */
class SimpleDeck extends Deck {
  
  public static final int capacity=52;
 
  
  public SimpleDeck() throws InvalidDeckException{
  
	  super();
	  this.populate();
    
  }
  /**
   *  This method add a Card to the bottom of the deck
   *  @param Card the card to be added
   */
  public void addCard(Card card) throws InvalidDeckException{
      
	if(card==null) {
	  throw new NullPointerException("Adding Null Card");
	}
    if(this.cards.size()==SimpleDeck.capacity){
      throw new InvalidDeckException("Adding to Full Deck");
    }
    
    if(ids.contains(card.toString())) {
    	throw new InvalidDeckException("Adding Duplicate Card");
    }
    
    this.cards.add(card);						//Duplicates are not allowed in simple card game
    this.ids.add(card.toString());				//String representation is used as identifier
    
  }
  
  /**
   *  This method removes a card from the top of deck
   *  @return Card card on the top of deck
   */
  public Card removeTop() throws InvalidDeckException{
    
    if(this.cards.size()==0){
      throw new InvalidDeckException("Removing from Empty Deck");
    }
    
    ids.remove(cards.get(0).toString()); 
    
    return cards.remove(0);						//Remove from top of the deck
    
  }
  
  /**
   *  This method shuffles a deck of cards.
   *  The randomness is shown by each card 
   *  positioned in any of the position with
   *  the probability of 1/52 
   */
  public void shuffle(){
    	  
    Random rand = new Random();
    
    for(int i=0;i<this.cards.size();i++) {				//(not swap with previous)*(Swap with next) = 1/52  
    	
    	int j = i + rand.nextInt(this.cards.size()-i);
    	
    	Card c = this.cards.get(i);
    	
    	this.cards.set(i, this.cards.get(j));
    	
    	this.cards.set(j, c);
    	
    }
    
  }
  /**
   *  This method populate the deck of cards called by
   *  the constructor.The resulting deck has 52 cards sorted
   *  ascending.
   */
  @Override
  public void populate() throws InvalidDeckException {
  	// TODO Auto-generated method stub
	  for(Rank r:Rank.values()){
	        
	      for(Suit s: Suit.values()){
	        
	    	if(this.cards.size()==SimpleDeck.capacity) {
	    		throw new InvalidDeckException("Deck is Full");
	    	}
	    	else this.cards.add(new Card(s,r));			//Each cards is added to bottom of deck
	        
	      }
	      
	  }
  }
  
  /**
   *  Util method to get the size of deck
   *  @return int size of deck
   */
  public int getSize(){
	  
    return this.cards.size();
    
  }
  
  /**
   *  Util method to String representation of deck
   *  @return String deck of cards
   */
  public String toString(){
	  
    return this.cards.toString();
    
  }

}

/**
 *  This is a POJO class that represents s single card
 *  The suit and rank has to be choosen from the defined 
 *  values
 */

class Card implements Comparable<Card>{
    
  private Suit suit;
  
  private Rank rank;
  
  public Card(Suit suit,Rank rank){
    
    this.suit = suit;
    
    this.rank = rank;
    
  }
  
  public Rank getRank(){
    return this.rank;
  }
  
  public Suit getSuit(){
    return this.suit;
  }
  /**
   *  this method is required by comparable interface
   *  higher ranked cards have higher priority
   *  @return int priority
   */
  @Override
  public int compareTo(Card c){
      
    return c.rank.ordinal()-this.rank.ordinal();
    
    
  }
  /**
   *  Util method to get String representation of a card
   *  e.g TWO of SPADE 
   *  @return String card 
   */
  @Override
  public String toString(){
    return this.rank.toString() + " of " + this.suit.toString();
  }
  
}




/*
 * 
 * This class extends the SimpleDeck and is used for Unit Testing
 * It uses its own populate methods that will dictate which cards
 * are given to whom. It allows duplicated cards since hands are
 * compared solely by their highest ranks.
 */
class TestDeck extends SimpleDeck {
	
	public TestDeck() throws InvalidDeckException {
		
		super();
		
	}
	/**
	   *  Custome constructor that favors one player over the 
	   *  other
	   */
	public TestDeck(int who) throws InvalidDeckException {
		
		super();
		
		if(who==1) {								
			this.populateP1Win();
		}
		else if (who==2){
			this.populateP2Win();
		}
		
	}
	/**
	   *  add cards now allows duplicate
	   *  @param Card the card to be added
	   */
	@Override
	public void addCard(Card card) throws InvalidDeckException{
	      
		if(card==null) {
		  throw new NullPointerException("Adding Null Card");
		}
	    if(this.cards.size()==SimpleDeck.capacity){
	      throw new InvalidDeckException("Adding to Full Deck");
	    }
	    
	    this.cards.add(card);
	    
	  }
	/**
	   *  populate method that should produces tie
	   */
	@Override
	public void populate() throws InvalidDeckException {
		// TODO Auto-generated method stub
		
		for(Suit s: Suit.values()) {
			this.addCard(new Card(s,Rank.ACE));
		}
		this.addCard(new Card(Suit.DIAMONDS,Rank.ACE));
		
		for(Suit s: Suit.values()) {
			this.addCard(new Card(s,Rank.ACE));
		}
		this.addCard(new Card(Suit.DIAMONDS,Rank.ACE));
		
	}
	/**
	   *  populate method that should favors player1
	   */
	public void populateP1Win() throws InvalidDeckException {
		// TODO Auto-generated method stub
		this.cards.clear();
		
		for(Suit s: Suit.values()) {
			this.addCard(new Card(s,Rank.ACE));
		}
		this.addCard(new Card(Suit.DIAMONDS,Rank.ACE));
		
		for(Suit s: Suit.values()) {
			this.addCard(new Card(s,Rank.ACE));
		}
		this.addCard(new Card(Suit.DIAMONDS,Rank.KING));
		
	}
	/**
	   *  populate method that should favors player2
	   */
	public void populateP2Win() throws InvalidDeckException {
		// TODO Auto-generated method stub
		this.cards.clear();
		
		for(Suit s: Suit.values()) {
			this.addCard(new Card(s,Rank.ACE));
		}
		this.addCard(new Card(Suit.DIAMONDS,Rank.KING));
		
		for(Suit s: Suit.values()) {
			this.addCard(new Card(s,Rank.ACE));
		}
		this.addCard(new Card(Suit.DIAMONDS,Rank.ACE));
		
	}
	
	
}

