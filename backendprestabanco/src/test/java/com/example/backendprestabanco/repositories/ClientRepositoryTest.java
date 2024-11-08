package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.ClientEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
        ClientEntity client = new ClientEntity("12345678-9", "John", "Doe", "john.doe@gmail.com","password", "123456789", 50000.0, "Good", 30, "Employee", 5, "Clear", 0);
        entityManager.persistAndFlush(client);

        // when
        ClientEntity found = clientRepository.findByRut(client.getRut());

        // then
        assertThat(found.getRut()).isEqualTo(client.getRut());
    }

    @Test
    public void whenFindByIncomeGreaterThan_thenReturnClients() {
        // given
        ClientEntity client1 = new ClientEntity("12345678-9", "John", "Doe", "john.doe@gmail.com","password", "123456789", 30000.0, "Good", 30, "Employee", 5, "Clear", 0);
        ClientEntity client2 = new ClientEntity("98765432-1", "Jane", "Doe", "jane.doe@gmail.com","contrasenia", "987654321", 60000.0, "Good", 25, "Employee", 3, "Clear", 0);
        entityManager.persist(client1);
        entityManager.persist(client2);
        entityManager.flush();

        // when
        List<ClientEntity> foundClients = clientRepository.findByIncomeGreaterThan(50000);

        // then
        assertThat(foundClients).hasSize(1).extracting(ClientEntity::getFirstName).containsOnly("Jane");
    }

    @Test
    public void whenFindByEmploymentType_thenReturnClients() {
        // given
        ClientEntity client1 = new ClientEntity("12345678-9", "John", "Doe", "john.doe@gmail.com","password", "123456789", 50000.0, "Good", 30, "Employee", 5, "Clear", 0);
        ClientEntity client2 = new ClientEntity("98765432-1", "Jane", "Doe", "jane.doe@gmail.com","password", "987654321", 60000.0, "Good", 25, "Self-Employed", 3, "Clear", 0);
        entityManager.persist(client1);
        entityManager.persist(client2);
        entityManager.flush();

        // when
        List<ClientEntity> foundClients = clientRepository.findByEmploymentType("Self-Employed");

        // then
        assertThat(foundClients).hasSize(1).extracting(ClientEntity::getFirstName).containsOnly("Jane");
    }
}
