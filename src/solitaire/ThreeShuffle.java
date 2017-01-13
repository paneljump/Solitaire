package solitaire;

import java.util.ArrayList;
import java.util.List;

import cardGroups.Pile;
import cards.Card;



public class ThreeShuffle extends Solitaire {
	
	protected int nShuffle;
	protected boolean draw;
	
	// to undo draws/shuffles, need to decrement IF they were the most recent
	boolean lastShuffle=false;
	boolean lastDraw=false;
	
	public ThreeShuffle() {
		super(); // doesn't do anything
		nDecks=1;
		nJokers=0;
		downDrawPile = new Pile(Card.makeDeck(nDecks, nJokers)); // 1 deck, no jokers
		gameBoard = Solitaire.buildBoard(16); // number of piles
		aceBoard = Solitaire.buildAceBoard(nDecks); // number of decks
		drawPile = new Pile();
		
		nShuffle=0;
		draw=false;
		
		this.deal();
	}
	
	// constructor for cloning (deep copy): see Solitaire (superclass) for details
	private ThreeShuffle(ThreeShuffle original) {
		super(original);
		//nDecks=original.nDecks;
		//nJokers=original.nJokers;
		nShuffle=original.nShuffle;
		draw=original.draw;
	}
	
	@Override
	public ThreeShuffle clone() {
		return new ThreeShuffle(this);
	}
	@Override
	public SolitaireGame cloneInterface() {
		return (SolitaireGame) this.clone();
	}
	
	public void deal() {
		String[] suits = Card.getSuits();
		for(int i=0;i<1;i++) {
			for(int j=0;j<4;j++) {
				Card c = new Card(suits[j],"A");
				aceBoard.get( (4*i)+j ).addFaceUp(c);
			}
		}
		downDrawPile.removeRank("A");
		this.dealBoard();
	}
	
	private void collectBoard() {
		int boardSize = gameBoard.size();
		for(int i=0;i<boardSize; i++) {
			List<Card> l = gameBoard.get(i).getPile();
			int size = l.size();
			if( size > 0 ) {
				for(int j=0;j<size;j++) {
					Card c = l.get(0);
					downDrawPile.getPile().add(c);
					l.remove(0);
				}
			}
		}
	}
	
	private void dealBoard() {
		this.collectBoard();
		
		downDrawPile.shuffle();
		for(int i=0;i<gameBoard.size();i++) {
			for(int j=0;j<3;j++) {
				if(downDrawPile.size()==0)
					break;
				Card c = downDrawPile.getPile().get(0);
				c.faceUp=true;
				downDrawPile.getPile().remove(0);
				gameBoard.get(i).getPile().add(0,c);
			}
		}
		this.nShuffle++;
		this.lastShuffle=true;
		this.lastDraw=false;
	}
	
	public boolean doDraw(int fromPile, int nCard) {
		if(draw)
			return false;
		int len = gameBoard.get(fromPile).size();
		if(len==0 || len<nCard-1)
			return false;
		Card c = gameBoard.get(fromPile).getPile().get(nCard);
		gameBoard.get(fromPile).getPile().remove(nCard);
		gameBoard.get(fromPile).getPile().add(0,c);
		draw=true;
		lastDraw=true; 
		lastShuffle=false;
		return true;
	}
	
	// This game doesn't have a flip pile so I'm using this function to re-deal
	@Override
	public void nextThree() {
		if(nShuffle>=3)
			return;
		dealBoard();
	}
	
	// need to add toggle lastDraw/lastShuffle, for undo functions
	public boolean moveBoardToAces(int fromIndex) {
		boolean out = super.moveBoardToAces(fromIndex);
		if(out){
			lastDraw=lastShuffle=false;
			}
		return out;
	}
	
	// moves top card from one pile to another, no jokers
	@Override
	public boolean moveBoardToBoard(int fromIndex, int toIndex) {
		// both piles need cards
		if(gameBoard.get(fromIndex).size()==0 || gameBoard.get(toIndex).size()==0)
			return false;
		
		Card lowerCard = gameBoard.get(fromIndex).getPile().get(0);
		Card higherCard = gameBoard.get(toIndex).getPile().get(0);
		
		boolean moveQ = Solitaire.adjacentSameSuit(lowerCard, higherCard, "aceToKing");
		if(moveQ) {
			gameBoard.get(fromIndex).getPile().remove(0);
			gameBoard.get(toIndex).getPile().add(0,lowerCard);
			lastShuffle=lastDraw=false;
		}
		return moveQ;
	}
}
