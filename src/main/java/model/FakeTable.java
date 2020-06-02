package model;

import java.util.ArrayList;
import java.util.List;

public class FakeTable extends Table{
    @Override
    public List<String> getColumnNames() {
        List<String> colunas = new ArrayList<>();
        colunas.add("pk");
        colunas.add("nome");
        colunas.add("telefone");
        return colunas;
    }

    @Override
    public String getTableName() {
        return "fake_table";
    }
}
