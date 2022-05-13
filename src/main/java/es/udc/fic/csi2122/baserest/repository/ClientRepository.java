package es.udc.fic.csi2122.baserest.repository;

import es.udc.fic.csi2122.baserest.entity.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

}
