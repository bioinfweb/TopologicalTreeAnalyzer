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
package info.bioinfweb.tta.data;


import java.io.File;

import info.bioinfweb.commons.log.ApplicationLogger;
import info.bioinfweb.tta.Main;
import info.bioinfweb.tta.analysis.TopologicalAnalyzer;
import info.bioinfweb.tta.analysis.UserExpressionsManager;
import info.bioinfweb.tta.data.parameters.AnalysisParameters;
import info.bioinfweb.tta.exception.AnalysisException;
import info.bioinfweb.tta.io.TopologicalDataFileNames;
import info.bioinfweb.tta.io.TreeWriter;
import info.bioinfweb.tta.io.UserValueTableWriter;
import info.bioinfweb.tta.io.parameters.AnalysisParameterIO;
import info.bioinfweb.tta.ui.CmdProgressMonitor;



public class AnalysisManager {
	public static final String TREE_DATA_FILE_NAME = "TreeData.txt";
	public static final String PAIR_DATA_FILE_NAME = "PairData.txt";
	
	
	private void logApplicationInfo(ApplicationLogger logger) {
		logger.addMessage(Main.APPLICATION_NAME);
		logger.addMessage("Version " + Main.getInstance().getVersion());
		logger.addMessage("<" + Main.APPLICATION_URL + ">");
		logger.addMessage("");
	}
	
	
	private boolean checkTopologicalDataFiles(File outputDirectory) {
		TopologicalDataFileNames fileNames = new TopologicalDataFileNames(outputDirectory.getAbsolutePath() + File.separator);
		if (fileNames.getTreeListFile().exists() || fileNames.getTreeDataFile().exists() || fileNames.getPairDataFile().exists()) {
			if (fileNames.getTreeListFile().exists() && fileNames.getTreeDataFile().exists() && fileNames.getPairDataFile().exists()) {
				return true;
			}
			else {
				throw new AnalysisException("The output directory \"" + outputDirectory.getAbsolutePath() + "\" contains one or more topological data files but not all of them.");
			}
		}
		else {
			return false;
		}
	}
	
	
	public void runAnalysis(String parametersFileName, ApplicationLogger logger) {
		try {
			File parametersFile = new File(parametersFileName).getAbsoluteFile();
			File parametersFileDirectory = new File(parametersFile.getParent()).getAbsoluteFile();

			logApplicationInfo(logger);
			
			// Read parameters:
			logger.addMessage("Reading parameters from \"" + parametersFile.getAbsolutePath() + "\"... ");
			AnalysisParameters parameters = AnalysisParameterIO.getInstance().read(parametersFile);
			String[] inputFiles = parameters.getRelativizedTreeFilesNames(parametersFileDirectory);
			logger.addMessage("Done.");
			
			// Determine output directory:
			File outputDirectory;
			if (parameters.getOutputDirectory().isAbsolute()) {
				outputDirectory = parameters.getOutputDirectory();
			}
			else {
				outputDirectory = new File(parametersFileDirectory.getAbsolutePath() + File.separator + parameters.getOutputDirectory().toString());
			}
			
			if (checkTopologicalDataFiles(outputDirectory)) {
				//TODO Load data.
				//TODO Check input order.
				//TODO Determine data possibly still to be calculated.
				//TODO Possibly perform remaining topological analysis.
			}
			else {
				//TODO Perform full topological analysis.
			}
			
			// Perform topological analysis:
			logger.addMessage("Performing topological analysis ");
			AnalysesData analysesData = new AnalysesData();
			//TODO Check for files
			//TopologicalDataWritingManager dataManager = new TopologicalDataWritingManager(analysesData, outputFilePrefix, timeout);
			
			TopologicalAnalyzer analyzer = new TopologicalAnalyzer(parameters.getTextComparisonParameters());
			CmdProgressMonitor progressMonitor = new CmdProgressMonitor();
			if (parameters.definedReferenceTree()) {
				logger.addMessage("for a single reference tree...");
				analyzer.compareWithReference(parameters.getReferenceTree().createTreeSelector(parametersFileDirectory), inputFiles, analysesData, progressMonitor);
			}
			else {
				logger.addMessage("for all possible pairs...");
				analyzer.compareAll(parameters.getRuntimeParameters(), inputFiles, analysesData, progressMonitor);
			}
			logger.addMessage("");  // Line break after progress bar.
			logger.addMessage("Done.");
			
			// Calculate user data:
			if (!parameters.getUserExpressions().getExpressions().isEmpty()) {
				logger.addMessage("Calculating user expressions... ");
				UserExpressionsManager manager = new UserExpressionsManager();
				manager.setExpressions(parameters.getUserExpressions());
				manager.evaluateExpressions(analysesData);
				logger.addMessage("Done.");
			}
			else {
				logger.addMessage("No user expressions have been defined.");
			}
			
			logger.addMessage("All outputs will be written to \"" + outputDirectory.getAbsolutePath() + "\".");
			outputDirectory.mkdirs();
			
			// Write user data tables:
			if (!parameters.getTreeExportColumns().getColumns().isEmpty() || !parameters.getPairExportColumns().getColumns().isEmpty()) {
				logger.addMessage("Writing user data tables... ");
				UserValueTableWriter tableWriter = new UserValueTableWriter();
				tableWriter.writeTreeData(new File(outputDirectory.getAbsolutePath() + File.separator + TREE_DATA_FILE_NAME),
						parameters.getTreeExportColumns(), analysesData.getTreeMap());
				tableWriter.writePairData(new File(outputDirectory.getAbsolutePath() + File.separator + PAIR_DATA_FILE_NAME), 
						parameters.getPairExportColumns(), analysesData.getComparisonMap());
				logger.addMessage("Done.");
			}
			else {
				logger.addMessage("No user values to export have been defined.");
			}
			
			// Write filtered tree output:
			if (!parameters.getFilters().isEmpty()) {
				logger.addMessage("Writing filtered tree files... ");
				new TreeWriter().writeFilterOutputs(parameters.getFilters(), outputDirectory, inputFiles, analysesData.getTreeMap());
				logger.addMessage("Done.");
			}
			else {
				logger.addMessage("No tree filters have been defined.");
			}
			
			logger.addMessage("Finished. (" + analysesData.getTreeCount() + " trees have been analyzed in " + analysesData.getComparisonMap().size() + 
					" pairs.)");
			//throw new IllegalArgumentException("The specified output location \"" + outputDirectory.getAbsolutePath() + "\" is not a directory.");
		} 
		catch (Exception e) {
			logger.addMessage("The error \"" + e.getLocalizedMessage() + "\" occurred.");
			e.printStackTrace(System.err);
		}
	}
	
}
