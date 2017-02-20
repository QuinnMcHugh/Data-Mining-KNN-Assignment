package com.company;

import java.util.*;

public class IrisRecord implements DataRecord {
	public double sepal_length;
	public double sepal_width;
	public double petal_length;
	public double petal_width;
	public String irisClass;
	public int ID;

	public int getID(){ return ID; }
	public String getClassName(){ return irisClass; }

	public String toString(){
		return "{sepal_length: " + sepal_length + ", sepal_width: "
			 + sepal_width + ", petal_length: " + petal_length + 
			 ", petal_width: " + petal_width + ", irisClass: " + 
			 irisClass + ", ID: " + ID + "}";
	}

	public List<Double> toVector(){
		List<Double> vector = new ArrayList<Double>();

		vector.add(sepal_length);
		vector.add(sepal_width);
		vector.add(petal_length);
		vector.add(petal_width);

		return vector;
	}
}