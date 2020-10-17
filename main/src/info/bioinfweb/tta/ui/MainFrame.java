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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.collections4.set.ListOrderedSet;

import info.bioinfweb.commons.io.ContentExtensionFileFilter;
import info.bioinfweb.commons.io.ContentExtensionFileFilter.TestStrategy;
import info.bioinfweb.commons.io.ExtensionFileFilter;
import info.bioinfweb.commons.swing.ListBackedListModel;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.jphyloio.formatinfo.JPhyloIOFormatInfo;
import info.bioinfweb.treegraph.gui.dialogs.CompareTextElementDataParametersPanel;
import info.bioinfweb.tta.data.UserExpression;
import info.bioinfweb.tta.data.parameters.AnalysisParameters;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;



public class MainFrame extends JFrame {
	private static MainFrame firstInstance = null;
	
	
	private AnalysisParameters model;
	private JPhyloIOReaderWriterFactory factory;
	private JPanel contentPane;
	private JTextField outputDirectoryTextField;
	private CompareTextElementDataParametersPanel compareNamesPanel;
	private JTextField treeFileTextField;
	private JTextField referenceTreeTextField;
	private JTable expressionsTable;
	private JTextField newExpressionTextField;
	private JFileChooser directoryChooser;
	private JFileChooser treeFileChooser;
	private JTable filterTable;
	private JTextField newFilterTextField;
	private ListBackedListModel<String> treeFileListModel;
	private JList<String> treeFileList; 
	private JButton addTreeFileButton;
	private JButton replaceTreeFileButton;
	private JButton removeTreeFile;
	private JButton moveUpTreeFileButton;
	private JButton moveDownTreeFileButton;
	private JButton relativeAbsoluteTreeFileButton;
	private JPanel generalTab;
	private JPanel runtimeTab;
	private JPanel treeListTab;
	private JPanel expressionsTab;
	private JPanel filtersTab;
	
	
	public static MainFrame getInstance() {
		if (firstInstance == null) {
			firstInstance = new MainFrame();
		}
		return firstInstance;
	}
	
	
	/**
	 * Create the frame.
	 */
	private MainFrame() {
		super();
		model = new AnalysisParameters();
		factory = new JPhyloIOReaderWriterFactory();
		initComponents();
	}
	
	
	public AnalysisParameters getModel() {
		return model;
	}


	public void setModel(AnalysisParameters model) {
		this.model = model;
		//TODO Update all component contents
		refreshTreeListButtonStatus();
	}

	
	private void initComponents() {
		setTitle("Topological Tree Analyzer");
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
		tabbedPane.addTab("General", null, getGeneralTab(), null);
		tabbedPane.addTab("Runtime", null, getRuntimeTab(), null);
		tabbedPane.addTab("Tree Files", null, getTreeListTab(), null);
		tabbedPane.addTab("User Expressions", null, getExpressionsTab(), null);
		tabbedPane.addTab("Tree Output", null, getFiltersTab(), null);
		contentPane.add(tabbedPane, BorderLayout.NORTH);
	}
	
	
	private JFileChooser getDirectoryChooser() {
		if (directoryChooser == null) {
			directoryChooser = new JFileChooser();
			directoryChooser.setAcceptAllFileFilterUsed(false);
			directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		return directoryChooser;
	}
	
	
	private UserExpressionsTableModel getExpressionsModel() {
		return (UserExpressionsTableModel)expressionsTable.getModel();
	}
	
	
	public JPanel getGeneralTab() {
		if (generalTab == null) {
			generalTab = new JPanel();
			GridBagLayout gbl_generalTab = new GridBagLayout();
			gbl_generalTab.columnWeights = new double[]{0.0, 1.0, 0.0};
			gbl_generalTab.rowWeights = new double[]{0.0, 1.0};
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
					File currentSelection = new File(outputDirectoryTextField.getText());
					if (currentSelection.exists()) {
						getDirectoryChooser().setCurrentDirectory(currentSelection);
					}
					if (getDirectoryChooser().showDialog(getInstance(), "Select") == JFileChooser.APPROVE_OPTION) {
						outputDirectoryTextField.setText(getDirectoryChooser().getSelectedFile().toString());  //TODO Absolute/relative?
					}
				}
			});
			
