package br.unifor.gui;
import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.SwingUtilities;

import br.unifor.entidades.Tupla;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class Resultado extends javax.swing.JFrame {
	private JPanel jPanel1;
	private JScrollPane jScrollPane1;
	private JTable jTable1;
	private DefaultTableModel tableModel;
	private int contadorLinhas;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Resultado inst = new Resultado();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public Resultado() {
		
		super();
		initGUI();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		contadorLinhas = 0;
		tableModel = new DefaultTableModel();
	}
	public void adicionarTupla(Tupla tupla1,Tupla tupla2) {
		
		// Criando Colunas	
		if (tableModel.getColumnCount() < 1) {
			tableModel.addColumn("#");
			for (String string : tupla1.getTupla().get(0).split(","))
				tableModel.addColumn(string);
			
			for (String string : tupla2.getTupla().get(0).split(","))
				tableModel.addColumn(string);
		}
		// Criando Linhas		
		String l1l2 = (++contadorLinhas) +  "," + tupla1.getTupla().get(1) + "," + tupla2.getTupla().get(1);
		tableModel.addRow(l1l2.split(","));
		
		
		jTable1.setModel(tableModel);

	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Resultado Junção");
			this.setResizable(false);
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(null);
				{
					jScrollPane1 = new JScrollPane();
					jPanel1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 0, 992, 864);
					{
						jTable1 = new JTable();
						jScrollPane1.setViewportView(jTable1);
					}
				}

			}
			pack();
			this.setSize(998, 899);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

}
