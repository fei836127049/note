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

```java
package com.company;

/**
 * @author zhf
 * @date 2022/1/21
 */
//给你一个二叉树的根节点 root ，返回其 最大路径和 。
public class MaxPathSum {
    public static void main(String[] args) {
        Integer[] array = {5,4,8,11,null,13,4,7,2,null,null,null,1};
        TreeNode root = TreeUtil.CreateTree(array);
        MaxPathSum maxPathSum = new MaxPathSum();
        int ans = maxPathSum.maxPathSum(root);
        System.out.println(ans);
    }
    int ans = Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) {
        dfs(root);
        return ans;
    }

    //可以使用递归求解


    public int dfs(TreeNode root){
        if (root == null) {
            return 0;
        }
        int leftMax = Math.max(0,dfs(root.left));
        int rightMax = Math.max(0,dfs(root.right));
        //计算当前最大路径和；
        ans = Math.max(ans,leftMax + rightMax + root.val);

        //计算当前路径和
        return Math.max(leftMax,rightMax) + root.val;
    }
}

```



### 128、最长连续序列

给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。

请你设计并实现**时间复杂度为 O(n)** 的算法解决此问题。

**可以用空间来换时间复杂度，用时间来换空间复杂度；**

使用暴力方法解题（时间复杂度nlogn)：

```java
package com.company;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhf
 * @date 2022/1/21
 */
//给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
    //时间复杂度为 O(n)
public class LongestCon {
    public static void main(String[] args) {
        //[9,1,4,7,3,-1,0,5,8,-1,6]
        int[] nums = new int[]{9,1,4,7,3,-1,0,5,8,-1,6};
        LongestCon longestCon = new LongestCon();
        int ans = longestCon.longestConsecutive(nums);
        System.out.println(ans);

    }
    public int longestConsecutive(int[] nums) {
        if (nums.length <= 1) {
            return nums.length;
        }
        Arrays.sort(nums);
        int max = 1;
        int temp = 1;
        int i = 1;
        while (i <= nums.length - 1) {
            if (nums[i] - nums[i - 1] == 1){
                temp++;
                i++;
            }else if(nums[i] - nums[i - 1] == 0){
                i++;
            }else {
                temp = 1;
                i++;
            }
            max = Math.max(temp,max);
        }
        return max;

    }
}

```

使用hash表解题：



### 136、只出现一次的数字

给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。

使用位运算：

```java
public int singleNumber(int[] nums) {
    int ans = 0;
    for (int num : nums) {
        ans ^= num;
    }
   return ans;
}
```

### 139、单词拆分

给你一个字符串 s 和一个字符串列表 wordDict 作为字典。请你判断是否可以利用字典中出现的单词拼接出 s 。

注意：不要求字典中出现的单词全部都使用，并且字典中的单词可以重复使用。



知识点：

```java
/*当前列表若包含某元素，返回结果为true, 若不包含该元素，返回结果为false。*/
public boolin list.contains(Object o)
/*截取字符串中的一段子字符串；*/
public StringsubString（int beginIndex, int endIndex）

```

本题使用回溯：

```java
 public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] valid = new boolean[s.length() + 1];
        valid[0] = true;
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (wordDict.contains(s.substring(j,i)) && valid[j]) {
                    valid[i] = true;
                }
            }
        }
        return valid[s.length()];
    }
```

### 141、环形链表

给你一个链表的头节点 head ，判断链表中是否有环。

如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。 为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。注意：pos 不作为参数进行传递 。仅仅是为了标识链表的实际情况。

如果链表中存在环 ，则返回 true 。 否则，返回 false 。

思路：使用快慢指针；快指针向前移动两个，慢指针移动一个，如果快慢指针相遇，那么存在环，如果在最后都没有相遇，那么就不存在环；

```java
    public boolean hasCycle(ListNode head) {
        if (head == null){
            return false;
        }
        ListNode fast = head;
        ListNode low = head;
        while (fast != null && fast.next != null){
            //快指针移动两个，慢指针移动一个；
            fast = fast.next.next;
            low = low.next;
            if (fast == low) {
                return true;
            }
        }
        return false;
    }
```

### 142、环形链表2

给定一个链表的头节点  head ，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。

