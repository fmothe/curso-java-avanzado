package ar.com.curso.datastore;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        DataStore<User, String> userStore = new DataStore<>();
        try{
            System.out.println("Añadiendo usuarios...");
            userStore.add("123", new User("123", "Federico"));
            userStore.add("1234", new User("1234", "Ana"));
            userStore.add("12345", new User("1235", "Maria"));


        }catch(NullPointerException e){
            System.out.println(e.getMessage());
        }

        List<User> users = userStore.getAll();
        for(User user : users){
            System.out.println(user);
        }
    }
}
