package br.unifor.entidades;

public class TabelasJuncaoS implements TabelaJuncao {
	private String tabelaS,colunaS;
	
	public TabelasJuncaoS(){
	}

	public void setTabela(String tabelaS) {
		this.tabelaS = tabelaS;
	}

	public void setColuna(String colunaS) {
		this.colunaS = colunaS;
	}


	public String getTabela() {
		return tabelaS;
	}

	public String getColuna() {
		return colunaS;
	}
	
	@Override
	public boolean getValido() {
		return colunaS != "" && tabelaS != "";
	}

	@Override
	public int getNumTabela() {
		return 2;
	}
}
