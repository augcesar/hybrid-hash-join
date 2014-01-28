package br.unifor.teste;

import java.io.File;
import java.io.IOException;

public class TesteCriarArquivo {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
//		File f = new File("./tuplas_disco/123","u8237812.txt");
//		if (!f.exists())
//			f.mkdirs();
//		f = new File("./tuplas_disco/assbde","arquivo.txt");
//		if (!f.exists())
//			f.createNewFile();
		
		
		
		deletarArquivosDisco();
		
		
		

	}
	
	
	public static void deletarArquivosDisco() {
		File ftabelas = new File("./tuplas_disco" + File.separator);
		if (ftabelas.isDirectory()) {
			String[] listTabelas  = ftabelas.list();
			
			//Tabelas			
			for (int i = 0; i < listTabelas.length; i++) {
				File tbuckets 	= new File(ftabelas,listTabelas[i]);
				String[] listbuckets		= tbuckets.list(); 
				
				//Buckets				
				for (int j = 0; j < listbuckets.length; j++) {
					File tTuplas 	= new File(tbuckets,listbuckets[j]);
					String[] listTuplas		= tTuplas.list(); 
					
					// Tuplas					
					for (int k = 0; k < listTuplas.length; k++) {
						new File(tTuplas,listTuplas[k]).delete();
					}

					tTuplas.delete();
				}
				tbuckets.delete();
			}
		}
	}
}
//for (int r = 0; r < root.length; r++) {
//	File fr = new File(f,root[r]);
//	
//	String[] interno = fr.list();
//	 for (int i = 0; i < interno.length; i++) {
//		File f2 = new File(f,interno[i]);
//		String[] interno2 = f2.list();
//		for (int j = 0; j < interno2.length; j++)
//			new File(f2,interno2[j]).delete();
//		new File(f,interno[i]).delete();
//	}
//	 
//}