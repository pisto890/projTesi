package it.unimi.dico.islab.proj739058.test;

import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Prova {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		Scanner in = new Scanner(System.in);
		String url = in.nextLine();
		for ( int i = 0 ; i < url.length() ; i++)
			if (url.charAt(i)==' ') {
				url = url.replace(' ', '_');
			}
		System.out.println(url);
		Document doc = Jsoup.connect("http://it.wikipedia.org/wiki/"+url).get();
		String contentDiv = doc.body().text();
		System.out.println(contentDiv);
		
	}

}
