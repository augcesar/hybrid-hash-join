package br.unifor.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DebugGraphics;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.SwingUtilities;

import br.unifor.dao.TabelasBancoDados;
import br.unifor.entidades.*;
import br.unifor.exception.BucketNaoEncontrado;
import br.unifor.exception.BucketSemEspaco;
import br.unifor.negocio.GerenciadorBucket;
import br.unifor.negocio.GerenciadorHashJoin;
import br.unifor.negocio.Logger;


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
public class Principal extends javax.swing.JFrame {
	private JPanel painelPrincipal;
	private JLabel labelLinhasBucket;
	private JComboBox inputLinhasBucket;
	private JSeparator separadorTabelas;
	private JComboBox selectTabela1;
	private JComboBox selectTabela2;
	private JMenuItem pararJuncao;
	private JLabel jLabel12;
	private JLabel jLabel11;
	private static JLabel labelBucketTotal2;
	private static JLabel labelBucketDisco2;
	private JLabel jLabel10;
	private JLabel jLabel9;
	private static JLabel labelBucketMemoria2;
	private JLabel jLabel8;
	private static JLabel labelBucketTotal;
	private static JLabel labelBucketDisco;
	private static JLabel labelBucketMemoria;
	private JLabel jLabel5;
	private JLabel jLabel4;
	private JLabel jLabel3;
	private JPanel jPanelEstatistica;
	private JTable jTable1;
	private JTable jTable2;
	private static JTextArea jTextAreaLog;
	private JScrollPane jScrollPane1;
	private JPanel jPanel1;
	private JPanel fieldColunas;
	private JMenuItem atualizarTabelas;
	private JMenu menuOpcoes;
	private JMenu Sobre;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JMenuBar jMenuBar1;
	private JPanel fieldTables;
	private JPanel fieldInputs;
	private JButton gerarHashHybridJoin;
	private JComboBox inputBucket;
	private JLabel labelBucket;
	private Logger logger;
	private GerenciadorHashJoin gerenciadorHashJoin;
	private TabelasJuncaoR tabJuncaoR;
	private TabelasJuncaoS tabJuncaoS;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Principal inst = new Principal();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public Principal() {
		super();
		initGUI();
		logger = new Logger(this);
		logger.setLogPrincipal("Iniciando...");
		
		// Limpar Buckets em Disco		
		GerenciadorHashJoin.limparTuplasDisco();
		
		// Preencher Combobox listando as tabelas do banco
		initObterTabelasBanco();
		tabJuncaoR = new TabelasJuncaoR();
		tabJuncaoS = new TabelasJuncaoS();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initObterTabelasBanco() {
		
		Vector<String> tabelas = new Vector<String>();
		tabelas.add("Selecione uma Tabela");
		for (String tabela : TabelasBancoDados.arrayListTables()) {
			tabelas.add(tabela);
		}
		
		selectTabela1.setModel(new DefaultComboBoxModel(tabelas));
		selectTabela2.setModel(new DefaultComboBoxModel(tabelas));
		
		logger.setLogPrincipal("Tabelas encontradas: " + tabelas.toString());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Hybrid Hash Join");
			this.setResizable(false);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					thisWindowClosing(evt);
				}
			});
			{
				painelPrincipal = new JPanel();
				getContentPane().add(painelPrincipal, BorderLayout.CENTER);
				painelPrincipal.setPreferredSize(new java.awt.Dimension(1030, 704));
				painelPrincipal.setLayout(null);
				{
					fieldTables = new JPanel();
					fieldTables.setLayout(null);
					painelPrincipal.add(fieldTables);
					fieldTables.setBounds(12, 9, 996, 253);
					fieldTables.setBorder(BorderFactory.createTitledBorder(null, "Tabelas de Junção", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
					{
						separadorTabelas = new JSeparator();
						fieldTables.add(separadorTabelas);
						separadorTabelas.setOrientation(SwingConstants.VERTICAL);
						separadorTabelas.setBounds(498, 23, 9, 213);
					}
					{
						ComboBoxModel selectTabela1Model = 
								new DefaultComboBoxModel(
										new String[] { "Buscando Tabelas" });
						selectTabela1 = new JComboBox();
						fieldTables.add(selectTabela1);
						selectTabela1.setModel(selectTabela1Model);
						selectTabela1.setBounds(27, 54, 453, 28);
						selectTabela1.addItemListener(new ItemListener() {
							public void itemStateChanged(ItemEvent evt) {
								selectTabela1ItemStateChanged(evt);
							}
						});
					}
					{
						ComboBoxModel selectTabela2Model = 
								new DefaultComboBoxModel(
										new String[] { "Buscando Tabelas" });
						selectTabela2 = new JComboBox();
						fieldTables.add(selectTabela2);
						selectTabela2.setModel(selectTabela2Model);
						selectTabela2.setBounds(526, 54, 453, 28);
						selectTabela2.addItemListener(new ItemListener() {
							public void itemStateChanged(ItemEvent evt) {
								selectTabela2ItemStateChanged(evt);
							}
						});
					}
					{
						jLabel1 = new JLabel();
						fieldTables.add(jLabel1);
						jLabel1.setText("Tabela");
						jLabel1.setBounds(27, 31, 45, 21);
					}
					{
						jLabel2 = new JLabel();
						fieldTables.add(jLabel2);
						jLabel2.setText("Tabela");
						jLabel2.setBounds(526, 33, 45, 21);
					}
					{
						fieldColunas = new JPanel();
						fieldTables.add(fieldColunas);
						fieldColunas.setBounds(26, 86, 454, 144);
						fieldColunas.setLayout(null);
						fieldColunas.setBorder(BorderFactory.createTitledBorder("Colunas"));
						{
							TableModel jTable1Model = 
									new DefaultTableModel(
											new String[][] { { "Selecione uma Tabela"} },
											new String[] { "Coluna" });
							jTable1 = new JTable();
							fieldColunas.add(jTable1);
							jTable1.setModel(jTable1Model);
							jTable1.setBounds(12, 24, 430, 109);						
							jTable1.setEnabled(false);
							jTable1.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent evt) {
									jTableMouseClicked(1,evt);
								}
							});
						}
					}
					{
						jPanel1 = new JPanel();
						fieldTables.add(jPanel1);
						jPanel1.setBounds(527, 87, 452, 143);
						jPanel1.setBorder(BorderFactory.createTitledBorder("Colunas"));
						jPanel1.setLayout(null);
						{
							TableModel jTable2Model = 
									new DefaultTableModel(
											new String[][] { { "Selecione uma Tabela"} },
											new String[] { "Coluna" });
							jTable2 = new JTable();
							jPanel1.add(jTable2);
							jTable2.setModel(jTable2Model);
							jTable2.setEnabled(false);
							jTable2.setBounds(9, 25, 431, 108);
							jTable2.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent evt) {
									jTableMouseClicked(2,evt);
								}
							});
						}
					}
				}
				{
					fieldInputs = new JPanel();
					painelPrincipal.add(fieldInputs);
					fieldInputs.setLayout(null);
					fieldInputs.setBounds(17, 270, 266, 137);
					fieldInputs.setBorder(BorderFactory.createTitledBorder("Opções"));
					{
						gerarHashHybridJoin = new JButton();
						fieldInputs.add(gerarHashHybridJoin);
						gerarHashHybridJoin.setText("HashJoin");
						gerarHashHybridJoin.setBounds(150, 95, 103, 28);
						gerarHashHybridJoin.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								gerarHashHybridJoinMouseClicked(evt);
							}
						});
					}
					{
						ComboBoxModel inputLinhasBucketModel = 
								new DefaultComboBoxModel(
										new String[] { "2","10","30","50","200","300", "500", "1000", "1500" });
						inputLinhasBucket = new JComboBox();
						fieldInputs.add(inputLinhasBucket);
						inputLinhasBucket.setModel(inputLinhasBucketModel);
						inputLinhasBucket.setBounds(113, 61, 140, 28);
					}
					{
						labelLinhasBucket = new JLabel();
						fieldInputs.add(labelLinhasBucket);
						labelLinhasBucket.setText("Linhas Bucket:");
						labelLinhasBucket.setBounds(18, 65, 105, 21);
					}
					{
						ComboBoxModel inputBucketModel = 
								new DefaultComboBoxModel(
										new String[] { "2", "5", "10", "15","50", "100", "300", "500", "700", "900", "1000" });
						inputBucket = new JComboBox();
						fieldInputs.add(inputBucket);
						inputBucket.setModel(inputBucketModel);
						inputBucket.setBounds(88, 28, 165, 28);
						inputBucket.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
					}
					{
						labelBucket = new JLabel();
						fieldInputs.add(labelBucket);
						labelBucket.setText("Buckets:");
						labelBucket.setBounds(18, 32, 90, 21);
					}
				}
				{
					jScrollPane1 = new JScrollPane();
					painelPrincipal.add(jScrollPane1);
					jScrollPane1.setBounds(19, 423, 989, 292);
					jScrollPane1.setBorder(BorderFactory.createTitledBorder("Log"));
					{
						jTextAreaLog = new JTextArea();
						jScrollPane1.setViewportView(jTextAreaLog);
						jTextAreaLog.setEditable(false);
						jTextAreaLog.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								jTextAreaLogMouseClicked(evt);
							}
						});
					}
				}
				{
					jPanelEstatistica = new JPanel();
					painelPrincipal.add(jPanelEstatistica);
					jPanelEstatistica.setBounds(306, 271, 702, 134);
					jPanelEstatistica.setBorder(BorderFactory.createTitledBorder("Estatística"));
					jPanelEstatistica.setLayout(null);
					{
						jLabel3 = new JLabel();
						jPanelEstatistica.add(jLabel3);
						jLabel3.setText("Bucket Memória:");
						jLabel3.setBounds(23, 38, 120, 21);
					}
					{
						jLabel4 = new JLabel();
						jPanelEstatistica.add(jLabel4);
						jLabel4.setText("Bucket Disco:");
						jLabel4.setBounds(23, 70, 88, 21);
					}
					{
						jLabel5 = new JLabel();
						jPanelEstatistica.add(jLabel5);
						jLabel5.setText("Bucket Total:");
						jLabel5.setBounds(23, 102, 85, 21);
					}
					{
						labelBucketMemoria = new JLabel();
						jPanelEstatistica.add(labelBucketMemoria);
						labelBucketMemoria.setText("0");
						labelBucketMemoria.setBounds(143, 38, 22, 21);
					}
					{
						labelBucketDisco = new JLabel();
						jPanelEstatistica.add(labelBucketDisco);
						labelBucketDisco.setText("0");
						labelBucketDisco.setBounds(143, 70, 22, 21);
					}
					{
						labelBucketTotal = new JLabel();
						jPanelEstatistica.add(labelBucketTotal);
						labelBucketTotal.setText("0");
						labelBucketTotal.setBounds(143, 102, 22, 21);
					}
					{
						jLabel8 = new JLabel();
						jPanelEstatistica.add(jLabel8);
						jLabel8.setText("/");
						jLabel8.setBounds(170, 38, 10, 21);
					}
					{
						labelBucketMemoria2 = new JLabel();
						jPanelEstatistica.add(labelBucketMemoria2);
						labelBucketMemoria2.setText("0");
						labelBucketMemoria2.setBounds(193, 38, 30, 21);
					}
					{
						jLabel9 = new JLabel();
						jPanelEstatistica.add(jLabel9);
						jLabel9.setText("/");
						jLabel9.setBounds(170, 70, 10, 21);
					}
					{
						jLabel10 = new JLabel();
						jPanelEstatistica.add(jLabel10);
						jLabel10.setText("/");
						jLabel10.setBounds(170, 102, 10, 21);
					}
					{
						labelBucketDisco2 = new JLabel();
						jPanelEstatistica.add(labelBucketDisco2);
						labelBucketDisco2.setText("0");
						labelBucketDisco2.setBounds(192, 70, 31, 21);
					}
					{
						labelBucketTotal2 = new JLabel();
						jPanelEstatistica.add(labelBucketTotal2);
						labelBucketTotal2.setText("0");
						labelBucketTotal2.setBounds(192, 102, 31, 21);
					}
					{
						jLabel11 = new JLabel();
						jPanelEstatistica.add(jLabel11);
						jLabel11.setText("R");
						jLabel11.setBounds(143, 13, 10, 21);
					}
					{
						jLabel12 = new JLabel();
						jPanelEstatistica.add(jLabel12);
						jLabel12.setText("S");
						jLabel12.setBounds(193, 13, 10, 21);
					}
				}
			}
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					menuOpcoes = new JMenu();
					jMenuBar1.add(menuOpcoes);
					menuOpcoes.setText("Opções");
					menuOpcoes.setBounds(43, 0, 67, 29);
					{
						atualizarTabelas = new JMenuItem();
						menuOpcoes.add(atualizarTabelas);
						atualizarTabelas.setText("Atualizar Tabelas");
						atualizarTabelas.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								atualizarTabelasActionPerformed(evt);
							}
						});
					}
					{
						pararJuncao = new JMenuItem();
						menuOpcoes.add(pararJuncao);
						pararJuncao.setText("Parar Junção");
						pararJuncao.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								pararJuncaoActionPerformed(evt);
							}
						});
					}
				}
				{
					Sobre = new JMenu();
					jMenuBar1.add(Sobre);
					Sobre.setText("Sobre");
				}
			}
			pack();
			this.setSize(1036, 816);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setLog(String mensagem) {
		String texto = jTextAreaLog.getText();
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH.mm.ss");
		texto += formatter.format(new Date());
		texto += ": "+mensagem;

		jTextAreaLog.setText(texto + "\n");
	}
	
	private void selectTabela1ItemStateChanged(ItemEvent evt) {
		selectTabela(1,evt);
	}
	private void selectTabela2ItemStateChanged(ItemEvent evt) {
		selectTabela(2,evt);
	}
	
	private void selectTabela (int tabela,ItemEvent evt) {

		if (evt.getStateChange() == 1) {
			String tabelaSelecionada = "";
			tabelaSelecionada = evt.getItem().toString();
			
			DefaultTableModel tableModel = new DefaultTableModel();
			tableModel.addColumn("Coluna 1");
			
			ArrayList<String> lista = TabelasBancoDados.arrayListColumnsTables(tabelaSelecionada);
			
			
			for (String coluna : lista)
				tableModel.addRow(new String[] {coluna});
			
			switch (tabela) {
			case 1:
				tabJuncaoR.setTabela(tabelaSelecionada);
				jTable1.setModel(tableModel);
				jTable1.setEnabled(true);
				break;
			case 2:
				tabJuncaoS.setTabela(tabelaSelecionada);
				jTable2.setModel(tableModel);
				jTable2.setEnabled(true);
				break;
			}
			if (lista.size() == 0)
				return;
			else
				logger.setLogPrincipal("Tabela"+tabela+" Selecionada: "+ tabelaSelecionada);
		}
	}

	private void atualizarTabelasActionPerformed(ActionEvent evt) {
		initObterTabelasBanco();
	}
	

	private void jTableMouseClicked(int tabela, MouseEvent evt) {
	      JTable target = (JTable)evt.getSource();
	      int row 		= target.getSelectedRow();
	      int column 	= target.getSelectedColumn();
	      if (row > -1 && column > -1) {
	    	  switch (tabela) {
	    	  case 1:
	    		  tabJuncaoR.setColuna(target.getValueAt(row,column).toString());
	    		  logger.setLogPrincipal("Coluna Tabela"+tabela+" selecionada: "+tabJuncaoR.getColuna());
	    		break;
	    	  case 2:
	    		  tabJuncaoS.setColuna(target.getValueAt(row,column).toString());
	    		  logger.setLogPrincipal("Coluna Tabela"+tabela+" selecionada: "+tabJuncaoS.getColuna());
				break;
			}	    	  
	      }
		}
	
	private void gerarHashHybridJoinMouseClicked(MouseEvent evt) {
		int totalBuckets 	= Integer.parseInt(inputBucket.getSelectedItem().toString());
		int linhasBuckets 	= Integer.parseInt(inputLinhasBucket.getSelectedItem().toString());
		if (tabJuncaoR.getValido() && tabJuncaoS.getValido()) {
			gerenciadorHashJoin = new GerenciadorHashJoin(tabJuncaoR,tabJuncaoS,totalBuckets, linhasBuckets);
			gerenciadorHashJoin.start();
		}else {
			logger.setLogPrincipal("Tabelas de junção não selecionadas");
		}
	}

	private void thisWindowClosing(WindowEvent evt) {
		if (gerenciadorHashJoin != null) {
			gerenciadorHashJoin.pararThreads();
		}
	}
	
	
	
	// LOG E ESTATISTICA
	
