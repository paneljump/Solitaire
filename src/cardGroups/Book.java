package cardGroups;

/*
 * Extends Pile (for class Card only), 
 * overrides addFaceUp, addFaceDown, addCard to restrict cards to same rank, wild, or joker
 * adds function that swaps natural for wild cards, jokers
 * 
 * still need to cover case where wild played as a natural
 * 
 */

import cards.Card;
import cards.CardLike;
import games.CardGame;

public class Book extends Pile {
	
	protected final String rank;
	
	public String getRank() { return rank; }
	
	public Book(String r) {
		super();
		rank=r;
	}
	
	public Book(Card c) {
		super();
		aPile.add(c);
		rank = c.getRank();
	}
	
	private Book(Pile p, String r) {
		aPile=p.getPile();
		rank=r;
	}
	
	public Book clone() {
		Pile p = new Pile(this.aPile);
		Pile pClone=p.clone();
		String newRank = new String(this.getRank());
		return new Book(pClone,newRank);
	}
	
	// override add card functions to only permit those that match rank
	// don't forget to allow jokers
	
	@Override
	public boolean addFaceUp(CardLike c) {
		Pile emptyWildList = new Pile();
		return addFaceUp(c,emptyWildList);
	}
	public boolean addFaceUp(CardLike c, Pile wildCardList) {
		Card card = (Card) c;
		card.setFaceUp(true);
		if( card.getIsJoker() ) {
			Card newCard = new Card("",this.rank,true);
			aPile.add(0,newCard);
			return true;
		}
		if(card.getRank().equalsIgnoreCase(this.rank) ) {
			aPile.add(0,card);
			return true;
		}
		else if( CardGame.isCardWild(card,wildCardList) ) {
			aPile.add(0,card);
			return true;
		}
		else
			return false;
	}
	@Override
	public boolean addFaceDown(CardLike c) {
		Pile emptyWilds = new Pile();
		return addFaceDown(c,emptyWilds);
	}
	public boolean addFaceDown(CardLike c, Pile wildCardList) {
		Card card = (Card) c;
		card.setFaceUp(true);
		if( card.getIsJoker() ) {
			Card newCard = new Card("",this.rank,true);
			newCard.setFaceUp(false);
			aPile.add(0,newCard);
			return true;
		}
		if(card.getRank().equalsIgnoreCase(this.rank) ) {
			card.setFaceUp(false);
			aPile.add(0,card);
			return true;
		}
		else if( CardGame.isCardWild(card,wildCardList) ) {
			card.setFaceUp(false);
			aPile.add(0,card);
			return true;
		}
		else
			return false;
	}
	
	@Override
	public boolean addCard(Card card) {
		Pile emptyWildList = new Pile();
		return addCard(card,emptyWildList);
	}
	
	public boolean addCard(Card card, Pile wildCardList) {
		boolean upQ = card.getFaceUp();
		card.setFaceUp(true);
		if( card.getIsJoker() ) {
			Card newCard = new Card("",this.rank,true);
			newCard.setFaceUp(upQ);
			aPile.add(0,newCard);
			return true;
		}
		else if(card.getRank().equalsIgnoreCase(this.rank) ) {
			card.setFaceUp(upQ);
			aPile.add(0,card);
			return true;
		}
		else if( CardGame.isCardWild(card,wildCardList) ) {
			card.setFaceUp(upQ);
			aPile.add(0,card);
			return true;
		}
		else
			return false;
	}
	
	// NOTE: this is not used for jokers
	public Card naturalForWild(Card natural, Pile wildCardList) {
		// case 1: card isn't a natural, returns the card
		if(!isCardNatural(natural,wildCardList))
			return natural;
		
		return natForWild(natural, wildCardList);
	}
	
	public Card naturalForJoker(Card natural, Pile wildCardList) {
		// case 1: card isn't a natural, returns the card
		if(!isCardNatural(natural,wildCardList))
			return natural;
		
		
		return natForJoker(natural);
	}
	
	//
	public boolean isCardNatural(Card c, Pile wildCardList) {
		boolean upQ = c.getFaceUp();
		c.setFaceUp(true);
		boolean out=true;
		if(c.getIsJoker() || CardGame.isCardWild(c,wildCardList) || !c.getRank().equalsIgnoreCase(this.rank))
			out=false;
		c.setFaceUp(upQ);
		return out;
	}
	

}
