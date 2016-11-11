package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.productions.construction.P1;
import com.agh.iet.komplastech.solver.productions.construction.P2;
import com.agh.iet.komplastech.solver.productions.construction.P3;
import com.agh.iet.komplastech.solver.productions.initialization.A;
import com.agh.iet.komplastech.solver.productions.initialization.A1;
import com.agh.iet.komplastech.solver.productions.initialization.AN;
import com.agh.iet.komplastech.solver.productions.solution.backsubstitution.*;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_2;
import com.agh.iet.komplastech.solver.productions.solution.factorization.A2_3;
import com.agh.iet.komplastech.solver.productions.solution.factorization.Aroot;
import com.agh.iet.komplastech.solver.support.Mesh;
import com.agh.iet.komplastech.solver.support.Vertex;

public class ProductionFactory {

    private Mesh mesh;

    public ProductionFactory(Mesh mesh) {
        this.mesh = mesh;
    }

    public Production createRootProduction(Vertex rootVertex) {
        return new P1(rootVertex, mesh);
    }

    public Production createIntermediateProduction(Vertex leftChild) {
        return new P2(leftChild, mesh);
    }

    public Production createLeafProductions(Vertex leftChild) {
        return new P3(leftChild, mesh);
    }

    public Production initializeLeftMostProduction(Vertex leftChild) {
        return new A1(leftChild, mesh);
    }

    public Production initializeProduction(Vertex middleChild) {
        return new A(middleChild, mesh);
    }

    public Production initializeRightMostProduction(Vertex rightChild) {
        return new AN(rightChild, mesh);
    }

    public Production mergeLeavesProduction(Vertex vertex) {
        return new A2_3(vertex, mesh);
    }

    public Production eliminateLeavesProduction(Vertex vertex) {
        return new E2_1_5(vertex, mesh);
    }

    public Production mergeIntermediateProduction(Vertex vertex) {
        return new A2_2(vertex, mesh);
    }

    public Production eliminateIntermediateProduction(Vertex vertex) {
        return new E2_2_6(vertex, mesh);
    }

    public Production rootSolverProduction(Vertex vertex) {
        return new Aroot(vertex, mesh);
    }

    public Production backwardSubstituteProduction(Vertex vertex) {
        return new Eroot(vertex, mesh);
    }

    public Production backwardSubstituteIntermediateProduction(Vertex vertex) {
        return new BS_2_6(vertex, mesh);
    }

    public Production backwardSubstituteLeavesProduction(Vertex vertex) {
        return new BS_1_5(vertex, mesh);
    }

}
