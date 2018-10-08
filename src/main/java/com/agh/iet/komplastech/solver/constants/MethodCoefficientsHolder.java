package com.agh.iet.komplastech.solver.constants;

import static com.agh.iet.komplastech.solver.constants.ExplicitMethodCoefficients.EXPLICIT_METHOD_COEFFICIENTS;

public class MethodCoefficientsHolder {

    private static MethodCoefficients methodCoefficients = EXPLICIT_METHOD_COEFFICIENTS;

    public static void setMethodCoefficients(MethodCoefficients methodCoefficients) {
        MethodCoefficientsHolder.methodCoefficients = methodCoefficients;
    }

    public static MethodCoefficients getMethodCoefficients() {
        return methodCoefficients;
    }

}
