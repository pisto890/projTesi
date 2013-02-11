package it.unimi.dico.islab.proj739058.test;

import java.text.DecimalFormat;
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
import it.unimi.dico.islab.proj739058.Stemming;


public class Test_stemming {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		String idk1,idk2;
		int language = TextAnalyzer.ITA;
		Scanner in = new Scanner(System.in);
		KCSessionManager.beginTransaction();
		List<KnowledgeChunk> kcl1 = new ArrayList<KnowledgeChunk>();
		List<KnowledgeChunk> kcl2 = new ArrayList<KnowledgeChunk>();
		TextManager tm = new TextManager();
		DecimalFormat df = new DecimalFormat("#.##");
				
		for ( int i = 1 ; i <= 4 ; i++) 
			kcl1.add(KCSessionManager.kcm.getKnowledgeChunkById(in.nextLine()));
		
		Map<String,String> m = tm.getTextbyProperties(kcl1);
		CorpusAnalyzer a = tm.getCorpusAnalyzer(m,language);
		TermEquip te = new TermEquip(a);
		a.useLowerFilter(true);
		a.useElisionFilter(true);
		a.useStopFilter(true);
		a.enableStemming();
		te.popTE(a);
		
		KCSessionManager.beginTransaction();

		//calcolo cosine
		
		System.out.println("Inserisci ora l'id dei kc da confrontare:");
		
		idk1 = in.nextLine();
		idk2 = in.nextLine();
		KnowledgeChunk k1 = KCSessionManager.kcm.getKnowledgeChunkById(idk1);
		KnowledgeChunk k2 = KCSessionManager.kcm.getKnowledgeChunkById(idk2);
		
		System.out.println(df.format(new Stemming().match(k1,k2)));
		
	}

}
