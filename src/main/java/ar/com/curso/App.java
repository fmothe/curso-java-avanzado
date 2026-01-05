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



    }



    // Esto solo funciona si no queremos hacer modificaciones
    //Si se quisiese modificar no se puede
    public static void printList(List<?> list){
        for(Object item : list){
            System.out.println(item);
        }


    }
}
