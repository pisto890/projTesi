package it.unimi.dico.islab.proj739058.test;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;


import com.google.gson.Gson;

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
	}

}
