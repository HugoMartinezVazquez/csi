package es.udc.fic.csi2122.baserest.controller;

import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import es.udc.fic.csi2122.baserest.dto.ClientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.udc.fic.csi2122.baserest.conversors.ClientConversors;
import es.udc.fic.csi2122.baserest.entity.Client;
import es.udc.fic.csi2122.baserest.repository.ClientRepository;

@RestController
@Transactional
@RequestMapping("client")

public class ClientRestController {
    private static final Logger logger = LoggerFactory.getLogger(BaseRestController.class);

    @PersistenceContext
    private EntityManager em;

    private ClientRepository clientRepository;

    @Autowired
    public ClientRestController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @GetMapping(value = "all")
    public List<ClientDto> getClients() {
        return ClientConversors.toClientDtoList(clientRepository.findAll());
    }

    /*Función que registra un nuevo cliente.
     * En caso de error devolvera un error 500
     * */

    @PostMapping(value = "")
    public Long newClient(@RequestBody ClientDto clientDto) {
        logger.info("Registering a new client: {}", clientDto);
        var newClient = em.merge(ClientConversors.toClient(clientDto));
        logger.info("Created client: {}", newClient);
        return newClient.getId();
    }

    /* Esta funcion modificar un cliente existente
     *
     * */

    @PutMapping(value = "id={id}")
    public void updateClient (@RequestBody ClientDto clientDto, @PathVariable Long id){
        Client client = clientRepository.getById(id);
        logger.info("Updating client: {}", client);
        client.setName(clientDto.name());
        client.setSurname(clientDto.surname());
        client.setAge(clientDto.age());
        client.setDni(clientDto.dni());
        client.setAddress(clientDto.address());
        client.setPaymentMethod(clientDto.paymentMethod());
        Client updatedClient = clientRepository.save(client);
        logger.info("Updated client: {}", updatedClient);
    }

    //Devuelve un cliente en particular
    @GetMapping(value = "/id={idClient}")
    public  ResponseEntity<ClientDto>getByName(@PathVariable Long idClient) {

        logger.info("Fetching client with id: {}", idClient);
        var client = clientRepository.findById(idClient);

        return ResponseEntity.of(client.map(c -> {
            logger.info("Found client with id {}: {}", idClient, c);
            return ClientConversors.toClientDto(c);
        }));
    }

}
