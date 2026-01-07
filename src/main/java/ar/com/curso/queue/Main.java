package ar.com.curso.queue;

public class Main {

    public static void main(String[] args) {
        Queue<Integer> numbers = new Queue<>();
        numbers.enqueue(10);
        numbers.enqueue(20);
        numbers.enqueue(30);

        numbers.print();
        System.out.println("Ver primero" + numbers.peek());
        System.out.println("Eliminar primero" + numbers.dequeue());
        numbers.print();

    }
}
