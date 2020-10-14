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


import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;



public class ReplaceRetainThresholdsPanel extends JPanel {
	private JRadioButton replaceRadioButton;
	private JRadioButton retainRadioButton;
	
	
	/**
	 * Create the panel.
	 */
	public ReplaceRetainThresholdsPanel() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		ButtonGroup buttonGroup = new ButtonGroup();

		replaceRadioButton = new JRadioButton("Replace current thresholds");
		buttonGroup.add(replaceRadioButton);
		add(replaceRadioButton);
		
		retainRadioButton = new JRadioButton("Add to current thresholds and retain them");
		buttonGroup.add(retainRadioButton);
		add(retainRadioButton);
	}
	
	
	public boolean isReplaceSelected() {
		return replaceRadioButton.isSelected();
	}
	
	
	public void setReplaceSelected(boolean replace) {
		if (replace) {
			replaceRadioButton.setSelected(true);
		}
		else {
			retainRadioButton.setSelected(true);
		}
	}
}
