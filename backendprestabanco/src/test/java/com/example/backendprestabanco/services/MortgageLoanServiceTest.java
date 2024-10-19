package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.MortgageLoanEntity;
import com.example.backendprestabanco.repositories.MortgageLoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class MortgageLoanServiceTest {

    @MockBean
    private MortgageLoanRepository mortgageLoanRepository;

    @Autowired
    private MortgageLoanService mortgageLoanService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetMortgageLoans_thenReturnLoanList() {
        ArrayList<MortgageLoanEntity> loans = new ArrayList<>();
        loans.add(new MortgageLoanEntity());

        given(mortgageLoanRepository.findAll()).willReturn(loans);

        ArrayList<MortgageLoanEntity> result = mortgageLoanService.getMortgageLoans();

        assertThat(result).hasSize(1);
    }

    @Test
    void whenSaveMortgageLoan_thenReturnSavedLoan() {
        MortgageLoanEntity loan = new MortgageLoanEntity();
        given(mortgageLoanRepository.save(loan)).willReturn(loan);

        MortgageLoanEntity result = mortgageLoanService.saveMortgageLoan(loan);

        assertThat(result).isNotNull();
    }

    @Test
    void whenDeleteMortgageLoan_thenReturnTrue() throws Exception {
        String rut = "12345678-9";
        doNothing().when(mortgageLoanRepository).deleteByRut(rut);

        boolean result = mortgageLoanService.deleteMortgageLoan(rut);

        assertThat(result).isTrue();
        verify(mortgageLoanRepository, times(1)).deleteByRut(rut);
    }
}
