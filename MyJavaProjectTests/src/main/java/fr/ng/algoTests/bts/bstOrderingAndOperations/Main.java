package fr.ng.algoTests.bts.bstOrderingAndOperations;

public class Main {
    public static void main(String[] args) {
        final BinaryTree tree = new BinaryTree();
        final int arr[] = new int[]{2, 3, 4, 5, 6, 7, 8};
//        final int arr[] = new int[]{1, 2, 3, 4, 5, 6, 7};
//       final  int arr[] = new int[]{1, 2, 3, 6, 9, 22, 23};
//       final  int arr[] = new int[]{1, 2, 3, 5, 6, 9, 22, 23};
        int n = arr.length;

        tree.setRoot(tree.sortedArrayToBST(arr, 0, n - 1));

        BinaryTree.preOrder(tree.getRoot());
        System.out.println();
        BinaryTree.inOrder(tree.getRoot());
        System.out.println();
        BinaryTree.postOrder(tree.getRoot());

        System.out.println();
        System.out.println("add Node : ");
        tree.addNote(new Node(1));
        BinaryTree.preOrder(tree.getRoot());

    }
}
