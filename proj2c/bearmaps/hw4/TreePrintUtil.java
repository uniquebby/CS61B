package bearmaps.hw4;

import java.util.ArrayList;

public class TreePrintUtil {
      public static void print(TreeNode root) {
          if (root == null) {
              return;
          }
          ArrayList<TreeNode> queue = new ArrayList<>();
          TreeNode cur = root;
          int level = -1;
          root.setLevel(0);
          queue.add(0, root);
          while (queue.size() != 0) {
              cur = queue.remove(0);
              if (level != cur.getLevel()) {
                  level = cur.getLevel();
                  System.out.println();
                  System.out.print("level" + level + ": ");
              }

              System.out.print(cur.getPrintInfo());
              if (cur.getLeftChild() != null) {
                  cur.getLeftChild().setLevel(level + 1);
                  queue.add(cur.getLeftChild());
              }
              if (cur.getRightChild() != null) {
                  cur.getRightChild().setLevel(level + 1);
                  queue.add(cur.getRightChild());
              }
          }
          System.out.println(level);
      }

    public interface TreeNode {

        String getPrintInfo();

        TreeNode getLeftChild();

        TreeNode getRightChild();

        int getLevel();

        void setLevel(int level);
    }

}
