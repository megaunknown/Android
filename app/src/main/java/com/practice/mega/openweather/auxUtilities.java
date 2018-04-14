package com.practice.mega.openweather;

import java.util.ArrayList;
import java.util.Random;

public class auxUtilities
{
    private static Random rand = new Random();
    /**
     * Generate Random number within a range without duplication.
     * min range
     * max range
     * */
    public static ArrayList<Integer> generateRandomNumberUniquely(int min, int max, int generatedNumber)
    {
        if((max - min)+1 < generatedNumber)
            throw new IllegalArgumentException("The number generatedNumber arguments range isn\'t correct.");

        ArrayList<Integer> list = new ArrayList<>(generatedNumber);

        int iTemp = 0;

        while(list.size()!= generatedNumber)
        {
            iTemp = rand.nextInt(max) + min;
            if(!list.contains(iTemp))
                list.add(iTemp);
        }
        return list;
    }

    /**
     * Generate Random number within a range without duplication.
     * min range
     * max range
     * */
    public static ArrayList<Integer> generateRandomNumber(int min, int max, int generatedNumber)
    {
        if((max - min)+1 < generatedNumber)
            throw new IllegalArgumentException("The number generatedNumber arguments range isn\'t correct.");

        ArrayList<Integer> list = new ArrayList<>(generatedNumber);

        int iTemp = 0;

        while(list.size()!= generatedNumber)
        {
            iTemp = rand.nextInt(max) + min;
            list.add(iTemp);
        }
        return list;
    }
}
