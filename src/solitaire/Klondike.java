package solitaire;
//import java.util.ArrayList;
//import java.util.List;

//import decks.*;
import cards.*;
import cardGroups.*;

public class Klondike extends Solitaire {
	
	
	public Klondike() {
		super(); // doesn't do anything
		nDecks=1;
		nJokers=0;
		nBoardPiles=7;
		downDrawPile = new Pile(Card.makeDeck(nDecks, nJokers)); // 1 deck, no jokers
		downDrawPile.shuffle();
		gameBoard = Solitaire.buildBoard(nBoardPiles); // number of piles
		aceBoard = Solitaire.buildAceBoard(nDecks); // number of decks
		this.deal();
		drawPile = new Pile();
	}
	
	// constructor for cloning (deep copy): see Solitaire (superclass) for details
	private Klondike(Klondike original) {
		super(original);
	}
	
	@Override
	public Klondike clone() {
		return new Klondike(this);
	}
	@Override
	public SolitaireGame cloneInterface() {
		return (SolitaireGame) this.clone();
	}
	
	public void deal() {
		for(int i=0;i<nBoardPiles;i++) {
			for(int j=i;j<nBoardPiles;j++) {
				Card c = downDrawPile.draw();
				if(i==j)
					gameBoard.get(j).addFaceUp(c);
				else
					gameBoard.get(j).addFaceDown(c);
			}
		}
	}
	
	// moves all faceup cards on one sevenBoard pile to another, returns whether legal or no
	@Override
	public boolean moveBoardToBoard(int fromIndex, int toIndex) {
		if( !this.checkSevenToSeven(fromIndex, toIndex) )
			return false;
		
		int len = gameBoard.get(fromIndex).size();
		int firstFaceUp = -1;
		for(int i=0;i<len;i++) {
			if(gameBoard.get(fromIndex).getPile().get(i).faceUp)
				firstFaceUp++;
			else
				break;
		}
		
		// public static boolean movePartialPileToPile(Pile startPile, Pile endPile, int maxIndex) 
		boolean moveQ = Solitaire.movePartialPileToPile(gameBoard.get(fromIndex), 
				gameBoard.get(toIndex), firstFaceUp);
		if(moveQ && gameBoard.get(fromIndex).size()>0)
			gameBoard.get(fromIndex).getPile().get(0).faceUp=true;
		
		return moveQ;
	}
	
	// is move (faceup cards from one 7-board index to another) legal?
	private boolean checkSevenToSeven(int fromIndex, int toIndex) {
		
		// case 1: can't move empty pile
		if(this.gameBoard.get(fromIndex).size()==0)
			return false;
		// case 2: check card compatibility
		Card lowerCard = firstFaceUp(fromIndex);
		//Solitaire.firstFaceUp();
		return (this.checkCardToSevens(lowerCard, toIndex));
	}
	
	private Card firstFaceUp(int pileIndex) {
		Pile p = gameBoard.get(pileIndex);
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
	
	
	// public static boolean adjacentAltColors(Card lowerCard, Card higherCard, String gameName)
	private boolean checkCardToSevens(Card lowerCard, int toIndex) {
	
		// case 1: can only move king TO empty pile
		if(this.gameBoard.get(toIndex).size()==0) {
			if(lowerCard.getRank().equalsIgnoreCase("K"))
				return true;
			else
				return false;
		}
		// case 2: card compatibility: opposite color and adjacent rank
		Card higherCard = gameBoard.get(toIndex).getPile().get(0);
		return Solitaire.adjacentAltColors(lowerCard, higherCard, "Klondike");
	}
	
	@Override
	public boolean moveDrawToBoard(int toIndex) {
		if(drawPile.size()==0)
			return false;
		Card c = drawPile.getPile().get(0);
		boolean check = this.checkCardToSevens(c, toIndex);
		if(!check) 
			return false;
		drawPile.getPile().remove(0);
		this.gameBoard.get(toIndex).getPile().add(0,c);
		
		return true;
	}
	
	@Override
	public boolean moveDrawToAces() {
		Card c = drawPile.getPile().get(0);
		boolean out = Solitaire.addCardToAces(c, aceBoard);
		if(out) 
			drawPile.getPile().remove(0);
		return out;
	}
}
