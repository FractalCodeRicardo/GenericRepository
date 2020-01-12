import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

public interface IRepository<T> {

    T findFirst(String where) throws Exception;
    List<T> find(String where) throws Exception;
    List<T> all() throws Exception;
    void insert(T entity) throws Exception;
    void update(T entity) throws Exception;
    void delete(T entity) throws Exception;
    void delete(String where) throws SQLException;
    boolean any(String where) throws SQLException;

}
