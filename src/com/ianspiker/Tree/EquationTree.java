package com.ianspiker.Tree;

import java.util.Arrays;

public class EquationTree extends Node implements Checkable {

    private Node head;

    public EquationTree(String[] equation) {

        Node currentItem = null;

        int i = 0;
        while (i < equation.length) {

            if (isDouble(equation[i])) {

                currentItem = toNumber(equation[i]);
                i++;
            } else if (isOperator(equation[i])) {

                currentItem = toOperator(equation[i]);
                i++;
            } else if (equation[i].equals("(")) {

                WrappedEquationTree wrappedTree = toEquationTree(equation, i);

                currentItem = wrappedTree.tree();
                i = wrappedTree.newIValue();
            }

            if (currentItem == null) {

                System.out.println("An unexpected error has occured");
                break;
            }

            if (head == null) {

                head = currentItem;
            } else {

                head = head.branch(currentItem);
            }

            currentItem = null;
        }
    }

    @Override
    public double evaluate() {

        return head.evaluate();
    }

    @Override
    public boolean check() {

        if (head instanceof Checkable h) {

            return h.check();
        }

        return true;
    }

    @Override
    public PEDMASLevel getPedmasScore() {
        return PEDMASLevel.HIGHEST;
    }

    @Override
    public Node branch(Node node) {

        if (this.compareTo(node) <= 0 || !(node instanceof Operator)) {
            return this;
        }

        Operator operator = (Operator) node;
        operator.setLeft(this);

        if (this.getParent() instanceof Operator p) {
                
            p.setRight(node);
        }

        node.setParent(this.getParent());
        setParent(node);
        return node;
    }

    private static boolean isDouble(String s) {

        try {

            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {

            return false;
        }
    }

    private static boolean isOperator(String s) {

        return switch (s) {
            case "+", "-", "*", "/", "^" -> true;
            default -> false;
        };
    }

    private static Number toNumber(String s) {

        try {

            return new Number(Double.parseDouble(s));
        } catch (NumberFormatException e) {

            System.out.println("An unexpected error has occured");
            return null;
        } 
    }

    private static Operator toOperator(String s) {

        try {
            
            return new Operator(s);
        } catch (Exception e) {

            System.out.println("An unexpected error has occured");
            return null;
        }
    }

    private static WrappedEquationTree toEquationTree(String[] equation, 
            int openingParensIndex) {

        int closingParensIndex = openingParensIndex;
        int nestLevel = 0;

        for (int j = openingParensIndex + 1; j < equation.length; j++) {

            if (equation[j].equals("(")) {
                nestLevel++;
            } else if (equation[j].equals(")")) {

                if (nestLevel <= 0) {

                    closingParensIndex = j;
                    break;
                }

                nestLevel--;
            }
        }

        if (openingParensIndex == closingParensIndex) {
                    
            System.out.println("An unexpected parens error has occured");
            return null;
        } 

        String[] subArray = Arrays.copyOfRange(
               equation, openingParensIndex + 1, closingParensIndex);

        return new WrappedEquationTree(new EquationTree(subArray), 
                closingParensIndex + 1);
    }

    @Override
    public String toString() {
        return "EquationTree (" + head + ")";
    }

    private static record WrappedEquationTree(EquationTree tree, 
            int newIValue) {}
}
