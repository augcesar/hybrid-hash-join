package br.unifor.dao;

import  java.sql.*; 

import javax.swing.JOptionPane;

public class BancoDados {
	
	static {
		try {
			Class.forName( "org.postgresql.Driver" );
		} catch (ClassNotFoundException e) {
			System.out.println("Erro ao carregar o Driver.");
			e.printStackTrace();
		}
	}
	
	public static Connection abrirConexao() {
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:postgresql://192.168.247.130:5432/hybridhashjoin","hybridhashjoin","hybridhashjoin");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),"Banco de Dados", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			e.printStackTrace();
		} 
		return con;
	}


}
