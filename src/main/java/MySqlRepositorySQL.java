

import java.util.List;

public class MySqlRepositorySQL {


    private Class<?> type;

    public MySqlRepositorySQL(Class<?> type) {
        this.type = type;
    }

    public String select(){
        return select(null, null);
    }

    public String select(String condition) {
        return  select(condition, null);
    }

    public String select(String condition, String selectFields){
        String table = ReflectionUtils.getTableName(type);

        condition = fixString(condition);
        selectFields = fixString((selectFields));

        if (!condition.isEmpty())
            condition = " WHERE " + condition;

        if(selectFields.isEmpty())
            selectFields = "*";

        return "SELECT " + selectFields + " FROM " + table + condition;
    }

    private String fixString(String string){
        string = string == null ? "" : string;
        string = string.trim();

        return string;
    }

    public String insert(){
        String table = ReflectionUtils.getTableName(type);
        List<String> columnNames = ReflectionUtils.getColumnNames(type);
        String columns = String.join(",", columnNames);
        String values = valuesString(columnNames.size());
        String insert = "INSERT INTO " + table + "(" + columns + ") VALUES" + values;

        return insert;
    }

    private String valuesString(int size){
        if(size<1)
            return "";

        String values = "(";
        for (int i = 0; i < size-1  ; i++)
            values = values +  "?" + ",";

        values = values + "?)";

        return values;
    }

    public String update(){
        String table = ReflectionUtils.getTableName(type);
        List<String> columnNames = ReflectionUtils.getColumnNames(type);
        String id = ReflectionUtils.getColumnPrimaryKey(type);

        columnNames.remove(id);

        for (int i = 0; i < columnNames.size() ; i++) {
            String setColumn = columnNames.get(i) + "=?";
            columnNames.set(i, setColumn);
        }
        String updateFields = String.join(",", columnNames);
        String update = "UPDATE " + table + " SET " + updateFields + " WHERE " + id + "=?";

        return update;
    }


    public String delete(){
        String table = ReflectionUtils.getTableName(type);
        String id = ReflectionUtils.getColumnPrimaryKey(type);

        return "DELETE FROM " + table + " WHERE " + id + "=?";
    }

    public String delete(String where){
        String table = ReflectionUtils.getTableName(type);
        return "DELETE FROM " + table + " WHERE " + where;
    }

    public String any(String where){
        String table = ReflectionUtils.getTableName(type);

        return "SELECT 1 FROM " + table + " WHERE " + where + " limit 1";
    }



}
