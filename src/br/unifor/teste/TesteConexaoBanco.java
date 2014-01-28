package br.unifor.teste;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.unifor.dao.BancoDados;
import br.unifor.dao.TabelasBancoDados;

public class TesteConexaoBanco {

	public TesteConexaoBanco() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
//		Connection c = BancoDados.abrirConexao();
//		Statement stm = c.createStatement();
//		ResultSet rs = stm.executeQuery("select descricao from teste");	
//		 while(rs.next()) 
//			System.out.println("descricao: " + rs.getString("descricao"));
//			 
//		c.close();
//		
		
		System.out.println(TabelasBancoDados.arrayListRecordsTables("teste","id").get(0));

	}

}
