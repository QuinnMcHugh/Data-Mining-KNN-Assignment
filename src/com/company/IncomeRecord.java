package com.company;

import java.util.*;

public class IncomeRecord implements DataRecord {
	public int ID;
	public int age;
	public String workclass;
	public int fnlwgt;
	public String education;
	public int educationCat;
	public String maritalStatus;
	public String occupation;
	public String relationship;
	public String race;
	public String gender;
	public int capitalActivity;
	public int hoursPerWeek;
	public String nativeCountry;
	public String incomeClass;

	public int getID(){ return ID; }
	public String getClassName(){ return incomeClass; }
	
	public String toString(){
		return "{ID: " + ID + ", age" + age + ", workclass: " + workclass + 
		", fnlwgt: " + fnlwgt + ", education: " + education + ", educationCat: " +
		educationCat + ", maritalStatus: " + maritalStatus + ", occupation: " + 
		occupation + ", relationship: " + relationship + ", race: " + race + 
		", gender: " + gender + ", capitalActivity: " + capitalActivity + 
		", hoursPerWeek: " + hoursPerWeek + ", nativeCountry: " + nativeCountry + 
		", incomeClass: " + incomeClass + "}";
	}
	
	public List<Double> toVector(){
		List<Double> vector = new ArrayList<Double>();
		
		// age
		double age_median = 36d, age_std_dev = 13.91, age_z_min = -1.37, age_z_max = 3.31;
		double age_z = (age - age_median) / age_std_dev;
		double age_vector = (age_z + Math.abs(age_z_min)) / (age_z_max + Math.abs(age_z_min));
		vector.add(age_vector);
		
		// workclass
		double workclass_vector = 1d;
		if (workclass.contains("gov")){
			workclass_vector = 0d;
		}
		vector.add(workclass_vector);
		
		// educationCat
		double educationCat_vector = (double)educationCat / 16d;
		vector.add(educationCat_vector);		
		
		// maritalStatus
		double maritalStatus_vector = 1d;
		if (maritalStatus.contains("Never")){
			maritalStatus_vector = 0d;
		}
		vector.add(maritalStatus_vector);
		
		// occupation
		double occupation_vector = 0d;
		if (occupation.contains("Exec-managerial") || occupation.contains("Tech-support")
			|| occupation.contains("Sales") || occupation.contains("Prof-specialty")
			|| occupation.contains("Adm-clerical")){
			occupation_vector = 1d;
		}
		vector.add(occupation_vector);
		
		// race
		double race_vector = 1d;
		if (race.contains("White")){
			race_vector = 0d;
		}
		vector.add(race_vector);
		
		// gender
		double gender_vector = 0d;
		if (gender.contains("Female")){
			gender_vector = 1d;
		}
		vector.add(gender_vector);
		
		// capitalActivity
		double capitalActivity_vector = 1d;
		if (capitalActivity == 0){
			capitalActivity_vector = 0d;
		}
		vector.add(capitalActivity_vector);
		
		// hoursPerWeek
		double hours_median = 40d, hours_std_dev = 12.2, hours_z_min = -3.11, hours_z_max = 4.84;
		double hours_z = (hoursPerWeek - hours_median) / hours_std_dev;
		double hours_vector = (hours_z + Math.abs(hours_z_min)) / (Math.abs(hours_z_min) + hours_z_max);
		vector.add(hours_vector);
		
		// nativeCountry
		double nativeCountry_vector = 1d;
		if (nativeCountry.contains("United-States")){
			nativeCountry_vector = 0d;
		}
		vector.add(nativeCountry_vector);
		
		return vector;
	}
}
