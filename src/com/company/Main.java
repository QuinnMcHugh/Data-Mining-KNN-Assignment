package com.company;

public class Main {

    public static void main(String[] args) {
        CSVData csvIncome = new CSVData("input-data/Income_Test.csv");

        DataFrame dfIncome = new IncomeDataFrame();
        dfIncome.parseFromCSV(csvIncome);

        SimilarityMetric metric = new SimilarityMetric(SimilarityMetric.MetricType.EUCLIDEAN);
        int k = 12;
        // training set
        // test set

        DataFrame dfIncomeTest = new IncomeDataFrame();
        IncomeRecord me = new IncomeRecord();
        me.age = 22;
        me.capitalActivity = 0;
        me.educationCat = 14;
        me.education = "Masters";
        me.gender = "Female";
        me.hoursPerWeek = 40;
        me.incomeClass = ">50k";
        me.maritalStatus = "Never-married";
        me.nativeCountry = "United-States";
        me.occupation = "Prof-specialty";
        me.race = "White";
        me.relationship = "Not-in-family";
        me.workclass = "Local-gov";

        dfIncomeTest.getRecords().add(me);

        ProximityCalculator.produceTable(dfIncome, dfIncomeTest, k, metric, "out/Income_Output.csv");
    }
}
