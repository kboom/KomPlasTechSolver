package com.agh.iet.komplastech.solver.productions.solution.backsubstitution;

import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.support.Vertex;

abstract class PFEProduction implements Production {

    Vertex partial_forward_elimination(Vertex T, int elim, int size, int nrhs) {
        for (int irow = 1; irow <= elim; irow++) {
            double diag = T.m_a.get(irow, irow);
            for (int icol = irow; icol <= size; icol++) {
                T.m_a.divideBy(irow, icol, diag);
            }
            for (int irhs = 1; irhs <= nrhs; irhs++) {
                T.m_b.divideBy(irow, irhs, diag);
            }
            for (int isub = irow + 1; isub <= size; isub++) {
                double mult = T.m_a.get(isub, irow);
                for (int icol = irow; icol <= size; icol++) {
                    T.m_a.subtract(isub, icol, T.m_a.get(irow, icol) * mult);
                }
                for (int irhs = 1; irhs <= nrhs; irhs++) {
                    T.m_b.subtract(isub, irhs, T.m_b.get(irow, irhs) * mult);
                }
            }
        }
        return T;
    }

    Vertex partial_backward_substitution(Vertex T, int elim, int size, int nrhs) {
        for (int irhs = 1; irhs <= nrhs; irhs++) {
            for (int irow = elim; irow >= 1; irow--) {
                T.m_x.set(irow, irhs, T.m_b.get(irow, irhs));
                for (int icol = irow + 1; icol <= size; icol++) {
                    T.m_x.subtract(irow, irhs, T.m_a.get(irow, icol) * T.m_x.get(icol, irhs));
                }
                T.m_x.divideBy(irow, irhs, T.m_a.get(irow, irow));
            }
        }
        return T;
    }

}