package es.udc.fic.csi2122.baserest.controller;

import es.udc.fic.csi2122.baserest.conversors.ClientConversors;
import es.udc.fic.csi2122.baserest.dto.ClientDto;
import es.udc.fic.csi2122.baserest.entity.Client;
import es.udc.fic.csi2122.baserest.repository.ClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static es.udc.fic.csi2122.baserest.utils.TestRestTemplateUtils.getForList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientRestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    private void initBaseUrl() {
        baseUrl = "http://localhost:" + port + "/client";
    }

    //Antes y despues de cada test debemos borrar
    //de la bd para que no dependan unos de otros
    @BeforeEach
    @AfterEach
    private void resetClients() {
        clientRepository.deleteAll();
    }

    //Testeamos que la bbdd esté vacía inicialmente
    @Test
    void getAllClients1() {
        var response = getForList(restTemplate, baseUrl + "/all", ClientDto.class);

        assertThat(response).isEmpty();
    }

    //Testeamos que el cliente registrado es único y cuadra con el añadido

    @Test
    void getAllClients2() {
        var client = new ClientDto("Antela", "Couceiro", 20,"43748590I","Calle Riegod de agua", "PayPal");
        restTemplate.postForObject(baseUrl + "/", client, Long.class);

        var response = getForList(restTemplate, baseUrl + "/all", ClientDto.class);

        assertThat(response).hasSize(1);
        assertThat(response.get(0)).isEqualTo(client);
    }

    //Testeamos que se registra bien y que el cliente concuerda con el que queria registrar

    @Test
    void resgisterClient() {
        var client = new ClientDto("Antela", "Couceiro", 20,"43748590I","Calle Riegod de agua", "PayPal");
        var response = restTemplate.postForEntity(baseUrl + "/", client, Long.class);

        assertThat(response.getStatusCode()).matches(HttpStatus::is2xxSuccessful);

        var id = response.getBody();
        var response2 = restTemplate.getForObject(baseUrl + "/id=" + id, ClientDto.class);
        assertThat(response2).isEqualTo(client);
    }

    //Testeamos que al hacer un update de un cliente se cambian los campos a los nuevos

    @Test
    void UpdateClient(){
        var client1 = new ClientDto("Antela", "Couceiro", 20,"43748590I","Calle Riego de agua", "PayPal");
        var response= restTemplate.postForEntity(baseUrl + "/", client1, Long.class);
        var id = response.getBody();

        var client2 = new ClientDto("Antela", "Couceiro", 21,"43748590I","Calle Riego de agua", "PayPal");
        restTemplate.put(baseUrl+ "/id="+ id,client2,Long.class);

        var response2 = restTemplate.getForObject(baseUrl + "/id=" + id, ClientDto.class);
        assertThat(response2).isEqualTo(client2);

    }

    /*Testeamos que el find by name efectivamente encuentra el cliente que queremos y que concuerda
        Para ello inserto 2 clientes y compruebo que efectivamente el que he buscado es el que queria
     */
    @Test
    void findById(){
        var client1 = new ClientDto("Antela", "Couceiro", 20,"43748590I","Calle Riego de agua", "PayPal");
        var response = restTemplate.postForEntity(baseUrl + "/", client1, Long.class);

        var client2 = new ClientDto("Yael", "Moure", 20,"86758475U","Calle Maruja Mallo", "Transferencia");
        restTemplate.postForEntity(baseUrl + "/", client2, Long.class);

        var id = response.getBody();
        var response2= restTemplate.getForObject(baseUrl+ "/id=" + id, ClientDto.class);
        assertThat(response2).isEqualTo(client1);

    }

}
