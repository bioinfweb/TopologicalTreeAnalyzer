package info.bioinfweb.osrfilter.analysis;


import java.io.File;
import java.io.IOException;

import info.bioinfweb.treegraph.document.undo.CompareTextElementDataParameters;



public class MainAnalyzer {
	private TopologicalAnalyzer topologicalAnalyzer = new TopologicalAnalyzer(new CompareTextElementDataParameters());  //TODO Specify the compare parameters later for every call.
	private UserExpressionsManager expressionsManager = new UserExpressionsManager();
	
	
	public void performAnalysis(int groupSize, File... files) throws IOException, Exception {
//		Map<TreePair, PairComparisonData> comparisons = topologicalAnalyzer.compareAll(groupSize, new TreeIterator(files));
//		expressionsManager.evaluateExpressions();
	}
}
