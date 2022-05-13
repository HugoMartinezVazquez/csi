package es.udc.fic.csi2122.baserest.dto;

//Clase Dto para client

public record ClientDto(String name, String surname, int age, String dni, String address, String paymentMethod) {
}
