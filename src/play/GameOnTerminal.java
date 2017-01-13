package play;

import java.util.List;
import java.util.Scanner;

import cardGroups.*;
import cards.Card;

public abstract class GameOnTerminal {
	
	public String display(Card c) {
		if(c.faceUp)
			if(c.getIsJoker())
				return "Joker";
			else
				return c.getRank() + " " + c.getSuit();
		else
			return "(facedown)";
	}
	
	public void showPile(Pile p) {
		for(int i=0;i<p.size();i++) 
			System.out.print(display(p.getPile().get(i)) + "  ");
		System.out.println("");
	}
	
	public void showPile(AcePile p) {
		for(int i=0;i<p.size();i++) 
			System.out.print(display(p.getPile().get(i)) + "  ");
		System.out.println("");
	}
	
	
	public void showPileBackwards(Pile p) {
		int len=p.size();
		for(int i=len-1;i>=0;i--) 
			System.out.print(display(p.getPile().get(i)) + "  ");
		System.out.println("");
	}

	// note: "board" usually separates game board from destination/ace board
	// this would be better as a generic
	public void showBoard(List<Pile> board) {
		int len=board.size();
		for(int i=0;i<len;i++) {
			System.out.print(i+".  ");
			this.showPile(board.get(i));
		}
	}
	
	public void showAceBoard(List<AcePile> board) {
		int len=board.size();
		for(int i=0;i<len;i++) {
			System.out.print(i+".  ");
			this.showPile(board.get(i));
		}
	}
	// for games like UpsideDownPyramid
	public void showBoardBackwards(List<Pile> board) {
		int len=board.size();
		for(int i=0;i<len;i++) {
			System.out.print(i+".  ");
			this.showPileBackwards(board.get(i));
		}
	}
	
	// nicked from MyStringReader and simplified
	public int getInt(Scanner in) {
        int i;
        String lastRead;
        try{
            lastRead=in.nextLine();
            i = Integer.parseInt(lastRead);
            return i;
        }
        catch(Exception e){
            e.getMessage();
            return -1;          // special value indicates an error
        }
    }

}
