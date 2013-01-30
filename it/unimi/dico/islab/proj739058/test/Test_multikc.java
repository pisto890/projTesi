package it.unimi.dico.islab.proj739058.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


import it.unimi.dico.islab.idbs2.kc.KnowledgeChunk;
import it.unimi.dico.islab.idbs2.kc.session.KCSessionManager;
import it.unimi.dico.islab.proj739058.TextManager;


public class Test_multikc {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		Scanner in = new Scanner(System.in);
		KCSessionManager.beginTransaction();
		List<KnowledgeChunk> kcl = new ArrayList<KnowledgeChunk>();
		TextManager tm = new TextManager();
		
		for ( int i = 0 ; i < 3 ; i++) 
			kcl.add(KCSessionManager.kcm.getKnowledgeChunkById(in.nextLine()));
		
		Map<String,String>ris = tm.getTextbyProperties(kcl);
        	System.out.println(ris);
		
	}

}
