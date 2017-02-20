package com.company;

public class Main {

    public static void main(String[] args) {
        CSVData csvIris = new CSVData("input-data/Iris_Test.csv");

        DataFrame dfIris = new IrisDataFrame();
        dfIris.parseFromCSV(csvIris);

        SimilarityMetric metric = new SimilarityMetric(SimilarityMetric.MetricType.EUCLIDEAN);
        DataRecord compare = dfIris.getRecords().get(21);
        int k = 12;

        ProximityCalculator.produceTable(dfIris, dfIris, k, metric, "out/Iris_Output.csv");
    }
}
