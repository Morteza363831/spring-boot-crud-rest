package org.example.springbootcrudrest.repository.inMem;

public interface CacheSynchronizer<T> {

    void initialize();
    void refresh();
    void clear();
    void add(T t);
    void remove(T t);
    void update(T t);
}
