package play;

import solitaire.*;

public class MyMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		 * To play on terminal, run here. Games supported are:
		 * UpsideDownPyramid()
		 * ThreeShuffle()
		 * Klondike()
		 * 
		 */
		
		SolitaireGame G = new UpsideDownPyramid();
		
		SolitaireOnTerminal UDPOT = new SolitaireOnTerminal(G);
		//KlondikeOnTerminal KOT = new KlondikeOnTerminal(K);
		while(!UDPOT.gameWrap.getWon() && !UDPOT.quit) {
			UDPOT.showDrawCards();
			System.out.println("");
			UDPOT.showAceBoard();
			System.out.println("");
			UDPOT.showMainBoard();
			UDPOT.nextMove();
		}
		UDPOT.showAceBoard();
		System.out.println("");
		UDPOT.showMainBoard();
		System.out.println("");
		

		

	}

}
