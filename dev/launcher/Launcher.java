package dev.launcher;

import java.io.IOException;

import dev.bot.Bot;
import dev.links.LinkManager;

/**
 * 
 * @author Jack Patton
 * @version 0.0.1
 * Description: Client program for the bot
 *
 */
public class Launcher {
	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 * Description: Initializes the bot, and runs it with a given link
	 */
	public static void main(String[] args) throws IOException{
		
		// Makes a linkManager to update all documents and their IDs
		//LinkManager lm = new LinkManager("https://en.wikipedia.org/wiki/Main_Page");
		//lm.run();
		
		// Makes the bot to parse them and runs it
		Bot b = new Bot("https://en.wikipedia.org/wiki/Main_Page");
		b.run();
	}
	
}
