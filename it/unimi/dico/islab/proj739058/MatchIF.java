package it.unimi.dico.islab.proj739058;

import it.unimi.dico.islab.idbs2.kc.KnowledgeChunk;

/**
 * Match Interface provides an abstract method "match" that is implemented
 * by Sim_calc.java. This one is used to check similarity between two kc taking their id
 * and similarity type: C,S,L (cosine,stemming and lemmatization).
 * The similarity algorithm is specific for each class.
 * @author Isacco Borsani N°mat: 739058
 *
 */

public interface MatchIF {

	public double match(KnowledgeChunk a , KnowledgeChunk b,char s);
	
}
