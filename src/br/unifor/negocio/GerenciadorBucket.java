package br.unifor.negocio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import br.unifor.entidades.*;
import br.unifor.exception.*;
import br.unifor.gui.Principal;

public class GerenciadorBucket extends Thread {
	private TabelaJuncao tabelaJuncao		= null;
	private int total 						= 0;
	private int linhas 						= 0 ;
	private HashMap<Integer, Bucket> bucketsMemoria	= null;
	private HashMap<Integer, Bucket> bucketsDisco 	= null;
	
	public GerenciadorBucket(TabelaJuncao tabelaJuncao, int total, int linhas) {
		this.tabelaJuncao	= tabelaJuncao;
		this.total  		= total;
		this.linhas 		= linhas;
		this.bucketsMemoria	= new HashMap<Integer, Bucket>();
		this.bucketsDisco	= new HashMap<Integer, Bucket>();
	}
	
	public void run() {
		
	}

	public synchronized void addTuplaBucket(String registroColunaTabela,Tupla tupla) throws BucketNaoEncontrado, IOException, InterruptedException {
		int idBucket = gerarIdBucket(registroColunaTabela);
		
		while (true) {
			if (this.bucketsMemoria.size() >= this.total) {
				Iterator<Entry<Integer, Bucket>> bi 			= this.bucketsMemoria.entrySet().iterator();
				Entry<Integer, Bucket> retiradoBucketMemoria 	= bi.next();
			
				retirarBucketMemoriaParaDisco(retiradoBucketMemoria.getKey());
				
				Principal.labelBucketMemoria(this.bucketsMemoria.size(),tabelaJuncao.getNumTabela());
				Principal.labelBucketDisco(this.bucketsDisco.size(),tabelaJuncao.getNumTabela());

			}else 
				break;
		}
		
		
		//Bucket Memoria
		if (existeBucket(TipoLocalBucket.MEMORIA,idBucket)) {
			this.bucketsMemoria.get(idBucket).addLinha(tupla);

			// Se o Bucket Memoria estiver com mais linhas que o informado, entao ele deve ir para DISCO
			// Em disco o bucket pode adicionar novas tuplas sem problema
			// Quando todos os bucktes em memoria estiverem feito o join, entao e hora de trazer os buckets de disco para memoria			
			Bucket bktMemoria = obterBucket(TipoLocalBucket.MEMORIA,idBucket);
			if (bktMemoria != null)
			if (bktMemoria.getTotalLinhasAlocadasMemoria() > this.linhas) {
				
				retirarBucketMemoriaParaDisco(idBucket);
				
				Principal.labelBucketMemoria(this.bucketsMemoria.size(),tabelaJuncao.getNumTabela());
				Principal.labelBucketDisco(this.bucketsDisco.size(),tabelaJuncao.getNumTabela());

			}

		//Bucket Disco	
		}else if (existeBucket(TipoLocalBucket.DISCO,idBucket)) {
			this.bucketsDisco.get(idBucket).addLinha(tupla);

		}else {
			//Se caiu aqui, foi pq o bucket ainda nao foi criado, entao criar em MEMORIA
			Bucket novoBucket = new Bucket(this.tabelaJuncao, idBucket, this.total);
			novoBucket.addLinha(tupla);
			this.bucketsMemoria.put(idBucket, novoBucket);
			
			Principal.labelBucketMemoria(this.bucketsMemoria.size(),tabelaJuncao.getNumTabela());

		}
	}


		
	private void retirarBucketMemoriaParaDisco(int idBucket) throws IOException {
		this.bucketsDisco.put(idBucket, this.bucketsMemoria.get(idBucket));
		this.bucketsMemoria.remove(idBucket);
		
		// Alterar tipo armazenamento do Bucket para Disco
		this.bucketsDisco.get(idBucket).setTipoLocalBucket(TipoLocalBucket.DISCO);
		this.bucketsDisco.get(idBucket).mandarTuplasMemoriaParaDisco();
		
		Principal.labelBucketMemoria(this.bucketsMemoria.size(),tabelaJuncao.getNumTabela());
		Principal.labelBucketDisco(this.bucketsDisco.size(),tabelaJuncao.getNumTabela());
	}
	
