package br.unifor.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TabelasBancoDados {
	public static ArrayList<String> arrayListTables() {
		ArrayList<String> list = new ArrayList<String>();
		try {
			Connection c = BancoDados.abrirConexao();
			Statement stm = c.createStatement();
			ResultSet rs = stm.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'");
				while (rs. next())
					list.add(rs.getString("table_name"));
			stm.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ArrayList<String> arrayListColumnsTables(String table) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			Connection c = BancoDados.abrirConexao();
			Statement stm = c.createStatement();
			ResultSet rs = stm.executeQuery("SELECT column_name,data_type FROM information_schema.columns WHERE table_schema = 'public'  AND table_name = '"+table+"'");
				while (rs. next())
					list.add(rs.getString("column_name"));
			stm.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<String> arrayListRecordsTables(String table, String attribute_join) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			Connection c = BancoDados.abrirConexao();
			Statement stm = c.createStatement();
			ResultSet rs = stm.executeQuery("SELECT "+attribute_join+" as attribute_join FROM "+table);
				while (rs. next())
					list.add(rs.getString("attribute_join"));
			stm.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}	
}
