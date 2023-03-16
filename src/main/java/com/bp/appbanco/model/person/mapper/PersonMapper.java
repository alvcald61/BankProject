package com.bp.appbanco.model.person.mapper;

import com.bp.appbanco.model.emuns.Gender;
import com.bp.appbanco.model.person.Client;
import com.bp.appbanco.service.dto.request.RegisterClientRequest;
import com.bp.appbanco.service.dto.response.ClientResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonMapper {
  private final ModelMapper modelMapper;
  
  public RegisterClientRequest clientToClientDto(Client client) {
    var result = modelMapper.map(client, RegisterClientRequest.class);
    result.setGender(client.getGender().getAbbreviation());
    result.setId(client.getPersonId());
    return result;
  }
  
  public Client clientDtoToClient(RegisterClientRequest clientDto) {
    var result =  modelMapper.map(clientDto, Client.class);
    result.setGender(Gender.genderFromAbbreviation(clientDto.getGender()));
    result.setPersonId(clientDto.getId());
    return result;
  }
  
  public ClientResponse clientToClientResponse(Client client) {
    var result = modelMapper.map(client, ClientResponse.class);
    result.setGender(client.getGender().getAbbreviation());
    result.setPersonId(client.getPersonId());
    return result;
  }
  
  
   
}
