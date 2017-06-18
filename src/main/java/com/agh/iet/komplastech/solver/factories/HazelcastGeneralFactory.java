package com.agh.iet.komplastech.solver.factories;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.VertexId;
import com.agh.iet.komplastech.solver.support.*;
import com.agh.iet.komplastech.solver.support.HazelcastVertexMap.ExtractLeafMatricesForRegion;
import com.agh.iet.komplastech.solver.support.PartialSolutionManager.GetColumnsAggregator;
import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class HazelcastGeneralFactory implements DataSerializableFactory {

    public static final int GENERAL_FACTORY_ID = 2;
    public static final int MESH = 101;
    public static final int PRODUCTION_ADAPTER = 102;
    public static final int MATRIX = 103;
    public static final int WEAK_VERTEX_REFERENCE = 104;
    public static final int VERTEX = 105;
    public static final int VERTEX_RANGE = 106;
    public static final int SOLUTION = 107;
    public static final int VERTEX_ID = 108;
    public static final int REGION_ID = 109;
    public static final int COMPUTE_CONFIG = 110;
    public static final int VERTEX_REGION_MAPPER = 111;
    public static final int MATRIX_EXTRACTOR = 112;
    public static final int MATRICES_BY_VERTEX = 113;
    public static final int GET_COLS_FROM_ROW = 114;

    @Override
    public IdentifiedDataSerializable create(int typeId) {
        switch (typeId) {
            case VERTEX_ID:
                return new VertexId();
            case MATRIX:
                return new Matrix();
            case VERTEX:
                return new Vertex();
            case VERTEX_RANGE:
                return new VertexRange();
            case MESH:
                return new Mesh();
            case PRODUCTION_ADAPTER:
                return new HazelcastProductionAdapter();
            case WEAK_VERTEX_REFERENCE:
                return new WeakVertexReference();
            case MATRICES_BY_VERTEX:
                return new MatricesByVertex();
            case SOLUTION:
                return new Solution();
            case REGION_ID:
                return new RegionId();
            case COMPUTE_CONFIG:
                return new ComputeConfig();
            case VERTEX_REGION_MAPPER:
                return new VertexRegionMapper();
            case MATRIX_EXTRACTOR:
                return new ExtractLeafMatricesForRegion();
            case GET_COLS_FROM_ROW:
                return new GetColumnsAggregator();
            default:
                throw new IllegalStateException("Could not find type for " + typeId);
        }
    }

}
