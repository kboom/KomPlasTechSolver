package com.agh.iet.komplastech.solver.productions;

import com.agh.iet.komplastech.solver.support.Vertex;

import java.io.Serializable;

public interface Production extends Serializable {

    Vertex apply(ProcessingContext context);

}