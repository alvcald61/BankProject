package com.bp.appbanco.controller;

import com.bp.appbanco.service.ClientService;
import com.bp.appbanco.service.dto.request.RegisterClientRequest;
import com.bp.appbanco.service.dto.response.ClientResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {
  private final ClientService clientService;

  @GetMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public ClientResponse getClient(@PathVariable Long id) {
    return clientService.getClientById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ClientResponse saveClient(@Valid @RequestBody RegisterClientRequest client) {
    return clientService.createClient(client);
  }
  
  @PutMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public ClientResponse updateClient(@PathVariable Long id, @Valid @RequestBody RegisterClientRequest client) {
    return clientService.updateClient(id, client);
  }

  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteClient(@PathVariable Long id) {
    clientService.deleteClient(id);
  }

  @PatchMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public ClientResponse patchClient(@PathVariable Long id, @RequestBody RegisterClientRequest client) {
    return clientService.updateClient(id, client);
  }
}
