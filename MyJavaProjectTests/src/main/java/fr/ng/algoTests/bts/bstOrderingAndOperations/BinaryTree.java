package fr.ng.algoTests.bts.bstOrderingAndOperations;

public class BinaryTree {

    private Node root;

    public Node sortedArrayToBST(int arr[], int start, int end) {

        // Base Case
        if (start > end) {
            return null;
        }

        // Get the middle element of the array and make it root
        int mid = (start + end) / 2;
        Node node = new Node(arr[mid]);

        // Recursively construct the left subtree and make it
        // left child of root
        node.setLeft(sortedArrayToBST(arr, start, mid - 1));

        //Recursively construct the right subtree and make it
        //right child of root
        node.setRight(sortedArrayToBST(arr, mid + 1, end));

        return node;
    }

    /**
     * A utility function to print preorder traversal of BST
     */
    public static void preOrder(Node node) {
        if (node == null) {
            return;
        }
        processNode(node);
        preOrder(node.getLeft());
        preOrder(node.getRight());
    }

    /**
     * A utility function to print in-order traversal of BST
     */
    public static void inOrder(Node node) {
        if (node == null) {
            return;
        }
        inOrder(node.getLeft());
        processNode(node);
        inOrder(node.getRight());
    }

    /**
     * A utility function to print postOrder traversal of BST
     */
    public static void postOrder(Node node) {
        if (node == null) {
            return;
        }
        postOrder(node.getLeft());
        postOrder(node.getRight());
        processNode(node);
    }

    private static void processNode(Node node) {
        System.out.print(node.getData() + " ");
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
