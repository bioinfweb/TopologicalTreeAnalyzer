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
package info.bioinfweb.tta;


import java.io.File;

import info.bioinfweb.commons.ProgramMainClass;
import info.bioinfweb.commons.appversion.ApplicationType;
import info.bioinfweb.commons.appversion.ApplicationVersion;
import info.bioinfweb.tta.analysis.TopologicalAnalyzer;
import info.bioinfweb.tta.analysis.UserExpressionsManager;
import info.bioinfweb.tta.data.AnalysesData;
import info.bioinfweb.tta.data.parameters.AnalysisParameters;
import info.bioinfweb.tta.io.TableWriter;
import info.bioinfweb.tta.io.TreeWriter;
import info.bioinfweb.tta.io.parameters.AnalysisParameterIO;
import info.bioinfweb.tta.ui.CmdProgressMonitor;



public class Main extends ProgramMainClass {
	public static final String APPLICATION_NAME = "Topological Tree Analyzer";
	public static final String APPLICATION_URL = "http://bioinfweb.info/TTA/";
	
	public static final String VERSION_COMMAND = "-version";
	
	public static final String TREE_DATA_FILE_NAME = "TreeData.txt";
	public static final String PAIR_DATA_FILE_NAME = "PairData.txt";
	
	
	private static Main firstInstance = null;
	
	
	private Main() {
		super(new ApplicationVersion(0, 1, 0, 204, ApplicationType.ALPHA));
	}
	
	
	public static Main getInstance() {
		if (firstInstance == null) {
			firstInstance = new Main();
		}
		return firstInstance;
	}
	
	
	private void runAnalysis(String[] args) {
		try {
			File parametersFile = new File(args[0]).getAbsoluteFile();
			File parametersFileDirectory = new File(parametersFile.getParent()).getAbsoluteFile();
			
			printApplicationInfo();
			System.out.println();
			
			// Read parameters:
			System.out.print("Reading parameters from \"" + parametersFile.getAbsolutePath() + "\"... ");
			AnalysisParameters parameters = AnalysisParameterIO.getInstance().read(parametersFile);
			String[] inputFiles = parameters.getRelativizedTreeFilesNames(parametersFileDirectory);
			System.out.println("Done.");
			
			// Perform topological analysis:
			System.out.print("Performing topological analysis ");
			AnalysesData analysesData = new AnalysesData();
			TopologicalAnalyzer analyzer = new TopologicalAnalyzer(parameters.getTextComparisonParameters());
			CmdProgressMonitor progressMonitor = new CmdProgressMonitor();
			if (parameters.definedReferenceTree()) {
				System.out.println("for a single reference tree...");
				analyzer.compareWithReference(parameters.getReferenceTree().createTreeSelector(parametersFileDirectory), inputFiles, analysesData, progressMonitor);
			}
			else {
				System.out.println("for all possible pairs...");
				analyzer.compareAll(parameters.getRuntimeParameters(), inputFiles, analysesData, progressMonitor);
			}
			System.out.println();  // Line break after progress bar.
			System.out.println("Done.");
			
			// Calculate user data:
			if (!parameters.getUserExpressions().getExpressions().isEmpty()) {
				System.out.print("Calculating user expressions... ");
				UserExpressionsManager manager = new UserExpressionsManager();
				manager.setExpressions(parameters.getUserExpressions());
				manager.evaluateExpressions(analysesData);
				System.out.println("Done.");
			}
			else {
				System.out.println("No user expressions have been defined.");
			}
			
			// Determine output directory:
			File outputDirectory;
			if (parameters.getOutputDirectory().isAbsolute()) {
				outputDirectory = parameters.getOutputDirectory();
			}
			else {
				outputDirectory = new File(parametersFileDirectory.getAbsolutePath() + File.separator + parameters.getOutputDirectory().toString());
			}
			System.out.println("All outputs will be written to \"" + outputDirectory.getAbsolutePath() + "\".");
			outputDirectory.mkdirs();
			
			// Write user data tables:
			if (!parameters.getTreeExportColumns().getColumns().isEmpty() || !parameters.getPairExportColumns().getColumns().isEmpty()) {
				System.out.print("Writing user data tables... ");
				TableWriter tableWriter = new TableWriter();
				tableWriter.writeTreeData(new File(outputDirectory.getAbsolutePath() + File.separator + TREE_DATA_FILE_NAME),
						parameters.getTreeExportColumns(), analysesData.getTreeMap());
				tableWriter.writePairData(new File(outputDirectory.getAbsolutePath() + File.separator + PAIR_DATA_FILE_NAME), 
						parameters.getPairExportColumns(), analysesData.getComparisonMap());
				System.out.println("Done.");
			}
			else {
				System.out.println("No user values to export have been defined.");
			}
			
			// Write filtered tree output:
			if (!parameters.getFilters().isEmpty()) {
				System.out.print("Writing filtered tree files... ");
				new TreeWriter().writeFilterOutputs(parameters.getFilters(), outputDirectory, inputFiles, analysesData.getTreeMap());
				System.out.println("Done.");
			}
			else {
				System.out.println("No tree filters have been defined.");
			}
			
			System.out.println("Finished. (" + analysesData.getTreeCount() + " trees have been analyzed in " + analysesData.getComparisonMap().size() + 
					" pairs.)");
			//throw new IllegalArgumentException("The specified output location \"" + outputDirectory.getAbsolutePath() + "\" is not a directory.");
		} 
		catch (Exception e) {
			System.out.println("The error \"" + e.getLocalizedMessage() + "\" occurred.");
			e.printStackTrace(System.err);
		}
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
				runAnalysis(args);
			}
		}
		else {
			System.out.println("A parameter file needs to be specified.");
		}
	}


	public static void main(String[] args) {
		Main.getInstance().run(args);
	}
}
