package com.agh.iet.komplastech.solver.factories;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.support.*;
import com.agh.iet.komplastech.solver.support.HazelcastVertexMap.ExtractLeafMatricesForRegion;
import com.agh.iet.komplastech.solver.support.PartialSolutionManager.GetColumnsAggregator;
import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static com.agh.iet.komplastech.solver.factories.HazelcastGeneralFactory.GeneralObjectType.getProducerMap;

public class HazelcastGeneralFactory implements DataSerializableFactory {

    public static final int GENERAL_FACTORY_ID = 2;

    public enum GeneralObjectType {

        VERTEX_ID(101, new ObjectProducer<Vertex>() {

            @Override
            public Vertex produce() {
                return new Vertex();
            }

        }),

        MATRIX(102, new ObjectProducer<Matrix>() {

            @Override
            public Matrix produce() {
                return new Matrix();
            }

        }),

        VERTEX(103, new ObjectProducer<Vertex>() {

            @Override
            public Vertex produce() {
                return new Vertex();
            }

        }),

        VERTEX_RANGE(104, new ObjectProducer<VertexRange>() {

            @Override
            public VertexRange produce() {
                return new VertexRange();
            }

        }),

        MESH(105, new ObjectProducer<Mesh>() {

            @Override
            public Mesh produce() {
                return new Mesh();
            }

        }),

        PRODUCTION_ADAPTER(106, new ObjectProducer<HazelcastProductionAdapter>() {

            @Override
            public HazelcastProductionAdapter produce() {
                return new HazelcastProductionAdapter();
            }

        }),

        WEAK_VERTEX_REFERENCE(107, new ObjectProducer<WeakVertexReference>() {

            @Override
            public WeakVertexReference produce() {
                return new WeakVertexReference();
            }

        }),

        MATRICES_BY_VERTEX(108, new ObjectProducer<MatricesByVertex>() {

            @Override
            public MatricesByVertex produce() {
                return new MatricesByVertex();
            }

        }),

        SOLUTION(109, new ObjectProducer<Solution>() {

            @Override
            public Solution produce() {
                return new Solution();
            }

        }),

        REGION_ID(110, new ObjectProducer<RegionId>() {

            @Override
            public RegionId produce() {
                return new RegionId();
            }

        }),

        COMPUTE_CONFIG(111, new ObjectProducer<ComputeConfig>() {

            @Override
            public ComputeConfig produce() {
                return new ComputeConfig();
            }

        }),

        VERTEX_REGION_MAPPER(112, new ObjectProducer<VertexRegionMapper>() {

            @Override
            public VertexRegionMapper produce() {
                return new VertexRegionMapper();
            }

        }),

        MATRIX_EXTRACTOR(113, new ObjectProducer<ExtractLeafMatricesForRegion>() {

            @Override
            public ExtractLeafMatricesForRegion produce() {
                return new ExtractLeafMatricesForRegion();
            }

        }),

        GET_COLS_FROM_ROW(114, new ObjectProducer<GetColumnsAggregator>() {

            @Override
            public GetColumnsAggregator produce() {
                return new GetColumnsAggregator();
            }

        });

        public final int id;
        public final ObjectProducer<?> producer;

        GeneralObjectType(int id, ObjectProducer<?> producer) {
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
