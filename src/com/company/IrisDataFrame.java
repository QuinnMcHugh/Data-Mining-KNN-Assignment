package com.company;

import java.util.*;

public class IrisDataFrame implements DataFrame {
	private List<DataRecord> records;
	
	public IrisDataFrame(){
		records = new ArrayList<DataRecord>();
	}
	
	public void parseFromCSV(CSVData csv){
		List<HashMap<String, String>> csvRecords = csv.getRecords();

		for (int i = 0; i < csvRecords.size(); i++){
			// Create new IrisRecord
			IrisRecord data = new IrisRecord();

			data.sepal_length = Double.parseDouble(csvRecords.get(i).get("sepal_length").trim());
			data.sepal_width = Double.parseDouble(csvRecords.get(i).get("sepal_width").trim());
			data.petal_length = Double.parseDouble(csvRecords.get(i).get("petal_length").trim());
			data.petal_width = Double.parseDouble(csvRecords.get(i).get("petal_width").trim());
			data.irisClass = csvRecords.get(i).get("class").trim();
			data.ID = i + 1;
			
			records.add(data);
		}
	}
	
	public List<DataRecord> getRecords(){ return records; }
}