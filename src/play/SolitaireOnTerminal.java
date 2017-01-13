package play;

import java.util.List;
import java.util.Scanner;

import cardGroups.*;
import solitaire.SolitaireGame;
import solitaire.SolitaireWrapper;

public class SolitaireOnTerminal extends GameOnTerminal {
	
	public SolitaireWrapper gameWrap;
	public boolean quit;
	
	// constructors
	public SolitaireOnTerminal(SolitaireWrapper g) {
		gameWrap = g;
	}
	
	public SolitaireOnTerminal(SolitaireGame g) {
		gameWrap = new SolitaireWrapper(g);
	}
	
	// display
	public void showMainBoard() {
		List<Pile> b = gameWrap.getGameBoard();
		showBoardBackwards(b);
	}
	
	public void showAceBoard() {
		List<AcePile> b = gameWrap.getAceBoard();
		showAceBoard(b);
	}
	
	public void showDrawCards() {
		Pile b = gameWrap.getDrawPile();
		showPile(b);
	}
	
	public void nextMove() {
		Scanner in=new Scanner(System.in);
		int i=0; 
		while( !quit && (i<1 || i>11) ) {
			System.out.println("What is your next move? \n"
					+ "1 - Move Board Index (0-9) to Board Index (0-9)\t\t"
					+ "2 - Move Board Index (0-9) to Ace Index\n"
					+ "3 - Draw to Board Index(0-9)\t\t"
					+ "4 - Draw to Ace Index\n"
					+ "5 - Deal 3\t\t"
					+ "6 - Draw Card\t\t"
					+ "7 - Undo last move\t\t"
					+ "8 - Start game over\t\t"
					+ "9 - Quit\n"
					+ "10 - Auto-Play to Ace Board");
			i = getInt(in);
		}
		
		switch(i) {
		case 1 :
			this.moveBoardToBoard(in);
			break;
			
		case 2 :
			
			moveBoardToAce(in);
			break;
			
		case 3 :
			moveDrawToBoard(in);
			break;
			
		case 4 :
			this.moveDrawToAces();
			break;
			
		case 5 :
			gameWrap.nextThree();
			break;
			
		case 6 :
			this.doDraw(in);
			break;
			
		case 7 : 
			gameWrap.undoLastMove();
			break;
		case 8 :
			gameWrap.startOver();
			break;
		case 9 : 
			quit=true;
			break;
		case 10 :
			gameWrap.autoPlayToAceBoard();
		}
		
	}
	
	private void doDraw(Scanner in) {
		int fromIndex, nCard;
		int i=-1;
		while( i<0 || i>16) {
			System.out.println("Enter Board Index (0-15) of origin cards");
			i = getInt(in);
		}
		fromIndex=i;
		i=-1;
		int nCards = this.gameWrap.getGameBoard().get(fromIndex).size();
		while( i<0 || i>=nCards) {
			System.out.println("Enter index of card to draw");
			i = getInt(in);
		}
		nCard=i;
		boolean result = gameWrap.doDraw(fromIndex, nCard);
		if(!result)
			System.out.println("Error: this move was not possible.");
	}
	
	private void moveBoardToBoard(Scanner in) {
		int fromIndex, toIndex;
		int i=-1;
		int boardSize = this.gameWrap.getGameBoard().size();
		while( i<0 || i>=boardSize) {
			System.out.println("Enter Board Index (0-9) of origin cards");
			i = getInt(in);
		}
		fromIndex=i;
		i=-1;
		while( i<0 || i>=boardSize) {
			System.out.println("Enter Board Index (0-9) of destination");
			i = getInt(in);
		}
		toIndex=i;
		boolean result = gameWrap.moveBoardToBoard(fromIndex, toIndex);
		if(!result)
			System.out.println("Error: this move was not possible.");
	}
	
	private void moveBoardToAce(Scanner in) {
		int fromIndex;
		int i=-1;
		int boardSize=this.gameWrap.getGameBoard().size();
		while( i<0 || i>=boardSize) {
			System.out.println("Enter Board Index (0-9) of origin cards");
			i = getInt(in);
		}
		fromIndex=i;
		boolean result = gameWrap.moveBoardToAces(fromIndex);
		if(!result)
			System.out.println("Error: this move was not possible.");
	}
	
	private void moveDrawToBoard(Scanner in) {
		int fromIndex;
		int i=-1;
		int boardSize=this.gameWrap.getGameBoard().size();
		while( i<0 || i>=boardSize) {
			System.out.println("Enter Board Index (0-9) of destination");
			i = getInt(in);
		}
		fromIndex=i;
		boolean result = gameWrap.moveDrawToBoard(fromIndex);
		if(!result)
			System.out.println("Error: this move was not possible.");
	}
	
	private void moveDrawToAces() {
		boolean result = gameWrap.moveDrawToAces();
		if(!result)
			System.out.println("Error: this move was not possible.");
	}

}
