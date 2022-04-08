package parimatch.project.simplewallet.DB.dto;

import java.sql.Timestamp;

public class Player {
  Integer playerId;
  Timestamp registrationDate;
  Integer status;
  Integer country;
  Double balance;
  String currency;

  public Player(Integer playerId, long registrationDate, Integer status, Integer country, Double balance,
                String currency) {
    this.playerId = playerId;
    this.registrationDate = new Timestamp(registrationDate);
    this.status = status;
    this.country = country;
    this.balance = balance;
    this.currency = currency;

  }

  public Player(Integer playerId, Timestamp registrationDate, Integer status, Integer country, Double balance,
                String currency) {
    this.playerId = playerId;
    this.registrationDate = registrationDate;
    this.status = status;
    this.country = country;
    this.balance = balance;
    this.currency = currency;
  }

  public Integer getPlayerId() {
    return playerId;
  }

  public Timestamp getRegistrationDate() {
    return registrationDate;
  }

  public Integer getStatus() {
    return status;
  }

  public Integer getCountry() {
    return country;
  }

  public Double getBalance() {
    return balance;
  }

  public Player setBalance(Double balance) {
    this.balance = balance;
    return this;
  }

  public String getCurrency() {
    return currency;
  }

  @Override
  public String toString() {
    return "Player{" +
            "playerId=" + playerId +
            ", registrationDate=" + registrationDate +
            ", status=" + status +
            ", country=" + country +
            ", balance=" + balance +
            ", currency='" + currency + '\'' +
            '}';
  }
}
