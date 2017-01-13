package solitaire;

import java.util.List;

import cardGroups.*;

public class SolitaireWrapper implements SolitaireGame {
	private SolitaireGame g;
	private SolitaireGame g_start;
	private SolitaireGame g_buffer;
	private SolitaireGame g_prev;
	
	// constructor
	public SolitaireWrapper(SolitaireGame game) {
		g = game;
		g_start = g.cloneInterface();
		g_buffer = g.cloneInterface();
		g_prev = g.cloneInterface();
	}
	
	@Override
	public boolean moveBoardToBoard(int fromIndex, int toIndex) {
		g_buffer = g.cloneInterface();
		boolean result = this.g.moveBoardToBoard(fromIndex, toIndex);
		if(result)
			g_prev = g_buffer;
		return result;
	}
	@Override
	public void nextThree() {
		g_prev = g.cloneInterface();
		g.nextThree();
	}
	@Override
	public boolean moveDrawToBoard(int toIndex) {
		g_buffer = g.cloneInterface();
		boolean result = this.g.moveDrawToBoard(toIndex);
		if(result)
			g_prev = g_buffer;
		return result;
	}
	@Override
	public boolean moveDrawToAces() {
		g_buffer = g.cloneInterface();
		boolean result = this.g.moveDrawToAces();
		if(result)
			g_prev = g_buffer;
		return result;
	}
	@Override
	public boolean moveBoardToAces(int fromIndex) {
		g_buffer = g.cloneInterface();
		boolean result = this.g.moveBoardToAces(fromIndex);
		if(result)
			g_prev = g_buffer;
		return result;
	}
	
	@Override
	public boolean doDraw(int fromPile, int nCard) {
		g_buffer=g.cloneInterface();
		boolean result = this.g.doDraw(fromPile, nCard);
		if(result)
			g_prev = g_buffer;
		return result;
	}
	
	// additional functionality: back up one move, and start over
	public void startOver() {
		g = g_start.cloneInterface();
		g_buffer = g_start.cloneInterface();
		g_prev = g_start.cloneInterface();
	}
	public void undoLastMove() {
		g = g_prev.cloneInterface();
		g_buffer = g_prev.cloneInterface();
	}
	
	// inherited accessors

	@Override
	public Pile getDownDrawPile() {
		return g.getDownDrawPile();
	}
	@Override
	public Pile getDrawPile() {
		return g.getDrawPile();
	}
	@Override
	public List<Pile> getGameBoard() {
		return g.getGameBoard();
	}
	@Override
	public List<AcePile> getAceBoard() {
		return g.getAceBoard();
	}
	@Override
	public boolean getWon() {
		return g.getWon();
	}
	@Override
	public SolitaireGame cloneInterface() {
		return g.cloneInterface();
	}

	@Override
	public boolean autoPlayToAceBoard() {
		return g.autoPlayToAceBoard();
	}

	@Override
	public boolean checkWin() {
		return g.checkWin();
	}

}
