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
			siMapping.index = records.get(i).getID();

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
			String type = records.get(siMapping.index - 1).getClassName();
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

//	public static List<Double> getKMostSimilar(DataFrame input, DataRecord compare, int k, String metric){
//		// this method should return one row in the final output table
//
//		// go through each record in dataframe and compute similarity score
//		// then go back and find k best scores.
//		// extract ID and score information of these and return
//
//		List<Double> scores = new ArrayList<Double>();
//		List<DataRecord> records = input.getRecords();
//
//		for (int i = 0; i < records.size(); i++){
//			List<Double> vector = records.get(i).toVector();
//			double result = 0d;
//
//			// compute the distance between the two vectors
//			if (metric.equals("euclidean")){
//				result = SimilarityMetrics.euclideanDistance(vector, compare.toVector());
//			}
//			else if (metric.equals("cosine")){
//				result = SimilarityMetrics.cosineSimilarity(vector, compare.toVector());
//			}
//
//			// add result to scores listing
//			scores.add(result);
//		}
//
//		List<Double> topKScores = new ArrayList<Double>();
//		List<Integer> topKIDs = new ArrayList<Integer>();
//
//		for (int i = 0; i < k; i++){
//			// find min value in scores
//			int minIndex = getMinIndex(scores);
//			topKIDs.add(records.get(minIndex).getID());
//			topKScores.add(scores.get(minIndex));
//
//			// set previously found value to large number and find next smallest
//			scores.set(minIndex, Double.MAX_VALUE);
//		}
//
//		// weave together top k ID's and scores into one list formatted for table
//		List<Double> ID_proximity = new ArrayList<Double>();
//		for (int i = 0; i < topKIDs.size(); i++){
//			ID_proximity.add((double)topKIDs.get(i));
//			ID_proximity.add(topKScores.get(i));
//		}
//
//		return ID_proximity;
//	}
//
//	// returns the index with the smallest value
//	private static int getMinIndex(List<Double> scores){
//		double min = Double.MAX_VALUE;
//		int minIndex = 0;
//
//		for (int i = 0; i < scores.size(); i++){
//			if (scores.get(i) < min){
//				min = scores.get(i);
//				minIndex = i;
//			}
//		}
//
//		return minIndex;
//	}
//
//	public static void produceTable(DataFrame input, DataFrame test, int k, String metric, String outputFileName){
//		// Set up table headers
//		String fileText = "Trans. ID,";
//		for (int i = 1; i <= k; i++){
//			fileText += (i + " ID,");
//			fileText += (i + " Prox.,");
//		}
//		fileText += "\n";
//
//		List<DataRecord> testRecords = test.getRecords();
//
//		// iterate through each test record, compute k best matches, add to file text
//		for (int i = 0; i < testRecords.size(); i++){
//			List<Double> similar = getKMostSimilar(input, testRecords.get(i), k, metric);
//
//			fileText += ((i + 1) + ",");
//			for (int j = 0; j < similar.size(); j++){
//				if (j % 2 == 0){
//					fileText += similar.get(j);
//				}
//				else {
//					fileText += similar.get(j);
//				}
//				fileText += ",";
//			}
//
//			fileText += "\n";
//		}
//
//		// Print file text to file named by parameter outputFileName
//		try {
//			PrintWriter p = new PrintWriter(new File(outputFileName));
//			p.print(fileText);
//			p.close();
//		}
//		catch (IOException e){
//			e.printStackTrace();
//		}
//
//	}
}