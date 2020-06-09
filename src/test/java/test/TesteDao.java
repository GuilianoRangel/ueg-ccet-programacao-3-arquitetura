package test;

import exceptions.PersistenceException;
import model.FakeTable;
import persistence.Dao;
import utils.Return;

public class TesteDao {
    public static void main(String[] args) {
        Dao d = testeListaAll();
        //testeInsert(d);
        //testeUpdate(d);
        FakeTable ftf = new FakeTable();
        ftf.setNome("N");
        ftf.setTelefone("%%%");;
        d.find(ftf);
    }

    private static void testeUpdate(Dao d) {
        FakeTable ftu = new FakeTable();
        ftu.setPk(10);
        ftu.setNome("Não sei nada!");

        try {
            Return rt = d.update(ftu);
            System.out.println(rt);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    private static Dao testeListaAll() {
        Dao d = new Dao(new FakeTable());
        System.out.println("Teste ListAll"+d.listAll());
        return d;
    }

    private static void testeInsert(Dao d) {
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
