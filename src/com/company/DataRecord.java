package com.company;

import java.util.*;

public interface DataRecord {
	public String toString();
	public List<Double> toVector();
	public int getID();
	public String getClassName();
}