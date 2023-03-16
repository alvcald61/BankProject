package com.bp.appbanco.service.impl;

import com.bp.appbanco.model.emuns.Gender;
import com.bp.appbanco.model.emuns.Status;
import com.bp.appbanco.model.person.Client;
import com.bp.appbanco.model.person.mapper.PersonMapper;
import com.bp.appbanco.repository.client.ClientRepository;
import com.bp.appbanco.service.AccountService;
import com.bp.appbanco.service.ClientService;
import com.bp.appbanco.service.dto.request.RegisterClientRequest;
import com.bp.appbanco.service.dto.response.ClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.bp.appbanco.service.utils.Utils.returnFirstNotNull;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class ClientServiceImpl implements ClientService {
  
  private final ClientRepository clientRepository;
  
  private final PersonMapper personMapper;
  
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final AccountService accountService;
  
  @Override
  public ClientResponse createClient(RegisterClientRequest registerClientRequest) {
    log.info("Creating client: " + registerClientRequest.getName());
    Client client = personMapper.clientDtoToClient(registerClientRequest);
    client.setStatus(Status.ACTIVE);
    client = clientRepository.save(client);
    log.info("Client created: " + client.getName());
    return personMapper.clientToClientResponse(client);
  }

  @Override
  public ClientResponse updateClient(Long id, RegisterClientRequest clientRequest) {
    log.info("Updating client: " + id);
    Client client = clientRepository.getClientIfExists(id);
    client.setPassword(Optional.ofNullable(clientRequest.getPassword()).map(bCryptPasswordEncoder::encode).orElse(client.getPassword()));
    client.setGender(Optional.ofNullable(clientRequest.getGender()).map(Gender::genderFromAbbreviation).orElse(client.getGender()));
    client.setAge(returnFirstNotNull(clientRequest.getAge(), client.getAge()));
    client.setAddress(returnFirstNotNull(clientRequest.getAddress(), client.getAddress()));
    client.setPhone( returnFirstNotNull(clientRequest.getPhone(), client.getPhone()));
    client.setName(returnFirstNotNull(clientRequest.getName(), client.getName()));
    client.setIdentifier(returnFirstNotNull(clientRequest.getIdentifier(), client.getIdentifier()));
    client = clientRepository.save(client);
    log.info("Client updated: " + client.getName());
    return personMapper.clientToClientResponse(client);
  }


  @Override
  public ClientResponse getClientById(Long id) {
    log.info("Getting client: " + id);
    Client client = clientRepository.getClientIfExists(id);
    log.info("Client found: " + client.getName());
    return personMapper.clientToClientResponse(client);
  }

  @Override
  public boolean deleteClient(Long id) {
    log.info("Deleting client: " + id);
    Client client = clientRepository.getClientIfExists(id);
    clientRepository.deleteByPersonId(client.getPersonId());
    accountService.deleteAllClientAccounts(id);
    log.info("Client deleted: " + client.getName());
    return true;
  }
}
