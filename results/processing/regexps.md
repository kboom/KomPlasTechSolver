-- Get problem sizes
^--------- Problem size \((\d+)\), Steps \((\d+)\), Max batch size \((\d+)\), Batch ratio \((\d+)\), Region height \((\d+)\), Member count \((\d+)\)$

-- Get solution times
Solution times: Creation \((\d+)\), Initialization \((\d+)\), Factorization \((\d+)\), Backwards Substitution \((\d+)\), Solution reading \((\d+)\), First stage \((\d+)\), Second Stage \((\d+)\), Total \((\d+)\)$

-- joined

^--------- Problem size \((\d+)\), Steps \((\d+)\), Max batch size \((\d+)\), Batch ratio \((\d+)\), Region height \((\d+)\), Member count \((\d+)\)\n\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3} INFO main com.agh.iet.komplastech.solver.SolverLauncher - Solution times: Creation \((\d+)\), Initialization \((\d+)\), Factorization \((\d+)\), Backwards Substitution \((\d+)\), Solution reading \((\d+)\), First stage \((\d+)\), Second Stage \((\d+)\), Total \((\d+)\)

extract via

$6,$1,$2,$3,$4,$5,$7,$8,$9,$10,$11,$12,$13,$14\n