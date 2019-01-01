package com.test.queue;

public class QueueArrayImpl<T> {
    private T[] items;
    private int n = 0;
    private int head;
    private int tail;

    public QueueArrayImpl(int capacity){
        items = (T[])new Object[capacity];
        n = capacity;
    }

    public boolean enqueue(T item){
        if(tail == n){
            return false;
        }else{
            items[tail] = item;
            tail ++;
            return true;
        }

    }

    public T dequeue(){
        if(head == tail){
            return null;
        }
        T t = items[head];
        head ++;
        return t;
    }
}
