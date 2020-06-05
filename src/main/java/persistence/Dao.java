package persistence;

import exceptions.PersistenceException;
import model.Table;
import utils.Return;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Dao implements IDao{

    private final Table table;
    private Connection db;

    public Dao(Table table){
        this.table = table;
        this.db = persistence.conection.Connection.getIntance().getDb();
    }

    private String getTableColumnsNamesForSelect(List<String> lista){
        return String.join(",", lista);
    }

    private String getTableColumnsNamesForInsert(List<String> columnNames) {
        //se for para utilizar o pk no insert retornas todas as colunas
        if(this.table.usePkInInsert()){
            return String.join(",", columnNames);
        }

        // Filtra a lista tranzendo removendo a pk da lista de colunas.
        List<String> lista = columnNames.stream().filter(
                    value -> !value.equals(this.table.getPkColumnNameUtil())
        ).collect(Collectors.toList());

        return String.join(",", columnNames);
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
          throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Return insert(Table table) throws PersistenceException {
        validFilledTable(table);

        String tableName = table.getTableName();
        String tableColumnName = this.getTableColumnsNamesForInsert(table.getColumnNames());

       /* tipo (id,nome)
                insert table tipo(nome) value('nome');*/

        String sql = "insert into " +
                tableName + " ("+tableColumnName+") " +
                " values(?,?)";


        return null;
    }



    private void validFilledTable(Table table) throws PersistenceException {
        if(table == null){
            throw new PersistenceException(
                    PersistenceAction.INSERT,
                    "Tabela de dados não informada.");
        }
    }

}
