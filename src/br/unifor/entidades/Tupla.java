package br.unifor.entidades;

import java.util.ArrayList;

public class Tupla {
	private ArrayList<String> tupla;
	private Object attributoJuncao;
	
	public Tupla(ArrayList<String> tupla,Object attributoJuncao) {
		this.tupla 				= tupla;
		this.attributoJuncao	= attributoJuncao;
	}
	
	public ArrayList<String> getTupla() {
		return this.tupla;
	}
	
	public Object getColunaJuncao() {
		return this.attributoJuncao;
	}
	
	public  String toString() {
		return "Coluna: "+ tupla.get(0) + "\nRegistro: "+tupla.get(1);
	}
}
