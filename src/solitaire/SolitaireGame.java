package solitaire;

import java.util.List;

import cardGroups.*;

public interface SolitaireGame {
	
	public boolean moveBoardToBoard(int fromIndex, int toIndex);
	public void nextThree();
	public boolean moveDrawToBoard(int toIndex);
	public boolean moveDrawToAces();
	public boolean moveBoardToAces(int fromIndex);
	public boolean doDraw(int fromPile, int nCard);
	public boolean autoPlayToAceBoard();
	public boolean checkWin();
	
	//public Deck getDeck();
	public Pile getDownDrawPile();
	public Pile getDrawPile();
	public List<Pile> getGameBoard();
	public List<AcePile> getAceBoard();
	public boolean getWon();
	
	public SolitaireGame cloneInterface();

}
