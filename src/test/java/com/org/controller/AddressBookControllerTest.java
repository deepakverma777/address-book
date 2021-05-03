package com.org.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.exception.ErrorCode;
import com.org.model.ComparisionRequest;
import com.org.model.ComparisionResponse;
import com.org.model.MaleCountResponse;
import com.org.model.OldestPersonResponse;
import com.org.service.AddressBookService;

@Tag("unittest")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AddressBookControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AddressBookService addressBookService;

  @Test
  void testGetMaleCount() throws JsonProcessingException, Exception {

    Mockito.when(addressBookService.getMaleCount())
        .thenReturn(MaleCountResponse.builder().maleCount(2l).build());

    mockMvc
        .perform(MockMvcRequestBuilders.get("/male/count").headers(getHttpHeaders()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.maleCount").value(2));
  }

  @Test
  void testFindOldest() throws JsonProcessingException, Exception {

    Mockito.when(addressBookService.findOldest())
        .thenReturn(OldestPersonResponse.builder().oldestPerson("Nick Night").build());

    mockMvc
        .perform(MockMvcRequestBuilders.get("/oldest").headers(getHttpHeaders()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.oldestPerson").value("Nick Night"));
  }

  @Test
  void testCompare() throws JsonProcessingException, Exception {

    Mockito.when(addressBookService.compare(any()))
        .thenReturn(ComparisionResponse.builder().daysCount(2862l).build());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/compare")
                .content(
                    new ObjectMapper()
                        .writeValueAsString(
                            ComparisionRequest.builder().name1("Robert").name2("Nick").build()))
                .headers(getHttpHeaders()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.daysCount").value(2862l));
  }
  

  @Test
  void shouldCompareThrowErrorWhenMediaTypeNotSupported() throws JsonProcessingException, Exception {

    Mockito.when(addressBookService.compare(any()))
        .thenReturn(ComparisionResponse.builder().daysCount(2862l).build());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/compare")
                .content(
                    new ObjectMapper()
                        .writeValueAsString(
                            ComparisionRequest.builder().name1("Robert").name2("Nick").build()))
                .headers(new HttpHeaders()))
        .andDo(MockMvcResultHandlers.print()).andExpect(status().isUnsupportedMediaType())
        .andExpect(jsonPath("$.errors[0].code", is(ErrorCode.MEDIA_TYPE_NOT_SUPPORTED.getCode())))
        .andExpect(jsonPath("$.errors[0].message", is("null media type is not supported. Supported media types are application/json")));
  }
  
  @Test
  void shouldCompareThrowErrorWhenMissingBodyAttribute() throws JsonProcessingException, Exception {

    Mockito.when(addressBookService.compare(any()))
        .thenReturn(ComparisionResponse.builder().daysCount(2862l).build());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/compare")
                .content(
                    new ObjectMapper()
                        .writeValueAsString(
                            ComparisionRequest.builder().name1(null).name2("Nick").build()))
                .headers(getHttpHeaders()))
        .andDo(MockMvcResultHandlers.print()).andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].code", is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode())))
        .andExpect(jsonPath("$.errors[0].message", is("InvalidParameter : name1 cannot be empty or null")));
  }


  public static HttpHeaders getHttpHeaders() {
    MultiValueMap<String, String> valuesMap = new LinkedMultiValueMap<>();
    valuesMap.add("Content-Type", "application/json");
    return new HttpHeaders(valuesMap);
  }
}
