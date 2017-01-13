package cardGroups;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import cards.CardLike;
import games.CardGame;

public class Run extends Pile {
	
	protected final String suit;
	protected final boolean strictSuit; // some games might allow suit to vary
	
	public String getSuit() { return suit; }
	public boolean getStrictQ() { return strictSuit; } 
	
	public Run(String s, boolean strictSuitQ) {
		super();
		suit=s;
		strictSuit=strictSuitQ;
	}
	
	public Run(Card c, boolean strictSuitQ) {
		super();
		aPile.add(c);
		suit = c.getSuit(); // this isn't going to work for a joker
		strictSuit=strictSuitQ;
	}
	
	private Run(Pile p, String s, boolean strictSuitQ) {
		aPile=p.getPile();
		suit=s;
		strictSuit=strictSuitQ;
	}
	
	public Run clone() {
		Pile p = new Pile(this.aPile);
		Pile pClone=p.clone();
		String newSuit = new String(this.getSuit());
		return new Run(pClone,newSuit,this.strictSuit);
	}
	
	// override add card functions to only permit those that match suit
	// don't forget to allow jokers
	// find a way to search and replace with naturals when possible
	
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
			
		// otherwise...
		return natForJoker(natural);
	}
		
	// NOTE: any non-duplicate non-joker non-wild same-suit card will be accepted, regardless of proximity
	public boolean isCardNatural(Card c, Pile wildCardList) {
		boolean upQ = c.getFaceUp();
		c.setFaceUp(true);
		if(c.getIsJoker() || CardGame.isCardWild(c,wildCardList) 
				|| (strictSuit &&!c.getSuit().equalsIgnoreCase(this.suit)) ) {
			c.setFaceUp(upQ);
			return false;
		}
		// NOTE: can have 2 aces, no other duplicates
		boolean out=false;
		int nIn = countDuplicateRanksOfNew(c,wildCardList);
		if(nIn==0)
			out=true;
		else if(nIn==1 && c.getRank().equalsIgnoreCase("A"))
			out=true;
		c.setFaceUp(upQ);
		return out;
	}
	
	// puts wild/jokers at end
	protected void sort(String[] rankArray, Pile wildCardList) {
		List<Card> sortedList = new ArrayList<>();
		List<Card> wildList = new ArrayList<>();
		int len = this.aPile.size();
		for(int i=0;i<len;i++) {
			Card c = aPile.get(i);
			boolean upQ = c.getFaceUp();
			if( c.getIsJoker() || CardGame.isCardWild(c,wildCardList) ) 
				wildList.add(c);
			else
				sortedList.add(c);
			c.setFaceUp(upQ);
		}
		sortedList.sort(new Card.byRank(rankArray));
		for(int i=0;i<wildList.size();i++)
			sortedList.add(wildList.get(i));
		this.aPile = sortedList;
	}
	
	/*
	// Methods to override:
	public boolean addFaceUp(CardLike c)
	public boolean addFaceUp(CardLike c, Pile wildCardList)
	public boolean addFaceDown(CardLike c)
	public boolean addFaceDown(CardLike c, Pile wildCardList)
	public boolean addCard(Card card)
	public boolean addCard(Card card, Pile wildCardList)
	*/
	
	@Override
	public boolean addFaceUp(CardLike c) {
		return addCard( (Card) c, new Pile(), CardGame.getCardRankList("aceToAce"), true, true );
	}
	public boolean addFaceUp(CardLike c, Pile wildCardList) {
		return addCard( (Card) c, wildCardList, CardGame.getCardRankList("aceToAce"), true, true );
	}
	@Override
	public boolean addFaceDown(CardLike c) {
		return addCard( (Card) c, new Pile(), CardGame.getCardRankList("aceToAce"), true, false );
	}
	public boolean addFaceDown(CardLike c, Pile wildCardList) {
		return addCard( (Card) c, wildCardList, CardGame.getCardRankList("aceToAce"), true, false );
	}
	@Override
	public boolean addCard(Card card) {
		return addCard( (Card) card, new Pile(), CardGame.getCardRankList("aceToAce"), false, false );
	}
	public boolean addCard(Card card, Pile wildCardList) {
		return addCard( (Card) card, wildCardList, CardGame.getCardRankList("aceToAce"), false, false );
	}
	
	protected boolean addCard(Card card, Pile wildCardList, String[] rankArray, boolean upDownQ, boolean faceUp) {
		// I'm assuming it can't wrap indefinitely; if it has 14 (A to A) it's done 
		int len=aPile.size();
		if(len>13)
			return false; 
		
		// determine if card belongs in this Run
		boolean upQ=card.getFaceUp();
		card.setFaceUp(true);
		if( card.getIsJoker() || CardGame.isCardWild(card,wildCardList) ) { // always add a wild
			if(upDownQ) {
				if(faceUp)
					card.setFaceUp(true);
				else
					card.setFaceUp(false);
			}
			else
				card.setFaceUp(upQ);
			this.aPile.add(card);
			return true;
		}
		boolean canAdd=true;
		canAdd=this.isCardNatural(card,wildCardList);
		if(!canAdd) {
			card.setFaceUp(upQ);
			return false;
		}
		
		// list existing ranks
		List<String> existingRanks = new ArrayList<String>();
		for(int i=0;i<len;i++) {
			Card cPile = aPile.get(i);
			boolean upQ2 = cPile.getFaceUp();
			cPile.setFaceUp(true);
			if( cPile.getIsJoker() || CardGame.isCardWild(cPile,wildCardList) )
				existingRanks.add("w");
			else
				existingRanks.add(cPile.getRank());
			cPile.setFaceUp(upQ2);
		}
		// up to 2 aces, up to 1 any other natural
		int count=0;
		String thisRank = card.getRank();
		for(int i=0;i<existingRanks.size();i++)
			if( existingRanks.get(i).equalsIgnoreCase(thisRank) )
				count++;
		if(thisRank.equalsIgnoreCase("A") && count<=1)
			canAdd=true;
		else if(count<=0)
			canAdd=true;
		else
			canAdd=false;
		
		if(canAdd) {
			if(upDownQ) {
				if(faceUp)
					card.setFaceUp(true);
				else
					card.setFaceUp(false);
			}
			else
				card.setFaceUp(upQ);
			this.aPile.add(card);
			return true;
		}
		else {
			card.setFaceUp(upQ);
			return false;
		}
		
	}
	

	


}
