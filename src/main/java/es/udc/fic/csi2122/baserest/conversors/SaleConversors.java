package es.udc.fic.csi2122.baserest.conversors;

import es.udc.fic.csi2122.baserest.dto.SaleDto;
import es.udc.fic.csi2122.baserest.entity.Sale;


import java.util.List;

public class SaleConversors {

    //Constructor privado
    private SaleConversors(){
    }

    //Venta --> Dto
    public static SaleDto toSaleDto (Sale sale){
        return new SaleDto(sale.getProdId(),sale.getClientId(),sale.getUnit(),sale.getCost(),sale.getTax());
    }

    //Lista de Venta --> Dto
    public static List<SaleDto> toSaleDtoList (List<Sale> sale) {
        return sale.stream().map(SaleConversors::toSaleDto).toList();
    }

    //Dto --> Venta
    public static Sale toSale (SaleDto saleDto){
        return new Sale(saleDto.prodId(),saleDto.clientId(),saleDto.unit(),saleDto.cost(),saleDto.tax());
    }
}
