package com.ianspiker.Tree;

import java.util.function.BinaryOperator;

class Operator extends Node implements Checkable {

    private static BinaryOperator<Double> add = Double::sum;
    private static BinaryOperator<Double> subtract = (d1, d2) -> d1 - d2;
    private static BinaryOperator<Double> multiply = (d1, d2) -> d1 * d2;
    private static BinaryOperator<Double> divide = (d1, d2) -> d1 / d2;
    private static BinaryOperator<Double> power = 
            (d1, d2) -> Math.pow(d1, d2);

    private BinaryOperator<Double> operation;
    private PEDMASLevel pedmasScore;
    private Evaluable left;
    private Evaluable right;

    private String originString;

    Operator(String operatorString) throws Exception {

        originString = operatorString;

        switch (operatorString) {
            case "+" -> {
                this.operation = add;
                this.pedmasScore = PEDMASLevel.LOW;
            }
            case "-" -> {
                this.operation = subtract;
                this.pedmasScore = PEDMASLevel.LOW;
            }
            case "*" -> {
                this.operation = multiply;
                this.pedmasScore = PEDMASLevel.MEDIUM;
            }
            case "/" -> {
                this.operation = divide;
                this.pedmasScore = PEDMASLevel.MEDIUM;
            }
            case "^" -> {
                this.operation = power;
                this.pedmasScore = PEDMASLevel.HIGH;
            }
            default -> throw new Exception("Invalid operator");
        };
    }

    void setLeft(Evaluable left) {
        this.left = left;
    }

    void setRight(Evaluable right) {
        this.right = right;
    }

    @Override
    public PEDMASLevel getPedmasScore() {

        return pedmasScore;
    }

    @Override
    public double evaluate() {

        return operation.apply(left.evaluate(), right.evaluate());
    }

    @Override
    public Node branch(Node node) {

        if (this.compareTo(node) >= 0 && node instanceof Operator operator) {

            operator.setLeft(this);
            node.setParent(this.getParent());
            setParent(node);

            return node;
        } else if (right == null) {

            right = node;
            node.setParent(this);
        } else if (right instanceof Node rightNode) {

            rightNode.branch(node);
        }

        return this;
    }

    @Override
    public boolean check() {

        boolean safeForEvaluation = true;

        if (left == null || right == null) {

            safeForEvaluation = false;
        } 

        if (left instanceof Checkable l) {

            safeForEvaluation = safeForEvaluation && l.check();
        }

        if (right instanceof Checkable r) {

            safeForEvaluation = safeForEvaluation && r.check();
        }

        return safeForEvaluation;
    }

    @Override
    public String toString() {
        return left + " " + originString + " " + right;
    }
}
