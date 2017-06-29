function [fitresult, gof] = nodeBenefit(minTimes)

nodeCounts = unique(minTimes.NodeCount);
problemSizes = unique(minTimes(minTimes.ProblemSize >= 1536^2, :).ProblemSize);

% Plot fit with data.
figure( 'Name', 'Time vs Nodes' );
grid on 

pointsStyle = {'bo','ko','ro','go'};
plotStyle = {'b--','k--','r--','g--'};

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
    opts.StartPoint = [418450.448604987 -0.778499327177966];

    % Fit model to data.
    [fitresult, gof] = fit( xData, yData, ft, opts );

    plot(xData,yData, pointsStyle{mod(i, numel(pointsStyle) + 1)});
%     plot( fitresult, xData, yData )

    if(i == 1)
        hold all
        xlabel('Nodes [-]')
        ylabel('Time [ms]')
    end
    
    text(xData(2),yData(2),sprintf('N = %.fk', problemSize/100000), 'FontSize',14)
    
     yD = feval(fitresult,xData);
     plot(xData,yD, plotStyle{mod(i, numel(plotStyle) + 1)} )
   
   
end

hold off

end


