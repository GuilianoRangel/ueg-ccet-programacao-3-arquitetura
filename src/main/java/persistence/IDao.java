package persistence;

import exceptions.PersistenceException;
import model.Table;
import utils.Return;

import java.util.List;

public interface IDao {
    List<Table> listAll();
    Return insert(Table table) throws PersistenceException;
}
