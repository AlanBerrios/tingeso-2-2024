package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.CreditEvaluationEntity;
import jakarta.transaction.Transactional;
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

    @Transactional
    @Test
    public void whenFindByRut_thenReturnCreditEvaluation() {
        // given
        CreditEvaluationEntity evaluation = new CreditEvaluationEntity(1L, "12345678-9", 0.3, "Good", 5, 0.2, true, "Approved", LocalDate.now());
        entityManager.merge(evaluation);  // Usar merge en lugar de persist

        // when
        CreditEvaluationEntity found = creditEvaluationRepository.findByRut("12345678-9");

        // then
        assertThat(found.getRut()).isEqualTo(evaluation.getRut());
    }

    @Transactional
    @Test
    public void whenFindByRutNativeQuery_thenReturnCreditEvaluation() {
        // given
        CreditEvaluationEntity evaluation = new CreditEvaluationEntity(2L, "98765432-1", 0.4, "Bad", 3, 0.5, false, "Rejected", LocalDate.now());
        entityManager.merge(evaluation);  // Usar merge en lugar de persist

        // when
        CreditEvaluationEntity found = creditEvaluationRepository.findByRutNativeQuery("98765432-1");

        // then
        assertThat(found.getRut()).isEqualTo(evaluation.getRut());
        assertThat(found.getCreditHistory()).isEqualTo("Bad");
    }

    @Transactional
    @Test
    public void whenFindByPaymentToIncomeRatioGreaterThan_thenReturnEvaluations() {
        // given
        CreditEvaluationEntity evaluation1 = new CreditEvaluationEntity(1L, "12345678-9", 0.3, "Good", 5, 0.2, true, "Approved", LocalDate.now());
        CreditEvaluationEntity evaluation2 = new CreditEvaluationEntity(2L, "98765432-1", 0.6, "Bad", 3, 0.5, false, "Rejected", LocalDate.now());

        entityManager.merge(evaluation1);  // Usar merge en lugar de persist
        entityManager.merge(evaluation2);
        entityManager.flush();

        // when
        List<CreditEvaluationEntity> foundEvaluations = creditEvaluationRepository.findByPaymentToIncomeRatioGreaterThan(0.4);

        // then
        assertThat(foundEvaluations).isNotEmpty();
        assertThat(foundEvaluations).hasSize(1);
        assertThat(foundEvaluations.get(0).getRut()).isEqualTo("98765432-1");
    }

    @Transactional
    @Test
    public void whenDeleteByRut_thenEvaluationShouldBeDeleted() {
        // given
        CreditEvaluationEntity evaluation = new CreditEvaluationEntity(1L, "12345678-9", 0.3, "Good", 5, 0.2, true, "Approved", LocalDate.now());
        entityManager.merge(evaluation);  // Usar merge en lugar de persist

        // when
        creditEvaluationRepository.deleteByRut("12345678-9");
        CreditEvaluationEntity deletedEvaluation = creditEvaluationRepository.findByRut("12345678-9");

        // then
        assertThat(deletedEvaluation).isNull();
    }
}
