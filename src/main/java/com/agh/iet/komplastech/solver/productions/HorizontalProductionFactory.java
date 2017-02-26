package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.problem.Problem;
import com.agh.iet.komplastech.solver.productions.construction.P1;
import com.agh.iet.komplastech.solver.productions.construction.P2;
import com.agh.iet.komplastech.solver.productions.construction.P3;
import com.agh.iet.komplastech.solver.productions.initialization.A;
import com.agh.iet.komplastech.solver.productions.solution.backsubstitution.*;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_2;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_2_H;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_3;
import com.agh.iet.komplastech.solver.productions.solution.factorization.Aroot;
import com.agh.iet.komplastech.solver.storage.ObjectStore;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

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
    public Production branchRootProduction() {
        return new P1(objectStore, mesh);
    }

    @Override
    public Production createIntermediateProduction() {
        return new P2(mesh);
    }

    @Override
    public Production createLeafProduction() {
        return new P3(mesh);
    }

    @Override
    public Production mergeLeavesProduction(Vertex vertex) {
        return new A2_3(mesh);
    }

    @Override
    public Production createLeafInitializingProduction() {
        return new A(mesh, problem);
    }






    @Override
    public Production eliminateLeavesProduction(Vertex vertex) {
        return new E2_1_5(vertex, mesh);
    }

    @Override
    public Production mergeIntermediateProduction(Vertex vertex) {
        return new A2_2(mesh);
    }

    @Override
    public Production eliminateIntermediateProduction(Vertex vertex) {
        return new E2_2_6(vertex, mesh);
    }

    @Override
    public Production rootSolverProduction(Vertex vertex) {
        return new Aroot(mesh);
    }

    @Override
    public Production backwardSubstituteProduction(Vertex vertex) {
        return new Eroot(vertex, mesh);
    }

    @Override
    public Production backwardSubstituteIntermediateProduction(Vertex vertex) {
        return new BS_2_6(vertex, mesh);
    }

    @Override
    public Production backwardSubstituteUpProduction(Vertex vertex) {
        return new BS_2_6_H(vertex, mesh);
    }

    @Override
    public Production backwardSubstituteLeavesProduction() {
        return new BS_1_5(vertex, mesh);
    }

    @Override
    public Production mergeUpProduction(Vertex vertex) {
        return new A2_2_H(mesh);
    }

}
