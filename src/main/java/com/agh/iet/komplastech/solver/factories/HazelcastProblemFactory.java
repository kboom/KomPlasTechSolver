package com.agh.iet.komplastech.solver.factories;

import com.agh.iet.komplastech.solver.problem.ConstantLinearProblem;
import com.agh.iet.komplastech.solver.problem.ConstantOneProblem;
import com.agh.iet.komplastech.solver.problem.HeatTransferProblem;
import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class HazelcastProblemFactory implements DataSerializableFactory {

    public static final int PROBLEM_FACTORY = 3;
    public static final int CONSTANT_ONE = 1001;
    public static final int CONSTANT_LINEAR = 1002;
    public static final int HEAT = 1003;

    @Override
    public IdentifiedDataSerializable create(int typeId) {
        switch (typeId) {
            case CONSTANT_ONE:
                return new ConstantOneProblem();
            case CONSTANT_LINEAR:
                return new ConstantLinearProblem();
            case HEAT:
                return new HeatTransferProblem();
            default:
                throw new IllegalStateException("Do not know problem for type " + typeId);
        }
    }

}
