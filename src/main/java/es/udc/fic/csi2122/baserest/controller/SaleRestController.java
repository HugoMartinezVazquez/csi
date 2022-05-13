package es.udc.fic.csi2122.baserest.controller;


import es.udc.fic.csi2122.baserest.entity.Sale;
import es.udc.fic.csi2122.baserest.dto.SaleDto;
import es.udc.fic.csi2122.baserest.conversors.SaleConversors;
import es.udc.fic.csi2122.baserest.repository.SaleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@RestController
@Transactional
@RequestMapping("sale") //Link url  http://localhost:8080/sale

public class SaleRestController {

    private static final Logger logger = LoggerFactory.getLogger(BaseRestController.class);

    @PersistenceContext
    private EntityManager em;

    private SaleRepository saleRepository;

    @Autowired
    public SaleRestController(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @GetMapping(value = "all")
    public List<SaleDto> getSaleList() {
        return SaleConversors.toSaleDtoList(saleRepository.findAll());
    }

    @PostMapping(value = "")
    public Long newSale(@RequestBody SaleDto saleDto) { //Si falla salta error
        logger.info("Creating new sale: {}", saleDto);
        var newSale = em.merge(SaleConversors.toSale(saleDto)); //Creamos una venta
        logger.info("Created sale: {}", newSale);
        return newSale.getId();
    }

    @GetMapping(value = "/id={id}")
    public ResponseEntity<SaleDto> getSaleByID(@PathVariable Long id) { //Get por id de venta
        logger.info("Fetching sale with id: {}", id);
        var sale = saleRepository.findById(id);
        return ResponseEntity.of(sale.map(p -> {
            logger.info("Found sale with id {}: {}", id, p);
            return SaleConversors.toSaleDto(p);
        }));
    }

    @GetMapping(value="/prodId={prodId}")
    public ResponseEntity<List<SaleDto>> getByProdId(@PathVariable Integer prodId){ //Get por id de producto
        final List<Sale> sales;
        logger.info("Fetching sale with prodId: {}", prodId);
        sales = saleRepository.findByProdId(prodId);
        return ResponseEntity.ok(SaleConversors.toSaleDtoList(sales));
    }

    @GetMapping(value="/clientId={clientId}")
    public ResponseEntity<List<SaleDto>> getByClientId(@PathVariable Integer prodId){ //Get por id de cliente
        final List<Sale> sales;
        logger.info("Fetching sale with clientId: {}", clientId);
        sales = saleRepository.findByClientId(clientId);
        return ResponseEntity.ok(SaleConversors.toSaleDtoList(sales));
    }
}