package com.company;

import java.util.*;

public class IncomeDataFrame implements DataFrame {
	private List<DataRecord> records;
	
	public IncomeDataFrame(){
		records = new ArrayList<DataRecord>();
	}
	
	public void parseFromCSV(CSVData csv){
		List<HashMap<String, String>> csvRecords = csv.getRecords();
		
		for (int i = 0; i < csvRecords.size(); i++){
			// If record contains missing data, then throw it out
			if (csvRecords.get(i).values().contains(" ?")){
				continue;
			}
			
			// Create new IncomeRecord
			IncomeRecord data = new IncomeRecord();
			
			// Below populates IncomeRecord
			data.ID = Integer.parseInt(csvRecords.get(i).get("ID"));
			data.age = Integer.parseInt(csvRecords.get(i).get("age"));
			
			// Remove leading whitespace from cells of String value
			data.workclass = csvRecords.get(i).get("workclass").trim();
			data.fnlwgt = Integer.parseInt(csvRecords.get(i).get("fnlwgt"));
			data.education = csvRecords.get(i).get("education").trim();
			data.educationCat = Integer.parseInt(csvRecords.get(i).get("education_cat"));
			data.maritalStatus = csvRecords.get(i).get("marital_status").trim();
			data.occupation = csvRecords.get(i).get("occupation").trim();
			data.relationship = csvRecords.get(i).get("relationship").trim();
			data.race = csvRecords.get(i).get("race").trim();
			data.gender = csvRecords.get(i).get("gender").trim();
			
			data.capitalActivity = Integer.parseInt(csvRecords.get(i).get("capital_gain")) + 
									Integer.parseInt(csvRecords.get(i).get("capital_loss"));
			data.hoursPerWeek = Integer.parseInt(csvRecords.get(i).get("hour_per_week"));
			data.nativeCountry = csvRecords.get(i).get("native_country").trim();
			data.incomeClass = csvRecords.get(i).get("class").trim();
			
			// Minor data corrections
			if (data.nativeCountry.equals("Hong")){
				data.nativeCountry = "Hong Kong";
			}
			if (data.nativeCountry.equals("South")){
				data.nativeCountry = "South Korea";
			}
			
			records.add(data);
		}
	}
	
	public List<DataRecord> getRecords(){ return records; }
}
