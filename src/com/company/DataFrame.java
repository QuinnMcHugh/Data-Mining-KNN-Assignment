package com.company;

import java.util.*;

public interface DataFrame {
	public void parseFromCSV(CSVData csv);
	public List<DataRecord> getRecords();
}
