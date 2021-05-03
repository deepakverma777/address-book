package com.org.service;

import com.org.model.ComparisionRequest;
import com.org.model.ComparisionResponse;
import com.org.model.MaleCountResponse;
import com.org.model.OldestPersonResponse;

public interface AddressBookService {

  MaleCountResponse getMaleCount();

  OldestPersonResponse findOldest();

  ComparisionResponse compare(ComparisionRequest comparisionRequest);
}
