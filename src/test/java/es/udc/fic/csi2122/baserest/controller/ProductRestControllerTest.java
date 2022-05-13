package es.udc.fic.csi2122.baserest.controller;


import es.udc.fic.csi2122.baserest.conversors.ProductConversors;
import es.udc.fic.csi2122.baserest.dto.ProductDto;
import es.udc.fic.csi2122.baserest.entity.Product;
import es.udc.fic.csi2122.baserest.repository.ProductRepository;
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
public class ProductRestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    private void initBaseUrl() {
        baseUrl = "http://localhost:" + port + "/product";
    }

    //Antes y despues de cada test debemos borrar todo lo que haya en la base de datos para que no dependan unos de otros
    @BeforeEach
    @AfterEach
    private void resetProducts() {
        productRepository.deleteAll();
    }

    /*Testeamos que la base de datos este inicialmente vacia
     * */
    @Test
    void getAllProducts1() {
        var response = getForList(restTemplate, baseUrl + "/all", ProductDto.class);

        assertThat(response).isEmpty();
    }

    /*Testeamos que al añadir un producto hay unicamente uno y que concuerda con el añadido
     * */
    @Test
    void getAllProducts2() {
        var product = new ProductDto("Fairy", 10,40,"Jabon de platos");
        restTemplate.postForObject(baseUrl + "/", product, Long.class);

        var response = getForList(restTemplate, baseUrl + "/all", ProductDto.class);

        assertThat(response).hasSize(1);
        assertThat(response.get(0)).isEqualTo(product);
    }

    /*Hacemos un test para ver que se añade bien y que el producto añadido concuerda con el que queria añadir
    * */
    @Test
    void createProduct() {
        var product = new ProductDto("Fairy", 10,40,"Jabon de platos");
        var response = restTemplate.postForEntity(baseUrl + "/", product, Long.class);

        assertThat(response.getStatusCode()).matches(HttpStatus::is2xxSuccessful);

        var id = response.getBody();
        var response2 = restTemplate.getForObject(baseUrl + "/id=" + id, ProductDto.class);
        assertThat(response2).isEqualTo(product);
    }

    /*Testeamos que el find by id efectivamente encuentra el producto que queremos y que concuerda
    Para ello inserto 2 productos y compruebo que efectivamente el que he buscado es el que queria
 * */
    @Test
    void findById(){
        var product1 = new ProductDto("Fairy", 10,40,"Jabon de platos");
        var response = restTemplate.postForEntity(baseUrl + "/", product1, Long.class);

        var product2 = new ProductDto("Magno", 4,20,"Jabon de cuerpo ");
        restTemplate.postForEntity(baseUrl + "/", product2, Long.class);



        var id = response.getBody();
        var response2 = restTemplate.getForObject(baseUrl + "/id=" + id, ProductDto.class);
        assertThat(response2).isEqualTo(product1);

    }



    /*Testeamos que al hacer un update de un producto efectivamente se cambian los campos a los nuevos
     * */
    @Test
    void UpdateProduct(){
        var product1 = new ProductDto("Fairy", 10,40,"Jabon de platos");
        var response= restTemplate.postForEntity(baseUrl + "/", product1, Long.class);
        var id = response.getBody();

        var product2 = new ProductDto("Fairy", 4,20,"Jabon de cocina");
        restTemplate.put(baseUrl+ "/id="+ id,product2,Long.class);


        var response2 = restTemplate.getForObject(baseUrl + "/id=" + id, ProductDto.class);
        assertThat(response2).isEqualTo(product2);

    }


    /*Comprobamos que el stock del producto cambia al añadirle
     * */
    @Test
    void AddProductStock(){
        //Usamos esta variable como numero de stock a añadir
        int addedStock = 5;

        //Primero añadimos un producto
        var product = new ProductDto("Fairy", 10,40,"Jabon de platos");
        var response1 = restTemplate.postForEntity(baseUrl + "/", product, Long.class);

        //Nos quedamos con el stock inicial
        var id = response1.getBody();
        var initialProduct = restTemplate.getForObject(baseUrl + "/id=" + id, ProductDto.class);
        var initialStock =initialProduct.stock();

        //Modificamos el stock del producto
        restTemplate.postForEntity(baseUrl + "/stock/id="+id+"&numStock="+ 5, product, Long.class);
        var response2 = restTemplate.getForObject(baseUrl + "/id=" + id, ProductDto.class);

        //Comprobamos que el stock final es el inicial mas el añadido
        assertThat(response2.stock()).isEqualTo(initialStock+addedStock);
    }


    /*Comprobamos que el stock del producto cambia restarle
     * */
    @Test
    void SubstractProductStock(){
        //Usamos esta variable para quitar stock
        int substractedStock= (-10);

        //Primero añadimos un producto
        var product = new ProductDto("Fairy", 10,40,"Jabon de platos");
        var response1 = restTemplate.postForEntity(baseUrl + "/", product, Long.class);

        //Nos quedamos con el stock inicial
        var id = response1.getBody();
        var initialProduct = restTemplate.getForObject(baseUrl + "/id=" + id, ProductDto.class);
        var initialStock =initialProduct.stock();

        //Modificamos el stock del producto
        restTemplate.postForEntity(baseUrl + "/stock/id="+id+"&numStock="+ substractedStock, product, Long.class);
        var response2 = restTemplate.getForObject(baseUrl + "/id=" + id, ProductDto.class);

        //Comprobamos que el stock final es el inicial menos el quitado
        //OJO - pongo intial + substracted porque substracted ya es una variable negativa
        assertThat(response2.stock()).isEqualTo(initialStock+substractedStock);

    }


    /*Comprobaremos tambien que salta un error cuando intentamos quitar stock y lo dejariamos por debajo de 0
     * */
    @Test
    void CantSubstractProductStock(){
        //Añado el producto
        var product = new ProductDto("Fairy", 10,4,"Jabon de platos");
        var response1 = restTemplate.postForEntity(baseUrl + "/", product, Long.class);
        var id = response1.getBody();

        //Modificamos el stock del de tal forma que quede negativo
        var response2 = restTemplate.postForEntity(baseUrl + "/stock/id="+id+"&numStock="+ (-10), product,ProductDto.class);

        //Comprobamos que el codigo de error matchea con un badRequest
        assertThat(response2.getStatusCode()).matches(HttpStatus::is4xxClientError);

    }


}
