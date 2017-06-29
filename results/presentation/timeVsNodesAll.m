function timeVsNodesAll( minTimes )

nodeCounts = unique(minTimes.NodeCount);
problemSizes = unique(minTimes.ProblemSize);

figure
hold on
for i = 1 : length(nodeCounts);
    nodeCount = nodeCounts(i);
    results = minTimes(minTimes.NodeCount == nodeCount, :).min_TotalTime;
    
    
    
    semilogx(problemSizes(1:size(results)), results, 'o');
    set(gca,'xscale','log','yscale','log')
end
hold off

end

