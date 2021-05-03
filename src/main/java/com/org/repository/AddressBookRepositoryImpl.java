package com.org.repository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import com.opencsv.CSVReader;
import com.org.exception.BaseException;
import com.org.exception.ErrorCode;
import com.org.model.Person;

@Component
@CacheConfig(cacheNames={"users"})
public class AddressBookRepositoryImpl implements AddressBookRepository {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  @Value("${filePath}")
  private String filePath;

  @Cacheable
  @Override
  public List<Person> getPersons() {
    try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
      List<String[]> rows = reader.readAll();
      List<Person> persons =
          rows.stream()
              .map(
                  r ->
                      Person.builder()
                          .name(StringUtils.isNoneBlank(r[0]) ? r[0].strip() : null)
                          .gender(StringUtils.isNoneBlank(r[1]) ? r[1].strip() : null)
                          .dateOfBirth(
                              StringUtils.isNoneBlank(r[2])
                                  ? LocalDate.parse(
                                      r[2].strip(),
                                      new DateTimeFormatterBuilder()
                                          .appendPattern("dd/MM/")
                                          .appendValueReduced(ChronoField.YEAR_OF_ERA, 2, 2, LocalDate.now().minusYears(80))
                                          .toFormatter())
                                  : null)
                          .build())
              .collect(Collectors.toList());
      return persons;
    } catch (FileNotFoundException e) {
      logger.atSevere().withStackTrace(StackSize.FULL).withCause(e).log(
          "File not found : %s", e.getMessage());
      throw new BaseException(ErrorCode.NOT_FOUND.getErrorData(filePath));
    } catch (IOException e) {
      logger.atSevere().withStackTrace(StackSize.FULL).withCause(e).log(
          "Error occoured while reading file : %s", e.getMessage());
      throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR.getErrorData());
    }
  }
}
