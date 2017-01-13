package cardGroups;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cards.Card;

public class Pile_old {

	
	public List<Card> aPile;
	
	// constructor for cloning
	private Pile_old(Pile_old original) {
		List<Card> aNewPile = new ArrayList<Card>();
		int len=original.aPile.size();
		for(int i=0;i<len;i++) {
			aNewPile.add(original.aPile.get(i).clone());
		}
		aPile = aNewPile;
	}
	
	@Override
	public Pile_old clone() { return new Pile_old(this); }
	
	public Pile_old() {
		this.aPile = new ArrayList<Card>();
	}
	
	public Pile_old(List<Card> l) {
		this.aPile = l;
	}
	
	public Pile_old(boolean isHand) {
		this.aPile = new ArrayList<Card>();
	}

	// can be overridden by subclasses with restrictions
	public boolean addFaceUp(Card card) {
		card.faceUp = true;
		aPile.add(0,card);
		return true;
	}
	
	// can be overridden by subclasses with restrictions
	public boolean addFaceDown(Card card) {
		card.faceUp = false;
		aPile.add(0,card);
		return true;
	}
	
	// can be overridden by subclasses with restrictions
	public boolean addCard(Card card) {
		aPile.add(0,card);
		return true;
	}
	
	public Card draw(int i) {
		Card c = this.aPile.get(i);
		this.aPile.remove(i);
		return c;
	}
	
	public Card draw() {
		return this.draw(0);
	}
	
	public Card blindDraw() {
		Random rnd = new Random();
		int drawn = rnd.nextInt(aPile.size());
		Card out = null;
		for(int i=0;i<aPile.size();i++ ) {
			if(i==drawn) {
				out = aPile.get(i);
				aPile.remove(i);
				break;
			}
		}
		return out;
	}
	
	public List<Card> discardAll() {
		List<Card> out = aPile;
		aPile = new ArrayList<Card>();
		return out;
	}
	
	public void addCardList(List<Card> list) {
		for(int i=0;i<list.size();i++)
			aPile.add(0,list.get(i));
	}
	
	
	// shuffle functions:
	// swap 2 cards
	public static void swap(List<Card> list, int i, int j) {
		Card tmp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, tmp);		}
		
	// modified from method in Collections class
	public static void cardListShuffle(List<Card> aList) {
		Random rnd = new Random();
		for (int i=aList.size() ; i>1 ; i--)
			swap(aList, i-1, rnd.nextInt(i));
	}
	
	public void shuffle() {
		cardListShuffle(aPile);
	}
	
	public void cut() {
		Random rnd = new Random();
		int deckSize=this.aPile.size();
		int cutAt = 0;
		while(cutAt==0 || cutAt==deckSize)
			cutAt = rnd.nextInt(deckSize);
		List<Card> cutDeck = new ArrayList<Card>();
		for(int i=cutAt;i<deckSize;i++ )
			cutDeck.add(this.aPile.get(i));
		for(int i=0;i<cutAt;i++)
			cutDeck.add(this.aPile.get(i));
		
		this.aPile = cutDeck;
	}
	
	// NOTE: cards faceup to read rank and suit, otherwise get nulls
	public void removeRank(String rank) {
		int len = this.aPile.size();
		for(int i=len-1;i>=0;i--) {
			Card c = this.aPile.get(i);
			c.faceUp=true;
			if(!c.getIsJoker() && c.getRank().equalsIgnoreCase(rank)) {
				this.aPile.remove(i);
			}
			else
				c.faceUp=false;
		}
	}

}
