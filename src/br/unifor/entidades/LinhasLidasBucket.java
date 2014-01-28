package br.unifor.entidades;

public class LinhasLidasBucket {
	private Integer linha,lida;
	public LinhasLidasBucket(Integer linha, Integer lida) {
		this.lida = lida;
		this.linha = linha;
	}
	public Integer getLinha() {
		return linha;
	}
	public void setLinha(Integer linha) {
		this.linha = linha;
	}
	public Integer getLida() {
		return lida;
	}
	public void setLida(Integer lida) {
		this.lida = lida;
	}
	
	

}
