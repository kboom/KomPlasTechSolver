package com.agh.iet.komplastech.solver;

import com.agh.iet.komplastech.solver.splines.Bspline1;
import com.agh.iet.komplastech.solver.splines.Bspline2;
import com.agh.iet.komplastech.solver.splines.Bspline3;

public class Solution {
	Solution(Mesh Mesh, double[][] Rhs){
		m_mesh = Mesh;
		m_rhs = Rhs;
	}
	public double get_value(double x, double y){
//	    System.out.println("Global: " + x + ", " + y);
		int ielemx = (int)(x / (m_mesh.getResolutionX()/m_mesh.getElementsX())) + 1;
		int ielemy = (int)(y / (m_mesh.getResolutionY()/m_mesh.getElementsY())) + 1;
		double localx = x-(m_mesh.getResolutionX()/m_mesh.getElementsX())*(ielemx - 1);
		double localy = y-(m_mesh.getResolutionY()/m_mesh.getElementsY())*(ielemy - 1);
//		System.out.println("Local: " + localx + ", " + localy);
		//we have ielemx-2,ielemx-1,ilemex B-splines along x 
		//and ielemy-2,ielemy-1,ilemey B-splines along y over the element where the point is located
		Bspline1 b1 = new Bspline1();
		Bspline2 b2 = new Bspline2();
		Bspline3 b3 = new Bspline3();
		double solution = 0.0;
		solution += b1.getValue(localx)*b1.getValue(localy)*m_rhs[ielemx][ielemy];
		solution += b1.getValue(localx)*b2.getValue(localy)*m_rhs[ielemx][ielemy+1];
		solution += b1.getValue(localx)*b3.getValue(localy)*m_rhs[ielemx][ielemy+2];
		solution += b2.getValue(localx)*b1.getValue(localy)*m_rhs[ielemx+1][ielemy];
		solution += b2.getValue(localx)*b2.getValue(localy)*m_rhs[ielemx+1][ielemy+1];
		solution += b2.getValue(localx)*b3.getValue(localy)*m_rhs[ielemx+1][ielemy+2];
		solution += b3.getValue(localx)*b1.getValue(localy)*m_rhs[ielemx+2][ielemy];
		solution += b3.getValue(localx)*b2.getValue(localy)*m_rhs[ielemx+2][ielemy+1];
		solution += b3.getValue(localx)*b3.getValue(localy)*m_rhs[ielemx+2][ielemy+2];
		return solution;
	}

	public SolutionGrid getSolutionGrid() {
		double Dx = (m_mesh.getResolutionX()/m_mesh.getElementsX());
		double Dy = (m_mesh.getResolutionY()/m_mesh.getElementsY());
		double x=-Dx/2;
		double y=-Dy/2;
		SolutionGrid solutionGrid = new SolutionGrid();
		for (int i = 1; i <= m_mesh.getElementsX(); ++ i) {
			x += Dx;
			y = -Dy/2;
			for (int j = 1; j <= m_mesh.getElementsY(); ++ j) {
				y += Dy;
				Point point = new Point(x, y, get_value(x,y));
				solutionGrid.addPoint(point);
			}
		}
		return solutionGrid;
	}


	Mesh m_mesh;
	double[][] m_rhs;

	public Mesh getM_mesh() {
		return m_mesh;
	}

	public double[][] getM_rhs() {
		return m_rhs;
	}
}
