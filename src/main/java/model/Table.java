package model;

import exceptions.ProgrammerException;
import lombok.Data;
import utils.Return;

import java.util.List;

public abstract @Data class Table<TYPE> {
    public static final String ERRO_CONFIGURANDO_VALORES_TABELA = "Erro Configurando Valores Tabela!";
    public static final String ERRO_COLUNA = "Erro Coluna: ";
    public static final String ERRO_VALOR = ", valor: ";
    private TYPE pk;

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
    public abstract Return setTableColumnValues(List<Object> valores);

    /**
     * Retorna uma lista com os valores da tabela, na mesma
     * ordem retornada pela metodo getColumnNames()
     * @return
     */
    public abstract Object getColumnValue(String columnName);

    /**
     * @return retornar true se deve utilizar a pk no insert, false caso não deva
     */
    public abstract boolean usePkInInsert();

    /**
     * Método retorna o nome da coluna PK da tabela.
     * @return
     */
    public abstract String getPkColumnName();

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

    // Tratamentos de erro

    /**
     * Metodo utilizado para configura mensagem de erro ao configurar coluna
     * @param columnName - Nome da Coluna
     * @param value - Valor da coluna(Object) que será impresso com toString()
     * @param ret - Obejto Return que será configurado com a mensagem de erro.
     */
    protected void setReturnColumnError(String columnName, Object value, Return ret) {
        ret.setSucesso(false);
        ret.setMessage(ERRO_CONFIGURANDO_VALORES_TABELA);
        ret.getErros().add(ERRO_COLUNA + columnName + ERRO_VALOR + value);
    }
}
