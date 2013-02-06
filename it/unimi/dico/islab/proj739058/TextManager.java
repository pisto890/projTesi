package it.unimi.dico.islab.proj739058;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.HashMap;

import org.hibernate.Session;

import it.unimi.dico.islab.idbs2.kc.KnowledgeChunk;
import it.unimi.dico.islab.idbs2.kc.Predicate;
import it.unimi.dico.islab.idbs2.kc.session.KCSessionManager;
import it.unimi.dico.islab.textanalyzer.tools.*;


/**
 * This class allows to get,manipulate and analyze texts 
 * coming from the KC-DB in relation with kc_identifier. 
 * @author Isacco Borsani N°mat: 739058
 * 
 */

public class TextManager {

	private List<String>properties = new ArrayList<String>();
	private Scanner doc;
	private File file;
	
	/**
	 * This constructor istantiates a file contaning
	 * rules, (p1-pn) , to get specific-predicate from KC-DB and
	 * save these rules inside a List named properties
	 * @throws Exception
	 */
	
	public TextManager() throws Exception {
		file = new File("input.txt");
		try{
			doc = new Scanner(file);
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(doc.hasNextLine()) {
			properties.add(doc.nextLine());
		}
	}
	

	/**
	 * This method allow to get an arbitrary "concat-text" coming from
	 * the tuples inside the Predicate relation having p1-pn equals to
	 * p1-pn inside the properties list created in the constructor and
	 * "belong_kc" equals to kc_id.
	 * The result is saved inside an HashMap containing the couple
	 * (kc-id , text)
	 * @param KnowledgeChunk kc
	 * @return Map<String,String> final_res
	 */
	
	@SuppressWarnings("unchecked")
	public Map<String,String> getTextbyProperties(KnowledgeChunk kc) {
		
		List<Predicate>pred = new ArrayList<Predicate>();
		String text_result = "",p1,pn,query;
		StringTokenizer stk;
		Map<String,String>final_res = new HashMap<String,String>(1);
		
		Session s = KCSessionManager.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		
		for (String line : properties) {
			
			stk = new StringTokenizer(line," ");
			p1 = stk.nextToken(); /* leggo la proprietà */
			pn = stk.nextToken(); /* idem */
			/* preparo gli argomenti della funzione */
			/* getPredicatesWithp1pn */
			KnowledgeChunk a = new KnowledgeChunk(p1);
			KnowledgeChunk b = new KnowledgeChunk(pn);
			query = "FROM Predicate pr WHERE pr.belongKc ='" + kc.getId() + "' AND (pr.p1 = '"+a.getId()+"' AND pr.pn ='"+b.getId()+"')";
			pred = s.createQuery(query).list();
				for ( Predicate pr : pred) 
					if (pr.getValueText() != null)
						text_result += pr.getValueText() + " ";
					else
						text_result += pr.getValue() + " ";
		}
		
		if (properties.size() == 0) 
			for (Predicate pr : kc.getLinks())
				if (pr.getValueText() != null)
					text_result += pr.getValueText() + " ";
				else
					text_result += pr.getValue() + " ";
				
		final_res.put(kc.getId(), text_result);
		return final_res;
	}
	
	
	/**
	 * This method ovverride the previous getting a list of Knowledge chunck
	 * @param List<KnowledgeChunk> kcl
	 * @return Map<String,String> final_res
	 */
	
	public Map<String,String> getTextbyProperties(List<KnowledgeChunk> kcl) {

		Map<String,String> final_res = new HashMap<String,String>();
		
		for (KnowledgeChunk kc : kcl) final_res.putAll(this.getTextbyProperties(kc));
		
		return final_res;
	}
	
	/**
	 * This method create a CorpusAnalyzer that contain the texts
	 * to analyze
	 * @param Map<String,String>kc_text,int language
	 * @return CorpusAnalyzer a
	 */

	public CorpusAnalyzer getCorpusAnalyzer(Map<String,String> kc_text, int language) {
		CorpusAnalyzer a = new CorpusAnalyzer(language);
		Set<String>key = kc_text.keySet();
		for (String k : key) {
			a.addText(k, (String)kc_text.get(k));
		}
		return a;
	}
	
}
