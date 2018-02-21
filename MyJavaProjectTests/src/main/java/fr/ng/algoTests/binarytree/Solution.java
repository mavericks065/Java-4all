package fr.ng.algoTests.binarytree;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Solution {

//    private static LinkedList<TreeNode> nextLevel = new LinkedList<>();
//    static LinkedList<TreeNode> currentLevel = new LinkedList<>();
//
//    static TreeNode arrayToTree(int[] array) {
//
//        return createTree(array);
//    }
//
//    private static TreeNode createTree(int[] array) {
//        if (array.length == 0)
//            return null;
//
//
//        TreeNode rootNode = new TreeNode(array[0]);
//        currentLevel.add(rootNode);
//
//
//        LinkedList<TreeNode> current = new LinkedList<TreeNode>();
//        LinkedList<TreeNode> next = new LinkedList<TreeNode>();
//        current.add(rootNode);
//        int i = 1;
//
//
//        if (current.isEmpty()) {
//            current = next;
//            next = new LinkedList<>();
//        }
//
//
//        TreeNode node = current.poll();
//        if (node.left == null) {
//            TreeNode nodeToInsert = new TreeNode(array[i]);
//            node.left = nodeToInsert;
//            next.add(nodeToInsert);
//            i++;
//        }
//        if (node.right == null) {
//            TreeNode nodeToInsert = new TreeNode(array[i]);
//            node.right = nodeToInsert;
//            next.add(nodeToInsert);
//            i++;
//        }
//        i++;
//
////        for(int i = 1; i <= array.length - 1; i++) {
////            int number = array[i];
////            boolean isProcessed = currentProcess(current, next, number);
////            if (!isProcessed) {
////                current.remove();
////            }
////            if (current.isEmpty()) {
////                current = next;
////                next = new LinkedList<>();
////            }
////        }
//        return rootNode;
//    }
//
//    private static TreeNode getNodeToProcess() {
//        if (currentLevel.isEmpty()) {
//            currentLevel = nextLevel;
//            nextLevel = new LinkedList<>();
//        }
//        TreeNode node = currentLevel.peek();
//        return node;
//    }
//
//    private static boolean currentProcess(LinkedList<TreeNode> current, LinkedList<TreeNode> next, int number) {
//        TreeNode node = current.poll();
//        if (node.left == null) {
//            TreeNode nodeToInsert = new TreeNode(number);
//            node.left = nodeToInsert;
//            next.add(nodeToInsert);
//            return true;
//        } else if (node.right == null) {
//            TreeNode nodeToInsert = new TreeNode(number);
//            node.right = nodeToInsert;
//            next.add(nodeToInsert);
//            return true;
//        }
//        return false;
//    }
//
//    public static void main(String[] args) {
//        int[] arr = {17, 0, -4, 3, 15};
//        TreeNode result = arrayToTree(arr);
//        System.out.print(result);
//    }


    public static Set<Integer> getDuplicates(int[] array) {
        int n = array.length;
        Set<Integer> duplicates = new HashSet<Integer>();
        for (int i = 0; i < n; i++) {

            if (array[Math.abs(array[i])] > 0) {
                array[Math.abs(array[i])] = -array[Math.abs(array[i])];
            } else {

                duplicates.add(Math.abs(array[i]));
            }
        }
        return duplicates;
    }

    public static void main(String[] args) {
        int[] array = {2, 4, 1, 2, 6, 1, 6, 3, 5};
        Set<Integer> duplicates = getDuplicates(array);
        System.out.println(Arrays.toString(duplicates.toArray()));
    }
}