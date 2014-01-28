package br.unifor.negocio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import br.unifor.entidades.*;
import br.unifor.gui.Principal;
import br.unifor.gui.Resultado;

public class JuncaoTabelasThread extends Thread {
	private GerenciadorBucket gerBucketTab1, gerBucketTab2;
	private ConstrucaoBucketThread threadtabelaR,threadtabelaS;
	private HashMap<Integer, Integer> bucketLinhasLidasR;
	private HashMap<Integer, HashMap<Integer, Integer>> bucketLinhasLidasS;
	private Resultado tabResultado;
	
	private long tempoJuncao;
	
	public JuncaoTabelasThread(GerenciadorBucket gerBucketTab1,GerenciadorBucket gerBucketTab2,ConstrucaoBucketThread threadtabelaR,ConstrucaoBucketThread threadtabelaS) {
		this.gerBucketTab1 = gerBucketTab1;
		this.gerBucketTab2 = gerBucketTab2;
		this.threadtabelaR = threadtabelaR;
		this.threadtabelaS = threadtabelaS;
		
		
		this.bucketLinhasLidasR 	= new HashMap<Integer, Integer>();
		this.bucketLinhasLidasS 	= new HashMap<Integer, HashMap<Integer, Integer>>();
		this.tabResultado  			= new Resultado();
		

	}
	
