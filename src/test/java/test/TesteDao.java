package test;

import model.FakeTable;
import persistence.Dao;

public class TesteDao {
    public static void main(String[] args) {
        Dao d = new Dao(new FakeTable());
        d.listAll();
    }
}
