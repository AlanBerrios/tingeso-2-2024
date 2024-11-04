package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.DebtEntity;
import com.example.backendprestabanco.repositories.DebtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class DebtServiceTest {

    @InjectMocks
    private DebtService debtService;

    @MockBean
    private DebtRepository debtRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetAllDebts_thenReturnDebtList() {
        List<DebtEntity> debts = List.of(new DebtEntity());
        given(debtRepository.findAll()).willReturn(debts);

        List<DebtEntity> result = debtService.getAllDebts();

        assertThat(result).isNotNull().hasSize(1);
    }

    @Test
    void whenSaveDebt_thenReturnSavedDebt() {
        DebtEntity debt = new DebtEntity();
        given(debtRepository.save(debt)).willReturn(debt);

        DebtEntity result = debtService.saveDebt(debt);

        assertThat(result).isNotNull();
    }

    @Test
    void whenGetDebtsByRut_thenReturnDebtList() {
        String rut = "12345678-9";
        List<DebtEntity> debts = List.of(new DebtEntity());
        given(debtRepository.findByRut(rut)).willReturn(debts);

        List<DebtEntity> result = debtService.getDebtsByRut(rut);

        assertThat(result).isNotNull().hasSize(1);
    }

    @Test
    void whenUpdateDebt_thenReturnUpdatedDebt() {
        DebtEntity debt = new DebtEntity();
        debt.setId(1L);
        debt.setTotalAmount(1000.0);

        given(debtRepository.save(debt)).willReturn(debt);

        DebtEntity result = debtService.updateDebt(debt);

        assertThat(result).isNotNull();
        assertThat(result.getTotalAmount()).isEqualTo(1000.0);
    }

    @Test
    void whenDeleteDebt_thenReturnTrueIfSuccessful() {
        Long debtId = 1L;
        doNothing().when(debtRepository).deleteById(debtId);

        boolean result = debtService.deleteDebt(debtId);

        assertThat(result).isTrue();
        verify(debtRepository, times(1)).deleteById(debtId);
    }

    @Test
    void whenDeleteDebt_thenReturnFalseIfDeletionFails() {
        Long debtId = 1L;
        doThrow(new RuntimeException("Error deleting debt")).when(debtRepository).deleteById(debtId);

        boolean result = debtService.deleteDebt(debtId);

        assertThat(result).isFalse();
        verify(debtRepository, times(1)).deleteById(debtId);
    }
}