	@Override
	public void run() {
		
		this.threadtabelaR.start();
		this.threadtabelaS.start();
		Principal.setLog("Iniciando Junção\n");
		System.out.println("Iniciando Junção");
		
		tempoJuncao = System.currentTimeMillis();
		
		
		
		while (true)
			try {
				
				algoritimoHibridHashJoin();
				
				try {
					this.sleep(400);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	
	private synchronized void algoritimoHibridHashJoin() throws IOException {
		
		//Ler os buckets que estão em memória
		ArrayList<Integer> bucketsMemoriaR = this.gerBucketTab1.bucketsEmMemoria();
		ArrayList<Integer> bucketsMemoriaS = this.gerBucketTab2.bucketsEmMemoria();
		
		
		// Verificar se bucket R e S estão em memória
		boolean bucketsMemoria = false;
		for (Integer bmr : bucketsMemoriaR) 
			for (Integer bms : bucketsMemoriaS) 
				if(bmr.equals(bms)) bucketsMemoria = true;
		
		if(!bucketsMemoria) {
	
				boolean existeEmMemoria = false;
				for (Integer bmr : bucketsMemoriaR) {
					try {
						if (this.gerBucketTab2.retirarBucketDiscoParaMemoria(bmr))
							existeEmMemoria = true;
						System.out.println("Retirar Tuplas Disco S: " + bmr);
					}catch(Exception e){}
					
					existeEmMemoria = true;
				}
						
				for (Integer bms : bucketsMemoriaS) {
					try {
						if (this.gerBucketTab1.retirarBucketDiscoParaMemoria(bms))
							existeEmMemoria = true;
						System.out.println("Retirar Tuplas Disco R: " + bms);
					}catch(Exception e){}
					
					
				}
				
				if (!existeEmMemoria) {
					ArrayList<Integer> gbt1 = this.gerBucketTab1.bucketsEmDisco();
					ArrayList<Integer> gbt2 = this.gerBucketTab2.bucketsEmDisco();
					
					if (gbt1.size() > 0 && gbt2.size() > 0){
						try {
							while (bucketsMemoriaR.size() < (gerBucketTab1.getTotal() / 2))
								this.gerBucketTab1.retirarBucketDiscoParaMemoria(gbt1.iterator().next());
							
							while (bucketsMemoriaS.size() < (gerBucketTab2.getTotal() / 2))
								this.gerBucketTab2.retirarBucketDiscoParaMemoria(gbt2.iterator().next());
						}catch(Exception e){}
					}
					
					// Parar Threads
					if (!threadtabelaR.isAlive() && !threadtabelaS.isAlive()) {
						if (bucketsMemoriaR.size() == 0 && bucketsMemoriaS.size() == 0 && gbt1.size() == 0 && gbt2.size() == 0) {
							Principal.setLog("Junção Finalizada");
							System.out.println("Junção Finalizada");
							
							Principal.setLog("Tempo de Junção: " + ((System.currentTimeMillis() - tempoJuncao) / 1000) + "seg" );
							
							this.threadtabelaR.stop();
							this.threadtabelaS.stop();
							this.stop();
						}
					}
				}
				
				return ;
		}
				
		// Comprar se existe algum buckets em memoria para junção
		for (Integer idBucket : bucketsMemoriaR) {
			
			Bucket bucketR = this.gerBucketTab1.obterBucket(TipoLocalBucket.MEMORIA, idBucket);
			Bucket bucketS = this.gerBucketTab2.obterBucket(TipoLocalBucket.MEMORIA, idBucket);
			
			// Se nulll, bucket sem linha
			if(bucketS != null && bucketR != null && 
			   bucketR.getTipoLocalBucket() == TipoLocalBucket.MEMORIA && 
			   bucketS.getTipoLocalBucket() == TipoLocalBucket.MEMORIA &&
			   bucketR.getTotalLinhasAlocadasMemoria() > 0 &&
			   bucketS.getTotalLinhasAlocadasMemoria() > 0
			   ) {
				
					// Ler uma linha de R e todas de S
					// quando todas as linha de S forem lidas, passar para a proxima de R 										
					for_r: for (int i = 0; i < bucketR.getTotalLinhasAlocadasMemoria(); i++) {
						
						int linhaLidaR 	= getProximaLinhaTabelaR(idBucket);
						Tupla tuplaR 	= bucketR.getProximaTupla(TipoLocalBucket.MEMORIA, linhaLidaR );
						
						//se null, chegou ai final
						if (tuplaR != null) {
							
							for (int j = 0; j < bucketS.getTotalLinhasAlocadasMemoria(); j++) {
																
								Tupla tuplaS = bucketS.getProximaTupla(TipoLocalBucket.MEMORIA, getProximaLinhaTabelaS(idBucket, linhaLidaR) );
								
								if (tuplaS != null) {
									if( tuplaR.getColunaJuncao().equals(tuplaS.getColunaJuncao()) ) {
										tabResultado.adicionarTupla(tuplaR, tuplaS);
										
									}else {
										System.err.println("Coluna de junção diferente");
										System.exit(0);
									}
								}else {
									getVoltarLinhaTabelaS(idBucket,linhaLidaR);
									break for_r;
								}
								
								
							}//forS
						}else {
							getVoltarLinhaTabelaR(idBucket);
							break for_r;
						}
					}//for R
					
					if (!threadtabelaR.isAlive() && !threadtabelaS.isAlive()) {
						if (bucketR.getTotalLinhasAlocadasMemoria() == bucketLinhasLidasR.get(idBucket)+1 &&
							bucketS.getTotalLinhasAlocadasMemoria() == checkLinhasLidasS(idBucket)+1) {
							gerBucketTab1.remover_bucket(idBucket);
							gerBucketTab2.remover_bucket(idBucket);
							System.out.println("\n\nRemovendo Bucket : " + idBucket);

							System.out.print("[Mem] R: ");
							for (Integer integer : bucketsMemoriaR) {
								System.out.print( integer + " ");
							}
							System.out.println("");
							System.out.print("[Mem] S: ");
							for (Integer integer : bucketsMemoriaS) {
								System.out.print( integer + " ");
							}
							
							System.out.println("");	
							
							System.out.print("[Disco] R: ");
							for (Integer integer : this.gerBucketTab1.bucketsEmDisco()) {
								System.out.print( integer + " ");
							}
							System.out.println("");
							System.out.print("[Disco] S: ");
							for (Integer integer : this.gerBucketTab2.bucketsEmDisco()) {
								System.out.print( integer + " ");
							}						
	
							System.out.println("");	
							
						}
					}

//				try {
//					System.out.println("\n");
//					System.out.println("id: " + idBucket);
//					System.out.println("Bucket R " + bucketR);
//					System.out.println("Bucket S " + bucketS);
//					System.out.println("Linha Lidas R " + bucketLinhasLidasR.get(idBucket));
//					System.out.println("Linha Lidas S " + bucketLinhasLidasS.get(idBucket));
//					System.out.println("Tipo Bucket R " + bucketR.getTipoLocalBucket());
//					System.out.println("Tipo Bucket S " + bucketS.getTipoLocalBucket());
//					System.out.println("Memoria Bucket R " + bucketR.getTotalLinhasAlocadasMemoria());
//					System.out.println("Memoria Bucket S " + bucketS.getTotalLinhasAlocadasMemoria());
//					System.out.println("LinhasLidas Bucket R " + (bucketLinhasLidasR.get(idBucket)+1));
//					System.out.println("LinhasLidas Bucket S " + (bucketLinhasLidasS.get(idBucket)+1));
//				}catch(Exception e) {
//					
//				}
			}	
			
		}	
	}
	
	
	private int checkLinhasLidasS(int idBucket) {
		
		int ultimaLinhaLidaS = 0;
		
		Iterator<Entry<Integer, Integer>> bl = bucketLinhasLidasS.get(idBucket).entrySet().iterator();
		while (bl.hasNext()) { 
			Entry<Integer, Integer> linha = bl.next();
			if (linha.getValue() > ultimaLinhaLidaS)
				ultimaLinhaLidaS = linha.getValue();
		}
		
		return ultimaLinhaLidaS;
	}

	private int getProximaLinhaTabelaR(int idBucket) {
		if (bucketLinhasLidasR.containsKey(idBucket)) {
			bucketLinhasLidasR.put(idBucket, bucketLinhasLidasR.get(idBucket) + 1 );
		}else
			bucketLinhasLidasR.put(idBucket, 0);
		
		return bucketLinhasLidasR.get(idBucket);
	}

	private int getVoltarLinhaTabelaR(int idBucket) {
		if (bucketLinhasLidasR.containsKey(idBucket)) {
			if (bucketLinhasLidasR.get(idBucket) > 0)
			 bucketLinhasLidasR.put(idBucket, bucketLinhasLidasR.get(idBucket) - 1 );
		}else
			bucketLinhasLidasR.put(idBucket, 0);
		
		return bucketLinhasLidasR.get(idBucket);
	}
	
	private int getProximaLinhaTabelaS(int idBucket,int linhaLidaR) {
		
	
		if (bucketLinhasLidasS.containsKey(idBucket) && bucketLinhasLidasS.get(idBucket).containsKey(linhaLidaR)) {
			bucketLinhasLidasS.get(idBucket).put(linhaLidaR, (bucketLinhasLidasS.get(idBucket).get(linhaLidaR) + 1) );
		}else {
			HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
			hash.put(linhaLidaR,0);
			bucketLinhasLidasS.put(idBucket, hash);
		}
		return bucketLinhasLidasS.get(idBucket).get(linhaLidaR);
		
	}

	private int getVoltarLinhaTabelaS(int idBucket,int linhaLidaR) {
		if (bucketLinhasLidasS.get(idBucket).containsKey(linhaLidaR)) {
			if (bucketLinhasLidasS.get(idBucket).get(linhaLidaR) > 0)
				bucketLinhasLidasS.get(idBucket).put(linhaLidaR, (bucketLinhasLidasS.get(idBucket).get(linhaLidaR) - 1) );
		}else {
			HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
			hash.put(linhaLidaR,0);
			bucketLinhasLidasS.put(idBucket, hash);
		}
		return bucketLinhasLidasS.get(idBucket).get(linhaLidaR);
	}
}
