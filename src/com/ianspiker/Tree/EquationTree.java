package com.ianspiker.Tree;

import java.util.Arrays;

public class EquationTree extends Node implements Checkable {

    private Node head;

    public EquationTree(String[] equation) {

        Node currentItem = null;

        int i = 0;
        while (i < equation.length) {

            if (isDouble(equation[i])) {

                try {

                    currentItem = new Number(Double.parseDouble(equation[i]));
                    i++;
                } catch (NumberFormatException e) {

                    System.out.println("An unexpected error has occured");
                    break;
                } 
            } else if (isOperator(equation[i])) {

                try {

                    currentItem = new Operator(equation[i]);
                    i++;
                } catch (Exception e) {

                    System.out.println("An unexpected error has occured");
                    break;
                }
            } else if (equation[i].equals("(")) {

                int openingParensIndex = i;
                int closingParensIndex = i;
                int nestLevel = 0;

                for (int j = i + 1; j < equation.length; j++) {

                    if (equation[j].equals("(")) {
                        nestLevel++;
                    }

                    if (equation[j].equals(")")) {

                        if (nestLevel > 0) {

                            nestLevel--;
                        } else {

                            closingParensIndex = j;
                            break;
                        }
                    }
                }

                if (openingParensIndex == closingParensIndex) {
                    
                    System.out.println("An unexpected parens error has occured");
                    break;
                } else {

                    String[] subArray = Arrays.copyOfRange(
                        equation, openingParensIndex + 1, closingParensIndex);

                    currentItem = new EquationTree(subArray);

                    i = closingParensIndex + 1;
                }
            }

            if (head == null) {

                head = currentItem;
            } else {

                head = head.branch(currentItem);
            }
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

    @Override
    public String toString() {
        return "EquationTree (" + head + ")";
    }
}
