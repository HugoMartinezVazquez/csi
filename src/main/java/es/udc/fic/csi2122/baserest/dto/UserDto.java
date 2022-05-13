package es.udc.fic.csi2122.baserest.dto;

/**
 * Dto class for controller responses
 * 
 * @param name the user name
 * @param age  the user age
 * 
 * @author alfonso.landin@udc.es
 */
public record UserDto(String name, int age) {
}
