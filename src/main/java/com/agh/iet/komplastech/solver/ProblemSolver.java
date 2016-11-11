package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.productions.HorizontalProductionFactory;
import com.agh.iet.komplastech.solver.productions.ProductionFactory;
import com.agh.iet.komplastech.solver.productions.VerticalProductionFactory;
import com.agh.iet.komplastech.solver.support.Mesh;

class ProblemSolver {

    private final Mesh mesh;

    ProblemSolver(Mesh meshData) {
        this.mesh = meshData;
    }

    Solution solveProblem() throws Exception {
        ProductionFactory horizontalProductionFactory = new HorizontalProductionFactory(mesh);
        LeafInitializer horizontalLeafInitializer = new HorizontalLeafInitializer(mesh);
        SingleDirectionSolver horizontalProblemSolver = new SingleDirectionSolver(horizontalProductionFactory, horizontalLeafInitializer, mesh);
        Solution horizontalSolution = horizontalProblemSolver.solveInHorizontalDirection();

        ProductionFactory verticalProductionFactory = new VerticalProductionFactory(mesh, horizontalSolution);
        LeafInitializer verticalLeafInitializer = new VerticalLeafInitializer(mesh, horizontalSolution);
        SingleDirectionSolver verticalProblemSolver = new SingleDirectionSolver(verticalProductionFactory, verticalLeafInitializer, mesh);

        return verticalProblemSolver.solveInHorizontalDirection();
    }

}