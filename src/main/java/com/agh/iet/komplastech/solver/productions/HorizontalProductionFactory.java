package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.productions.construction.P1;
import com.agh.iet.komplastech.solver.productions.construction.P2;
import com.agh.iet.komplastech.solver.productions.construction.P3;
import com.agh.iet.komplastech.solver.productions.solution.ExtractSolutionProduction;
import com.agh.iet.komplastech.solver.productions.solution.backsubstitution.*;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_2;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_2_H;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_3;
import com.agh.iet.komplastech.solver.productions.solution.factorization.Aroot;
import com.agh.iet.komplastech.solver.support.VertexRange;
import com.agh.iet.komplastech.solver.support.VertexRegionMapper;

public class HorizontalProductionFactory implements ProductionFactory {

    @Override
    public Production createBranchRootProduction() {
        return new P1();
    }

    @Override
    public Production createBranchIntermediateProduction() {
        return new P2();
    }

    @Override
    public Production createLeafProduction(VertexRange range) {
        return new P3(range);
    }

    @Override
    public Production createLeafMergingProduction() {
        return new A2_3();
    }

    @Override
    public Production createLeafEliminatingProduction() {
        return new E2_1_5();
    }

    @Override
    public Production createFirstIntermediateMergingProduction() {
        return new A2_2();
    }

    @Override
    public Production createFirstIntermediateEliminatingProduction() {
        return new E2_2_6();
    }

    @Override
    public Production mergeUpProduction() {
        return new A2_2_H();
    }

    @Override
    public Production eliminateUpProduction() {
        return new E2_2_6();
    }

    @Override
    public Production createRootSolvingProduction() {
        return new Aroot();
    }

    @Override
    public Production createRootBackwardsSubstitutingProduction() {
        return new Eroot();
    }

    @Override
    public Production backwardSubstituteIntermediateProduction() {
        return new BS_2_6();
    }

    @Override
    public Production backwardSubstituteUpProduction() {
        return new BS_2_6_H();
    }

    @Override
    public Production backwardSubstituteLeavesProduction() {
        return new BS_1_5();
    }

    @Override
    public Production extractSolutionProduction() {
        return new ExtractSolutionProduction();
    }

}
