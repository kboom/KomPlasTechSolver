function [fitresult, gof] = nodeBenefit(minTimes)

nodeCounts = unique(minTimes.NodeCount);
problemSizes = unique(minTimes.ProblemSize);

% Plot fit with data.
figure(1);
grid on
title('Time vs Nodes')
xlabel('Nodes [-]')
ylabel('Time [ms]')
hold all
        
pointsStyle = {'bx','kx','rx','gx'};
plotStyle = {'b--','k--','r--','g--'};
outliersStyle = {'bo','ko','ro','go'};

approximations = []

for i = 1 : size(problemSizes)
   problemSize = problemSizes(i);
   
   nodes = zeros(0);
   results = zeros(0);
   for j = 1 : size(nodeCounts)
      nodeCount = nodeCounts(j);
      match = minTimes(minTimes.ProblemSize == problemSize & minTimes.NodeCount == nodeCount,:);
      if(height(match) > 0)
          nodes(end + 1) = nodeCount;
          results(end + 1) = match.min_TotalTime;
      end
   end
   
      [xData, yData] = prepareCurveData(nodes, results);

    % Set up fittype and options.
    ft = fittype( 'power1' );
    opts = fitoptions( 'Method', 'NonlinearLeastSquares' );
    opts.Display = 'Off';
    
    fit1 = fit( xData, yData, ft, opts );
    
    fdata = feval(fit1,xData);
    I = abs(fdata - yData) > 1.5*std(yData);
    outliers = excludedata(xData,yData,'indices',I);
    
    fit2 = fit(xData,yData,ft,'Exclude',outliers);

    h = plot(fit2,plotStyle{mod(i, numel(pointsStyle))  + 1 },xData,yData,...
        pointsStyle{mod(i, numel(pointsStyle))  + 1 },...
        outliers, outliersStyle{mod(i, numel(pointsStyle))  + 1 });
    
    approximations = [ approximations h(end) ];
    
%     semilogy(xData,yData, pointsStyle{mod(i, numel(pointsStyle))  + 1 });
%     semilogy(outliers, 'rx');
    
%     plot( fitresult, xData, yData )
    
%     text(xData(2),log(yData(2)),sprintf('N = %.fk', problemSize/100000), 'FontSize',14)
    
%      yD = feval(fitresult,xData);
%      semilogy(xData,yD, plotStyle{mod(i, numel(plotStyle)) + 1} )
   
   
end
legend(approximations, arrayfun(@(x) sprintf('N = %.fk', x / 1000), problemSizes, 'un', 0))
hold off

end


