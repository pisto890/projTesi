package it.unimi.dico.islab.proj739058;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;

import it.unimi.dico.islab.idbs2.kc.KnowledgeChunk;
import it.unimi.dico.islab.idbs2.kc.Term;
import it.unimi.dico.islab.idbs2.kc.TermTransformation;
import it.unimi.dico.islab.idbs2.kc.session.KCSessionManager;
import it.unimi.dico.islab.textanalyzer.tools.CorpusAnalyzer;
import it.unimi.dico.islab.textanalyzer.tools.TermsDescriptor;
import it.unimi.dico.islab.textanalyzer.tools.TextAnalyzer;

/**
 * This class provides to create a term_equip
 * @author Isacco Borsani N°mat: 739058
 *
 */

public class TermEquip {

	
	@SuppressWarnings("unused")
	private CorpusAnalyzer a;
	
	/**
	 * @param CorpusAnalyzer ca
	 */
	
	public TermEquip(CorpusAnalyzer ca) {
		a = ca;
	}
	
	/**
	 * This method is used to populate term_eq relation inside KC-DB taking
	 * as input the CorpusAnalyzer created previously with "getCorpusAnalyzer" inside 
	 * TextManager class and boolean parameters to set up the analysis-text preferences.
	 * If stem param is true the method is used also to populate term_form relation inside 
	 * KC-DB.
	 * @param void
	 * @return void
	 */
	
	public void popTE(CorpusAnalyzer a) {
		
		boolean stem = false;
		boolean lem = false;
		
		if (a.isStemming()) stem = true;
		if (a.isLemmatization()) lem = true;
		
		a.disableStemLem();
		a.analyze();
		TextAnalyzer ta = new TextAnalyzer(a.getLanguage());
		
		if (stem)
			ta.enableStemming(); // Solo se è abilitato lo stemming
		
		if (lem)
			ta.enableLemmatization();
		
		Set<String> keys = a.getAllTextIDs(); //it takes all kc keys 
		TermsDescriptor occurrences = a.runOccurrences(); //SPOSTATO
		
		for (String k : keys) {
			KnowledgeChunk kc = KCSessionManager.kcm.getKnowledgeChunkById(k);
			Vector<String> v = a.getAnalyzedTextByID(k); //String vector of current kc
			TermsDescriptor TFIDF = a.getAnalyzedTFIDF(k); // it computes , for each term in kc , the tf-idf relevance
			for (String s : v) {
				//for-each term of analyzed text we calculate occurences and
				//tf-idf relevance
				Double occurrence = 0.0;
				Double relevance = 0.0;
				if(occurrences.containsKey(s))
					occurrence = occurrences.get(s);

				if (TFIDF.containsKey(s)) 
					relevance = TFIDF.get(s);
								
				kc.addTerm(s, relevance, occurrence.intValue());
				//Alternative forms
				if (stem) {
					Vector<String> alternatives = ta.analyzeText(s);
					//Per ogni forma alternata popolare la rispettiva tabella
						for (String altern : alternatives)
								kc.getTerm(s).addTransformation("Stemming", altern);
				}else if(lem) {
					Vector<String> alternatives = ta.analyzeText(s);
					//Per ogni forma alternata popolare la rispettiva tabella
						for (String altern : alternatives)
								kc.getTerm(s).addTransformation("Lemmatization", altern);
				}
			}
		}
		KCSessionManager.commitTransaction();
		if (stem) a.enableStemming();
		else if (lem) a.enableLemmatization();
	}
	
	/**
	 * This method enrich the term-equip of KnowledgeChunk a
	 * Is usefull to increase the matching-accuracy 
	 * when the input is composed by two short segments of text
	 * e.g: Apple pie vs Apple computer
	 * @param KnowledgeChunk a
	 * @param List<KnowledgeChunk> kcl
	 * @param CorpusAnalyzer c
	 * @throws Exception
	 */
	
	
	//Caso base
	public void enrichText(List<KnowledgeChunk> list , List<KnowledgeChunk> kcl , CorpusAnalyzer a, char type , int k) throws Exception {
		
		String google = "http://ajax.googleapis.com/ajax/services/search/web?num=1&v=1.0&q=";
		String search = "";
		String charset = "UTF-8";
						
		String enrichedText = "";
		
		Session s = KCSessionManager.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		
		//Creating new HashMap to restore a new termEquip and termForm with kc enriched
		TextManager tm = new TextManager();
		Map<String,String> m = tm.getTextbyProperties(kcl); // e se glielo passassi?
		Document doc = new Document("");
		
		List<List<Term>> l = new ArrayList();
		List<Term>li = new ArrayList();
		/*Getting terms SWITCH */
		
		//for-each kc of the kc that i want enrich (this is the base case)
		//Assume that "list" is a subset of "kcl"
		for (KnowledgeChunk kc : list) {
			
			switch(type) {
			
			case 'b':
				//Every term t
				li= s.createQuery("FROM Term t WHERE t.kc = '" + kc.getId() + "'").list();
				break;
			case 'a':
				//Top k-relevance-term
				Query q = s.createQuery("FROM Term t " +
						"WHERE t.kc = '" + kc.getId() + "' " +
						"ORDER BY t.relevance DESC ");
				q.setMaxResults(k);
				li = q.list();
				break;
				
			case 'h':
				//Only Hashtag : Twitter case study
				li = s.createQuery("FROM Term t " +
						"WHERE t.kc = '" + kc.getId() + "'" +
								" AND t.value LIKE '#%'").list();
				break;
			}
			if ( li.isEmpty()) System.out.println("ciao");
			l.add(li);
		}
		
		/*Delete old Term_equip & Term_form */
		
		@SuppressWarnings("unchecked")
		List<Term> ls1 =
				s.createQuery("FROM Term t").list();
		
		if(a.isStemming() || a.isLemmatization()) {
			@SuppressWarnings("unchecked")
			List<TermTransformation> ls2 = s.createQuery("FROM TermTransformation tr").list();
			for ( TermTransformation tr : ls2 ) 
				s.delete(tr);
		}
		
		for ( Term t : ls1 ) 
			s.delete(t);
				
	s.getTransaction().commit();
		
	/*End*/
	
		int count = 0;
		for (List<Term> ls : l) {
			
			m.remove(list.get(count).getId());

			//for-each term, enrich with Google & Wikipedia
			for (Term t : ls) {
				switch(type) {
				case 'h' :
					search = t.getValue().replace("#", "") + " wikipedia";
					break;
				default:
					search = t.getValue() + " wikipedia";
					break;
				}
				System.out.println(search);
				URL url = new URL(google + URLEncoder.encode(search, charset) +"&gl=it");
				Reader reader = new InputStreamReader(url.openStream(), charset);
				GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);
				if (results == null ) System.out.println("pd");
				try {
					System.out.println(results.getResponseData().getResults().get(0).getUrl().replace("en.wikipedia.org","it.wikipedia.org"));
					doc = Jsoup.connect(results.getResponseData().getResults().get(0).getUrl().replace("en.wikipedia.org", "it.wikipedia.org")).get();
					enrichedText += doc.body().text();
				}catch(HttpStatusException | IndexOutOfBoundsException | NullPointerException e) {
					System.out.println(search.replace("wikipedia", ""));
					enrichedText += " " + search.replace("wikipedia", "");
				}

			}
			
			m.put(list.get(count++).getId(), enrichedText);
			enrichedText = "";
		}
		
