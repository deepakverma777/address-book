package com.org.service;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.exception.BaseException;
import com.org.exception.ErrorCode;
import com.org.model.ComparisionRequest;
import com.org.model.ComparisionResponse;
import com.org.model.MaleCountResponse;
import com.org.model.OldestPersonResponse;
import com.org.model.Person;
import com.org.repository.AddressBookRepository;

@Service
public class AddressBookServiceImpl implements AddressBookService {

  @Autowired private AddressBookRepository addressBookRepository;

  @Override
  public MaleCountResponse getMaleCount() {
    var personList = addressBookRepository.getPersons();
    return MaleCountResponse.builder()
        .maleCount(
            personList
                .stream()
                .filter(
                    address -> address.getGender() != null && "Male".equals(address.getGender()))
                .count())
        .build();
  }

  @Override
  public OldestPersonResponse findOldest() {
    var personList = addressBookRepository.getPersons();
    return OldestPersonResponse.builder()
        .oldestPerson(
            personList
                .stream()
                .min((p1, p2) -> p1.getDateOfBirth().compareTo(p2.getDateOfBirth()))
                .get()
                .getName())
        .build();
  }

  @Override
  public ComparisionResponse compare(ComparisionRequest comparisionRequest) {
    var personList = addressBookRepository.getPersons();
    Person p1 =
        personList
            .stream()
            .filter(p -> p.getName() != null && p.getName().contains(comparisionRequest.getName1()))
            .findFirst()
            .orElseThrow(
                () ->
                    new BaseException(
                        ErrorCode.NOT_FOUND.getErrorData(comparisionRequest.getName1())));
    Person p2 =
        personList
            .stream()
            .filter(p -> p.getName() != null && p.getName().contains(comparisionRequest.getName2()))
            .findFirst()
            .orElseThrow(
                () ->
                    new BaseException(
                        ErrorCode.NOT_FOUND.getErrorData(comparisionRequest.getName2())));
    return ComparisionResponse.builder()
        .daysCount(ChronoUnit.DAYS.between(p1.getDateOfBirth(), p2.getDateOfBirth()))
        .build();
  }
}
