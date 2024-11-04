package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.CreditRequestEntity;
import com.example.backendprestabanco.repositories.CreditRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class CreditRequestServiceTest {

    @InjectMocks
    private CreditRequestService creditRequestService;

    @MockBean
    private CreditRequestRepository creditRequestRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetCreditRequests_thenReturnRequestList() {
        ArrayList<CreditRequestEntity> requests = new ArrayList<>();
        requests.add(new CreditRequestEntity());

        given(creditRequestRepository.findAll()).willReturn(requests);

        ArrayList<CreditRequestEntity> result = creditRequestService.getCreditRequests();

        assertThat(result).hasSize(1);
    }

    @Test
    void whenSaveCreditRequest_thenReturnSavedRequest() {
        CreditRequestEntity request = new CreditRequestEntity();
        given(creditRequestRepository.save(request)).willReturn(request);

        CreditRequestEntity result = creditRequestService.saveCreditRequest(request);

        assertThat(result).isNotNull();
    }

    @Test
    void whenGetCreditRequestByRut_thenReturnRequest() {
        String rut = "12345678-9";
        CreditRequestEntity request = new CreditRequestEntity();
        request.setRut(rut);

        given(creditRequestRepository.findByRut(rut)).willReturn(request);

        CreditRequestEntity result = creditRequestService.getCreditRequestByRut(rut);

        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo(rut);
    }

    @Test
    void whenUpdateCreditRequest_thenReturnUpdatedRequest() {
        CreditRequestEntity request = new CreditRequestEntity();
        request.setRut("12345678-9");
        request.setRequestedAmount(50000.0);

        given(creditRequestRepository.save(request)).willReturn(request);

        CreditRequestEntity result = creditRequestService.updateCreditRequest(request);

        assertThat(result).isNotNull();
        assertThat(result.getRequestedAmount()).isEqualTo(50000.0);
    }

    @Test
    void whenDeleteCreditRequest_thenReturnTrue() throws Exception {
        String rut = "12345678-9";
        doNothing().when(creditRequestRepository).deleteByRut(rut);

        boolean result = creditRequestService.deleteCreditRequest(rut);

        assertThat(result).isTrue();
        verify(creditRequestRepository, times(1)).deleteByRut(rut);
    }

    @Test
    void whenDeleteCreditRequest_thenThrowExceptionIfDeletionFails() {
        String rut = "12345678-9";
        doThrow(new RuntimeException("Error al eliminar")).when(creditRequestRepository).deleteByRut(rut);

        assertThatThrownBy(() -> creditRequestService.deleteCreditRequest(rut))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Error al eliminar");

        verify(creditRequestRepository, times(1)).deleteByRut(rut);
    }
}
