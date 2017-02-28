package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.productions.construction.P1;
import com.agh.iet.komplastech.solver.productions.construction.P2;
import com.agh.iet.komplastech.solver.productions.construction.P3;
import com.agh.iet.komplastech.solver.productions.solution.backsubstitution.*;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_2;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_2_H;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_3;
import com.agh.iet.komplastech.solver.productions.solution.factorization.Aroot;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Mesh;

public class HorizontalProductionFactory implements ProductionFactory {

    private final Problem problem;
    private final Mesh mesh;
    private final ObjectStore objectStore;

    public HorizontalProductionFactory(
            ObjectStore objectStore,
            Mesh mesh,
            Problem problem) {
        this.objectStore = objectStore;
        this.mesh = mesh;
        this.problem = problem;
    }

    @Override
    public Production createBranchRootProduction() {
        return new P1(objectStore, mesh);
    }

    @Override
    public Production createBranchIntermediateProduction() {
        return new P2(objectStore);
    }

    @Override
    public Production createLeafProduction() {
        return new P3(objectStore);
    }

    @Override
    public Production createLeafMergingProduction() {
        return new A2_3(mesh);
    }

    @Override
    public Production createLeafEliminatingProduction() {
        return new E2_1_5(mesh);
    }

    @Override
    public Production createFirstIntermediateMergingProduction() {
        return new A2_2(mesh);
    }

    @Override
    public Production createFirstIntermediateEliminatingProduction() {
        return new E2_2_6(mesh);
    }

    @Override
    public Production mergeUpProduction() {
        return new A2_2_H(mesh);
    }

    @Override
    public Production eliminateUpProduction() {
        return new E2_2_6(mesh);
    }

    @Override
    public Production createRootSolvingProduction() {
        return new Aroot(mesh);
    }

    @Override
    public Production createRootBackwardsSubstitutingProduction() {
        return new Eroot(mesh);
    }

    @Override
    public Production backwardSubstituteIntermediateProduction() {
        return new BS_2_6(mesh);
    }

    @Override
    public Production backwardSubstituteUpProduction() {
        return new BS_2_6_H(mesh);
    }

    @Override
    public Production backwardSubstituteLeavesProduction() {
        return new BS_1_5(mesh);
    }

}
