package info.bioinfweb.osrfilter;


import java.io.File;

import info.bioinfweb.osrfilter.analysis.TopologicalAnalyzer;
import info.bioinfweb.osrfilter.analysis.UserExpressionsManager;
import info.bioinfweb.osrfilter.data.AnalysesData;
import info.bioinfweb.osrfilter.data.parameters.AnalysisParameters;
import info.bioinfweb.osrfilter.io.TableWriter;
import info.bioinfweb.osrfilter.io.TreeWriter;
import info.bioinfweb.osrfilter.io.parameters.AnalysisParameterIO;
import info.bioinfweb.osrfilter.ui.CmdProgressMonitor;



public class Main {
	public static final String TREE_DATA_FILE_NAME = "TreeData.txt";
	public static final String PAIR_DATA_FILE_NAME = "PairData.txt";
	
	
	public static void main(String[] args) {
		if (args.length >= 1) {
			try {
				File parametersFile = new File(args[0]).getAbsoluteFile();
				File parametersFileDirectory = new File(parametersFile.getParent()).getAbsoluteFile();
				
				// Read parameters:
				System.out.print("Reading parameters from \"" + parametersFile.getAbsolutePath() + "\"... ");
				AnalysisParameters parameters = AnalysisParameterIO.getInstance().read(parametersFile);
				System.out.println("Done.");
				
				// Perform topological analysis:
				System.out.println("Performing topological analysis... ");
				AnalysesData analysesData = new AnalysesData();
				new TopologicalAnalyzer(parameters.getTextComparisonParameters()).compareAll(parameters.getGroupSize(), 
						parameters.getRelativizedTreeFilesNames(parametersFileDirectory), analysesData, new CmdProgressMonitor());
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
					new TreeWriter().writeFilterOutputs(parameters.getFilters(), parameters.getOutputDirectory(), parameters.getTreeFilesNames(), analysesData.getTreeMap());
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
		else {
			System.out.println("A parameter file needs to be specified.");
		}
	}
}
