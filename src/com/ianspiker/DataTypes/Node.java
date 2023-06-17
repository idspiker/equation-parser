package com.ianspiker.DataTypes;

import java.util.Comparator;
import java.util.Stack;

public class Node implements Calculatable {
    
    private Calculatable left;
    private Calculatable right;
    private Operator operation;
    
    static private Stack<ParentheticalContext> parensContextStack = new Stack<>();

    public Node(String left, String right, String operation) {
        this(
            new NumberNode(Double.parseDouble(left)), 
            right, 
            Operator.getOperator(operation)
        );
    }

    private Node(Calculatable left, String right, Operator operation) {

        this(left, new NumberNode(Double.parseDouble(right)), operation);
    }

    private Node(Calculatable left, Calculatable right, Operator operation) {

        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    public double calculate() {

        double calculatedLeft = left.calculate();
        double calculatedRight = right.calculate();

        return switch(operation) {
            case ADDITION -> calculatedLeft + calculatedRight;
            case SUBTRACTION -> calculatedLeft - calculatedRight;
            case MULTIPLICATION -> calculatedLeft * calculatedRight;
            case DIVISION -> calculatedLeft / calculatedRight;
            case POWER -> Math.pow(calculatedLeft, calculatedRight); 
        };
    }

    public Node addBranch(String newOp, Calculatable newRight) {

        Operator newOperation = Operator.getOperator(newOp);

        int comparison = new Operator.OperatorComparator()
            .compare(operation, newOperation);

        if (comparison >= 0) {

            return new Node(this, newRight, newOperation);
        }  else {
            if (right instanceof Node n) {
                right = n.addBranch(newOp, newRight);
            } else {
                Node newNode = new Node(right, newRight, newOperation);
    
                right = newNode;
            }
    
            return this;
        }
    }

    public Node addBranch(String newOp, String newRight) {

        return addBranch(newOp, new NumberNode(Double.parseDouble(newRight)));
    }

    public Node addParentheticalContext(String newOp, 
        Node contextHead) {

        ParentheticalContext newContext = new ParentheticalContext(contextHead);

        parensContextStack.push(newContext);

        return addBranch(newOp, newContext);
    }

    public void addParentheticalBranch(String newOp, String newRight) {

        parensContextStack.peek().addBranch(newOp, newRight);
    }

    public void exitCurrentParentheticalContext() {
        
        parensContextStack.pop();
    }

    @Override
    public String toString() {
        return "Node [left=" + left + ", right=" + 
            right + ", operation=" + operation + "]";
    }

    private static enum Operator {
        ADDITION, SUBTRACTION, DIVISION, MULTIPLICATION, POWER;

        public int getPEDMASScore() {
            return switch (this) {
                case ADDITION, SUBTRACTION -> 1;
                case MULTIPLICATION, DIVISION -> 2;
                case POWER -> 3;
            };
        }

        private static Operator getOperator(String opString) {
            return switch (opString) {
                case "+" -> Operator.ADDITION;
                case "-" -> Operator.SUBTRACTION;
                case "*" -> Operator.MULTIPLICATION;
                case "/" -> Operator.DIVISION;
                case "^" -> Operator.POWER;
                default -> Operator.ADDITION;
            };
        }

        private static class OperatorComparator 
            implements Comparator<Operator> {

            @Override
            public int compare(Operator o1, Operator o2) {
                
                return Integer.valueOf(o1.getPEDMASScore())
                    .compareTo(Integer.valueOf(o2.getPEDMASScore()));
            }
        }
    }

    private static class NumberNode implements Calculatable {

        private double value;

        public NumberNode(double value) {
            this.value = value;
        }

        public double calculate() {
            return value;
        }

        @Override
        public String toString() {
            return "%.4f".formatted(value);
        }
    }

    private static class ParentheticalContext implements Calculatable {

        private Node head;

        public ParentheticalContext(Node head) {

            this.head = head;
        }

        @Override
        public double calculate() {
            
            return head.calculate();
        }

        public void addBranch(String newOp, String newRight) {

            head = head.addBranch(newOp, newRight);
        }

        @Override
        public String toString() {
            return "ParentheticalContext [head=" + head + "]";
        }
    }
}
