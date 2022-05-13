package es.udc.fic.csi2122.baserest.controller;


import es.udc.fic.csi2122.baserest.conversors.ProductConversors;
import es.udc.fic.csi2122.baserest.dto.ProductDto;
import es.udc.fic.csi2122.baserest.dto.UserDto;
import es.udc.fic.csi2122.baserest.entity.Product;
import es.udc.fic.csi2122.baserest.repository.ProductRepository;
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
@RequestMapping("product")
//al poner el request mapping esto va a linkearse a la url  http://localhost:8080/product
public class ProductRestController {

    private static final Logger logger = LoggerFactory.getLogger(BaseRestController.class);

    @PersistenceContext
    private EntityManager em;

    private ProductRepository productRepository;

    @Autowired
    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //Este get a http://localhost:8080/product/all es más para depuracion y comprobar que se van añadiendo las cosas bien
    @GetMapping(value = "all")
    public List<ProductDto> getPorudctList() {
        return ProductConversors.toProductDtoList(productRepository.findAll());
    }



    /*Función que crea un nuevo producto.
     * En caso de error devolvera un error 500
     * */
    @PostMapping(value = "")
    public Long newProduct(@RequestBody ProductDto productDto) {
        logger.info("Creating new product: {}", productDto);
        var newProduct = em.merge(ProductConversors.toProduct(productDto));
        logger.info("Created product: {}", newProduct);
        return newProduct.getId();
    }

    /* Esta funcion para acceder a la informacion de un producto en particular
     *
     * */
    @GetMapping(value = "/id={id}")
    public ResponseEntity<ProductDto> getProductByID(@PathVariable Long id) {
        logger.info("Fetching product with id: {}", id);
        var product = productRepository.findById(id);
        return ResponseEntity.of(product.map(p -> {
            logger.info("Found product with id {}: {}", id, p);
            return ProductConversors.toProductDto(p);
        }));
    }


    //Funcion para modificar un producto existente
    @PutMapping (value = "id={id}")
    public void updateProduct (@RequestBody ProductDto productDto,@PathVariable Long id){
        Product product = productRepository.getById(id);
        logger.info("Updating product: {}", product);
        product.setName(productDto.name());
        product.setStock(productDto.stock());
        product.setPrice(productDto.price());
        product.setDescription(productDto.description());
        Product updatedProduct = productRepository.save(product);
        logger.info("Updated product: {}", updatedProduct);
    }

    //Modificar stock sera un post a /product/stock/id&numero
    //Controlamos que el del producto no baje de 0 - en caso de ser asi se devolverá un bad request
    @PostMapping (value = "stock/id={id}&numStock={numStock}")
    public void updateStock (@PathVariable Long id,@PathVariable int numStock){
        Product product = productRepository.getById(id);
        int newStock = product.getStock()+ numStock;
        if(newStock<0){
            //Esto lo hacemos para que no se pueda cambiar el stock de un producto a uno negativo
            logger.info("Product stock cant be lower than 0. Resulting stock would be {}",newStock);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Product Stock cant be lower than 0");
        }else {
            product.setStock(newStock);
            logger.info("Changing stock in {} units", numStock);
            Product updatedProduct = productRepository.save(product);
            logger.info("Updated stock to: {}", updatedProduct.getStock());
        }

    }

}
