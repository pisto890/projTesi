package it.unimi.dico.islab.proj739058.test;

import it.unimi.dico.islab.idbs2.kc.KnowledgeChunk;
import it.unimi.dico.islab.idbs2.kc.session.KCManager;
import it.unimi.dico.islab.idbs2.kc.session.KCSessionManager;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class Prova_inserimento {

	/**
	 * @param args
	 * @throws TwitterException 
	 */
	public static void main(String[] args) throws TwitterException {
	    // The factory instance is re-useable and thread safe.
	    Twitter twitter = TwitterFactory.getSingleton();
	    String[] arr = {"beppe_grillo","FedericoRampini","ementana","Michele_Santor","robertosaviano","NichiVendola","fabfazio","SenatoreMonti","pbersani" };
	    int count = 1;
	    
	    for ( int i = 0 ; i < 9 ; i++) {
		    List<Status> statuses= twitter.getUserTimeline(arr[i]);
	   	 	for (Status status : statuses) {
	   	 	KCSessionManager.beginTransaction();
	   	 	KCManager kcm = KCSessionManager.kcm;
	   	 	//Creo un nuovo KC con id "/en/woody_allen"
	   	 	KnowledgeChunk temp = new KnowledgeChunk(arr[i]+(count++));
	   	 	temp.setType('I');
	   	 	KnowledgeChunk prop = new KnowledgeChunk("it/tweet");
	   	 	prop.setType('P');
	    	temp.addPredicate(prop, prop, null, status.getText(), 1, null, status.getText().length());
	    	kcm.saveOrUpdate(temp);
	    	KCSessionManager.commitTransaction();
	   	 	}
	   	 	
	    }

	}

}
