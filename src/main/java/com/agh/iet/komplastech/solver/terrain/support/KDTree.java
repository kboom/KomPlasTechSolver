package com.agh.iet.komplastech.solver.terrain.support;

public final class KDTree {

    private KDNode root;

    private double d_min;
    private KDNode nearestNeighbour;

    private int kdId;

    private KDNode checkedNodes[];
    private int checkedNodesCount;

    private double x_min[], x_max[];
    private boolean max_boundary[], min_boundary[];
    private int n_boundary;

    public KDTree(int i) {
        root = null;
        kdId = 1;
        checkedNodes = new KDNode[i];
        max_boundary = new boolean[2];
        min_boundary = new boolean[2];
        x_min = new double[2];
        x_max = new double[2];
    }

    public void add(double[] x) {
        if (root == null) {
            root = new KDNode(x, 0);
            root.id = kdId++;
        } else {
            KDNode pNode;
            if ((pNode = root.insert(x)) != null) {
                pNode.id = kdId++;
            }
        }
    }

    public double[] findNearest(double[] x) {
        KDNode nearestNode = findNearestNode(x);
        if(nearestNode != null) {
            return nearestNode.x;
        } else {
            return new double[x.length];
        }
    }

    private KDNode findNearestNode(double[] x) {
        if (root == null)
            return null;

        checkedNodesCount = 0;
        KDNode parent = root.findParent(x);
        nearestNeighbour = parent;
        d_min = root.distance2(x, parent.x, 2);

        if (parent.equal(x, parent.x))
            return nearestNeighbour;

        searchParent(parent, x);
        uncheck();

        return nearestNeighbour;
    }

    private void checkSubtree(KDNode node, double[] x) {
        if ((node == null) || node.checked)
            return;

        checkedNodes[checkedNodesCount++] = node;
        node.checked = true;
        setBoundingCube(node, x);

        int dim = node.axis;
        double d = node.x[dim] - x[dim];

        if (d * d > d_min) {
            if (node.x[dim] > x[dim])
                checkSubtree(node.left, x);
            else
                checkSubtree(node.right, x);
        } else {
            checkSubtree(node.left, x);
            checkSubtree(node.right, x);
        }
    }

    private void setBoundingCube(KDNode node, double[] x) {
        if (node == null)
            return;
        int d = 0;
        double dx;
        for (int k = 0; k < 2; k++) {
            dx = node.x[k] - x[k];
            if (dx > 0) {
                dx *= dx;
                if (!max_boundary[k]) {
                    if (dx > x_max[k])
                        x_max[k] = dx;
                    if (x_max[k] > d_min) {
                        max_boundary[k] = true;
                        n_boundary++;
                    }
                }
            } else {
                dx *= dx;
                if (!min_boundary[k]) {
                    if (dx > x_min[k])
                        x_min[k] = dx;
                    if (x_min[k] > d_min) {
                        min_boundary[k] = true;
                        n_boundary++;
                    }
                }
            }
            d += dx;
            if (d > d_min)
                return;

        }

        if (d < d_min) {
            d_min = d;
            nearestNeighbour = node;
        }
    }

    private KDNode searchParent(KDNode parent, double[] x) {
        for (int k = 0; k < 2; k++) {
            x_min[k] = x_max[k] = 0;
            max_boundary[k] = min_boundary[k] = false; //
        }
        n_boundary = 0;

        KDNode search_root = parent;
        while (parent != null && (n_boundary != 2 * 2)) {
            checkSubtree(parent, x);
            search_root = parent;
            parent = parent.parent;
        }

        return search_root;
    }

    private void uncheck() {
        for (int n = 0; n < checkedNodesCount; n++)
            checkedNodes[n].checked = false;
    }

}
