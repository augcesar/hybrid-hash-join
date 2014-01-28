package br.unifor.entidades;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import br.unifor.exception.*;
import br.unifor.gui.Principal;

public class Bucket {
	private TabelaJuncao tabelaJuncao 	= null;
	private Integer idBucket			= -1;
	private int totalLinhas 			= 0;
	private ArrayList<Tupla> linhas 	= null;
	private TipoLocalBucket tipoLocalBucket;
	private String diretorioBucketDisco = "./tuplas_disco" + File.separator;
	
	private boolean ja_construido;

	public Bucket(TabelaJuncao tabelaJuncao, Integer idBucket, int totalLinhas) {
		this.tabelaJuncao		= tabelaJuncao;
		this.idBucket			= idBucket;
		this.totalLinhas 		= totalLinhas;
		this.linhas 			= new ArrayList<Tupla>();
		this.tipoLocalBucket	= TipoLocalBucket.MEMORIA;
		this.ja_construido		= false;

	}

	public void addLinha(Tupla tupla) throws IOException{

		//Bucket Memoria: gravar no ArrayList
		//Bucket Disco: gravar em Disco 		
		switch (this.tipoLocalBucket) {
		case MEMORIA:
			linhas.add(tupla);
			break;

		case DISCO:

			mandarTuplasMemoriaParaDisco();

			// Pegar Tupla passada por parametro e jogar em disco
			addTuplaDisco(tupla);
			
			break;
		}
	}
	
	public synchronized void mandarTuplasMemoriaParaDisco() throws IOException {
		
		// Se o o bucket estiver em disco.		
		if (this.tipoLocalBucket == TipoLocalBucket.DISCO) {
			// Pega o que tem no ArrayList linhas e jogar para DISCO
			if (this.linhas.size() > 0) {
				for (Tupla tuplaMemoria : this.linhas) {
					addTuplaDisco(tuplaMemoria);
				}
				
				//	Limpa as linhas que estavam e memoria			
				this.linhas = new ArrayList<Tupla>();
			}
		}
	}
	

	public synchronized boolean mandarTuplasDiscoParaMemoria() throws IOException  {
		// Se o o bucket estiver em memoria.		
		if (this.tipoLocalBucket == TipoLocalBucket.DISCO) {
			// Pegar linhas do disco e jogar para Memoria
			
			ArrayList<String> tuplaDados = new ArrayList<String>();
			File ftabelas 			= new File(diretorioBucketDisco + tabelaJuncao.getTabela(),this.idBucket.toString());
			
			String[] listTuplas  = ftabelas.list();
			for (int k = 0; k < listTuplas.length; k++) {
				File f_aux			= new File(ftabelas,listTuplas[k]);	
				
				FileReader fr 		= new FileReader(f_aux);
				BufferedReader br 	= new BufferedReader(fr);
				
				tuplaDados.add(br.readLine());
				tuplaDados.add(br.readLine());
				
				String colunaJuncao = br.readLine();
				
				br.close();
				fr.close();
//				f_aux.delete();
				
				this.linhas.add(new Tupla(tuplaDados, colunaJuncao )) ;
			}		
			ftabelas.delete();
			return true;
		}
		return false;
	}
	
	private void addTuplaDisco(Tupla tupla) throws IOException{

		// Como guardar as tuplas ?
		// tuplas_disco/idBucket/tupla.hashCode()
		// EX: tuplas_disco/8/1954411564.txt
		
		// Dentro guarda 
		// Linha 0 - Colunas
		// Linha 1 - Dados
		// Linha 2 - Junção
			
		File f = new File(this.diretorioBucketDisco + this.tabelaJuncao.getTabela(), this.idBucket.toString());
//		System.out.println("Path Tupla Disco: " + f.getPath());
		if (!f.exists())
			f.mkdirs();
		f = new File(this.diretorioBucketDisco + this.tabelaJuncao.getTabela() + File.separator + this.idBucket.toString(), tupla.hashCode()+".txt");
		if (!f.exists()) {
			f.createNewFile();
		}else
			System.err.println("Arquivo Já existe : "+tupla.hashCode());
		
		FileWriter fw 		=  new FileWriter(f,true);
		BufferedWriter bw 	= new BufferedWriter(fw);

		bw.write(tupla.getTupla().get(0));
		bw.newLine();
		bw.write(tupla.getTupla().get(1));
		bw.newLine();
		bw.write(tupla.getColunaJuncao().toString());
		bw.close();
		fw.close();
		
	}

	public Tupla getProximaTupla(TipoLocalBucket tipoLocalBucket, Integer linha) throws IOException{		
		Tupla tupla = null;
		
		//Bucket Memoria: ler do ArrayList
		//Bucket Disco: ler do Disco
		switch (tipoLocalBucket) {
		case MEMORIA:
			
			// Sem tuplas: Bucket todo lido
			if (linhas.size() == 0 || linhas.size() <= linha)
				return null;
			
			tupla = this.linhas.get(linha);
			
			break;
		case DISCO:

			File dir = new File(this.diretorioBucketDisco + this.tabelaJuncao.getTabela(), this.idBucket.toString());
			String[] listArquivos = dir.list();
			
			// sem arquivo no disco: Bucket todo lido
			if(listArquivos.length == 0)
				return null;
			
			File f 				= new File(this.diretorioBucketDisco + this.tabelaJuncao.getTabela() + File.separator + this.idBucket.toString(), listArquivos[0]);	
			FileReader fr 		= new FileReader(f);
			BufferedReader br 	= new BufferedReader(fr);
			
			ArrayList<String> tuplaDisco = new ArrayList<String>();
			tuplaDisco.add(br.readLine());
			tuplaDisco.add(br.readLine());
			
			br.close();
			fr.close();
			
			//Remover linha obtida			
			f.delete();
			
			tupla = new Tupla(tuplaDisco,tabelaJuncao.getColuna());	
			break;
		}		
	
		return tupla;
	}
	
	
	public synchronized void removerTodasTuplas(TipoLocalBucket tipoLocalBucket) {
		switch (tipoLocalBucket) {
		case MEMORIA:
			this.linhas = new ArrayList<Tupla>();
			break;

		case DISCO:
			break;
		}
	}
	
	
	public int getTotalLinhasAlocadasMemoria() {
		// Linha Memória		
		return this.linhas.size();
	}
	
	public TipoLocalBucket getTipoLocalBucket() {
		return tipoLocalBucket;
	}

	public void setTipoLocalBucket(TipoLocalBucket tipoLocalBucket) {
		this.tipoLocalBucket = tipoLocalBucket;
	}

	public boolean getJaConstruido() {
		return ja_construido;
	}

	public void setJaConstruido(boolean ja_construido) {
		this.ja_construido = ja_construido;
	}
	
	public  String toString() {
		return "id: " + idBucket + " linha memoria: "+ linhas.size();
	}


}
