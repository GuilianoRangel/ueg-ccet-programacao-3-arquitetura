package model;

import lombok.Data;

import java.util.List;

public abstract @Data class Table {
    private Long pk;

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

}