如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。 为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。如果 pos 是 -1，则在该链表中没有环。注意：pos 不作为参数进行传递，仅仅是为了标识链表的实际情况。

思想：快慢指针：设置两个快慢指针，当快慢指针在环形区域相遇以后，设置另外一个指针，该指针从链表头部出发，与慢指针同步移动，最后两个指针会在环的起点相遇；

```java
    public ListNode detectCycle(ListNode head) {
        if (head == null){
            return null;
        }
        ListNode ans = head;
        ListNode fast = head;
        ListNode low = head;
        while (fast != null && fast.next != null){
            //快指针移动两个，慢指针移动一个；
            fast = fast.next.next;
            low = low.next;
            if (fast == low) {
              //两个指针会在环的起点相遇；
                while (ans != low) {
                    ans = ans.next;
                    low = low.next;
                }
                return ans;
            }
        }
        return null;
    }
```

### 148、排序链表

给你链表的头结点 head ，请将其按 升序 排列并返回 排序后的链表 。

思路：先计算出链表的长度，然后根据中这个新建一个数组，将链表的值存入数组，然后再根据排序后的数组进行链表的建立。

```java
 public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        int n = 0;
        ListNode root = head;
        //计算链表长度
        while (root.next != null){
            n++;
            root = root.next;
        }
   		//新建一个数组存放链表的值
        List<Integer> list = new ArrayList<>();
        root = head;
        for (int i = 0; i <= n; i++) {
            list.add(root.val);
            root = root.next;
        }
        //给这个数组排序；
        list.sort(Comparator.comparing(Integer::intValue));
        root = head;
        for (int i = 0; i <= n; i++) {
            root.val = list.get(i);
            root = root.next;

        }
        return root;
    }
```



### 152、乘积最大子数组

给你一个整数数组 nums ，请你找出数组中乘积最大的非空连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。

测试用例的答案是一个 32-位 整数。

子数组 是数组的连续子序列



思路：双层循环，每一次计算当前元素所在的子集的最大值；

```java
    public int maxProduct(int[] nums) {
        int len = nums.length;
        int ans = Integer.MIN_VALUE;
        for (int i = 0; i < len; i++) {
            int temp = 1;
            for (int j = i; j < len; j++) {
                temp = temp * nums[j];
                ans = Math.max(ans,temp);
            }

        }
        return ans;
    }
```

### 155、最小栈

设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。

实现 MinStack 类:

- MinStack() 初始化堆栈对象。

- void push(int val) 将元素val推入堆栈。
- void pop() 删除堆栈顶部的元素。
- int top() 获取堆栈顶部的元素。
- int getMin() 获取堆栈中的最小元素。

思路：

- 首先是入栈规则，设置一个辅助栈，当辅助栈为空时，辅助栈和主栈同时入栈相同的元素，后面的入栈规则是，当入栈元素小于辅助栈的栈顶元素，则将该元素压入辅助栈，如果该元素大于辅助栈栈顶元素，则不入栈。
- 出栈规则，当主栈栈顶元素与辅助栈栈顶元素一样时，辅助栈和主栈栈顶元素都出栈，如果不相等，则只有主栈元素出栈，保证辅助栈的栈顶元素为当前主栈元素中最小的值；

```java

    private Stack<Integer> MainStack;

    private Stack<Integer> MinStack;
//    初始化栈
    public MinStack() {
        //原始栈
        this.MainStack = new Stack<>();

        //辅助栈
        this.MinStack = new Stack<>();
    }

//    将元素val推入堆栈
    public void push(int val) {
        MainStack.push(val);
        if (MinStack.isEmpty() || (!MinStack.isEmpty() && MinStack.peek() >= val)){
            MinStack.push(val);
        }
    }

//    删除堆栈顶部的元素
    public void pop() {
        int val = MainStack.pop();

        if (val == MinStack.peek()){
            MinStack.pop();
        }
    }

//    获取堆栈顶部的元素
    public int top() {
       return MainStack.peek();
    }

//    获取堆栈中的最小元素
    public int getMin() {
        if (MinStack.isEmpty()) {
            return 0;
        }
        return MinStack.peek();
    }
```

### 160、相交链表

给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。如果两个链表不存在相交节点，返回 null 。

函数返回结果后，链表必须 保持其原始结构 。

