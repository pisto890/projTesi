package it.unimi.dico.islab.proj739058.test;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;


import com.google.gson.Gson;

public class Prova {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		    String google = "http://ajax.googleapis.com/ajax/services/search/web?num=1&v=1.0&q=";
		    String search = "calcio wikipedia";
		    String charset = "UTF-8";

		    URL url = new URL(google + URLEncoder.encode(search, charset) + "&gl=it");
		    Reader reader = new InputStreamReader(url.openStream(), charset);
		    GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

		    // Show URL of 1st result.
		    System.out.println(results.getResponseData().getResults().get(1).getUrl().replace("en","it"));


	}

}
