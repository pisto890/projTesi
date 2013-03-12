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
		List<KnowledgeChunk> kcl1 = new ArrayList<KnowledgeChunk>();
		TextManager tm = new TextManager();
		
		System.out.println("Inserisci i quattro kc-id che verranno considerati per la creazione del CorpusAnalyzer");
		
		for ( int i = 1 ; i <= 4 ; i++) 
			kcl1.add(KCSessionManager.kcm.getKnowledgeChunkById(in.nextLine()));
		
		Map<String,String> m = tm.getTextbyProperties(kcl1);
		CorpusAnalyzer a = tm.getCorpusAnalyzer(m,language);
		TermEquip te = new TermEquip(a);
		//a.useLowerFilter(true); //è questo il filtro che non consente la presenza degli hashtag nel term_equip
		a.useElisionFilter(true);
		a.useStopFilter(true);
		//a.enableStemming();
		a.enableLemmatization();
		te.popTE(a);
				
	}

}
