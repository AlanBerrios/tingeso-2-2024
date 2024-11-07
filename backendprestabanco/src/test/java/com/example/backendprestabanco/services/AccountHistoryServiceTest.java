package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.AccountHistoryEntity;
import com.example.backendprestabanco.repositories.AccountHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountHistoryServiceTest {

    @MockBean
    private AccountHistoryRepository accountHistoryRepository;

    @Autowired
    private AccountHistoryService accountHistoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetAccountHistoryByRut_thenReturnHistoryList() {
        String rut = "12345678-9";
        List<AccountHistoryEntity> historyList = new ArrayList<>();
        historyList.add(new AccountHistoryEntity());

        given(accountHistoryRepository.findByRut(rut)).willReturn(historyList);

        List<AccountHistoryEntity> result = accountHistoryService.getAccountHistoryByRut(rut);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void whenSaveAccountHistory_thenReturnSavedHistory() {
        AccountHistoryEntity history = new AccountHistoryEntity();
        history.setRut("21.055.282-0");
        history.setAccountType("Ahorros");
        history.setTransactionType("Depósito");
        history.setTransactionAmount(10000.0);
        history.setBalanceAfterTransaction(25000.0);
        history.setTransactionDate(LocalDate.now());
        history.setTransactionTime(LocalTime.now());

        // Simular la ejecución de la consulta nativa sin retorno
        doNothing().when(accountHistoryRepository).saveTransactionNativeQuery(
                history.getRut(),
                history.getAccountType(),
                history.getTransactionType(),
                history.getTransactionAmount(),
                history.getBalanceAfterTransaction(),
                history.getTransactionDate(),
                history.getTransactionTime()
        );

        accountHistoryService.saveAccountHistory(history);

        // Verificar que el método fue llamado correctamente
        verify(accountHistoryRepository, times(1)).saveTransactionNativeQuery(
                history.getRut(),
                history.getAccountType(),
                history.getTransactionType(),
                history.getTransactionAmount(),
                history.getBalanceAfterTransaction(),
                history.getTransactionDate(),
                history.getTransactionTime()
        );
    }


}
