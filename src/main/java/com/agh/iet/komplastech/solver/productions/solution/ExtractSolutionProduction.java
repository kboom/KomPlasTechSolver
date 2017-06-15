package com.agh.iet.komplastech.solver.productions.solution;

import com.agh.iet.komplastech.solver.productions.ProcessingContext;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.MathUtils;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.PartialSolutionManager;
import com.agh.iet.komplastech.solver.support.Vertex;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;
import java.util.HashMap;

import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.EXTRACT_SOLUTION_PRODUCTION;
import static com.agh.iet.komplastech.solver.factories.HazelcastProductionFactory.PRODUCTION_FACTORY;

public class ExtractSolutionProduction implements Production {


    @Override
    public void apply(ProcessingContext context) {
        final Vertex vertex = context.getVertex();
        final Mesh mesh = context.getMesh();
        final PartialSolutionManager partialSolutionManager = context.getPartialSolutionManager();

        int absoluteIndex = vertex.getVertexId().getAbsoluteIndex();
        int treeHeight = MathUtils.log2(mesh.getElementsX() / 3);
        int firstElement = (int) Math.pow(2, treeHeight);

        if (absoluteIndex == firstElement) {
            partialSolutionManager.setAll(

                    new HashMap<Integer, double[]>() {{
                        put(1, vertex.m_x.getRow(1));
                        put(2, vertex.m_x.getRow(2));
                        put(3, vertex.m_x.getRow(3));
                        put(4, vertex.m_x.getRow(4));
                        put(5, vertex.m_x.getRow(5));
                    }}
            );
        } else {
            int offset = 6 + (absoluteIndex - firstElement - 1) * 3;
            partialSolutionManager.setAll(

                    new HashMap<Integer, double[]>() {{
                        put(offset, vertex.m_x.getRow(3));
                        put(offset + 1, vertex.m_x.getRow(4));
                        put(offset + 2, vertex.m_x.getRow(5));
                    }}

            );
        }

    }

    @Override
    public int getFactoryId() {
        return PRODUCTION_FACTORY;
    }

    @Override
    public int getId() {
        return EXTRACT_SOLUTION_PRODUCTION;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {

    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {

    }

}
