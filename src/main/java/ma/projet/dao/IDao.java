package ma.projet.dao;
import java.util.List;
public interface IDao<T> {
    T create(T entity);
    T update(T entity);
    void delete(T entity);
    T findById(int id);
    List<T> findAll();
}
