package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.ClientEntity;
import com.example.backendprestabanco.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;

    public ArrayList<ClientEntity> getClients() {
        return (ArrayList<ClientEntity>) clientRepository.findAll();
    }

    public ClientEntity saveClient(ClientEntity client) {
        return clientRepository.save(client);
    }

    public ClientEntity getClientByRut(String rut) {
        return clientRepository.findByRut(rut);
    }

    public ClientEntity updateClient(ClientEntity client) {
        return clientRepository.save(client);
    }

    public boolean deleteClient(String rut) throws Exception {
        if (clientRepository.existsByRut(rut)) { // Verificar si el cliente existe antes de eliminar
            clientRepository.deleteByRut(rut);
            return true;
        } else {
            throw new IllegalArgumentException("Error al eliminar el cliente: Cliente con RUT no encontrado: " + rut);
        }
    }



}
