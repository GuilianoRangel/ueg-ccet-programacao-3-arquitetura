package model;

import lombok.Data;
import utils.Return;

import java.util.ArrayList;
import java.util.List;

public @Data class FakeTable extends Table<Integer>{
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
        return "pk";
    }

    /**
     * @return retornar true se deve utilizar a pk no insert, false caso não deva
     */
    @Override
    public boolean usePkInInsert() { return true;  }

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
    public Return setTableColumnValues(List<Object> valores) {
        Return ret = new Return(true, "sucesso");
        try {
            this.setPk((Integer) valores.get(0));
        }catch(Exception e){
            setReturnColumnError("pk", valores.get(0), ret);
        }
        try {
            this.setNome((String) valores.get(1));
        }catch(Exception e){
            setReturnColumnError("nome", valores.get(1), ret);
        }
        try {
            this.setTelefone((String) valores.get(2));
        }catch(Exception e){
            setReturnColumnError("telefone", valores.get(2), ret);
        }
        return ret;
    }

    /**
     * Retorna uma lista com os valores da tabela, na mesma
     * ordem retornada pela metodo getColumnNames()
     *
     * @param columnName
     * @return
     */
    @Override
    public Object getColumnValue(String columnName) {
        if(columnName.equals("pk")){
            return this.getPk();
        }else if(columnName.equals("nome")){
            return this.getNome();
        }else if(columnName.equals("telefone")){
            return this.getTelefone();
        }

        return null;
    }


}
