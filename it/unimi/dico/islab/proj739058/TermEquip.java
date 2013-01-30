package it.unimi.dico.islab.proj739058;

import java.util.Set;
import java.util.Vector;

import it.unimi.dico.islab.idbs2.kc.KnowledgeChunk;
import it.unimi.dico.islab.idbs2.kc.session.KCSessionManager;
import it.unimi.dico.islab.textanalyzer.tools.CorpusAnalyzer;
import it.unimi.dico.islab.textanalyzer.tools.TermsDescriptor;
import it.unimi.dico.islab.textanalyzer.tools.TextAnalyzer;

/**
 * This class provides ...
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
	
	public void popTE(int language,boolean stem,boolean elision,boolean lowerf,boolean stopf) {
		
		a.disableStemLem();
		if (elision)
			a.useElisionFilter(true);
		if (lowerf)
			a.useLowerFilter(true);
		if (stopf)
			a.useStopFilter(true);
		
		a.analyze();
		TextAnalyzer ta = new TextAnalyzer(language);
		if (stem) {
			ta.enableStemming(); // Solo se è abilitato lo stemming
			if (elision)
				ta.useElisionFilter(true);
			if (lowerf)
				ta.useLowerFilter(true);
			if (stopf)
				ta.useStopFilter(true);
		}
		Set<String> keys = a.getAllTextIDs();
		for (String k : keys) {
			KnowledgeChunk kc = KCSessionManager.kcm.getKnowledgeChunkById(k);
			Vector<String> v = a.getAnalyzedTextByID(k);
			TermsDescriptor occurrences = a.runOccurrences();
			//TermsDescriptor IDF = a.runIDF();
			//getTextByID returns not-anaylized-textValue of the map indexed by id = k
			//TermsDescriptor TF = a.getAnalyzedTF(a.getTextByID(k));
			TermsDescriptor TFIDF = a.getAnalyzedTFIDF(k);
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
								kc.getTerm(s).addTransformation(s, altern);
				}
			}
		}
		KCSessionManager.commitTransaction();
	}
}