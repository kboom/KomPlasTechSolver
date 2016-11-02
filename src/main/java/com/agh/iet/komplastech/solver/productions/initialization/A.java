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
    public A(Vertex Vert, Mesh Mesh) {
        super(Vert, Mesh);
    }

    public Vertex apply(Vertex T) {
        System.out.println("A");
        T.m_a[1][1] = 1.0 / 20.0;
        T.m_a[1][2] = 13.0 / 120;
        T.m_a[1][3] = 1.0 / 120;
        T.m_a[2][1] = 13.0 / 120.0;
        T.m_a[2][2] = 45.0 / 100.0;
        T.m_a[2][3] = 13.0 / 120.0;
        T.m_a[3][1] = 1.0 / 120.0;
        T.m_a[3][2] = 13.0 / 120.0;
        T.m_a[3][3] = 1.0 / 20.0;
        // multiple right-hand sides
        BSpline1 b1 = new BSpline1();
        BSpline2 b2 = new BSpline2();
        BSpline3 b3 = new BSpline3();
        RightHandSide rhs = new RightHandSide();
        //B-spline B_i has support over elements [i-2],[i-1],[i] restricted to [1,nelemy]
        for (int i = 1; i <= T.m_mesh.getDofsY(); i++) {
//            for (int k=1; k<=gauss.GAUSS_POINT_COUNT; k++) {
//                double x = gauss.GAUSS_POINTS[k]*T.m_mesh.m_dx/T.m_mesh.m_nelemx + T.m_beg;
//                T.m_b[1][i] += gauss.GAUSS_POINT_WEIGHTS[k] * b3.getValue(gauss.GAUSS_POINTS[k])*x;
//                T.m_b[2][i] += gauss.GAUSS_POINT_WEIGHTS[k] * b2.getValue(gauss.GAUSS_POINTS[k])*x;
//                T.m_b[3][i] += gauss.GAUSS_POINT_WEIGHTS[k] * b1.getValue(gauss.GAUSS_POINTS[k])*x;
//            }
        	//integral of B1 with B_i
        	for (int k = 1; k<= GaussPoints.GAUSS_POINT_COUNT; k++) {
           	  double x = GAUSS_POINTS[k]*T.m_mesh.getElementsX()/T.m_mesh.getElementsX() + T.m_beg; //+ beginning of actual element;
          	  for (int l = 1; l<= GaussPoints.GAUSS_POINT_COUNT; l++) {
          	  	if(i>2) {
           	      double y = (GAUSS_POINTS[l]+(i-3))*T.m_mesh.getResolutionY()/T.m_mesh.getElementsY();
                  T.m_b[1][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b3.getValue(GAUSS_POINTS[k]) * b1.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
          	  	}
          	  	if(i>1 && (i-2)<T.m_mesh.getElementsY()) {
                  double y = (GAUSS_POINTS[l]+(i-2))*T.m_mesh.getResolutionY()/T.m_mesh.getElementsY();
                  T.m_b[1][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b3.getValue(GAUSS_POINTS[k]) * b2.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
          	  	}
                if((i-1)<T.m_mesh.getElementsY()) {
                  double y = (GAUSS_POINTS[l]+(i-1))*T.m_mesh.getResolutionY()/T.m_mesh.getElementsY();
                  T.m_b[1][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b3.getValue(GAUSS_POINTS[k]) * b3.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
                }
          	  }
        	}
        	//integral of B2 with B_i
        	for (int k = 1; k<= GaussPoints.GAUSS_POINT_COUNT; k++) {
           	  double x = GAUSS_POINTS[k]*T.m_mesh.getResolutionX()/T.m_mesh.getElementsX() +  T.m_beg; //+ beginning of actual element;
          	  for (int l = 1; l<= GaussPoints.GAUSS_POINT_COUNT; l++) {
          	  	if(i>2) {
	           	  double y = (GAUSS_POINTS[l]+(i-3))*T.m_mesh.getResolutionY()/T.m_mesh.getElementsY();
	              T.m_b[2][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b2.getValue(GAUSS_POINTS[k]) * b1.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
          	  	}
          	  	if(i>1 && (i-2)<T.m_mesh.getElementsY()) {
                  double y = (GAUSS_POINTS[l]+(i-2))*T.m_mesh.getResolutionY()/T.m_mesh.getElementsY();
                  T.m_b[2][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b2.getValue(GAUSS_POINTS[k]) * b2.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
          	  	}
                if((i-1)<T.m_mesh.getElementsY()) {
                  double y = (GAUSS_POINTS[l]+(i-1))*T.m_mesh.getResolutionY()/T.m_mesh.getElementsY();
                  T.m_b[2][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b2.getValue(GAUSS_POINTS[k]) * b3.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
                }
          	  }
        	}
        	//integral of B3 with B_i
        	for (int k = 1; k<= GaussPoints.GAUSS_POINT_COUNT; k++) {
           	  double x = GAUSS_POINTS[k]*T.m_mesh.getResolutionX()/T.m_mesh.getElementsX() +  T.m_beg; //+ beginning of actual element;
          	  for (int l = 1; l<= GaussPoints.GAUSS_POINT_COUNT; l++) {

          	  	if(i>2) {
             	  double y = (GAUSS_POINTS[l]+(i-3))*T.m_mesh.getResolutionY()/T.m_mesh.getElementsY();
                  T.m_b[3][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b1.getValue(GAUSS_POINTS[k]) * b1.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
          	  	}
          	  	if(i>1 && (i-2)<T.m_mesh.getElementsY()) {
                  double y = (GAUSS_POINTS[l]+(i-2))*T.m_mesh.getResolutionY()/T.m_mesh.getElementsY();
                  T.m_b[3][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b1.getValue(GAUSS_POINTS[k]) * b2.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
          	  	}
                if((i-1)<T.m_mesh.getElementsY()) {
                  double y = (GAUSS_POINTS[l]+(i-1))*T.m_mesh.getResolutionY()/T.m_mesh.getElementsY();
                  T.m_b[3][i] += GAUSS_POINT_WEIGHTS[k] * GAUSS_POINT_WEIGHTS[l] * b1.getValue(GAUSS_POINTS[k]) * b3.getValue(GAUSS_POINTS[l])*rhs.getValue(x,y);
                }
          	  }
        	}
        }
        return T;
    }
}
