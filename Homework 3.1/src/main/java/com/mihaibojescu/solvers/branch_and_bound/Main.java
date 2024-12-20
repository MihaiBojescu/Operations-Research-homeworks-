package com.mihaibojescu.solvers.branch_and_bound;

import java.util.Arrays;

import com.mihaibojescu.solvers.branch_and_bound.interfaces.Solver;
import com.mihaibojescu.solvers.branch_and_bound.math.Matrix;
import com.mihaibojescu.solvers.branch_and_bound.math.Problem;
import com.mihaibojescu.solvers.branch_and_bound.util.Result;
import com.mihaibojescu.solvers.branch_and_bound.math.BranchAndBoundSolver;
import com.mihaibojescu.solvers.branch_and_bound.math.TwoPhaseSimplexSolverAdapter;

public class Main {
    public static void main(String[] args) throws Exception {
        Problem problem = new Problem(
                new Matrix(new double[] { 2, 3 }), new Matrix(new double[][] { { 3, 2 }, { 4, 5 }, }),
                new Matrix(new double[] { 13, 11 }));
        Solver twoPhaseSolver = new TwoPhaseSimplexSolverAdapter();
        Solver branchAndBound = new BranchAndBoundSolver(twoPhaseSolver, 0.0001, true);
        Result result = branchAndBound.run(problem);

        if (result.getObjectiveValue() == Solver.INF) {
            System.out.println("The problem is unbounded.");
            return;
        }

        if (result.getObjectiveValue() == -Solver.INF) {
            System.out.println("The problem is infeasible.");
            return;
        }

        System.out.println("Optimal solution found: z = " + result.getObjectiveValue());
        System.out.println("Solution: " + Arrays.toString(result.getSolution()));
    }
}
