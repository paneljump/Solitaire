package solitaire;

// import java.util.ArrayList;
// import java.util.List;

// import decks.AcePile;
import cards.*;
import games.*;
import cardGroups.*;

public class UpsideDownPyramid extends Solitaire {
	
	public UpsideDownPyramid() {
		super(); // shouldn't do anything
		nDecks=2;
		nJokers=0;
		nBoardPiles=10;
		downDrawPile = new Pile( Card.makeDeck(2,0) ); // 2 decks, no jokers
		downDrawPile.shuffle();
		gameBoard = Solitaire.buildBoard(nBoardPiles); // number of piles
		aceBoard = Solitaire.buildAceBoard(nDecks); // number of decks
		this.deal();
		drawPile = new Pile();
	}
	
	// constructor for cloning
	
	private UpsideDownPyramid(UpsideDownPyramid original) {
		super(original);
	}
	
	
	@Override
	public UpsideDownPyramid clone() { return new UpsideDownPyramid(this); }
	@Override
	public SolitaireGame cloneInterface() { return (SolitaireGame) this.clone(); }
	
	public void deal() {
		downDrawPile.removeRank("A");
		String[] suits = Card.getSuits();
		for(int i=0;i<nDecks;i++) {
			for(int j=0;j<4;j++) {
				Card c = new Card(suits[j],"A");
				aceBoard.get( (4*i)+j ).addFaceUp(c);
			}
		}
		int n=1;
		for(int i=0;i<6;i++) {
			for(int j=0;j<n;j++) {
				Card c = downDrawPile.draw();
				gameBoard.get(i).addFaceUp(c);
			}
			n+=2;
		}
		n=8;
		for(int i=6;i<10;i++) {
			for(int j=0;j<n;j++) {
				Card c = downDrawPile.draw();
				gameBoard.get(i).addFaceUp(c);
			}
			n-=2;
		}
	}
	
	// because we have to move all adjacent cards at once
	private int findMaxAdjacentCardIndex(Pile p) {
		int len=p.size();
		if(len==0)
			return -1;
		
		int out=0;
		boolean nextMatch=true;
		while(out<len-1 && nextMatch) {
			Card lowerCard = p.getPile().get(out);
			Card higherCard = p.getPile().get(out+1);
			nextMatch = Solitaire.adjacentAltColors(lowerCard, higherCard, "UpsideDownPyramid");
			if(nextMatch)
				out++;
		}
		
		return out;
	}
	
	@Override
	public boolean moveBoardToBoard(int fromIndex, int toIndex) {
		// case 1: pile to be moved is empty
		if(gameBoard.get(fromIndex).size()==0)
			return false;
		// case 2: destination pile is empty (can only take a king)
		int maxIndex = findMaxAdjacentCardIndex(gameBoard.get(fromIndex));
		Card lowerCard = gameBoard.get(fromIndex).getPile().get(maxIndex);
		if(gameBoard.get(toIndex).size()==0) {
			if(lowerCard.getRank().equalsIgnoreCase("K")) {
				boolean moveQ = Solitaire.movePartialPileToPile(gameBoard.get(fromIndex), 
						gameBoard.get(toIndex), maxIndex);
				return moveQ;
			}
			else
				return false;
		}
		// case 3: destination pile has top card
		Card higherCard = gameBoard.get(toIndex).getPile().get(0);
		boolean moveQ = Solitaire.adjacentAltColors(lowerCard, higherCard, "aceToKing");
		if(moveQ) {
			moveQ = Solitaire.movePartialPileToPile(gameBoard.get(fromIndex), 
					gameBoard.get(toIndex), maxIndex);
		}
		return moveQ;
	}
	/* in Solitaire
	@Override
	public boolean moveBoardToAces(int boardIndex) {
		if(gameBoard.get(boardIndex).aPile.size()==0)
			return false;
		Card c = gameBoard.get(boardIndex).aPile.get(0);
		boolean moveQ = Solitaire.addCardToAces(c, aceBoard);
		if(moveQ)
			gameBoard.get(boardIndex).aPile.remove(0);
		return moveQ;
	}
	*/
	
	@Override
	public boolean moveDrawToBoard(int boardIndex) {
		if(this.drawPile.size()==0)
			return false;
		Card lowerCard = drawPile.getPile().get(0);
		
		// case 1: destination pile empty (can only take king)
		if(gameBoard.get(boardIndex).size()==0) {
			if(lowerCard.getRank().equalsIgnoreCase("K")) {
				gameBoard.get(boardIndex).getPile().add(0,lowerCard);
				drawPile.getPile().remove(0);
				return true;
			}
			else {
				return false;
			}
		}
		
		// case 2: destination pile has card(s)
		Card higherCard = gameBoard.get(boardIndex).getPile().get(0);
		boolean moveQ = Solitaire.adjacentAltColors(lowerCard, higherCard, "aceToKing");
		if(moveQ) {
			gameBoard.get(boardIndex).getPile().add(0,lowerCard);
			drawPile.getPile().remove(0);
			return true;
		}
		else
			return false;	
	}
	
	@Override
	public boolean moveDrawToAces() {
		if(this.drawPile.size()==0)
			return false;
		Card c = drawPile.getPile().get(0);
		boolean result = Solitaire.addCardToAces(c, aceBoard);
		if(result)
			drawPile.getPile().remove(0);
		return result;
	}

}
