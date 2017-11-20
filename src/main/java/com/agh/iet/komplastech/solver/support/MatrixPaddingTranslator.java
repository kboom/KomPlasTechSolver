package com.agh.iet.komplastech.solver.support;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class MatrixPaddingTranslator {

    public static double[][] withPadding(double[][] original) {
        double[][] paddedMatrix = new double[original.length + 1][original[0].length + 1];
        for(int r = 0; r < original.length; r++) {
            List<Double> collect = Arrays.stream(original[r]).boxed().collect(Collectors.toList());
            collect.add(0, 0d);
            paddedMatrix[r + 1] = ArrayUtils.toPrimitive(collect.toArray(new Double[original[r].length]));
        }
        return paddedMatrix;
    }

    public static double[][] withoutPadding(double[][] original) {
        double[][] trimmedMatrix = new double[original.length - 1][original[1].length - 1];
        for(int r = 1; r < original.length; r++) {
            trimmedMatrix[r - 1] = Arrays.copyOfRange(original[r], 1, original[r].length);
        }
        return trimmedMatrix;
    }

}
