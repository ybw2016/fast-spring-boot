package com.fast.springboot.basic.algorithm.sort;

/**
 * 链表交换数据
 *
 * @author yanbowen
 * @since 2026-09-25
 */
public class LinklistChangeData {
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
        changeOrder(p);
        print(p);
    }

    public static void print(LinkList linkList) {
        while (linkList != null) {
            System.out.print(linkList.data + ",");
            linkList = linkList.next;
        }
    }

    public static void changeOrder(LinkList linkList) {
        LinkList head = linkList;
        LinkList p = head;
        int count = 1;
        while (p != null) {
            p = p.next;
            count++;
            if (p != null && count == 3) {
                int temp = head.data;
                head.data = p.data;
                p.data = temp;
                head = p.next;
                count = 0;
            }
        }
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
