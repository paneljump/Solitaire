package cardGroups;

import cardGroups.Pile;
import cards.Card;
import cards.CardLike;
import games.CardGame;

public class AcePile extends Pile {
	
	protected String suit;
	
	public String getSuit() { return suit; }
	
	public AcePile(String s) { 
		super();
		suit=s; 
	}
	
	public AcePile(Pile p, String s) {
		aPile=p.getPile();
		suit=s;
	}
	
	@Override
	public AcePile clone() { 
		Pile p = new Pile(this.aPile);
		Pile pClone = p.clone();
		String newSuit = new String(this.getSuit());
		
		return new AcePile(pClone, newSuit);
	}
	
	@Override 
	public boolean addFaceUp(CardLike c) {
		Card card = (Card) c;
		int len = this.aPile.size();
		if(len==13)
			return false; // pile is full
		card.faceUp = true;
		if(card.getIsJoker()) {
			String thisRank = CardGame.getCardRankList("aceToKing")[len];
			Card newJoke = new Card(suit,thisRank,true);
			aPile.add(0,newJoke);
			return true;  // if pile isn't full, always add joker (set suit and rank!)
		}
		if(len==0) {
			String cSuit = card.getSuit();
			String cRank = card.getRank();
			if(cSuit.equalsIgnoreCase(suit) && cRank.equalsIgnoreCase("A")) {
				aPile.add(0,card);
				return true;
			}
			else
				return false;
		}
		
		Card topCard = this.aPile.get(0);
		if(!topCard.faceUp)
			return false;
		
		// card.faceUp = true;
		//boolean moveQ = Solitaire.adjacentSameSuit(topCard, card, "aceToKing"); // ok for joker (see above)
		boolean moveQ = topCard.sameSuit(card);
		if(moveQ) {
			String[] aceToKing = CardGame.getCardRankList("aceToKing");
			if( topCard.rankIndex(aceToKing)+1 == card.rankIndex(aceToKing) )
				moveQ=true;
			else
				moveQ=false;
		}
			
		if(!moveQ)
			return false;
		
		aPile.add(0,card);
		return true;
	}
	
	@Override
	public boolean addFaceDown(CardLike card) { return false; }

}
