package com.org.repository;

import java.util.List;

import com.org.model.Person;

public interface AddressBookRepository {
	
	List<Person> getPersons();
	
}
