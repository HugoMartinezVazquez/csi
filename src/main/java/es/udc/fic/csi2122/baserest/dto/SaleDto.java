package es.udc.fic.csi2122.baserest.dto;


/*
 * Venta clase Dto
 */

public record SaleDto (Long prodId, Long clientId, Integer unit, Float cost, Float tax){

}
