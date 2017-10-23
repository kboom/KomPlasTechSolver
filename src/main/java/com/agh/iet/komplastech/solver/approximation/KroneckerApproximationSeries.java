package com.agh.iet.komplastech.solver.approximation;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Builder
@EqualsAndHashCode
@ToString
public class KroneckerApproximationSeries {

    private final List<KroneckerApproximation> series;

    public List<KroneckerApproximation> getSeries() {
        return Collections.unmodifiableList(series);
    }

    public KroneckerApproximation getTarget() {
        return series.get(series.size() -  1);
    }

}
