package com.company;

import java.util.*;
import java.io.*;

public class CSVData {
	private List<String> attributes;
	private List<HashMap<String, String>> records;
	
	public CSVData(String fileName){
		// Instantiate private variables
		attributes = new ArrayList<String>();
		records = new ArrayList<HashMap<String, String>>();
		
		try {
			// Read in provided CSV
			Scanner s = new Scanner(new File(fileName));
		
			// Parse in names of columns
			String headers = s.nextLine();
			String[] atts = headers.split(",");
			for (int i = 0; i < atts.length; i++){
				// take out all instances of quotes to cleanse csv input, trailing whitespace too
				attributes.add(atts[i].replace("\"", "").trim());
			}
			
			// Parse in all records where each record is a Map of attribute to value
			while (s.hasNextLine()){
				String line = s.nextLine();
				String[] vals = line.split(",");
				
				HashMap<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < vals.length; i++){
					// take out all instances of quotes to cleanse csv input
					map.put(attributes.get(i), vals[i].replace("\"", ""));
				}
				records.add(map);
			}
			
			s.close();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public List<String> getAttributes(){ return attributes; }
	public List<HashMap<String, String>> getRecords(){ return records; }
}
