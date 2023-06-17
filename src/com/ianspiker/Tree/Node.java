package com.ianspiker.Tree;

abstract class Node implements Evaluable {
    
    private Node parent;

    Node() {
        this(null);
    }

    Node(Node parent) {
        this.parent = parent;
    }

    public abstract Node branch(Node node);

    public abstract PEDMASLevel getPedmasScore();

    int compareTo(Node o) {

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
