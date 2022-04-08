package parimatch.project.gameintegrationservice.communicationmodels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class FailedQueryResponse {

  @JsonProperty("status")
  private final String status = "errors";

  @JsonProperty("message")
  private final String message;

  @JsonProperty("errors")
  private List<DetailedErrorInformation> information = new ArrayList<>();

  public FailedQueryResponse(int code, String message) {
    this.message = message;
    this.information.add(new DetailedErrorInformation(code));
  }
}
