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
package info.bioinfweb.tta;


import java.awt.EventQueue;

import javax.swing.UIManager;

import info.bioinfweb.commons.ProgramMainClass;
import info.bioinfweb.commons.appversion.ApplicationType;
import info.bioinfweb.commons.appversion.ApplicationVersion;
import info.bioinfweb.commons.log.ConsoleApplicationLogger;
import info.bioinfweb.tta.analysis.AnalysisManager;
import info.bioinfweb.tta.ui.MainFrame;



public class Main extends ProgramMainClass {
	public static final String APPLICATION_NAME = "Topological Tree Analyzer";
	public static final String APPLICATION_URL = "http://bioinfweb.info/TTA/";
	
	public static final String VERSION_COMMAND = "-version";
	
	private static Main firstInstance = null;
	
	
	private Main() {
		super(new ApplicationVersion(0, 8, 0, 348, ApplicationType.ALPHA));
	}
	
	
	public static Main getInstance() {
		if (firstInstance == null) {
			firstInstance = new Main();	
		}
		return firstInstance;
	}
	
	
	private void startGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame.getInstance().setVisible(true);
			}
		});
	}
	
	
	private void printApplicationInfo() {
		System.out.println(APPLICATION_NAME);
		System.out.println("Version " + getVersion());
		System.out.println("<" + APPLICATION_URL + ">");
	}
	
	
	private void run(String[] args) {
		if (args.length >= 1) {
			if (args[0].toLowerCase().equals(VERSION_COMMAND)) {
				printApplicationInfo();
			}
			else {
				new AnalysisManager().runAnalysis(args[0], new ConsoleApplicationLogger());
			}
		}
		else {
			System.out.println("Please specify a parameters file.");
			//startGUI();
		}
	}


	public static void main(String[] args) {
		Main.getInstance().run(args);
	}
}
