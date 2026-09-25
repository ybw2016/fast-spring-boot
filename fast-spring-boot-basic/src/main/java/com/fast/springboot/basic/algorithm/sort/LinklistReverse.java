package com.fast.springboot.basic.algorithm.sort;

/**
 * 链表倒置
 *
 * @author yanbowen
 * @since 2026-09-25
 */
public class LinklistReverse {
    public static void main(String[] args) {
        LinkList head7 = new LinkList(7, null);
        LinkList head6 = new LinkList(6, head7);
        LinkList head5 = new LinkList(5, head6);
        LinkList head4 = new LinkList(4, head5);
        LinkList head3 = new LinkList(3, head4);
        LinkList head2 = new LinkList(2, head3);
        LinkList head1 = new LinkList(1, head2);
        LinkList p = head1;
        System.out.println("原始链接顺序: ");
        print(p);
        System.out.println("\n改变后的链接顺序: ");
        LinkList newHead = reverse(p);
        print(newHead);
    }

    public static void print(LinkList linkList) {
        while (linkList != null) {
            System.out.print(linkList.data + ",");
            linkList = linkList.next;
        }
    }

    public static LinkList reverse(LinkList linkList) {
        if (linkList == null) {
            return null;
        }
        LinkList head = linkList;
        LinkList p = linkList.next;
        head.next = null;
        while (p != null) {
            LinkList q = p.next;
            p.next = head;
            head = p;
            p = q;
        }
        return head;
    }

    public static class LinkList {
        private Integer data;
        private LinkList next;

        public LinkList(Integer data, LinkList next) {
            this.data = data;
            this.next = next;
        }
    }
}
