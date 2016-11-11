package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.initialization.HorizontalLeafInitializer;
import com.agh.iet.komplastech.solver.initialization.LeafInitializer;
import com.agh.iet.komplastech.solver.initialization.VerticalLeafInitializer;
import com.agh.iet.komplastech.solver.productions.HorizontalProductionFactory;
import com.agh.iet.komplastech.solver.productions.ProductionFactory;
import com.agh.iet.komplastech.solver.productions.VerticalProductionFactory;
import com.agh.iet.komplastech.solver.support.Mesh;

class TwoDimensionalProblemSolver {

    private final Mesh mesh;

    TwoDimensionalProblemSolver(Mesh meshData) {
        this.mesh = meshData;
    }

    Solution solveProblem() throws Exception {
        ProductionFactory horizontalProductionFactory = new HorizontalProductionFactory(mesh);
        LeafInitializer horizontalLeafInitializer = new HorizontalLeafInitializer(mesh);
        DirectionSolver horizontalProblemSolver = new DirectionSolver(horizontalProductionFactory, horizontalLeafInitializer, mesh);
        Solution horizontalSolution = horizontalProblemSolver.solveInHorizontalDirection();

        ProductionFactory verticalProductionFactory = new VerticalProductionFactory(mesh, horizontalSolution);
        LeafInitializer verticalLeafInitializer = new VerticalLeafInitializer(mesh, horizontalSolution);
        DirectionSolver verticalProblemSolver = new DirectionSolver(verticalProductionFactory, verticalLeafInitializer, mesh);

        return verticalProblemSolver.solveInHorizontalDirection();
    }

}