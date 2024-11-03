package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.CreditEvaluationEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
@ActiveProfiles("test")
class CreditEvaluationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CreditEvaluationRepository creditEvaluationRepository;

    @Test
    public void whenFindByRut_thenReturnCreditEvaluation() {
        // given
        CreditEvaluationEntity evaluation = new CreditEvaluationEntity(1L, "12345678-9", 0.3, "Good", 5, 0.2, true, "Approved", LocalDate.now());
        entityManager.persistAndFlush(evaluation);

        // when
        CreditEvaluationEntity found = creditEvaluationRepository.findByRut("12345678-9");

        // then
        assertThat(found.getRut()).isEqualTo(evaluation.getRut());
    }

    @Test
    public void whenFindByRutNativeQuery_thenReturnCreditEvaluation() {
        // given
        CreditEvaluationEntity evaluation = new CreditEvaluationEntity(2L, "98765432-1", 0.4, "Bad", 3, 0.5, false, "Rejected", LocalDate.now());
        entityManager.persistAndFlush(evaluation);

        // when
        CreditEvaluationEntity found = creditEvaluationRepository.findByRutNativeQuery("98765432-1");

        // then
        assertThat(found.getRut()).isEqualTo(evaluation.getRut());
        assertThat(found.getCreditHistory()).isEqualTo("Bad");
    }

    @Test
    public void whenFindByPaymentToIncomeRatioGreaterThan_thenReturnEvaluations() {
        // given
        CreditEvaluationEntity evaluation1 = new CreditEvaluationEntity(1L, "12345678-9", 0.3, "Good", 5, 0.2, true, "Approved", LocalDate.now());
        CreditEvaluationEntity evaluation2 = new CreditEvaluationEntity(2L, "98765432-1", 0.6, "Bad", 3, 0.5, false, "Rejected", LocalDate.now());

        entityManager.persist(evaluation1);
        entityManager.persist(evaluation2);
        entityManager.flush();

        // when
        List<CreditEvaluationEntity> foundEvaluations = creditEvaluationRepository.findByPaymentToIncomeRatioGreaterThan(0.4);

        // then
        assertThat(foundEvaluations).isNotEmpty(); // Asegúrate de que no esté vacío
        assertThat(foundEvaluations).hasSize(1); // Debería haber una sola evaluación con ratio > 0.4
        assertThat(foundEvaluations.get(0).getRut()).isEqualTo("98765432-1"); // Verificar que sea el correcto
    }


    @Test
    public void whenDeleteByRut_thenEvaluationShouldBeDeleted() {
        // given
        CreditEvaluationEntity evaluation = new CreditEvaluationEntity(1L, "12345678-9", 0.3, "Good", 5, 0.2, true, "Approved", LocalDate.now());
        entityManager.persistAndFlush(evaluation);

        // when
        creditEvaluationRepository.deleteByRut("12345678-9");
        CreditEvaluationEntity deletedEvaluation = creditEvaluationRepository.findByRut("12345678-9");

        // then
        assertThat(deletedEvaluation).isNull();
    }
}
