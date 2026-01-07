package ar.com.curso.list;

public class LinkedList<T> {
    private Node<T> head;

    public void add(T data){
        Node<T> newNode = new Node<>(data);
        if(head==null){
            head = newNode;
        }else{
            Node<T> current = head;
            while(current.next!=null){
                current = current.next;
            }
            current.next = newNode;

        }
    }

    public void printList(){
        Node<T> current = head;
        while(current!=null){
            System.out.println("Node: "+ current.data);
            current = current.next;
        }
    }

    public void update(T old, T node){
        Node<T> current = head;
        while(current!=null){
            if(current.data.equals(old)){
                current.data = node;
                return;
            }
            current = current.next;
        }
    }

    public void delete(T data){
//        Si no tenemos cabecera
        if(head == null) return;
//        Si el elemento a eliminar es el primer elemento de la lista
        if(head.data.equals(data)){
            head = head.next;
            return;
        }

        Node<T> current = head.next;
        Node<T> previous = head;

        while(current!= null && !current.data.equals(data)){
            previous = current;
            current = current.next;
        }
        if(current == null) return;

        previous.next = current.next;
    }
}
