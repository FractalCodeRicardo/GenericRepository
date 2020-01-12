
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlRepositoryTest {

    private Connection connection;
    private DataBaseTest db;
    private List<EntityTest> entities;


    @Before
    public void initializeDataBase() {
        this.connection = this.createConnection();
        this.db = new DataBaseTest(connection);
        this.db.initDataBase();
        this.entities =getEntries();
        this.db.insertRecords(entities);
    }


    private List<EntityTest> getEntries() {
        List<EntityTest> entries = new ArrayList<>();
        entries.add(new EntityTest(0, "Entidad1"));
        entries.add(new EntityTest(0, "Entidad2"));
        entries.add(new EntityTest(0, "Entidad3"));
        entries.add(new EntityTest(0, "Entidad4"));

        return entries;
    }


    private Connection createConnection() {
        String url = "jdbc:mysql://localhost:3306/MySqlRepositoryTest?useSSL=false";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Test
    public void findFirstReturnMatch() {
        try {
            MySqlRepository<EntityTest> repository = new MySqlRepository<>(this.connection, EntityTest.class);
            EntityTest entity = repository.findFirst("");

            if (entity == null || !entity.getDescription().equals("Entidad1"))
                Assert.fail("El método no devuelve el resultado esperado.");

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void findFirstReturnWhereMatch() {

        MySqlRepository<EntityTest> repository = new MySqlRepository<>(this.connection, EntityTest.class);
        try {
            EntityTest entity = repository.findFirst("description='Entidad2'");

            if (entity == null || !entity.getDescription().equals("Entidad2"))
                Assert.fail("El método no devuelve el resultado esperado.");

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void findReturnWhereMatch() {
        MySqlRepository<EntityTest> repository = new MySqlRepository<>(this.connection, EntityTest.class);
        try {
            List<EntityTest> list= repository.find("description='Entidad2'");

            if (list == null || !list.get(0).getDescription().equals("Entidad2"))
                Assert.fail("El método no devuelve el resultado esperado.");

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void insertTest(){
        try{
            EntityTest entityTest = new EntityTest(0, "Temp");
            MySqlRepository<EntityTest> repository = new MySqlRepository<>(this.connection, EntityTest.class);
            repository.insert(entityTest);

            ResultSet rs = connection.
                    createStatement().
                    executeQuery("Select 1 from EntityTest where description='Temp'");

            if(!rs.next())
                Assert.fail("El método no devuelve el resultado esperado.");
        }
        catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }


    @Test
    public void updateTest(){
        try{
            EntityTest entityTest = this.entities.get(0);
            entityTest.setDescription("modified");

            MySqlRepository<EntityTest> repository = new MySqlRepository<>(this.connection, EntityTest.class);
            repository.update(entityTest);

            ResultSet rs = connection.
                    createStatement().
                    executeQuery("Select 1 from EntityTest where description='modified'");

            if(!rs.next())
                Assert.fail("El método no devuelve el resultado esperado.");
        }
        catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }


    @Test
    public void deleteTest(){
        try{
            EntityTest entityTest = this.entities.get(0);
            MySqlRepository<EntityTest> repository = new MySqlRepository<>(this.connection, EntityTest.class);
            repository.delete(entityTest);

            ResultSet rs = connection.
                    createStatement().
                    executeQuery("Select 1 from EntityTest where id=" + entityTest.getId());

            if(rs.next())
                Assert.fail("El método no devuelve el resultado esperado.");
        }
        catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }


    @Test
    public void deleteWhereTest(){
        try{
            MySqlRepository<EntityTest> repository = new MySqlRepository<>(this.connection, EntityTest.class);
            repository.delete("1=1");
            ResultSet rs = connection.
                    createStatement().
                    executeQuery("Select 1 from EntityTest where 1=1");

            if(rs.next())
                Assert.fail("El método no devuelve el resultado esperado.");
        }
        catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void anyTrue(){
        try{
            MySqlRepository<EntityTest> repository = new MySqlRepository<>(this.connection, EntityTest.class);

            if(!repository.any("1=1"))
                Assert.fail("El método debe devolver verdadero");

        }
        catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }


    @Test
    public void anyMatch(){
        try{
            MySqlRepository<EntityTest> repository = new MySqlRepository<>(this.connection, EntityTest.class);

            if(!repository.any("id=2"))
                Assert.fail("El método debe devolver verdadero");

        }
        catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }



    @Test
    public void anyFalse(){
        try{
            MySqlRepository<EntityTest> repository = new MySqlRepository<>(this.connection, EntityTest.class);

            if(repository.any("0=1"))
                Assert.fail("El método debe devolver falso");

        }
        catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }



}
