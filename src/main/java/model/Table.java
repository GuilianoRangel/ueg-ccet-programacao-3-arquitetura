package model;

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


}
