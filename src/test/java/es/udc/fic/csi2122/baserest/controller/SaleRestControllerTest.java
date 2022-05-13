package es.udc.fic.csi2122.baserest.controller;


import es.udc.fic.csi2122.baserest.entity.Sale;
import es.udc.fic.csi2122.baserest.dto.SaleDto;
import es.udc.fic.csi2122.baserest.conversors.SaleConversors;
import es.udc.fic.csi2122.baserest.repository.SaleRepository;

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
public class SaleRestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    private void initBaseUrl() {
        baseUrl = "http://localhost:" + port;
    }

    @BeforeEach
    @AfterEach
    private void resetSales() {
        saleRepository.deleteAll();
    } //Borrar BBDD

    @Test
    void getAllSalesIni() { //Test sobre BBDD vacía
        var response = getForList(restTemplate, baseUrl + "/sale/all", SaleDto.class);
        assertThat(response).isEmpty();
    }

    @Test
    void getAllSalesIns() { //Test sobre insercion correcta
        Product pr1 = new Product("Fairy", 10,40,"Jabon de platos");
        Client cl1 = new Client("Antela", "Couceiro", 20,"43748590I","Calle Riegod de agua", "PayPal");

        var prod = restTemplate.postForEntity(baseUrl + "/product/", pr1, Long.class);
        assertThat(prod.getStatusCode()).matches(HttpStatus::is2xxSuccessful);
        var cli = restTemplate.postForEntity(baseUrl + "/client/", cl1, Long.class);
        assertThat(cli.getStatusCode()).matches(HttpStatus::is2xxSuccessful);

        pr1.setId(prod.getBody());
        cl1.setId(cli.getBody());

        var sale = new SaleDto(pr1, cl1, 3, 1.4f, 1.6f);

        var response = getForList(restTemplate, baseUrl + "sale/all", SaleDto.class);

        assertThat(response).hasSize(1); //Un unico elemento
        assertThat(response.get(0)).isEqualTo(sale); //El objeto insertado es correcto
    }

    @Test
    void createSale() { //Test sobre insercion y validacion
        Product pr1 = new Product("Fairy", 10,40,"Jabon de platos");
        Client cl1 = new Client("Antela", "Couceiro", 20,"43748590I","Calle Riegod de agua", "PayPal");

        var prod = restTemplate.postForEntity(baseUrl + "/product/", pr1, Long.class);
        assertThat(prod.getStatusCode()).matches(HttpStatus::is2xxSuccessful);
        var cli = restTemplate.postForEntity(baseUrl + "/client/", cl1, Long.class);
        assertThat(cli.getStatusCode()).matches(HttpStatus::is2xxSuccessful);

        pr1.setId(prod.getBody());
        cl1.setId(cli.getBody());

        var sale = new SaleDto(pr1, cl1, 3, 1.4f, 1.6f);

        var response = restTemplate.postForEntity(baseUrl + "/sale/", sale, Long.class);

        assertThat(response.getStatusCode()).matches(HttpStatus::is2xxSuccessful);

        var id = response.getBody();

        var response2 = restTemplate.getForObject(baseUrl + "/sale/id=" + id, SaleDto.class);

        assertThat(response2).isEqualTo(sale); //Comparamos objetos
    }

    @Test
    void findById() { //Test sobre busqueda de sale
        Product pr1 = new Product("Fairy", 10,40,"Jabon de platos");
        Client cl1 = new Client("Antela", "Couceiro", 20,"43748590I","Calle Riegod de agua", "PayPal");
        Product pr1 = new Product("Magno", 4,20,"Jabon de cuerpo ");
        Client cl1 = new Client("Yael", "Moure", 20,"86758475U","Calle Maruja Mallo", "Transferencia");

        var prod1 = restTemplate.postForEntity(baseUrl + "/product/", pr1, Long.class);
        assertThat(prod1.getStatusCode()).matches(HttpStatus::is2xxSuccessful);
        var cli1 = restTemplate.postForEntity(baseUrl + "/client/", cl1, Long.class);
        assertThat(cli1.getStatusCode()).matches(HttpStatus::is2xxSuccessful);
        var prod2 = restTemplate.postForEntity(baseUrl + "/product/", pr2, Long.class);
        assertThat(prod2.getStatusCode()).matches(HttpStatus::is2xxSuccessful);
        var cli2 = restTemplate.postForEntity(baseUrl + "/client/", cl2, Long.class);
        assertThat(cli2.getStatusCode()).matches(HttpStatus::is2xxSuccessful);

        pr1.setId(prod1.getBody());
        cl1.setId(cli1.getBody());
        pr2.setId(prod2.getBody());
        cl2.setId(cli2.getBody());

        var sale1 = new SaleDto(pr1, cl1, 3, 1.4f, 1.6f);

        var response = restTemplate.postForEntity(baseUrl + "/sale/", sale1, Long.class);

        var sale2 = new SaleDto(pr2, cl2, 3, 3.5f, 0.21f);
        restTemplate.postForEntity(baseUrl + "/sale/", sale2, Long.class);

        var id = response.getBody();

        var response2 = restTemplate.getForObject(baseUrl + "/sale/id=" + id, SaleDto.class);

        assertThat(response2).isEqualTo(sale1); //Comparamos objetos
    }

    @Test
    void findByProdId(Long idProd) { //Test sobre busqueda de producto        Product pr1 = new Product("Fairy", 10,40,"Jabon de platos");
        Client cl1 = new Client("Antela", "Couceiro", 20,"43748590I","Calle Riegod de agua", "PayPal");
        Product pr1 = new Product("Magno", 4,20,"Jabon de cuerpo ");
        Client cl1 = new Client("Yael", "Moure", 20,"86758475U","Calle Maruja Mallo", "Transferencia");

        var prod1 = restTemplate.postForEntity(baseUrl + "/product/", pr1, Long.class);
        assertThat(prod1.getStatusCode()).matches(HttpStatus::is2xxSuccessful);
        var cli1 = restTemplate.postForEntity(baseUrl + "/client/", cl1, Long.class);
        assertThat(cli1.getStatusCode()).matches(HttpStatus::is2xxSuccessful);
        var prod2 = restTemplate.postForEntity(baseUrl + "/product/", pr2, Long.class);
        assertThat(prod2.getStatusCode()).matches(HttpStatus::is2xxSuccessful);
        var cli2 = restTemplate.postForEntity(baseUrl + "/client/", cl2, Long.class);
        assertThat(cli2.getStatusCode()).matches(HttpStatus::is2xxSuccessful);

        pr1.setId(prod1.getBody());
        cl1.setId(cli1.getBody());
        pr2.setId(prod2.getBody());
        cl2.setId(cli2.getBody());

        var sale1 = new SaleDto(pr1, cl1, 3, 1.4f, 1.6f);

        var response = restTemplate.postForEntity(baseUrl + "/sale/", sale1, Long.class);

        var sale2 = new SaleDto(pr2, cl2, 3, 3.5f, 0.21f);
        restTemplate.postForEntity(baseUrl + "/sale/", sale2, Long.class);

        var id = response.getBody();

        var response2 = restTemplate.getForObject(baseUrl + "/sale/id=" + idProd, SaleDto.class);

        assertThat(response2).isEqualTo(sale1); //Comparamos objetos
    }
}

    @Test
    void findByClientId(Long idClient) { //Test sobre busqueda de producto        Product pr1 = new Product("Fairy", 10,40,"Jabon de platos");
        Client cl1 = new Client("Antela", "Couceiro", 20,"43748590I","Calle Riegod de agua", "PayPal");
        Product pr1 = new Product("Magno", 4,20,"Jabon de cuerpo ");
        Client cl1 = new Client("Yael", "Moure", 20,"86758475U","Calle Maruja Mallo", "Transferencia");

        var prod1 = restTemplate.postForEntity(baseUrl + "/product/", pr1, Long.class);
        assertThat(prod1.getStatusCode()).matches(HttpStatus::is2xxSuccessful);
        var cli1 = restTemplate.postForEntity(baseUrl + "/client/", cl1, Long.class);
        assertThat(cli1.getStatusCode()).matches(HttpStatus::is2xxSuccessful);
        var prod2 = restTemplate.postForEntity(baseUrl + "/product/", pr2, Long.class);
        assertThat(prod2.getStatusCode()).matches(HttpStatus::is2xxSuccessful);
        var cli2 = restTemplate.postForEntity(baseUrl + "/client/", cl2, Long.class);
        assertThat(cli2.getStatusCode()).matches(HttpStatus::is2xxSuccessful);

        pr1.setId(prod1.getBody());
        cl1.setId(cli1.getBody());
        pr2.setId(prod2.getBody());
        cl2.setId(cli2.getBody());

        var sale1 = new SaleDto(pr1, cl1, 3, 1.4f, 1.6f);

        var response = restTemplate.postForEntity(baseUrl + "/sale/", sale1, Long.class);

        var sale2 = new SaleDto(pr2, cl2, 3, 3.5f, 0.21f);
        restTemplate.postForEntity(baseUrl + "/sale/", sale2, Long.class);

        var response2 = restTemplate.getForObject(baseUrl + "/sale/id=" + idClient, SaleDto.class);

        assertThat(response2).isEqualTo(sale1); //Comparamos objetos
    }
}