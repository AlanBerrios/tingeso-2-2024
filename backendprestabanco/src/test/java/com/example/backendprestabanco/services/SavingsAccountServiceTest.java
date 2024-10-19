package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.SavingsAccountEntity;
import com.example.backendprestabanco.repositories.SavingsAccountRepository;
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
class SavingsAccountServiceTest {

    @MockBean
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private SavingsAccountService savingsAccountService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetAllSavingsAccounts_thenReturnAccountList() {
        List<SavingsAccountEntity> accounts = List.of(new SavingsAccountEntity());
        given(savingsAccountRepository.findAll()).willReturn(accounts);

        List<SavingsAccountEntity> result = savingsAccountService.getAllSavingsAccounts();

        assertThat(result).hasSize(1);
    }

    @Test
    void whenSaveSavingsAccount_thenReturnSavedAccount() {
        SavingsAccountEntity account = new SavingsAccountEntity();
        given(savingsAccountRepository.save(account)).willReturn(account);

        SavingsAccountEntity result = savingsAccountService.saveSavingsAccount(account);

        assertThat(result).isNotNull();
    }

    @Test
    void whenDeleteSavingsAccount_thenReturnTrue() {
        Long id = 1L;
        given(savingsAccountRepository.existsById(id)).willReturn(true);

        boolean result = savingsAccountService.deleteSavingsAccount(id);

        assertThat(result).isTrue();
    }
}
