package com.agh.iet.komplastech.solver.productions;

public interface ProductionFactory {

    Production branchRootProduction();

    Production createIntermediateProduction();

    Production createLeafProduction();

    Production createLeafMergingProduction();

    Production createLeafEliminatingProduction();

    Production createFirstIntermediateMergingProduction();

    Production mergeUpProduction();

    Production eliminateUpProduction();

    Production createFirstIntermediateEliminatingProduction();

    Production createRootSolvingProduction();

    Production backwardSubstituteProduction();

    Production backwardSubstituteIntermediateProduction();

    Production backwardSubstituteUpProduction();

    Production backwardSubstituteLeavesProduction();

    Production createLeafInitializingProduction();
}
