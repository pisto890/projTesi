package it.unimi.dico.islab.proj739058.test;


import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


public class Prova {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static void main(String[] args) throws IOException, SAXException {

			Scanner in = new Scanner(System.in);
			String search = in.nextLine();
			String link = "http://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="+search+"&srprop=timestamp&format=xml";
			StaffSax handler = new StaffSax();
			
		    XMLReader myReader = XMLReaderFactory.createXMLReader();
		    myReader.setContentHandler(handler);
		    URL url = new URL(link);
		    myReader.parse(new InputSource(url.openStream()));
		    for (String s : handler.results){
		    	System.out.println(s.replace(" ", "_"));
		    }
		    
	}

}
