package it.unimi.dico.islab.proj739058.test;

import java.util.Scanner;

import it.unimi.dico.islab.idbs2.kc.session.KCSessionManager;
import it.unimi.dico.islab.proj739058.TextManager;


public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		Scanner in = new Scanner(System.in);
		KCSessionManager.beginTransaction();
		TextManager tm = new TextManager();
		System.out.println(tm.getTextbyProperties(KCSessionManager.kcm.getKnowledgeChunkById(in.nextLine())));
		
	}

}
