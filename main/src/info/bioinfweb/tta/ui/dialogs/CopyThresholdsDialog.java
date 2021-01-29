/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020-2021  Ben C. Stöver
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


import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import info.bioinfweb.wikihelp.client.OkCancelApplyWikiHelpDialog;
import info.bioinfweb.wikihelp.client.WikiHelp;



public class CopyThresholdsDialog extends OkCancelApplyWikiHelpDialog {
	private final JPanel contentPanel = new JPanel();


	/**
	 * Create the dialog.
	 */
	public CopyThresholdsDialog(Frame owner, WikiHelp wikiHelp) {
		super(owner, true, wikiHelp);

		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWeights = new double[]{1.0};
		gbl_contentPanel.rowWeights = new double[]{0.0, 1.0};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JComboBox comboBox = new JComboBox();
			GridBagConstraints gbc_comboBox = new GridBagConstraints();
			gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBox.anchor = GridBagConstraints.NORTH;
			gbc_comboBox.insets = new Insets(0, 0, 5, 0);
			gbc_comboBox.gridx = 0;
			gbc_comboBox.gridy = 0;
			contentPanel.add(comboBox, gbc_comboBox);
		}
		{
			ReplaceRetainThresholdsPanel panel = new ReplaceRetainThresholdsPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
		}
		{
			getApplyButton().setVisible(false);
			getContentPane().add(getButtonsPanel());
		}
	}


	@Override
	protected boolean apply() {
		// TODO Auto-generated method stub
		return false;
	}
}
