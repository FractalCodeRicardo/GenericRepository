


import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;


public class MySqlRepository<T> implements IRepository<T> {

    private Connection connection;
    private Class<T> type;
    private MySqlRepositorySQL sql;

    public MySqlRepository(Connection connection, Class<T> type) {
        this.connection = connection;
        this.type = type;
        this.sql = new MySqlRepositorySQL(type);
    }


    @Override
    public T findFirst(String where) throws Exception {
        String select = sql.select(where) + " limit 1";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(select);

        if (resultSet.next())
            return createEntity(resultSet);

        return null;
    }

    private List<T> createEntities(ResultSet resultSet) throws Exception {
        List<T> entities = new ArrayList<>();

        while (resultSet.next()) {
            T entity = createEntity(resultSet);

            if (entity != null)
                entities.add(entity);
        }

        return entities;
    }

    private T createEntity(ResultSet resultSet) throws Exception {
        HashMap<String, Field> mappedColumns = ReflectionUtils.getMappedFieldsColumns(type);
        Set<String> columnNames = mappedColumns.keySet();
        T entity = type.newInstance();

        for (String columnName : columnNames) {
            Field field = mappedColumns.get(columnName);
            Object value = resultSet.getObject(columnName);

            setValue(entity, field, value);
        }

        return entity;
    }

    private void setValue(T entity, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(entity, value);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public List<T> find(String where) throws Exception {
        String select = sql.select(where);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(select);

        return createEntities(resultSet);
    }

    @Override
    public List<T> all() throws Exception {
        return find("");
    }

    @Override
    public void insert(T entity) throws Exception {
        String insert = sql.insert();
        List<String> columns = ReflectionUtils.getColumnNames(type);
        PreparedStatement statement = createStatement(insert, entity, columns);
        statement.executeUpdate();
    }

    private PreparedStatement createStatement(String sql, T entity, List<String> columns) throws Exception {
        PreparedStatement statement = connection.prepareStatement(sql);
        setStatementValues(entity, statement,columns );
        return statement;
    }

    @Override
    public void update(T entity) throws Exception {
        String update = sql.update();
        List<String> columns = ReflectionUtils.getColumnNames(type);
        String id = ReflectionUtils.getColumnPrimaryKey(type);

        columns.remove(id);
        columns.add(id);

        PreparedStatement statement = createStatement(update, entity, columns);
        statement.executeUpdate();
    }

    private void setStatementValues(T entity,
                                    PreparedStatement statement,
                                    List<String> columns) throws Exception {

        int i = 1;
        for (String column : columns) {
            Field field = type.getDeclaredField(column);
            field.setAccessible(true);
            Object value = field.get(entity);

            statement.setObject(i, value);
            i++;
        }
    }

    @Override
    public void delete(T entity) throws Exception {
        String delete = sql.delete();
        String id = ReflectionUtils.getColumnPrimaryKey(type);
        List<String> columns = new ArrayList<>();
        columns.add(id);

        PreparedStatement statement = createStatement(delete, entity, columns);
        statement.executeUpdate();

    }

    @Override
    public void delete(String where) throws SQLException {
        String delete = sql.delete(where);
        connection.createStatement().execute(delete);
    }

    @Override
    public boolean any(String where) throws SQLException {
        String any = sql.any(where);
        ResultSet result = connection.createStatement().executeQuery(any);
        return result.next();
    }
}
