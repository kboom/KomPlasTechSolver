package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.splines.Bspline1;
import com.agh.iet.komplastech.solver.splines.Bspline2;
import com.agh.iet.komplastech.solver.splines.Bspline3;

public class Solution {
	Solution(MeshData Mesh, double[][] Rhs){
		m_mesh = Mesh;
		m_rhs = Rhs;
	}
	public double get_value(double x, double y){
//	    System.out.println("Global: " + x + ", " + y);
		int ielemx = (int)(x / (m_mesh.m_dx/m_mesh.m_nelemx)) + 1;
		int ielemy = (int)(y / (m_mesh.m_dy/m_mesh.m_nelemy)) + 1;
		double localx = x-(m_mesh.m_dx/m_mesh.m_nelemx)*(ielemx - 1);
		double localy = y-(m_mesh.m_dy/m_mesh.m_nelemy)*(ielemy - 1);
//		System.out.println("Local: " + localx + ", " + localy);
		//we have ielemx-2,ielemx-1,ilemex B-splines along x 
		//and ielemy-2,ielemy-1,ilemey B-splines along y over the element where the point is located
		Bspline1 b1 = new Bspline1();
		Bspline2 b2 = new Bspline2();
		Bspline3 b3 = new Bspline3();
		double solution = 0.0;
		solution += b1.get_value(localx)*b1.get_value(localy)*m_rhs[ielemx][ielemy];
		solution += b1.get_value(localx)*b2.get_value(localy)*m_rhs[ielemx][ielemy+1];
		solution += b1.get_value(localx)*b3.get_value(localy)*m_rhs[ielemx][ielemy+2];
		solution += b2.get_value(localx)*b1.get_value(localy)*m_rhs[ielemx+1][ielemy];
		solution += b2.get_value(localx)*b2.get_value(localy)*m_rhs[ielemx+1][ielemy+1];
		solution += b2.get_value(localx)*b3.get_value(localy)*m_rhs[ielemx+1][ielemy+2];
		solution += b3.get_value(localx)*b1.get_value(localy)*m_rhs[ielemx+2][ielemy];
		solution += b3.get_value(localx)*b2.get_value(localy)*m_rhs[ielemx+2][ielemy+1];
		solution += b3.get_value(localx)*b3.get_value(localy)*m_rhs[ielemx+2][ielemy+2];
		return solution;
	}
	MeshData m_mesh;
	double[][] m_rhs;
}
