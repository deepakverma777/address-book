package com.org.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.org.exception.BaseException;
import com.org.exception.ErrorCode;
import com.org.model.ComparisionRequest;
import com.org.model.ComparisionResponse;
import com.org.model.MaleCountResponse;
import com.org.model.OldestPersonResponse;
import com.org.model.Person;
import com.org.repository.AddressBookRepository;

@Tag("unittest")
@ExtendWith(SpringExtension.class)
class AddressBookServiceImplTest {

  @Mock private AddressBookRepository addressBookRepository;
  @InjectMocks private AddressBookServiceImpl testClass;

  @Test
  void shouldReturnValidMaleCount() {
    // given
    var persons = getPersonList();
    when(addressBookRepository.getPersons()).thenReturn(persons);

    // when
    var actual = testClass.getMaleCount();

    // then
    assertEquals(MaleCountResponse.builder().maleCount(2l).build(), actual);
  }

  @Test
  void shouldReturnValidOldest() {
    // given
    var persons = getPersonList();
    when(addressBookRepository.getPersons()).thenReturn(persons);

    // when
    var actual = testClass.findOldest();

    // then
    assertEquals(OldestPersonResponse.builder().oldestPerson("Nick Night").build(), actual);
  }

  @Test
  void shouldReturnPositiveDaysCount() {
    // given
    var persons = getPersonList();
    var request = ComparisionRequest.builder().name1("Nick").name2("Robert").build();
    when(addressBookRepository.getPersons()).thenReturn(persons);

    // when
    var actual = testClass.compare(request);

    // then
    assertEquals(ComparisionResponse.builder().daysCount(2862l).build(), actual);
  }

  @Test
  void shouldReturnNegitiveDaysCount() {
    // given
    var persons = getPersonList();
    var request = ComparisionRequest.builder().name1("Robert").name2("Nick").build();
    when(addressBookRepository.getPersons()).thenReturn(persons);

    // when
    var actual = testClass.compare(request);

    // then
    assertEquals(ComparisionResponse.builder().daysCount(-2862l).build(), actual);
  }

  @Test
  void shouldThrowExceptionWhenPersionNotFound() {
    // given
    var persons = getPersonList();
    var request = ComparisionRequest.builder().name1("Tim").name2("Nick").build();
    when(addressBookRepository.getPersons()).thenReturn(persons);

    // when
    BaseException ex = assertThrows(BaseException.class, () -> testClass.compare(request));

    // then
    assertAll(
        () -> assertEquals(ErrorCode.NOT_FOUND.getCode(), ex.getErrorData().getCode()),
        () -> assertEquals(ErrorCode.NOT_FOUND.getReasonCode(), ex.getErrorData().getReasonCode()));
  }

  public List<Person> getPersonList() {
    var person1 =
        Person.builder()
            .name("Nick Night")
            .gender("Male")
            .dateOfBirth(LocalDate.of(1977, 03, 16))
            .build();
    var person2 =
        Person.builder()
            .name("Robert Pattinson")
            .gender("Male")
            .dateOfBirth(LocalDate.of(1985, 01, 15))
            .build();
    return List.of(person1, person2);
  }
}
