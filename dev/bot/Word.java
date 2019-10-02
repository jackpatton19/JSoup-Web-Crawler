package dev.bot;

/**
 * 
 * @author Jack Patton
 * @version 0.0.1
 * Description: stores a word object with number of times found in text
 *
 */
public class Word {
	
	// Variables
	@SuppressWarnings("unused")
	private String word, path;
	private int num = 1;
	private int docId;
	
	/**
	 * 
	 * @param word
	 * Constructor
	 */
	public Word(String word, String path, int docId){
		this.word = word;
		this.path = path;
		this.docId = docId;
	}
	
	/**
	 * 
	 * @param word
	 * Constructor 2
	 */
	public Word(String word){
		this.word = word;
	}
	
	/**
	 * Adds one to the num
	 */
	public void add(){
		num += 1;
	}
	
	// GETTERS SETTERS =========================
	
	public String getWord(){
		return word.toLowerCase();
	}
	
	public int getNum(){
		return num;
	}
	
	public int getDocId(){
		return docId;
	}
	
	public void setDocId(int val){
		this.docId = val;
	}
	
}
