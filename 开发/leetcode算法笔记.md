# leetcode算法笔记

### 105、根据前序遍历和中序遍历建立二叉树

给定一棵树的前序遍历 `preorder` 与中序遍历  `inorder`。请构造二叉树并返回其根节点。

知识点：**先序遍历**序列**第一个元素**为该二叉树的根节点，**中序遍历**序列中，**根节点左边**的的序列为左子树，右边的序列为**右子树**



- 可使用递归：先通过先序遍历的第一个元素为根节点，找到中序遍历序列中根节点的位置，然后通过递归方法，分别对根节点的左子树和右子树进行递归；

```java
 public TreeNode buildTree(int[] preorder, int[] inorder) {
        return findTree(preorder,inorder,0, preorder.length - 1, 0, inorder.length - 1);
    }

    /**
     *
     * @param preorder  先序遍历
     * @param inorder   中序遍历
     * @param preLeft  先序遍历左指针
     * @param preRight  先序遍历右指针
     * @param inLeft    中序遍历左指针
     * @param inRight    中序遍历右指针
     * @return
     */
    public TreeNode findTree(int[] preorder, int[] inorder, int preLeft, int preRight, int inLeft,int inRight){
        //记录中序遍历中根节点的下标
        int t = 0;
        //先序遍历第一个元素为二叉树的根节点；
        TreeNode root = new TreeNode(preorder[preLeft]);
        //查找中序遍历中的根节点
        for (t = inLeft; inorder[t]!=preorder[preLeft]&&t <= inRight; t++) ;
        //左右子数的长度
        int leftLen = t - inLeft;
        int rightLen = inRight - t;
        //递归左子树
        if (leftLen > 0) {
            root.left = findTree(preorder, inorder, preLeft + 1, preLeft + leftLen, inLeft, t - 1);
        }
        //递归右子树
        if (rightLen > 0) {
            root.right = findTree(preorder, inorder, preRight - rightLen + 1, preRight, t + 1, inRight);
        }
        return root;
    }
```

### 114、二叉树展开为链表

给你二叉树的根结点 root ，请你将它展开为一个单链表：

- 展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。

- 展开后的单链表应该与二叉树 先序遍历 顺序相同。

使用后序遍历的方法：采用一个临时节点存放右子树

```java
public void flatten(TreeNode root) {
    if (root == null) {
        return;
    }
    TreeNode temp = new TreeNode();
    flatten(root.left);
    flatten(root.right);
    //采用后序遍历
    temp = root.right;
    root.right = root.left;
    root.left = null;
    while (root.right != null) {
        root = root.right;
    }
    root.right = temp;
}
```



### 121、买卖股票的最佳时机

给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。

你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。

返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。



使用回溯：

```java
public class BestTime {
    public static void main(String[] args) {
        BestTime bestTime = new BestTime();
        int[] pri = {7,6,4,3,1};
        int ans = bestTime.maxProfit(pri);
        System.out.println(ans);
    }


    public int maxProfit(int[] prices) {
        if (prices.length <= 1){
            return 0;
        }
        //使用回溯的方法；

        //当前股票的最大收益值
        int max = 0;
        //今天之前股票最低价；
        int min = prices[0];
        for (int i = 0; i < prices.length - 1; i++) {
            max = Math.max(max,prices[i] - min);
            min = Math.min(min,prices[i]);
        }
        return max;
    }
}
```



### 124、二叉树中的最大路径和

路径 被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中 至多出现一次 。该路径 至少包含一个 节点，且不一定经过根节点。

路径和 是路径中各节点值的总和。

给你一个二叉树的根节点 root ，返回其 最大路径和 。

