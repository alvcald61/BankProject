package com.bp.appbanco.repository.client;

import com.bp.appbanco.model.person.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, ClientDao {
  @Transactional
  @Modifying
  @Query("UPDATE Client c SET c.status = com.bp.appbanco.model.emuns.Status.INACTIVE WHERE c.personId = ?1")
  void deleteByPersonId(Long personId); 
  
}
