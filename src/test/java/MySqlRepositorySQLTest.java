import org.junit.Assert;
import org.junit.Test;

public class MySqlRepositorySQLTest {

    @Test
    public void selectReturnsSelectStatement(){
        MySqlRepositorySQL sql =  new MySqlRepositorySQL(EntityTest.class);
        String expectedResult = "SELECT * FROM EntityTest".toLowerCase();

        String result = sql.select("").toLowerCase();

        if(!expectedResult.equals(result))
            Assert.fail("El método no devuelve el resultado esperado.");
    }

    @Test
    public void selectReturnsSelectTop1Statement(){
        MySqlRepositorySQL sql =  new MySqlRepositorySQL(EntityTest.class);
        String expectedResult = "SELECT top 1 * FROM EntityTest".toLowerCase();
        String result = sql.select("", "top 1 *").toLowerCase();
        if(!expectedResult.equals(result))
            Assert.fail("El método no devuelve el resultado esperado.");
    }

    @Test
    public void insertTest(){
        MySqlRepositorySQL sql = new MySqlRepositorySQL(EntityTest.class);
        String expectedResult= "insert into entitytest(id,description) values(?,?)";
        String result = sql.insert();

        if(!expectedResult.equals(result.toLowerCase()))
            Assert.fail("El método no devuelve el resultado esperado");
    }


    @Test
    public void updateTest(){
        MySqlRepositorySQL sql = new MySqlRepositorySQL(EntityTest.class);
        String expectedResult= "update entitytest set description=? where id=?";
        String result = sql.update();

        if(!expectedResult.equals(result.toLowerCase()))
            Assert.fail("El método no devuelve el resultado esperado");
    }


    @Test
    public void deleteTest(){
        MySqlRepositorySQL sql = new MySqlRepositorySQL(EntityTest.class);
        String expectedResult= "delete from entitytest where id=?";
        String result = sql.delete();

        if(!expectedResult.equals(result.toLowerCase()))
            Assert.fail("El método no devuelve el resultado esperado");
    }
}
