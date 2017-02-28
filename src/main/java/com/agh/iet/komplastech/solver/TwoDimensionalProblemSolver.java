package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.initialization.HorizontalLeafInitializer;
import com.agh.iet.komplastech.solver.initialization.LeafInitializer;
import com.agh.iet.komplastech.solver.initialization.VerticalLeafInitializer;
import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.productions.HorizontalProductionFactory;
import com.agh.iet.komplastech.solver.productions.ProductionFactory;
import com.agh.iet.komplastech.solver.productions.VerticalProductionFactory;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.tracking.TreeIteratorFactory;

class TwoDimensionalProblemSolver implements Solver {

    private final Mesh mesh;

    private final ProductionExecutorFactory launcherFactory;

    private final ObjectStore objectStore;

    TwoDimensionalProblemSolver(ProductionExecutorFactory launcherFactory,
                                Mesh meshData,
                                ObjectStore objectStore) {
        this.launcherFactory = launcherFactory;
        this.mesh = meshData;
        this.objectStore = objectStore;
    }

    @Override
    public Solution solveProblem(Problem rhs) {
        Solution horizontalSolution = solveProblemHorizontally(rhs);
        return solveProblemVertically(horizontalSolution, rhs);
    }

    private Solution solveProblemHorizontally(Problem rhs) {
        HorizontalProductionFactory productionFactory = new HorizontalProductionFactory(objectStore, mesh, rhs);
        HorizontalLeafInitializer horizontalLeafInitializer = new HorizontalLeafInitializer(mesh, rhs, launcherFactory);
        TreeIteratorFactory treeIteratorFactory = new TreeIteratorFactory();
        DirectionSolver horizontalProblemSolver = new DirectionSolver(
                objectStore,
                productionFactory,
                launcherFactory,
                treeIteratorFactory,
                horizontalLeafInitializer,
                mesh
        );

        return horizontalProblemSolver.solveProblem(rhs);
    }

    private Solution solveProblemVertically(Solution horizontalSolution, Problem rhs) {
        LeafInitializer verticalLeafInitializer = new VerticalLeafInitializer(mesh, horizontalSolution, launcherFactory);
        ProductionFactory verticalProductionFactory = new VerticalProductionFactory(objectStore, mesh, horizontalSolution);
        TreeIteratorFactory treeIteratorFactory = new TreeIteratorFactory();
        DirectionSolver verticalProblemSolver = new DirectionSolver(
                objectStore,
                verticalProductionFactory,
                launcherFactory,
                treeIteratorFactory,
                verticalLeafInitializer,
                mesh
        );

        return verticalProblemSolver.solveProblem(rhs);
    }

}