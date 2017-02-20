package com.company;

import java.util.*;
import java.io.*;

public class ProximityCalculator {

	// Within a DataFrame 'input', find the 'k' most
	// similar records to 'compare' using 'metric' for calculation
	private static List<ScoreIndexMapping> getKMostSimilar(DataFrame input, DataRecord compare, int k, SimilarityMetric metric){
		List<ScoreIndexMapping> mostSimilar = new ArrayList<>();
		List<DataRecord> records = input.getRecords();

		Comparator<ScoreIndexMapping> comparator = new ScoreIndexComparator();
		PriorityQueue<ScoreIndexMapping> heap = new PriorityQueue<>(records.size(), comparator);

		for (int i = 0; i < records.size(); i++){
			ScoreIndexMapping siMapping = new ScoreIndexMapping();

			// Compute score between records[i] and compare
			siMapping.score = metric.calculate(compare.toVector(), records.get(i).toVector());
			siMapping.index = i;

			// Add new ScoreIndex pair to heap
			heap.add(siMapping);
		}

		// Fill list with top k ScoreIndexMappings, return list
		for (int i = 0; i < k; i++){
			mostSimilar.add(heap.remove());
		}

		return mostSimilar;
	}

	// Tuple bounding class label (String) and posterior probability (double)
	private static class PredictedClassInfo {
		public String label;
		public double posterior;
		public String toString(){ return label + " -> " + (posterior * 100) + "%"; }
	}

	// Returns a tuple (PredictedClassInfo object) containing class name and posterior probability
	private static PredictedClassInfo getPredictedClassInfo(DataFrame input, List<ScoreIndexMapping> kMostSimilar){
		List<DataRecord> records = input.getRecords();
		Map<String, Integer> classOccurrences = new HashMap<>();

		// Compile Map of each of the k nearest neighbors by class label to count in k-nearest
		for (ScoreIndexMapping siMapping : kMostSimilar){
			String type = records.get(siMapping.index).getClassName();
			if (classOccurrences.containsKey(type)){
				classOccurrences.put(type, classOccurrences.get(type) + 1);
			}
			else {
				classOccurrences.put(type, 1);
			}
		}

		System.out.println(classOccurrences);

		// Find the class label with the most occurrences in the k nearest neighbors
		String maxKey = null;
		int count = 0;
		for (Map.Entry<String, Integer> entry : classOccurrences.entrySet()){
			if (maxKey == null || entry.getValue() > classOccurrences.get(maxKey)){
				maxKey = entry.getKey();
			}
			count += entry.getValue();
		}

		PredictedClassInfo labelInfo = new PredictedClassInfo();
		labelInfo.label = maxKey;
		labelInfo.posterior = (double)classOccurrences.get(maxKey) / (double)count;

		return labelInfo;
	}

	public static void produceTable(DataFrame prior, DataFrame test, int k, SimilarityMetric metric, String fileName){
		// Set up table headers
		String fileText = "Transaction ID,";
		fileText += "Actual Class,";
		fileText += "Predicted Class,";
		fileText += "Posterior Probability\n";

		List<DataRecord> priorRecords = prior.getRecords();
		List<DataRecord> testRecords = test.getRecords();

		System.out.println(getPredictedClassInfo(prior, getKMostSimilar(prior, test.getRecords().get(0), k, metric)));

		for (int i = 0; i < testRecords.size(); i++){
			// Transaction ID field
			fileText += (Integer.toString(i + 1) + ",");

			// Actual Class field
			fileText += (testRecords.get(i).getClassName() + ",");

			// Compute prediction
			PredictedClassInfo info = getPredictedClassInfo(prior, getKMostSimilar(prior, testRecords.get(i), k, metric));

			// Predicted Class field
			fileText += info.label + ",";

			// Posterior Probability field
			fileText += info.posterior + "\n";
		}

		// Write CSV to disk
		writeToFile(fileName, fileText);
	}

	// Writes text to the file specified
	private static void writeToFile(String fileName, String text){
		try {
			PrintWriter p = new PrintWriter(new File(fileName));
			p.print(text);
			p.close();
		}
		catch (IOException e){
			e.printStackTrace();;
		}
	}
}