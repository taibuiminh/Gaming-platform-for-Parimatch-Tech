package parimatch.project.simplewallet.DB.dto;

public class Country {
  Integer countryId;
  String countryName;
  String countryCode;

  public Country(Integer countryId, String countryName, String countryCode) {
    this.countryId = countryId;
    this.countryName = countryName;
    this.countryCode = countryCode;
  }

  public Integer getCountryId() {
    return countryId;
  }

  public String getCountryName() {
    return countryName;
  }

  public String getCountryCode() {
    return countryCode;
  }

  @Override
  public String toString() {
    return "Country{" +
            "countryId=" + countryId +
            ", countryName='" + countryName + '\'' +
            ", countryCode='" + countryCode + '\'' +
            '}';
  }
}
