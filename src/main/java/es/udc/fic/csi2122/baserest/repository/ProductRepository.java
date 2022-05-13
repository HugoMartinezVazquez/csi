package es.udc.fic.csi2122.baserest.repository;

import es.udc.fic.csi2122.baserest.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository  extends JpaRepository<Product, Long> {



}
