# KomPlasTechSolver

### Usage

Binaries can be downloaded from [here](https://github.com/kboom/KomPlasTechSolver/tree/master/dist).
Extract and run proper executable file from the *bin* directory.
Make sure *JRE* (1.8+) is installed on the system and available on the *PATH*.

Command line arguments are:

1. **--log** / **-l** => logging detailed results (off by default)
2. **--plot** / **-p** => plotting results (on by default)
3. **--problem-size** / **-s** => specifying problem size (12 by default)
4. **--max-threads** / **-t** => enforcing maximum number of threads used (12 by default)
5. **--delta** / **-d** => time step (0.001 by default)
6. **--steps** / **-o** => number of time steps to run simulation for (100 by default)

### Contact & More information

[http://home.agh.edu.pl/~paszynsk/](http://home.agh.edu.pl/~paszynsk/)

### Examples

#### Heat transfer simulation

In this example an exemplary problem of heat transfer is being solved. A ball of heat is put into the center of the plane gradually heating the surface. The grid size is 24x24 though it can be any value.

![Heat transfer simulation](heat.gif)


