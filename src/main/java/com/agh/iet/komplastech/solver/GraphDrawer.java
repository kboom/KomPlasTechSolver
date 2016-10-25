package com.agh.iet.komplastech.solver;

class GraphDrawer {
    GraphDrawer() {
    }

    // draw the graph
    void draw(Vertex v) {
        // go left
        while (v.m_left != null) {
            v = v.m_left;
        }
        // plot
        while (v.m_right != null) {
            System.out.print(v.m_label + "--");
            v = v.m_right;
        }
        System.out.println(v.m_label);
    }
}
