package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.support.Vertex;

public interface ProductionFactory {

    Production createRootProduction(Vertex vertex);

    Production createIntermediateProduction(Vertex vertex);

    Production createLeafProductions(Vertex vertex);

    Production mergeLeavesProduction(Vertex vertex);

    Production eliminateLeavesProduction(Vertex vertex);

    Production mergeIntermediateProduction(Vertex vertex);

    Production eliminateIntermediateProduction(Vertex vertex);

    Production rootSolverProduction(Vertex vertex);

    Production backwardSubstituteProduction(Vertex vertex);

    Production backwardSubstituteIntermediateProduction(Vertex vertex);

    Production backwardSubstituteLeavesProduction(Vertex vertex);

}
