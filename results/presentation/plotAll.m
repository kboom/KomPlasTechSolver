function plotAll( minTimes )

nodeCounts = unique(minTimes.NodeCount);
problemSizes = unique(minTimes.ProblemSize);

figure
hold on
for i = 1 : length(nodeCounts);
    nodeCount = nodeCounts(i);
    results = minTimes(minTimes.NodeCount == nodeCount, :).min_TotalTime;
    scatter(problemSizes, results);
    set(gca,'xscale','log','yscale','log')
end
hold off

end

