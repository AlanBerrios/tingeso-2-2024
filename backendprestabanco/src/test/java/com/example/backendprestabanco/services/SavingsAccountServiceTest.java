package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.SavingsAccountEntity;
import com.example.backendprestabanco.repositories.SavingsAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class SavingsAccountServiceTest {

    @InjectMocks
    private SavingsAccountService savingsAccountService;

    @MockBean
    private SavingsAccountRepository savingsAccountRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetAllSavingsAccounts_thenReturnAccountList() {
        List<SavingsAccountEntity> accounts = List.of(new SavingsAccountEntity());
        given(savingsAccountRepository.findAll()).willReturn(accounts);

        List<SavingsAccountEntity> result = savingsAccountService.getAllSavingsAccounts();

        assertThat(result).isNotNull().hasSize(1);
    }

    @Test
    void whenGetSavingsAccountByRut_thenReturnAccount() {
        String rut = "12345678-9";
        SavingsAccountEntity account = new SavingsAccountEntity();
        account.setRut(rut);

        given(savingsAccountRepository.findByRut(rut)).willReturn(account);

        SavingsAccountEntity result = savingsAccountService.getSavingsAccountByRut(rut);

        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo(rut);
    }

    @Test
    void whenSaveSavingsAccount_thenReturnSavedAccount() {
        SavingsAccountEntity account = new SavingsAccountEntity();
        given(savingsAccountRepository.save(account)).willReturn(account);

        SavingsAccountEntity result = savingsAccountService.saveSavingsAccount(account);

        assertThat(result).isNotNull();
    }

    @Test
    void whenUpdateSavingsAccount_thenReturnUpdatedAccount() {
        SavingsAccountEntity account = new SavingsAccountEntity();
        account.setAccountId(1L);
        given(savingsAccountRepository.save(account)).willReturn(account);

        SavingsAccountEntity result = savingsAccountService.updateSavingsAccount(account);

        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(1L);
    }

    @Test
    void whenDeleteSavingsAccount_thenReturnTrue() {
        Long id = 1L;
        doNothing().when(savingsAccountRepository).deleteById(id);

        boolean result = savingsAccountService.deleteSavingsAccount(id);

        assertThat(result).isTrue();
        verify(savingsAccountRepository, times(1)).deleteById(id);
    }

    @Test
    void whenDeleteSavingsAccount_thenReturnFalseIfExceptionThrown() {
        Long id = 2L;
        doThrow(new RuntimeException("Error deleting account")).when(savingsAccountRepository).deleteById(id);

        boolean result = savingsAccountService.deleteSavingsAccount(id);

        assertThat(result).isFalse();
        verify(savingsAccountRepository, times(1)).deleteById(id);
    }
}
