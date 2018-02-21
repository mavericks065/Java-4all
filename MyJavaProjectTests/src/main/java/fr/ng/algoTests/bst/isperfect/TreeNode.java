package fr.ng.algoTests.bst.isperfect;

/**
 * Exercise
 *
 * A perfect binary tree is a binary tree in which all interior nodes have two children
 and all leaves have the same depth or same level.

 You are given a class called TreeNode. Implement the method isPerfect which determines
 if a given tree denoted by its root node is perfect.

 Note: TreeNode class contains helper methods for tree creation, which might be handy
 for your solution. Feel free to update those methods, but make sure you keep their
 signatures intact (since they are used from within test cases).

 You can (and should) add more tests to validate your solution, since not all cases
 are covered in the example test cases.
 */
public class TreeNode {

    public TreeNode left;
    public TreeNode right;
    public int value;

    public TreeNode() {
    }

    public TreeNode(int value, TreeNode left, TreeNode right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public TreeNode(int value) {
        this(value, null, null);
    }

    // TODO implement method
    public static boolean isPerfect(TreeNode root) {
        if (root == null || (root.left == null && root.right == null)) {
            return true;
        }
        if (size(root.left) != size(root.right))
            return false;
        if (isPerfect(root.left) && isPerfect(root.right)) {
            return true;
        } else {
            return false;
        }
    }

    private static int size(TreeNode node) {
        if (node == null)
            return 0;
        return size(node.left) + size(node.right) + 1;
    }

    static TreeNode leaf() {
        return new TreeNode();
    }

    static TreeNode join(TreeNode left, TreeNode right) {
        return new TreeNode().withChildren(left, right);
    }

    TreeNode withLeft(TreeNode left) {
        this.left = left;
        return this;
    }

    TreeNode withRight(TreeNode right) {
        this.right = right;
        return this;
    }

    TreeNode withChildren(TreeNode left, TreeNode right) {
        this.left = left;
        this.right = right;
        return this;
    }

    TreeNode withLeftLeaf() {
        return withLeft(leaf());
    }

    TreeNode withRightLeaf() {
        return withRight(leaf());
    }

    TreeNode withLeaves() {
        return withChildren(leaf(), leaf());
    }

}