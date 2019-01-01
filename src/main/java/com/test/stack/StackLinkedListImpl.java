package com.test.stack;

public class StackLinkedListImpl<T> {

    private Node top;

    public void push(T t){
        Node newNode = new Node(t,null);
        if(top == null) {
            top = newNode;
        }else {
            newNode.next = top;
            top = newNode;
        }
    }

    public T pop(){
        if(top == null){
            return null;
        }
        T value = (T)top.data;
        top = top.next;
        return value;
    }

    public void printAll(){
        Node p = top;
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
