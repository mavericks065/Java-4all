package fr.ng.algoTests;

/**
 * Created by nicolasguignard-octo on 27/02/2017.
 */
public class BinaryTree {

    static Node root;

    public Node createTree(int[] parentValues, int n) {

        Node[] createdNode = new Node[n];

        for (int i = 0; i < n; i++) {

            createdNode[i] = null;
        }
        for (int i = 0; i < n; i++) {
            createNode(parentValues, i, createdNode);
        }
        return root;
    }

    private void createNode(int[] parentValues, int i, Node[] createdNode) {

        if (createdNode[i] != null) {
            return;
        }

        createdNode[i] = new Node(i);

        // if parent is not created i create it
        if (createdNode[parentValues[i]] == null) {
            createNode(parentValues, parentValues[i], createdNode);
        }

        Node parentNode = createdNode[parentValues[i]];
        if (parentNode.getLeft() == null) {
            parentNode.setLeft(createdNode[i]);
        } else {
            parentNode.setRight(createdNode[i]);
        }

    }
}