package parimatch.project.gameintegrationservice.communicationmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;

public class SuccessfulQueryResponse {

  @JsonProperty("status")
  private final String status = "success";

  @JsonRawValue
  @JsonProperty("data")
  private final String data;

  public SuccessfulQueryResponse(String data) {
    this.data = data;
  }

  public String getStatus() {
    return status;
  }

  public String getData() {
    return data;
  }
}
