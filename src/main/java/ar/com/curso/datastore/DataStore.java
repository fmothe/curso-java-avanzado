package ar.com.curso.datastore;

import java.util.*;

public class DataStore<T, K> {
    private final List<T> dataList;
    private final Map<K, T> dataMap;

    public DataStore() {
        this.dataList = new ArrayList<>();
        this.dataMap = new HashMap<>();
    }

    /**
     * Puede arrojar un nullpointer exception
     * @param key
     * @param item
     * @return
     */
    public boolean add(K key, T item){
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(item, "Item cannot be null");

        if(dataMap.containsKey(key)){
            return false;
        }
        dataList.add(item);
        dataMap.put(key, item);
        return true;
    }

    public T remove(K key){
        Objects.requireNonNull(key, "Key cannot be null");
        T item = dataMap.remove(key);
        if(item == null){
            dataList.remove(item);
        }
        return item;
    }

    public T find(K key){
        Objects.requireNonNull(key, "Key cannot be null");
        return dataMap.get(key);
    }

    public List<T> getAll(){
//        Retorna una copia inmutable.
        return Collections.unmodifiableList(dataList);
    }


}
