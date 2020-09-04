package info.bioinfweb.osrfilter;


import java.io.File;

import info.bioinfweb.osrfilter.analysis.TopologicalAnalyzer;
import info.bioinfweb.osrfilter.analysis.UserExpressionsManager;
import info.bioinfweb.osrfilter.data.AnalysesData;
import info.bioinfweb.osrfilter.data.parameters.AnalysisParameters;
import info.bioinfweb.osrfilter.io.TableWriter;
import info.bioinfweb.osrfilter.io.TreeWriter;
import info.bioinfweb.osrfilter.io.parameters.AnalysisParameterIO;
import info.bioinfweb.osrfilter.io.treeiterator.AnalysisTreeIterator;



public class Main {
	public static void main(String[] args) {
		if (args.length >= 1) {
			try {
				// Read parameters:
				AnalysisParameters parameters = AnalysisParameterIO.getInstance().read(new File(args[0]));
				
				// Perform topological analysis:
				AnalysesData analysesData = new AnalysesData();
				new TopologicalAnalyzer(parameters.getTextComparisonParameters()).compareAll(parameters.getGroupSize(), 
						new AnalysisTreeIterator(parameters.getTreeFilesNames()), analysesData);
				
				// Calculate user data:
				UserExpressionsManager manager = new UserExpressionsManager();
				manager.setExpressions(parameters.getUserExpressions());
				manager.evaluateExpressions(analysesData);
				
				// Write user data tables:
				parameters.getOutputDirectory().mkdirs();
				TableWriter tableWriter = new TableWriter();
				tableWriter.writeTreeData(new File(parameters.getOutputDirectory().getAbsolutePath() + File.separator + "TreeData.txt"),  //TODO Define constant for filenames.
						parameters.getTreeExportColumns(), analysesData.getTreeMap());
				tableWriter.writePairData(new File(parameters.getOutputDirectory().getAbsolutePath() + File.separator + "PairData.txt"), 
						parameters.getPairExportColumns(), analysesData.getComparisonMap());
				
				// Write filtered tree output:
				new TreeWriter().writeFilterOutputs(parameters.getFilters(), parameters.getOutputDirectory(), parameters.getTreeFilesNames(), analysesData.getTreeMap());
				
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
