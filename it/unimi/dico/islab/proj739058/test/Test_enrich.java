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


public class Test_enrich {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		int language = TextAnalyzer.ITA;
		Scanner in = new Scanner(System.in);
		KCSessionManager.beginTransaction();
		List<KnowledgeChunk> kcl = new ArrayList<KnowledgeChunk>();
		List<KnowledgeChunk> list = new ArrayList<KnowledgeChunk>();
		TextManager tm = new TextManager();
		
		for ( int i = 1 ; i <= 4 ; i++) 
			kcl.add(KCSessionManager.kcm.getKnowledgeChunkById(in.nextLine()));
		
		Map<String,String> m = tm.getTextbyProperties(kcl);
		CorpusAnalyzer a = tm.getCorpusAnalyzer(m,language);
		TermEquip te = new TermEquip(a);
		a.useLowerFilter(true);
		//a.useElisionFilter(true);
		a.useStopFilter(true);
		//a.enableStemming();
		te.popTE(a);
		System.out.println("Inserisci la lista di kc che vuoi arricchire");
		
		for ( int i = 0 ; i < 1 ; i++ ) 
			list.add(KCSessionManager.kcm.getKnowledgeChunkById(in.nextLine()));
			
		te.enrichText(list,kcl,a,'b',3);		
		//te.denrichText(kcl, a);
		
	}

}
