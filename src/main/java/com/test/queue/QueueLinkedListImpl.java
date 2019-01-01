package com.test.queue;

public class QueueLinkedListImpl<T> {
    private Node head;
    private Node tail;

    public void enqueue(T t){
        if(tail == null){
            Node newNode = new Node(t,null);
            head = newNode;
            tail = newNode;
        }else{
            tail.next = new Node(t,null);
            tail = tail.next;
        }
    }

    public T dequeue(){
        if(head == null){
            return null;
        }
        T value = (T)head.data;
        head = head.next;
        if(head == null){
            tail = null;
        }
        return value;
    }

    public void printAll(){
        Node p = head;
        while(p != null){
            System.out.print(p.data + "");
            p = p.next;
        }
        System.out.println();
    }

    private static class Node<T>{
        private T data;
        private Node next;

        public Node(T data,Node next){
            this.data = data;
            this.next = next;
        }
    }
}
