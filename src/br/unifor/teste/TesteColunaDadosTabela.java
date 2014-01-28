package br.unifor.teste;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import br.unifor.dao.BancoDados;

public class TesteColunaDadosTabela {

	public static void main(String[] args) throws SQLException {
		
		String colunasTabela 	= "";
		String registroTabela 	= "";
		
		Connection c 	= BancoDados.abrirConexao();
		Statement stm 	= c.createStatement();
		ResultSet rs = stm.executeQuery("select id,* from teste limit 1");	
		while(rs.next()) {
			ResultSetMetaData rsmd = rs.getMetaData(); 
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				colunasTabela +=  rsmd.getColumnName(i);
				if (i != rsmd.getColumnCount())
					colunasTabela += ",";
			}		
		}
		rs.close();
		rs = stm.executeQuery("select * from teste");	
		
		
		while(rs.next()) {
			ResultSetMetaData rsmd = rs.getMetaData(); 
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				registroTabela +=  rs.getString(i);
				if (i != rsmd.getColumnCount())
					registroTabela += ",";
			}		
		}
		
		
		c.close();
		
		System.err.println(colunasTabela);
		
	}

}
