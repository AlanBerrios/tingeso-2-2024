package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.ClientEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ClientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void whenFindByRut_thenReturnClient() {
        // given
        ClientEntity client = new ClientEntity("12345678-9", "John", "Doe", "john.doe@gmail.com", "123456789", 50000.0, "Good", 30, "Employee", 5);
        entityManager.persistAndFlush(client);

        // when
        ClientEntity found = clientRepository.findByRut(client.getRut());

        // then
        assertThat(found.getRut()).isEqualTo(client.getRut());
    }

    @Test
    public void whenFindByEmail_thenReturnClient() {
        // given
        ClientEntity client = new ClientEntity("98765432-1", "Jane", "Doe", "jane.doe@gmail.com", "987654321", 60000.0, "Good", 25, "Employee", 3);
        entityManager.persistAndFlush(client);

        // when
        ClientEntity found = clientRepository.findByRut(client.getRut());

        // then
        assertThat(found.getEmail()).isEqualTo("jane.doe@gmail.com");
    }
}
