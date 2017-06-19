package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.support.VertexRange;

public interface ProductionFactory {

    Production createBranchRootProduction();

    Production createBranchIntermediateProduction();

    Production createLeafProduction(VertexRange range);

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

    Production extractSolutionProduction();

}
