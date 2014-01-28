package br.unifor.negocio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import br.unifor.dao.BancoDados;
import br.unifor.entidades.*;
import br.unifor.exception.BucketNaoEncontrado;
import br.unifor.gui.Principal;

public class ConstrucaoBucketThread extends Thread {
	private GerenciadorBucket gerenciadorBucket;
	
	public ConstrucaoBucketThread(GerenciadorBucket gerenciadorBucket) {
		this.gerenciadorBucket 	= gerenciadorBucket;
	}
	
	public void run() {
		try {
			Connection c 	= BancoDados.abrirConexao();
			Statement stm 	= c.createStatement();
			String sql 		= "SELECT "+ this.gerenciadorBucket.getTabelaJuncao().getColuna() +" as attribute_join,*  FROM " + this.gerenciadorBucket.getTabelaJuncao().getTabela();
			
			ResultSet rs	= stm.executeQuery(sql);
					
			while(rs.next()) {
				try {
					gerenciadorBucket.addTuplaBucket( rs.getString("attribute_join") , criarArrayListParaTupla(rs) );
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (BucketNaoEncontrado e) {
			e.printStackTrace();
		}
		
	}

	private Tupla criarArrayListParaTupla(ResultSet rs) throws SQLException {
		ArrayList<String> colunaEdados = new ArrayList<String>();
		
		// 1 - Pegar todoas as colunas da ResultSet
		// EX: coluna1,coluna2,coluna3		
		
		// 2 - Pegar todos os dados da ResultSet
		// EX: dado1,dado2,dado3
		
		// Resultado: 
		// Posicao 0: Colunas
		// Posicao 1: Dados
		
		// colunaEdados.add(colunas);
		// colunaEdados.add(dados);		

		String colunas 	= "";
		String dados 	= "";
		ResultSetMetaData rsmd = rs.getMetaData();
		
		for (int i = 2; i <= rsmd.getColumnCount(); i++) {
			colunas +=  rsmd.getColumnName(i);
			if (i != rsmd.getColumnCount())
				colunas += ",";
		}		

		for (int i = 2; i <= rsmd.getColumnCount(); i++) {
			dados +=  rs.getString(i);
			if (i != rsmd.getColumnCount())
				dados += ",";
		}		

		colunaEdados.add(colunas);
		colunaEdados.add(dados);
		
		return new Tupla(colunaEdados,rs.getString("attribute_join"));
	}
}
