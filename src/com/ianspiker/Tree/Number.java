package com.ianspiker.Tree;

class Number extends Node {

    private double value;

    Number(double value) {
        this.value = value;
    }

    @Override
    public double evaluate() {

        return value;
    }

    @Override
    public PEDMASLevel getPedmasScore() {

        return PEDMASLevel.HIGHEST;
    }

    @Override
    public Node branch(Node node) {

        if (this.compareTo(node) > 0) {

            if (node instanceof Operator o) {
                o.setLeft(this);
                if (this.getParent() instanceof Operator p) {
                    
                    p.setRight(node);
                }
                node.setParent(this.getParent());
                setParent(node);
                return node;
            }
        }

        return this;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}