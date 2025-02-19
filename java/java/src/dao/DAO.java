// DAO.java
package dao;

import java.util.List;

public interface DAO<T> {
    boolean create(T obj);
    T read(int id);
    boolean update(T obj);
    boolean delete(int id);
    List<T> findAll();
}
