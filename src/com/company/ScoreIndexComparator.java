package com.company;

import java.util.Comparator;

/**
 * Created by quinnmchugh on 2/19/17.
 */
public class ScoreIndexComparator implements Comparator<ScoreIndexMapping> {
    @Override
    public int compare(ScoreIndexMapping x, ScoreIndexMapping y){
        if (x.score > y.score){
            return 1;
        }
        else if (x.score < y.score){
            return -1;
        }
        else {
            return 0;
        }
    }
}