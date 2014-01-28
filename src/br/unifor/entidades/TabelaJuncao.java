package br.unifor.entidades;

public interface TabelaJuncao {
	public void setTabela(String tabelaR);
	public void setColuna(String colunaR);
	public String getTabela();
	public String getColuna();
	public boolean getValido();
	public int getNumTabela();
}
