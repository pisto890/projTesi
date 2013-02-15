package it.unimi.dico.islab.proj739058;

import java.util.List;
import java.util.TreeMap;

import org.hibernate.Session;

import it.unimi.dico.islab.idbs2.kc.KnowledgeChunk;
import it.unimi.dico.islab.idbs2.kc.Term;
import it.unimi.dico.islab.idbs2.kc.session.KCSessionManager;

/**
 * This class provides a method that it compute the similarity between two kc using
 * cosine-similarity algorithm.
 * @author isacco
 *
 */

public class Cosin_sim {

	public double match(KnowledgeChunk a , KnowledgeChunk b) {
		
		//Numerator and denominator of Cosine Similarity
		double num = 0.0;
		double den = 0.0;
		//norm of term list1 and term list2
		double norm_ls1 = 0.0;
		double norm_ls2 = 0.0;
		//result of Cosine Similarity
		double result;
		//Getting id-kc
		String id1 = a.getId();
		String id2 = b.getId();
		Term tmp;
		TreeMap<String,Term> tree_b = new TreeMap<String,Term>();
		Session s = KCSessionManager.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		
		@SuppressWarnings("unchecked")
		List<Term> ls1 =
				s.createQuery("FROM Term t WHERE t.kc = '" + id1 + "'").list();
		
		@SuppressWarnings("unchecked")
		List<Term> ls2 =
				s.createQuery("FROM Term t WHERE t.kc = '" + id2 + "'").list();
	
		for (Term t : ls2) {
			tree_b.put(t.getValue(),t);
			norm_ls2 += t.getRelevance() * t.getRelevance();
		}
		
		for (Term t : ls1) {
			norm_ls1 += t.getRelevance() * t.getRelevance();
			tmp = tree_b.get(t.getValue());
			if ( tmp != null ) {
				num += t.getRelevance() * tmp.getRelevance();
			}
		}
		
		den = Math.sqrt(norm_ls1) * Math.sqrt(norm_ls2);
		
		result = num/den;
		
		s.getTransaction().commit();
		
		return result;
	}
	
}
