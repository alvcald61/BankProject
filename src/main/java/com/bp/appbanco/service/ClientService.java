package com.bp.appbanco.service;

import com.bp.appbanco.service.dto.request.RegisterClientRequest;
import com.bp.appbanco.service.dto.response.ClientResponse;

public interface ClientService {
  ClientResponse createClient(RegisterClientRequest registerClientRequest);
  ClientResponse updateClient(Long id, RegisterClientRequest clientRequest);
  ClientResponse getClientById(Long id);
  boolean deleteClient(Long id);
}
