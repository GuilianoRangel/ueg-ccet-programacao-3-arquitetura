package persistence;

import exceptions.PersistenceException;
import model.Table;
import utils.Return;

import java.util.List;

public interface IDao {
    <T extends Table<?>> List<T> listAll(T table);
    <T extends Table<?>> List<T> find(T table);
    Return update(Table<?> table) throws PersistenceException;
    Return insert(Table<?> table) throws PersistenceException;
    Return delete(Table<?> table) throws PersistenceException;
    <T extends Table<?>> T getByPk(T table)throws PersistenceException;
}
