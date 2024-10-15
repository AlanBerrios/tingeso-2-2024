package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.ClientEntity;
import com.example.backendprestabanco.services.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Test
    public void listClients_ShouldReturnClients() throws Exception {
        // given
        ClientEntity client1 = new ClientEntity("12345678-9", "Alex", "Garcia", "alex@gmail.com", "123456789", 50000.0, "Good", 30, "Employee", 5, "Clear", 0);
        ClientEntity client2 = new ClientEntity("98765432-1", "Beatriz", "Miranda", "beatriz@gmail.com", "987654321", 60000.0, "Good", 25, "Self-Employed", 3, "Clear", 0);

        List<ClientEntity> clientList = new ArrayList<>(Arrays.asList(client1, client2));

        given(clientService.getClients()).willReturn((ArrayList<ClientEntity>) clientList);

        // when + then
        mockMvc.perform(get("/api/v1/clients/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Alex")))
                .andExpect(jsonPath("$[1].firstName", is("Beatriz")));
    }

    @Test
    public void getClientByRut_ShouldReturnClient() throws Exception {
        // given
        ClientEntity client = new ClientEntity("12345678-9", "Alex", "Garcia", "alex@gmail.com", "123456789", 50000.0, "Good", 30, "Employee", 5, "Clear", 0);

        given(clientService.getClientByRut("12345678-9")).willReturn(client);

        // when + then
        mockMvc.perform(get("/api/v1/clients/{rut}", "12345678-9"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("Alex")));
    }

    @Test
    public void saveClient_ShouldReturnSavedClient() throws Exception {
        // given
        ClientEntity savedClient = new ClientEntity("17.777.457-8", "Esteban", "Marquez", "esteban@gmail.com", "123456789", 40000.0, "Fair", 35, "Employee", 10, "Clear", 0);

        given(clientService.saveClient(Mockito.any(ClientEntity.class))).willReturn(savedClient);

        String clientJson = """
            {
                "rut": "17.777.457-8",
                "firstName": "Esteban",
                "lastName": "Marquez",
                "email": "esteban@gmail.com",
                "phone": "123456789",
                "income": 40000.0,
                "creditHistory": "Fair",
                "age": 35,
                "employmentType": "Employee",
                "employmentSeniority": 10,
                "historyStatus": "Clear",
                "pendingDebts": 0
            }
            """;

        // when + then
        mockMvc.perform(post("/api/v1/clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Esteban")));
    }

    @Test
    public void updateClient_ShouldReturnUpdatedClient() throws Exception {
        // given
        ClientEntity updatedClient = new ClientEntity("12.345.678-9", "Marco", "Jimenez", "marco@gmail.com", "987654321", 45000.0, "Good", 40, "Self-Employed", 15, "Clear", 0);

        given(clientService.updateClient(Mockito.any(ClientEntity.class))).willReturn(updatedClient);

        String clientJson = """
            {
                "rut": "12.345.678-9",
                "firstName": "Marco",
                "lastName": "Jimenez",
                "email": "marco@gmail.com",
                "phone": "987654321",
                "income": 45000.0,
                "creditHistory": "Good",
                "age": 40,
                "employmentType": "Self-Employed",
                "employmentSeniority": 15,
                "historyStatus": "Clear",
                "pendingDebts": 0
            }
            """;

        // when + then
        mockMvc.perform(put("/api/v1/clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Marco")));
    }

    @Test
    public void deleteClientByRut_ShouldReturn204() throws Exception {
        // given
        given(clientService.deleteClient("12345678-9")).willReturn(true);

        // when + then
        mockMvc.perform(delete("/api/v1/clients/{rut}", "12345678-9"))
                .andExpect(status().isNoContent());
    }
}
