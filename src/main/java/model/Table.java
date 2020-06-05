package model;

import exceptions.ProgrammerException;
import lombok.Data;

import java.util.List;

public abstract @Data class Table {
    private Integer pk;

    /**
     * Método deve retornar uma lista com o nome das colunas da tabela.
     * @return
     */
    public abstract List<String> getColumnNames();

    /**
     * Método deve retornar o nome
     * @return
     */
    public abstract String getTableName();

    /**
     * criar uma nova instância do objeto tabela em questão.
     * @return
     */
    public abstract Table getNewTableObject();

    /**
     * metodo recebe uma lista de object com valores das colunas na mesma
     * ordem retornada pela metodo getColumnNames()
     * @param valores
     * @return
     */
    public abstract boolean setTableColumnValues(List<Object> valores);

    /**
     * Retorna uma lista com os valores da tabela, na mesma
     * ordem retornada pela metodo getColumnNames()
     * @return
     */
    public abstract Object getColumnValue(String columnName);


    /**
     * Método retorna o nome da coluna PK da tabela.
     * @return
     */
    public abstract String getPkColumnName();

    /**
     * @return retornar true se deve utilizar a pk no insert, false caso não deva
     */
    public abstract boolean usePkInInsert();

    public String getPkColumnNameUtil(){
        List<String> columnNames = this.getColumnNames();
        boolean findPk = false;
        for (String columnName : columnNames) {
            if(columnName.equals(this.getPkColumnName())){
                findPk = true;
            }
        }
        if (!findPk) {
            throw  new ProgrammerException("ERRO: A pk retornada no Método getPkColumnName() " +
                    " da classe: "+this.getClass().getName()+
                    " Não está na lista de Colunas Retornada pelo método getColumnsNames()");
        }
        return this.getPkColumnName();
    }
}
