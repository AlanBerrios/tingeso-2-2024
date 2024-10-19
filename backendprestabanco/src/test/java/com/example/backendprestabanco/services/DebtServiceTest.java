package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.DebtEntity;
import com.example.backendprestabanco.repositories.DebtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class DebtServiceTest {

    @MockBean
    private DebtRepository debtRepository;

    @Autowired
    private DebtService debtService;

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
}
