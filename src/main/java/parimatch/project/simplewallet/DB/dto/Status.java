package parimatch.project.simplewallet.DB.dto;

public class Status {
  Integer statusId;
  String stateName;
  String description;

  public Status(Integer statusId, String stateName, String description) {
    this.statusId = statusId;
    this.stateName = stateName;
    this.description = description;
  }

  public Integer getStatusId() {
    return statusId;
  }

  public String getStateName() {
    return stateName;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return "Status{" +
            "statusId=" + statusId +
            ", stateName='" + stateName + '\'' +
            ", description='" + description + '\'' +
            '}';
  }
}
