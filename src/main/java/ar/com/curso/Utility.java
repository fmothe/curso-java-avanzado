package ar.com.curso;

public class Utility {

    public static <T> void printItem(T value){
//        Si la clase fuese generica uno no necesita aclarar <T>
//        pero como la clase no es generica se tiene que especificar
        System.out.println(value);
    }

    public static <T, K> void printItem(T value, K value2){
        System.out.println(value + " " + value2);
    }
}
