package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.*;
import com.agh.iet.komplastech.solver.splines.Bspline1;
import com.agh.iet.komplastech.solver.splines.Bspline2;
import com.agh.iet.komplastech.solver.splines.Bspline3;

import java.util.concurrent.CyclicBarrier;

public class A extends Production {
    public A(Vertex Vert, MeshData Mesh) {
        super(Vert, Mesh);
    }

    Vertex apply(Vertex T) {
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
        Bspline1 b1 = new Bspline1();
        Bspline2 b2 = new Bspline2();
        Bspline3 b3 = new Bspline3();
        RightHandSide rhs = new RightHandSide();       
        GaussPoints gauss = new GaussPoints();
        //B-spline B_i has support over elements [i-2],[i-1],[i] restricted to [1,nelemy]
        for (int i = 1; i <= T.m_mesh.m_dofsy; i++) {
//            for (int k=1; k<=gauss.m_nr_points; k++) {
//                double x = gauss.m_points[k]*T.m_mesh.m_dx/T.m_mesh.m_nelemx + T.m_beg;
//                T.m_b[1][i] += gauss.m_weights[k] * b3.get_value(gauss.m_points[k])*x;
//                T.m_b[2][i] += gauss.m_weights[k] * b2.get_value(gauss.m_points[k])*x;
//                T.m_b[3][i] += gauss.m_weights[k] * b1.get_value(gauss.m_points[k])*x;
//            }
        	//integral of B1 with B_i
        	for (int k=1; k<=gauss.m_nr_points; k++) {
           	  double x = gauss.m_points[k]*T.m_mesh.m_dx/T.m_mesh.m_nelemx + T.m_beg; //+ beginning of actual element;
          	  for (int l=1; l<=gauss.m_nr_points; l++) {
          	  	if(i>2) {
           	      double y = (gauss.m_points[l]+(i-3))*T.m_mesh.m_dy/T.m_mesh.m_nelemy;
                  T.m_b[1][i] += gauss.m_weights[k] * gauss.m_weights[l] * b3.get_value(gauss.m_points[k]) * b1.get_value(gauss.m_points[l])*rhs.get_value(x,y);
          	  	}
          	  	if(i>1 && (i-2)<T.m_mesh.m_nelemy) {
                  double y = (gauss.m_points[l]+(i-2))*T.m_mesh.m_dy/T.m_mesh.m_nelemy;
                  T.m_b[1][i] += gauss.m_weights[k] * gauss.m_weights[l] * b3.get_value(gauss.m_points[k]) * b2.get_value(gauss.m_points[l])*rhs.get_value(x,y);
          	  	}
                if((i-1)<T.m_mesh.m_nelemy) {
                  double y = (gauss.m_points[l]+(i-1))*T.m_mesh.m_dy/T.m_mesh.m_nelemy;
                  T.m_b[1][i] += gauss.m_weights[k] * gauss.m_weights[l] * b3.get_value(gauss.m_points[k]) * b3.get_value(gauss.m_points[l])*rhs.get_value(x,y);
                }
          	  }
        	}
        	//integral of B2 with B_i
        	for (int k=1; k<=gauss.m_nr_points; k++) {
           	  double x = gauss.m_points[k]*T.m_mesh.m_dx/T.m_mesh.m_nelemx +  T.m_beg; //+ beginning of actual element;
          	  for (int l=1; l<=gauss.m_nr_points; l++) {
          	  	if(i>2) {
	           	  double y = (gauss.m_points[l]+(i-3))*T.m_mesh.m_dy/T.m_mesh.m_nelemy;
	              T.m_b[2][i] += gauss.m_weights[k] * gauss.m_weights[l] * b2.get_value(gauss.m_points[k]) * b1.get_value(gauss.m_points[l])*rhs.get_value(x,y);
          	  	}                
          	  	if(i>1 && (i-2)<T.m_mesh.m_nelemy) {
                  double y = (gauss.m_points[l]+(i-2))*T.m_mesh.m_dy/T.m_mesh.m_nelemy;
                  T.m_b[2][i] += gauss.m_weights[k] * gauss.m_weights[l] * b2.get_value(gauss.m_points[k]) * b2.get_value(gauss.m_points[l])*rhs.get_value(x,y);
          	  	}
                if((i-1)<T.m_mesh.m_nelemy) {
                  double y = (gauss.m_points[l]+(i-1))*T.m_mesh.m_dy/T.m_mesh.m_nelemy;
                  T.m_b[2][i] += gauss.m_weights[k] * gauss.m_weights[l] * b2.get_value(gauss.m_points[k]) * b3.get_value(gauss.m_points[l])*rhs.get_value(x,y);
                }
          	  }
        	}            
        	//integral of B3 with B_i
        	for (int k=1; k<=gauss.m_nr_points; k++) {
           	  double x = gauss.m_points[k]*T.m_mesh.m_dx/T.m_mesh.m_nelemx +  T.m_beg; //+ beginning of actual element;
          	  for (int l=1; l<=gauss.m_nr_points; l++) {
          	  	
          	  	if(i>2) {
             	  double y = (gauss.m_points[l]+(i-3))*T.m_mesh.m_dy/T.m_mesh.m_nelemy;
                  T.m_b[3][i] += gauss.m_weights[k] * gauss.m_weights[l] * b1.get_value(gauss.m_points[k]) * b1.get_value(gauss.m_points[l])*rhs.get_value(x,y);
          	  	}
          	  	if(i>1 && (i-2)<T.m_mesh.m_nelemy) {
                  double y = (gauss.m_points[l]+(i-2))*T.m_mesh.m_dy/T.m_mesh.m_nelemy;
                  T.m_b[3][i] += gauss.m_weights[k] * gauss.m_weights[l] * b1.get_value(gauss.m_points[k]) * b2.get_value(gauss.m_points[l])*rhs.get_value(x,y);
          	  	}
                if((i-1)<T.m_mesh.m_nelemy) {
                  double y = (gauss.m_points[l]+(i-1))*T.m_mesh.m_dy/T.m_mesh.m_nelemy;
                  T.m_b[3][i] += gauss.m_weights[k] * gauss.m_weights[l] * b1.get_value(gauss.m_points[k]) * b3.get_value(gauss.m_points[l])*rhs.get_value(x,y);
                }
          	  }
        	}            
        }
        return T;
    }
}
