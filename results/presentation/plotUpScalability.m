function plotUpScalability( minTimes )

results = minTimes(minTimes.NodeCount == 10, :);

figure
hold on
scatter(results.ProblemSize, results.min_TotalTime)
title('Minimum computations time for problem size');
xlabel('Elements in mesh [-]');
ylabel('Elapsed time [ms]');
set(gca,'xscale','log','yscale','log')

x = logspace(1, 10);
y1 = feval(@(x) (x / 450), x);
% errorbar(resultsBigOnly.ProblemSize, resultsBigOnly.min_TotalTime, [1000, 1000, 4000, 10000, 1000], '.')

plot(x, y1, 'b');
legend('Data points','O(N)=x')

grid on
grid minor

hold off

end

