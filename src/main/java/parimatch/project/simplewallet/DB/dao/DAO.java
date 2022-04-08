package parimatch.project.simplewallet.DB.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, S> {

  T get(S id) throws SQLException;

  List<T> getAll() throws SQLException;

  void save(T t) throws SQLException;

  T update(T t) throws SQLException;

  void delete(int id) throws SQLException;
}
