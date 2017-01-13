package cardGroups;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import cards.GenPile;
import games.CardGame;

public class Pile extends GenPile<Card> {
	
	// constructor for cloning
	private Pile(Pile original) {
		List<Card> aNewPile = new ArrayList<>();
		int len=original.size();
		for(int i=0;i<len;i++) {
			aNewPile.add(original.aPile.get(i).clone());
		}
		aPile = aNewPile;
		playerName = new String(original.getPlayerName());
	}
	
	@Override
	public Pile clone() { return new Pile(this); }
	
	public Pile() {
		super();
	}
	
	public Pile(List<Card> l) {
		this.aPile = l;
		playerName = "";
	}
	
	public void removeRank(String rank) {
		int len = this.aPile.size();
		for(int i=len-1;i>=0;i--) {
			Card c =  this.aPile.get(i);
			c.setFaceUp(true); 
			if(!c.getIsJoker() && c.getRank().equalsIgnoreCase(rank)) {
				this.aPile.remove(i);
			}
			else
				c.faceUp=false;
		}
	}
	
	public int countWilds(Pile wildCardList) {
		int out=0;
		int len=this.aPile.size();
		for(int i=0;i<len;i++) {
			Card c = this.aPile.get(i);
			boolean upQ=c.getFaceUp();
			c.setFaceUp(true);
			if( CardGame.isCardWild(c ,wildCardList) )
				out++;
			c.setFaceUp(upQ);
		}
		return out;
	}
	
	public int countJokers() {
		int out=0;
		int len=this.aPile.size();
		for(int i=0;i<len;i++) {
			Card c = this.aPile.get(i);
			boolean upQ=c.getFaceUp();
			c.setFaceUp(true);
			if( c.getFaceUp() )
				out++;
			c.setFaceUp(upQ);
		}
		return out;
	}
	
	// 
	protected Card natForJoker(Card natural) {
		boolean upQ = natural.getFaceUp();
		natural.setFaceUp(true);
		// search for joker, returns a new one with no suit/rank
		int len = this.aPile.size();
		for(int i=0;i<len;i++) {
			Card c = this.aPile.get(i);
			boolean upQ2 = c.getFaceUp();
			c.setFaceUp(true);
			if( c.getIsJoker() ) {
				this.aPile.remove(c);
				natural.setFaceUp(upQ2);
				this.addCard(i,natural);
				Card newJoker = new Card();
				newJoker.setFaceUp(upQ);
				return newJoker; // NOTE: this joker has empty string for rank and suit
			}
			else
				c.setFaceUp(upQ2);
		}
		natural.setFaceUp(upQ);
		return natural;
		
	}
	
	/*
	 * for use with subclasses like Book and Run. Once card has been determined to be a natural,
	 * use this to complete the swap.
	 * 
	 * NOTE: this is not used for jokers. upQ is state of natural before flipping to test
	 * (which usually happens in the calling function because natural needs to be determined)
	 */
	protected Card natForWild(Card natural, Pile wildCardList) {
		boolean upQ = natural.getFaceUp();
		natural.setFaceUp(true);
			
		// case 2: 
		int len = this.aPile.size();
		for(int i=0;i<len;i++) {
			Card c = this.aPile.get(i);
			boolean upQ2 = c.getFaceUp();
			c.setFaceUp(true);
			if( CardGame.isCardWild(c,wildCardList) ) {
				this.aPile.remove(c);
				natural.setFaceUp(upQ2);
				this.addCard(i,natural);
				c.setFaceUp(upQ);
				return c;
			}
			else
				c.setFaceUp(upQ2);
		}
		return natural;
	}
	
	/*
	 * test a non-pile card against cards in a Pile: return number of times that card exists 
	 * (useful for testing runs--aces can have 2, nothing else can duplicate)
	 */
	protected int countDuplicateRanksOfNew(Card c, Pile wildCardList) {
		boolean upQ=c.getFaceUp();
		c.setFaceUp(true);
		
		int count=0;
		int len=this.aPile.size();
		for(int i=0;i<len;i++) {
			Card cPile = aPile.get(i);
			boolean upQ2=cPile.getFaceUp();
			cPile.setFaceUp(true);
			if( !CardGame.isCardWild(cPile, wildCardList) ) {
				if( cPile.getRank().equalsIgnoreCase(c.getRank()) ) 
					count++;
			}
			cPile.setFaceUp(upQ2);
		}
		c.setFaceUp(upQ);
		return count;
	}

}
