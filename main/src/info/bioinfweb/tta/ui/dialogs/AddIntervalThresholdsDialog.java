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
package info.bioinfweb.tta.ui.dialogs;


import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import info.bioinfweb.wikihelp.client.OkCancelApplyWikiHelpDialog;
import info.bioinfweb.wikihelp.client.WikiHelp;



public class AddIntervalThresholdsDialog extends OkCancelApplyWikiHelpDialog {
	private final JPanel contentPanel = new JPanel();
	private ReplaceRetainThresholdsPanel replaceRetainPanel;

	
	/**
	 * Create the dialog.
	 */
	public AddIntervalThresholdsDialog(Frame owner, WikiHelp wikiHelp) {
		super(owner, true, wikiHelp);

		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblStepLength = new JLabel("Step Length:");
			GridBagConstraints gbc_lblStepLength = new GridBagConstraints();
			gbc_lblStepLength.anchor = GridBagConstraints.WEST;
			gbc_lblStepLength.insets = new Insets(0, 0, 5, 5);
			gbc_lblStepLength.gridx = 0;
			gbc_lblStepLength.gridy = 0;
			contentPanel.add(lblStepLength, gbc_lblStepLength);
		}
		{
			JSpinner stepSpinner = new JSpinner();
			stepSpinner.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
			GridBagConstraints gbc_stepSpinner = new GridBagConstraints();
			gbc_stepSpinner.fill = GridBagConstraints.HORIZONTAL;
			gbc_stepSpinner.insets = new Insets(0, 0, 5, 0);
			gbc_stepSpinner.gridx = 1;
			gbc_stepSpinner.gridy = 0;
			contentPanel.add(stepSpinner, gbc_stepSpinner);
		}
		{
			JLabel lblMinimum = new JLabel("Minimum:");
			GridBagConstraints gbc_lblMinimum = new GridBagConstraints();
			gbc_lblMinimum.insets = new Insets(0, 0, 5, 5);
			gbc_lblMinimum.anchor = GridBagConstraints.WEST;
			gbc_lblMinimum.gridx = 0;
			gbc_lblMinimum.gridy = 1;
			contentPanel.add(lblMinimum, gbc_lblMinimum);
		}
		{
			JSpinner minimumSpinner = new JSpinner();
			minimumSpinner.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
			GridBagConstraints gbc_minimumSpinner = new GridBagConstraints();
			gbc_minimumSpinner.fill = GridBagConstraints.HORIZONTAL;
			gbc_minimumSpinner.insets = new Insets(0, 0, 5, 0);
			gbc_minimumSpinner.gridx = 1;
			gbc_minimumSpinner.gridy = 1;
			contentPanel.add(minimumSpinner, gbc_minimumSpinner);
		}
		{
			JLabel lblMaximum = new JLabel("Maximum:");
			GridBagConstraints gbc_lblMaximum = new GridBagConstraints();
			gbc_lblMaximum.insets = new Insets(0, 0, 5, 5);
			gbc_lblMaximum.anchor = GridBagConstraints.WEST;
			gbc_lblMaximum.gridx = 0;
			gbc_lblMaximum.gridy = 2;
			contentPanel.add(lblMaximum, gbc_lblMaximum);
		}
		{
			JSpinner maximumSpinner = new JSpinner();
			maximumSpinner.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
			GridBagConstraints gbc_maximumSpinner = new GridBagConstraints();
			gbc_maximumSpinner.insets = new Insets(0, 0, 5, 0);
			gbc_maximumSpinner.fill = GridBagConstraints.HORIZONTAL;
			gbc_maximumSpinner.gridx = 1;
			gbc_maximumSpinner.gridy = 2;
			contentPanel.add(maximumSpinner, gbc_maximumSpinner);
		}
		{
			replaceRetainPanel = new ReplaceRetainThresholdsPanel();
			GridBagConstraints gbc_replaceRetainPanel = new GridBagConstraints();
			gbc_replaceRetainPanel.gridwidth = 2;
			gbc_replaceRetainPanel.insets = new Insets(5, 0, 5, 0);
			gbc_replaceRetainPanel.fill = GridBagConstraints.BOTH;
			gbc_replaceRetainPanel.gridx = 0;
			gbc_replaceRetainPanel.gridy = 3;
			contentPanel.add(replaceRetainPanel, gbc_replaceRetainPanel);
		}
		{
			getApplyButton().setVisible(false);
			getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
		}
	}


	@Override
	protected boolean apply() {
		// TODO Auto-generated method stub
		return false;
	}
}
