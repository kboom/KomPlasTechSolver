package com.agh.iet.komplastech.solver.productions;

import java.io.Serializable;

public interface Production extends Serializable {

    void apply(ProcessingContext context);

}