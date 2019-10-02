package dev.links;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.select.Elements;

/**
 * 
 * @author Jack Patton
 * @version 0.0.1
 * Description: Does all link handling for making the link directory
 *
 */
public class LinkManager {
	
	// TODO 0.0.2 Make a start over or continue choice
	
	// Variables
	private String path;
	private BufferedWriter writer;
	private int docId;
	private Queue<String> queue = new LinkedList<>();
	
	/**
	 * 
	 * @param path
	 * Constructor
	 * @throws IOException 
	 */
	public LinkManager(String path) throws IOException{
		this.path = path;
		this.writer = new BufferedWriter(new FileWriter("links.txt"));
		this.docId = 0;
		queue.add(path);
	}
	
	/**
	 * 
	 * @throws IOException
	 * Runs the bot, writing links to the file
	 */
	public void run() throws IOException{
		
		// Write the first Link
		writer.write(docId + " " + path);
		writer.flush();
		docId++;
		
		// Write the consecutive Links
		while(!(queue.isEmpty())){
			getLinks(path);
			queue.remove();
		}
		writer.close();
	}
	
	/**
	 * 
	 * @param path
	 * @throws IOException
	 * Gets the links of any given file 
	 */
	private void getLinks(String path) throws IOException{
		
		// Gets response and makes document for first link
		Document doc = Jsoup.connect(path).timeout(100000).validateTLSCertificates(false).ignoreHttpErrors(true).get();
		doc.outputSettings().escapeMode(EscapeMode.extended);
		Elements links = doc.select("a[href]");
		
		// Loops through each link
		for(Element link : links){
			
			// If the queue does not contain this link
			if(!queue.contains(link.attr("abs:href"))){
				Connection.Response resp2 = Jsoup.connect(link.attr("abs:href")).timeout(100000).validateTLSCertificates(false).ignoreHttpErrors(true).execute();
				
				// Checks if content type exists and is text
				if((!(resp2.contentType() == null)) && (resp2.contentType().contains("text/html"))){
					
					// Adds link to the queue
					queue.add(link.attr("abs:href"));
					
					// writes docId and link
					writer.write("\n" + docId + " " + link.attr("abs:href"));
					writer.flush();
					System.out.println("written");
					docId++;
				}
			}
		}
		
	}
	
}
