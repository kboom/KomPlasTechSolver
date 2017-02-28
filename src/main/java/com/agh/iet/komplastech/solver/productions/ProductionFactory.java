package com.agh.iet.komplastech.solver.productions;

public interface ProductionFactory {

    Production createBranchRootProduction();

    Production createBranchIntermediateProduction();

    Production createLeafProduction();

    Production createLeafMergingProduction();

    Production createLeafEliminatingProduction();

    Production createFirstIntermediateMergingProduction();

    Production mergeUpProduction();

    Production eliminateUpProduction();

    Production createFirstIntermediateEliminatingProduction();

    Production createRootSolvingProduction();

    Production createRootBackwardsSubstitutingProduction();

    Production backwardSubstituteIntermediateProduction();

    Production backwardSubstituteUpProduction();

    Production backwardSubstituteLeavesProduction();

}
