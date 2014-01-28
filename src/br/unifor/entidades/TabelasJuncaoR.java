package br.unifor.entidades;

public class TabelasJuncaoR implements TabelaJuncao {
	private String tabelaR,colunaR;
	
	public TabelasJuncaoR(){
	}
	
	public void setTabela(String tabelaR) {
		this.tabelaR = tabelaR;
	}

	public void setColuna(String colunaR) {
		this.colunaR = colunaR;
	}

	public String getTabela() {
		return tabelaR;
	}

	public String getColuna() {
		return colunaR;
	}

	@Override
	public boolean getValido() {
		return colunaR != "" && tabelaR != "";
	}

	@Override
	public int getNumTabela() {
		return 1;
	}
}
