package br.unifor.teste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class TesteBucketsEmMemoria {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		HashMap<Integer, String> bucketsMemoria = new HashMap<Integer, String>();
		bucketsMemoria.put(2, "ab");
		bucketsMemoria.put(4, "ab");
		bucketsMemoria.put(1, "ab");
		
		ArrayList<Integer> s = new ArrayList<Integer>();
		Iterator<Entry<Integer, String>> bi 			= bucketsMemoria.entrySet().iterator();
		while (bi.hasNext()) { 
			Entry<Integer, String> retiradoBucketMemoria 	= bi.next();
			s.add(retiradoBucketMemoria.getKey());
		}
		
		for (Integer integer : s) {
			System.out.println(integer);
		}

	}

}
