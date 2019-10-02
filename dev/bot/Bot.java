package dev.bot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dev.db.DbManager;

/**
 * 
 * @author Jack Patton
 * @version 0.0.4
 * Description: Bot that takes in a link and gets all words and frequencies
 *
 */
public class Bot {
	
	// ==================================================================
	// TODO 0.0.4 Database manager working
	// TODO 0.0.4 Make file storage system / inverted Index
	// TODO 0.0.5 Use mysql to web host a database
	// TODO 0.0.5 Web Based Database system / inverted index
	// ==================================================================
	
	// Variables
	private Document doc;
	private String text;
	private ArrayList<Word> words;
	private char[] stopWords = {',', '.', '-', '(', ')', '!', '/', ':', ';', '"', '–', '·', '?', '>', '<', '|', '\''};
	private ArrayList<String> links = new ArrayList<>();
	private Scanner reader;
	private DbManager db;
	
	/**
	 * 
	 * @param path
	 * Constructor
	 * @throws FileNotFoundException 
	 */
	public Bot(String path) throws FileNotFoundException{
		words = new ArrayList<>();
		reader = new Scanner(new File("links.txt"));
		db = new DbManager("index", words);
		getLinks();
	}
	
	// =============================================================================================================
	/**
	 * Does heavy lifting for parsing and storing text
	 * @throws IOException 
	 */
	public void run() throws IOException{
		
		int num = 0;
		
		// Goes through links and does getWords on them
		for(String s: links){
			Connection.Response resp = Jsoup.connect(s).validateTLSCertificates(false).ignoreHttpErrors(true).timeout(100000).execute();
			if(resp.contentType().contains("text/html")){
				System.out.println("Doc " + num);
				doc = Jsoup.connect(s).validateTLSCertificates(false).ignoreHttpErrors(true).timeout(100000).get();
				text = doc.text();
				getWords(s, text, links.indexOf(s));
				num++;
			}
		}
	}
	
	private void getWords(String path, String text, int docId) throws IOException{
		String newText = text;
		words.clear();
		newText = cleanText(newText);
		Scanner s = new Scanner(newText);
		
		// Temp array for words to be added
		Word toAdd = new Word("", path, docId);
		
		// Gets the next word and clears the toAdd array
		String first = s.next().toLowerCase();
					
		// Adds the first word
		words.add(new Word(first.toLowerCase(), path, docId));
		
		// While there is still data left, checks words against current word
		while(s.hasNext()){
			
			// Gets the next word
			String next = s.next();
			boolean needToAdd = false;
			
			// Goes through words array and checks if it is already in there
			for(Word w : words){
				if(w.getWord().equals(next.toLowerCase())){
					w.add();
					needToAdd = false;
					break;
				}else{
					needToAdd = true;
				}
			}
			
			// Adds all words that weren't already in words
			if(needToAdd){
				toAdd = new Word(next, path, docId);
				words.add(toAdd);
			}
			
		}
		
		sort();
		db.saveNewWord(words);

		
		s.close();
	}
	// =================================================================================================
	
	/**
	 * Cleans the stop words and symbols from the text
	 */
	private String cleanText(String text){
		for(char s : stopWords){
			text = text.replace(s, ' ');
		}
		return text;
	}
	
	/**
	 * Sorts the arraylist by frequency
	 */
	private void sort(){
		for(int i = 0; i < words.size(); i++){
			for(int j = 0; j < words.size(); j++){
				if(words.get(j).getNum() > words.get(i).getNum()){
					Word big = words.get(j);
					words.set(j, words.get(i));
					words.set(i, big);
				}
			}
		}
	}
	
	/**
	 * Gets all the links from the file and puts in arraylist
	 */
	private void getLinks(){
		while(reader.hasNextLine()){
			int id = reader.nextInt();
			String link = reader.next();
			links.add(id, link);
		}
	}
}
