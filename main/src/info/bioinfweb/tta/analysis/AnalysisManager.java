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
package info.bioinfweb.tta.analysis;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import info.bioinfweb.commons.log.ApplicationLogger;
import info.bioinfweb.commons.log.MultipleApplicationLoggersAdapter;
import info.bioinfweb.commons.log.TextFileApplicationLogger;
import info.bioinfweb.tta.Main;
import info.bioinfweb.tta.data.AnalysesData;
import info.bioinfweb.tta.data.UserExpressions;
import info.bioinfweb.tta.data.database.DatabaseTools;
import info.bioinfweb.tta.data.parameters.AnalysisParameters;
import info.bioinfweb.tta.io.TreeWriter;
import info.bioinfweb.tta.io.UserValueTableWriter;
import info.bioinfweb.tta.io.parameters.AnalysisParameterIO;
import info.bioinfweb.tta.io.treeiterator.TreeSelector;
import info.bioinfweb.tta.ui.CmdProgressMonitor;



public class AnalysisManager {
	public static final String LOG_FILE_NAME = "Log.txt";
	public static final String TREE_LIST_FILE_NAME = "Trees.txt";
	public static final String TREE_DATA_FILE_NAME = "TreeData.txt";
	public static final String PAIR_DATA_FILE_NAME = "PairData.txt";
	public static final String TOPOLOGICAL_DATA_FILE_PREFIX = "TopologicalData";
	public static final String USER_DATA_FILE_PREFIX = "UserData";
	public static final String DATABASE_FILE_EXTENSION = ".mv.db";
	
	
	private void logApplicationInfo(ApplicationLogger logger) {
		logger.addMessage(Main.APPLICATION_NAME);
		logger.addMessage("Version " + Main.getInstance().getVersion());
		logger.addMessage("<" + Main.APPLICATION_URL + ">");
		logger.addMessage("");
	}
	
	
	public static String createDatabaseURL(File outputDirectory, String filePrefix) {
		return "jdbc:h2:" + outputDirectory.getAbsolutePath() + File.separator + filePrefix;
	}
	
	
	public static void createUserValueDatabase(File outputDirectory, UserExpressions userExpressions) throws SQLException {
		Connection userDataConnection = DriverManager.getConnection(createDatabaseURL(outputDirectory, AnalysisManager.USER_DATA_FILE_PREFIX));
		try {
			DatabaseTools.createUserDataTables(userDataConnection, userExpressions);
		}
		finally {
			userDataConnection.close();
		}
	}
	
	
	public void runAnalysis(String parametersFileName, ApplicationLogger outputLogger) {
		MultipleApplicationLoggersAdapter logger = null;
		try {
			File parametersFile = new File(parametersFileName).getAbsoluteFile();
			File parametersFileDirectory = new File(parametersFile.getParent()).getAbsoluteFile();
			
			// Read parameters:
			AnalysisParameters parameters = AnalysisParameterIO.getInstance().read(parametersFile);
			String[] inputFiles = parameters.getRelativizedTreeFilesNames(parametersFileDirectory);
			
			// Determine output directory:
			File outputDirectory;
			if (parameters.getOutputDirectory().isAbsolute()) {
				outputDirectory = parameters.getOutputDirectory();
			}
			else {
				outputDirectory = new File(parametersFileDirectory.getAbsolutePath() + File.separator + parameters.getOutputDirectory().toString());
			}
			outputDirectory.mkdirs();
			
			// Start logging:
			logger = new MultipleApplicationLoggersAdapter();
			logger.getLoggers().add(outputLogger);
			TextFileApplicationLogger fileLogger = new TextFileApplicationLogger(new File(outputDirectory.getAbsolutePath() + File.separator + LOG_FILE_NAME), true);
			logger.getLoggers().add(fileLogger);
			try {
				logApplicationInfo(logger);
				logger.addMessage("Parameters read from \"" + parametersFile.getAbsolutePath() + "\".");
				logger.addMessage("All outputs will be written to \"" + outputDirectory.getAbsolutePath() + "\".");
				
				// Perform topological analysis:
				TreeSelector treeSelector = null;
				if (parameters.definedReferenceTree()) {
					logger.addMessage("Performing topological analysis for a single reference tree...");
					treeSelector = parameters.getReferenceTree().createTreeSelector(parametersFileDirectory);
				}
				else {
					logger.addMessage("Performing topological analysis for all possible pairs...");
				}
				
				CmdProgressMonitor progressMonitor = new CmdProgressMonitor();	//TODO This should be parameterized. (Will not always display progress on the console.)
				AnalysesData analysesData = new TopologicalAnalyzer(parameters.getTextComparisonParameters()).
						performAnalysis(inputFiles, outputDirectory, treeSelector, parameters, progressMonitor);

				try {
					System.out.println();;  // Line break after progress bar.  //TODO This should be done differently since the progress might have been displayed in the GUI.
					logger.addMessage("Done.");
					
					// Calculate user data:
					if (!parameters.getUserExpressions().getExpressions().isEmpty()) {
						logger.addMessage("Calculating user expressions... ");
						UserExpressionsManager manager = new UserExpressionsManager();
						manager.setExpressions(parameters.getUserExpressions());
						createUserValueDatabase(outputDirectory, parameters.getUserExpressions());
						manager.evaluateExpressions(analysesData, new CmdProgressMonitor());	//TODO The progress monitor should be parameterized. (Will not always display progress on the console.)
						System.out.println();
						logger.addMessage("Done.");
					}
					else {
						logger.addMessage("No user expressions have been defined.");
					}
					
					// Write user data tables:
					if (!parameters.getTreeExportColumns().getColumns().isEmpty() || !parameters.getPairExportColumns().getColumns().isEmpty()) {
						logger.addMessage("Writing user data tables... ");
						UserValueTableWriter tableWriter = new UserValueTableWriter();
						//TODO Write tree list and use only index as tree identifier in subsequent tables.
						tableWriter.writeTreeList(new File(outputDirectory.getAbsolutePath() + File.separator + TREE_LIST_FILE_NAME),
								analysesData.getInputOrder(), parameters.getTreeExportColumns().getColumnDelimiter(), parameters.getTreeExportColumns().getLineDelimiter());
						tableWriter.writeTreeData(new File(outputDirectory.getAbsolutePath() + File.separator + TREE_DATA_FILE_NAME),
								analysesData.getInputOrder(), parameters.getTreeExportColumns(), analysesData.getTreeUserData());
						tableWriter.writePairData(new File(outputDirectory.getAbsolutePath() + File.separator + PAIR_DATA_FILE_NAME), 
								analysesData.getInputOrder(), parameters.getPairExportColumns(), analysesData.getPairUserData());
						logger.addMessage("Done.");
					}
					else {
						logger.addMessage("No user values to export have been defined.");
					}
					
					// Write filtered tree output:
					if (!parameters.getFilters().isEmpty()) {
						logger.addMessage("Writing filtered tree files... ");
						new TreeWriter().writeFilterOutputs(parameters.getFilters(), outputDirectory, inputFiles, analysesData.getTreeUserData());
						logger.addMessage("Done.");
					}
					else {
						logger.addMessage("No tree filters have been defined.");
					}
					
					logger.addMessage("Finished. (" + analysesData.getTreeCount() + " trees have been analyzed in " /*+ analysesData.getComparisonMap().size()*/ + 
							" pairs.)");  //TODO Find new way to determine number of pairs.
					//throw new IllegalArgumentException("The specified output location \"" + outputDirectory.getAbsolutePath() + "\" is not a directory.");
				}
				finally {
					analysesData.close();
				}
			}
			finally {
				fileLogger.close();  //TODO This does not help if the application is terminated externally. File logging should be refactored to close the output file after each update as done for the topological data files.
			}
		} 
		catch (Exception e) {
			ApplicationLogger exceptionLogger = outputLogger; 
			if (logger != null) {
				exceptionLogger = logger;
			}
			exceptionLogger.addMessage("The error \"" + e.getLocalizedMessage() + "\" occurred.");
			e.printStackTrace(System.err);
		}
	}
	
}
