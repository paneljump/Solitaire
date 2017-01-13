package cards;

/*
 * This is a generic "pile" for CardLike objects...I wrote it as generic, which might not be 
 * entirely appropriate (there is no type checking when I cast from CardLike to T, see addFaceUp 
 * and addFaceDown) 
 * 
 * Note it has no special constructors/cloning (I'm doing that in subclasses)
 * 
 * shuffle(), cut(), 
 * addFaceUp(), addFaceDown(), addCard(), 
 * draw(int i), draw() (draws from top (0)), blindDraw() (takes one randomly)
 * discardAll()
 * addCardList(T list) (adds all elements to existing list)
 * 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cards.CardLike;

public class GenPile<T> {
	
	protected List<T> aPile;
	protected String playerName; 
	
	public GenPile(List<T> l) {
		this.aPile = l;
		playerName = "";
	}
	
	public String getPlayerName() { return playerName; }
	public void setPlayerName(String name) { this.playerName = name; }
	
	public List<T> getPile() { return aPile; }
	public void setPile(List<T> p) { this.aPile=p; }
	
	public int size() { return aPile.size(); }
	
	public GenPile() {
		this.aPile = new ArrayList<>();
		playerName = "";
	}
	
	// @Override
	public boolean contains(T t) {
		if(aPile.contains(t))
			return true;
		else
			return false;
	}
	
	// can be overridden by subclasses with restrictions
	public boolean addFaceUp(CardLike card) {
		return addFaceUp(card,0);
	}
	public boolean addFaceUp(CardLike card, int index) {
		card.setFaceUp(true);
		aPile.add(index,(T)card);
		return true;
	}
	
	// can be overridden by subclasses with restrictions
	public boolean addFaceDown(CardLike card) {
		return addFaceDown(card,0);
	}
	public boolean addFaceDown(CardLike card, int index) {
		card.setFaceUp(false);
		aPile.add(index,(T)card);
		return true;
	}
	
	// can be overridden by subclasses with restrictions
	public boolean addCard(T card) {
		return addCard(0,card);
	}
	public boolean addCard(int index, T card) {
		aPile.add(index,card);
		return true;
	}
	
	public T draw(int i) {
		T c = this.aPile.get(i);
		this.aPile.remove(i);
		return c;
	}
	
	public T draw() {
		return this.draw(0);
	}
	
	public T blindDraw() {
		Random rnd = new Random();
		int drawn = rnd.nextInt(aPile.size());
		T out = null;
		for(int i=0;i<aPile.size();i++ ) {
			if(i==drawn) {
				out = aPile.get(i);
				aPile.remove(i);
				break;
			}
		}
		return out;
	}
	
	public List<T> discardAll() {
		List<T> out = aPile;
		aPile = new ArrayList<>();
		return out;
	}
	
	public void addCardList(List<T> list) {
		for(int i=0;i<list.size();i++)
			aPile.add(0,list.get(i));
	}
	
	
	// shuffle functions:
	// swap 2 cards
	public void swap(List<T> list, int i, int j) {
		T tmp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, tmp);		}
		
	// modified from method in Collections class
	public void cardListShuffle(List<T> aList) {
		Random rnd = new Random();
		for (int i=aList.size() ; i>1 ; i--)
			swap(aList, i-1, rnd.nextInt(i));
	}
	
	public void shuffle() {
		cardListShuffle(aPile);
	}
	
	public void cut() {
		Random rnd = new Random();
		int deckSize=this.aPile.size();
		int cutAt = 0;
		while(cutAt==0 || cutAt==deckSize)
			cutAt = rnd.nextInt(deckSize);
		List<T> cutDeck = new ArrayList<T>();
		for(int i=cutAt;i<deckSize;i++ )
			cutDeck.add(this.aPile.get(i));
		for(int i=0;i<cutAt;i++)
			cutDeck.add(this.aPile.get(i));
		
		this.aPile = cutDeck;
	}

}
