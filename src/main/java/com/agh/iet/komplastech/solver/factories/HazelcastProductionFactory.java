package com.agh.iet.komplastech.solver.factories;

import com.agh.iet.komplastech.solver.productions.CompositeProduction;
import com.agh.iet.komplastech.solver.productions.construction.P1;
import com.agh.iet.komplastech.solver.productions.construction.P1y;
import com.agh.iet.komplastech.solver.productions.construction.P2;
import com.agh.iet.komplastech.solver.productions.construction.P3;
import com.agh.iet.komplastech.solver.productions.initialization.A;
import com.agh.iet.komplastech.solver.productions.initialization.Ay;
import com.agh.iet.komplastech.solver.productions.solution.ExtractSolutionProduction;
import com.agh.iet.komplastech.solver.productions.solution.backsubstitution.*;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_2;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_2_H;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_3;
import com.agh.iet.komplastech.solver.productions.solution.factorization.Aroot;
import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.ProductionType.getProducerMap;

public class HazelcastProductionFactory implements DataSerializableFactory {

    public static final int PRODUCTION_FACTORY = 1;

    public enum ProductionType {

        A_PRODUCTION(1, new ObjectProducer<A>() {

            @Override
            public A produce() {
                return new A();
            }

        }),

        P1_PRODUCTION(2, new ObjectProducer<P1>() {

            @Override
            public P1 produce() {
                return new P1();
            }

        }),

        P1y_PRODUCTION(3, new ObjectProducer<P1y>() {

            @Override
            public P1y produce() {
                return new P1y();
            }

        }),

        P2_PRODUCTION(4, new ObjectProducer<P2>() {

            @Override
            public P2 produce() {
                return new P2();
            }

        }),

        P3_PRODUCTION(5, new ObjectProducer<P3>() {

            @Override
            public P3 produce() {
                return new P3();
            }

        }),

        Ay_PRODUCTION(6, new ObjectProducer<Ay>() {

            @Override
            public Ay produce() {
                return new Ay();
            }

        }),

        EXTRACT_SOLUTION_PRODUCTION(7, new ObjectProducer<ExtractSolutionProduction>() {

            @Override
            public ExtractSolutionProduction produce() {
                return new ExtractSolutionProduction();
            }

        }),

        BS_1_5_PRODUCTION(8, new ObjectProducer<BS_1_5>() {

            @Override
            public BS_1_5 produce() {
                return new BS_1_5();
            }

        }),

        BS_2_6_PRODUCTION(9, new ObjectProducer<BS_2_6>() {

            @Override
            public BS_2_6 produce() {
                return new BS_2_6();
            }

        }),

        BS_2_6_H_PRODUCTION(10, new ObjectProducer<BS_2_6_H>() {

            @Override
            public BS_2_6_H produce() {
                return new BS_2_6_H();
            }

        }),

        E2_1_5_PRODUCTION(11, new ObjectProducer<E2_1_5>() {

            @Override
            public E2_1_5 produce() {
                return new E2_1_5();
            }

        }),

        E2_2_6_PRODUCTION(12, new ObjectProducer<E2_2_6>() {

            @Override
            public E2_2_6 produce() {
                return new E2_2_6();
            }

        }),

        ERoot_PRODUCTION(13, new ObjectProducer<Eroot>() {

            @Override
            public Eroot produce() {
                return new Eroot();
            }

        }),

        A2_2_PRODUCTION(14, new ObjectProducer<A2_2>() {

            @Override
            public A2_2 produce() {
                return new A2_2();
            }

        }),

        A2_2_H_PRODUCTION(15, new ObjectProducer<A2_2_H>() {

            @Override
            public A2_2_H produce() {
                return new A2_2_H();
            }

        }),

        A2_3_PRODUCTION(16, new ObjectProducer<A2_3>() {

            @Override
            public A2_3 produce() {
                return new A2_3();
            }

        }),

        ARoot_PRODUCTION(17, new ObjectProducer<Aroot>() {

            @Override
            public Aroot produce() {
                return new Aroot();
            }

        }),

        COMPOSITE_PRODUCTION(18, new ObjectProducer<CompositeProduction>() {

            @Override
            public CompositeProduction produce() {
                return new CompositeProduction();
            }

        });


        public final int id;
        public final ObjectProducer<?> producer;

        ProductionType(int id, ObjectProducer<?> producer) {
            this.id = id;
            this.producer = producer;
        }

        public static Map<Integer, ObjectProducer<?>> getProducerMap() {
            return Arrays.stream(values()).collect(Collectors.toMap(o -> o.id, o -> o.producer));
        }

    }

    private static final Map<Integer, ObjectProducer<?>> producerMap = getProducerMap();

    @Override
    public IdentifiedDataSerializable create(int typeId) {
        return producerMap.get(typeId).produce();
    }

}
