package com.agh.iet.komplastech.solver.terrain.support;

class KDNode {
    int axis;
    double[] x;
    int id;
    boolean checked;
    boolean orientation;

    KDNode parent, left, right;

    KDNode(double[] x0, int axis0) {
        x = new double[2];
        axis = axis0;
        for (int k = 0; k < 2; k++)
            x[k] = x0[k];

        left = right = parent = null;
        checked = false;
        id = 0;
    }

    KDNode findParent(double[] x0) {
        KDNode parent = null;
        KDNode next = this;
        int split;
        while (next != null) {
            split = next.axis;
            parent = next;
            if (x0[split] > next.x[split])
                next = next.right;
            else
                next = next.left;
        }
        return parent;
    }

    KDNode insert(double[] p) {
        KDNode parent = findParent(p);
        if (equal(p, parent.x))
            return null;

        KDNode newNode = new KDNode(p, parent.axis + 1 < 2 ? parent.axis + 1 : 0);
        newNode.parent = parent;

        if (p[parent.axis] > parent.x[parent.axis]) {
            parent.right = newNode;
            newNode.orientation = true;
        } else {
            parent.left = newNode;
            newNode.orientation = false;
        }

        return newNode;
    }

    boolean equal(double[] x1, double[] x2) {
        for (int k = 0; k < x1.length; k++) {
            if (x1[k] != x2[k])
                return false;
        }

        return true;
    }

    double distance2(double[] x1, double[] x2, int dim) {
        double S = 0;
        for (int k = 0; k < dim; k++)
            S += (x1[k] - x2[k]) * (x1[k] - x2[k]);
        return S;
    }

}