			JLabel lblComparisonOptionsFor = new JLabel("Comparison options for node names:");
			GridBagConstraints gbc_lblComparisonOptionsFor = new GridBagConstraints();
			gbc_lblComparisonOptionsFor.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblComparisonOptionsFor.insets = new Insets(0, 0, 5, 5);
			gbc_lblComparisonOptionsFor.gridx = 0;
			gbc_lblComparisonOptionsFor.gridy = 1;
			generalTab.add(lblComparisonOptionsFor, gbc_lblComparisonOptionsFor);
			
			compareNamesPanel = new CompareTextElementDataParametersPanel();
			GridBagConstraints gbc_compareNamesPanel = new GridBagConstraints();
			gbc_compareNamesPanel.insets = new Insets(0, 0, 5, 0);
			gbc_compareNamesPanel.fill = GridBagConstraints.BOTH;
			gbc_compareNamesPanel.gridwidth = 2;
			gbc_compareNamesPanel.gridx = 1;
			gbc_compareNamesPanel.gridy = 1;
			generalTab.add(compareNamesPanel, gbc_compareNamesPanel);
		}
		return generalTab;
	}
	
	
	public JPanel getRuntimeTab() {
		if (runtimeTab == null) {
			runtimeTab = new JPanel();
			GridBagLayout gbl_runtimeTab = new GridBagLayout();
			gbl_runtimeTab.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0};
			gbl_runtimeTab.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
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
			gbc_memoryUnitComboBox.insets = new Insets(0, 0, 5, 0);
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
			gbc_threadsDefinedRadioButton.insets = new Insets(0, 0, 5, 5);
			gbc_threadsDefinedRadioButton.gridx = 1;
			gbc_threadsDefinedRadioButton.gridy = 3;
			runtimeTab.add(threadsDefinedRadioButton, gbc_threadsDefinedRadioButton);
			
			JSpinner threadsSpinner = new JSpinner();
			GridBagConstraints gbc_threadsSpinner = new GridBagConstraints();
			gbc_threadsSpinner.insets = new Insets(0, 0, 5, 0);
			gbc_threadsSpinner.gridwidth = 2;
			gbc_threadsSpinner.fill = GridBagConstraints.HORIZONTAL;
			gbc_threadsSpinner.gridx = 2;
			gbc_threadsSpinner.gridy = 3;
			runtimeTab.add(threadsSpinner, gbc_threadsSpinner);
			
			JLabel runtimeSpacingLabel = new JLabel(" ");
			GridBagConstraints gbc_runtimeSpacingLabel = new GridBagConstraints();
			gbc_runtimeSpacingLabel.insets = new Insets(0, 0, 0, 5);
			gbc_runtimeSpacingLabel.gridx = 0;
			gbc_runtimeSpacingLabel.gridy = 4;
			runtimeTab.add(runtimeSpacingLabel, gbc_runtimeSpacingLabel);
		}
		return runtimeTab;
	}
	
	
	private void refreshAddTreeButtonStatus() {
		addTreeFileButton.setEnabled(!treeFileTextField.getText().trim().isEmpty());
	}
	
	
	private void refreshTreeListButtonStatus() {
		int index = treeFileList.getSelectedIndex();
		boolean selected = index > -1;
		replaceTreeFileButton.setEnabled(selected);
		removeTreeFile.setEnabled(selected);
		moveUpTreeFileButton.setEnabled(index > 0);
		moveDownTreeFileButton.setEnabled(selected && (index < treeFileListModel.getSize() - 1));
		relativeAbsoluteTreeFileButton.setEnabled(selected);
	}


	private JFileChooser getTreeFileChooser() {
		if (treeFileChooser == null) {
			treeFileChooser = new JFileChooser();
			treeFileChooser.setAcceptAllFileFilterUsed(true);
			
			ListOrderedSet<String> validExtensions = new ListOrderedSet<String>();
			for (String formatID : factory.getFormatIDsSet()) {
				JPhyloIOFormatInfo info = factory.getFormatInfo(formatID);
				if (info.isElementModeled(EventContentType.TREE, true)) {
					ContentExtensionFileFilter filter = info.createFileFilter(TestStrategy.BOTH);
					validExtensions.addAll(filter.getExtensions());
					treeFileChooser.addChoosableFileFilter(filter);
				}
			}
			ExtensionFileFilter allFormatsFilter = new ExtensionFileFilter("All supported formats", false, validExtensions.asList());
			treeFileChooser.addChoosableFileFilter(allFormatsFilter);
			treeFileChooser.setFileFilter(allFormatsFilter);
		}
		return treeFileChooser;
	}
	
	
	public JPanel getTreeListTab() {
		if (treeListTab == null) {
			treeListTab = new JPanel();
			GridBagLayout gbl_treeListTab = new GridBagLayout();
			gbl_treeListTab.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
			gbl_treeListTab.columnWeights = new double[]{1.0, 0.0};
			treeListTab.setLayout(gbl_treeListTab);
			
			treeFileTextField = new JTextField();
			treeFileTextField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					refreshAddTreeButtonStatus();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					refreshAddTreeButtonStatus();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {}
			});
			GridBagConstraints gbc_treeFileTextField = new GridBagConstraints();
			gbc_treeFileTextField.insets = new Insets(0, 0, 5, 5);
			gbc_treeFileTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_treeFileTextField.gridx = 0;
			gbc_treeFileTextField.gridy = 0;
			treeListTab.add(treeFileTextField, gbc_treeFileTextField);
			treeFileTextField.setColumns(10);
			
			JButton selectTreeFileButton = new JButton("...");
			selectTreeFileButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					File currentSelection = new File(treeFileTextField.getText());
					if (currentSelection.exists()) {
						getTreeFileChooser().setSelectedFile(currentSelection);
					}
					if (getTreeFileChooser().showDialog(getInstance(), "Select") == JFileChooser.APPROVE_OPTION) {
						treeFileTextField.setText(getTreeFileChooser().getSelectedFile().toString());  //TODO Absolute/relative?
					}	
				}
			});
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
			
			addTreeFileButton = new JButton("Add");
			addTreeFileButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (treeFileList.getSelectedIndex() != -1) {
						int index = treeFileList.getSelectedIndex() + 1;
						treeFileListModel.add(index, treeFileTextField.getText().trim());
						treeFileList.setSelectedIndex(index);
					}
					else {
						treeFileListModel.add(treeFileTextField.getText().trim());
						treeFileList.setSelectedIndex(treeFileListModel.getSize() - 1);
					}
				}
			});
			GridBagConstraints gbc_addTreeFileButton = new GridBagConstraints();
			gbc_addTreeFileButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_addTreeFileButton.insets = new Insets(0, 0, 5, 0);
			gbc_addTreeFileButton.gridx = 1;
			gbc_addTreeFileButton.gridy = 1;
			treeListTab.add(addTreeFileButton, gbc_addTreeFileButton);
			
			replaceTreeFileButton = new JButton("Replace");
			replaceTreeFileButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (treeFileList.getSelectedIndex() != -1) {
						treeFileListModel.set(treeFileList.getSelectedIndex(), treeFileTextField.getText());
					}
				}
			});
			GridBagConstraints gbc_replaceTreeFileButton = new GridBagConstraints();
			gbc_replaceTreeFileButton.insets = new Insets(0, 0, 5, 0);
			gbc_replaceTreeFileButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_replaceTreeFileButton.gridx = 1;
			gbc_replaceTreeFileButton.gridy = 2;
			treeListTab.add(replaceTreeFileButton, gbc_replaceTreeFileButton);
			
			removeTreeFile = new JButton("Remove");
			removeTreeFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (treeFileList.getSelectedIndex() != -1) {
						treeFileListModel.remove(treeFileList.getSelectedIndex());
					}
				}
			});
			GridBagConstraints gbc_removeTreeFile = new GridBagConstraints();
			gbc_removeTreeFile.fill = GridBagConstraints.HORIZONTAL;
			gbc_removeTreeFile.insets = new Insets(0, 0, 5, 0);
			gbc_removeTreeFile.gridx = 1;
			gbc_removeTreeFile.gridy = 3;
			treeListTab.add(removeTreeFile, gbc_removeTreeFile);
			
			moveUpTreeFileButton = new JButton("Move Up");
			moveUpTreeFileButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (treeFileList.getSelectedIndex() > 0) {
						int newPos = treeFileList.getSelectedIndex() - 1;
						treeFileListModel.add(newPos, treeFileListModel.remove(treeFileList.getSelectedIndex()));
						treeFileList.setSelectedIndex(newPos);
					}  // No error message, since button should not be enabled then.
				}
			});
			GridBagConstraints gbc_moveUpTreeFileButton = new GridBagConstraints();
			gbc_moveUpTreeFileButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_moveUpTreeFileButton.insets = new Insets(0, 0, 5, 0);
			gbc_moveUpTreeFileButton.gridx = 1;
			gbc_moveUpTreeFileButton.gridy = 4;
			treeListTab.add(moveUpTreeFileButton, gbc_moveUpTreeFileButton);
			
			moveDownTreeFileButton = new JButton("Move Down");
			moveDownTreeFileButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if ((treeFileList.getSelectedIndex() > -1) && (treeFileList.getSelectedIndex() < treeFileListModel.getSize() - 1)) {
						int newPos = treeFileList.getSelectedIndex() + 1;
						treeFileListModel.add(newPos, treeFileListModel.remove(treeFileList.getSelectedIndex()));
						treeFileList.setSelectedIndex(newPos);
					}  // No error message, since button should not be enabled then.
				}
			});
			GridBagConstraints gbc_moveDownTreeFileButton = new GridBagConstraints();
			gbc_moveDownTreeFileButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_moveDownTreeFileButton.insets = new Insets(0, 0, 5, 0);
			gbc_moveDownTreeFileButton.gridx = 1;
			gbc_moveDownTreeFileButton.gridy = 5;
			treeListTab.add(moveDownTreeFileButton, gbc_moveDownTreeFileButton);
			
			relativeAbsoluteTreeFileButton = new JButton("Make Relative");
			GridBagConstraints gbc_relativeAbsoluteTreeFileButton = new GridBagConstraints();
			gbc_relativeAbsoluteTreeFileButton.insets = new Insets(0, 0, 5, 0);
			gbc_relativeAbsoluteTreeFileButton.gridx = 1;
			gbc_relativeAbsoluteTreeFileButton.gridy = 6;
			treeListTab.add(relativeAbsoluteTreeFileButton, gbc_relativeAbsoluteTreeFileButton);
			treeListTab.setLayout(gbl_treeListTab);
			GridBagLayout gbl_referenceTreePanel = new GridBagLayout();
			gbl_referenceTreePanel.columnWeights = new double[]{0.0, 1.0, 0.0};
			
			treeFileListModel = new ListBackedListModel<String>(model.getTreeFilesNames());  //TODO Update this when a new model is set
			treeFileList = new JList<String>(treeFileListModel);
			treeFileList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					//TODO Call this method also on startup and after model change to set initial button status. => Should probably a method of MainFrame for that.
					refreshTreeListButtonStatus();
				}
			});
			treeFilesScrollPane.setViewportView(treeFileList);
			
			refreshAddTreeButtonStatus();
			refreshTreeListButtonStatus();
			
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
		}
		return treeListTab;
	}
	
	
	public JPanel getExpressionsTab() {
		if (expressionsTab == null) {
			expressionsTab = new JPanel();
			GridBagLayout gbl_expressionsTab = new GridBagLayout();
			gbl_expressionsTab.rowWeights = new double[]{0.0, 1.0, 0.0};
			gbl_expressionsTab.columnWeights = new double[]{0.0, 1.0, 0.0};
			expressionsTab.setLayout(gbl_expressionsTab);
			
			JLabel lblExpressions = new JLabel("Expressions:");
			GridBagConstraints gbc_lblExpressions = new GridBagConstraints();
			gbc_lblExpressions.gridwidth = 3;
			gbc_lblExpressions.anchor = GridBagConstraints.WEST;
			gbc_lblExpressions.insets = new Insets(0, 0, 5, 0);
			gbc_lblExpressions.gridx = 0;
			gbc_lblExpressions.gridy = 0;
			expressionsTab.add(lblExpressions, gbc_lblExpressions);
			
			expressionsTable = new JTable();
			expressionsTable.setModel(new UserExpressionsTableModel(model.getUserExpressions().getExpressions()));  //TODO Update this when a new model is set 
			GridBagConstraints gbc_expressionsTable = new GridBagConstraints();
			gbc_expressionsTable.insets = new Insets(0, 0, 5, 0);
			gbc_expressionsTable.gridwidth = 3;
			gbc_expressionsTable.fill = GridBagConstraints.BOTH;
			gbc_expressionsTable.gridx = 0;
			gbc_expressionsTable.gridy = 1;
			expressionsTab.add(expressionsTable, gbc_expressionsTable);
			
			JLabel lblNewExpression = new JLabel("New expression:");
			GridBagConstraints gbc_lblNewExpression = new GridBagConstraints();
			gbc_lblNewExpression.insets = new Insets(0, 0, 0, 5);
			gbc_lblNewExpression.anchor = GridBagConstraints.WEST;
			gbc_lblNewExpression.gridx = 0;
			gbc_lblNewExpression.gridy = 2;
			expressionsTab.add(lblNewExpression, gbc_lblNewExpression);
			
			newExpressionTextField = new JTextField();
			GridBagConstraints gbc_newExpressionTextField = new GridBagConstraints();
			gbc_newExpressionTextField.weightx = 1.0;
			gbc_newExpressionTextField.insets = new Insets(0, 0, 0, 5);
			gbc_newExpressionTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_newExpressionTextField.gridx = 1;
			gbc_newExpressionTextField.gridy = 2;
			expressionsTab.add(newExpressionTextField, gbc_newExpressionTextField);
			
			JButton addExpressionButton = new JButton("Add");
			addExpressionButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String name = newExpressionTextField.getText();
					if (!getExpressionsModel().getExpressions().containsKey(name)) {
						getExpressionsModel().getExpressions().put(name, new UserExpression(false, ""));
						getExpressionsModel().refreshFromMap();
					}
					else {
						JOptionPane.showMessageDialog(getInstance(), "An expression with the name \"" + name + 
								"\" is already present.\n Please chose another name or rename the other expression in the table.", "Duplicate name", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			GridBagConstraints gbc_btnAdd = new GridBagConstraints();
			gbc_btnAdd.insets = new Insets(0, 0, 0, 5);
			gbc_btnAdd.gridx = 2;
			gbc_btnAdd.gridy = 2;
			expressionsTab.add(addExpressionButton, gbc_btnAdd);
		}
		return expressionsTab;
	}
	
	
	public JPanel getFiltersTab() {
		if (filtersTab == null) {
			filtersTab = new JPanel();
			GridBagLayout gbl_filtersTab = new GridBagLayout();
			gbl_filtersTab.columnWidths = new int[]{0, 0, 0, 0, 0};
			gbl_filtersTab.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			gbl_filtersTab.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_filtersTab.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
			filtersTab.setLayout(gbl_filtersTab);
			
			JLabel lblFilters = new JLabel("Filters:");
			GridBagConstraints gbc_lblFilters = new GridBagConstraints();
			gbc_lblFilters.anchor = GridBagConstraints.WEST;
			gbc_lblFilters.insets = new Insets(0, 0, 5, 5);
			gbc_lblFilters.gridx = 0;
			gbc_lblFilters.gridy = 0;
			filtersTab.add(lblFilters, gbc_lblFilters);
			
			filterTable = new JTable();
			GridBagConstraints gbc_filterTable = new GridBagConstraints();
			gbc_filterTable.gridwidth = 4;
			gbc_filterTable.insets = new Insets(0, 0, 5, 0);
			gbc_filterTable.fill = GridBagConstraints.BOTH;
			gbc_filterTable.gridx = 0;
			gbc_filterTable.gridy = 1;
			filtersTab.add(filterTable, gbc_filterTable);
			
			JLabel lblThresholds = new JLabel("Thresholds:");
			GridBagConstraints gbc_lblThresholds = new GridBagConstraints();
			gbc_lblThresholds.insets = new Insets(0, 0, 5, 5);
			gbc_lblThresholds.anchor = GridBagConstraints.WEST;
			gbc_lblThresholds.gridx = 0;
			gbc_lblThresholds.gridy = 2;
			filtersTab.add(lblThresholds, gbc_lblThresholds);
			
			JSpinner thresholdSpinner = new JSpinner();
			thresholdSpinner.setModel(new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(0.1)));
			GridBagConstraints gbc_spinner = new GridBagConstraints();
			gbc_spinner.gridwidth = 3;
			gbc_spinner.fill = GridBagConstraints.HORIZONTAL;
			gbc_spinner.insets = new Insets(0, 0, 5, 5);
			gbc_spinner.gridx = 0;
			gbc_spinner.gridy = 3;
			filtersTab.add(thresholdSpinner, gbc_spinner);
			
			JList<Double> thresholdsList = new JList<Double>();
			GridBagConstraints gbc_thresholdsList = new GridBagConstraints();
			gbc_thresholdsList.gridwidth = 3;
			gbc_thresholdsList.gridheight = 5;
			gbc_thresholdsList.insets = new Insets(0, 0, 5, 5);
			gbc_thresholdsList.fill = GridBagConstraints.BOTH;
			gbc_thresholdsList.gridx = 0;
			gbc_thresholdsList.gridy = 4;
			filtersTab.add(thresholdsList, gbc_thresholdsList);
			
			JButton ThresholdIntervalButton = new JButton("Interval...");
			GridBagConstraints gbc_ThresholdIntervalButton = new GridBagConstraints();
			gbc_ThresholdIntervalButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_ThresholdIntervalButton.insets = new Insets(0, 0, 5, 0);
			gbc_ThresholdIntervalButton.gridx = 3;
			gbc_ThresholdIntervalButton.gridy = 4;
			filtersTab.add(ThresholdIntervalButton, gbc_ThresholdIntervalButton);
			
			JButton AddThresholdButton = new JButton("Add Threshold");
			GridBagConstraints gbc_AddThresholdButton = new GridBagConstraints();
			gbc_AddThresholdButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_AddThresholdButton.insets = new Insets(0, 0, 5, 0);
			gbc_AddThresholdButton.gridx = 3;
			gbc_AddThresholdButton.gridy = 3;
			filtersTab.add(AddThresholdButton, gbc_AddThresholdButton);
			
			JButton copyThresholdsButton = new JButton("Copy from...");
			GridBagConstraints gbc_copyThresholdsButton = new GridBagConstraints();
			gbc_copyThresholdsButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_copyThresholdsButton.insets = new Insets(0, 0, 5, 0);
			gbc_copyThresholdsButton.gridx = 3;
			gbc_copyThresholdsButton.gridy = 5;
			filtersTab.add(copyThresholdsButton, gbc_copyThresholdsButton);
			
			JButton RemoveThresholdButton = new JButton("Remove");
			GridBagConstraints gbc_RemoveThresholdButton = new GridBagConstraints();
			gbc_RemoveThresholdButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_RemoveThresholdButton.insets = new Insets(0, 0, 5, 0);
			gbc_RemoveThresholdButton.gridx = 3;
			gbc_RemoveThresholdButton.gridy = 6;
			filtersTab.add(RemoveThresholdButton, gbc_RemoveThresholdButton);
			
			JButton ClearThresholdsButton = new JButton("Clear");
			GridBagConstraints gbc_ClearThresholdsButton = new GridBagConstraints();
			gbc_ClearThresholdsButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_ClearThresholdsButton.insets = new Insets(0, 0, 5, 0);
			gbc_ClearThresholdsButton.gridx = 3;
			gbc_ClearThresholdsButton.gridy = 7;
			filtersTab.add(ClearThresholdsButton, gbc_ClearThresholdsButton);
			
			JLabel lblNewFilter = new JLabel("New Filter:");
			GridBagConstraints gbc_lblNewFilter = new GridBagConstraints();
			gbc_lblNewFilter.anchor = GridBagConstraints.WEST;
			gbc_lblNewFilter.insets = new Insets(0, 0, 0, 10);
			gbc_lblNewFilter.gridx = 0;
			gbc_lblNewFilter.gridy = 9;
			filtersTab.add(lblNewFilter, gbc_lblNewFilter);
			
			newFilterTextField = new JTextField();
			GridBagConstraints gbc_newFilterTextField = new GridBagConstraints();
			gbc_newFilterTextField.insets = new Insets(0, 0, 0, 5);
			gbc_newFilterTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_newFilterTextField.gridx = 1;
			gbc_newFilterTextField.gridy = 9;
			filtersTab.add(newFilterTextField, gbc_newFilterTextField);
			newFilterTextField.setColumns(10);
			
			JComboBox filterTypeComboBox = new JComboBox();
			GridBagConstraints gbc_filterTypeComboBox = new GridBagConstraints();
			gbc_filterTypeComboBox.insets = new Insets(0, 0, 0, 5);
			gbc_filterTypeComboBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_filterTypeComboBox.gridx = 2;
			gbc_filterTypeComboBox.gridy = 9;
			filtersTab.add(filterTypeComboBox, gbc_filterTypeComboBox);
			
			JButton btnAddFilter = new JButton("Add Filter");
			GridBagConstraints gbc_btnAddFilter = new GridBagConstraints();
			gbc_btnAddFilter.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnAddFilter.gridx = 3;
			gbc_btnAddFilter.gridy = 9;
			filtersTab.add(btnAddFilter, gbc_btnAddFilter);
		}
		return filtersTab;
	}
}
