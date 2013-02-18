package it.unimi.dico.islab.proj739058;

import it.unimi.dico.islab.idbs2.kc.KnowledgeChunk;
import it.unimi.dico.islab.proj739058.Cosin_sim;
import it.unimi.dico.islab.proj739058.Stemming_Lem;


public class Sim_calc implements MatchIF{

	/**
	 * This class implements Match interface giving three kind of similarity measures:
	 * COSINE-SIMILARITY,STEMMING and LEMMATIZATION
	 * @author Isacco Borsani N°mat: 739058
	 */
	
	public double match(KnowledgeChunk a,KnowledgeChunk b,char s) {
		
		double ris = 0.0;
		
		switch(s) {
		
		case 'c':
			ris = new Cosin_sim().match(a, b);
			break;
		
		case 's':
			ris = new Stemming_Lem().match(a, b);
			break;
			
		case 'l':
			ris = new Stemming_Lem().match(a, b);
			break;
		
		}
		
		return ris;
	}
	
}
