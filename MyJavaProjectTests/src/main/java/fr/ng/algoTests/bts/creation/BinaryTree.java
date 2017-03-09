package fr.ng.algoTests.bts.creation;

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

        // If 'i' is root, change root pointer and return
        if (parentValues[i] == -1) {
            root = createdNode[i];
            return;
        }

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

    //For adding new line in a program
    public void newLine() {
        System.out.println("");
    }

    // Utility function to do inorder traversal
    public void inorder(Node node) {
        if (node != null) {
            inorder(node.getLeft());
            System.out.print(node.getData() + " ");
            inorder(node.getRight());
        }
    }

    public static void main(String[] args) {

        BinaryTree tree = new BinaryTree();
//        int parent[] = new int[]{-1, 0, 0, 1, 1, 3, 5};
        int parent[] = new int[]{1, 5, 5, 2, 2, -1, 3};
        int n = parent.length;
        Node node = tree.createTree(parent, n);
        System.out.println("Inorder traversal of constructed tree ");
        tree.inorder(node);
        tree.newLine();
    }
}