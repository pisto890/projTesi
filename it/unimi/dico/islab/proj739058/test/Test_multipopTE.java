package it.unimi.dico.islab.proj739058.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import it.unimi.dico.islab.idbs2.kc.KnowledgeChunk;
import it.unimi.dico.islab.idbs2.kc.session.KCSessionManager;
import it.unimi.dico.islab.proj739058.TermEquip;
import it.unimi.dico.islab.proj739058.TextManager;
import it.unimi.dico.islab.textanalyzer.tools.CorpusAnalyzer;
import it.unimi.dico.islab.textanalyzer.tools.TextAnalyzer;


public class Test_multipopTE {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		int language = TextAnalyzer.ITA;
		Scanner in = new Scanner(System.in);
		KCSessionManager.beginTransaction();
		List<KnowledgeChunk> kcl = new ArrayList<KnowledgeChunk>();
		TextManager tm = new TextManager();
		
		for ( int i = 1 ; i <= 5 ; i++) 
			kcl.add(KCSessionManager.kcm.getKnowledgeChunkById(in.nextLine()));
		
		Map<String,String> m = tm.getTextbyProperties(kcl);
		System.out.println(m);
		CorpusAnalyzer a = tm.getCorpusAnalyzer(m,language);
		TermEquip te = new TermEquip(a);
		te.popTE(language,true,false,false,false);
	}

}
