package it.unimi.dico.islab.proj739058;

import it.unimi.dico.islab.idbs2.kc.KnowledgeChunk;

/**
 * Match Interface provides an abstract method "match" that is implemented
 * by a class, this one is used to check similarity between two kc taking their id.
 * The similarity algorithm is specific for each class.
 * @author isacco
 *
 */

public interface MatchIF {

	public double match(KnowledgeChunk a , KnowledgeChunk b);
	
}
