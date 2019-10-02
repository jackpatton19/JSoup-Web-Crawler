package dev.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import dev.bot.Word;

/**
 * 
 * @author Jack Patton
 * @version 0.0.1
 * Description: A manager for all the saving and database stuff
 *
 */
public class DbManager {
	
	// Variables
	private String folderName;
	private ArrayList<Word> words = new ArrayList<>();
	private ArrayList<Word> index = new ArrayList<>();
	
	// TODO 0.0.1 Make saving new words to arraylist
	// TODO 0.0.1 Make writing to folder and files
	// TODO 0.0.1 Do frequencies as well
	
	/**
	 * 
	 * @param folderName
	 */
	public DbManager(String folderName, ArrayList<Word> words){
		this.folderName = folderName;
		this.words = words;
	}
	
	/**
	 * 
	 * @param w
	 * @param docId
	 * @throws IOException
	 * Checks if word is in index, and if not, makes a new file with the information
	 */
	public void saveNewWord(ArrayList<Word> words) throws IOException{
		this.words = words;
		for(Word w : words){
			if(!isInIndex(w)){
				System.out.println(w.getWord() + " placed into the index");
				File f = new File("index/" + w.getWord().toLowerCase() + ".txt");
				BufferedWriter wr = new BufferedWriter(new FileWriter(new File(folderName + "/" + w.getWord().toLowerCase() + ".txt"), true));
				BufferedWriter ir = new BufferedWriter(new FileWriter("index.txt", true));		// SET TRUE TO START OVER, MUST DELETE EVERYTHING IN INDEX FILE BEFORE RUNNING IN TESTING
				ir.write(w.getWord().toLowerCase() + "\n");
				wr.write("doc:" + w.getDocId() + " num:" + w.getNum() + "\n");
				ir.flush();
				wr.flush();
			}else if(isInIndex(w)){
				BufferedWriter wr = new BufferedWriter(new FileWriter(folderName + "/" + w.getWord().toLowerCase() + ".txt", true));
				wr.write("doc:" + w.getDocId() + " num:" + w.getNum() + "\n");
				wr.close();
			}
		}
	}

	/**
	 * 
	 * @param w
	 * @return
	 * Returns true if a word is in the index
	 * Returns false if word is not in the index
	 */
	private boolean isInIndex(Word w){
		Scanner s = new Scanner("index.txt");
		boolean isInIndex = false;
		while(s.hasNextLine()){
			if(w.getWord().toLowerCase().equals(s.next())){
				isInIndex = true;
				break;
			}
		}
		return isInIndex;
		
	}
	
}
