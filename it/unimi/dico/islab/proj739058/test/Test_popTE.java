package it.unimi.dico.islab.proj739058.test;

import java.util.Map;
import java.util.Scanner;

import it.unimi.dico.islab.idbs2.kc.session.KCSessionManager;
import it.unimi.dico.islab.proj739058.TermEquip;
import it.unimi.dico.islab.proj739058.TextManager;
import it.unimi.dico.islab.textanalyzer.tools.CorpusAnalyzer;
import it.unimi.dico.islab.textanalyzer.tools.TextAnalyzer;


public class Test_popTE {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		int language = TextAnalyzer.ITA;
		Scanner in = new Scanner(System.in);
		KCSessionManager.beginTransaction();
		TextManager tm = new TextManager();
		Map<String,String> m = tm.getTextbyProperties(KCSessionManager.kcm.getKnowledgeChunkById(in.nextLine()));
		System.out.println(m);
		CorpusAnalyzer a = tm.getCorpusAnalyzer(m,language);
		TermEquip te = new TermEquip(a);
		te.popTE(a);
	}

}
