
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
class Factory {
	
	
	public static Constructor<?> createHandCons(String type) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		
		switch(type) {
			case "SimpleHand":
				return Class.forName("SimpleHand").getConstructor(List.class);
			default:
				return Class.forName("SimpleHand").getConstructor(List.class);
				
		}
	}
	
	public static Deck createDeck(String type) throws InvalidDeckException {
		
		switch(type) {
			case "SimpleDeck":return new SimpleDeck();
			default: return new SimpleDeck();
		}
		
	}
	
	public static Dealer createDealer(String type, Deck deck, Constructor<?> handCons, Field capacityField) throws IllegalArgumentException, IllegalAccessException, InvalidDeckException {
		
		switch(type) {
		
			case "SimpleDealer": return new SimpleDealer(deck,handCons,capacityField.getInt(null));
			
			default: return new SimpleDealer(deck,handCons,capacityField.getInt(null));
		}
		
		
		
	}
	
	public static Player createPlayer() {
		return new Player();
	}
	
}

class Solution {
	
  public static void main(String[] args) {
	  
    Scanner input = null;
    
    String ans = null;
    
    try {    
    	
    	input = new Scanner(System.in);
    	
    	Deck simpleDeck = Factory.createDeck("SimpleDeck");
    	
    	Dealer simpleDealer = Factory.createDealer("SimpleDealer",simpleDeck, Factory.createHandCons("SimpleHand"), Class.forName("SimpleHand").getDeclaredField("capacity"));
    	
    	Player p1 = Factory.createPlayer();
    	
    	Player p2 = Factory.createPlayer();
    	
    	simpleDealer.shuffle();
    	
    	do {
    		
    	  simpleDealer.dealHand(p1);
    
    	  simpleDealer.dealHand(p2);
    
    	  System.out.println(simpleDealer.determineWinner(p1,p2));
    
    	  System.out.println(p1.showHand());
    
    	  System.out.println(p2.showHand());
    	  
    	  if(!simpleDealer.canPlay()) {
    		  break;
    	  }
    	  System.out.println("Deal Again?(Y/N)");
    	  
    	  ans = input.nextLine();

    	}
    	while(ans.equals("Y"));
    	
    	System.out.println("Thank you for playing");
    
    }
    catch(Exception e) {
    	e.printStackTrace();
    }
    
    
  }
}

enum Suit {
    
  SPADES,
  CLUBS,
  DIAMONDS,
  HEARTS;
  
}
enum Rank {
  
  TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,TEN,JACK,QUEEN,KING,ACE;
  
}
@SuppressWarnings("serial")
class InvalidHandException extends Exception {

	public InvalidHandException(String msg) {
		super(msg);
	}
	
}
@SuppressWarnings("serial")
class InvalidDeckException extends Exception {
	
	public InvalidDeckException(String msg) {
		super(msg);
	}
	
}

abstract class Hand implements Comparable<Hand>{
	
	protected List<Card> cards;
	
}

class SimpleHand extends Hand  {
    
  public final static int capacity = 5;
  
  public SimpleHand(List<Card> cards) throws InvalidHandException{
    
	if(cards.size()!=SimpleHand.capacity) {
		
		throw new InvalidHandException("Invalid number of cards in hand");
	}
	  
    this.cards = cards;
    
    
    
  }
  
  @Override
  public int compareTo(Hand h){
	  
	
    Collections.sort(this.cards);
      
    Collections.sort(h.cards);
    
    for(int i=0;i<SimpleHand.capacity;i++){
      
      int ret = this.cards.get(i).compareTo(h.cards.get(i));
      
      if(ret!=0){
        return ret;
      }
      
    }
    
	
    return 0;
    
  }
  
  
  public String toString(){
	  
    return this.cards.toString();
  
  }
  
  
  
}

class Player {
  
  private Hand hand;
  
  public Player(){

  }
  
  public void receiveHand(Hand hand) throws NullPointerException{
    
	if(hand==null) {
		throw new NullPointerException("Received Null Hand");
	}
	
    this.hand = hand;
    
  }
  
  
  public Hand gethand() {
    
	if(this.hand==null) {
		throw new NullPointerException("Player's hand not initialized");
	}
	
    return this.hand;
    
  }
  
