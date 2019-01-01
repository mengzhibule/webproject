package com.test.linkedlist;

/**
 * 单链表
 *
 * @author shao
 * @version [版本号, 2018年12月31日]
 */
public class SingleLinkedList {

    // 链表结点的个数
    private int size;
    // 头结点
    private Node head;

    /**
     * 插入头结点的值
     *
     * @param value
     */
    public void insertToHead(Object value) {
        Node newNode = new Node(value, null);
        insertToHead(newNode);
    }

    /**
     * 插入头结点
     *
     * @param node
     */
    public void insertToHead(Node node) {
        //如果头结点为空，那么newNode就是head
        if (head == null) {
            head = node;
        } else {
            // 如果head不为空，那么newNode.next = head
            node.next = head;
            head = node;
        }
        size++;
    }

    /**
     * 插入尾节点的值
     *
     * @param value
     */
    public void insertTail(Object value) {
        Node newNode = new Node(value, null);
        insertTail(newNode);

    }

    /**
     * 插入尾节点
     * @param node
     */
    public void insertTail(Node node) {
        //如果头结点为空，新节点就是头结点
        if (head == null) {
            head = node;
        } else {
            //如果链表不为空，遍历整个链表，直到找到尾节点。
            Node p = head;
            while (p != null && p.next != null) {
                p = p.next;
            }
            //给新节点的后继指针赋值为null，表示尾节点
            node.next = null;
            //之前的尾节点的后继指针需要指向新的node
            p.next = node;
        }
        size++;
    }

    /**
     * 在某个节点后插入值
     * @param node
     * @param value
     */
    public void insertAfter(Node node, Object value) {
        Node newNode = new Node(value, null);
        insertAfter(node, newNode);
    }

    /**
     * 在某个节点后面插入节点
     * @param node
     * @param newNode
     */
    public void insertAfter(Node node, Node newNode) {
        if(head == null){
            return;
        }
        if (node == null) {
            return;
        }
        //如果node不为空，那么需要断开之前的指针，新节点的后继指针指向老节点的之前的后继指针指向的node
        //老节点的现在的后继指针指向的newnode
        newNode.next = node.next;
        node.next = newNode;
        size++;
    }

    /**
     * 在某个节点之前插入值
     * @param node
     * @param value
     */
    public void insertBefore(Node node, Object value) {
        Node newNode = new Node(value, null);
        insertBefore(node, newNode);
    }

    /**
     * 在某个节点之前插入节点
     * @param node
     * @param newNode
     */
    public void insertBefore(Node node, Node newNode) {
        if (node == null) {
            return;
        }
        //如果node就是head，那么调用insertToHead即可
        if (head == node) {
            insertToHead(newNode);
            return;
        }
        //遍历整个链表，直到找到老节点之前的节点
        Node p = head;
        while (p != null && p.next != p) {
            p = p.next;
        }
        if (p == null) {
            return;
        }
        //新节点的后继指针指向指定的节点
        newNode.next = node;
        //指点节点之前的节点的后继指针指向新的节点
        p.next = newNode;
        size++;
    }

    /**
     * 根据节点删除
     * @param p
     */
    public void deleteByNode(Node p) {
        if (p == null || head == null) {
            return;
        }
        //如果需要删除的头结点，那么之前的头结点的后面的节点，就变成了新的节点
        if (p == head) {
            head = head.next;
            return;
        }

        //遍历
        Node q = head;
        while (q != null && q.next != p) {
            q = q.next;
        }

        if (q == null) {
            return;
        }
        //要删除的节点，之前的节点的后继指针需要指向要删除的节点之后的节点。
        q.next = q.next.next;
        size--;
    }

    /**
     * 根据值删除
     * @param value
     */
    public void deleteByValue(Object value) {
        if (head == null) {
            return;
        }
        Node p = head;
        Node q = null;
        while (p != null && p.data.equals(value)) {
            q = p;
            p = p.next;
        }
        if (p == null) {
            return;
        }

        if (q == null) {
            head = head.next;
        } else {
            q.next = q.next.next;
        }
        size--;
    }

    public Node findByValue(Object value) {
        Node p = head;
        while (p != null && p.data.equals(value)) {
            p = p.next;
        }
        return p;
    }

    public Node findByIndex(int index) {
        Node p = head;
        int pos = 0;
        while (p != null && pos != index) {
            p = p.next;
            ++pos;
        }
        return p;
    }

    public boolean TFResult(Node left, Node right) {
        Node l = left;
        Node r = right;
        while (l != null && r != null) {
            if (l.data.equals(r.data)) {
                l = l.next;
                r = r.next;
                continue;
            } else {
                break;
            }
        }

        if (l == null && r == null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean palindrome() {
        if (head == null) {
            return false;
        }
        System.out.println("开始执行找到中间结点");
        Node p = head;
        Node q = head;
        if (p.next == null) {
            System.out.println("只有一个元素");
            return true;
        }
        while (q.next != null && q.next.next != null) {
            p = p.next;
            q = q.next.next;
        }

        System.out.println("中间结点" + p.data);
        System.out.println("开始执行奇数结点的回文判断");
        Node leftNode = null;
        Node rightNode = null;
        if (q.next == null) {
            rightNode = p.next;
            leftNode = inverseLinkList(p).next;
            System.out.println("左边第一个结点" + leftNode.data);
            System.out.println("右边第一个结点" + rightNode.data);
        } else {
            rightNode = p.next;
            leftNode = inverseLinkList(p);
        }
        return TFResult(leftNode, rightNode);
    }

    public Node inverseLinkList_head(Node p) {
        Node headNode = new Node(new Object(), null);
        headNode.next = p;
        Node cur = p.next;
        p.next = null;
        Node next = null;
        while (cur != null) {
            next = cur.next;
            cur.next = headNode.next;
            headNode.next = cur;
            System.out.println("first" + headNode.data);
            cur = next;
        }
        return headNode;
    }

    public static Node createNode(int value) {
        return new Node(value, null);
    }

    public Node inverseLinkList(Node p) {
        Node pre = null;
        Node r = head;
        System.out.println("z -----" + r.data);
        Node next = null;
        while (r != p) {
            next = r.next;
            r.next = pre;
            pre = r;
            r = next;
        }
        r.next = pre;
        return r;
    }

    public void printAll() {
        Node p = head;
        while (p != null) {
            System.out.print(p.data + "");
            p = p.next;
        }
        System.out.println();
    }

    // 结点
    public static class Node {
        // 每个结点的数据
        private Object data;
        // 后继指针next
        private Node next;

        public Node(Object data, Node next) {
            this.data = data;
            this.next = next;
        }

        public Object getData() {
            return data;
        }
    }

}
