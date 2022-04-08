package parimatch.project.gameintegrationservice.communicationmodels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DetailedErrorInformation {

  @JsonProperty("code")
  private final String code;

  @JsonProperty("message")
  private final String message;

  public DetailedErrorInformation(int code) {
    switch (code) {
      case 801:
        this.message = "Unexpected error";
        break;
      case 802:
        this.message = "Round is finished and cannot be operated upon";
        break;
      case 803:
        this.message = "Win and bet have the same txIds!";
        break;
      case 810:
        this.message = "Security key mismatch";
        break;
      case 811:
        this.message = "The amount for the transaction is negative";
        break;
      case 812:
        this.message = "Transaction currency doesn't match player's";
        break;
      case 820:
        this.message = "Wrong player Id.";
        break;
      case 821:
        this.message = "A player is blocked";
        break;
      case 831:
        this.message = "Insufficient funds";
        break;
      case 833:
        this.message = "Bet already settled";
        break;
      case 840:
        this.message = "Bet with specified ID not found";
        break;
      default:
        this.message = "";
    }

    if (code == 802 || code == 811 || code == 812) {
      this.code = String.valueOf(801);
    } else {
      this.code = String.valueOf(code);
    }
  }
}
