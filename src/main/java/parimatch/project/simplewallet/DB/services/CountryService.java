package parimatch.project.simplewallet.DB.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.DB.dao.DAOCountry;
import parimatch.project.simplewallet.DB.dto.Country;

import java.sql.SQLException;
import java.util.List;

public class CountryService {
  private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
  DAOCountry daoCountry = new DAOCountry();

  public Country getCountryById(int id) throws SQLException {
    logger.info("Get country with id {}", id);
    return daoCountry.get(id);
  }

  public Country updateCountry(Country newCountry) throws SQLException {
    logger.info("Update country with id {}", newCountry.getCountryId());
    daoCountry.update(newCountry);
    return newCountry;
  }

  public void deleteCountryById(int id) throws SQLException {
    logger.info("Delete country with id {}", id);
    daoCountry.delete(id);
  }

  public Country insertCountry(Country country) throws SQLException {
    logger.info("Insert country with id {}", country.getCountryId());
    daoCountry.save(country);
    return country;
  }

  public List<Country> getAllCountries() throws SQLException {
    logger.info("Get all countries");
    return daoCountry.getAll();
  }


}
