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

    private final Table<?> table;
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
        List<String> lista = this.getTableColumnNamesWithoutPk(table);
        return lista;
    }

    private List<String> getTableColumnNamesWithoutPk(Table<?> table){
        List<String> lista = table.getColumnNames().stream().filter(
                value -> !value.equals(this.table.getPkColumnNameUtil())
        ).collect(Collectors.toList());
        return lista;
    }

    public List<Table<?>> listAll(){
        return findOrList(null);
    }

    public List<Table<?>> find(Table<?> table){
        return findOrList(table);
    }

    /**
     * Metodo utilizado para listar ou persquisar.
     * Caso seja informado um table o objeto os seus atributos serão utilizados para
     * fazer condicional,
     * Os atributos do tipo String será feito por like os demais por igualdade.
     * @param table  objeto com os valores de busca
     * @return - lista de Table conforme parâmetros informados.
     */
    private List<Table<?>> findOrList(Table table) {
        String columnNames =this.getTableColumnsNamesForSelect(this.table.getColumnNames());
        String tableName = this.table.getTableName();
        String sql = "select " + columnNames + " from " + tableName ;
        // Gerar SQL de where " where colun1 = ? and colu=?
        String sqlWhere = getFindStringWhere(table);
        sql = sql + sqlWhere;

        // TODO Remover debug
        System.out.println("SQL: " + sql);

        List<Table<?>> list;
        try {
            PreparedStatement preparedStatement = this.db.prepareStatement(sql);

            // Responsavel por setar as variáveis.
            setPreparedStatmentForWhere(table, preparedStatement);

            ResultSet rs = preparedStatement.executeQuery();
            list = getListTableFromResultset(rs);
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
        return list;
    }

    private void setPreparedStatmentForWhere(Table table, PreparedStatement preparedStatement) throws SQLException {
        if(table!=null) {
            List<String> columnNamesWhere = table.getColumnNames();
            // System.out.println(columnNamesWhere);
            int psInd = 1;
            for(int i=1; i <= columnNamesWhere.size(); i++){
                String columnName = columnNamesWhere.get(i-1);
                Object value = table.getColumnValue(columnNamesWhere.get(i-1));
                // System.out.println("Value:"+columnNamesWhere.get(i-1)+"="+value+" psInd:"+psInd);
                if(value != null){
                    if(value instanceof  String && !columnName.equals(table.getPkColumnName())){
                        value = (String)value + "%";
                    }
                    // System.out.println("Value:"+columnNamesWhere.get(i-1)+"="+value);
                    preparedStatement.setObject(psInd++, value);
                }
            }
        }
    }

    private String getFindStringWhere(Table table) {
        String sqlWhere = "";
        if(table != null){
            List<String> columnWhere = table.getColumnNames();
            for (String columnName : columnWhere) {
                Object value = table.getColumnValue(columnName);
                if (value != null) {
                    if (value instanceof String && !columnName.equals(table.getPkColumnName())) {
                        sqlWhere += "and " + columnName + " like ? ";
                    } else {
                        sqlWhere += "and " + columnName + " = ? ";
                    }
                }
            }
            if(!sqlWhere.equals("")){
                sqlWhere = " where "+sqlWhere.substring(3);

            }
        }
        return sqlWhere;
    }

    private List<Table<?>> getListTableFromResultset(ResultSet rs) throws SQLException {
        List<Table<?>> list = new ArrayList<>();
        while(rs.next()){
            List<Object> valores = new ArrayList<>();
            for (String columnName : this.table.getColumnNames()) {
                valores.add(rs.getObject(columnName));
            }
            Table newTable = this.table.getNewTableObject();
            // TODO verificar posteriormente se faz um tratamento do erro
            // de configuração dos campos.
            newTable.setTableColumnValues(valores);
            list.add(newTable);
        }
        return list;
    }

    @Override
    public Return insert(Table<?> table) throws PersistenceException {
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
        ps = getPreparedStatementForAction(PersistenceAction.INSERT,sql);
        Return rt = setValuesPreparedstatementForInsert(table, ps);
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

    public Return delete(Table<?> table) throws PersistenceException {
        validFilledTable(table);
        Return ret = new Return(true,"Exclusão realizada com sucesso!");

        String tableName = table.getTableName();
        String pkName = table.getPkColumnName();
        String sql = "delete from "+tableName+ "" +
                " where "+pkName+" = ?";

        PreparedStatement ps;

        ps = getPreparedStatementForAction(PersistenceAction.DELETE,sql);
        Return rt = setPreparedStatementForDelete(table, ps);
        if (rt != null) return rt;

        int ok = 0;
        try {
            ok = ps.executeUpdate();
        } catch (SQLException e) {
           ret = new Return(false, "Erro ao excluír!",Arrays.asList(e.getMessage()));
           return ret;
        }
        if( ok!=1 ){
            ret = new Return (false, "Exclusão falhou, nenhum item encontrado!");
        }

        return ret;
    }

    protected Return setPreparedStatementForDelete(Table<?> table, PreparedStatement ps) {
        try {
            ps.setObject(1, table.getPk());
        }catch (SQLException e) {
            Return rt = new Return(
                    false,
                    "Erro ao Configurar valor da PK na tabela " +
                            table.getTableName()+ " no Campo: "+table.getPkColumnName()
                            + "Valor informado: "+table.getPk(), Arrays.asList(e.getMessage()));
            return rt;
        }
        return null;
    }

    /**
     * Metodo utilizado para atualizar um registro no banco de dados
     * O Modelo de ve ser enviado prenchido.
     * @param table
     * @return
     */
    public Return update(Table<?> table) throws PersistenceException {
        validFilledTable(table);
        String strColumForUpdate = getStrColumnForUpdate(table);
        String strPkName = table.getPkColumnName();

        String sql = "update "+table.getTableName()+ " "+
                " set " + strColumForUpdate +
                " where "+strPkName+" = ? " ;
        System.out.println("SQL Update: "+sql);

        PreparedStatement ps;

        ps = getPreparedStatementForAction(PersistenceAction.UPDATE,sql);

        Return rt = this.setValuesPreparedstatementForUpdate(table,ps);

        if (rt != null) return rt;

        try {
            int i = ps.executeUpdate();
        } catch (SQLException e) {
            return new Return(false, "Erro ao atualizar:", Arrays.asList(e.getMessage()));
        }

        rt = new Return(true, "Atualização relalizada com sucesso!");
        return rt;
    }

    private String getStrColumnForUpdate(Table table) {
        List<String> columnUpdates = this.getTableColumnNamesWithoutPk(table);
        String strColumForUpdate = "";
        for (String column : columnUpdates) {
            strColumForUpdate += ", "+column+" = ?";
        }
        strColumForUpdate = strColumForUpdate.substring(1);
        return strColumForUpdate;
    }

    private Return setValuesPreparedstatementForInsert(Table table, PreparedStatement ps) {
        // insert into fake_table(nome) values(?)
        // pk=10, nome="teste", telefone="234234"
        List<String> tableColumnNames = this.getTableColumnsNamesForInsert(table);
        return setValuesToTable(table, ps, tableColumnNames);
    }
    private Return setValuesPreparedstatementForUpdate(Table table, PreparedStatement ps) {
        List<String> tableColumnNames = this.getTableColumnNamesWithoutPk(table);
        try {
            ps.setObject(tableColumnNames.size()+1, table.getPk());
        } catch (SQLException e) {
            Return rt = new Return(
                    false,
                    "Erro ao Configurar Valores na tabela " +
                            table.getTableName()+ " no Campo: "+table.getPkColumnName()
                            + "Valor informado: "+table.getPk(), Arrays.asList(e.getMessage()));
            return rt;
        }
        return setValuesToTable(table, ps, tableColumnNames);
    }

    private Return setValuesToTable(Table table, PreparedStatement ps, List<String> tableColumnNames) {
        for(int i = 1; i<= tableColumnNames.size(); i++ ){
            String columnName = tableColumnNames.get(i-1);
            Object value = table.getColumnValue(columnName);
            try {
                ps.setObject(i,value);
            } catch (SQLException e) {
                Return rt = new Return(
                        false,
                        "Erro ao Configurar Valores na tabela " +
                                table.getTableName()+ " no Campo: "+columnName
                                + "Valor informado: "+value, Arrays.asList(e.getMessage()));
                return rt;
            }
        }
        return null;
    }

    private PreparedStatement getPreparedStatementForAction(PersistenceAction pa, String sql) throws PersistenceException {
        PreparedStatement ps;
        try {
            ps = this.db.prepareStatement(sql);
        } catch (SQLException e) {
           throw new PersistenceException(
                   pa,
                   "Erro ao Preparar SQL: "+ sql, e);
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

    /**
     * Metod para retornar o objeto preenchido, buscando pela PK do objeto informado
     * @param table - objeto com o atributo Pk preenchido.
     * @return objeto Com todos os atributos preenchidos pelo banco ou Null senão encontrar.
     */
    public Table<?> getByPk(Table<?> table){
        List<Table<?>> tables = this.find(table);
        if( tables.size()!= 1){
            return null;
        }

        return tables.get(0);
    }
}