//	private JLabel labelTuplaProcessada;
//	private JLabel labelTuplaAdicionada;
//	private JLabel labelBucketTotal;
//	private JLabel labelBucketDisco;
//	private JLabel labelBucketMemoria;

	public static void labelBucketDisco(Integer num, Integer numTabela) {
		if(numTabela == 1)
			labelBucketDisco.setText(num.toString());
		else
			labelBucketDisco2.setText(num.toString());
		labelBucketTotal();
	}	

	public static void labelBucketMemoria(Integer num, Integer numTabela) {
		if(numTabela == 1)
			labelBucketMemoria.setText(num.toString());
		else
			labelBucketMemoria2.setText(num.toString());
		
		labelBucketTotal();
	}
	
	private static void labelBucketTotal() {
		Integer cont = Integer.parseInt(labelBucketMemoria.getText()) + Integer.parseInt(labelBucketDisco.getText());
		labelBucketTotal.setText(cont.toString());

		Integer cont2 = Integer.parseInt(labelBucketMemoria2.getText()) + Integer.parseInt(labelBucketDisco2.getText());
		labelBucketTotal2.setText(cont2.toString());
	}

	private void pararJuncaoActionPerformed(ActionEvent evt) {
		labelBucketDisco.setText("0");
		labelBucketDisco2.setText("0");
		labelBucketMemoria.setText("0");
		labelBucketMemoria2.setText("0");
		labelBucketTotal.setText("0");
		labelBucketTotal2.setText("0");
		
		if (gerenciadorHashJoin != null) {
			gerenciadorHashJoin.pararThreads();
		}		
		GerenciadorHashJoin.limparTuplasDisco();
		
		Principal.setLog(" Threads de Junção paradas.");
		
	}
	
	private void jTextAreaLogMouseClicked(MouseEvent evt) {
		if (evt.getClickCount() == 2)
			jTextAreaLog.setText("");
	}

}
