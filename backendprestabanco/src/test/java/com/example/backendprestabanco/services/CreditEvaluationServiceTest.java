package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.ClientEntity;
import com.example.backendprestabanco.entities.AccountHistoryEntity;
import com.example.backendprestabanco.entities.SavingsAccountEntity;
import com.example.backendprestabanco.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class CreditEvaluationServiceTest {

    @Mock
    private CreditEvaluationRepository creditEvaluationRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SavingsAccountRepository savingsAccountRepository;

    @Mock
    private AccountHistoryRepository accountHistoryRepository;

    @Mock
    private DebtRepository debtRepository;

    @Autowired
    @InjectMocks
    private CreditEvaluationService creditEvaluationService;

    private ClientEntity testClient;
    private SavingsAccountEntity testSavingsAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Inicializar cliente y cuenta de ahorros de prueba
        testClient = new ClientEntity();
        testClient.setRut("12345678-9");
        testClient.setIncome(50000.0);
        testClient.setAge(30);

        testSavingsAccount = new SavingsAccountEntity();
        testSavingsAccount.setRut(testClient.getRut());
        testSavingsAccount.setBalance(15000.0);
        testSavingsAccount.setOpeningDate(LocalDate.of(2018, 1, 1));
    }

    @Test
    public void whenSimulateCredit_thenReturnMonthlyPayment() {
        double monthlyPayment = creditEvaluationService.simulateCredit(100000, 5, 30);
        assertThat(monthlyPayment).isCloseTo(536.82, within(0.01)); // Tolerancia de 0.01
    }

    @Test
    public void whenFeeIncomeRelation_thenReturnCorrectRelation() {
        double relation = creditEvaluationService.feeIncomeRelation(5000, 1500);
        assertThat(relation).isEqualTo(30.0);
    }

    @Test
    public void whenCheckCreditHistory_thenReturnTrue() {
        // Configurar mock para el repositorio de clientes
        testClient.setHistoryStatus("Good");
        testClient.setPendingDebts(0);
        given(clientRepository.findByRut("12345678-9")).willReturn(testClient);

        boolean result = creditEvaluationService.clientCreditHistoryIsGood(testClient.getRut());
        assertThat(result).isTrue();
    }

    @Test
    public void whenCheckAgeCondition_thenReturnFalseIfOver70() {
        // Modificar la edad del cliente a más de 70
        testClient.setAge(75);
        given(clientRepository.findByRut("12345678-9")).willReturn(testClient);

        boolean result = creditEvaluationService.clientAgeCondition(testClient.getRut());
        assertThat(result).isFalse();
    }

    @Test
    public void whenHasRegularDeposits_thenReturnTrue() {
        // Mock the ClientRepository to return a valid client
        ClientEntity mockClient = new ClientEntity();
        mockClient.setRut("12345678-9");
        mockClient.setIncome(50000.0); // Set the income required for deposit calculations
        given(clientRepository.findByRut("12345678-9")).willReturn(mockClient);  // Simulación del cliente

        // Mock account history for regular deposits
        List<AccountHistoryEntity> mockDeposits = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            AccountHistoryEntity deposit = new AccountHistoryEntity();
            deposit.setTransactionType("Depósito");
            deposit.setTransactionAmount(3000.0); // Set deposit amount to match the deposit requirements
            deposit.setTransactionDate(LocalDate.now().minusMonths(i)); // Recent transaction for last 12 months
            mockDeposits.add(deposit);
        }
        given(accountHistoryRepository.findByRutAndTransactionTypeAndTransactionDateAfter(anyString(), eq("Depósito"), any()))
                .willReturn(mockDeposits);

        // Execute the method under test
        boolean result = creditEvaluationService.hasRegularDeposits("12345678-9");

        // Verify the result
        assertThat(result).isTrue();
    }


    @Test
    public void whenEvaluateSavingsCapacity_thenReturnSolid() {
        // Simular cliente en el repositorio de clientes
        given(clientRepository.findByRut("12345678-9")).willReturn(testClient);

        // Simular cuenta de ahorros con saldo suficiente
        given(savingsAccountRepository.findByRut("12345678-9")).willReturn(testSavingsAccount);

        // Simular historial de depósitos
        List<AccountHistoryEntity> mockDeposits = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            AccountHistoryEntity deposit = new AccountHistoryEntity();
            deposit.setRut(testClient.getRut());
            deposit.setTransactionType("Depósito");
            deposit.setTransactionAmount(3000.0);
            deposit.setTransactionDate(LocalDate.now().minusMonths(i));
            mockDeposits.add(deposit);
        }
        given(accountHistoryRepository.findByRutAndTransactionTypeAndTransactionDateAfter("12345678-9", "Depósito", LocalDate.now().minusMonths(12)))
                .willReturn(mockDeposits);

        // Simular condición para no retiros significativos
        given(accountHistoryRepository.findByRutAndTransactionTypeAndTransactionDateAfter("12345678-9", "Retiro", LocalDate.now().minusMonths(6)))
                .willReturn(new ArrayList<>());

        String result = creditEvaluationService.evaluateSavingsCapacity(testClient.getRut(), 100000);
        assertThat(result).isEqualTo("Solid");
    }

    @Test
    public void whenHasMinimumRequiredBalance_thenReturnTrue() {
        // Simular cuenta de ahorros con saldo suficiente
        given(savingsAccountRepository.findByRut("12345678-9")).willReturn(testSavingsAccount);

        boolean result = creditEvaluationService.hasMinimumRequiredBalance("12345678-9", 100000);
        assertThat(result).isTrue();
    }

    @Test
    public void whenHasMinimumRequiredBalance_thenReturnFalse() {
        // Simular cuenta de ahorros con saldo insuficiente
        testSavingsAccount.setBalance(5000.0);
        given(savingsAccountRepository.findByRut("12345678-9")).willReturn(testSavingsAccount);

        boolean result = creditEvaluationService.hasMinimumRequiredBalance("12345678-9", 100000);
        assertThat(result).isFalse();
    }
}
