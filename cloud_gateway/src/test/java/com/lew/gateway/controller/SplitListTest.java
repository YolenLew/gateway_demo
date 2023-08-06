/*
 * Copyright (c) 2023. This is my custom copyright information.2023-2023. All rights reserved.
 */

package com.lew.gateway.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * @author Yolen
 * @date 2023/8/6
 */
public class SplitListTest {

    public static void main(String[] args) {
        List<Bean> originalList = generateMockData();

        // Sort the original list by count in ascending order
        originalList.sort(Comparator.comparingInt(Bean::getCount));

        List<List<Bean>> splitLists = splitListByValue(originalList, Bean::getCount, 300000);

        // Print the split lists
        for (int i = 0; i < splitLists.size(); i++) {
            System.out.println("Subset " + (i + 1) + ":");
            List<Bean> subList = splitLists.get(i);
            for (Bean bean : subList) {
                System.out.println("Count: " + bean.getCount());
            }
            System.out.println("--------------------");
        }
    }

    // Mock method to generate sample data
    public static List<Bean> generateMockData() {
        List<Bean> mockData = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            int count = random.nextInt(90000) + 10000; // Generating random count between 10,000 and 100,000
            mockData.add(new Bean(count));
        }
        return mockData;
    }

    // A generic method to split a list of objects based on a numerical field value
    public static <T> List<List<T>> splitListByValue(List<T> originalList, ValueExtractor<T> valueExtractor,
        int maxValue) {
        List<List<T>> result = new ArrayList<>();
        List<T> currentSubList = new ArrayList<>();
        int currentCount = 0;

        for (T item : originalList) {
            int value = valueExtractor.extractValue(item);

            if (currentCount + value <= maxValue) {
                currentSubList.add(item);
                currentCount += value;
            } else {
                result.add(currentSubList);
                currentSubList = new ArrayList<>();
                currentSubList.add(item);
                currentCount = value;
            }
        }

        if (!currentSubList.isEmpty()) {
            result.add(currentSubList);
        }

        return result;
    }
}

// Functional interface to extract the numerical value from an object
@FunctionalInterface
interface ValueExtractor<T> {
    int extractValue(T item);
}

class Bean {
    private int count;

    public Bean(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
