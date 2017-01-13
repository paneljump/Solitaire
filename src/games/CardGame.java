package games;

import java.util.List;

import cardGroups.Pile;
import cards.Card;

/*
 * 
 * 
 */

public class CardGame {
	
	// card rank by game
	public static String[] getCardRankList(String rankListName) {
		String[] aceToKing = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
		String[] aceToAce = {"A","2","3","4","5","6","7","8","9","10","J","Q","K","A"};
		if(rankListName.equalsIgnoreCase("aceToKing"))
			return aceToKing;
		else if(rankListName.equalsIgnoreCase("aceToAce"))
			return aceToAce;
		return null;
	}
	
	// so I can wrap rank (usually aces high but you never know...)
	public static String[] wrapRank(String rankListName, String highRank) {
		String[] rankList = CardGame.getCardRankList(rankListName);
		int highRankIndex=0;
		return null;
	}
	
	// generate list of wild cards: adds cards of a given rank of all suits to the list.
	public static Pile makeWildCardList(Pile oldWildList,String rank) {
		Pile wildList = oldWildList.clone();
		String[] suitList = Card.getSuits();
		for(int i=0;i<suitList.length;i++) { // public Card(String s, String r) 
			Card c = new Card(suitList[i],rank);
			c.setFaceUp(true);
			wildList.addCard(c);
		}
		return wildList;
	}
	
	// NOTE: this is only for non-jokers. Maybe it belongs in CardGame?
	public static boolean isCardWild(Card card, Pile wildCardList) {
		if(!card.getFaceUp())
			return false;
		if(wildCardList.getPile().size()==0)
			return false;
		for(int i=0;i<wildCardList.getPile().size();i++) {
			if(card.equals(wildCardList.getPile().get(i)))
				return true;
		}
		return false;
	}

}
