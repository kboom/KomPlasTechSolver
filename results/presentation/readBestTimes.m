function minTimes = readBestTimes( data )

% https://www.mathworks.com/help/matlab/matlab_prog/calculations-on-tables.html
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

data = data(:, {'NodeCount', 'ProblemSize', 'RegionHeight', 'TotalTime'});

times = varfun(@min,data,'InputVariables','TotalTime','GroupingVariables',{'NodeCount','ProblemSize', 'RegionHeight'});

meshElementCountTable = varfun(@(x) (x .* x), times, 'InputVariables','ProblemSize');

times.ProblemSize = meshElementCountTable.Fun_ProblemSize;

minTimes = times;

end

