package persistence;

import exceptions.PersistenceException;
import model.Table;
import utils.Return;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

    private List<String> getTableColumnsNamesForInsert(Table table) {
        List<String> columnNames = table.getColumnNames();
        //se for para utilizar o pk no insert retornas todas as colunas
        if(table.usePkInInsert()){
            return columnNames;
        }
        // Filtra a lista tranzendo removendo a pk da lista de colunas.
        List<String> lista = columnNames.stream().filter(
                    value -> !value.equals(this.table.getPkColumnNameUtil())
        ).collect(Collectors.toList());
        return lista;
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
        List<String> tableColumnNames = this.getTableColumnsNamesForInsert(table);
        String strTableColumnNames = String.join(",", tableColumnNames);

        String tableStrColumnValues = getTableStrColumnsForInsert(table);

        String sql = "insert into " +
                tableName + " ("+strTableColumnNames+") " +
                " values("+tableStrColumnValues+")";
        System.out.println("SQL Insert: "+sql);
        PreparedStatement ps;

        ps = getPreparedStatementForInsert(sql);

        Return rt = setValoresPreparedstatementForInsert(table, ps);
        if (rt != null) return rt;

        try {
            int i = ps.executeUpdate();
        } catch (SQLException e) {
            //TODO adicionar tratamentos para chaves estrangeira
            String mens = e.getMessage();
            if(mens.contains("duplicate key")){
                return  new Return(
                        false,
                        "Erro ao Inserir, já existe uma registro com o identificador "+table.getPk(), Arrays.asList(e.getMessage()));
            }
            return new Return(false, "Erro ao Inserir:", Arrays.asList(e.getMessage()));
        }


        rt = new Return(true, "Inserção relalizada com sucesso!");
        return rt;
    }

    private Return setValoresPreparedstatementForInsert(Table table, PreparedStatement ps) {
        // insert into fake_table(nome) values(?)
        // pk=10, nome="teste", telefone="234234"
        List<String> tableColumnNames = this.getTableColumnsNamesForInsert(table);
        for(int i = 1; i<= tableColumnNames.size(); i++ ){
            String columnName = tableColumnNames.get(i-1);
            Object value = table.getColumnValue(columnName);
            try {
                ps.setObject(i,value);
            } catch (SQLException e) {
                Return rt = new Return(
                        false,
                        "Erro ao Configurar Valores para Inserção na tabela " +
                                table.getTableName()+ " no Campo: "+columnName
                                + "Valor informado: "+value, Arrays.asList(e.getMessage()));
                return rt;
            }
        }
        return null;
    }

    private PreparedStatement getPreparedStatementForInsert(String sql) throws PersistenceException {
        PreparedStatement ps;
        try {
            ps = this.db.prepareStatement(sql);
        } catch (SQLException e) {
           throw new PersistenceException(
                   PersistenceAction.INSERT,
                   "Erro ao Preparar SQL para Inserção: "+ sql, e);
        }
        return ps;
    }

    /**
     * Metodo utilizado para retornar uma string no forma ?,?,... para as colunas que deverão
     * ser inseridas na SQL de insert.
     * @param table
     * @return
     */
    private String getTableStrColumnsForInsert(Table table) {
        List<String> colunas = this.getTableColumnsNamesForInsert(table);
        String tableColumnValues = "";
        for (String coluna : colunas) {
            tableColumnValues += ",?";
        }
        tableColumnValues = tableColumnValues.substring(1);
        return tableColumnValues;
    }


    private void validFilledTable(Table table) throws PersistenceException {
        if(table == null){
            throw new PersistenceException(
                    PersistenceAction.INSERT,
                    "Tabela de dados não informada.");
        }
    }

}
