package es.udc.fic.csi2122.baserest.conversors;

import es.udc.fic.csi2122.baserest.dto.ProductDto;
import es.udc.fic.csi2122.baserest.entity.Product;


import java.util.List;

public class ProductConversors {

    //Ponemos un constructor privado para evitar que lo instancien
    private ProductConversors(){
    }

    //conversor de product a dto
    public static ProductDto toProductDto (Product product){
        return new ProductDto(product.getName(),product.getPrice(),product.getStock(),product.getDescription());
    }

    //Conversor de lista de product a lista de Dto
    public static List<ProductDto> toProductDtoList (List<Product> product) {
        return product.stream().map(ProductConversors::toProductDto).toList();
    }

    //Conversor de dto a product

    public static Product toProduct (ProductDto productDto){
        return new Product(productDto.name(),productDto.price(),productDto.stock(),productDto.description());
    }
}
