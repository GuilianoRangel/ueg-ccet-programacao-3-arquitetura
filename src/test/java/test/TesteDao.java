package test;

import exceptions.PersistenceException;
import model.FakeTable;
import persistence.Dao;
import utils.Return;

public class TesteDao {
    public static void main(String[] args) {
        Dao d = new Dao(new FakeTable());
        System.out.println(d.listAll());
        FakeTable ft = new FakeTable();
        ft.setPk(10);
        ft.setNome("Coisa");
        try {
            Return rt = d.insert(ft);
            System.out.println(rt);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }
}
