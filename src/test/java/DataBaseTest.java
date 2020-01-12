import java.sql.*;
import java.util.List;

public class DataBaseTest {

    private Connection connection;

    public DataBaseTest(Connection connection) {
        this.connection = connection;
    }


    public void initDataBase(){
        createTable();
    }


    private void createTable(){
        try {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS EntityTest;");
            statement.execute("CREATE TABLE EntityTest(id int AUTO_INCREMENT, description varchar(255) NOT NULL, PRIMARY KEY (id));");
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void insertRecords(List<EntityTest> entries){
        if(entries == null || entries.size()<1)
            return;

        try {

            for (EntityTest entity: entries)
                insertRecord((entity));

        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private void insertRecord(EntityTest entry) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO EntityTest (id, description) VALUES(?,?)",  Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, entry.getId());
        statement.setString(2, entry.getDescription());

        statement.executeUpdate();

        ResultSet generatedKeys = statement.getGeneratedKeys();

        if(generatedKeys.next())
            entry.setId(generatedKeys.getLong(1));

    }
}
