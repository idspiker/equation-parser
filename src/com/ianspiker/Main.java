package com.ianspiker;

import java.util.Scanner;

import com.ianspiker.Tree.EquationTree;

public class Main {

    public static void main(String[] args) {
        
        System.out.println("Enter an equation: ");
        try (Scanner scan = new Scanner(System.in)) {
            String[] equation = scan.nextLine().trim().split(" ");
            for (int i = 0; i < equation.length; i++) {
                equation[i] = equation[i].trim();
            }

            EquationTree tree = new EquationTree(equation);

            if (!tree.check()) {
                return;
            }

            System.out.println(tree);

            double result = tree.evaluate();

            System.out.println("Result: " + result);
        }
    }
}