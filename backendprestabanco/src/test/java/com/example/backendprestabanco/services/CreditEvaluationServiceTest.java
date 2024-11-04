package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.*;
import com.example.backendprestabanco.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class CreditEvaluationServiceTest {

    @InjectMocks
    private CreditEvaluationService creditEvaluationService;

    @MockBean
    private CreditEvaluationRepository creditEvaluationRepository;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private DebtRepository debtRepository;
    @MockBean
    private SavingsAccountRepository savingsAccountRepository;
    @MockBean
    private AccountHistoryRepository accountHistoryRepository;

    private ClientEntity testClient;
    private SavingsAccountEntity testSavingsAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testClient = new ClientEntity();
        testClient.setRut("12345678-9");
        testClient.setIncome(50000.0);
        testClient.setAge(30);
        testClient.setHistoryStatus("Good");
        testClient.setPendingDebts(0);

        testSavingsAccount = new SavingsAccountEntity();
        testSavingsAccount.setRut(testClient.getRut());
        testSavingsAccount.setBalance(15000.0);
        testSavingsAccount.setOpeningDate(LocalDate.of(2018, 1, 1));
    }

    @Test
    public void whenSimulateCredit_thenReturnMonthlyPayment() {
        double monthlyPayment = creditEvaluationService.simulateCredit(100000, 5, 30);
        assertThat(monthlyPayment).isEqualTo(537.0);
    }

    @Test
    public void whenFeeIncomeRelation_thenReturnCorrectRelation() {
        double relation = creditEvaluationService.feeIncomeRelation(5000, 1500);
        assertThat(relation).isEqualTo(30.0);
    }

    @Test
    public void whenFeeIncomeRelationCondition_thenReturnFalseIfAboveThreshold() {
        boolean result = creditEvaluationService.feeIncomeRelationCondition(3000, 1200);
        assertThat(result).isFalse();
    }

    @Test
    public void whenCheckCreditHistory_thenReturnTrue() {
        given(clientRepository.findByRut("12345678-9")).willReturn(testClient);
        boolean result = creditEvaluationService.clientCreditHistoryIsGood(testClient.getRut());
        assertThat(result).isTrue();
    }

    @Test
    public void whenCheckAgeCondition_thenReturnFalseIfOver70() {
        testClient.setAge(75);
        given(clientRepository.findByRut("12345678-9")).willReturn(testClient);
        boolean result = creditEvaluationService.clientAgeCondition(testClient.getRut());
        assertThat(result).isFalse();
    }

    @Test
    public void whenCheckAgeCondition_thenReturnTrueIfUnder75() {
        testClient.setAge(30);
        given(clientRepository.findByRut("12345678-9")).willReturn(testClient);
        boolean result = creditEvaluationService.clientAgeCondition(testClient.getRut());
        assertThat(result).isTrue();
    }

    @Test
    public void whenDebtIncomeRelation_thenThrowExceptionIfClientNotFound() {
        given(clientRepository.findByRut("99999999-9")).willReturn(null);
        assertThatThrownBy(() -> creditEvaluationService.debtIncomeRelation("99999999-9"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cliente no encontrado");
    }

    @Test
    public void whenMaxFinancingAmountCondition_thenThrowExceptionForInvalidLoanType() {
        assertThatThrownBy(() -> creditEvaluationService.maxFinancingAmountCondition("invalid loan type", 80000, 100000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Tipo de préstamo no válido");
    }

    @Test
    public void whenMaxFinancingAmountCondition_thenReturnTrueForPrimaryResidence() {
        boolean result = creditEvaluationService.maxFinancingAmountCondition("primera vivienda", 80000, 100000);
        assertThat(result).isTrue();
    }

    @Test
    public void whenMaxFinancingAmountCondition_thenReturnFalseForSecondResidence() {
        boolean result = creditEvaluationService.maxFinancingAmountCondition("segunda vivienda", 80000, 100000);
        assertThat(result).isFalse();
    }

    @Test
    public void whenHasConsistentSavingsHistory_thenReturnTrue() {
        // Simulación de cuenta de ahorros para evitar excepción
        given(savingsAccountRepository.findByRut("12345678-9")).willReturn(testSavingsAccount);

        List<AccountHistoryEntity> transactions = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            AccountHistoryEntity deposit = new AccountHistoryEntity();
            deposit.setTransactionType("Depósito");
            deposit.setTransactionAmount(3000.0);
            deposit.setTransactionDate(LocalDate.now().minusMonths(i));
            deposit.setBalanceAfterTransaction(1500.0);
            transactions.add(deposit);
        }
        given(accountHistoryRepository.findByRutAndTransactionDateAfter("12345678-9", LocalDate.now().minusMonths(12)))
                .willReturn(transactions);

        boolean result = creditEvaluationService.hasConsistentSavingsHistory("12345678-9");
        assertThat(result).isTrue();
    }

    @Test
    public void whenHasConsistentSavingsHistory_thenReturnFalseForSignificantWithdrawal() {
        // Simulación de cuenta de ahorros para evitar excepción
        given(savingsAccountRepository.findByRut("12345678-9")).willReturn(testSavingsAccount);

        List<AccountHistoryEntity> transactions = new ArrayList<>();
        AccountHistoryEntity withdrawal = new AccountHistoryEntity();
        withdrawal.setTransactionType("Retiro");
        withdrawal.setTransactionAmount(10000.0);  // Más del 50% del saldo actual
        withdrawal.setTransactionDate(LocalDate.now().minusMonths(1));
        withdrawal.setBalanceAfterTransaction(5000.0);  // Asignación de valor para evitar NullPointerException
        transactions.add(withdrawal);

        given(accountHistoryRepository.findByRutAndTransactionDateAfter("12345678-9", LocalDate.now().minusMonths(12)))
                .willReturn(transactions);

        boolean result = creditEvaluationService.hasConsistentSavingsHistory("12345678-9");
        assertThat(result).isFalse();
    }


    @Test
    public void whenHasMinimumRequiredBalance_thenReturnTrue() {
        given(savingsAccountRepository.findByRut("12345678-9")).willReturn(testSavingsAccount);
        boolean result = creditEvaluationService.hasMinimumRequiredBalance("12345678-9", 100000);
        assertThat(result).isTrue();
    }

    @Test
    public void whenEvaluateSavingsCapacity_thenReturnSolid() {
        given(clientRepository.findByRut("12345678-9")).willReturn(testClient);
        given(savingsAccountRepository.findByRut("12345678-9")).willReturn(testSavingsAccount);

        List<AccountHistoryEntity> mockDeposits = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            AccountHistoryEntity deposit = new AccountHistoryEntity();
            deposit.setTransactionType("Depósito");
            deposit.setTransactionAmount(3000.0);
            deposit.setTransactionDate(LocalDate.now().minusMonths(i));
            mockDeposits.add(deposit);
        }
        given(accountHistoryRepository.findByRutAndTransactionTypeAndTransactionDateAfter("12345678-9", "Depósito", LocalDate.now().minusMonths(12)))
                .willReturn(mockDeposits);
        given(accountHistoryRepository.findByRutAndTransactionTypeAndTransactionDateAfter("12345678-9", "Retiro", LocalDate.now().minusMonths(6)))
                .willReturn(new ArrayList<>());

        String result = creditEvaluationService.evaluateSavingsCapacity(testClient.getRut(), 100000);
        assertThat(result).isEqualTo("Solid");
    }

    @Test
    public void whenDeleteCreditEvaluation_thenReturnTrue() throws Exception {
        String rut = "12345678-9";
        doNothing().when(creditEvaluationRepository).deleteByRut(rut);

        boolean isDeleted = creditEvaluationService.deleteCreditEvaluation(rut);
        assertThat(isDeleted).isTrue();
        verify(creditEvaluationRepository, times(1)).deleteByRut(rut);
    }

    // Prueba para clientCreditHistoryIsGood cuando el historial no es bueno
    @Test
    public void whenCheckCreditHistory_thenReturnFalseIfBadHistoryOrPendingDebts() {
        testClient.setHistoryStatus("Bad");
        testClient.setPendingDebts(1);
        given(clientRepository.findByRut("12345678-9")).willReturn(testClient);

        boolean result = creditEvaluationService.clientCreditHistoryIsGood("12345678-9");
        assertThat(result).isFalse();
    }

    // Prueba para debtIncomeRelation cuando el ratio es mayor al 50%
    @Test
    public void whenDebtIncomeRelation_thenReturnFalseIfDebtRatioExceedsLimit() {
        testClient.setIncome(2000.0);
        given(clientRepository.findByRut("12345678-9")).willReturn(testClient);

        List<DebtEntity> debts = new ArrayList<>();
        DebtEntity debt = new DebtEntity();
        debt.setTotalAmount(2000.0); // Relación deuda/ingreso será 1 (100%)
        debts.add(debt);
        given(debtRepository.findByRut("12345678-9")).willReturn(debts);

        boolean result = creditEvaluationService.debtIncomeRelation("12345678-9");
        assertThat(result).isFalse();
    }

    // Prueba para maxFinancingAmountCondition con tipos de préstamo adicionales
    @Test
    public void whenMaxFinancingAmountCondition_thenReturnTrueForCommercialProperty() {
        boolean result = creditEvaluationService.maxFinancingAmountCondition("propiedades comerciales", 60000, 100000);
        assertThat(result).isTrue();
    }

    @Test
    public void whenMaxFinancingAmountCondition_thenReturnTrueForRemodeling() {
        boolean result = creditEvaluationService.maxFinancingAmountCondition("remodelación", 50000, 100000);
        assertThat(result).isTrue();
    }

    // Prueba para hasMinimumRequiredBalance cuando el saldo es insuficiente
    @Test
    public void whenHasMinimumRequiredBalance_thenReturnFalseIfInsufficientBalance() {
        testSavingsAccount.setBalance(5000.0); // Insuficiente para cumplir el requisito de 10% del monto
        given(savingsAccountRepository.findByRut("12345678-9")).willReturn(testSavingsAccount);

        boolean result = creditEvaluationService.hasMinimumRequiredBalance("12345678-9", 100000);
        assertThat(result).isFalse();
    }

    // Prueba para hasRegularDeposits cuando el cliente no existe
    @Test
    public void whenHasRegularDeposits_thenThrowExceptionIfClientNotFound() {
        given(clientRepository.findByRut("99999999-9")).willReturn(null);

        assertThatThrownBy(() -> creditEvaluationService.hasRegularDeposits("99999999-9"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cliente no encontrado para el RUT: 99999999-9");
    }

    // Prueba para hasRequiredBalanceForTenure cuando la cuenta no existe
    @Test
    public void whenHasRequiredBalanceForTenure_thenThrowExceptionIfAccountNotFound() {
        given(savingsAccountRepository.findByRut("99999999-9")).willReturn(null);

        assertThatThrownBy(() -> creditEvaluationService.hasRequiredBalanceForTenure("99999999-9", 100000))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cuenta de ahorros no encontrada para el cliente con RUT: 99999999-9");
    }

    // Prueba para hasSignificantRecentWithdrawals cuando hay un retiro significativo
    @Test
    public void whenHasSignificantRecentWithdrawals_thenReturnFalseForSignificantWithdrawal() {
        given(savingsAccountRepository.findByRut("12345678-9")).willReturn(testSavingsAccount);

        List<AccountHistoryEntity> withdrawals = new ArrayList<>();
        AccountHistoryEntity withdrawal = new AccountHistoryEntity();
        withdrawal.setTransactionType("Retiro");
        withdrawal.setTransactionAmount(6000.0); // Más del 30% del saldo actual de 15000
        withdrawal.setTransactionDate(LocalDate.now().minusMonths(1));
        withdrawals.add(withdrawal);

        given(accountHistoryRepository.findByRutAndTransactionTypeAndTransactionDateAfter("12345678-9", "Retiro", LocalDate.now().minusMonths(6)))
                .willReturn(withdrawals);

        boolean result = creditEvaluationService.hasSignificantRecentWithdrawals("12345678-9");
        assertThat(result).isFalse();
    }

    // Prueba para evaluateSavingsCapacity cuando se cumplen de 3 a 4 reglas
    @Test
    public void whenEvaluateSavingsCapacity_thenReturnModerateIfThreeRulesMet() {
        given(clientRepository.findByRut("12345678-9")).willReturn(testClient);
        given(savingsAccountRepository.findByRut("12345678-9")).willReturn(testSavingsAccount);

        List<AccountHistoryEntity> deposits = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            AccountHistoryEntity deposit = new AccountHistoryEntity();
            deposit.setTransactionType("Depósito");
            deposit.setTransactionAmount(3000.0);
            deposit.setTransactionDate(LocalDate.now().minusMonths(i));
            deposits.add(deposit);
        }
        given(accountHistoryRepository.findByRutAndTransactionTypeAndTransactionDateAfter("12345678-9", "Depósito", LocalDate.now().minusMonths(12)))
                .willReturn(deposits);

        given(accountHistoryRepository.findByRutAndTransactionTypeAndTransactionDateAfter("12345678-9", "Retiro", LocalDate.now().minusMonths(6)))
                .willReturn(Collections.emptyList());

        String result = creditEvaluationService.evaluateSavingsCapacity(testClient.getRut(), 100000);
        assertThat(result).isEqualTo("Moderate");
    }

    // Prueba para evaluateSavingsCapacity cuando se cumplen menos de 3 reglas
    @Test
    public void whenEvaluateSavingsCapacity_thenReturnWeakIfLessThanThreeRulesMet() {
        testSavingsAccount.setBalance(2000.0); // Saldo insuficiente para la regla 1
        given(clientRepository.findByRut("12345678-9")).willReturn(testClient);
        given(savingsAccountRepository.findByRut("12345678-9")).willReturn(testSavingsAccount);

        List<AccountHistoryEntity> deposits = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            AccountHistoryEntity deposit = new AccountHistoryEntity();
            deposit.setTransactionType("Depósito");
            deposit.setTransactionAmount(100.0); // Depósito insuficiente para regla 3
            deposit.setTransactionDate(LocalDate.now().minusMonths(i));
            deposits.add(deposit);
        }
        given(accountHistoryRepository.findByRutAndTransactionTypeAndTransactionDateAfter("12345678-9", "Depósito", LocalDate.now().minusMonths(12)))
                .willReturn(deposits);

        given(accountHistoryRepository.findByRutAndTransactionTypeAndTransactionDateAfter("12345678-9", "Retiro", LocalDate.now().minusMonths(6)))
                .willReturn(Collections.emptyList());

        String result = creditEvaluationService.evaluateSavingsCapacity("12345678-9", 100000);
        assertThat(result).isEqualTo("Weak");
    }


}
