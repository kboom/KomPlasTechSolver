% https://www.mathworks.com/help/matlab/matlab_prog/calculations-on-tables.html

data = readtable('4c8g.csv');
data.Properties.VariableNames{'Var1'} = 'NodeCount';
data.Properties.VariableNames{'Var2'} = 'ProblemSize';
data.Properties.VariableNames{'Var3'} = 'BatchRatio';
data.Properties.VariableNames{'Var4'} = 'MaxBatchSize';
data.Properties.VariableNames{'Var5'} = 'Steps';
data.Properties.VariableNames{'Var6'} = 'RegionHeight';
data.Properties.VariableNames{'Var7'} = 'CreationTime';
data.Properties.VariableNames{'Var8'} = 'InitializationTime';
data.Properties.VariableNames{'Var9'} = 'FactorizationTime';
data.Properties.VariableNames{'Var10'} = 'BackwardsSubstitutionTime';
data.Properties.VariableNames{'Var11'} = 'SolutionReading';
data.Properties.VariableNames{'Var12'} = 'FirstStageTime';
data.Properties.VariableNames{'Var13'} = 'SecondStageTime';
data.Properties.VariableNames{'Var14'} = 'TotalTime';

data = data(:, {'NodeCount', 'ProblemSize', 'TotalTime'});

minTimes = varfun(@min,data,'InputVariables','TotalTime','GroupingVariables',{'NodeCount','ProblemSize'});

meshElementCountTable = varfun(@(x) (x .* x), minTimes, 'InputVariables','ProblemSize');

minTimes.ProblemSize = meshElementCountTable.Fun_ProblemSize;

% minTimes

% minTimes = minTimes(minTimes.ProblemSize <= 6144, :);

% figure(1)
% gca = scatter3(minTimes.NodeCount, minTimes.ProblemSize, minTimes.min_TotalTime,...
%      'MarkerEdgeColor','k',...
%      'MarkerFaceColor',[0 .75 .75]);
%  set(gca,'XLim',[-3 3],'YLim',[-3 3],'ZLim',[-3 3])
%  set(gca,'xscale','log','zscale','log','yscale','log')

