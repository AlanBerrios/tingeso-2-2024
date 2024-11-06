package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.ClientEntity;
import com.example.backendprestabanco.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@CrossOrigin("*")
public class ClientController {
	// Desde el controlador solo puede llamar a clases del servicio (OBLIGATORIO)
	@Autowired
	ClientService clientService;

	@GetMapping("/")
	public ResponseEntity<List<ClientEntity>> listClients() {
		List<ClientEntity> clients = clientService.getClients();
		return ResponseEntity.ok(clients);
	}

	@GetMapping("/{rut}")
	public ResponseEntity<ClientEntity> getClientByRut(@PathVariable String rut) {
		ClientEntity client = clientService.getClientByRut(rut);
		return ResponseEntity.ok(client);
	}

	@PostMapping("/")
	public ResponseEntity<ClientEntity> saveClient(@RequestBody ClientEntity client) {
		ClientEntity newClient = clientService.saveClient(client);
		return ResponseEntity.ok(newClient);
	}

	@PutMapping("/")
	public ResponseEntity<ClientEntity> updateClient(@RequestBody ClientEntity client) {
		ClientEntity updatedClient = clientService.updateClient(client);
		return ResponseEntity.ok(updatedClient);
	}

	@DeleteMapping("/{rut}")
	public ResponseEntity<Void> deleteClient(@PathVariable String rut) {
		boolean isDeleted = clientService.deleteClient(rut);
		if (isDeleted) {
			return ResponseEntity.noContent().build(); // Retorna 204 No Content si se elimina correctamente
		} else {
			return ResponseEntity.notFound().build(); // Retorna 404 si no se encuentra el cliente
		}
	}




}
