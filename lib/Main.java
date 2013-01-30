package lib;

import java.util.Scanner;

import it.unimi.dico.islab.idbs2.kc.session.KCSessionManager;

import org.hibernate.Session;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Dichiaro una variabile di tipo String da usare come input
		//per il getter by id della classe KCManager
		String s,argp = "http://islab.di.unimi.it/sent_cloud/sentence";
		Scanner in = new Scanner();
		s = in.nextLine();
		KCSessionManager.beginTransaction();
		KnowledgeChunk x = KCSessionManager.kcm.getKnowledgeChunkById(s);
		List<Predicate> pred = KCSessionManager.pm.getPredicatesWithP1Pn(argp,argp);
		
		
	}

}
