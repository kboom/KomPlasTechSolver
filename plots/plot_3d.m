function plot_3d(results_path)
    csv = csvread(results_path, 1, 0);
    plot3(csv(1,:), csv(2,:), csv(3,:));
    title('Visualisation of problem solution')
    xlabel('x coordinate');
    ylabel('y coordinate');
    zlabel('function value');
end

