package com.agh.iet.komplastech.solver.factories;

import com.agh.iet.komplastech.solver.productions.CompositeProduction;
import com.agh.iet.komplastech.solver.productions.construction.P1;
import com.agh.iet.komplastech.solver.productions.construction.P1y;
import com.agh.iet.komplastech.solver.productions.construction.P2;
import com.agh.iet.komplastech.solver.productions.construction.P3;
import com.agh.iet.komplastech.solver.productions.initialization.A;
import com.agh.iet.komplastech.solver.productions.initialization.Ay;
import com.agh.iet.komplastech.solver.productions.solution.backsubstitution.*;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_2;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_2_H;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_3;
import com.agh.iet.komplastech.solver.productions.solution.factorization.Aroot;
import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class HazelcastProductionFactory implements DataSerializableFactory {

    public static final int PRODUCTION_FACTORY = 1;

    public static final int A_PRODUCTION = 1;
    public static final int P1_PRODUCTION = 2;
    public static final int P1y_PRODUCTION = 3;
    public static final int P2_PRODUCTION = 4;
    public static final int P3_PRODUCTION = 5;
    public static final int Ay_PRODUCTION = 6;
    public static final int BS_1_5_PRODUCTION = 7;
    public static final int BS_2_6_PRODUCTION = 8;
    public static final int BS_2_6_H_PRODUCTION = 9;
    public static final int E2_1_5_PRODUCTION = 10;
    public static final int E2_2_6_PRODUCTION = 11;
    public static final int ERoot_PRODUCTION = 12;
    public static final int A2_2_PRODUCTION = 13;
    public static final int A2_2_H_PRODUCTION = 14;
    public static final int A2_3_PRODUCTION = 15;
    public static final int ARoot_PRODUCTION = 16;
    public static final int COMPOSITE_PRODUCTION = 17;

    @Override
    public IdentifiedDataSerializable create(int typeId) {
        switch (typeId) {
            case A_PRODUCTION:
                return new A();
            case P1_PRODUCTION:
                return new P1();
            case P1y_PRODUCTION:
                return new P1y();
            case P2_PRODUCTION:
                return new P2();
            case P3_PRODUCTION:
                return new P3();
            case Ay_PRODUCTION:
                return new Ay();
            case BS_1_5_PRODUCTION:
                return new BS_1_5();
            case BS_2_6_PRODUCTION:
                return new BS_2_6();
            case BS_2_6_H_PRODUCTION:
                return new BS_2_6_H();
            case E2_1_5_PRODUCTION:
                return new E2_1_5();
            case E2_2_6_PRODUCTION:
                return new E2_2_6();
            case ERoot_PRODUCTION:
                return new Eroot();
            case A2_2_PRODUCTION:
                return new A2_2();
            case A2_2_H_PRODUCTION:
                return new A2_2_H();
            case A2_3_PRODUCTION:
                return new A2_3();
            case ARoot_PRODUCTION:
                return new Aroot();
            case COMPOSITE_PRODUCTION:
                return new CompositeProduction();
            default:
                throw new IllegalStateException("Do not know type " + typeId);
        }
    }

}
