package ar.com.curso.stack;

import java.util.ArrayList;
import java.util.List;

public class Stack<T> {

    private final List<T> elements = new ArrayList<>();


    public void push(T data){
        elements.add(data);
        System.out.println("Pushed: "+data);
    }

    public boolean isEmpty(){
        return elements.isEmpty();
    }

    public T peek(){
        if(isEmpty()) throw new IllegalStateException("Stack is empty");
        return elements.getLast();
    }

    public T pop(){
        if(isEmpty()) throw new IllegalStateException("Stack is empty");
        return elements.removeLast();
    }

    public void print(){
        for(Object o: elements){
            System.out.println(o);
        }
    }
}
