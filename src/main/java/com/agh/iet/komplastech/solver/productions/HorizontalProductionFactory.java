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
import com.agh.iet.komplastech.solver.support.ComputeConfig;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.VertexRange;
import com.agh.iet.komplastech.solver.support.VertexRegionMapper;

public class HorizontalProductionFactory implements ProductionFactory {

    private final Problem problem;
    private final Mesh mesh;
    private final VertexRegionMapper vertexRegionMapper;

    public HorizontalProductionFactory(
            Mesh mesh,
            Problem problem,
            VertexRegionMapper vertexRegionMapper) {
        this.mesh = mesh;
        this.problem = problem;
        this.vertexRegionMapper = vertexRegionMapper;
    }

    @Override
    public Production createBranchRootProduction() {
        return new P1(mesh, vertexRegionMapper);
    }

    @Override
    public Production createBranchIntermediateProduction() {
        return new P2(mesh, vertexRegionMapper);
    }

    @Override
    public Production createLeafProduction(VertexRange range) {
        return new P3(mesh, range, vertexRegionMapper);
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
