package persistence;

import model.FakeTable;

public class DaoFakeTable extends Dao<Integer, FakeTable> {
    public DaoFakeTable(FakeTable table) {
        super(table);
    }
}
