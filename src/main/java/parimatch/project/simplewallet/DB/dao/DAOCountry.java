package parimatch.project.simplewallet.DB.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.DB.DBConnection;
import parimatch.project.simplewallet.DB.dto.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOCountry implements DAO<Country, Integer> {
  private static final Logger logger = LoggerFactory.getLogger(DAOCountry.class);

  @Override
  public Country get(Integer id) throws SQLException {
    String getCountry = "SELECT * FROM countries WHERE country_id = ?";
    Country country = null;
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getCountry)) {
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int countryId = resultSet.getInt("country_id");
        String countryName = resultSet.getString("country_name");
        String countryCode = resultSet.getString("country_code");
        country = new Country(countryId, countryName, countryCode);
      }
    }
    return country;
  }

  @Override
  public List getAll() throws SQLException {
    String getCountry = "SELECT * FROM countries ";
    List<Country> listOfCountries = new ArrayList<>();
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getCountry)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int countryId = resultSet.getInt("country_id");
        String countryName = resultSet.getString("country_name");
        String countryCode = resultSet.getString("country_code");
        Country country = new Country(countryId, countryName, countryCode);
        listOfCountries.add(country);
      }
    }
    return listOfCountries;
  }

  @Override
  public void save(Country country) throws SQLException {
    String insertAccountScrtipt = "INSERT INTO countries (\"country_id\", \"country_name\", \"country_code\") VALUES (?, ?, ?);";
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertAccountScrtipt)) {
      preparedStatement.setInt(1, country.getCountryId());
      preparedStatement.setString(2, country.getCountryName());
      preparedStatement.setString(3, country.getCountryCode());
      preparedStatement.executeUpdate();
    }
  }

  @Override
  public Country update(Country country) throws SQLException {
    String updateCountryScript = "UPDATE countries SET \"country_name\" = ?, \"country_code\" = ? WHERE \"country_id\" = ?;";
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(updateCountryScript)) {
      preparedStatement.setString(1, country.getCountryName());
      preparedStatement.setString(2, country.getCountryCode());
      preparedStatement.setInt(3, country.getCountryId());
      preparedStatement.executeUpdate();
    }
    return country;
  }

  @Override
  public void delete(int id) throws SQLException {
    String deleteCountry = "DELETE FROM countries WHERE country_id = ?";
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(deleteCountry)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    }
  }
}
