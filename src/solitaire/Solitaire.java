package solitaire;

import java.util.ArrayList;
import java.util.List;

import cardGroups.*;
import cards.Card;
//import decks.Deck;
import games.CardGame;



/*
 * This class is for methods (mostly static) common to many Solitaire games
 */

public class Solitaire extends CardGame implements Cloneable, SolitaireGame {
	
	// build board (general function)
		public static List<Pile> buildBoard(int n) {
			List<Pile> board = new ArrayList<Pile>();
			for(int i=0;i<n;i++)
				board.add(new Pile());
			return board;
		}
		
		// build ace board (general function)
		public static List<AcePile> buildAceBoard(int n) {
			String[] suits = Card.getSuits();
			List<AcePile> aceBoard = new ArrayList<AcePile>();
			for(int i=0;i<n;i++)
				for(int j=0;j<suits.length;j++)
					aceBoard.add(new AcePile(suits[j]));
			
			return aceBoard;
		}
		
		
		
		// when a pile has face up on top of face down, finds first face-up card
		public Card firstFaceUp(Pile p) {
			int len=p.size();
			if(len==0)
				return null;
			if(!p.getPile().get(0).faceUp)
				p.getPile().get(0).faceUp = true; // flip top card if needed
			
			int f=0,i=0;
			while(i<len && p.getPile().get(i).faceUp) {
				f = i++;
			}
			return p.getPile().get(f);
		}
		
		// working in Klondike
		public static void nextThree(Pile downDrawPile, Pile drawPile) {
			int ddSize = downDrawPile.size();
			int udSize = drawPile.size();
			
			// case 1: no cards left
			if(ddSize==0 && udSize==0) 
				return;
			
			// case 2: all cards turned (reset)
			if(ddSize==0) {
				for(int i=0;i<udSize;i++){
					Card c = drawPile.getPile().get(0);
					downDrawPile.addCard(c);
					drawPile.getPile().remove(0);
				}
				return;
			}
			
			// case 3: turn up to 3 cards
			int lim = ddSize<3 ? ddSize : 3;
			for(int i=0;i<lim;i++) {
				Card c = downDrawPile.getPile().get(0);
				drawPile.addFaceUp(c);
				downDrawPile.getPile().remove(0);
			}
		}
		
		// adjacent cards
		public static boolean adjacentCards(Card lowerCard, Card higherCard, String rankListName) {
			String[] rankList = CardGame.getCardRankList("aceToKing");
			if(lowerCard.rankIndex(rankList)+1 == higherCard.rankIndex(rankList))
				return true;
			else
				return false;
		}
		
		// adjacent alternating colors, note this does not wrap around
		public static boolean adjacentAltColors(Card lowerCard, Card higherCard, String rankListName) {
			if(adjacentCards(lowerCard, higherCard, "aceToKing") && lowerCard.oppositeColor(higherCard))
				return true;
			else
				return false;
			
		}
		
		// adjacent same suit
		public static boolean adjacentSameSuit(Card lowerCard, Card higherCard, String rankListName) {
			if(adjacentCards(lowerCard, higherCard, "aceToKing") && lowerCard.sameSuit(higherCard))
				return true;
			else
				return false;
		}
		
		// move some cards from one pile to another (common Solitaire need) T/F success
		public static boolean movePartialPileToPile(Pile startPile, Pile endPile, int maxIndex) {
			int sLen=startPile.size();
			if(sLen==0 || sLen<maxIndex)
				return false;
			
			for(int i=maxIndex;i>=0;i--) {
				Card c = startPile.getPile().get(i);
				startPile.getPile().remove(i);
				endPile.getPile().add(0,c);
			}
			return true;
		}
		
		// add card to next allowed AcePile in aceboard, T/F success
		public static boolean addCardToAces(Card c, List<AcePile> aceBoard) {
			boolean addQ=false;
			for(int i=0;i<aceBoard.size();i++) {
				addQ = aceBoard.get(i).addFaceUp(c);
				if(addQ) 
					break;
			}
			return addQ;
		}
	
