package fr.ng.algoTests.bst.isperfect;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class PerfectTreeTest {

    /**
     * null
     */
    @Test
    public void testNullTree() {
        TreeNode root = null;
        assertThat("null tree should be perfect", TreeNode.isPerfect(root), is(true));
    }

    /**
     *   0
     *  / \
     * 0   0
     */
    @Test
    public void testFullOneLevelTree() {
        TreeNode root = TreeNode.leaf().withLeaves();
        assertThat("root with two leaves should be perfect", TreeNode.isPerfect(root), is(true));
    }


    /**
     *   0
     *  /
     * 0
     */
    @Test
    public void testOneChildTree() {
        TreeNode root = TreeNode.leaf().withLeftLeaf();
        assertThat("root with single leaf should not be perfect", TreeNode.isPerfect(root), is(false));
    }
}