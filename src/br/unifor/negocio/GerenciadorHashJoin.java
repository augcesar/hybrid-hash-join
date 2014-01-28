package br.unifor.negocio;

import java.io.File;

import br.unifor.entidades.*;
import br.unifor.gui.Principal;

public class GerenciadorHashJoin extends Thread {

	private static GerenciadorBucket gerBucketTab1,gerBucketTab2;
	private static String diretorioBucketDisco = "./tuplas_disco" + File.separator;
	private ConstrucaoBucketThread 	threadtabelaR,threadtabelaS;
	private JuncaoTabelasThread		juncaoTabelasThread;
	
	public GerenciadorHashJoin(TabelaJuncao tabJuncaoR, TabelaJuncao tabJuncaoS,int totalBuckets,int totalLinhasBucket) {
		this.gerBucketTab1 = new GerenciadorBucket(tabJuncaoR, totalBuckets, totalLinhasBucket);
		this.gerBucketTab2 = new GerenciadorBucket(tabJuncaoS, totalBuckets, totalLinhasBucket);
	}

	public void run() {
		
		threadtabelaR 			= new ConstrucaoBucketThread(gerBucketTab1);
		threadtabelaS 			= new ConstrucaoBucketThread(gerBucketTab2);
		juncaoTabelasThread		= new JuncaoTabelasThread(gerBucketTab1,gerBucketTab2,threadtabelaR,threadtabelaS);

		juncaoTabelasThread.start();

		//Thread - Regra do Hibridi Hash Join				
		
		//A tabela S em tempo de construcao deve verificar se existe um bucket na tabela R com o mesmo ID,
		//Se existe R(bucket_id) e S(bucket_id) ja ir fazendo a juncao.
		//Criar uma thread para essa juncao	
	}
	
	public static void limparTuplasDisco() {
		File ftabelas = new File(diretorioBucketDisco);
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

	@SuppressWarnings("deprecation")
	public void pararThreads() {
		this.threadtabelaR.stop();
		this.threadtabelaS.stop();
		this.juncaoTabelasThread.stop();
		this.stop();
		
	}
}
