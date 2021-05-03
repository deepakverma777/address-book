package com.org.repository;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.org.model.Person;

@Tag("unittest")
@ExtendWith(SpringExtension.class)
class AddressBookRepositoryImplTest {

  @InjectMocks private AddressBookRepositoryImpl testClass;
  
  @BeforeEach
  public void setup() {
    ReflectionTestUtils.setField(testClass, "filePath", "src/test/resources/AddressBook.csv");
  }

  @Test
  void shouldReturnPersons() {
    
	  // given
	  var person1 = Person.builder()
			  .name("Nick Night")
			  .gender("Male")
			  .dateOfBirth(LocalDate.of(1977, 03, 16))
			  .build();
	  var person2 = Person.builder()
			  .name("Robert Pattinson")
			  .gender("Male")
			  .dateOfBirth(LocalDate.of(1985, 01, 15))
			  .build();	  
	  var expected = List.of(person1,person2);
	  //when 
	  var actual = testClass.getPersons();
	  
	  //then
	  assertIterableEquals(expected, actual);
  }
  
}
