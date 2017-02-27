package fr.ng.algoTests;

public class Node {
    private int data;
    private Node left;
    private Node right;

    public Node(int d) {
        this.data = d;
        left = null;
        right = null;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}