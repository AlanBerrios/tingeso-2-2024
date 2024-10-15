package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.ClientEntity;
import com.example.backendprestabanco.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class ClientServiceTest {

    @MockBean
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetClients_thenReturnClientList() {
        // given
        ClientEntity client1 = new ClientEntity("12345678-9", "Alex", "Garcia", "alex@gmail.com", "123456789", 50000.0, "Good", 30, "Employee", 5, "Clear", 0);
        ClientEntity client2 = new ClientEntity("98765432-1", "Beatriz", "Miranda", "beatriz@gmail.com", "987654321", 60000.0, "Good", 25, "Self-Employed", 3, "Clear", 0);

        List<ClientEntity> clientList = new ArrayList<>();
        clientList.add(client1);
        clientList.add(client2);

        given(clientRepository.findAll()).willReturn(clientList);

        // when
        List<ClientEntity> foundClients = clientService.getClients();

        // then
        assertThat(foundClients).hasSize(2);
        assertThat(foundClients).extracting(ClientEntity::getFirstName).contains("Alex", "Beatriz");
    }

    @Test
    public void whenGetClientByRut_thenReturnClient() {
        // given
        ClientEntity client = new ClientEntity("12345678-9", "Alex", "Garcia", "alex@gmail.com", "123456789", 50000.0, "Good", 30, "Employee", 5, "Clear", 0);
        given(clientRepository.findByRut(client.getRut())).willReturn(client);

        // when
        ClientEntity foundClient = clientService.getClientByRut("12345678-9");

        // then
        assertThat(foundClient.getFirstName()).isEqualTo("Alex");
    }

    @Test
    public void whenSaveClient_thenReturnSavedClient() {
        // given
        ClientEntity clientToSave = new ClientEntity("17.777.457-8", "Esteban", "Marquez", "esteban@gmail.com", "123456789", 40000.0, "Fair", 35, "Employee", 10, "Clear", 0);
        given(clientRepository.save(clientToSave)).willReturn(clientToSave);

        // when
        ClientEntity savedClient = clientService.saveClient(clientToSave);

        // then
        assertThat(savedClient.getRut()).isEqualTo("17.777.457-8");
        assertThat(savedClient.getFirstName()).isEqualTo("Esteban");
    }

    @Test
    public void whenUpdateClient_thenReturnUpdatedClient() {
        // given
        ClientEntity clientToUpdate = new ClientEntity("12.345.678-9", "Marco", "Jimenez", "marco@gmail.com", "987654321", 45000.0, "Good", 40, "Self-Employed", 15, "Clear", 0);
        given(clientRepository.save(clientToUpdate)).willReturn(clientToUpdate);

        // when
        ClientEntity updatedClient = clientService.updateClient(clientToUpdate);

        // then
        assertThat(updatedClient.getFirstName()).isEqualTo("Marco");
    }

    @Test
    public void whenDeleteClient_thenReturnTrue() throws Exception {
        // given
        String rut = "12345678-9";
        doNothing().when(clientRepository).deleteByRut(rut);

        // when
        boolean isDeleted = clientService.deleteClient(rut);

        // then
        assertThat(isDeleted).isTrue();
        verify(clientRepository, times(1)).deleteByRut(rut);
    }
}
