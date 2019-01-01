package com.test.stack;

/**
 * 基于数组的栈数据结构实现
 */
public class StackArrayImpl<T> {

    private T[] items;
    private int count;//栈中元素的个数
    private int n;//栈的大小

    public StackArrayImpl(int n){
        this.items = (T[])new Object[n];
        this.n = n;
    }

    public boolean push(T item){
        //数组空间不足，直接返回失败，入栈失败
        if(count == n){
            return false;
        }
        //将item放在下标为count的位置，并且count ++；
        items[count] = item;

        count ++;
        return true;
    }

    public T pop(){
        if(count == 0){
            return null;
        }
        T t = items[count - 1];
        count --;
        return t;
    }

}