  public String showHand() throws NullPointerException{
	
	if(this.hand==null) {
		throw new NullPointerException("Player's hand not initialized");
	}
	
    return this.hand.toString();
  }
  
}

abstract class Dealer {
	
	protected Deck deck;
	
	protected Constructor<?> handCons;
	
	protected int handCap;
	
	public abstract void dealHand(Player p) throws InvalidHandException, InvalidDeckException, NullPointerException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
	
	public abstract String determineWinner(Player p1, Player p2) throws NullPointerException, InvalidHandException;
	
	public abstract void shuffle();
	
	public abstract boolean canPlay();
	
} 

class SimpleDealer extends Dealer {

  
  public SimpleDealer(Deck deck, Constructor<?> handCons, int handCap) throws InvalidDeckException{
    this.deck = deck;
    this.handCons = handCons;
    this.handCap = handCap;
  }
  
  public void shuffle(){
	  
    this.deck.shuffle();
  
  }
  
  
  public void dealHand(Player p) throws InvalidHandException, InvalidDeckException, NullPointerException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
    
	if(p==null) {
	  throw new NullPointerException("Player not initialized");
	}
	  
    if(this.deck.getSize()<this.handCap){
      throw new InvalidDeckException("Not Enough Cards to Deal");
    }
    
    List<Card> cards = new ArrayList<>();
    
    for(int i=0;i<this.handCap;i++){
      
      cards.add(this.deck.removeTop());
      
    }
    
    p.receiveHand((Hand)this.handCons.newInstance(cards));
    
    
  }
  
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
  
  private int compareHands(Player p1, Player p2) throws InvalidHandException{
    
	if(p1==null || p2==null) {
		throw new NullPointerException("Player Not Initialized");
	}
	
	if(p1.gethand()==null || p2.gethand()==null) {
		throw new InvalidHandException("Player Hand is Null");
	}
	
    return p1.gethand().compareTo(p2.gethand());
    
  }
  
  @Override
  public boolean canPlay() {
	  // TODO Auto-generated method stub
	  return this.deck.getSize()>=(2*this.handCap);
  }
  
  public Deck getDeck(){
    return this.deck;
  }

}
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
class SimpleDeck extends Deck {
  
  
  
  public static final int capacity=52;
 
  
  public SimpleDeck() throws InvalidDeckException{
  
	  super();
	  this.populate();
    
  }
  
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
    
    this.cards.add(card);
    this.ids.add(card.toString());
    
  }
  
  public Card removeTop() throws InvalidDeckException{
    
    if(this.cards.size()==0){
      throw new InvalidDeckException("Removing from Empty Deck");
    }
    
    ids.remove(cards.get(0).toString());
    
    return cards.remove(0);
    
  }
  
  public void shuffle(){
    	  
    Random rand = new Random();
    
    for(int i=0;i<this.cards.size();i++) {
    	
    	int j = i + rand.nextInt(this.cards.size()-i);
    	
    	Card c = this.cards.get(i);
    	
    	this.cards.set(i, this.cards.get(j));
    	
    	this.cards.set(j, c);
    	
    }
    
  }
  
  @Override
  public void populate() throws InvalidDeckException {
  	// TODO Auto-generated method stub
	  for(Rank r:Rank.values()){
	        
	      for(Suit s: Suit.values()){
	        
	    	if(this.cards.size()==SimpleDeck.capacity) {
	    		throw new InvalidDeckException("Deck is Full");
	    	}
	    	else this.cards.add(new Card(s,r));
	        
	      }
	      
	  }
  }
  
  public int getSize(){
	  
    return this.cards.size();
    
  }
  
  public String toString(){
	  
    return this.cards.toString();
    
  }

}

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
  
  @Override
  public int compareTo(Card c){
      
    return c.rank.ordinal()-this.rank.ordinal();
    
    
  }
  
  @Override
  public String toString(){
    return this.rank.toString() + " of " + this.suit.toString();
  }
  
}

