package com.ianspiker.Tree;

abstract class Node implements Evaluable, Comparable<Node> {
    
    private Node parent;

    public abstract Node branch(Node node);

    public abstract PEDMASLevel getPedmasScore();

    public int compareTo(Node o) {

        return PEDMASLevel.getComparator()
                .compare(getPedmasScore(), o.getPedmasScore());
    }

    Node getParent() {
        return parent;
    }

    void setParent(Node parent) {
        this.parent = parent;
    }
}
