function [fitresult, gof] = proveLinear(minTimes)

x = unique(minTimes.ProblemSize);
y = minTimes(minTimes.NodeCount == max(minTimes.NodeCount), :).min_TotalTime;

[xData, yData] = prepareCurveData( x, y );

% Set up fittype and options.
ft = fittype( 'poly1' );

% Fit model to data.
[fitresult, gof] = fit( xData, yData, ft );

% Plot fit with data.
figure( 'Name', 'Execution time vs Node Count' );

% h = plot( fitresult, xData, yData );

h = loglog(xData,yData, 'o');
% yD = feval(fitresult,xData);
% loglog(xData,yD,'--r')
 
% legend( h, 'y vs. x', 'untitled fit 1', 'Location', 'NorthEast' );
% Label axes
xlabel x
ylabel y
grid on

end


