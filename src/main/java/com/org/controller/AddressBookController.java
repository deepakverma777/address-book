package com.org.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.org.model.ComparisionRequest;
import com.org.model.ComparisionResponse;
import com.org.model.MaleCountResponse;
import com.org.model.OldestPersonResponse;
import com.org.service.AddressBookService;

@RestController
@Validated
public class AddressBookController {
	
  @Autowired
  private AddressBookService addressBookService;

  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @GetMapping(value = "/male/count", produces = MediaType.APPLICATION_JSON_VALUE)
  public MaleCountResponse getMaleCount() {
    return addressBookService.getMaleCount();
  }

  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @GetMapping(value = "/oldest", produces = MediaType.APPLICATION_JSON_VALUE)
  public OldestPersonResponse findOldest() {
    return addressBookService.findOldest();
  }

  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @PostMapping(
      value = "/compare",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ComparisionResponse compare(
      @RequestBody @Valid ComparisionRequest comparisionRequest,
      @RequestHeader HttpHeaders httpHeaders) {
    return addressBookService.compare(comparisionRequest);
  }
}
