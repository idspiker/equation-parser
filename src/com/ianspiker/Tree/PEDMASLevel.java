package com.ianspiker.Tree;

import java.util.Comparator;

enum PEDMASLevel {
    LOW, MEDIUM, HIGH, HIGHEST;

    private static Comparator<PEDMASLevel> comparator = 
            Comparator.comparing(o -> o.getConstantInt());

    public static Comparator<PEDMASLevel> getComparator() {
        return comparator;
    }

    private int getConstantInt() {

        return switch(this) {
            case LOW -> 0;
            case MEDIUM -> 1;
            case HIGH -> 2;
            case HIGHEST -> 3;
        };
    }
}
