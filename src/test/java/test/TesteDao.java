package test;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import exceptions.PersistenceException;
import model.FakeTable;
import model.Table;
import persistence.Dao;
import persistence.IDao;
import utils.Return;

import java.util.List;

public class TesteDao {
    public static void main(String[] args) {
        Dao d = testeListaAll();
        //testeInsert(d);
        //testeUpdate(d);
        testeFind(d);
        testeDelete(d);
        testeGetByid(d);
    }

    protected static void testeGetByid(Dao d) {
        FakeTable ft = new FakeTable();
        ft.setPk(11);
        Table<?> table = d.getByPk(ft);
        FakeTable ft2 = (FakeTable) table;
        //System.out.println(ft2.getNome());
        System.out.println(table);
    }

    protected static void testeDelete(Dao d) {
        FakeTable ft = new FakeTable();
        ft.setPk(11);


        try {
            Return ret = d.delete(ft);
            System.out.println(ret);
        } catch (PersistenceException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    protected static void testeFind(Dao d) {
        FakeTable ftf = new FakeTable();
        ftf.setNome("T");
        //ftf.setTelefone("%%%");;
        List<Table<?>> tables = d.find(ftf);
        System.out.println(tables);

        FakeTable ft2 = new FakeTable();
        ft2.setPk(2);
        System.out.println(d.find(ft2));
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
        Dao d = new Dao();
        System.out.println("Teste ListAll"+d.listAll(new FakeTable()));
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
