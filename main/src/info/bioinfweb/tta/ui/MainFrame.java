/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020  Ben C. Stöver
 * <http://bioinfweb.info/TTA>
 * 
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package info.bioinfweb.tta.ui;


import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import info.bioinfweb.treegraph.gui.dialogs.CompareTextElementDataParametersPanel;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;



public class MainFrame extends JFrame {
	private JPanel contentPane;
	private JTextField outputDirectoryTextField;
	private CompareTextElementDataParametersPanel compareNamesPanel;
	private JTextField treeFileTextField;
	private JTextField referenceTreeTextField;
	private JTable expressionsTable;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 817, 888);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.NORTH);
		
		JPanel generalTab = new JPanel();
		tabbedPane.addTab("General", null, generalTab, null);
		GridBagLayout gbl_generalTab = new GridBagLayout();
		gbl_generalTab.columnWidths = new int[]{146, 493, 0, 0};
		gbl_generalTab.rowHeights = new int[]{0, 51, 0};
		gbl_generalTab.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_generalTab.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		generalTab.setLayout(gbl_generalTab);
		
		JLabel lblOutputDirectory = new JLabel("Output directory:");
		GridBagConstraints gbc_lblOutputDirectory = new GridBagConstraints();
		gbc_lblOutputDirectory.anchor = GridBagConstraints.WEST;
		gbc_lblOutputDirectory.insets = new Insets(0, 0, 5, 5);
		gbc_lblOutputDirectory.gridx = 0;
		gbc_lblOutputDirectory.gridy = 0;
		generalTab.add(lblOutputDirectory, gbc_lblOutputDirectory);
		
		outputDirectoryTextField = new JTextField();
		GridBagConstraints gbc_outputDirectoryTextField = new GridBagConstraints();
		gbc_outputDirectoryTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_outputDirectoryTextField.insets = new Insets(0, 0, 5, 5);
		gbc_outputDirectoryTextField.gridx = 1;
		gbc_outputDirectoryTextField.gridy = 0;
		generalTab.add(outputDirectoryTextField, gbc_outputDirectoryTextField);
		outputDirectoryTextField.setColumns(10);
		
		JButton selectOutputDirectoryButton = new JButton("...");
		GridBagConstraints gbc_selectOutputDirectoryButton = new GridBagConstraints();
		gbc_selectOutputDirectoryButton.insets = new Insets(0, 0, 5, 0);
		gbc_selectOutputDirectoryButton.gridx = 2;
		gbc_selectOutputDirectoryButton.gridy = 0;
		generalTab.add(selectOutputDirectoryButton, gbc_selectOutputDirectoryButton);
		selectOutputDirectoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JLabel lblComparisonOptionsFor = new JLabel("Comparison options for node names:");
		GridBagConstraints gbc_lblComparisonOptionsFor = new GridBagConstraints();
		gbc_lblComparisonOptionsFor.anchor = GridBagConstraints.WEST;
		gbc_lblComparisonOptionsFor.insets = new Insets(0, 0, 0, 5);
		gbc_lblComparisonOptionsFor.gridx = 0;
		gbc_lblComparisonOptionsFor.gridy = 1;
		generalTab.add(lblComparisonOptionsFor, gbc_lblComparisonOptionsFor);
		
		compareNamesPanel = new CompareTextElementDataParametersPanel();
		GridBagConstraints gbc_compareNamesPanel = new GridBagConstraints();
		gbc_compareNamesPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_compareNamesPanel.gridwidth = 2;
		gbc_compareNamesPanel.insets = new Insets(0, 0, 0, 5);
		gbc_compareNamesPanel.anchor = GridBagConstraints.NORTH;
		gbc_compareNamesPanel.gridx = 1;
		gbc_compareNamesPanel.gridy = 1;
		generalTab.add(compareNamesPanel, gbc_compareNamesPanel);
		
		JPanel runtimeTab = new JPanel();
		tabbedPane.addTab("Runtime", null, runtimeTab, null);
		tabbedPane.setEnabledAt(1, true);
		GridBagLayout gbl_runtimeTab = new GridBagLayout();
		gbl_runtimeTab.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0};
		gbl_runtimeTab.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		runtimeTab.setLayout(gbl_runtimeTab);
		
		JLabel lblMaximumRamUsage = new JLabel("Maximum RAM usage:");
		GridBagConstraints gbc_lblMaximumRamUsage = new GridBagConstraints();
		gbc_lblMaximumRamUsage.anchor = GridBagConstraints.WEST;
		gbc_lblMaximumRamUsage.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaximumRamUsage.gridx = 0;
		gbc_lblMaximumRamUsage.gridy = 0;
		runtimeTab.add(lblMaximumRamUsage, gbc_lblMaximumRamUsage);
		
		JRadioButton memoryAutoRadioButton = new JRadioButton("automatic");
		GridBagConstraints gbc_memoryAutoRadioButton = new GridBagConstraints();
		gbc_memoryAutoRadioButton.anchor = GridBagConstraints.WEST;
		gbc_memoryAutoRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_memoryAutoRadioButton.gridx = 1;
		gbc_memoryAutoRadioButton.gridy = 0;
		runtimeTab.add(memoryAutoRadioButton, gbc_memoryAutoRadioButton);
		
		JLabel lblMaximumParallelThreads = new JLabel("Maximum parallel threads:");
		GridBagConstraints gbc_lblMaximumParallelThreads = new GridBagConstraints();
		gbc_lblMaximumParallelThreads.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaximumParallelThreads.gridx = 0;
		gbc_lblMaximumParallelThreads.gridy = 2;
		runtimeTab.add(lblMaximumParallelThreads, gbc_lblMaximumParallelThreads);
		
		JRadioButton memoryDefinedRadioButton = new JRadioButton("as defined:");
		GridBagConstraints gbc_memoryDefinedRadioButton = new GridBagConstraints();
		gbc_memoryDefinedRadioButton.anchor = GridBagConstraints.WEST;
		gbc_memoryDefinedRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_memoryDefinedRadioButton.gridx = 1;
		gbc_memoryDefinedRadioButton.gridy = 1;
		runtimeTab.add(memoryDefinedRadioButton, gbc_memoryDefinedRadioButton);
		
		JSpinner memorySpinner = new JSpinner();
		GridBagConstraints gbc_memorySpinner = new GridBagConstraints();
		gbc_memorySpinner.weightx = 1.0;
		gbc_memorySpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_memorySpinner.insets = new Insets(0, 0, 5, 5);
		gbc_memorySpinner.gridx = 2;
		gbc_memorySpinner.gridy = 1;
		runtimeTab.add(memorySpinner, gbc_memorySpinner);
		
		JComboBox memoryUnitComboBox = new JComboBox();
		GridBagConstraints gbc_memoryUnitComboBox = new GridBagConstraints();
		gbc_memoryUnitComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_memoryUnitComboBox.gridx = 3;
		gbc_memoryUnitComboBox.gridy = 1;
		runtimeTab.add(memoryUnitComboBox, gbc_memoryUnitComboBox);
		
		JRadioButton threadsAutoRadioButton = new JRadioButton("automatic");
		GridBagConstraints gbc_threadsAutoRadioButton = new GridBagConstraints();
		gbc_threadsAutoRadioButton.anchor = GridBagConstraints.WEST;
		gbc_threadsAutoRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_threadsAutoRadioButton.gridx = 1;
		gbc_threadsAutoRadioButton.gridy = 2;
		runtimeTab.add(threadsAutoRadioButton, gbc_threadsAutoRadioButton);
		
		JRadioButton threadsDefinedRadioButton = new JRadioButton("as defined:");
		GridBagConstraints gbc_threadsDefinedRadioButton = new GridBagConstraints();
		gbc_threadsDefinedRadioButton.anchor = GridBagConstraints.WEST;
		gbc_threadsDefinedRadioButton.insets = new Insets(0, 0, 0, 5);
		gbc_threadsDefinedRadioButton.gridx = 1;
		gbc_threadsDefinedRadioButton.gridy = 3;
		runtimeTab.add(threadsDefinedRadioButton, gbc_threadsDefinedRadioButton);
		
		JSpinner threadsSpinner = new JSpinner();
		GridBagConstraints gbc_threadsSpinner = new GridBagConstraints();
		gbc_threadsSpinner.gridwidth = 2;
		gbc_threadsSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_threadsSpinner.insets = new Insets(0, 0, 0, 5);
		gbc_threadsSpinner.gridx = 2;
		gbc_threadsSpinner.gridy = 3;
		runtimeTab.add(threadsSpinner, gbc_threadsSpinner);
		
		JPanel treeListTab = new JPanel();
		tabbedPane.addTab("Tree Files", null, treeListTab, null);
		tabbedPane.setEnabledAt(2, true);
		GridBagLayout gbl_treeListTab = new GridBagLayout();
		gbl_treeListTab.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gbl_treeListTab.columnWeights = new double[]{1.0, 0.0};
		treeListTab.setLayout(gbl_treeListTab);
		
		treeFileTextField = new JTextField();
		GridBagConstraints gbc_treeFileTextField = new GridBagConstraints();
		gbc_treeFileTextField.insets = new Insets(0, 0, 5, 5);
		gbc_treeFileTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_treeFileTextField.gridx = 0;
		gbc_treeFileTextField.gridy = 0;
		treeListTab.add(treeFileTextField, gbc_treeFileTextField);
		treeFileTextField.setColumns(10);
		
		JButton selectTreeFileButton = new JButton("...");
		GridBagConstraints gbc_selectTreeFileButton = new GridBagConstraints();
		gbc_selectTreeFileButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_selectTreeFileButton.insets = new Insets(0, 0, 5, 0);
		gbc_selectTreeFileButton.gridx = 1;
		gbc_selectTreeFileButton.gridy = 0;
		treeListTab.add(selectTreeFileButton, gbc_selectTreeFileButton);
		
		JScrollPane treeFilesScrollPane = new JScrollPane();
		GridBagConstraints gbc_treeFilesScrollPane = new GridBagConstraints();
		gbc_treeFilesScrollPane.gridheight = 7;
		gbc_treeFilesScrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_treeFilesScrollPane.fill = GridBagConstraints.BOTH;
		gbc_treeFilesScrollPane.gridx = 0;
		gbc_treeFilesScrollPane.gridy = 1;
		treeListTab.add(treeFilesScrollPane, gbc_treeFilesScrollPane);
		
		JList treeFileList = new JList();
		treeFilesScrollPane.setViewportView(treeFileList);
		
		JButton addTreeFileButton = new JButton("Add");
		GridBagConstraints gbc_addTreeFileButton = new GridBagConstraints();
		gbc_addTreeFileButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_addTreeFileButton.insets = new Insets(0, 0, 5, 0);
		gbc_addTreeFileButton.gridx = 1;
		gbc_addTreeFileButton.gridy = 1;
		treeListTab.add(addTreeFileButton, gbc_addTreeFileButton);
		
		JButton replaceTreeFileButton = new JButton("Replace");
		GridBagConstraints gbc_replaceTreeFileButton = new GridBagConstraints();
		gbc_replaceTreeFileButton.insets = new Insets(0, 0, 5, 0);
		gbc_replaceTreeFileButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_replaceTreeFileButton.gridx = 1;
		gbc_replaceTreeFileButton.gridy = 2;
		treeListTab.add(replaceTreeFileButton, gbc_replaceTreeFileButton);
		
		JButton removeTreeFile = new JButton("Remove");
		GridBagConstraints gbc_removeTreeFile = new GridBagConstraints();
		gbc_removeTreeFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_removeTreeFile.insets = new Insets(0, 0, 5, 0);
		gbc_removeTreeFile.gridx = 1;
		gbc_removeTreeFile.gridy = 3;
		treeListTab.add(removeTreeFile, gbc_removeTreeFile);
		
		JButton moveUpTreeFileButton = new JButton("Move Up");
		GridBagConstraints gbc_moveUpTreeFileButton = new GridBagConstraints();
		gbc_moveUpTreeFileButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_moveUpTreeFileButton.insets = new Insets(0, 0, 5, 0);
		gbc_moveUpTreeFileButton.gridx = 1;
		gbc_moveUpTreeFileButton.gridy = 4;
		treeListTab.add(moveUpTreeFileButton, gbc_moveUpTreeFileButton);
		
		JButton moveDownTreeFileButton = new JButton("Move Down");
		GridBagConstraints gbc_moveDownTreeFileButton = new GridBagConstraints();
		gbc_moveDownTreeFileButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_moveDownTreeFileButton.insets = new Insets(0, 0, 5, 0);
		gbc_moveDownTreeFileButton.gridx = 1;
		gbc_moveDownTreeFileButton.gridy = 5;
		treeListTab.add(moveDownTreeFileButton, gbc_moveDownTreeFileButton);
		
		JButton relativeAbsoluteTreeFileButton = new JButton("Make Relative");
		GridBagConstraints gbc_relativeAbsoluteTreeFileButton = new GridBagConstraints();
		gbc_relativeAbsoluteTreeFileButton.insets = new Insets(0, 0, 5, 0);
		gbc_relativeAbsoluteTreeFileButton.gridx = 1;
		gbc_relativeAbsoluteTreeFileButton.gridy = 6;
		treeListTab.add(relativeAbsoluteTreeFileButton, gbc_relativeAbsoluteTreeFileButton);
		treeListTab.setLayout(gbl_treeListTab);
		GridBagLayout gbl_referenceTreePanel = new GridBagLayout();
		gbl_referenceTreePanel.columnWeights = new double[]{0.0, 1.0, 0.0};
		
		JPanel referenceTreePanel = new JPanel();
		GridBagConstraints gbc_referenceTreePanel = new GridBagConstraints();
		gbc_referenceTreePanel.insets = new Insets(10, 0, 0, 0);
		gbc_referenceTreePanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_referenceTreePanel.gridwidth = 2;
		gbc_referenceTreePanel.gridx = 0;
		gbc_referenceTreePanel.gridy = 8;
		treeListTab.add(referenceTreePanel, gbc_referenceTreePanel);
		referenceTreePanel.setLayout(gbl_referenceTreePanel);
		
		JCheckBox referenceTreeCheckBox = new JCheckBox("Reference Tree");
		GridBagConstraints gbc_referenceTreeCheckBox = new GridBagConstraints();
		gbc_referenceTreeCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_referenceTreeCheckBox.anchor = GridBagConstraints.NORTHWEST;
		gbc_referenceTreeCheckBox.gridx = 0;
		gbc_referenceTreeCheckBox.gridy = 0;
		referenceTreePanel.add(referenceTreeCheckBox, gbc_referenceTreeCheckBox);
		
		JComboBox referenceTreeFileComboBox = new JComboBox();
		GridBagConstraints gbc_referenceTreeFileComboBox = new GridBagConstraints();
		gbc_referenceTreeFileComboBox.gridwidth = 2;
		gbc_referenceTreeFileComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_referenceTreeFileComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_referenceTreeFileComboBox.gridx = 1;
		gbc_referenceTreeFileComboBox.gridy = 0;
		referenceTreePanel.add(referenceTreeFileComboBox, gbc_referenceTreeFileComboBox);
		
		JComboBox referenceTreeTypeComboBox = new JComboBox();
		GridBagConstraints gbc_referenceTreeTypeComboBox = new GridBagConstraints();
		gbc_referenceTreeTypeComboBox.insets = new Insets(0, 0, 0, 5);
		gbc_referenceTreeTypeComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_referenceTreeTypeComboBox.gridx = 1;
		gbc_referenceTreeTypeComboBox.gridy = 1;
		referenceTreePanel.add(referenceTreeTypeComboBox, gbc_referenceTreeTypeComboBox);
		
		referenceTreeTextField = new JTextField();
		GridBagConstraints gbc_referenceTreeTextField = new GridBagConstraints();
		gbc_referenceTreeTextField.insets = new Insets(0, 0, 0, 5);
		gbc_referenceTreeTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_referenceTreeTextField.gridx = 2;
		gbc_referenceTreeTextField.gridy = 1;
		referenceTreePanel.add(referenceTreeTextField, gbc_referenceTreeTextField);
		referenceTreeTextField.setColumns(10);
		
		JPanel expressionsTab = new JPanel();
		tabbedPane.addTab("User Expressions", null, expressionsTab, null);
		tabbedPane.setEnabledAt(3, true);
		GridBagLayout gbl_expressionsTab = new GridBagLayout();
		gbl_expressionsTab.columnWidths = new int[]{0, 0};
		gbl_expressionsTab.rowHeights = new int[]{0, 0, 0};
		gbl_expressionsTab.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_expressionsTab.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		expressionsTab.setLayout(gbl_expressionsTab);
		
		JLabel lblExpressions = new JLabel("Expressions:");
		GridBagConstraints gbc_lblExpressions = new GridBagConstraints();
		gbc_lblExpressions.anchor = GridBagConstraints.WEST;
		gbc_lblExpressions.insets = new Insets(0, 0, 5, 0);
		gbc_lblExpressions.gridx = 0;
		gbc_lblExpressions.gridy = 0;
		expressionsTab.add(lblExpressions, gbc_lblExpressions);
		
		expressionsTable = new JTable();
		expressionsTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "Expression", "Type"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		GridBagConstraints gbc_expressionsTable = new GridBagConstraints();
		gbc_expressionsTable.fill = GridBagConstraints.BOTH;
		gbc_expressionsTable.gridx = 0;
		gbc_expressionsTable.gridy = 1;
		expressionsTab.add(expressionsTable, gbc_expressionsTable);
		
		JPanel filtersTab = new JPanel();
		tabbedPane.addTab("Tree Output", null, filtersTab, null);
	}
	
	
	public CompareTextElementDataParametersPanel getCompareNamesPanel() {
		return compareNamesPanel;
	}
}
