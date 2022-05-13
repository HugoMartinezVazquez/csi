package es.udc.fic.csi2122.baserest.conversors;

import java.util.List;

import es.udc.fic.csi2122.baserest.dto.ClientDto;
import es.udc.fic.csi2122.baserest.entity.Client;


public class ClientConversors {

    //Evitamos instanciar con un constructor privado
    private ClientConversors() {
    }

    //Conversor de client a dto
    public static ClientDto toClientDto(Client client) {
        return new ClientDto(client.getName(), client.getSurname(),client.getAge(),  client.getDni(), client.getAdress(), client.getPaymentMethod());
    }

    //Conversor de lista de client a lista de Dto
    public static List<ClientDto> toClientDtoList(List<Client> clients) {
        return clients.stream().map(ClientConversors::toClientDto).toList();
    }

    //Conversor de dto a client
    public static Client toClient(ClientDto client) {
        return new Client(client.name(), client.surname(), client.age(), client.dni(), client.address(), client.paymentMethod());
    }
}