	public boolean retirarBucketDiscoParaMemoria(int idBucket) throws IOException {
		Bucket bkt = this.bucketsDisco.get(idBucket);
		boolean retiradaDisco = false;
		if (bkt.getTipoLocalBucket() == TipoLocalBucket.DISCO) {
			this.bucketsMemoria.put(idBucket, bkt);
			this.bucketsDisco.remove(idBucket);
			
			// Alterar tipo armazenamento do Bucket para Memoria
			retiradaDisco = this.bucketsMemoria.get(idBucket).mandarTuplasDiscoParaMemoria();
			this.bucketsMemoria.get(idBucket).setTipoLocalBucket(TipoLocalBucket.MEMORIA);
			
			Principal.labelBucketMemoria(this.bucketsMemoria.size(),tabelaJuncao.getNumTabela());
			Principal.labelBucketDisco(this.bucketsDisco.size(),tabelaJuncao.getNumTabela());
		}
		return retiradaDisco;
	}

	private boolean existeBucket(TipoLocalBucket tipoLocal, int idBucket) {
		switch (tipoLocal) {
		case MEMORIA:
			return this.bucketsMemoria.containsKey(idBucket);
		case DISCO:
			return this.bucketsDisco.containsKey(idBucket);
		default:
			return false;
		}
	}
	
//	private static int MAGIC2 = 4;
//	private int gerarIdBucket (String key) {
//		int h = 0;
//		for (int k=0; k<key.length(); k++) {
//			h = MAGIC2 * h + (int) key.charAt(k);
//		}
//		return h << 1 >>> 1;
//	}
	
	private int gerarIdBucket(String key) {
	    int hash = key.hashCode();
	    return hash;
//	    return Math.abs(hash % this.total);
	}
	
	public Bucket obterBucket(TipoLocalBucket tipoLocal,int id){
		Bucket bucket = null;
		switch (tipoLocal) {
		case MEMORIA:
			bucket = this.bucketsMemoria.get(id);
			
			//Remover Bucket se não tiver tupla
			if (bucket != null)
			if (bucket.getTotalLinhasAlocadasMemoria() == 0) {
				bucket = null;
				this.bucketsMemoria.remove(id);
				Principal.labelBucketMemoria(1, this.bucketsMemoria.size());
			}
			break;
		case DISCO:		
			bucket = this.bucketsDisco.get(id);
			break;
		}
		return bucket;
	}
	
	public synchronized void remover_bucket(Integer idBucket) {
		 this.bucketsMemoria.remove(idBucket);
		 Principal.labelBucketMemoria(this.bucketsMemoria.size(),tabelaJuncao.getNumTabela());
	}
	
	public synchronized ArrayList<Integer> bucketsEmMemoria() {
		
		//Lista KEYS de Bucket em memoria		
		ArrayList<Integer> s = new ArrayList<Integer>();
		
		if (this.bucketsMemoria.size()>0) {	
			Iterator<Entry<Integer, Bucket>> bi 			= this.bucketsMemoria.entrySet().iterator();
			while (bi.hasNext()) { 
				Entry<Integer, Bucket> retiradoBucketMemoria 	= bi.next();
				s.add(retiradoBucketMemoria.getKey());
			}
		}
		return s;
	}

	public synchronized ArrayList<Integer> bucketsEmDisco() {
		
		//Lista KEYS de Bucket em disco		
		ArrayList<Integer> s = new ArrayList<Integer>();
		
		if (this.bucketsDisco.size()>0) {	
			Iterator<Entry<Integer, Bucket>> bi 			= this.bucketsDisco.entrySet().iterator();
			while (bi.hasNext()) { 
				Entry<Integer, Bucket> retiradoBucketDisco 	= bi.next();
				s.add(retiradoBucketDisco.getKey());
			}
		}
		return s;
	}
	public int getTotal() {
		return total;
	}

	public int getLinhas() {
		return linhas;
	}
	public TabelaJuncao getTabelaJuncao() {
		return this.tabelaJuncao;
	}
}
