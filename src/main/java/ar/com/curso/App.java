package ar.com.curso;


import java.util.ArrayList;

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



    }
}
