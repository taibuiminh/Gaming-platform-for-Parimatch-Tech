package parimatch.project.simplewallet.DB.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.DB.dao.DAOStatus;
import parimatch.project.simplewallet.DB.dto.Status;

import java.sql.SQLException;
import java.util.List;

public class StatusService {
  private static final Logger logger = LoggerFactory.getLogger(StatusService.class);
  DAOStatus daoStatus = new DAOStatus();

  public Status getStatusById(int id) throws SQLException {
    logger.info("Get status with id {}", id);
    return daoStatus.get(id);
  }

  public Status updateStatus(Status newStatus) throws SQLException {
    logger.info("Update status with id {}", newStatus.getStatusId());
    daoStatus.update(newStatus);
    return newStatus;
  }

  public void deleteStatusById(int id) throws SQLException {
    logger.info("Delete status with id {}", id);
    daoStatus.delete(id);
  }

  public Status insertStatus(Status status) throws SQLException {
    logger.info("Insert status with id {}", status.getStatusId());
    daoStatus.save(status);
    return status;
  }

  public List<Status> getAllStatuses() throws SQLException {
    logger.info("Get all statuses");
    return daoStatus.getAll();
  }


}
