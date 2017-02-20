package com.company;

import java.util.*;

public class SimilarityMetric {
	// Define an enum for metrics defined in this class
	public enum MetricType {
		EUCLIDEAN, COSINE_SIMILARITY
	}

	// Private variable to store what type of metric should be used
	private MetricType type;

	public MetricType getType(){ return this.type; }
	public void setType(MetricType pType){ this.type = pType; }

	public SimilarityMetric(MetricType pType){
		this.type = pType;
	}

	public double calculate(List<Double> v1, List<Double> v2){
		switch (this.type){
			case EUCLIDEAN:
				return euclideanDistance(v1, v2);
			case COSINE_SIMILARITY:
				return cosineSimilarity(v1, v2);
			default:
				this.type = null;
				return -1.0d;
		}
	}

	// Values closest to zero mean highest similarity
	private double euclideanDistance(List<Double> v1, List<Double> v2){
		double sum = 0;
		for (int i = 0; i < v1.size(); i++){
			double diff = v1.get(i) - v2.get(i);
			diff *= diff;
			sum += diff;
		}
		return Math.sqrt(sum);
	}
	
	// Values closest to zero mean highest similarity (subtracting from 1 is my modification which makes this true)
	private double cosineSimilarity(List<Double> v1, List<Double> v2){
		double dotProduct = 0;
		for (int i = 0; i < v1.size(); i++){
			dotProduct += (v1.get(i) * v2.get(i));
		}
		
		double aMag = 0, bMag = 0;
		for (int i = 0; i < v1.size(); i++){
			aMag += Math.pow(v1.get(i), 2);
			bMag += Math.pow(v2.get(i), 2);
		}
		
		aMag = Math.sqrt(aMag);
		bMag = Math.sqrt(bMag);
		
		return 1 - (dotProduct / (aMag * bMag));
	}
}
