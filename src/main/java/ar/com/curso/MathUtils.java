package ar.com.curso;



public class MathUtils {
//    Para restringir los tipos de genericos se puede hacer de la siguiente forma
//    <T extends Number> Fuerza a que el generico extiendan de la clase padre
    public static <T extends Number> double sum(T a, T b){
        return a.doubleValue()+ b.doubleValue();
    }
}
