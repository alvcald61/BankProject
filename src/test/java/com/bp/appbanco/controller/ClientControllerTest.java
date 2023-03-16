package com.bp.appbanco.controller;


import com.bp.appbanco.errors.EntityNotFoundException;
import com.bp.appbanco.service.ClientService;
import com.bp.appbanco.service.dto.request.RegisterClientRequest;
import com.bp.appbanco.service.dto.response.ClientResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@EnableWebMvc
class ClientControllerTest {
  private final static String URL = "/api/v1/client";

  ObjectWriter objectWriter;
  private MockMvc mockMvc;
  @MockBean
  private ClientService clientService;
  private final Gson gson = new Gson();

  @BeforeEach
  public void setup(){
    var objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

    objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
    mockMvc = MockMvcBuilders.standaloneSetup(new ClientController(clientService)).setControllerAdvice(new ErrorHandlerController()).build();
  }

  public ClientResponse generateClient() {
    return ClientResponse.builder()
      .personId(1L)
      .age(20)
      .name("Alvaro Calderon")
      .address("Indepencia y Libertadores")
      .phone("12345566")
      .build();
  }

  public RegisterClientRequest generateRegisterClientRequest() {
    return RegisterClientRequest.builder()
      .age(20)
      .name("Alvaro Calderon")
      .address("Indepencia y Libertadores")
      .phone("12345566")
      .password("123456")
      .build();
  }

  @Test
  @DisplayName("Test successfully get client")
  public void testGetClient() throws Exception {
    //Given
    ClientResponse clientResponse = generateClient();
    given(clientService.getClientById(anyLong())).willReturn(clientResponse);
    //When
    ResultActions result = this.mockMvc.perform(get(URL + "/1"));
    //Then
    result.andExpect(status().isOk())
      .andExpect(jsonPath("$.personId").value(1));
  }

  @Test
  @DisplayName("Test not found client")
  public void testGetClientNotFound() throws Exception {
    //Given
    given(clientService.getClientById(anyLong())).willThrow(new EntityNotFoundException(anyString()));
    //When
    ResultActions result = this.mockMvc.perform(get(URL + "/1"));
    //Then
    result.andExpect(status().isNotFound())
      .andExpect(jsonPath("$.hasErrors").value(true))
      .andExpect(jsonPath("$.error").exists());

  }

  @Test
  @DisplayName("Test saving client")
  public void testSaveClient() throws Exception {
    //Given
    ClientResponse clientResponse = generateClient();
    RegisterClientRequest registerClientRequest = generateRegisterClientRequest();
    given(clientService.createClient(registerClientRequest)).willReturn(clientResponse);
    //When 
    ResultActions result = this.mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(registerClientRequest)));
    //Then 
    result.andExpect(status().isCreated())
      .andExpect(jsonPath("$.personId").value(1));
  }

  @Test
  @DisplayName("Test saving client fail due to maformed json")
  public void testSaveClientError() throws Exception {
    //Given
    ClientResponse clientResponse = generateClient();
    given(clientService.createClient(any())).willReturn(clientResponse);
    //When
    ResultActions result = this.mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(""));
    //Then
    result.andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.hasErrors").value(true));

  }

  @Nested
  @DisplayName("Test saving client fail due to validation")
  public class ValidationFailures {

    private List<RegisterClientRequest> generateList() {
      List<RegisterClientRequest> list = new ArrayList<>();
      RegisterClientRequest registerClientRequest = generateRegisterClientRequest();
      registerClientRequest.setAge(-1);
      list.add(registerClientRequest);
      registerClientRequest = generateRegisterClientRequest();
      registerClientRequest.setName("");
      list.add(registerClientRequest);
      registerClientRequest = generateRegisterClientRequest();
      registerClientRequest.setAddress("");
      list.add(registerClientRequest);
      registerClientRequest = generateRegisterClientRequest();
      registerClientRequest.setPhone("");
      list.add(registerClientRequest);
      registerClientRequest = generateRegisterClientRequest();
      registerClientRequest.setPassword("");
      list.add(registerClientRequest);
      return list;
    }

    @Test
    @DisplayName("Test saving client fail due name, address, phone and password empty and age less than 0")
    public void testSaveClientErrorNameEmpty() {
      //Given
      ClientResponse clientResponse = generateClient();
      List<RegisterClientRequest> list = generateList();
      given(clientService.createClient(any())).willReturn(clientResponse);
      list.forEach(registerClientRequest -> {
        try {
          //When
          ResultActions result = mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(registerClientRequest)));
          //Then
          result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.hasErrors").value(true))
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.error.errors").isArray())
            .andExpect(jsonPath("$.error.errors[0]", Matchers.oneOf(
              "Age must be greater than 0",
              "Name is required",
              "Address is required",
              "Phone is required",
              "Password is required"
            )));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });

    }
  }
}