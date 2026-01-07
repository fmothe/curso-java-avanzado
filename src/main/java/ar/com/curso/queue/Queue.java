package ar.com.curso.queue;


import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Queue<T> {

    private LinkedList<T> elements = new LinkedList<>();


    public void enqueue(T data){
        elements.addLast(data);
        System.out.println("Enqueued: "+data);
    }

    public boolean isEmpty(){
        return elements.isEmpty();
    }

    public T peek(){
//      Esta exception indica que se intento acceder al elemento pero no se encontro
        if(isEmpty()) throw new NoSuchElementException("Queue is empty");
        return elements.getFirst();
    }

    public T dequeue(){
        if(isEmpty()) throw new NoSuchElementException("Queue is empty");
        return elements.removeFirst();
    }

    public void print(){
        for(Object o: elements){
            System.out.println(o);
        }
    }
}
