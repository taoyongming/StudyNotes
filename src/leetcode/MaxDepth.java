package leetcode;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

/**
 * maxDepth :
 *
 * @author tym
 * @ceeate 2020/1/14
 **/
public class MaxDepth {

    public int maxDepth(TreeNode root) {

       if(root == null) {
           return  0;
       }else {
           int left_height = maxDepth(root.left);
           left_height += 1;
           int right_height = maxDepth(root.right);
           right_height += 1;


           return left_height > right_height ? left_height :right_height;
       }

    }

    public int maxDepth2(TreeNode root) {
        Queue<Pair<TreeNode, Integer>> stack = new LinkedList<>();
        if (root != null) {
            stack.add(new Pair(root, 1));
        }
        int depth = 0;
        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> current = stack.poll();
            root = current.getKey();
            int current_depth = current.getValue();
            if (root != null) {
                depth = Math.max(depth, current_depth);
                stack.add(new Pair(root.left, current_depth + 1));
                stack.add(new Pair(root.right, current_depth + 1));
            }
        }
        return depth;
    }


}
