package es.udc.fic.csi2122.baserest.repository;

import es.udc.fic.csi2122.baserest.entity.Sale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository  extends JpaRepository<Sale, Long> {

    List <Sale> findByProdId (Long prodId);

    List <Sale> findByClientId (Long clientId);

}