	// common members
	protected Pile downDrawPile;
	protected Pile drawPile;
	protected List<Pile> gameBoard;
	protected List<AcePile> aceBoard;
	protected boolean isWon = false;
	protected int nBoardPiles;
	protected int nDecks;
	protected int nJokers; // not necessary for most Solitaire but might be useful later
	
	// common accessors
	// I took out accessor to DeckMaker because I don't really need it
	@Override
	public Pile getDownDrawPile() { return downDrawPile; }
	@Override
	public Pile getDrawPile() { return drawPile; }
	@Override
	public List<Pile> getGameBoard() { return gameBoard; }
	@Override
	public List<AcePile> getAceBoard() { return aceBoard; }
	@Override
	public boolean getWon() { return isWon; }
	
	public Solitaire() {}
	
	// constructor for cloning (deep copy), can't be private (need inheritance here)
	protected Solitaire(Solitaire original) {
		//deck = new DeckMaker(); // doesn't store values, just methods, so just making a new one is fine
		downDrawPile = original.downDrawPile.clone();
		drawPile = original.drawPile.clone();
		isWon = original.isWon;
		
		int len=original.getGameBoard().size();
		List<Pile> newGameBoard = new ArrayList<Pile>();
		for(int i=0;i<len;i++) {
			newGameBoard.add(original.getGameBoard().get(i).clone());
		}
		gameBoard=newGameBoard;
		
		List<AcePile> newAce = new ArrayList<AcePile>();
		len=original.getAceBoard().size();
		for(int i=0;i<len;i++) {
			newAce.add(original.getAceBoard().get(i).clone());
		}
		aceBoard=newAce;
	}
	
	// given the above constructor, I hope I can make these easily for each game: clone() and cloneInterface()
		
	
	// note: all of these below are overridden by subclasses (except nextThree()!). I'm not sure if Solitaire should be 
	// abstract considering I'm trying to keep the common deep copy constructor.

	@Override
	public boolean moveBoardToBoard(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return false;
	}
	
	// this one is defined here; everything else is null
	@Override
	public void nextThree() {
		Solitaire.nextThree(downDrawPile, drawPile);
	}

	@Override
	public boolean moveDrawToBoard(int toIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveDrawToAces() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override // same as Klondike...can this be moved to Solitaire?
	public boolean moveBoardToAces(int fromIndex) {
		if(gameBoard.get(fromIndex).size()==0)
			return false;
		Card c = gameBoard.get(fromIndex).getPile().get(0);
		boolean out = Solitaire.addCardToAces(c, aceBoard);
		if(out){
			gameBoard.get(fromIndex).getPile().remove(0);
			if(gameBoard.get(fromIndex).size()>0)
				gameBoard.get(fromIndex).getPile().get(0).faceUp = true; // flip new top card
			}
		return out;
	}
	
	public boolean autoPlayToAceBoard() {
		boolean changeQ=false, again=true, c2=true;
		while(again) {
			again=false;
			for(int i=0;i<gameBoard.size();i++) {
				c2=true;
				while(c2) {
					c2 = moveBoardToAces(i);
					if(c2) {
						changeQ=true;
						again=true;
					}
				}
			}
		}
		return changeQ;
	}
	
	@Override
	public boolean doDraw(int fromPile, int nCard) { 
		// TODO Auto-generated method stub
		return false; 
	}

	@Override
	public SolitaireGame cloneInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkWin() {
		// TODO Auto-generated method stub
		boolean winQ=true;
		int aceLen=aceBoard.size();
		for(int i=0;i<aceLen;i++) {
			if(aceBoard.get(i).size()==0){
				winQ=false;
				break;
			}
			Card c = aceBoard.get(i).getPile().get(0);
			if(!c.getRank().equalsIgnoreCase("K")) {
				winQ=false;
			}
		}
		return winQ;
	}

}
