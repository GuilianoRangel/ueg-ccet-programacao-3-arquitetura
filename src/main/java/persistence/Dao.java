package persistence;

import model.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Dao {

    private final Table table;
    private Connection db;

    public Dao(Table table){
        this.table = table;
        this.db = persistence.conection.Connection.getIntance().getDb();
    }

    private String getTableColumnsNamesForSelect(List<String> lista){
        return String.join(",", lista);
    }
    public List<Table> listAll(){
        String columnNames =this.getTableColumnsNamesForSelect(this.table.getColumnNames());
        String tableName = this.table.getTableName();
        String sql = "select " + columnNames + " from " + tableName ;

        // TODO Remover debug
        System.out.println("SQL: " + sql);

        List<Table> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = this.db.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                List<Object> valores = new ArrayList<>();

                for (String columnName : this.table.getColumnNames()) {
                    valores.add(rs.getObject(columnName));
                }

                Table newTable = this.table.getNewTableObject();

                newTable.setTableColumnValues(valores);

                list.add(newTable);
            }

        } catch (SQLException e) {
            // TODO fazer o tratamento
            e.printStackTrace();
        }


        return list;
    }
}
