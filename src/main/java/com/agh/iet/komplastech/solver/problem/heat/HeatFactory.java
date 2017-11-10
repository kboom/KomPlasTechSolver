package com.agh.iet.komplastech.solver.problem.heat;

import com.agh.iet.komplastech.solver.Solution;
import com.agh.iet.komplastech.solver.SolutionFactory;
import com.agh.iet.komplastech.solver.problem.NonStationaryProblem;
import com.agh.iet.komplastech.solver.problem.ProblemFactory;
import com.agh.iet.komplastech.solver.support.Mesh;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeatFactory implements ProblemFactory {

    private final Mesh mesh;
    private final int problemSize;
    private final double delta;

    @Override
    public NonStationaryProblem getProblem() {
        return new HeatTransferProblem(delta, mesh, problemSize);
    }

    @Override
    public SolutionFactory getSolutionFactory() {
        return new SolutionFactory() {

            @Override
            public Solution createFinalSolution(Solution solution) {
                return new HeatSolution(mesh, solution.getRhs());
            }

        };
    }

}
