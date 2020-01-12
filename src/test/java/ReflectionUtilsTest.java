import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ReflectionUtilsTest {

    @Test
    public void getTableNameReturnAnnotation() {
        String tableName = ReflectionUtils.getTableName(EntityTest.class);
        if (!tableName.equals("EntityTest"))
            Assert.fail("El método no devuelve bien el nombre de la tabla");
    }


    @Test
    public void getMappedFieldTest(){
        HashMap<String, Field>  maps = ReflectionUtils.getMappedFieldsColumns(EntityTest.class);

        if(!maps.containsKey("id"))
            Assert.fail("El método no devuelve bien las columnas mapeadas.");

        if(!maps.containsKey("description"))
            Assert.fail("El método no devuelve bien las columnas mapeadas.");
    }






}
