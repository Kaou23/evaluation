package ma.projet.dao;

import java.util.List;

public interface IDao<T> {
    void create(T o);
    T getById(int id);
    List<T> getAll();
    void update(T o);
    void delete(T o);
}