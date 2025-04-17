package org.example.springbootcrudrest.repository.cachesync;

import java.util.List;

public interface CacheSynchronizer<T> {

    void initialize();
    void refresh();
    void clear();
    void add(T t);
    void remove(T t);
    void update(T t);
    List<T> getAll();
}
