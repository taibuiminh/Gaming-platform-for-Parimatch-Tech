package parimatch.project.gameintegrationservice.utilprocessors.gameflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameActivity {

  Integer txId;
  String typeTransaction;

  public GameActivity() {
  }

  public Integer getTxId() {
    return txId;
  }

  public String getTypeTransaction() {
    return typeTransaction;
  }

  public void setTypeTransaction(String typeTransaction) {
    this.typeTransaction = typeTransaction;
  }

  @JsonProperty("transaction")
  private void unpackNestedJson(Map<String, String> transaction) {
    this.txId = Integer.parseInt(transaction.get("txId"));
  }

  @Override
  public String toString() {
    return "GameActivity{" +
            "txId=" + txId +
            ", typeTransaction='" + typeTransaction + '\'' +
            '}';
  }
}