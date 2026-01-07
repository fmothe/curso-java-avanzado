package ar.com.curso;


import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App{
    public static void main( String[] args ){

        Box<String> stringBox = new Box<>("Federico");

        System.out.println(stringBox);

        Utility.printItem(10);

        Utility.printItem("gabriel", 2);

        List<String> names = new ArrayList<>();
        names.add("Federico");
        names.add("pepito");

        List<Integer> numbers = new ArrayList<>();
        numbers.add(10);
        numbers.add(20);

        printList(names);
        printList(numbers);

        sumNumbers(numbers);

        addNumbers(numbers);


    }



    // Esto solo funciona si no queremos hacer modificaciones
    //Si se quisiese modificar no se puede
    public static void printList(List<?> list){
        for(Object item : list){
            System.out.println(item);
        }
    }

    //Si quisese modificar se tiene que hacer de la siguiente forma
//    Para hacer una modificacion hay que usar super, sino no te permite hacer una covariancia
    public static void sumNumbers(List<? extends Number> numbers){
        double sum = 0;
        for(Number number : numbers){
            sum += number.doubleValue();
        }
        System.out.println(sum);

    }

    //super permite leer llamandolo con object.
    public static void addNumbers(List<? super Integer> numbers){
        numbers.add(5);
        numbers.add(20);
        numbers.add(30);

        Object num = numbers.get(0);
        System.out.println(num.getClass());
    }
}
