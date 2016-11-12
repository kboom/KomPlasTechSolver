function vismesh(results_path)
    csv = csvread(results_path, 1, 0);
    x = csv(:,1);
    y = csv(:,2);
    z = csv(:,3);
    mesh(x, y, z);
    title('Visualisation of problem solution')
    xlabel('x coordinate');
    ylabel('y coordinate');
    zlabel('function value');
end