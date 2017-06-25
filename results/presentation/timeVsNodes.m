function timeVsNodes( minTimes )

results = minTimes(minTimes.ProblemSize == 6144^2, :);

results.min_TotalTime = 100 .* (ones(length(results.min_TotalTime), 1) - abs(results.min_TotalTime ./ max(results.min_TotalTime)));

figure
hold on
scatter(results.NodeCount, results.min_TotalTime)
title('Minimum computations time for specific number of nodes used');
xlabel('Nodes used [-]');
ylabel('Performance gain [%]');

p = polyfit(results.NodeCount, results.min_TotalTime, 1);
yfit = polyval(p, results.NodeCount);
error = abs(results.min_TotalTime - yfit);

errorbar(results.NodeCount, yfit, error);

SSresid = sum(error.^2);
SStotal = (length(results.min_TotalTime)-1) * var(results.min_TotalTime);
rsq = 1 - SSresid/SStotal;

legend('Data points', sprintf('Performance gain y = %0.fx [%%] (%d%% variance prediction)', p(1), round(rsq * 100)));
grid on
grid minor

end

