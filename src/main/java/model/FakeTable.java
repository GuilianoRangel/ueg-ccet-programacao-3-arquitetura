package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public @Data class FakeTable extends Table{
    private String nome;
    private String telefone;

    @Override
    public List<String> getColumnNames() {
        List<String> colunas = new ArrayList<>();
        colunas.add("pk");
        colunas.add("nome");
        colunas.add("telefone");
        return colunas;
    }

    @Override
    public String getPkColumnName() {
        return "id";
    }

    /**
     * @return retornar true se deve utilizar a pk no insert, false caso não deva
     */
    @Override
    public boolean usePkInInsert() {
        return false;
    }

    @Override
    public String getTableName() {
        return "fake_table";
    }

    /**
     * criar uma nova instância do objeto tabela em questão.
     *
     * @return
     */
    @Override
    public Table getNewTableObject() {
        return new FakeTable();
    }

    /**
     * metodo recebe uma lista de object com valores das colunas na mesma
     * ordem retornada pela metodo getColumnNames()
     *
     * @param valores
     * @return
     */
    @Override
    public boolean setTableColumnValues(List<Object> valores) {
        this.setPk((Integer) valores.get(0));
        this.setNome((String) valores.get(1));
        this.setTelefone((String) valores.get(2));
        return false;
    }


}
