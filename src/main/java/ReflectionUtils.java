import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReflectionUtils {


    public static <T> String getTableName(Class<T> tClass) {
        Table table = tClass.getAnnotation(Table.class);

        if (table == null)
            tClass.getName();

        return table.name();
    }


    public static <T> HashMap<String, Field> getMappedFieldsColumns(Class<T> tClass) {
        HashMap<String, Field> map = new HashMap<>();
        Field[] fields = tClass.getDeclaredFields();

        for (Field field : fields) {
            String columnName = getColumnName(field);

            if(columnName != null && !columnName.isEmpty())
                map.put(columnName, field);
        }

        return map;
    }


    public static <T> List<String> getColumnNames(Class<?> tClass){
        Field[] fields = tClass.getDeclaredFields();
        List<String> list =  new ArrayList<>();
        for (Field field : fields) {
            String columnName = getColumnName(field);

            if(columnName != null && !columnName.isEmpty())
                list.add(columnName);
        }
        return list;
    }

    private static String getColumnName(Field field){
        Column column = field.getAnnotation(Column.class);
        Id id = field.getAnnotation(Id.class);

        if (column == null && id==null)
            return null;

        String columnName = "";
        if(column != null && !column.name().isEmpty())
            columnName = column.name();
        else
            columnName = field.getName();

        return columnName;
    }


    public static String getColumnPrimaryKey(Class<?> tClass){
        Field[] fields = tClass.getDeclaredFields();

        for(Field f: fields){
            Id id = f.getAnnotation(Id.class);

            if(id!=null){
                return getColumnName(f);
            }
        }

        return "";
    }


}
