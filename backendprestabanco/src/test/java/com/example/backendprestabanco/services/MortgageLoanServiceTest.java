package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.MortgageLoanEntity;
import com.example.backendprestabanco.repositories.MortgageLoanRepository;
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
class MortgageLoanServiceTest {

    @InjectMocks
    private MortgageLoanService mortgageLoanService;

    @MockBean
    private MortgageLoanRepository mortgageLoanRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
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

    @Test
    void whenGetMortgageLoanById_thenReturnLoan() {
        MortgageLoanEntity loan = new MortgageLoanEntity();
        loan.setId(1L);
        given(mortgageLoanRepository.findById(1L)).willReturn(Optional.of(loan));

        MortgageLoanEntity result = mortgageLoanService.getMortgageLoanById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void whenGetMortgageLoanById_thenThrowExceptionIfNotFound() {
        given(mortgageLoanRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> mortgageLoanService.getMortgageLoanById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mortgage loan not found");
    }

    @Test
    void whenGetMortgageLoansByRut_thenReturnLoanList() {
        String rut = "12345678-9";
        List<MortgageLoanEntity> loans = List.of(new MortgageLoanEntity());
        given(mortgageLoanRepository.findAllByRut(rut)).willReturn(loans);

        List<MortgageLoanEntity> result = mortgageLoanService.getMortgageLoansByRut(rut);

        assertThat(result).isNotNull().hasSize(1);
    }

    @Test
    void whenGetMortgageLoanByRut_thenReturnLoan() {
        String rut = "12345678-9";
        MortgageLoanEntity loan = new MortgageLoanEntity();
        loan.setRut(rut);
        given(mortgageLoanRepository.findByRut(rut)).willReturn(loan);

        MortgageLoanEntity result = mortgageLoanService.getMortgageLoanByRut(rut);

        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo(rut);
    }

    @Test
    void whenUpdateMortgageLoanStatus_thenVerifyStatusUpdated() {
        Long loanId = 1L;
        String status = "Approved";
        doNothing().when(mortgageLoanRepository).updateMortgageLoanStatus(loanId, status);

        mortgageLoanService.updateMortgageLoanStatus(loanId, status);

        verify(mortgageLoanRepository, times(1)).updateMortgageLoanStatus(loanId, status);
    }
}
