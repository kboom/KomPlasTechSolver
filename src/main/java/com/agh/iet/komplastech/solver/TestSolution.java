package com.agh.iet.komplastech.solver;

class TestSolution {
    TestSolution() {
    }

    // test the tree
    boolean test(Vertex v, double Beg, double End, int NRHS) {
        boolean result;
        boolean ret = true;
        if (v.m_left == null) {
            for (int i = 1; i <= NRHS; i++) {
                System.out.println("rhs[*][" + i + "]=" + v.m_x[1][i] + "," + v.m_x[2][i] + "," + v.m_x[3][i]);
            }
        }
        if (v.m_middle == null) {
            // browse
            if (v.m_left != null) {
                result = test(v.m_left, Beg, Beg + (End - Beg) * 0.5, NRHS);
                if (result == false)
                    ret = false;
            }
            if (v.m_right != null) {
                result = test(v.m_right, Beg + (End - Beg) * 0.5, End, NRHS);
                if (result == false)
                    ret = false;
            }
        } else {
            // browse
            if (v.m_left != null) {
                result = test(v.m_left, Beg, Beg + (End - Beg) / 3.0, NRHS);
                if (result == false)
                    ret = false;
            }
            if (v.m_middle != null) {
                result = test(v.m_middle, Beg + (End - Beg) / 3.0, Beg + 2.0 * (End - Beg) / 3.0, NRHS);
                if (result == false)
                    ret = false;
            }
            if (v.m_right != null) {
                result = test(v.m_right, Beg + 2.0 * (End - Beg) / 3.0, End, NRHS);
                if (result == false)
                    ret = false;
            }
        }
        return ret;
    }
}
