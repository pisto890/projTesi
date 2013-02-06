package it.unimi.dico.islab.proj739058;

import java.util.Set;
import java.util.Vector;

import it.unimi.dico.islab.idbs2.kc.KnowledgeChunk;
import it.unimi.dico.islab.idbs2.kc.session.KCSessionManager;
import it.unimi.dico.islab.textanalyzer.tools.CorpusAnalyzer;
import it.unimi.dico.islab.textanalyzer.tools.TermsDescriptor;
import it.unimi.dico.islab.textanalyzer.tools.TextAnalyzer;

/**
 * This class provides to create a term_equip
 * @author Isacco Borsani
 *
 */

public class TermEquip {

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
		
		if (a.isStemming()) stem = true;
		
		a.disableStemLem();
		a.analyze();
		TextAnalyzer ta = new TextAnalyzer(a.getLanguage());
		
		if (stem) {
			ta.enableStemming(); // Solo se è abilitato lo stemming
		}
		
		Set<String> keys = a.getAllTextIDs(); //it takes all kc keys 
		for (String k : keys) {
			KnowledgeChunk kc = KCSessionManager.kcm.getKnowledgeChunkById(k);
			Vector<String> v = a.getAnalyzedTextByID(k); //String vector of current kc
			TermsDescriptor occurrences = a.runOccurrences();
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
				}
			}
		}
		KCSessionManager.commitTransaction();
	}
}