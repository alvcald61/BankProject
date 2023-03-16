package com.bp.appbanco.repository.client;

import com.bp.appbanco.model.person.Client;

public interface ClientDao {
 Client getClientIfExists(Long id);
}
