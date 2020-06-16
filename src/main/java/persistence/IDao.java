package persistence;

import exceptions.PersistenceException;
import model.Table;
import utils.Return;

import java.util.List;

public interface IDao {
    List<Table<?>> listAll();
    List<Table<?>> find(Table<?> table);
    Return update(Table<?> table) throws PersistenceException;
    Return insert(Table<?> table) throws PersistenceException;
    Return delete(Table<?> table) throws PersistenceException;
}
