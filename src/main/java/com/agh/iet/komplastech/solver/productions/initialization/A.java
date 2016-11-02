package com.agh.iet.komplastech.solver.productions.initialization;

import com.agh.iet.komplastech.solver.constants.GaussPoints;
import com.agh.iet.komplastech.solver.Mesh;
import com.agh.iet.komplastech.solver.RightHandSide;
import com.agh.iet.komplastech.solver.Vertex;
import com.agh.iet.komplastech.solver.productions.Production;
import com.agh.iet.komplastech.solver.splines.BSpline1;
import com.agh.iet.komplastech.solver.splines.BSpline2;
import com.agh.iet.komplastech.solver.splines.BSpline3;

import static com.agh.iet.komplastech.solver.constants.GaussPoints.GAUSS_POINTS;
import static com.agh.iet.komplastech.solver.constants.GaussPoints.GAUSS_POINT_WEIGHTS;

public class A extends Production {

    public A(Vertex node, Mesh mesh) {
        super(node, mesh);
    }

    public Vertex apply(Vertex node) {
        initializeCoefficientsMatrix(node);

        initializeRightHandSides(node);
        return node;
    }

    private void initializeCoefficientsMatrix(Vertex node) {
        node.m_a[1][1] = 1.0 / 20.0;
        node.m_a[1][2] = 13.0 / 120;
        node.m_a[1][3] = 1.0 / 120;
        node.m_a[2][1] = 13.0 / 120.0;
        node.m_a[2][2] = 45.0 / 100.0;
        node.m_a[2][3] = 13.0 / 120.0;
        node.m_a[3][1] = 1.0 / 120.0;
        node.m_a[3][2] = 13.0 / 120.0;
        node.m_a[3][3] = 1.0 / 20.0;
    }

    private void initializeRightHandSides(Vertex node) {
        BSpline1 b1 = new BSpline1();
        BSpline2 b2 = new BSpline2();
        BSpline3 b3 = new BSpline3();

        RightHandSide rhs = new RightHandSide();

        for (int i = 1; i <= node.m_mesh.getDofsY(); i++) {
        	for (int k = 1; k<= GaussPoints.GAUSS_POINT_COUNT; k++) {
           	  double x = GAUSS_POINTS[k]*node.m_mesh.getElementsX()/node.m_mesh.getElementsX() + node.m_beg; //+ beginning of actual element;
          	  for (int l = 1; l<= GaussPoints.GAUSS_POINT_COUNT; l++) {
          	  	if(i>2) {
           	      double y = (GAUSS_POINTS[l]+(i-3))*node.m_mesh.getResolutionY()/node.m_mesh.getElementsY();
                  node.m_b[1][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b3.getValue(GAUSS_POINTS[k]) * b1.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
          	  	}
          	  	if(i>1 && (i-2)<node.m_mesh.getElementsY()) {
                  double y = (GAUSS_POINTS[l]+(i-2))*node.m_mesh.getResolutionY()/node.m_mesh.getElementsY();
                  node.m_b[1][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b3.getValue(GAUSS_POINTS[k]) * b2.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
          	  	}
                if((i-1)<node.m_mesh.getElementsY()) {
                  double y = (GAUSS_POINTS[l]+(i-1))*node.m_mesh.getResolutionY()/node.m_mesh.getElementsY();
                  node.m_b[1][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b3.getValue(GAUSS_POINTS[k]) * b3.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
                }
          	  }
        	}
        	//integral of B2 with B_i
        	for (int k = 1; k<= GaussPoints.GAUSS_POINT_COUNT; k++) {
           	  double x = GAUSS_POINTS[k]*node.m_mesh.getResolutionX()/node.m_mesh.getElementsX() +  node.m_beg; //+ beginning of actual element;
          	  for (int l = 1; l<= GaussPoints.GAUSS_POINT_COUNT; l++) {
          	  	if(i>2) {
	           	  double y = (GAUSS_POINTS[l]+(i-3))*node.m_mesh.getResolutionY()/node.m_mesh.getElementsY();
	              node.m_b[2][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b2.getValue(GAUSS_POINTS[k]) * b1.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
          	  	}
          	  	if(i>1 && (i-2)<node.m_mesh.getElementsY()) {
                  double y = (GAUSS_POINTS[l]+(i-2))*node.m_mesh.getResolutionY()/node.m_mesh.getElementsY();
                  node.m_b[2][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b2.getValue(GAUSS_POINTS[k]) * b2.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
          	  	}
                if((i-1)<node.m_mesh.getElementsY()) {
                  double y = (GAUSS_POINTS[l]+(i-1))*node.m_mesh.getResolutionY()/node.m_mesh.getElementsY();
                  node.m_b[2][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b2.getValue(GAUSS_POINTS[k]) * b3.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
                }
          	  }
        	}
        	//integral of B3 with B_i
        	for (int k = 1; k<= GaussPoints.GAUSS_POINT_COUNT; k++) {
           	  double x = GAUSS_POINTS[k]*node.m_mesh.getResolutionX()/node.m_mesh.getElementsX() +  node.m_beg; //+ beginning of actual element;
          	  for (int l = 1; l<= GaussPoints.GAUSS_POINT_COUNT; l++) {

          	  	if(i>2) {
             	  double y = (GAUSS_POINTS[l]+(i-3))*node.m_mesh.getResolutionY()/node.m_mesh.getElementsY();
                  node.m_b[3][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b1.getValue(GAUSS_POINTS[k]) * b1.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
          	  	}
          	  	if(i>1 && (i-2)<node.m_mesh.getElementsY()) {
                  double y = (GAUSS_POINTS[l]+(i-2))*node.m_mesh.getResolutionY()/node.m_mesh.getElementsY();
                  node.m_b[3][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b1.getValue(GAUSS_POINTS[k]) * b2.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
          	  	}
                if((i-1)<node.m_mesh.getElementsY()) {
                  double y = (GAUSS_POINTS[l]+(i-1))*node.m_mesh.getResolutionY()/node.m_mesh.getElementsY();
                  node.m_b[3][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b1.getValue(GAUSS_POINTS[k]) * b3.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
                }
          	  }
        	}
        }
    }

}
