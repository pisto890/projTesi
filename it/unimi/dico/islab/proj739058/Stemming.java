package it.unimi.dico.islab.proj739058;

import java.util.List;
import java.util.TreeMap;

import org.hibernate.Session;

import it.unimi.dico.islab.idbs2.kc.KnowledgeChunk;
import it.unimi.dico.islab.idbs2.kc.session.KCSessionManager;

/**
 * This class provides a method that it compute the similarity between two kc using
 * cosine-similarity algorithm.
 * @author isacco
 *
 */

public class Stemming implements MatchIF {

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
		Object[] tmp;
		TreeMap<String,Object[]> tree_b = new TreeMap<String,Object[]>();
		Session s = KCSessionManager.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		
		String query1 ="SELECT tr.transformationValue, t.kc.id, AVG(t.relevance)" +
						" FROM Term t LEFT JOIN t.transformations tr" +
						" WHERE t.kc = '" + id1+ "' " +
						" GROUP BY tr.transformationValue, t.kc";
		
		String query2 ="SELECT tr.transformationValue, t.kc.id, AVG(t.relevance)" +
				" FROM Term t LEFT JOIN t.transformations tr" +
				" WHERE t.kc = '" + id2+ "' " +
				" GROUP BY tr.transformationValue, t.kc";

		
		@SuppressWarnings("unchecked")
		List<Object[]> ls1 =
				s.createQuery(query1).list();
		@SuppressWarnings("unchecked")		
		List<Object[]> ls2 = s.createQuery(query2).list();

		/* N.B: ls1 and ls2 contains an array of object:
		 * obj[0] = transf_value :: String
		 * obj[1] = kc :: String
		 * obj[2] = relevanceAVG :: String
		 */
		
		for (Object[] obj: ls2) {
			tree_b.put((String)obj[0],obj);
			norm_ls2 += (double)obj[2] * (double)obj[2];
		}
		
		for (Object[] obj : ls1) {
			norm_ls1 += (double)obj[2] * (double)obj[2];
			tmp = tree_b.get((String)obj[0]);
			if ( tmp != null ) {
				num += (double) obj[2]* (double)tmp[2];
			}
		}
		
		den = Math.sqrt(norm_ls1) * Math.sqrt(norm_ls2);
		
		result = num/den;
		
		s.getTransaction().commit();
		
		return result;
	}
	
}

/*       
 * SELECT transf_value, kc, AVG(relevance)
FROM term_form INNER JOIN term_eq ON term_form.term = term_eq.id
WHERE kc = 'http://islab.di.unimi.it/sent_cloud/sentence#1'
GROUP BY transf_value,kc


 * 
 * 
 * 
 * */
