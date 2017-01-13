package cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import cardGroups.Pile;

public class Card implements CardLike {
	
	// supported ranks, suits and corresponding colors
	private static final String[] Ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
	private static final String[] Suits = {"clubs","hearts","spades","diamonds"};
	public static String[] getRanks() { return Card.Ranks; }
	public static String[] getSuits() { return Card.Suits; }
	
	// immutable properties of any given card object
	private final Boolean isJoker;
	private final String suit;
	private final String rank;
	
	// accessors: can only see face value if card is face up
	public Boolean getIsJoker() {
		if(faceUp)
			return isJoker;
		else
			return null;
	}
	public String getSuit() {
		if(faceUp)
			return this.suit;
		else
			return null;
	}
	public String getRank() {
		if(faceUp)
			return this.rank;
		else
			return null;
	}
	
	// more accessors: can see value if player owns pile it belongs to
	public Boolean peekIsJoker(String name, Pile p) {
		if(!p.contains(this))
			return getIsJoker(); // no special access if player doesn't own/hold pile
		if(name.equalsIgnoreCase(p.getPlayerName()))
			return isJoker;
		else
			return getIsJoker();
	}
	public String peekSuit(String name, Pile p) {
		if(!p.contains(this))
			return getSuit();
		if(name.equalsIgnoreCase(p.getPlayerName()))
			return suit;
		else
			return getSuit();
	}
	public String peekRank(String name, Pile p) {
		if(!p.contains(this))
			return getRank();
		if(name.equalsIgnoreCase(p.getPlayerName()))
			return rank;
		else
			return getRank();
	}
	
	
	// changeable properties of any given card object
	public Boolean faceUp;
	
	// for CardLike interface
	@Override
	public boolean getFaceUp() { return faceUp; }
	@Override
	public void setFaceUp(boolean tf) { this.faceUp=tf; }
	
	// make a joker
	public Card() {
		this.faceUp = false;
		this.isJoker = new Boolean(true);
		this.suit="";
		this.rank="joker";
	}
	
	// make a joker with specific rank and suit (to play joker as specific card)
	public Card(String s, String r, boolean isJoker) {
		this.faceUp = true;
		this.isJoker = new Boolean(true);
		this.suit = s;
		this.rank = r;
	}
	
	// make a non-joker, no validation right now
	public Card(String s, String r) {
		this.faceUp = false;
		this.isJoker = new Boolean(false);
		this.suit = s;
		this.rank = r;
	}
	
	// constructor for cloning, temporarily turn faceup to read values
	private Card(Card original) {
		boolean tmp = original.faceUp;
		original.faceUp=true;
		
		this.faceUp = tmp;
		this.isJoker = new Boolean(original.getIsJoker());
		this.suit = new String(original.getSuit());
		this.rank = new String(original.getRank());
		
		original.faceUp=tmp; 
	}
	
	@Override
	public Card clone() {
		return new Card(this);
	}

	// from here down, I'm providing methods for comparing cards. NOTE: I haven't overridden HashCode
	public boolean equals(Card c) {
		if( !this.faceUp || !c.getFaceUp() )
			return false;
		if( this.isJoker && c.getIsJoker() )
			return true;
		if( this.rank.equalsIgnoreCase(c.getRank()) && this.suit.equalsIgnoreCase(c.getSuit()) )
			return true;
		else
			return false;
	}
	
	public boolean sameSuit(Card otherCard) {
		if( this.suit.equalsIgnoreCase( otherCard.getSuit() ) )
			return true;
		else
			return false;
	}
	
	public boolean oppositeColor(Card otherCard) {
		if( this.suit.equalsIgnoreCase("spades") || this.suit.equalsIgnoreCase("clubs")) {
			if(otherCard.getSuit().equalsIgnoreCase("hearts") || otherCard.getSuit().equalsIgnoreCase("diamonds"))
				return true;
			else
				return false;
		}
		if( this.suit.equalsIgnoreCase("hearts") || this.suit.equalsIgnoreCase("diamonds")) {
			if(otherCard.getSuit().equalsIgnoreCase("clubs") || otherCard.getSuit().equalsIgnoreCase("spades"))
				return true;
			else
				return false;
		}
		return false; // it might hit this line if it's an unassigned joker
	}
	
	public int rankIndex(String[] rankList) {
		int rank=-1;
		for (int i=0;i<rankList.length;i++)
			if(this.rank.equalsIgnoreCase(rankList[i]))
				rank=i;
		return rank; // NOTE: will return -1 if rank isn't in list (for example, unassigned joker)
	}
	
	// comparator classes: to sort, use Collections.sort(cardList, new byRank() );   or byRank(rankList)
	// NOTE: jokers are last
	public static class byRank implements Comparator<Card> {
		protected String[] rankArray;
		
		public byRank() { rankArray = Card.getRanks(); }
		public byRank(String[] rL) { rankArray = rL; }

		@Override
		public int compare(Card c1, Card c2) {
			boolean upQ1=c1.getFaceUp(), upQ2=c2.getFaceUp();
			c1.setFaceUp(true);
			c2.setFaceUp(true);
			int out;
			if(c1.getIsJoker())
				out = 1;
			else if(c2.getIsJoker())
				out = -1;
			else {
				List<String> rankList = Arrays.asList(rankArray);
			    Integer rank1 = rankList.indexOf(c2.getRank());
			    Integer rank2 = rankList.indexOf(c2.getRank());
			    out = rank1.compareTo(rank2);
			}
			c1.setFaceUp(upQ1);
			c2.setFaceUp(upQ2);
			return out;
		  }
	}
	public class bySuit implements Comparator<Card> {
		protected String[] suitArray;
		
		public bySuit() { suitArray = Card.getRanks(); }
		public bySuit(String[] sL) { suitArray = sL; }

		@Override
		public int compare(Card c1, Card c2) {
			boolean upQ1=c1.getFaceUp(), upQ2=c2.getFaceUp();
			c1.setFaceUp(true);
			c2.setFaceUp(true);
			int out;
			if(c1.getIsJoker())
				out = 1;
			else if(c2.getIsJoker())
				out = -1;
			else {
				List<String> suitList = Arrays.asList(suitArray);
			    Integer suit1 = suitList.indexOf(c2.getSuit());
			    Integer suit2 = suitList.indexOf(c2.getSuit());
			    out = suit1.compareTo(suit2);
			}
			c1.setFaceUp(upQ1);
			c2.setFaceUp(upQ2);
			return out;
		  }
	}
	
	// build deck
	// build a deck
	private static void addADeck(List<Card> deck) {
		String ranks[] = Card.getRanks();
		String suits[] = Card.getSuits();
		for(int i=0;i<suits.length;i++)
			for(int j=0;j<ranks.length;j++) {
				deck.add( new Card(suits[i],ranks[j]) );
			}
	}
		
	private static List<Card> addAJoker(List<Card> deck) {
		deck.add( new Card() );
		return deck;
	}
		
	public static List<Card> makeDeck(int nDecks, int nJokers) {
		List<Card> deck = new ArrayList<Card>();
		for(int i=0;i<nDecks;i++)
			addADeck(deck);
		for(int i=0;i<nJokers;i++)
			addAJoker(deck);
		return deck;
	}
	
	

}
