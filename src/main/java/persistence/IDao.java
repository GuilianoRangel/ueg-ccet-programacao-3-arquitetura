package persistence;

import exceptions.PersistenceException;
import model.Table;
import utils.Return;

import java.util.List;

public interface IDao<TYPE, TABLE extends Table<TYPE>> {
    List<TABLE> listAll();
    List<TABLE> find(TABLE table);
    Return update(TABLE table) throws PersistenceException;
    Return insert(TABLE table) throws PersistenceException;
    Return delete(TABLE table) throws PersistenceException;
    TABLE getByPk(TABLE table)throws PersistenceException;
}