		CorpusAnalyzer nc = tm.getCorpusAnalyzer(m,a.getLanguage());
		TermEquip te = new TermEquip(nc);
		if (a.isLowerFilter())nc.useLowerFilter(true);
		if (a.isElisionFilter())nc.useElisionFilter(true);
		if (a.isStopFilter())nc.useStopFilter(true);
		if (a.isStemming())nc.enableStemming();
		if (a.isLemmatization()) nc.enableLemmatization();

		te.popTE(nc);		
			
	}
	
	public void denrichText(List<KnowledgeChunk> kcl , CorpusAnalyzer c) throws Exception {
		
		Session s = KCSessionManager.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		
			@SuppressWarnings("unchecked")
			List<Term> ls1 =
					s.createQuery("FROM Term t").list();
			
			if(c.isStemming() || c.isLemmatization()) {
				@SuppressWarnings("unchecked")
				List<TermTransformation> ls2 = s.createQuery("FROM TermTransformation tr").list();
				for ( TermTransformation tr : ls2 ) 
					s.delete(tr);
			}
			
			for ( Term t : ls1 ) 
				s.delete(t);
			
		s.getTransaction().commit();
		
		TextManager tm = new TextManager();
		Map<String,String> m = tm.getTextbyProperties(kcl);
		CorpusAnalyzer n = tm.getCorpusAnalyzer(m,c.getLanguage());
		TermEquip te = new TermEquip(n);
		if (c.isLowerFilter())n.useLowerFilter(true);
		if (c.isElisionFilter())n.useElisionFilter(true);
		if (c.isStopFilter())n.useStopFilter(true);
		if (c.isStemming())n.enableStemming();
		if (c.isLemmatization())n.enableLemmatization();
	
		te.popTE(n);
		
		
	}
}


/*String url_value = m.get(a.getId());

for ( int i = 0 ; i < url_value.length() ; i++)
	if (url_value.charAt(i)==' ') {
		url_value = url_value.replace(' ', '_');
	}
Document doc = Jsoup.connect("http://it.wikipedia.org/wiki/"+url_value).get();
String enrichedText = doc.body().text();*/



/*public void enrichText(KnowledgeChunk a,List<KnowledgeChunk> kcl , CorpusAnalyzer c) throws Exception {

String enrichedText = "";
Session s = KCSessionManager.getSessionFactory().getCurrentSession();
s.beginTransaction();

	@SuppressWarnings("unchecked")
	List<Term> ls1 =
			s.createQuery("FROM Term t").list();
	
	if(c.isStemming() || c.isLemmatization()) {
		@SuppressWarnings("unchecked")
		List<TermTransformation> ls2 = s.createQuery("FROM TermTransformation tr").list();
		for ( TermTransformation tr : ls2 ) 
			s.delete(tr);
	}
	
	for ( Term t : ls1 ) 
		s.delete(t);
	
s.getTransaction().commit();

TextManager tm = new TextManager();
Map<String,String> m = tm.getTextbyProperties(kcl);

m.remove(a.getId());
m.put(a.getId(), enrichedText);
CorpusAnalyzer n = tm.getCorpusAnalyzer(m,c.getLanguage());
TermEquip te = new TermEquip(n);
if (c.isLowerFilter())n.useLowerFilter(true);
if (c.isElisionFilter())n.useElisionFilter(true);
if (c.isStopFilter())n.useStopFilter(true);
if (c.isStemming())n.enableStemming();
if (c.isLemmatization())n.enab
eLemmatization();

te.popTE(n);

}*/

/* SELECT * 
FROM term_eq 
WHERE kc = 'http://islab.di.unimi.it/sent_cloud/sentence#1'
ORDER BY relevance desc
LIMIT 3 */

//http://islab.di.unimi.it/sent_cloud/sentence#1
