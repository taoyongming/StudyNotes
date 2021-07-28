package leetcode;

import java.util.Stack;

/**
 * DESCRIPTION :
 *Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
 *
 * push(x) -- Push element x onto stack.
 * pop() -- Removes the element on top of the stack.
 * top() -- Get the top element.
 * getMin() -- Retrieve the minimum element in the stack.
 *  
 * @author tym
 * @ceeate 2020/3/6
 **/
class MinStack {

    private Stack<Integer> data;
    private Stack<Integer> helper;

    /** initialize your data structure here. */
    public MinStack() {
       data = new Stack<Integer>();
       helper = new Stack<Integer>();
    }

    public void push(int x) {
       data.push(x);
       if(helper.isEmpty() || helper.peek() >= x) {
           helper.push(x);
       }else {
           helper.push(helper.peek());
       }
    }

    public void pop() {
        data.pop();
        helper.pop();
    }

    public int top() {
        if(!data.isEmpty()) {
            return  data.peek();
        }
        throw new RuntimeException("栈中元素为空，此操作非法");
    }

    public int getMin() {
        if(!data.isEmpty()) {
            return  helper.peek();
        }
        throw new RuntimeException("栈中元素为空，此操作非法");
    }
}