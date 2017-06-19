package com.agh.iet.komplastech.solver.support;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.ORDERED;


public class BatchingIterator<T> implements Iterator<Set<T>> {

    public static <T> Stream<Set<T>> batchedStreamOf(Stream<T> originalStream, int batchSize) {
        return asStream(new BatchingIterator<>(originalStream.iterator(), batchSize));
    }

    private static <T> Stream<T> asStream(Iterator<T> iterator) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, ORDERED),
                false);
    }

    private int batchSize;
    private Set<T> currentBatch;
    private Iterator<T> sourceIterator;

    public BatchingIterator(Iterator<T> sourceIterator, int batchSize) {
        this.batchSize = batchSize;
        this.sourceIterator = sourceIterator;
    }

    @Override
    public boolean hasNext() {
        prepareNextBatch();
        return currentBatch != null && !currentBatch.isEmpty();
    }

    @Override
    public Set<T> next() {
        return currentBatch;
    }

    private void prepareNextBatch() {
        currentBatch = new HashSet<>(batchSize);
        while (sourceIterator.hasNext() && currentBatch.size() < batchSize) {
            currentBatch.add(sourceIterator.next());
        }
    }

}