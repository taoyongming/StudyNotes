package leetcode;

/**
 * ReverseList :
 *
 * @author tym
 * @ceeate 2020/1/14
 **/
public class ReverseList {

    public ListNode reverseList(ListNode head) {
        ListNode tmp = null;
        ListNode pre = null;
        ListNode cur = head;

        while(cur != null) {
            //记录当前节点下一个节点
            tmp = cur.next;
            //将当前结点next指向pre
            cur.next = pre;
            //将cur节点和pre节点都前移一位
            pre = cur;
            cur = tmp;
        }
        return  pre;
    }
}
