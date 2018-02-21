package fr.ng.algoTests.bst.bstOrderingAndOperations;

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

    public void addNote(final Node node) {
        if (root == null) {
            root = node;
        } else {
            addNodeToTree(root, node);
        }
    }

    private void addNodeToTree(final Node root, final Node newNode) {
        if (newNode.getData() < root.getData()) {
            if (root.getLeft() == null) {
                root.setLeft(newNode);
            } else {
                addNodeToTree(root.getLeft(), newNode);
            }
        } else {
            if (root.getRight() == null) {
                root.setRight(newNode);
            } else {
                addNodeToTree(root.getRight(), newNode);
            }
        }
    }

    public boolean contains(final Node node) {
        boolean result = findWithParent(node) != null ?
                true :
                false;
        return result;
    }

    private Node findWithParent(Node node) {
        Node current = this.root;

        while (current != null) {
            if (current.getData() > node.getData()) {
                current = current.getLeft();
            } else if (current.getData() < node.getData()) {
                current = current.getRight();
            } else {
                break;
            }
        }
        return current;
    }

    public int findDistanceBetweenNode(int n1, int n2) {
        int x = pathlength(root, n1) - 1;
        int y = pathlength(root, n2) - 1;
        int lcaData = findLCA(root, n1, n2).getData();
        int lcaDistance = pathlength(root, lcaData) - 1;
        return (x + y) - 2 * lcaDistance;
    }

    private Node findLCA(Node root, int n1, int n2) {
        if (root != null) {
            if (root.getData() == n1 || root.getData() == n2) {
                return root;
            }
            Node left = findLCA(root.getLeft(), n1, n2);
            Node right = findLCA(root.getRight(), n1, n2);

            if (left != null && right != null) {
                return root;
            }
            if (left != null) {
                return left;
            }
            if (right != null) {
                return right;
            }
        }
        return null;
    }

    private int pathlength(Node root, int n1) {
        if (root != null) {
            int x = 0;
            if ((root.getData() == n1)
                    || (x = pathlength(root.getLeft(), n1)) > 0
                    || (x = pathlength(root.getRight(), n1)) > 0) {
                return x + 1;
            }
            return 0;
        }
        return 0;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
